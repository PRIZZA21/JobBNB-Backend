package com.JobBNB.dev.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.JobBNB.dev.user.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByVerificationToken(String verificationToken);
}
