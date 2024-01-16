package com.company.web.springdemo.services;

import com.company.web.springdemo.exceptions.EntityDuplicateException;
import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.exceptions.UnauthorizedOperationException;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.FilterOptions;
import com.company.web.springdemo.models.User;
import com.company.web.springdemo.repositories.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository repository;

    @Autowired
    public BeerServiceImpl(BeerRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Beer> get(FilterOptions filterOptions) {
        return repository.get(filterOptions);
    }

    @Override
    public Beer get(int id) {
        return repository.get(id);
    }

    @Override
    public void create(Beer beer, User user) {
        boolean duplicateExists = true;
        try {
            repository.get(beer.getName());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Beer", "name", beer.getName());
        }
        beer.setCreatedBy(user);
        repository.create(beer);
    }

    @Override
    public void update(Beer beer, User user) {
        if (!user.isAdmin() && !beer.getCreatedBy().equals(user)) {
            throw new UnauthorizedOperationException("Only admins or owners can modify the beer.");
        }
        boolean exists = false;
        Beer toUpdate = get(beer.getId());
        if (toUpdate.equals(null)) {
            throw new EntityNotFoundException("id", beer.getId());
        }

        repository.update(beer);
    }

    @Override
    public void delete(int id, User user) {
        if (!user.isAdmin() && !user.equals(get(id).getCreatedBy())) {
            throw new UnauthorizedOperationException("Only admins or owners can delete the beer.");
        }
        repository.delete(id);
    }

}
