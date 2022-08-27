package br.com.atech.usermanager.service;

import br.com.atech.usermanager.api.dto.CreateUserDto;
import br.com.atech.usermanager.api.dto.EditUserDto;
import br.com.atech.usermanager.constant.ErrorCode;
import br.com.atech.usermanager.domain.model.Status;
import br.com.atech.usermanager.domain.model.User;
import br.com.atech.usermanager.domain.repository.UserRepository;
import br.com.atech.usermanager.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Transactional
    public User create(final CreateUserDto createUserDto) {
        log.info("UserService.create - start - input  [{}]", createUserDto.getEmail());

        User user = modelMapper.map(createUserDto, User.class);
        validateUser(user);
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
        validateUser(user);
        User userCreated = userRepository.save(user);

        log.info("UserService.replace - end- output [{},{}]", userCreated.getEmail(),userCreated.getId());
        return userCreated;
    }

    public void validateUser(User user ){
        log.info("UserService.validateUser - start - input [{}]", user.getEmail());
        if(emailInUse(user.getEmail())){
            throw  new BadRequestException(ErrorCode.EMAIL_IN_USE);
        }
        if(!passwordIsValid(user.getPassword())){
            throw  new BadRequestException(ErrorCode.SHORT_PASSWORD );
        }
    }
    public boolean emailInUse(final String email) {
        return  userRepository.findByEmail(email).isPresent();
    }

    public boolean passwordIsValid(final String password) {
        return  password.length() >= 6 ? true : false;
    }


}
