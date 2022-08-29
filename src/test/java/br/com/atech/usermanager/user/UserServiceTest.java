package br.com.atech.usermanager.user;

import br.com.atech.usermanager.exception.ShortPasswordException;
import br.com.atech.usermanager.exception.UserIsDeletedException;
import br.com.atech.usermanager.exception.UserIsInactiveException;
import br.com.atech.usermanager.model.Status;
import br.com.atech.usermanager.model.User;
import br.com.atech.usermanager.repository.UserRepository;
import br.com.atech.usermanager.service.UserService;
import br.com.atech.usermanager.util.PaginationUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;

import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    static  User fullUser;

    static User incompleteUser;

    @BeforeEach
    public  void load(){
        fullUser = UserMockUtil.getUserWithId();
        incompleteUser = UserMockUtil.getUserWithoutId();
    }


    @Test
    void create_doNotCreateIfStatusIsDeleted() {
        fullUser.setStatus(Status.DELETED);
        incompleteUser.setStatus(Status.DELETED);

        BDDMockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(fullUser);

        Assertions.assertThatThrownBy(() -> userService.create(incompleteUser)).isInstanceOf(UserIsDeletedException.class);
    }
    @Test
    void create_doNotCreateIfPasswordIsShort() {
        fullUser.setPassword("senha");
        incompleteUser.setPassword("senha");

        BDDMockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(fullUser);

        Assertions.assertThatThrownBy(() -> userService.create(incompleteUser)).isInstanceOf(ShortPasswordException.class);
    }


    @Test
    void update_doNotCreateIfStatusIsDeleted() {
        fullUser.setStatus(Status.DELETED);

        BDDMockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(fullUser);

        Assertions.assertThatThrownBy(() -> userService.create(fullUser)).isInstanceOf(UserIsDeletedException.class);
    }
    @Test
    void update_doNotCreateIfPasswordIsShort() {
        fullUser.setPassword("senha");

        BDDMockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(fullUser);

        Assertions.assertThatThrownBy(() -> userService.create(fullUser)).isInstanceOf(ShortPasswordException.class);
    }

    @Test
    void findAByNameOrCode_CannotIsEmptyAndCannotHaveListChange() {
        BDDMockito.when(userRepository.findAByNameOrEmailOrUserName(ArgumentMatchers.any(PageRequest.class),
                ArgumentMatchers.anyString())).thenReturn(new PageImpl<>(List.of(fullUser)));

        Page<User> page = userService.findAByNameOrEmailOrUserName(PaginationUtil.configuringPageable(null, null,
                null, null), "");

        Assertions.assertThat(page).isNotEmpty();
        Assertions.assertThat(page.toList()).isNotEmpty();
        Assertions.assertThat(page.toList().size()).isEqualTo(1);
        Assertions.assertThat(page.toList().get(0)).isEqualTo(fullUser);
    }

    @Test
    void findValidUserByEmail_CannotIsEmpty() {
        BDDMockito.when(userRepository.findByEmail(ArgumentMatchers.any(String.class))).thenReturn(fullUser);

        User user = userService.findValidUserByEmail("luiz@atech.com");

        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user).isEqualTo(fullUser);
    }

    @Test
    void findValidUserByEmail_cannotIsDeleted() {
        fullUser.setStatus(Status.DELETED);
        BDDMockito.when(userRepository.findByEmail(ArgumentMatchers.any(String.class))).thenReturn(fullUser);

        Assertions.assertThatThrownBy(() -> userService.findValidUserByEmail("luiz@atech.com")).isInstanceOf(UserIsDeletedException.class);
    }
    @Test
    void findValidUserByEmail_cannotIsInactive() {
        fullUser.setStatus(Status.INACTIVE);
        BDDMockito.when(userRepository.findByEmail(ArgumentMatchers.any(String.class))).thenReturn(fullUser);

        Assertions.assertThatThrownBy(() -> userService.findValidUserByEmail("luiz@atech.com")).isInstanceOf(UserIsInactiveException.class);
    }


}
