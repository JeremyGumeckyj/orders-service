import com.service.ProductService;
import com.service.impl.ProductServiceImpl;
import com.repository.ProductRepository;
import dto.Product;
import com.service.util.exception.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {ProductService.class, ProductServiceImpl.class, Product.class, ProductRepository.class})
public class ProductServiceTest {

    @MockBean
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    public void testGetAllProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(UUID.randomUUID(), "product1", 9.99, true));
        products.add(new Product(UUID.randomUUID(), "product2", 99.99, false));

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAll();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetById() {
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "product1", 100.00, true);

        when(productRepository.findById(productId)).thenReturn(java.util.Optional.of(product));

        Product result = productService.getById(productId);

        assertEquals(productId, result.getId());
        assertEquals("product1", result.getName());
    }

    @Test
    public void testGetProductByIdNotFound() {
        UUID productId = UUID.randomUUID();

        when(productRepository.findById(productId)).thenReturn(java.util.Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.getById(productId));
    }

    @Test
    public void testAddProduct() {
        Product product = new Product(UUID.randomUUID(), "newproduct", 15.25, true);

        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.addProduct(product);

        assertEquals("newproduct", result.getName());
    }

    @Test
    public void testUpdateProduct() {
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "product1", 999.99, true);

        when(productRepository.findById(productId)).thenReturn(java.util.Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.updateProduct(product);

        assertEquals("product1", result.getName());
    }

    @Test
    public void testUpdateProductNotFound() {
        Product product = new Product(UUID.randomUUID(), "updatedProduct", 25.0, false);

        when(productRepository.findById(product.getId())).thenReturn(java.util.Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.updateProduct(product));
    }

    @Test
    public void testDeleteById() {
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "product1", 115.15, false);

        when(productRepository.findById(productId)).thenReturn(java.util.Optional.of(product));
        doNothing().when(productRepository).deleteById(productId);

        productService.deleteById(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }
}