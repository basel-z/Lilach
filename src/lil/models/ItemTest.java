//package src.lil.models;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.jupiter.api.Test;
//
//import src.lil.Enums.ItemType;
//
//class ItemTest {
////    public static void main(String[] args) {
////    	try {
////			addItems();
////		} catch (Exception e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////
////	Item getFromDb = new Item(ItemType.BOUQUET,"",0.0);
////	@Test
////    public void findItem() throws Exception{
////    	Item getFromDb =  Item.findById(6);
////        if(getFromDb==null) {System.out.println("Failed");}
////    }
////	@Test
////    public void findItem() throws Exception{
////    	Item getFromDb =  Item.findById(6);
////        if(getFromDb==null) {System.out.println("Failed");}
////    }
////	@Test
////    public void updateItem() throws Exception{
//////		getFromDb.setId(6);
////		getFromDb.setPrice(50.0);
//////		getFromDb.setDominantColor("");
//////		getFromDb.setType(ItemType.BOUQUET);
//////		getFromDb.setUpdated(1);
////    	boolean updated = Item.update(getFromDb);
////        if(!(updated)) {System.out.println("Failed");}
////    }
////	@Test
////    public void deleteItem() throws Exception{
////    	boolean deleted = Item.delete(8);
////        if(!(deleted)) {System.out.println("Failed");}
////    }
////	@Test
////    public void findItem() throws Exception{
////   	Item getFromDb =  Item.findById(2);
////   	System.out.println(getFromDb.getType());
////   	if(getFromDb==null) System.out.println("Failed");}
////
////	@Test
////    public void changePriceAllStores() throws Exception{
////		System.out.println(Item.changePriceAllStores(2, 0.8));
////   	//System.out.println(getFromDb.getType());
////   	//if(getFromDb==null) {System.out.println("Failed");}
////  }
////	@Test
////    public void changePriceAllItems() throws Exception{
////		System.out.println(Item.changePriceAllItems(1, 0.8));
////   	//System.out.println(getFromDb.getType());
////   	//if(getFromDb==null) {System.out.println("Failed");}
////  }
//
//
////	@Test
////    public void insettest() throws Exception{
////	Item newItem= new Item(ItemType.BOUQUET,"RED",100.0,"");
////	newItem.insert();
////	}
////    }
////	public static void addItems() throws Exception{
////		Item newItem1= new Item(ItemType.CUSTOM,"",0.0,"",true);
////		Item newItem2= new Item(ItemType.FLOWER,"blue",10.0,"",false);
////		Item newItem3= new Item(ItemType.FLOWER,"blue",10.0,"",false);
////		//System.out.println("insert = "+newItem2.insert()+"\n");
////		System.out.println("insert = "+newItem3.insert());
////		System.out.println("insert = "+newItem2.insert());
////
////		newItem1.addItem(newItem2);
////		newItem1.addItem(newItem3);
////		System.out.println("insert = "+newItem1.insert());
////		System.out.println("item id="+newItem1.getId());
////		//System.out.println("items in item);
////		System.out.println("total price"+newItem1.getItemPrice());
////		System.out.println("total price"+newItem2.getItemPrice());
////		List<Item> items=new ArrayList<Item>();
////		items=Item.filterItems(" <> 'CUSTOM'",1); //this means IS NOT CUSTOM
////		for(Item i:items) {
////			System.out.println(i.getPrice());
////		}
//		//System.out.println("item id="+filterItems())
//
//		//System.out.println("total price"+newItem1.getItemPrice(newItem1.getId())+"\n");
////	}
//
//}
//
