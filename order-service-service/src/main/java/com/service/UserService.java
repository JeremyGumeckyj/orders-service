package com.service;

import dto.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> getAll();

    User createUser(User user);

    User getById(UUID id);

    User updateUser(User user);

    void deleteById(UUID id);
}
