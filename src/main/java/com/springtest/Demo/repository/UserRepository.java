package com.springtest.Demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    @Query(value = "select * from users where email = :email", nativeQuery = true   )
    public Optional<User> findByEmail(String email);


    @Query(value = "select * from users where username = :username", nativeQuery = true   )
    public Optional<User> findByUsername(String username);
}
