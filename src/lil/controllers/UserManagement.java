package src.lil.controllers;

import src.lil.Enums.ReportType;
import src.lil.Enums.Role;
import src.lil.models.Client;
import src.lil.models.Employee;

public interface UserManagement {
    public void viewReport(ReportType reportType);
    public boolean updateUser(Object updatedUser);
    public boolean setUserAbilityToOrder(int userId, boolean isBlock);
    public boolean changeRule(int userId, Role role);
    public Employee getEmployeeById(int userId);
    public Client getClientById(int userId);
    public boolean DeleteUser(int userId, boolean isClient);

}
