package com.emsapi.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emsapi.models.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {
    public Optional<UserModel> findByEmail(String email);
}
