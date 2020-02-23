package src.lil.controllers;

import java.sql.SQLException;
import java.util.List;

import src.lil.exceptions.NoItemWithId;
import src.lil.models.Item;

public interface StoreInterface {
	public boolean update_items_price(List<Integer> items_id,Integer _sale) throws SQLException, NoItemWithId;
	public void get_store_items() throws SQLException,NoItemWithId;
}
