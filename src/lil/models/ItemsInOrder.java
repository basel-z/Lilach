//package src.lil.models;
//import src.lil.Enums.*;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.StringTokenizer;
//
//import src.lil.common.DBConnection;
//import src.lil.controllers.OrderServices;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import src.lil.models.*;
//import src.lil.models.Order.AlreadyExists;
//
//public class ItemsInOrder extends Order {
//
//	public Order order;
//	public Item items;
//	
//	private int order_id,item_id;
//	private ItemType item_type;
//	private CustomItemType custom_item_type;
//	private String price_Domian, Dominant_Color;
//	private int quantity;
//	private double Total_price; 
//	
//	
//	
//	public ItemsInOrder(ItemType item_type, CustomItemType custom_item_type, String price_Domian, String Dominant_Color, int quantity, double Total_price) {
//		
//		this.item_type = item_type;
//		this.custom_item_type = custom_item_type;
//		this.price_Domian = price_Domian;
//		this.Dominant_Color = Dominant_Color;
//		this.quantity = quantity;
//		this.Total_price = Total_price;
//	}
//	
//	
//    public boolean insert() throws SQLException, AlreadyExists{
//    	int itemId=0;
//    	String list="";
//        try {Connection db = DBConnection.getInstance().getConnection();
//             PreparedStatement preparedStatement = db.prepareStatement("insert into itemsInOrder (item_type,custom_item_type,item_id,price_Domian,Dominant_Color,quantity,Total_price) values (?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS) ;{
//            preparedStatement.setString(1, this.item_type.toString());
//        	preparedStatement.setString(2, this.custom_item_type.toString());
//        	preparedStatement.setInt(3, getItemId());
//        	preparedStatement.setString(4, getPriceDom());
//        	preparedStatement.setString(5, getDominantColor());
//        	preparedStatement.setInt(6, getQuntity());
//        	preparedStatement.setDouble(5, getPrice());
//        	preparedStatement.executeUpdate();
//        	db.close();
//        	return true;
//                }
//        } catch (Exception e) {
//        	System.out.println(e.getMessage());
//        	return false;
//
//           // throw new AlreadyExists();
//        }
//    }
//    
//
//    private int getItemId() {
//		
//    	return 0;
//	}
//
//
//	public ItemType getType() {
//    	return item_type;
//    	
//    }
//    public double getPrice() {
//    	return Total_price;
//    }
//    public String getPriceDom() {
//    	return price_Domian;
//    }
//    public String getDominantColor() {
//    	return Dominant_Color;
//    }
//    public int getQuntity() {
//    	return quantity;
//    }
//    
//    
//    
//    
//
//}
