package com.emsapi.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.emsapi.domains.CompanyDomain;
import com.emsapi.domains.util.InvalidCompanyDomainException;
import com.emsapi.dtos.authentication.SignInDTORequest;
import com.emsapi.dtos.authentication.SignInDTOResponse;
import com.emsapi.dtos.authentication.SignUpDTORequest;
import com.emsapi.dtos.authentication.SignUpDTOResponse;
import com.emsapi.dtos.company.DeleteCompanyDTOResponse;
import com.emsapi.dtos.company.GetCompanyDTOResponse;
import com.emsapi.dtos.company.UpdateCompanyDTORequest;
import com.emsapi.dtos.company.UpdateCompanyDTOResponse;
import com.emsapi.models.CompanyModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.services.util.CompanyNotFoundException;
import com.emsapi.services.util.EmailOrPasswordInvalidException;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Service
public class CompanyService {
    private CompanyRepository companyRepository;
	private JwtService jwtService;

    public CompanyService(CompanyRepository companyRepository, JwtService jwtService) {
        this.companyRepository = companyRepository;
		this.jwtService = jwtService;
    }

    public SignUpDTOResponse signUp(SignUpDTORequest signUpDTORequest) throws InvalidCompanyDomainException {
        CompanyDomain companyDomain = CompanyDomain.validate(
            signUpDTORequest.getName(),
            signUpDTORequest.getDescription(),
			signUpDTORequest.getEmail(),
			signUpDTORequest.getPassword()
        );

        CompanyModel companyModel = new CompanyModel(
            companyDomain.getName(),
            companyDomain.getDescription(),
			companyDomain.getEmail(),
			companyDomain.getPassword()
        );

        CompanyModel saveCompanyModelResponse = this.companyRepository.save(companyModel);

        return new SignUpDTOResponse(saveCompanyModelResponse.getCompanyId().toString());
    }

    public SignInDTOResponse signIn(SignInDTORequest signInDTORequest) throws Exception {
        Optional<CompanyModel> findCompanyModel = this.companyRepository.findByEmail(signInDTORequest.getEmail());

        if (findCompanyModel.isEmpty()) {
            throw new EmailOrPasswordInvalidException();
        }

        boolean validPassword = BCrypt.verifyer().verify(
            signInDTORequest.getPassword().toCharArray(),
            findCompanyModel.get().getPassword()
        ).verified;

        if (!validPassword) {
            throw new EmailOrPasswordInvalidException();
        }

        String jwt = this.jwtService.generateJwt(findCompanyModel.get().getCompanyId().toString());

        return new SignInDTOResponse(jwt);
    }

    public GetCompanyDTOResponse get(String companyId) throws CompanyNotFoundException { 
        Optional<CompanyModel> companyModel = this.companyRepository.findById(UUID.fromString(companyId));

        return new GetCompanyDTOResponse(
            companyModel.get().getCompanyId().toString(),
            companyModel.get().getName(),
            companyModel.get().getDescription()
        );
    }

    public UpdateCompanyDTOResponse update(UpdateCompanyDTORequest updateCompanyDTORequest, String companyId) throws CompanyNotFoundException, InvalidCompanyDomainException {
        CompanyDomain companyDomain = CompanyDomain.validate(
			updateCompanyDTORequest.getName(),
			updateCompanyDTORequest.getDescription(),
			updateCompanyDTORequest.getEmail(),
			updateCompanyDTORequest.getPassword()	
		);
        
        CompanyModel companyModel = new CompanyModel(
			UUID.fromString(companyId),
			companyDomain.getName(),
			companyDomain.getDescription(),
			companyDomain.getEmail(),
			companyDomain.getPassword()
		);
    
        CompanyModel updateCompanyModel = this.companyRepository.save(companyModel);

        return new UpdateCompanyDTOResponse(updateCompanyModel.getCompanyId().toString());
    }

    public DeleteCompanyDTOResponse delete(String companyId) throws CompanyNotFoundException {        
        this.companyRepository.deleteById(UUID.fromString(companyId));

        return new DeleteCompanyDTOResponse(companyId);
    }
}
