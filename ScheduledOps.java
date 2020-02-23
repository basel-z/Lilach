import src.lil.common.DBConnection;
import src.lil.models.Client;
import src.lil.models.Complain;
import src.lil.models.Order;
import src.lil.models.User;
import src.lil.common.sendMail;
import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ScheduledOps {
    public static void messageAboutComplain() {
        ArrayList<Complain> complains = null;
        try{
            String contact_email,contact_phone,complain_title,complain_text,store_adress,order_Id, user_id;
            int complain_id;
            Date date;
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM complains where complain_closed=0 and complain_date <= now() - INTERVAL 1 DAY";
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
                complains.add(new Complain(complain_id,contact_email,contact_phone, complain_title, complain_text, store_adress, date,order_Id,user_id));
            }
            connection.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }
        ArrayList<String> customersEmails = new ArrayList<>();
        if (complains == null) {
            return;
        }
        for (Complain complain : complains) {
            if (complain != null) {
                customersEmails.add(complain.getContact_email());
                        String emailSubject = "Status about complain " + complain.getComplainId();
                        String emailBody =
                                "Hello :)\n We're sorry to tell you that our customers service is still working on your complain  \n\n Thank you and best regards\nLilach LTD";
                        new sendMail(customersEmails,emailSubject,emailBody);
                }
                customersEmails.remove(0);
            }
    }
    public static void sendAboutDelivery(){
        try {Connection db =DBConnection.getInstance().getConnection();
            ArrayList<String> customersEmails = new ArrayList<>();
            Statement statment = db.createStatement();
            LocalTime localTime = LocalTime.now();
            localTime = localTime.minusMinutes(1);
            localTime = localTime.minusSeconds(localTime.getSecond());
            ResultSet rs = statment.executeQuery("SELECT user_id FROM orders WHERE Shipping_Hour ="+ DateTimeFormatter.ofPattern("HH:mm:ss").format(localTime));
            while(rs.next()){
                ResultSet rs1 = statment.executeQuery("SELECT client_email FROM clients WHERE client_id ="+ rs.getString("user_id"));
                rs1.next();
                customersEmails.add(rs1.getString("client_email"));
                String emailSubject = "Status about delivery ";
                String emailBody =
                        "Hello :)\n We're happy to tell you that your order have been delivered successfully   \n\n Thank you and best regards\nLilach LTD";
                new sendMail(customersEmails,emailSubject,emailBody);
                customersEmails.clear();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateBalanceForMonth(){
        try {Connection db =DBConnection.getInstance().getConnection();
            Statement statment = db.createStatement();
            statment.executeQuery("Update clients set client_balance = client_balance + 50  WHERE client_subscriptionType = 'Monthly'");
            }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateBalanceForYear(){
        try {Connection db =DBConnection.getInstance().getConnection();
            Statement statment = db.createStatement();
            statment.executeQuery("Update clients set client_balance = client_balance + 300  WHERE client_subscriptionType = 'Yearly'");
            }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
