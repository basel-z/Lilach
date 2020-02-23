package src.lil.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import javafx.util.Pair;
import src.lil.common.DBConnection;
import src.lil.controllers.StoreInterface;
import src.lil.exceptions.NoItemWithId;
import src.lil.models.Order.AlreadyExists;

public class Store implements StoreInterface {
	private Integer store_id;
	private String address;
	private Map<Integer, Item> _items;

	/**
	 * default constructor.
	 */
	public Store() {
		_items = new HashMap<Integer, Item>();
	}

	/**
	 * constructor which adds the store into DataBase.
	 * 
	 * @param _id      the id of the new store
	 * @param _address the address of the new store.
	 * @throws SQLException
	 * @throws AlreadyExists if the an existent store with the same id already in the DB.
	 */
	public Store(Integer _id, String _address) throws SQLException ,AlreadyExists {
		String sql_insert = "INSERT INTO stores(store_id,store_address) VALUES(?,?)";
		String sql_check = "SELECT * FROM stores WHERE store_id = ?";
		try (Connection db = DBConnection.getInstance().getConnection();
				PreparedStatement pstmt1 = db.prepareStatement(sql_check);
				PreparedStatement pstmt = db.prepareStatement(sql_insert);) {
			pstmt1.setInt(1,_id);
			ResultSet rs = pstmt1.executeQuery();
			if(rs.next())
				throw new AlreadyExists();
			pstmt.setInt(1, _id);
			pstmt.setString(2, _address);
			pstmt.executeUpdate();
			db.close();
		}
		_items = new HashMap<Integer, Item>();
	}

	/**
	 * fetches the store information from DB.
	 * 
	 * @param _id the id of the store to fetch
	 * @return new object of an existent store in DATABASE
	 * @throws SQLException
	 * @throws NotFound     in case we insert an id for a store not in DB.
	 */
	public void get_store_by_id(Integer _id) throws SQLException, NotFound {
		String sql_fetch = "SELECT * FROM stores WHERE store_id = ?";
		try (Connection db = DBConnection.getInstance().getConnection();
				PreparedStatement pstmt = db.prepareStatement(sql_fetch);) {
			pstmt.setInt(1,_id);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next())
				throw new NotFound();
			this.set_id(rs.getInt("store_id"));
			this.set_address(rs.getString("store_address"));
			db.close();
		}
	}
	/**
	 * sale on each item 
	 * @param _sale	the sale percentage
	 * @return 
	 * @throws AlreadyExists
	 */
	public  boolean change_price_all_items(Integer _sale) throws SQLException {
		int sale =  (100-_sale);
		try (Connection db = DBConnection.getInstance().getConnection();
				PreparedStatement preparedStatement2 = db.prepareStatement("update prices set price = price * ?  where store_id = ?");
				PreparedStatement pstmt = db.prepareStatement("UPDATE prices SET price = price / ? WHERE store_id = ?");) {
			preparedStatement2.setInt(2, this.store_id);
			preparedStatement2.setInt(1,  100 - sale);
			pstmt.setInt(1,100);
			pstmt.setInt(2, this.store_id);
			preparedStatement2.executeUpdate();
			pstmt.executeUpdate();
			db.close();
			return true;
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	/**
	 * changes the prices of specific items 
	 * new prices is the old price * _sale%.
	 */
	public boolean update_items_price(List<Integer> items_id, Integer _sale) throws SQLException, NoItemWithId {
		String sql_update = "Update prices SET price = price * ? WHERE store_id = ? AND item_id = ?";
		String sql_update2 = "Update prices SET price = price / ? WHERE store_id = ? AND item_id = ?";
		try (Connection db = DBConnection.getInstance().getConnection();) {
			PreparedStatement ps = db.prepareStatement(sql_update);
			PreparedStatement ps2 = db.prepareStatement(sql_update2);
			for (Integer integer : items_id) {
				
				ps.setDouble(1,100- _sale);
				ps.setInt(2, this.store_id);
				ps.setInt(3, integer);
				ps2.setDouble(1, 100);
				ps2.setInt(2, this.store_id);
				ps2.setInt(3, integer);
				ps.executeUpdate();
				ps2.executeUpdate();
				
			}
			db.close();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 	fetches the items with the stores prices from the DATABASE.
	 */
	public void get_store_items() throws SQLException, NoItemWithId {
		try (Connection db = DBConnection.getInstance().getConnection();
				PreparedStatement preparedStatement = db.prepareStatement("SELECT * FROM items");
				ResultSet rs = preparedStatement.executeQuery()) {
			while (rs.next()) {
				this._items.put(rs.getInt("item_Id"), new Item(rs));
			}
			PreparedStatement Statment = db.prepareStatement("SELECT * FROM prices WHERE store_id = ?");
			Statment.setInt(1, this.store_id);
			ResultSet rs2 = Statment.executeQuery();
			while (rs2.next()) {
				if (this._items.containsKey(rs2.getInt("item_id"))) {
					throw new NoItemWithId(rs2.getInt("item_id"));
				}
				this._items.get(rs2.getInt("item_id")).setPrice(Double.valueOf(rs2.getInt("price")));
			}
			db.close();
		}
	}
	/**
	 * fetches the id and the store address from the DATABASE
	 * @return List contains pairs (integer = id ,string = address).
	 */
	public static Map<String,Integer> get_store_addresses(){
		Map<String,Integer> addresses = new HashMap<String,Integer>();
		try{
			Connection db = DBConnection.getInstance().getConnection();
			PreparedStatement pstmt = db.prepareStatement("SELECT * FROM stores");
			ResultSet res = pstmt.executeQuery();
			while(res.next()) {
				addresses.put(res.getString("store_address"),res.getInt("store_id"));
			}
			db.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return addresses;
	}
	/**
	 * 
	 * @param _id
	 */
	public void set_id(Integer _id) {
		this.store_id = _id;
	}

	public void set_address(String _address) {
		this.address = _address;
	}

	public Integer get_id() {
		return this.store_id;
	}

	public String get_address() {
		return this.address;
	}
}
