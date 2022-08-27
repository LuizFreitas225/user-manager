package br.com.atech.usermanager.domain.repository;

import br.com.atech.usermanager.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query(value = " SELECT p FROM User p "
            + " WHERE LOWER (p.name ) LIKE %:searchTerm% "
            + " OR LOWER (p.email) LIKE %:searchTerm% "
            + " OR LOWER (p.userName) LIKE %:searchTerm% ")
    Page<User> findAByNameOrEmailOrUserName(PageRequest pageRequest, String searchTerm);
}
