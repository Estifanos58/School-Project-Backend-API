package com.schoolproject.ChatAPP.repository;

import com.schoolproject.ChatAPP.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> { // Add generic types <User, String>

    Optional<User> findByEmail(String email);

    // Search query with regex excluding the current user
    List<User> findByIdNotAndFirstnameRegexIgnoreCaseOrIdNotAndLastnameRegexIgnoreCaseOrIdNotAndEmailRegexIgnoreCase(
            String id1, String firstname, String id2, String lastname, String id3, String email
    );

    // Find all users excluding the specified ID
    List<User> findByIdNot(String id);
}
