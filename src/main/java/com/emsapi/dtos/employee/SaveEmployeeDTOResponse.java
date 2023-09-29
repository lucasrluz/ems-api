package com.emsapi.dtos.employee;

public class SaveEmployeeDTOResponse {
    private String employeeId;

    public SaveEmployeeDTOResponse(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
