package org.example.myblog.repository;

import org.example.myblog.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    boolean existsByUsername(String username);

    Users findByUsername(String username);

    Users findUserById(Long id);
}
