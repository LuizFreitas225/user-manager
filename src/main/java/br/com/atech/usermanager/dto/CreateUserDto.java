package br.com.atech.usermanager.dto;

import br.com.atech.usermanager.constant.ErrorMessage;
import br.com.atech.usermanager.model.Status;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateUserDto {
    @NotBlank(message = ErrorMessage.NAME_IS_MANDATORY)
    private String name;
    @NotBlank(message = ErrorMessage.EMAIL_IS_MANDATORY)
    @Email(message = ErrorMessage.VALID_EMAIL_IS_REQUIRED)
    private String email;
    @NotBlank(message = ErrorMessage.PASSWORD_IS_MANDATORY)
    private String password;
    @NotBlank(message = ErrorMessage.USER_NAME_IS_MANDATORY)
    private String userName;
    @NotNull(message = ErrorMessage.STATUS_IS_MANDATORY)
    @Enumerated(EnumType.ORDINAL)
    private Status status;
}
