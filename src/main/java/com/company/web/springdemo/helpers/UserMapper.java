package com.company.web.springdemo.helpers;

import com.company.web.springdemo.models.User;
import com.company.web.springdemo.models.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserMapper() {
    }

    public User fromDto(UserDto userDto) {
        User user = new User();
        user.setUserName(userDto.getUserName());
        user.setPassword(userDto.getPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        return user;
    }


}
