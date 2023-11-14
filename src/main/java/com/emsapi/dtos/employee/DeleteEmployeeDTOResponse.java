package com.emsapi.dtos.employee;

public class DeleteEmployeeDTOResponse {
	private String employeeId;

	public DeleteEmployeeDTOResponse(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
}
