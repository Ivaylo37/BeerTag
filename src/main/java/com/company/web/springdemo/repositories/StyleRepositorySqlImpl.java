package com.company.web.springdemo.repositories;

import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.Style;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@PropertySource("classpath:application.properties")
public class StyleRepositorySqlImpl implements StyleRepository {

    private final String dbUrl, dbUsername, dbPassword;

    @Autowired
    public StyleRepositorySqlImpl(Environment environment) {
        dbUrl = environment.getProperty("database.url");
        dbUsername = environment.getProperty("database.username");
        dbPassword = environment.getProperty("database.password");
    }

    @Override
    public List<Style> get() {
        String query = "select * from styles;";
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement statement = connection.prepareStatement(query);
        ) {
            try (
                    ResultSet resultSet = statement.executeQuery()
            ) {
                return getStyles(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Style get(int id) {
        String query = "select * from styles where id = ?";
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, id);
            try (
                    ResultSet resultSet = statement.executeQuery()
            ) {
                List<Style> result = getStyles(resultSet);
                if (result.isEmpty()) {
                    throw new EntityNotFoundException("Style", id);
                }
                return result.get(0);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private List<Style> getStyles(ResultSet resultSet) {
        List<Style> styles = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int styleId = resultSet.getInt("id");
                String styleName = resultSet.getString("style_name");
                Style style = new Style(styleId, styleName);
                styles.add(style);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return styles;
    }

}
