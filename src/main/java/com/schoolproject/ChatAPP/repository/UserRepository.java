package com.schoolproject.ChatAPP.repository;

import com.schoolproject.ChatAPP.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> { // Add generic types <User, String>

    Optional<User> findByEmail(String email);
}
