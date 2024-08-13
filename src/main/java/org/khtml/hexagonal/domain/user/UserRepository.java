package org.khtml.hexagonal.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByProviderId(String providerId);
    Optional<User> findByProviderId(String providerId);
}
