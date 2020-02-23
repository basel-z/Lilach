package src.lil.models;

import src.lil.Enums.Role;
import src.lil.common.DBConnection;
import src.lil.common.sendMail;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class customerService extends Employee {
    private List<Complain> complains;
    public customerService(int userId, String name, String phone, String email, String password,  String bankAccount) throws Order.NotFound, IOException {
        super(userId, name, phone, bankAccount,email, password, Role.customerService,"-1",-1.0);
        this.complains = viewComplains();
    }
    public customerService(ResultSet rs) throws Exception {
    	super(rs);
    	this.complains = viewComplains();
    }
    public customerService(){super();}
    public List<Complain> viewComplains() throws Order.NotFound, IOException {
        String contact_email,contact_phone,complain_title,complain_text,store_adress,sql,order_Id, user_id;
        int complain_id;
        Date date;
        // TODO Auto-generated method stub
        List<Complain> complainList = new ArrayList<>();
        try{
            Connection conn = DBConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            sql = "SELECT * FROM complains where complain_closed=0";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                complain_id=rs.getInt("complain_id");
                contact_email=rs.getString("contact_email");
                contact_phone=rs.getString("contact_phone");
                complain_title=rs.getString("complain_title");
                complain_text=rs.getString("complain_text");
                store_adress=rs.getString("store_adress");
                order_Id=rs.getString("order_Id");
                user_id=rs.getString("client_id");
                date=rs.getDate("complain_date");
                complainList.add(new Complain(complain_id,contact_email,contact_phone, complain_title, complain_text, store_adress, date,order_Id,user_id));
//		    	System.out.println("Complain date: "+date.toString()+" Complain ID: " + complain_id + " Contact email: " + contact_email + " Contact phone: " + contact_phone + " Store adress: "+store_adress+"\nTitle: " + complain_title+"\nComplain: "+complain_text);
            }
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se) {
            se.printStackTrace();
        }
        complainList.sort(Comparator.comparing(o -> o.getDate()));
        return complainList;
    }

    private Order findOrderById(Complain complain) throws SQLException, Order.NotFound {
        try {Connection db = DBConnection.getInstance().getConnection();
            Statement statment = db.createStatement();
            ResultSet rs = statment.executeQuery("select * from orders where order_id ="+ complain.getOrder_Id());
            rs.next();
            Order order = new Order();
            order.fillFieldsFromResultSet(rs);
            return order;
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private  Client getClientById(int userId) {
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

    public boolean replyComplain(Complain complain,String reply_text, double refund){
        try{
            ArrayList<String> email = new ArrayList<>();
            email.add(complain.getContact_email());
            Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement updateUserQuery = connection.prepareStatement("UPDATE complains SET reply_text=?, refund=?,complain_closed=1 WHERE complain_id=?");
                updateUserQuery.setString(1,reply_text);
                updateUserQuery.setString(2, String.valueOf(refund));
                updateUserQuery.setInt(3,complain.getComplain_id());
                updateUserQuery.executeUpdate();
                connection.close();
                if(refund <1) {
                    String userId = complain.getUser_id();
                    if(userId != null) {
                        Client client = this.getClientById(Integer.parseInt(userId));
                        assert client != null;
                        client.pay(refund);
                        String text = "Hello :)\n Our costumer service had resolve your complain\n Costumer service reply:\n"+reply_text+"\n You have got refund: " + String.valueOf(refund) +"\n Thank you and best regards\n Lilach Ltd";
                        new sendMail(email,"Complain " + complain.getComplainId() + " Closed", text);
                    }
                }
            String text = "Hello :)\n Our costumer service had resolve your complain\n Costumer service reply:\n\n"+reply_text+"\n\n\n Thank you and best regards\n Lilach Ltd";
            new sendMail(email,"Complain " + complain.getComplainId() + " Closed", text);


            return true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public List<Complain> getComplains() {
        return complains;
    }

    public Complain getComplainById(int id){
        try{
            Complain complain = null;
            String contact_email,contact_phone,complain_title,complain_text,store_adress,order_Id, user_id;
            int complain_id;
            Date date;
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stmt = connection.createStatement();
            String sql  = "select * from  complains  WHERE complain_id=" +String.valueOf(id);
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                complain_id=rs.getInt("complain_id");
                contact_email=rs.getString("contact_email");
                contact_phone=rs.getString("contact_phone");
                complain_title=rs.getString("complain_title");
                complain_text=rs.getString("complain_text");
                store_adress=rs.getString("store_adress");
                order_Id=rs.getString("order_Id");
                user_id=rs.getString("client_id");
                date=rs.getDate("complain_date");
                complain = new Complain(complain_id,contact_email,contact_phone, complain_title, complain_text, store_adress, date,order_Id,user_id);
            }
            connection.close();
            return complain;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    @Override
    public String toString(){
            return "customerService ["+to_String();
    }
}
