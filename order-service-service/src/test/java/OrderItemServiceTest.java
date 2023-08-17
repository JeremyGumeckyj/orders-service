import com.service.OrderItemService;
import com.service.impl.OrderItemServiceImpl;
import com.repository.OrderItemRepository;
import com.repository.ProductRepository;
import com.repository.OrderRepository;
import dto.OrderItem;
import dto.Product;
import dto.Order;
import com.service.util.exception.NotFoundException;

import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {OrderItemService.class, OrderItemServiceImpl.class, OrderItemRepository.class, OrderItem.class, ProductRepository.class, Product.class, OrderRepository.class, Order.class})
public class OrderItemServiceTest {

    @MockBean
    private OrderItemRepository orderItemRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private OrderRepository orderRepository;

    private OrderItemService orderItemService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        orderItemService = new OrderItemServiceImpl(orderItemRepository, productRepository, orderRepository);
    }

    @Test
    public void testGetAll() {
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(new OrderItem(UUID.randomUUID(), 2, UUID.randomUUID(), UUID.randomUUID()));
        orderItems.add(new OrderItem(UUID.randomUUID(), 1, UUID.randomUUID(), UUID.randomUUID()));

        when(orderItemRepository.findAll()).thenReturn(orderItems);

        List<OrderItem> result = orderItemService.getAll();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetById() {
        UUID orderItemId = UUID.randomUUID();
        OrderItem orderItem = new OrderItem(orderItemId, 1, UUID.randomUUID(), UUID.randomUUID());

        when(orderItemRepository.findById(orderItemId)).thenReturn(java.util.Optional.of(orderItem));

        OrderItem result = orderItemService.getById(orderItemId);

        assertEquals(orderItemId, result.getId());
        assertEquals(1, result.getQuantity());
    }

    @Test
    public void testAddOrderItem() {
        UUID productId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        Product product = new Product(productId, "product1", 20.00, true);
        Order order = new Order(orderId, "order1", UUID.randomUUID());
        OrderItem orderItem = new OrderItem(UUID.randomUUID(), 1, productId, orderId);

        when(productRepository.findById(productId)).thenReturn(java.util.Optional.of(product));
        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));
        when(orderItemRepository.save(orderItem)).thenReturn(orderItem);

        OrderItem result = orderItemService.addOrderItem(orderItem);

        assertEquals(1,result.getQuantity());
    }

    @Test
    public void testUpdateOrderItem() {
        UUID productId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        UUID orderItemId = UUID.randomUUID();
        OrderItem orderItem = new OrderItem(orderItemId, 1, productId, orderId);

        when(orderItemRepository.findById(orderItemId)).thenReturn(java.util.Optional.of(orderItem));
        when(orderItemRepository.save(orderItem)).thenReturn(orderItem);

        OrderItem result = orderItemService.updateOrderItem(orderItem);

        assertEquals(orderItemId, result.getId());
    }

    @Test
    public void testDeleteOrderItem() {
        UUID orderItemId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        OrderItem orderItem = new OrderItem(orderItemId, 1, productId, orderId);

        when(orderItemRepository.findById(orderItemId)).thenReturn(java.util.Optional.of(orderItem));
        doNothing().when(orderItemRepository).deleteById(orderItemId);

        orderItemService.deleteById(orderItemId);

        verify(orderItemRepository, times(1)).deleteById(orderItemId);
    }

    @Test
    public void testGetOrderByIdNotFound() {
        UUID orderItemId = UUID.randomUUID();

        when(orderItemRepository.findById(orderItemId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderItemService.getById(orderItemId));
    }

    @Test
    public void testAddOrderItemInvalidProduct() {
        UUID productId = UUID.randomUUID();
        OrderItem orderItem = new OrderItem(UUID.randomUUID(), 1, productId, UUID.randomUUID());

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> orderItemService.addOrderItem(orderItem));
    }

    @Test
    public void testAddOrderItemInvalidOrder() {
        UUID orderId = UUID.randomUUID();
        OrderItem orderItem = new OrderItem(UUID.randomUUID(), 1, UUID.randomUUID(), orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> orderItemService.addOrderItem(orderItem));
    }
}
