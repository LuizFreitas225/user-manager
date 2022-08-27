package br.com.atech.usermanager.service;

import br.com.atech.usermanager.api.dto.CreateUserDto;
import br.com.atech.usermanager.constant.ErrorCode;
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

        if(emailInUse(createUserDto.getEmail())){
            throw  new BadRequestException(ErrorCode.EMAIL_IN_USE);
        }
        User user = modelMapper.map(createUserDto, User.class);
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

    public boolean emailInUse(final String email) {
        log.info("UserService.emailInUse - start - input [{}]", email);
        return  userRepository.findByEmail(email).isPresent();
    }


}
