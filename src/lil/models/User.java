package src.lil.models;

import src.lil.Enums.Role;
import src.lil.common.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class User {
	protected int userId;
	protected String name;
	protected String phone;
	protected String bankAccount;
	protected boolean isBlocked;
	protected String email;
	protected String password;
	protected String storeId;
	protected double balance;

	public User() {
	}

	public User(int userId, String name, String phone, String bankAccount, String email, String password,
			String storeId, double balance) {
		this.userId = userId;
		this.name = name;
		this.phone = phone;
		this.bankAccount = bankAccount;
		this.email = email;
		this.password = password;
		this.isBlocked = false;
		this.storeId = storeId;
		this.balance = balance;
	}

	public String to_String() {
		return "user_id=" + this.userId + ",name=" + this.name + ",phone=" + this.phone + ",bank_acc="
				+ this.bankAccount + ",email=" + this.email + ",password=" + this.password + ",block=" + this.isBlocked
				+ ",store_id=" + this.storeId + ",balance=" + this.balance + "]";
	}

	public abstract boolean register() throws Exception;

	public Role getRole() {
		return Role.Client;
	}

	public String getName() {
		return name;
	}

	public int getUserId() {
		return userId;
	}

	public String getPhone() {
		return phone;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public String getEmail() {
		return email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public boolean getIsConnected() {
		return isBlocked;
	}

	public String getStoreId() {
		return storeId;
	}

	public double getBalance() {
		return balance;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean blocked) {
		isBlocked = blocked;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public void setStoreId(String storeId) {
		this.storeId= storeId;
	}
	public void setBalance(Double _balance) {
		this.balance = _balance;
	}
	public boolean pay(double amount) {
		try {
			Connection connection = DBConnection.getInstance().getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT client_balance  FROM clients WHERE client_id=" + userId);
			rs.next();
			double balance = rs.getDouble("client_balance");
			balance = balance - amount;
			PreparedStatement updateUserQuery = connection.prepareStatement("UPDATE clients SET client_balance = ? , client_block = ? WHERE client_id = ?");
			updateUserQuery.setDouble(1,balance);
			updateUserQuery.setBoolean(2,false);
			updateUserQuery.setInt(3,userId);
			updateUserQuery.executeUpdate();
			connection.close();
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	public static List<Client> get_all_clients() {
		List<Client> clients = new ArrayList<Client>();
		try {
			Connection db = DBConnection.getInstance().getConnection();
			PreparedStatement pstmt = db.prepareStatement("SELECT * FROM clients");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				clients.add(new Client(rs));
			}
			db.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return clients;
	}

	public static List<Object> get_all_employees() throws Exception {
		List<Object> employees = new ArrayList<Object>();
		try {
			Connection db = DBConnection.getInstance().getConnection();
			PreparedStatement pstmt = db.prepareStatement("SELECT * FROM users");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if (!rs.getString("user_role").equals("ChainManger")) {
					if (rs.getString("user_role").equals("Employee")) {
						employees.add(new Employee(rs));
					} else if (rs.getString("user_role").equals("customerService")) {
						employees.add(new customerService(rs));
					} else {
						employees.add(new StoreManger(rs));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employees;
	}
}
