package br.com.atech.usermanager.api.dto;

import lombok.Data;
import lombok.Getter;


@Data
public class UserProfileDto {
    private String name;
    private String email;
    private String userName;

}
