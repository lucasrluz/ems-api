package com.emsapi.models;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "_role")
public class RoleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID roleId;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "roleModel")
    private Set<EmployeeModel> employeeModels;

    public RoleModel() {}

    public RoleModel(String name, Set<EmployeeModel> employeeModels) {
        this.name = name;
        this.employeeModels = employeeModels;
    }

    public RoleModel(UUID roleId, String name, Set<EmployeeModel> employeeModels) {
        this.roleId = roleId;
        this.name = name;
        this.employeeModels = employeeModels;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<EmployeeModel> getEmployeeModels() {
        return employeeModels;
    }

    public void setEmployeeModels(Set<EmployeeModel> employeeModels) {
        this.employeeModels = employeeModels;
    }
}
