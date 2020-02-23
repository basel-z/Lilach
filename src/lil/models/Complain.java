package src.lil.models;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import src.lil.common.DBConnection;
import src.lil.controllers.ComplainsInterface;
import src.lil.models.Order.NotFound;

public class Complain implements ComplainsInterface {
	public static int idCount=4;
	private int complain_id;
	private String contact_email;
	private String contact_phone;
	private String complain_title;
	private String complain_text;
	private String store_adress;
	private Date date;
	private String order_Id;
	private String user_id;
	public Complain( String contact_email, String contact_phone, String complain_title, String complain_text, String store_adress, Date date, String order_Id, String user_id){
		this.complain_text = complain_text;
		this.contact_email = contact_email;
		this.contact_phone = contact_phone;
		this.complain_title = complain_title;
		this.store_adress = store_adress;
		this.date = date;
		this.order_Id = order_Id;
		this.user_id = user_id;


	}
	public Complain( String contact_email, String contact_phone, String complain_title, String complain_text, String store_adress, String order_Id, String user_id){
		this.complain_text = complain_text;
		this.contact_email = contact_email;
		this.contact_phone = contact_phone;
		this.complain_title = complain_title;
		this.store_adress = store_adress;
		this.date = new Date(Calendar.getInstance().getTime().getTime());
		this.order_Id = order_Id;
		this.user_id = user_id;


	}
	public Complain(int complain_id, String contact_email, String contact_phone, String complain_title, String complain_text, String store_adress, Date date, String order_Id, String user_id){
		this.complain_text = complain_text;
		this.contact_email = contact_email;
		this.contact_phone = contact_phone;
		this.complain_title = complain_title;
		this.store_adress = store_adress;
		this.date = date;
		this.order_Id = order_Id;
		this.user_id = user_id;
		this.complain_id = complain_id;

	}

	public String getComplain_title() {
		return complain_title;
	}

	public String getOrder_Id() {
		return order_Id;
	}

	public int getComplain_id() {
		return complain_id;
	}

	public Date getDate() {
		return date;
	}

	public String getUser_id() {
		return user_id;
	}

	public String getContact_email() {
		return contact_email;
	}

	public int getComplainId() {
		return complain_id;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void addComplain() throws SQLException, Order.NotFound, IOException {
//		Date date = new Date(Calendar.getInstance().getTime().getTime());
//		java.sql.Timestamp today = new java.sql.Timestamp(new java.util.Date().getTime());
		try (Connection conn = DBConnection.getInstance().getConnection()){
			PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO complains (contact_email,contact_phone,complain_title,complain_text,store_adress,complain_date,complain_closed) VALUES (?, ?, ?, ?, ?, ?, ?)");
			preparedStatement.setString(1, contact_email);
			preparedStatement.setString(2, contact_phone);
			preparedStatement.setString(3, complain_title);
			preparedStatement.setString(4, complain_text);
			preparedStatement.setString(5, store_adress);
			preparedStatement.setTimestamp(6,new Timestamp(new java.util.Date().getTime()));
			preparedStatement.setBoolean(7,false);
			try {
				preparedStatement.executeUpdate();
				System.out.println("Added new complain");
				preparedStatement.close();
				conn.close();
			}catch(SQLException se) {
				se.printStackTrace();
			}
		}catch(SQLException se) {
			se.printStackTrace();
		}
	}

}
