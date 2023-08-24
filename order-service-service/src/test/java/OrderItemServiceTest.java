import com.service.OrderItemService;
import com.service.impl.OrderItemServiceImpl;
import com.repository.OrderItemRepository;
import com.repository.ProductRepository;
import com.repository.OrderRepository;
import com.service.util.exception.IllegalArgumentException;
import dto.OrderItem;
import dto.Product;
import dto.Order;
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
        OrderItemService.class,
        OrderItemServiceImpl.class,
        OrderItemRepository.class,
        OrderItem.class,
        ProductRepository.class,
        Product.class,
        OrderRepository.class,
        Order.class})
@ExtendWith(MockitoExtension.class)
class OrderItemServiceTest {

    @MockBean
    private OrderItemRepository orderItemRepository;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private OrderRepository orderRepository;

    private OrderItemService orderItemService;

    @BeforeEach
    public void setup() {
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

        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> orderItemService.getById(orderItemId));
        assertEquals("Order item not found", notFoundException.getMessage());
    }

    @Test
    public void shouldAddOrderItemInvalidProduct() {
        UUID productId = UUID.randomUUID();
        OrderItem orderItem = new OrderItem(UUID.randomUUID(), 1, productId, UUID.randomUUID());

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> orderItemService.addOrderItem(orderItem));
        assertEquals("Invalid product", illegalArgumentException.getMessage());
    }

    @Test
    public void shouldAddOrderItemInvalidOrder() {
        UUID orderId = UUID.randomUUID();
        when(productRepository.findById(any())).thenReturn(Optional.of(new Product()));
        OrderItem orderItem = new OrderItem(UUID.randomUUID(), 1, UUID.randomUUID(), null);

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> orderItemService.addOrderItem(orderItem));
        assertEquals("Invalid order", illegalArgumentException.getMessage());
    }

    @Test
    public void shouldAddOrderItemProductNotAvailable() {
        UUID productId = UUID.randomUUID();
        when(productRepository.findById(any())).thenReturn(Optional.of(new Product(productId, "product1", 125.00, false)));
        when(orderRepository.findById(any())).thenReturn(Optional.of(new Order()));
        OrderItem orderItem = new OrderItem(UUID.randomUUID(), 1, UUID.randomUUID(), null);

        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> orderItemService.addOrderItem(orderItem));
        assertEquals("Product is not in stock", notFoundException.getMessage());
    }

    @Test
    public void shouldValidateIfOrderItemExistsWithNullId() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> orderItemService.validateIfOrderItemExists(null));
        assertEquals("Id in OrderItem entity can not be null", illegalArgumentException.getMessage());
    }

    @Test
    public void shouldValidateIfOrderItemExistsWithNonexistentId() {
        UUID orderId = UUID.randomUUID();

        when(orderItemRepository.findById(orderId)).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> orderItemService.validateIfOrderItemExists(orderId));
        assertEquals("OrderItem not found", notFoundException.getMessage());
    }
}
