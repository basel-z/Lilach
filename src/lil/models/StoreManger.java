package src.lil.models;

import java.sql.ResultSet;

import src.lil.Enums.ReportType;
import src.lil.Enums.Role;

import java.sql.ResultSet;

public class StoreManger extends Employee {


    public StoreManger(int userId, String name, String phone, String bankAccount, String email, String password, Role role, String store_id, double balance) {
        super(userId, name, phone, bankAccount, email, password, role, store_id, balance);
    }
    @Override
    public String toString() {
    	return "StoreManger [" + to_String();
    }
    public StoreManger(){super();}
    public StoreManger(ResultSet rs) throws Exception {
    	super(rs);
    }
    public void viewReport(ReportType reportType){

    }
}
