package com.emsapi.dtos.company;

public class UpdateCompanyDTORequest {
    private String name;
    private String description;
	private String email;
	private String password;
    
    public UpdateCompanyDTORequest(String name, String description, String email, String password) {
        this.name = name;
        this.description = description;
		this.email = email;
		this.password = password;
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
}
