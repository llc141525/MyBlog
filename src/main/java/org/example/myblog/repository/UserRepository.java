package org.example.myblog.repository;

import org.example.myblog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByArticles_User_Username(String username);

    List<User> findUserByUsername(String username);

    User findByArticles_User_Username(String username);

    boolean existsByUsername(String username);

    User findByUsername(String username);

    User findUserById(Long id);
}
