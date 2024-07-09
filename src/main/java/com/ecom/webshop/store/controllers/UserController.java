package com.ecom.webshop.store.controllers;

import com.ecom.webshop.store.dtos.ApiResponseMessage;
import com.ecom.webshop.store.dtos.ImageResponse;
import com.ecom.webshop.store.dtos.PageableResponse;
import com.ecom.webshop.store.dtos.UserDto;
import com.ecom.webshop.store.entities.User;
import com.ecom.webshop.store.services.FileService;
import com.ecom.webshop.store.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;
    private Logger logger= LoggerFactory.getLogger((UserController.class));

    //Create
    @PostMapping("/createuser")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto dtoUser = userService.createUser(userDto);
        return new ResponseEntity<>(dtoUser, HttpStatus.CREATED);

    }


    //Update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") String userId
            , @Valid @RequestBody UserDto userDto) {
        UserDto updatedDtoUser = userService.updateUser(userDto, userId);
        return new ResponseEntity<>(updatedDtoUser, HttpStatus.OK);

    }


    //Delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) throws IOException {

        userService.deleteUser(userId);

        ApiResponseMessage message = ApiResponseMessage
                .builder()
                .message("User is deleted successfully!!").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(message, HttpStatus.OK);

    }
    //Get User by Id
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    //Get all
    @GetMapping("/allusers")
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(@RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                                      @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
                                                                      @RequestParam(value="sortBy",defaultValue = "name",required = false)  String sortBy,
                                                                      @RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir) {
        return new ResponseEntity<>(userService.getAllUsers(pageNumber,pageSize,sortBy,sortDir), HttpStatus.OK);
    }

    //Get by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }

    //Search user
    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keywords) {
        return new ResponseEntity<>(userService.searchUser(keywords), HttpStatus.OK);
    }

    //uploading image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(
            @RequestParam("userImage") MultipartFile image,@PathVariable String userId
            ) throws IOException {
        String imageName=fileService.uploadImage(image,imageUploadPath);
        UserDto user=userService.getUserById(userId);
        user.setImageName(imageName);
        UserDto userDto=userService.updateUser(user,userId);
       ImageResponse imageResponse= ImageResponse.builder().imageName(imageName).message("Image is Uploaded Successfully").success(true).status(HttpStatus.CREATED).build();
       return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    //serving the image to the user
    @GetMapping("/image/{userId}")
    public void serverUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {

        //get user
        UserDto user=userService.getUserById(userId);
        logger.info("User image name :{}",user.getImageName());
        InputStream resource = fileService.getResource(imageUploadPath, user.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());

    }


}
