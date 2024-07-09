package com.ecom.webshop.store.services.impl;

import com.ecom.webshop.store.dtos.PageableResponse;
import com.ecom.webshop.store.dtos.UserDto;
import com.ecom.webshop.store.entities.User;
import com.ecom.webshop.store.exceptions.ResourceNotFoundException;
import com.ecom.webshop.store.helper.Helper;
import com.ecom.webshop.store.repositories.UserRepository;
import com.ecom.webshop.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;
    @Value("${user.profile.image.path}")
    private String imagePath;

    private Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDto createUser(UserDto userDto) {
        //New unique id
        String userId= UUID.randomUUID().toString();
        userDto.setUserId(userId);
        User user=dtoToEntity(userDto);
        User savedUser=userRepository.save(user);
        UserDto newDto=entityToDto(savedUser);
        return userDto;
    }



    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found with given id!!"));
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());

        //save data to repo
        User updatedUser=userRepository.save(user);

        UserDto updatedDto=entityToDto(updatedUser);
        return updatedDto;
    }

    @Override
    public void deleteUser(String userId)  {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found with given id!!"));
        //delete image
         String fullPath=imagePath+user.getImageName();
        System.out.println(fullPath);
         try {
             Path path = Paths.get(fullPath);
             Files.delete(path);
         }
         catch(NoSuchFileException ex){
             logger.info("User Image not found in folder");
             ex.printStackTrace();
         }
         catch (IOException e){
             e.printStackTrace();
         }

        //delete user
        userRepository.delete(user);

    }

    @Override
    public PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase(("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending()));
        //Paging starts at 0 index
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<User> page=userRepository.findAll(pageable);
        PageableResponse<UserDto> response = Helper.getPageableReponse(page, UserDto.class);

        return response;

    }

    @Override
    public UserDto getUserById(String userId) {
       User user= userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found with this userId!!"));
        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user=userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not found with given email id "));

        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users=userRepository.findByNameContaining(keyword);
        List<UserDto> dtosUsers= users.stream().map(user-> entityToDto(user)).collect(Collectors.toList());
        return dtosUsers;
    }
    private User dtoToEntity(UserDto userDto) {
//        User user=User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName()).build();

        return mapper.map(userDto,User.class);
    }
    private UserDto entityToDto(User savedUser) {
//        UserDto userDto=UserDto.builder()
//                .userId(savedUser.getUserId())
//                .name(savedUser.getName())
//                .email(savedUser.getPassword())
//                .about(savedUser.getAbout())
//                .gender(savedUser.getGender())
//                .imageName(savedUser.getImageName())
//                .build();
        return mapper.map(savedUser,UserDto.class);
    }


}
