package src.lil.models;

import src.lil.Enums.ReportType;
import src.lil.Enums.Role;
import src.lil.Enums.SubscriptionType;
import src.lil.common.DBConnection;
import src.lil.controllers.UserManagement;

import java.sql.*;

public class ChainManger extends StoreManger implements UserManagement {

	
    public ChainManger(int userId, String name, String phone, String email, String password,  String bankAccount) {
        super(userId, name, phone, bankAccount,email, password, Role.ChainManger,"-1",-1.0);
    }
    public ChainManger(){super();}
    public ChainManger(ResultSet rs) throws Exception {
    	super(rs);
    }
    @Override
    public String toString() {
    	return "ChainManger ["+to_String();
    }
    @Override
    public void viewReport(ReportType reportType){ //TODO: DEAL WITH REPORT

    }


    @Override
    public boolean updateUser(Object updatedUser) {
        try{
            Connection connection = DBConnection.getInstance().getConnection();

            if(updatedUser instanceof Employee){
                PreparedStatement updateUserQuery = connection.prepareStatement("UPDATE users SET user_email=?, user_password=?,user_name=?,user_role=?,user_bankAccount=?,user_phone=?,store_id=?,user_balance=? WHERE user_id=?");
                updateUserQuery.setString(1,((Employee) updatedUser).getEmail());
                updateUserQuery.setString(2,((Employee) updatedUser).getPassword());
                updateUserQuery.setString(3,((Employee) updatedUser).getName());
                updateUserQuery.setString(4,((Employee) updatedUser).getRole().toString());
                updateUserQuery.setString(5,((Employee) updatedUser).getBankAccount());
                updateUserQuery.setString(6,((Employee) updatedUser).getPhone());
                updateUserQuery.setString(7,((Employee) updatedUser).getStoreId());
                updateUserQuery.setDouble(8,((Employee) updatedUser).getBalance());
                updateUserQuery.setInt(9,((Employee) updatedUser).getUserId());
                updateUserQuery.executeUpdate();
                connection.close();
                return true;
            }
            else if (updatedUser instanceof Client){
                PreparedStatement updateUserQuery = connection.prepareStatement("UPDATE clients SET client_name=?, client_phone=?, client_bankAccount=?, client_email=?,client_password=?,client_creditCard=?,client_shippingAddress=?,client_subscriptionType=?,client_block=?,store_id=?,client_balance=? WHERE client_id=?");
                updateUserQuery.setString(1,((Client) updatedUser).getName());
                updateUserQuery.setString(2,((Client) updatedUser).getPhone());
                updateUserQuery.setString(3,((Client) updatedUser).getBankAccount());
                updateUserQuery.setString(4,((Client) updatedUser).getEmail());
                updateUserQuery.setString(5,((Client) updatedUser).getPassword());
                updateUserQuery.setString(6,((Client) updatedUser).getCreditCardNumber());
                updateUserQuery.setString(7,((Client) updatedUser).getShippingAddress());
                updateUserQuery.setString(8,((Client) updatedUser).getSubscriptionType().toString());
                updateUserQuery.setBoolean(9,((Client) updatedUser).getIsConnected());

                updateUserQuery.setString(10,((Client) updatedUser).getStoreId());
                updateUserQuery.setDouble(11,((Client) updatedUser).getBalance());
                updateUserQuery.setInt(12,((Client) updatedUser).getUserId());

                updateUserQuery.executeUpdate();
                connection.close();
                return true;
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }

    @Override
    public boolean setUserAbilityToOrder(int userId, boolean isBlock) {
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement updateUserQuery = connection.prepareStatement("UPDATE clients SET client_block=? WHERE client_id=?");
            updateUserQuery.setBoolean(1,isBlock);
            updateUserQuery.setInt(2,userId);
            updateUserQuery.executeUpdate();
            connection.close();
            return true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean changeRule(int userId, Role role) {
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement updateUserQuery = connection.prepareStatement("UPDATE users SET user_role=? WHERE user_id=?");
            updateUserQuery.setString(1,role.toString());
            updateUserQuery.setInt(2,userId);
            updateUserQuery.executeUpdate();
            connection.close();
            return true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }


    @Override
    public Employee getEmployeeById(int userId) {
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT *  FROM users WHERE user_id=" + userId);
            rs.next();
            Employee employee = new Employee(rs);
            connection.close();
            return employee;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Client getClientById(int userId) {
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT *  FROM clients WHERE client_id=" + userId);
            rs.next();
            Client client = new Client(rs);
            connection.close();
            return client;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean DeleteUser(int userId, boolean isClient){
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            if (isClient){
                PreparedStatement updateUserQuery = connection.prepareStatement("DELETE FROM clients WHERE client_id=?");
                updateUserQuery.setInt(1,userId);
                updateUserQuery.executeUpdate();
                connection.close();
            }
            else {
                PreparedStatement updateUserQuery = connection.prepareStatement("DELETE FROM users WHERE user_id=?");
                updateUserQuery.setInt(1,userId);
                updateUserQuery.executeUpdate();
                connection.close();
            }
            return true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public String getAllUsersWithBalance(){
        try{
        Connection connection = DBConnection.getInstance().getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT *  FROM clients  WHERE client_balance > 0 and client_block=0");
        StringBuilder result = new StringBuilder();
        while(rs.next()){
            result.append(rs.getString("client_name")).append(" ~ ID: ").append(rs.getString("client_id")).append("-").append(rs.getDouble("client_balance")).append(",");
        }
        result = new StringBuilder(result.substring(0, result.length() - 1));
        connection.close();
        return result.toString();
        }
        catch (Exception e){
        System.out.println(e.getMessage());
        return null;
        }
    }
}
