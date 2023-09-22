package com.emsapi.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.emsapi.domains.CompanyDomain;
import com.emsapi.domains.util.InvalidCompanyDomainException;
import com.emsapi.dtos.company.GetAllCompanyDTOResponse;
import com.emsapi.dtos.company.GetCompanyDTOResponse;
import com.emsapi.dtos.company.SaveCompanyDTORequest;
import com.emsapi.dtos.company.SaveCompanyDTOResponse;
import com.emsapi.dtos.company.UpdateCompanyDTORequest;
import com.emsapi.dtos.company.UpdateCompanyDTOResponse;
import com.emsapi.models.CompanyModel;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.repositories.UserRepository;
import com.emsapi.services.util.CompanyNotFoundException;

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

    public GetCompanyDTOResponse get(String companyId, String userId) throws CompanyNotFoundException {
        Optional<UserModel> findUser = this.userRepository.findById(UUID.fromString(userId));
        
        Optional<CompanyModel> companyModel = this.companyRepository.findById(UUID.fromString(companyId));

        if (companyModel.isEmpty()) {
            throw new CompanyNotFoundException();
        }

        if (!companyModel.get().getUserModel().equals(findUser.get())) {
            throw new CompanyNotFoundException();
        }

        return new GetCompanyDTOResponse(
            companyModel.get().getCompanyId().toString(),
            companyModel.get().getName(),
            companyModel.get().getDescription()
        );
    }

    public UpdateCompanyDTOResponse update(UpdateCompanyDTORequest updateCompanyDTORequest, String companyId, String userId) throws CompanyNotFoundException, InvalidCompanyDomainException {
        CompanyDomain companyDomain = CompanyDomain.validate(updateCompanyDTORequest.getName(), updateCompanyDTORequest.getDescription());
        
        Optional<CompanyModel> findCompanyModel = this.companyRepository.findById(UUID.fromString(companyId));
        
        if (findCompanyModel.isEmpty()) {
            throw new CompanyNotFoundException();
        }

        Optional<UserModel> findUserModel = this.userRepository.findById(UUID.fromString(userId));
        
        if (!findCompanyModel.get().getUserModel().equals(findUserModel.get())) {
            throw new CompanyNotFoundException();
        }

        CompanyModel companyModel = new CompanyModel(UUID.fromString(companyId), companyDomain.getName(), companyDomain.getDescription(), findUserModel.get());
    
        CompanyModel updateCompanyModel = this.companyRepository.save(companyModel);

        return new UpdateCompanyDTOResponse(updateCompanyModel.getCompanyId().toString());
    }
}
