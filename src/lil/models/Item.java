package src.lil.models;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import src.lil.Enums.ItemType;
import src.lil.common.DBConnection;
import src.lil.models.Order.AlreadyExists;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Item {
	private int id,updated=0;
	private String dominantColor="",image;
	private String type;
	private Double price;
	private Boolean canAddToBouquet=false;
	private List<Item> flowerInItem;
	private ImageView object_image;

	public CheckBox checked;
	public Button flowers_number;


	public Item(){

	}

	public Item(ResultSet rs) throws SQLException{
		//super();
		this.fillFieldsFromResultSet(rs);
	}
	
	public void fillFieldsFromResultSet(ResultSet rs) throws SQLException{
        this.id = rs.getInt("item_Id");
        this.dominantColor = rs.getString("dominant_color");
        this.type = rs.getString("item_type");
        //System.out.println("id= "+this.id);
        this.price = rs.getDouble("item_price");
       // System.out.println("price="+this.price);
        this.updated = rs.getInt("updated");
        this.image = rs.getString("image");
        this.updated=rs.getInt("updated");
        this.canAddToBouquet=rs.getBoolean("canAddToBouquet");
        if(canAddToBouquet==true){
        	this.flowerInItem=getFlowersInItemFromDb(this.id);
        }else {
        	this.flowerInItem=null;
        }
        
        this.checked = new CheckBox();
        this.flowers_number=new Button("Add to boquete");
        flowers_number.setStyle("-fx-background-color: #FFA500");
        if(this.type.equals(ItemType.FLOWER.toString()))
        	this.flowers_number.setVisible(true);
        else
        	this.flowers_number.setVisible(false);
		if(!image.isEmpty()) {
			this.object_image = new ImageView(image);
			this.object_image.setFitHeight(200);
			this.object_image.setFitWidth(200);
		}else
			this.object_image=null;
    }
	
//    public static boolean calcPriceOfCustomItem(Item item,int storeId) throws AlreadyExists { // item id's "1,5,3,100"
//        try (Connection db = DBConnection.getInstance().getConnection();){
//        	for(Item item:item.get)
//	        	PreparedStatement preparedStatement2 = db.prepareStatement("select price from prices where store_id =? and ")
//	            preparedStatement2.setInt(1, newPrice.intValue());
//	            preparedStatement2.setInt(2, storeId);
//	            preparedStatement2.executeUpdate();
//	            db.close();
//	            return true;
//        } catch (Exception e) {
//           	System.out.println(e.getMessage());
//            throw new AlreadyExists();
//        }
//    }
    
	
    public static Item findById(Integer id) throws SQLException, AlreadyExists {
        try (Connection db = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = db.prepareStatement("select * from items where item_Id=?")){
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (!rs.next()) {
                 //   throw new NotFound();
                }
                Item item = new Item(rs);
                return item;
            }catch (Exception e) {
            	System.out.println(e.getMessage());
                throw new AlreadyExists();
            }
        } catch (Exception e) {
        	System.out.println(e.getMessage());
            throw new AlreadyExists();
        }
    }
    //change price of one item in all stores
    public static boolean changePriceAllStores(Integer itemId,Double newPrice) throws AlreadyExists {
        try (Connection db = DBConnection.getInstance().getConnection();
        	PreparedStatement preparedStatement2 = db.prepareStatement("update prices set price =price*"+newPrice+" where item_Id =?")){
            preparedStatement2.setInt(1, itemId);
            preparedStatement2.executeUpdate();
            db.close();
            return true;
        } catch (Exception e) {
           	System.out.println(e.getMessage());
            throw new AlreadyExists();
        }
    }

    public static boolean changePricesInStore(Integer storeId,Double newPrice) throws AlreadyExists { // item id's "1,5,3,100"
        try (Connection db = DBConnection.getInstance().getConnection();
        	PreparedStatement preparedStatement2 = db.prepareStatement("update prices set price = price*? where store_id =?")){
            preparedStatement2.setInt(1, newPrice.intValue());
            preparedStatement2.setInt(2, storeId);
            preparedStatement2.executeUpdate();
            db.close();
            return true;
        } catch (Exception e) {
           	System.out.println(e.getMessage());
            throw new AlreadyExists();
        }
    }
    
    public static boolean delete(Integer id) throws SQLException, AlreadyExists {
        try (Connection db = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = db.prepareStatement("delete from items where item_Id = ?")) {
            preparedStatement.setInt(1, id);
        	preparedStatement.executeUpdate();
        	db.close();
        } catch (Exception e) {
        	System.out.println(e.getMessage());
            throw new AlreadyExists();
        }
        try (Connection db2 = DBConnection.getInstance().getConnection();
        		PreparedStatement preparedStatement2 = db2.prepareStatement("delete from prices where item_Id = ?")) {
               preparedStatement2.setInt(1, id);
           	preparedStatement2.executeUpdate();
           	db2.close();
           	return true;
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	throw new AlreadyExists();
        }
        
    }
    
    public boolean insert() throws SQLException, AlreadyExists{
    	int itemId=0;
    	String list="";
        try (Connection db = DBConnection.getInstance().getConnection();
                PreparedStatement preparedStatement = db.prepareStatement("insert into items (item_type,dominant_color,item_price,image,updated,canAddToBouquet,flowersInItem) values (?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, this.type);
        	preparedStatement.setString(2, this.dominantColor);
        	preparedStatement.setDouble(3, this.price);
        	preparedStatement.setString(4, this.image);
        	preparedStatement.setInt(5, this.updated);
        	preparedStatement.setBoolean(6, this.canAddToBouquet);
        	preparedStatement.setString(7, itemsToString());
        	preparedStatement.executeUpdate();
        	db.close();
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	System.out.println("insert ");
        	return false;

           // throw new AlreadyExists();
        }
        try (Connection db1 = DBConnection.getInstance().getConnection();
        ResultSet rs1= db1.prepareStatement("select max(item_Id) from items").executeQuery()){
            if (rs1.next()) {
            	itemId = rs1.getInt(1);
            }
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	System.out.println("max ");
        	return false;

           // throw new AlreadyExists();
        }
        this.id=itemId;
        try (Connection db3 = DBConnection.getInstance().getConnection();
        ResultSet rs= db3.prepareStatement("select distinct store_id from prices").executeQuery()){
	        while(rs.next()) {
	        	int fuken=this.price.intValue();
	        	list += "("+rs.getInt(1)+","+fuken+","+itemId+"),";
	        }
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	System.out.println("distinct ");
        	return false;

        //	throw new AlreadyExists();
        }
	        list = list.substring(0, list.length()-1) + ";";

	        System.out.println(list);
	        try (Connection db2 = DBConnection.getInstance().getConnection();
	        		PreparedStatement preparedStatement2 = db2.prepareStatement("INSERT INTO prices(store_id, price, item_id) values"+list)) {
	               //preparedStatement2.setString(1, list);
	           	preparedStatement2.executeUpdate();
	           	db2.close();
	        } catch (Exception e) {
	        	System.out.println(e.getMessage());
	        	System.out.println("insert price ");
	        	//throw new AlreadyExists();
	        	return false;
	        }
	        return true;
    } 
    public static boolean update(Item item) throws SQLException, AlreadyExists{
        try (Connection db = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = db.prepareStatement("UPDATE items SET item_type=? ,dominant_color=?,item_price=? , image=? ,updated=? WHERE item_Id=?")) {
        	preparedStatement.setString(1, item.type.toString());
        	preparedStatement.setString(2, item.dominantColor);
        	preparedStatement.setDouble(3, item.price);
        	preparedStatement.setString(4, item.image);
        	preparedStatement.setInt(5, 1);
        	preparedStatement.setInt(6, item.id);
        	preparedStatement.executeUpdate();
        	db.close();
        	return true;
        } catch (Exception e) {
        	System.out.println(e.getMessage());
            throw new AlreadyExists();
        }
    }
    // recieves item id and returns arrayList of all the flowers in it
    public static List<Item> getFlowersInItemFromDb(int itemId){
    	String flowers=getFlowersInItemIdFromDb(itemId);
		System.out.println("flowers= "+flowers);
		//String s="select * from items where item_Id in "+flowers;
		//System.out.println(s);
    	List<Item> items=new ArrayList<Item>();
        try (Connection db = DBConnection.getInstance().getConnection();
        		ResultSet rs = db.prepareStatement("select * from items where item_Id in "+flowers).executeQuery()){
                while (rs.next()) {
                    Item item = new Item(rs);
                	items.add(item);
                }
        } catch (Exception e) {
            	System.out.println(e.getMessage());
            	return null;
        }
        return items;
    }
    
    private String itemsToString() {
    	String str="";
    	if(canAddToBouquet==true) {
    		str += "(";
	    	for(Item item : flowerInItem) {
	    		str+=item.getId()+",";
	    	}
	    	str = str.substring(0, str.length()-1) + ")";
    	}
    	return str;
    }
    
    public static String getFlowersInItemIdFromDb(int itemId) {
    	String flowers="";
        try (Connection db = DBConnection.getInstance().getConnection();
        		ResultSet rs = db.prepareStatement("select flowersInItem from items where item_id="+itemId).executeQuery()){
                if (rs.next()) {
                	flowers = rs.getString(1);
                }
                
        } catch (Exception e) {
            	System.out.println(e.getMessage());
            	return null;
        }
        return flowers;
    }
    
//    public double getItemPrice(int itemId) {
//    	double totalPrice=0;
//    	List<Item> items=new ArrayList<Item>();
//    	items=getFlowersInItemFromDb(itemId);
//        for (Item item : items) { 	
//        	System.out.println(item.getId());
//        	totalPrice+=item.getPrice();
//        }
//        return totalPrice;
//    }
    
    public double getItemPrice() {
    	double totalPrice=0;
    	if(canAddToBouquet==true) {
	        for (Item item : flowerInItem) { 		
	        	totalPrice+=item.getPrice();
	        }
    	}else {
    		totalPrice=price;
    	}
        return totalPrice;
    }
    
    //filters
    public static List<Item> filterItems(String type ,int storeId){
    	List<Item> items=new ArrayList<Item>();
        try (Connection db = DBConnection.getInstance().getConnection();){
        		try(ResultSet rs = db.prepareStatement("SELECT * FROM items WHERE canAddToBouquet ="+type).executeQuery()){
	                while (rs.next()) {
	                    Item item = new Item(rs);
	                    items.add(item); 
	                	
	                }
	              
	            } catch (Exception e) {
	            	  System.out.println(e.getMessage());
	            	 db.close();
	            	  return null;
	            }for(Item item:items) {
	            	int currId=item.getId();
	        		try(ResultSet rs = db.prepareStatement("select price from prices where item_id="+currId+" and store_id="+storeId).executeQuery()){
	        			if(rs.next()) {
	        				item.setPrice(rs.getDouble(1));
	        				
	        			}
		            } catch (Exception e) {
		            	  System.out.println(e.getMessage());
		            	  db.close();
		            	  return null;
		            }   
	            }
	            db.close();
        } catch (Exception e) {
            	System.out.println(e.getMessage());
            	return null;
        }
    	return items;
    }
    	
    
  //  public static boolean update
    public Item (String itemType,String dominantColor, Double itemPrice, String image , Boolean canAddToBouquet) {
    	//this.id = itemId;
    	this.type = itemType;
	    this.dominantColor = dominantColor;
	    this.price = itemPrice;
	    this.updated = 0;
	    this.image = image;
		if(!image.isEmpty()) {
			this.object_image = new ImageView(image);
			this.object_image.setFitHeight(200);
			this.object_image.setFitWidth(200);
		}else
			this.object_image=null;
	    this.canAddToBouquet=canAddToBouquet;
	    this.flowerInItem=new ArrayList<Item>();
    }
    
    
    
    
    public int getId() {
    	return id;
    }

    public void setId(int id) {
    	this.id = id;
    }

    public int getUpdated() {
   		return updated;
   	}

   	public void setUpdated(int updated) {
  		this.updated = updated;
   	}

   	public String getDominantColor() {
   		return dominantColor;
    }

   	public void setDominantColor(String dominantColor) {
    	this.dominantColor = dominantColor;
    }
   	
   	public String getImage() {
   		return image;
    }

   	public void setImage(String image) {
    	this.image = image;
    }

    public  String getType() {
    	return type;
    }

    public void setType(String type) {
    	this.type = type;
    }

    public Double getPrice() {
    	return price;
    }

    public void setPrice(Double price) {
    	this.price = price;
    }
    public void addItem(Item item) {
    	flowerInItem.add(item);
    	price+=item.getPrice();
    }

    public CheckBox getChecked(){
		return this.checked;
	}
    public boolean is_custom() {
    	return canAddToBouquet;
    }
	public void setChecked(CheckBox new_checked){
		this.checked=new_checked;
	}
	public Button getFlowers_number(){ return this.flowers_number; }
	public void setFlowers_number(Button btn){this.flowers_number=btn;}
	public void setObject_image(String imagev){
		this.object_image=new ImageView(imagev);
	}
	public List<Item> getFlowersInItem(){
		return this.flowerInItem;
	}

	public ImageView getObject_image(){
		return this.object_image;
	}
    public void removeItem(Item item) {
         for (Item i : flowerInItem) { 		      
        	 if(item.getId()==i.getId()) {
        		 flowerInItem.remove(i);
        	 }
         }
    }
}
