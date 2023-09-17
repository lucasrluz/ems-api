package com.emsapi.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.emsapi.domains.CompanyDomain;
import com.emsapi.domains.util.InvalidCompanyDomainException;
import com.emsapi.dtos.company.GetAllCompanyDTOResponse;
import com.emsapi.dtos.company.SaveCompanyDTORequest;
import com.emsapi.dtos.company.SaveCompanyDTOResponse;
import com.emsapi.models.CompanyModel;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.repositories.UserRepository;

@Service
public class CompanyService {
    private CompanyRepository companyRepository;
    private UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public SaveCompanyDTOResponse save(SaveCompanyDTORequest saveCompanyDTORequest, String userId) throws InvalidCompanyDomainException {
        CompanyDomain companyDomain = CompanyDomain.validate(
            saveCompanyDTORequest.getName(),
            saveCompanyDTORequest.getDescription()
        );

        Optional<UserModel> findUserModel = this.userRepository.findById(UUID.fromString(userId));

        CompanyModel companyModel = new CompanyModel(
            companyDomain.getName(),
            companyDomain.getDescription(),
            findUserModel.get()
        );

        CompanyModel saveCompanyModelResponse = this.companyRepository.save(companyModel);

        return new SaveCompanyDTOResponse(saveCompanyModelResponse.getCompanyId().toString());
    }

    public List<GetAllCompanyDTOResponse> getAll(String userId) {
        Optional<UserModel> findUser = this.userRepository.findById(UUID.fromString(userId));
        
        List<CompanyModel> companyModels = this.companyRepository.findByUserModel(findUser.get());

        List<GetAllCompanyDTOResponse> getAllCompanyDTOResponse = new ArrayList<GetAllCompanyDTOResponse>();

        companyModels.forEach((element) -> {
            GetAllCompanyDTOResponse newGetAllCompanyDTOResponse = new GetAllCompanyDTOResponse(
                element.getCompanyId().toString(),
                element.getName(),
                element.getDescription()
            );

            getAllCompanyDTOResponse.add(newGetAllCompanyDTOResponse);
        });

        return getAllCompanyDTOResponse;
    }
}
