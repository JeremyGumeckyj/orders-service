import com.service.OrderService;
import com.service.impl.OrderServiceImpl;
import com.repository.OrderRepository;
import com.repository.UserRepository;
import com.service.util.exception.IllegalArgumentException;
import dto.Order;
import dto.User;
import com.service.util.exception.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {
        OrderService.class,
        OrderServiceImpl.class,
        Order.class,
        OrderRepository.class,
        UserRepository.class,
        User.class})
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private UserRepository userRepository;

    private OrderService orderService;

    @BeforeEach
    public void setup() {
        orderService = new OrderServiceImpl(orderRepository, userRepository);
    }

    @Test
    public void testGetAll() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(UUID.randomUUID(), "order1", UUID.randomUUID()));
        orders.add(new Order(UUID.randomUUID(), "order2", UUID.randomUUID()));

        when(orderRepository.findAll()).thenReturn(orders);

        List <Order> result = orderService.getAll();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetById() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order(orderId, "order1", UUID.randomUUID());

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));

        Order result = orderService.getById(orderId);

        assertEquals(orderId, result.getId());
        assertEquals("order1", result.getName());
    }

    @Test
    public void testAddOrder() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "user1", "user1@example.com");
        Order order = new Order(UUID.randomUUID(), "newOrder", userId);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.createOrder(order);

        assertEquals("newOrder", result.getName());
    }

    @Test
    public void testUpdateOrder() {
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Order order = new Order(orderId, "order1", userId);

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.updateOrder(order);

        assertEquals("order1", result.getName());
    }

    @Test
    public void testDeleteOrder() {
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Order order = new Order(orderId, "order1", userId);

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));
        doNothing().when(orderRepository).deleteById(orderId);

        orderService.deleteById(orderId);

        verify(orderRepository, times(1)).deleteById(orderId);
    }
    @Test
    public void shouldGetOrderByIdNotFound() {
        UUID orderId = UUID.randomUUID();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> orderService.getById(orderId));
        assertEquals("Order not found", notFoundException.getMessage());
    }

    @Test
    public void shouldCreateOrderInvalidUser() {
        UUID userId = UUID.randomUUID();
        Order order = new Order(UUID.randomUUID(), "newOrder", userId);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(order));
        assertEquals("This user is not registered", illegalArgumentException.getMessage());
    }

    @Test
    public void shouldValidateIfOrderExistsWithNullId() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> orderService.validateIfOrderExists(null));
        assertEquals("Id in Order entity can not be null", illegalArgumentException.getMessage());
    }

    @Test
    public void shouldValidateIfOrderItemExistsWithNonexistentId() {
        UUID orderId = UUID.randomUUID();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> orderService.validateIfOrderExists(orderId));
        assertEquals("Order not found", notFoundException.getMessage());
    }
}
