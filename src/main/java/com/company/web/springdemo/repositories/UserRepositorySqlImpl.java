package com.company.web.springdemo.repositories;


import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@PropertySource("classpath:application.properties")
public class UserRepositorySqlImpl implements UserRepository{


    private final String dbUrl, dbUsername, dbPassword;
    @Autowired
    public UserRepositorySqlImpl(Environment environment) {
        dbUrl = environment.getProperty("database.url");
        dbUsername = environment.getProperty("database.username");
        dbPassword = environment.getProperty("database.password");
    }


    @Override
    public List<User> getAll() {
        String query = "select * from users;";
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement statement = connection.prepareStatement(query);
        ){
            try (
                    ResultSet resultSet = statement.executeQuery()
            ){
                return getUsers(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public User getById(int id) {
        String query = "select * from users where id = ?";
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement statement = connection.prepareStatement(query);
        ){
            statement.setInt(1, id);
            try (
                    ResultSet resultSet = statement.executeQuery()
            ){
                List<User> result = getUsers(resultSet);
                if (result.isEmpty()) {
                    throw new EntityNotFoundException("User", id);
                }
                return result.get(0);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public User getByUsername(String username) {
        String query = "select * from users where username = ?";
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement statement = connection.prepareStatement(query);
        ){
            statement.setString(1, username);
            try (
                    ResultSet resultSet = statement.executeQuery()
            ){
                List<User> result = getUsers(resultSet);
                if (result.isEmpty()) {
                    throw new EntityNotFoundException("Username", "username", username);
                }
                return result.get(0);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void create(User user) {
        String query = "INSERT INTO users (username, first_name, last_name, password, email) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getEmail());
            statement.setBoolean(6, user.isAdmin());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String query = "delete from users where id = ?;";
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private List<User> getUsers(ResultSet resultSet) {
        List<User> users = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                boolean isAdmin = resultSet.getBoolean("is_admin");
                User user = new User(id, password, username, firstName, lastName, email, isAdmin);
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return users;
    }
}
