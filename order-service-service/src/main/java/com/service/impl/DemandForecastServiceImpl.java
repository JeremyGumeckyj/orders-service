package com.service.impl;

import com.repository.ProductRepository;
import com.service.util.exception.IllegalArgumentException;
import com.service.util.exception.NotFoundException;
import dto.DemandForecast;
import dto.OrderItem;
import org.apache.commons.math3.fitting.leastsquares.*;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Pair;
import com.repository.DemandForecastRepository;
import com.repository.OrderItemRepository;
import com.service.DemandForecastService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DemandForecastServiceImpl implements DemandForecastService {

    private final DemandForecastRepository demandForecastRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;


    @Autowired
    public DemandForecastServiceImpl(DemandForecastRepository demandForecastRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository) {
        this.demandForecastRepository = demandForecastRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<DemandForecast> getDemandForecastsByProductId(UUID productId) {
        return getDemandForecastByProductId(productId);
    }

    @Override
    public DemandForecast createDemandForecast(UUID productId, DemandForecast demandForecast) {
        demandForecast.setId(UUID.randomUUID());
        demandForecast.setProductId(productId);
        double predictedDemand = calculateDemandForecast(productId);
        demandForecast.setPredictedDemand(predictedDemand);
        return demandForecastRepository.save(demandForecast);
    }

    @Override
    public DemandForecast updateDemandForecast(UUID productId, UUID forecastId, DemandForecast demandForecast) {
        // Перевіряємо, чи існує прогноз з вказаним ідентифікатором та чи відповідає productId
        DemandForecast existingForecast = demandForecastRepository.findById(forecastId)
                .orElseThrow(() -> new NotFoundException("Demand forecast not found"));
        if (!existingForecast.getProductId().equals(productId)) {
            throw new IllegalArgumentException("Demand forecast does not belong to the specified product");
        }
        // Оновлюємо дані прогнозу
        demandForecast.setId(forecastId);
        demandForecast.setProductId(productId);
        double predictedDemand = calculateDemandForecast(productId);
        demandForecast.setPredictedDemand(predictedDemand);
        return demandForecastRepository.save(demandForecast);
    }

    @Override
    public void deleteDemandForecastAndValidateByProductId(UUID productId, UUID forecastId) {
        // Перевіряємо, чи існує прогноз з вказаним ідентифікатором та чи відповідає productId
        DemandForecast existingForecast = demandForecastRepository.findById(forecastId)
                .orElseThrow(() -> new NotFoundException("Demand forecast not found"));
        if (!existingForecast.getProductId().equals(productId)) {
            throw new IllegalArgumentException("Demand forecast does not belong to the specified product");
        }
        // Видаляємо прогноз
        demandForecastRepository.deleteById(forecastId);
    }

    @Override
    public void deleteDemandForecast(UUID id) {
        validateIfDemandForecastExists(id);
        demandForecastRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        demandForecastRepository.deleteAll();
    }

    public void validateIfDemandForecastExists(UUID id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("Id in OrderItem entity can not be null");
        }

        Optional<DemandForecast> demandForecastFromDB = demandForecastRepository.findById(id);
        demandForecastFromDB
                .orElseThrow(() -> new NotFoundException("OrderItem not found"));
    }

    private List<OrderItem> getOrderItemsByProductId(UUID productId) {
        //return orderItemRepository.findByProductId(productId);
        return orderItemRepository.findAll().stream().filter(orderItem -> orderItem.getProductId().equals(productId)).collect(Collectors.toList());
    }

    private List<DemandForecast> getDemandForecastByProductId(UUID productId) {
        return demandForecastRepository.findAll().stream().filter(demandForecast -> demandForecast.getProductId().equals(productId)).collect(Collectors.toList());
    }

    private double calculateDemandForecast(UUID productId) {
        List<OrderItem> orderItems = getOrderItemsByProductId(productId);

        // Формування даних для лінійної регресії
        RealMatrix xMatrix = generateXMatrix(orderItems);
        RealVector yVector = generateYVector(orderItems);

        // Побудова моделі лінійної регресії
        MultivariateJacobianFunction model = new DemandRegressionModel(xMatrix, yVector);

        // Використання методу найменших квадратів для оптимізації
        LeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();

        LeastSquaresProblem problem = new LeastSquaresBuilder()
                .model(model) // Передаємо об'єкт моделі
                .target(yVector.toArray()) // Передаємо вектор відгуку у вигляді масиву
                .start(new double[]{0, 0}) // Початкова точка для пошуку оптимуму
                .lazyEvaluation(false) // Вимикаємо ліниве обчислення
                .maxEvaluations(1000) // Максимальна кількість оцінок функції
                .maxIterations(1000) // Максимальна кількість ітерацій
                .build(); // Побудова об'єкта LeastSquaresProblem

        LeastSquaresOptimizer.Optimum optimum = optimizer.optimize(problem);

        // Повертаємо прогнозоване значення
        double[] params = optimum.getPoint().toArray();
        double intercept = params[0];
        double slope = params[1];

        //
        double MC = monthCalculation(productId);
        System.out.println("Month calculation = " + MC);

        double TMC = threeMonthCalculation(productId);
        System.out.println("Three month calculation = " + TMC);

        return (double) (intercept + slope * xMatrix.getColumnVector(0).getEntry(xMatrix.getRowDimension() - 1));
    }

    // Метод для генерації матриці ознак (X)
    private RealMatrix generateXMatrix(List<OrderItem> orderItems) {
        int n = orderItems.size(); // Кількість спостережень
        RealMatrix xMatrix = MatrixUtils.createRealMatrix(n, 2); // Створюємо матрицю розміром n x 2

        // Заповнюємо матрицю ознак
        for (int i = 0; i < n; i++) {
            // Перший стовпчик - номери спостережень
            xMatrix.setEntry(i, 0, i + 1);
            // Другий стовпчик - відстань від початку навчання (в днях, можна змінити на інші одиниці)
            xMatrix.setEntry(i, 1, Duration.between(orderItems.get(0).getOrderDate().atStartOfDay(), orderItems.get(i).getOrderDate().atStartOfDay()).toDays());
        }

        return xMatrix;
    }

    // Метод для генерації вектора відгуку (Y)
    private RealVector generateYVector(List<OrderItem> orderItems) {
        int n = orderItems.size(); // Кількість спостережень
        RealVector yVector = new ArrayRealVector(n); // Створюємо вектор розміром n

        // Заповнюємо вектор відгуку
        for (int i = 0; i < n; i++) {
            yVector.setEntry(i, orderItems.get(i).getQuantity()); // Кількість товару в спостереженні
        }

        return yVector;
    }

    private double monthCalculation(UUID productId) {
        List<OrderItem> orderItemsList = getOrderItemsByProductId(productId)
                .stream()
                .filter(orderItem -> orderItem.getOrderDate().getMonthValue() == 5)
                .collect(Collectors.toList());

        int n = orderItemsList.size();

        double sum = orderItemsList.stream()
                .mapToDouble(OrderItem::getQuantity)
                .sum();

        return sum / n;
    }

    private double threeMonthCalculation(UUID productId) {
        List<Integer> targetMonths = Arrays.asList(3, 4, 5); // Список місяців, які вам потрібні

        List<OrderItem> orderItemsList = getOrderItemsByProductId(productId)
                .stream()
                .filter(orderItem -> targetMonths.contains(orderItem.getOrderDate().getMonthValue()))
                .collect(Collectors.toList());

        int n = orderItemsList.size();

        double sum = orderItemsList.stream()
                .mapToDouble(OrderItem::getQuantity)
                .sum();

        return sum / n;
    }

    // Модель лінійної регресії
    private record DemandRegressionModel(RealMatrix xMatrix,
                                         RealVector yVector) implements MultivariateJacobianFunction {

        @Override
        public Pair<RealVector, RealMatrix> value(RealVector point) {
            // Вираховуємо значення для функції та її похідних
            RealVector value = xMatrix.operate(point).subtract(yVector);
            RealMatrix jacobian = xMatrix;
            return new Pair<>(value, jacobian);
        }
    }
}
