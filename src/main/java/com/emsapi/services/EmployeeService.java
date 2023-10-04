package com.emsapi.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.emsapi.domains.EmployeeDomain;
import com.emsapi.domains.util.InvalidEmployeeDomainException;
import com.emsapi.dtos.employee.GetAllEmployeeDTOResponse;
import com.emsapi.dtos.employee.SaveEmployeeDTORequest;
import com.emsapi.dtos.employee.SaveEmployeeDTOResponse;
import com.emsapi.models.CompanyModel;
import com.emsapi.models.EmployeeModel;
import com.emsapi.models.RoleModel;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.repositories.EmployeeRepository;
import com.emsapi.repositories.RoleRepository;
import com.emsapi.repositories.UserRepository;
import com.emsapi.services.util.CompanyNotFoundException;
import com.emsapi.services.util.RoleNotFoundException;

@Service
public class EmployeeService {
    private EmployeeRepository employeeRepository;
    private CompanyRepository companyRepository;
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    public EmployeeService(EmployeeRepository employeeRepository, CompanyRepository companyRepository, RoleRepository roleRepository, UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
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

    public List<GetAllEmployeeDTOResponse> getAll(String companyId, String userId) throws CompanyNotFoundException {
        UserModel findUserModel = this.userRepository.findById(UUID.fromString(userId)).get();

        Optional<CompanyModel> findCompanyModel = this.companyRepository.findById(UUID.fromString(companyId));

        if (findCompanyModel.isEmpty()) {
            throw new CompanyNotFoundException();
        }

        if (!findCompanyModel.get().getUserModel().getUserId().equals(findUserModel.getUserId())) {
            throw new CompanyNotFoundException();
        }

        List<EmployeeModel> employeeModels = this.employeeRepository.findByCompanyModel(findCompanyModel.get());

        List<GetAllEmployeeDTOResponse> getAllEmployeeDTOResponses = new ArrayList<GetAllEmployeeDTOResponse>();

        employeeModels.forEach((element) -> {
            GetAllEmployeeDTOResponse getAllEmployeeDTOResponse = new GetAllEmployeeDTOResponse(
                element.getFirstName(),
                element.getLastName(),
                element.getAge(),
                element.getAddress(),
                element.getCompanyModel().getCompanyId().toString(),
                element.getEmail(),
                element.getRoleModel().getRoleId().toString()
            );

            getAllEmployeeDTOResponses.add(getAllEmployeeDTOResponse);
        });

        return getAllEmployeeDTOResponses;
    }
}
