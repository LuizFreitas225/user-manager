package br.com.atech.usermanager.api;

import br.com.atech.usermanager.dto.CreateUserDto;
import br.com.atech.usermanager.dto.EditUserDto;
import br.com.atech.usermanager.dto.UserProfileDto;
import br.com.atech.usermanager.model.User;
import br.com.atech.usermanager.service.UserService;
import br.com.atech.usermanager.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserApi {
    private  final UserService userService;

    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<UserProfileDto> create(@RequestBody @Valid CreateUserDto createUserDto) {
        log.info("UserController.create - start - input  [{}]", createUserDto.getEmail());

        User user = modelMapper.map(createUserDto, User.class);
        User userCreated = userService.create(user);

        log.info("UserController.create - end - outPut  [{}]", userCreated.getId());
        return new ResponseEntity<>(modelMapper.map(userCreated, UserProfileDto.class), HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDto> getUserProfileById(@PathVariable(value = "id") long id) {
        log.info("UserController.getUserProfileById - start - input  [{}]", id);
        return new ResponseEntity<>(modelMapper.map(userService.findAndValidateById(id), UserProfileDto.class), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(value = "id") long id) {
        log.info("UserController.getUserProfileById - start - input  [{}]", id);
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<UserProfileDto> edit(@RequestBody @Valid EditUserDto editUserDto) {
        log.info("UserController.edit - start - input  [{},{}]", editUserDto.getEmail(), editUserDto.getId());

        User user = modelMapper.map(editUserDto, User.class);
        User userCreated = userService.update(user);

        log.info("UserController.edit - end - outPut  [{},{}]", userCreated.getEmail(), userCreated.getId());
        return new ResponseEntity<>(modelMapper.map(userCreated, UserProfileDto.class), HttpStatus.OK);
    }

    @GetMapping
    public Page<User> search(
        @RequestParam(value = "page", required = false) final Integer page,
        @RequestParam(value = "size", required = false) final Integer size,
        @RequestParam(value = "sort", required = false) final String sort,
        @RequestParam(value = "orderBy", required = false) final String orderBy,
        @RequestParam(value = "searchTerm", required = false, defaultValue = "") final String searchTerm
    ) {

        log.info("UserController.search - start - input  [{},{},{},{}]", page, size, sort, searchTerm);

        return userService.findAByNameOrEmailOrUserName(PaginationUtil.configuringPageable(page, size, sort, orderBy), searchTerm);
    }


}
