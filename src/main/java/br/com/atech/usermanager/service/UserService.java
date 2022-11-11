package br.com.atech.usermanager.service;

import br.com.atech.usermanager.exception.EmailInUseException;
import br.com.atech.usermanager.exception.ShortPasswordException;
import br.com.atech.usermanager.exception.UserIsDeletedException;
import br.com.atech.usermanager.exception.UserIsInactiveException;
import br.com.atech.usermanager.exception.UserNotFoundException;
import br.com.atech.usermanager.model.Status;
import br.com.atech.usermanager.model.User;
import br.com.atech.usermanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private static final int MAX_PASSWORD_SIZE_ALLOWED = 6;

    public User create(final User user) {
        log.info("UserService.create - start - input  [{}]", user.getEmail());

        validateCreateUser(user);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        User userCreated = userRepository.save(user);

        log.info("UserService.create - end- output [{}]", user.getEmail());
        return userCreated;
    }

    public User findAndValidateById(final long id) {
        log.info("UserService.findAndValidateById - start - input [{}]", id);

        User userFound = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException());

        log.info("UserService.findAndValidateById - end - output [{}]", userFound.getId());
        return userFound;
    }

    public void delete(final long id) {
        log.info("UserService.findAndValidateById - start - input [{}]", id);

        User userFound = findAndValidateById(id);
        userFound.setStatus(Status.DELETED);
        userRepository.save(userFound);
    }

    public User update(final User user) {
        log.info("UserService.update - start - input  [{},{}]", user.getEmail(), user.getId());

        User currentUser = this.findValidUserByEmail(user.getEmail());
        user.setCreateDate(currentUser.getCreateDate());
        validateEditUser(currentUser, user);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        User userCreated = userRepository.save(user);

        log.info("UserService.update - end- output [{},{}]", userCreated.getEmail(), userCreated.getId());
        return userCreated;
    }

    public Page<User> findAByNameOrEmailOrUserName(PageRequest pageRequest, String searchTerm) {
        log.info("UserService.findAByNameOrEmailOrUserName - start - input [{}]", searchTerm);
        Page<User> userPage = userRepository.findAByNameOrEmailOrUserName(pageRequest, searchTerm);
        log.info("UserService.findAByNameOrEmailOrUserName - end - output [{}]", userPage.getTotalElements());
        return userPage;
    }
    public User findValidUserByEmail(String email) {
        log.info("UserService.findByEmail - start - input [{}]", email);
        User user = userRepository.findByEmail(email);

        if (user.getStatus() == Status.DELETED) {
           throw new UserIsDeletedException();
        }

        if (user.getStatus() == Status.INACTIVE) {
            throw new UserIsInactiveException();
        }
        return user;
    }

    public void validateCreateUser(User user) {
        log.info("UserService.validateUser - start - input [{}]", user.getEmail());
        if (validateDeletedUser(user)) {
            throw new UserIsDeletedException();
        }
        if (!passwordIsValid(user.getPassword())) {
            throw new ShortPasswordException();
        }
        if (emailInUse(user.getEmail())) {
            throw new EmailInUseException();
        }
    }
    public void validateEditUser(User currentUser, User newUser) {
        log.info("UserService.validateEditUser - start - input [{}]", currentUser.getEmail(), newUser.getEmail());
        if (validateDeletedUser(currentUser)) {
            throw new UserIsDeletedException();
        }

        if (!passwordIsValid(newUser.getPassword())) {
            throw new ShortPasswordException();
        }

        if (!currentUser.getEmail().equals(newUser.getEmail()) && emailInUse(newUser.getEmail())) {
            throw new EmailInUseException();
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
