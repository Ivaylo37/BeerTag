package com.company.web.springdemo.repositories;

import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.Style;
import com.company.web.springdemo.models.User;

import java.util.List;

public interface StyleRepository {

    List<Style> get();

    Style get(int id);

}