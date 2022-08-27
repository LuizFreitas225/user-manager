package br.com.atech.usermanager.domain.model;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "enjoyer")
@Data
public class User extends DefaultEntity{
    private String name;
    @Column(unique=true)
    private String email;
    private String password;
    private String userName;

    @Enumerated(EnumType.STRING)
    private Status status;
}
