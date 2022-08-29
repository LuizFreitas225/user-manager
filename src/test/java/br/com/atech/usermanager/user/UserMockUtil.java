package br.com.atech.usermanager.user;

import br.com.atech.usermanager.model.Status;
import br.com.atech.usermanager.model.User;

import java.time.LocalDateTime;

public final class UserMockUtil {
    public  static User getUserWithId(){
        User user = new User();
        user.setId((long) 1);
        user.setName("Luiz");
        user.setPassword("senha1");
        user.setStatus(Status.ACTIVE);
        user.setUserName("luizNicolau");
        user.setEmail("luiz@atech.com");
        user.setCreateDate(LocalDateTime.now());
        user.setLastModifiedDate(LocalDateTime.now());

        return user;
    }

    public  static User getUserWithoutId(){
        User user = new User();
        user.setName("Luiz");
        user.setPassword("senha1");
        user.setStatus(Status.ACTIVE);
        user.setUserName("luizNicolau");
        user.setEmail("luiz@atech.com.br");
        user.setCreateDate(LocalDateTime.now());
        user.setLastModifiedDate(LocalDateTime.now());

        return user;
    }

}
