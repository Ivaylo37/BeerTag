package com.company.web.springdemo.repositories;

import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.Style;
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
public class BeerRepositorySqlImpl implements BeerRepository{

    private final String dbUrl, dbUsername, dbPassword;
    @Autowired
    public BeerRepositorySqlImpl(Environment environment) {
        dbUrl = environment.getProperty("database.url");
        dbUsername = environment.getProperty("database.username");
        dbPassword = environment.getProperty("database.password");
    }


    @Override
    public List<Beer> get(String name, Double minAbv, Double maxAbv, Integer styleId, String sortBy, String sortOrder) {
        try (
            Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        ) {
            String query = "SELECT * FROM beers " +
                    "join styles on styles.id = beers.beer_style " +
                    "join users on beers.beer_creator = users.id " +
                    "WHERE 1 + 1";
            if (name != null) {
                query += " AND beers.name = ?";
            }
            if (minAbv != null) {
                query += " AND beers.abv >= ?";
            }
            if (maxAbv != null) {
                query += " AND beers.abv <= ?";
            }
            if (styleId != null) {
                query += " AND beers.style_id = ?";
            }
            if (sortBy != null && sortOrder != null) {
                query += " ORDER BY " + sortBy + " " + sortOrder;
            }
            try (
                    PreparedStatement statement = connection.prepareStatement(query);
            ) {
                int parameterIndex = 1;
                if (name != null) {
                    statement.setString(parameterIndex++, name);
                }
                if (minAbv != null) {
                    statement.setDouble(parameterIndex++, minAbv);
                }
                if (maxAbv != null) {
                    statement.setDouble(parameterIndex++, maxAbv);
                }
                if (styleId != null) {
                    statement.setInt(parameterIndex++, styleId);
                }
                ResultSet resultSet = statement.executeQuery();
                return getBeers(resultSet);
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Beer get(int id) {
        String query = "select id, beer_name, beer_abv from beers where id = ?";
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement statement = connection.prepareStatement(query);
                ){
            statement.setInt(1, id);
            try (
                    ResultSet resultSet = statement.executeQuery()
            ){
                List<Beer> result = getBeers(resultSet);
                if (result.isEmpty()) {
                    throw new EntityNotFoundException("Beer", id);
                }
                return result.get(0);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Beer get(String name) {
        String query = "select id, beer_name, beer_abv from beers where beer_name = ?";
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement statement = connection.prepareStatement(query);
        ){
            statement.setString(1, name);
            try (
                    ResultSet resultSet = statement.executeQuery()
            ){
                List<Beer> result = getBeers(resultSet);
                if (result.isEmpty()) {
                    throw new EntityNotFoundException("Beer", "name", name);
                }
                return result.get(0);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void create(Beer beer) {
        String query = "INSERT INTO beers (beer_name, beer_abv, beer_style, beer_creator) " +
                "VALUES (?, ?, ?, ?)";
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, beer.getName());
            statement.setDouble(2, beer.getAbv());
            statement.setInt(3, beer.getStyle().getId());
            statement.setInt(4, beer.getCreatedBy().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(Beer beer) {
        String query = "update beers set beer_name = ?, beer_abv = ?," +
                " beer_style = ?, beer_creator = ? " +
                "where id = ?;";
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, beer.getName());
            statement.setDouble(2, beer.getAbv());
            statement.setInt(3, beer.getStyle().getId());
            statement.setInt(4, beer.getCreatedBy().getId());
            statement.setInt(5, beer.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String query = "delete from beers where id = ?;";
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, id);

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private List<Beer> getBeers(ResultSet resultSet) {
        List<Beer> beers = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int beerId = resultSet.getInt("id");
                String beerName = resultSet.getString("name");
                double beerAbv = resultSet.getDouble("abv");
                int styleId1 = resultSet.getInt("style_id");
                int user_id = resultSet.getInt("beer_creator");
                String style_name = resultSet.getString("style_name");
                String username = resultSet.getString("username");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                boolean isAdmin = resultSet.getBoolean("is_admin");
                Style style = new Style(styleId1, style_name);
                User creator = new User(user_id, password, username, firstName, lastName, email, isAdmin);
                Beer beer = new Beer(beerId, beerName, beerAbv, style, creator);
                beers.add(beer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return beers;
    }
}
