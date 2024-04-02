package com.kopchak.authserver.repository;

import com.kopchak.authserver.domain.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<AppUser, Integer> {
    Optional<AppUser> findByUsername(String username);
}
