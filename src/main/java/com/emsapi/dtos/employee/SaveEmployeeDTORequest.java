package com.emsapi.dtos.employee;

public class SaveEmployeeDTORequest {
    private String firstName;
    private String lastName;
    private String age;
    private String address;
    private String email;
    private String companyId;
    private String roleId;
    
    public SaveEmployeeDTORequest() {}

    public SaveEmployeeDTORequest(String firstName, String lastName, String age, String address, String email, String companyId, String roleId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.address = address;
        this.email = email;
        this.companyId = companyId;
        this.roleId = roleId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
