package com.emsapi.dtos.employee;

public class UpdateEmployeeDTOResponse {
    private String employeeId;

    public UpdateEmployeeDTOResponse(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
