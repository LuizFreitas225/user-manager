package br.com.atech.usermanager.service;

import br.com.atech.usermanager.dto.CreateUserDto;
import br.com.atech.usermanager.dto.EditUserDto;
import br.com.atech.usermanager.constant.ErrorCode;
import br.com.atech.usermanager.model.Status;
import br.com.atech.usermanager.model.User;
import br.com.atech.usermanager.repository.UserRepository;
import br.com.atech.usermanager.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private static final int MAX_PASSWORD_SIZE_ALLOWED = 6;

    @Transactional
    public User create(final CreateUserDto createUserDto) {
        log.info("UserService.create - start - input  [{}]", createUserDto.getEmail());

        User user = modelMapper.map(createUserDto, User.class);
        validateCreateUser(user);
        User userCreated = userRepository.save(user);

        log.info("UserService.create - end- output [{}]", createUserDto.getEmail());
        return userCreated;
    }

    public User findAndValidateById(final long id) {
        log.info("UserService.findAndValidateById - start - input [{}]", id);

        User userFound = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));

        log.info("UserService.findAndValidateById - end - output [{}]", userFound.getId());
        return userFound;
    }

    @Transactional
    public void delete(final long id) {
        log.info("UserService.findAndValidateById - start - input [{}]", id);

        User userFound = findAndValidateById(id);
        userFound.setStatus(Status.DELETED);
        userRepository.save(userFound);
    }

    @Transactional
    public User replace(final EditUserDto editUserDto) {
        log.info("UserService.replace - start - input  [{},{}]", editUserDto.getEmail(), editUserDto.getId());

        User user = modelMapper.map(editUserDto, User.class);
        User currentUser = findAndValidateById(user.getId());
        user.setCreateDate(currentUser.getCreateDate());
        validateEditUser(currentUser, user);
        User userCreated = userRepository.save(user);

        log.info("UserService.replace - end- output [{},{}]", userCreated.getEmail(), userCreated.getId());
        return userCreated;
    }

    public Page<User> findAByNameOrEmailOrUserName(PageRequest pageRequest, String searchTerm) {
        log.info("UserService.findAByNameOrEmailOrUserName - start - input [{}]", searchTerm);
        Page<User> userPage = userRepository.findAByNameOrEmailOrUserName(pageRequest, searchTerm);
        log.info("UserService.findAByNameOrEmailOrUserName - end - output [{}]", userPage.getTotalElements());
        return userPage;
    }

    public void validateCreateUser(User user) {
        log.info("UserService.validateUser - start - input [{}]", user.getEmail());
        if (validateDeletedUser(user)) {
            throw new BadRequestException(ErrorCode.DELETED_STATUS);
        }
        if (emailInUse(user.getEmail())) {
            throw new BadRequestException(ErrorCode.EMAIL_IN_USE);
        }
        if (!passwordIsValid(user.getPassword())) {
            throw new BadRequestException(ErrorCode.SHORT_PASSWORD);
        }
    }
    public void validateEditUser(User currentUser, User newUser) {
        log.info("UserService.validateEditUser - start - input [{}]", currentUser.getEmail(), newUser.getEmail());
        if (validateDeletedUser(currentUser)) {
            throw new BadRequestException(ErrorCode.DELETED_STATUS);
        }

        if (!passwordIsValid(newUser.getPassword())) {
            throw new BadRequestException(ErrorCode.SHORT_PASSWORD);
        }

        if (!currentUser.getEmail().equals(newUser.getEmail()) && emailInUse(newUser.getEmail())) {
            throw new BadRequestException(ErrorCode.EMAIL_IN_USE);
        }
    }
    public boolean validateDeletedUser(User user) {
        return (user.getStatus() == Status.DELETED);
    }

    public boolean emailInUse(final String email) {
        return  userRepository.existsByEmail(email);
    }

    public boolean passwordIsValid(final String password) {
        return  password.length() >= MAX_PASSWORD_SIZE_ALLOWED;
    }
}
