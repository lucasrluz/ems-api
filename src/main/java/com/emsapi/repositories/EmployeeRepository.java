package com.emsapi.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emsapi.models.CompanyModel;
import com.emsapi.models.EmployeeModel;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeModel, UUID> {
    List<EmployeeModel> findByCompanyModel(CompanyModel companyModel);
}
