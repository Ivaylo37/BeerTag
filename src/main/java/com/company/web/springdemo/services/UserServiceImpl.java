package com.company.web.springdemo.services;

import com.company.web.springdemo.exceptions.EntityDuplicateException;
import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.exceptions.UnauthorizedOperationException;
import com.company.web.springdemo.models.User;
import com.company.web.springdemo.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> getAll() {
        return repository.getAll();
    }

    @Override
    public User getById(int id) {
        return repository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return repository.getByUsername(username);
    }

    @Override
    public void createUser(User user) {
        boolean duplicateExists = true;
        try {
            repository.getByUsername(user.getUserName());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("User", "username", user.getUserName());
        }

        repository.create(user);
    }

    @Override
    public void deleteUser(int id, User user) {
        if(!user.isAdmin()) {
            throw new UnauthorizedOperationException("Only admins can delete the user.");
        }
        repository.delete(id);
    }
}
