package src.lil.models;

import src.lil.Enums.Role;
import src.lil.common.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Employee extends User {
    protected Role role;

    public Employee(int userId, String name, String phone, String bankAccount, String email, String password,  Role role, String storeId, Double balannce) {
        super(userId, name, phone, bankAccount, email, password, storeId,balannce);
        this.role = role;
    }
    @Override
    public String toString() {
    	return "Employee ["+to_String();
    }
    @Override
    public boolean register() throws Exception {
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            //check if exist
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT *  FROM users WHERE user_id=" + userId);
            if(rs.getRow() != 0 ) {
                System.out.println("User already exist");
                return false;
            }
            String SQL_INSERT = "INSERT INTO users (user_id, user_email, user_password, user_name, user_role," +
                    " user_bankAccount, user_phone,store_id, user_balance) VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement updateUserQuery = connection.prepareStatement(SQL_INSERT);
            updateUserQuery.setInt(1,userId);
            updateUserQuery.setString(2,email);
            updateUserQuery.setString(3,password);
            updateUserQuery.setString(4,name);
            updateUserQuery.setString(5,role.toString());
            updateUserQuery.setString(6,bankAccount);
            updateUserQuery.setString(7,phone);
            updateUserQuery.setString(8,storeId);
            updateUserQuery.setDouble(9,balance);
            updateUserQuery.executeUpdate();
            connection.close();
            return true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Employee(ResultSet rs) throws Exception{
        this(rs.getInt("user_id"),rs.getString("user_name"),rs.getString("user_phone"),rs.getString("user_bankAccount"),rs.getString("user_email"),rs.getString("user_password"), Role.valueOf(rs.getString("user_role")),rs.getString("store_id"),rs.getDouble("user_balance"));
    }

    public Employee(){super();}

    public boolean updateItem(Item item){
        try{
                Connection connection = DBConnection.getInstance().getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("insert into items (id,type,dominant_color,price,updated) values (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, item.getId());
                preparedStatement.setString(2, item.getType().toString());
                preparedStatement.setString(3, item.getDominantColor());
                preparedStatement.setDouble(4, item.getPrice());
                preparedStatement.setInt(5, 1);
                connection.close();
                return true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    @Override
    public Role getRole(){
        return this.role;
    }

    public void setRole(Role role){
        this.role = role;
    }

    public boolean equals (Object obj) {
        if (this==obj) return true;
        Employee emp = (Employee) obj ;
        return this.name.equals(emp.getName()) && this.role.equals(emp.getRole())
                && this.bankAccount.equals(emp.getBankAccount())&& this.email.equals(emp.getEmail())
                && this.password.equals(emp.getPassword())&& this.phone.equals(emp.getPhone())
                && this.userId == (emp.getUserId()) && this.isBlocked == (emp.getIsConnected());
    }
}