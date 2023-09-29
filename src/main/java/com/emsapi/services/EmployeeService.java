package com.emsapi.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.emsapi.domains.EmployeeDomain;
import com.emsapi.domains.util.InvalidEmployeeDomainException;
import com.emsapi.dtos.employee.SaveEmployeeDTORequest;
import com.emsapi.dtos.employee.SaveEmployeeDTOResponse;
import com.emsapi.models.CompanyModel;
import com.emsapi.models.EmployeeModel;
import com.emsapi.models.RoleModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.repositories.EmployeeRepository;
import com.emsapi.repositories.RoleRepository;
import com.emsapi.services.util.CompanyNotFoundException;
import com.emsapi.services.util.RoleNotFoundException;

@Service
public class EmployeeService {
    private EmployeeRepository employeeRepository;
    private CompanyRepository companyRepository;
    private RoleRepository roleRepository;

    public EmployeeService(EmployeeRepository employeeRepository, CompanyRepository companyRepository, RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
        this.roleRepository = roleRepository;
    }

    public SaveEmployeeDTOResponse save(SaveEmployeeDTORequest saveEmployeeDTORequest) throws InvalidEmployeeDomainException, RoleNotFoundException, CompanyNotFoundException {
        EmployeeDomain employeeDomain = EmployeeDomain.validate(
            saveEmployeeDTORequest.getFirstName(),
            saveEmployeeDTORequest.getLastName(),
            saveEmployeeDTORequest.getAge(),
            saveEmployeeDTORequest.getAddress(),
            saveEmployeeDTORequest.getEmail()
        );
        
        Optional<RoleModel> findRoleModel = this.roleRepository.findById(UUID.fromString(saveEmployeeDTORequest.getRoleId()));

        if (findRoleModel.isEmpty()) {
            throw new RoleNotFoundException();
        }

        Optional<CompanyModel> findCompanyModel = this.companyRepository.findById(UUID.fromString(saveEmployeeDTORequest.getCompanyId()));

        if (findCompanyModel.isEmpty()) {
            throw new CompanyNotFoundException();
        }

        EmployeeModel employeeModel = new EmployeeModel(
            employeeDomain.getFirstName(),
            employeeDomain.getLastName(),
            employeeDomain.getAge(),
            employeeDomain.getAddress(),
            employeeDomain.getEmail(),
            findRoleModel.get(),
            findCompanyModel.get()
        );

        EmployeeModel saveEmployeeModel = this.employeeRepository.save(employeeModel);

        return new SaveEmployeeDTOResponse(saveEmployeeModel.getEmployeeId().toString());
    }
}
