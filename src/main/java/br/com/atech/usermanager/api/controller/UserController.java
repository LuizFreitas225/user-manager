package br.com.atech.usermanager.api.controller;

import br.com.atech.usermanager.api.dto.CreateUserDto;
import br.com.atech.usermanager.api.dto.EditUserDto;
import br.com.atech.usermanager.api.dto.UserProfileDto;
import br.com.atech.usermanager.domain.model.User;
import br.com.atech.usermanager.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserController {
    private  final UserService userService;

    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<UserProfileDto>  create(@RequestBody @Valid CreateUserDto createUserDto) {
        log.info("UserController.create - start - input  [{}]", createUserDto.getEmail());

        User userCreated = userService.create(createUserDto);

        log.info("ProductController.create - end - outPut  [{}]", userCreated.getId());
        return new ResponseEntity<>(modelMapper.map(userCreated, UserProfileDto.class), HttpStatus.CREATED);
    }
    @GetMapping( "/{id}")
    public ResponseEntity<UserProfileDto> getUserProfileById(@PathVariable(value = "id") long id){
        log.info("UserController.getUserProfileById - start - input  [{}]", id );
        return  new ResponseEntity<>(modelMapper.map(userService.findAndValidateById(id) , UserProfileDto.class),
                HttpStatus.OK);
    }
    @DeleteMapping( "/{id}")
    public ResponseEntity delete(@PathVariable(value = "id") long id){
        log.info("UserController.getUserProfileById - start - input  [{}]", id );
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping
    public ResponseEntity<UserProfileDto> edit(@RequestBody @Valid EditUserDto editUserDto) {
        log.info("UserController.edit - start - input  [{},{}]", editUserDto.getEmail(),editUserDto.getId());

        User userCreated = userService.replace(editUserDto);

        log.info("ProductController.edit - end - outPut  [{},{}]", userCreated.getEmail(), userCreated.getId());
        return new ResponseEntity<>(modelMapper.map(userCreated, UserProfileDto.class), HttpStatus.OK);
    }

}
