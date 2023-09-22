package com.service.impl;

import com.repository.UserRepository;
import com.service.UserService;
import com.service.util.exception.IllegalArgumentException;
import com.service.util.exception.NotFoundException;
import dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public User createUser(User user) {
        user.setId(UUID.randomUUID());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        validateIfUserExists(user.getId());
        return userRepository.save(user);
    }

    @Override
    public void deleteById(UUID id) {
        validateIfUserExists(id);
        userRepository.deleteById(id);
    }

    public void validateIfUserExists(UUID id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("Id in User entity can not be null");
        }

        Optional<User> userFromDB = userRepository.findById(id);
        userFromDB
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
