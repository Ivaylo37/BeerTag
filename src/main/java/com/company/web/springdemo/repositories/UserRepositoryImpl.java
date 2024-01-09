package com.company.web.springdemo.repositories;

import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class UserRepositoryImpl implements UserRepository{

    private final List<User> users;

    public UserRepositoryImpl() {
        users = new ArrayList<>();
        User admin = new User("spas", "asafsdafds", "spas", "spasov", "test");
        admin.setAdmin(true);
        users.add(admin);
        users.add(new User("pesho", "asafsdafds", "spas", "spasov", "test"));
        users.add(new User("gosho", "asafsdafds", "spas", "spasov", "test"));
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users);
    }

    @Override
    public User getById(int id) {
        return getAll().stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("User", id));
    }

    @Override
    public User getByUsername(String username) {
        return getAll().stream()
                .filter(user -> user.getUserName().equals(username))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("User", "username", username));
    }

    @Override
    public void create(User user) {
        int nextId = users.size() + 1;
        user.setId(nextId);
        user.setAdmin(false);
        users.add(user);
    }

    @Override
    public void delete(int id) {
        User userToDelete = getById(id);
        users.remove(userToDelete);
    }


}
