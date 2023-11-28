package com.emsapi.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.emsapi.domains.EmployeeDomain;
import com.emsapi.domains.util.InvalidEmployeeDomainException;
import com.emsapi.dtos.employee.DeleteEmployeeDTOResponse;
import com.emsapi.dtos.employee.GetAllEmployeeDTOResponse;
import com.emsapi.dtos.employee.GetEmployeeDTOResponse;
import com.emsapi.dtos.employee.SaveEmployeeDTORequest;
import com.emsapi.dtos.employee.SaveEmployeeDTOResponse;
import com.emsapi.dtos.employee.UpdateEmployeeDTORequest;
import com.emsapi.dtos.employee.UpdateEmployeeDTOResponse;
import com.emsapi.models.CompanyModel;
import com.emsapi.models.EmployeeModel;
import com.emsapi.models.RoleModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.repositories.EmployeeRepository;
import com.emsapi.repositories.RoleRepository;
import com.emsapi.services.util.CompanyNotFoundException;
import com.emsapi.services.util.EmployeeNotFoundException;
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

    public List<GetAllEmployeeDTOResponse> getAll(String companyId) throws CompanyNotFoundException {
        Optional<CompanyModel> findCompanyModel = this.companyRepository.findById(UUID.fromString(companyId));

        if (findCompanyModel.isEmpty()) {
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

    public GetEmployeeDTOResponse get(String employeeId, String companyId) throws CompanyNotFoundException, EmployeeNotFoundException {
        Optional<EmployeeModel> findEmployeeModel = this.employeeRepository.findById(UUID.fromString(employeeId));

        if (findEmployeeModel.isEmpty()) {
            throw new EmployeeNotFoundException();
        }

        return new GetEmployeeDTOResponse(
            findEmployeeModel.get().getEmployeeId().toString(),
            findEmployeeModel.get().getFirstName(),
            findEmployeeModel.get().getLastName(),
            findEmployeeModel.get().getAge(),
            findEmployeeModel.get().getAddress(),
            findEmployeeModel.get().getCompanyModel().getCompanyId().toString(),
            findEmployeeModel.get().getEmail(),
            findEmployeeModel.get().getRoleModel().getRoleId().toString()
        );
    }

    public UpdateEmployeeDTOResponse update(UpdateEmployeeDTORequest updateEmployeeDTORequest, String employeeId) throws InvalidEmployeeDomainException, CompanyNotFoundException, RoleNotFoundException, EmployeeNotFoundException {
        EmployeeDomain employeeDomain = EmployeeDomain.validate(
            updateEmployeeDTORequest.getFirstName(),
            updateEmployeeDTORequest.getLastName(),
            updateEmployeeDTORequest.getAge(),
            updateEmployeeDTORequest.getAddress(),
            updateEmployeeDTORequest.getEmail()
        );

        Optional<EmployeeModel> findEmployeeModel = this.employeeRepository.findById(UUID.fromString(employeeId));

        if (findEmployeeModel.isEmpty()) {
            throw new EmployeeNotFoundException();
        }

        Optional<CompanyModel> findCompanyModel = this.companyRepository.findById(UUID.fromString(updateEmployeeDTORequest.getCompanyId()));

        if (findCompanyModel.isEmpty()) {
            throw new CompanyNotFoundException();
        } 

        Optional<RoleModel> findRoleModel = this.roleRepository.findById(UUID.fromString(updateEmployeeDTORequest.getRoleId()));

        if (findRoleModel.isEmpty()) {
            throw new RoleNotFoundException();
        }

        EmployeeModel employeeModel = new EmployeeModel(
            UUID.fromString(employeeId),
            employeeDomain.getFirstName(),
            employeeDomain.getLastName(),
            employeeDomain.getAge(),
            employeeDomain.getAddress(),
            employeeDomain.getEmail(),
            findRoleModel.get(),
            findCompanyModel.get()
        );

        EmployeeModel updateEmployeeModel = this.employeeRepository.save(employeeModel);

        return new UpdateEmployeeDTOResponse(updateEmployeeModel.getEmployeeId().toString());
    }

	public DeleteEmployeeDTOResponse delete(String employeeId) throws EmployeeNotFoundException {
		Optional<EmployeeModel> findEmployeeModel = this.employeeRepository.findById(UUID.fromString(employeeId));

		if (findEmployeeModel.isEmpty()) {
			throw new EmployeeNotFoundException();
		}	

		this.employeeRepository.deleteById(findEmployeeModel.get().getEmployeeId());

		return new DeleteEmployeeDTOResponse(findEmployeeModel.get().getEmployeeId().toString());
	}
}

