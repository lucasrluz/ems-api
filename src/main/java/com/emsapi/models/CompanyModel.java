package com.emsapi.models;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "_company")
public class CompanyModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID companyId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

    @OneToMany(mappedBy = "companyModel")
    private Set<EmployeeModel> employeeModels;

    public CompanyModel() {}

    public CompanyModel(String name, String description, String email, String password) {
        this.name = name;
        this.description = description;
        this.email = email;
        this.password = password;
    }

    public CompanyModel(UUID companyId, String name, String description, String email, String password) {
        this.companyId = companyId;
        this.name = name;
        this.description = description;
        this.email = email;
        this.password = password;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<EmployeeModel> getEmployeeModels() {
		return employeeModels;
	}

	public void setEmployeeModels(Set<EmployeeModel> employeeModels) {
		this.employeeModels = employeeModels;
	}
}
