package com.emsapi.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emsapi.models.CompanyModel;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyModel, UUID> {
	Optional<CompanyModel> findByEmail(String email);
}
