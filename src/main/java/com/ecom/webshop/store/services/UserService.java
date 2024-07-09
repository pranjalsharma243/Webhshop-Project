package com.ecom.webshop.store.services;

import com.ecom.webshop.store.dtos.PageableResponse;
import com.ecom.webshop.store.dtos.UserDto;
import com.ecom.webshop.store.entities.User;

import java.io.IOException;
import java.util.List;

public interface UserService {

    //create

    UserDto createUser(UserDto userDto);

    //update
    UserDto updateUser(UserDto userDto, String userId);

    //delete
    void deleteUser(String userId) throws IOException;

    //get all users
    PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get single user by id
    UserDto getUserById(String userId);

    //get single user by email
    UserDto getUserByEmail(String email);

    //search user
    List<UserDto> searchUser(String keyword);

    //misc features

}
