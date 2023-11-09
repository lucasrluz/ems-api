package com.emsapi.util;

import com.emsapi.dtos.employee.UpdateEmployeeDTORequest;

public abstract class UpdateEmployeeDTORequestBuilder {
    public static UpdateEmployeeDTORequest createWithValidData(String roleId, String companyId) {
        return new UpdateEmployeeDTORequest("bar", "foo", "22", "9809 Margo Street", companyId, "barfoo@gmail.com", roleId);
    }

	public static UpdateEmployeeDTORequest createWithEmptyFirstName(String roleId, String companyId) {
		return new UpdateEmployeeDTORequest("", "foo", "22", "9809 Margo Street", companyId, "barfoo@gmail.com", roleId);
	}

	public static UpdateEmployeeDTORequest createWithEmptyLastName(String roleId, String companyId) {
		return new UpdateEmployeeDTORequest("bar", "", "22", "9809 Margo Street", companyId, "barfoo@gmail.com", roleId);
	}

	public static UpdateEmployeeDTORequest createWithEmptyAge(String roleId, String companyId) {
		return new UpdateEmployeeDTORequest("bar", "foo", "", "9809 Margo Street", companyId, "barfoo@gmail.com", roleId);
	}

	public static UpdateEmployeeDTORequest createWithEmptyAddress(String roleId, String companyId) {
		return new UpdateEmployeeDTORequest("bar", "foo", "22", "", companyId, "barfoo@gmail.com", roleId);
	}

	public static UpdateEmployeeDTORequest createWithEmptyEmail(String roleId, String companyId) {
		return new UpdateEmployeeDTORequest("bar", "foo", "22", "9809 Margo Street", companyId, "", roleId);
	}

	public static UpdateEmployeeDTORequest createWithInvalidEmailFormat(String roleId, String companyId) {
		return new UpdateEmployeeDTORequest("bar", "foo", "22", "9809 Margo Street", companyId, "@gmail.com", roleId);
	}
}
