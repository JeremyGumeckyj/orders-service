import com.service.UserService;
import com.service.impl.UserServiceImpl;
import com.repository.UserRepository;
import dto.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {UserService.class, UserServiceImpl.class, User.class, UserRepository.class})
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(UUID.randomUUID(), "user1", "user1@example.com"));
        users.add(new User(UUID.randomUUID(), "user2", "user2@example.com"));

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAll();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetUserById() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "user1", "user1@example.com");

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        User result = userService.getById(userId);

        assertEquals(userId, result.getId());
        assertEquals("user1", result.getUsername());
    }

    @Test
    public void testCreateUser() {
        User user = new User(null, "newuser", "newuser@example.com");

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertEquals("newuser", result.getUsername());
    }

    @Test
    public void testUpdateUser() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "user1", "user1@example.com");

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.updateUser(user);

        assertEquals("user1", result.getUsername());
    }

    @Test
    public void testDeleteUser() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "userToDelete", "userToDelete@example.com");

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteById(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}
