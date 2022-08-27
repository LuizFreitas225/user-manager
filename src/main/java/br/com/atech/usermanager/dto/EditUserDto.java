package br.com.atech.usermanager.dto;

import br.com.atech.usermanager.constant.ErrorCode;
import br.com.atech.usermanager.model.Status;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EditUserDto {
    @NotNull(message =  ErrorCode.ID_IS_MANDATORY)
    private Long id;
    @NotBlank(message = ErrorCode.NAME_IS_MANDATORY)
    private String name;
    @NotBlank(message = ErrorCode.EMAIL_IS_MANDATORY)
    @Email(message = ErrorCode.VALID_EMAIL_IS_REQUIRED)
    private String email;
    @NotBlank(message = ErrorCode.PASSWORD_IS_MANDATORY)
    private String password;
    @NotBlank(message = ErrorCode.USER_NAME_IS_MANDATORY)
    private String userName;
    @NotNull(message = ErrorCode.STATUS_IS_MANDATORY)
    @Enumerated(EnumType.ORDINAL)
    private Status status;
}
