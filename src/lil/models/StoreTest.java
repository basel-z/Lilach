//package src.lil.models;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.Assert;
//import org.junit.jupiter.api.Test;
//
//import junit.framework.TestCase;
//import src.lil.exceptions.NoItemWithId;
//import src.lil.models.Order.AlreadyExists;
//
//class StoreTest extends TestCase {
//
////	Store my_store;
////	List<Integer> update = new ArrayList<Integer>();
////
////	protected void setUp() throws Exception {
////		my_store.get_store_by_id(1);
////		update.add(3);
////	}
//
//	@Test
////	void test_store_constructor() throws SQLException, AlreadyExists {
////		Store my_new_store;
////		try {
////			my_new_store = new Store(3, "Jacobs");
////		} catch (AlreadyExists e) {
////			System.out.println("already exists");
////		}
////		try {
////			my_new_store = new Store(3, "Jacobs");
////		} catch (AlreadyExists e) {
////			System.out.println("already exists");
////		}
////	}
////	
////	@Test
////	void test_store_fetch() throws SQLException {
////		Store mystore = new Store();
////		try {
////			mystore.get_store_by_id(4);
////		} catch (NotFound e) {
////			System.out.println("No store found");
////		}
////		try {
////			mystore.get_store_by_id(1);
////			System.out.println(mystore.get_address() + " id:" + mystore.get_id());
////		} catch (Exception e) {
////			System.out.println("Not found");
////		}
////	}
//
////	void test_change_all_prices() throws SQLException, NotFound {
////		Store newstore = new Store();
////		newstore.get_store_by_id(1);
////		System.out.println(newstore.get_id());
////		try {
////			Assert.assertTrue(newstore.change_price_all_items(10));
////		} catch (SQLException e) {
////			e.printStackTrace();
////		}
////	}
////	
////	void test_change_specific() throws SQLException, NotFound, NoItemWithId {
////		Store newstore = new Store();
////		newstore.get_store_by_id(1);
////		List<Integer> update = new ArrayList<Integer>();
////		update.add(3);
////		newstore.get_store_items();
////		try {
////			Assert.assertTrue(newstore.update_items_price(update, 10));
////		} catch (SQLException | NoItemWithId e) {
////			e.printStackTrace();
////		}
////	}
//	
//}
