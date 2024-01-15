package com.company.web.springdemo.services;

import com.company.web.springdemo.exceptions.EntityDuplicateException;
import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.exceptions.UnauthorizedOperationException;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.User;
import com.company.web.springdemo.repositories.BeerRepository;
import com.company.web.springdemo.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final BeerRepository beerRepository;

    public UserServiceImpl(UserRepository repository, BeerRepository beerRepository) {
        this.userRepository = repository;
        this.beerRepository = beerRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public void createUser(User user) {
        boolean duplicateUserNameExists = true;
        boolean duplicatePasswordExists = false;
        try {
            userRepository.getByUsername(user.getUserName());
        } catch (EntityNotFoundException e) {
            duplicateUserNameExists = false;
            if (getAll().stream().anyMatch(user1 -> user1.getPassword().equals(user.getPassword()))) {
                duplicatePasswordExists = true;
            }
        }

        if (duplicateUserNameExists) {
            throw new EntityDuplicateException("User", "username", user.getUserName());
        }
        if (duplicatePasswordExists) {
            throw new EntityDuplicateException("User", "password", user.getPassword());
        }

        userRepository.create(user);
    }

    @Override
    public void deleteUser(int id, User user) {
        if(!user.isAdmin()) {
            throw new UnauthorizedOperationException("Only admins can delete the user.");
        }
        userRepository.delete(id);
    }

    @Override
    public List<Beer> addBeerToWishList(int userId, int beerId) {
        User user = userRepository.getById(userId);
        Beer beer = beerRepository.get(beerId);
        if (user.getWishList().add(beer)) {
            userRepository.update(user);
        }
        return new ArrayList<>(user.getWishList());
    }

    @Override
    public List<Beer> removeBeerFromWishList(int userId, int beerId) {
        User user = userRepository.getById(userId);
        Beer beer = beerRepository.get(beerId);
        if (user.getWishList().remove(beer)) {
            userRepository.update(user);
        }
        return new ArrayList<>(user.getWishList());
    }
}
