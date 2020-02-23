package src.lil.client.lilachgui;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import src.lil.client.Instance;
import src.lil.models.Item;
import src.lil.models.User;

public class ManageStoreCont extends LilachController {
	@FXML
	TableView<Item> menue_tableview;

	@FXML
	private TextField type_sp_id;

	@FXML
	private Text percentage_text;

	@FXML
	TableColumn<Item, String> id_cul;

	@FXML
	TableColumn<Item, String> name_cul;

	@FXML
	TableColumn<Item, String> price_cul;

	@FXML
	TableColumn<Item, String> checkbox_cul;

	@FXML
	private Button update_btn;

	@FXML
	private Button update_all_btn;


	
	@FXML
	void handle_update_all(MouseEvent event) {
		if(check_sale_input() == false) {
			return;
		}
		try {
			Instance.resetResponse();
			Instance.getClientConsole().get_client().sendToServer("items Update all in :" + ((User)Instance.getCurrentUser()).getStoreId() + ":" + type_sp_id.getText());
			while(Instance.getResponse()==null) {
				System.out.println("Loading...");
			}
			if(Instance.getResponse().equals("successful.")) {
				display_items();
				percentage_text.setFill(Color.BLACK);
				percentage_text.setText("Successful");

			}else {
				System.out.println("ERROR!");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void handle_update_selected(MouseEvent event) {
		if(check_sale_input() == false) {
			return;
		}
		List<Integer> items_to_update = getCheckedItems();
		if(items_to_update.size()==0) {
			percentage_text.setFill(Color.RED);
			percentage_text.setText("select atleast one item!" );
			return;
		}
		Gson gson = new Gson();
		String json = gson.toJson(items_to_update);
		try {
			
			Instance.resetResponse();
			Instance.getClientConsole().get_client().sendToServer("item update :" + ((User)Instance.getCurrentUser()).getStoreId() + ":" + json + ":" + type_sp_id.getText());
			while(Instance.getResponse()==null) {
				System.out.println("Loading...");
			}
			if(Instance.getResponse().equals("successful.")) {
				percentage_text.setFill(Color.BLACK);
				percentage_text.setText("Successful");
				display_items();
			}else {
				System.out.println("ERROR!");
			}
		
		}catch (Exception e) {
			e.printStackTrace();
		}
	}




	@FXML
	public void initialize() {
		this.check_logins();
		display_items();
	}

	@FXML
	boolean check_sale_input() {
		if (type_sp_id.getText().matches("[0-9]+") && type_sp_id.getText().length() <= 2) {
			percentage_text.setText("");
			return true;
			
		} else {
			percentage_text.setFill(Color.RED);
			percentage_text.setText("Enter number between 1-99");
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public ObservableList<Item> getItems(List<Item> getFromDB) {
		ObservableList<Item> items = FXCollections.observableArrayList();

		for (Item item : getFromDB) {
			
			Item toItem = item;

			toItem.checked.setVisible(true);

		}

		items.addAll(getFromDB);

		return items;
	}

	public List<Integer>getCheckedItems(){
		List<Integer> items_to_update = new ArrayList<Integer>();
		for(Object item : menue_tableview.getItems()){
			Item toItem = (Item)item;
			if(toItem.getChecked().isSelected())
				items_to_update.add(toItem.getId());
				toItem.setChecked(new CheckBox());
		}
		return items_to_update;
	}
	
	public void display_items() {
		List<Item> items;
		items = Item.filterItems("false", Integer.parseInt(((User)Instance.getCurrentUser()).getStoreId()));
		id_cul.setCellValueFactory(new PropertyValueFactory<>("id"));
		name_cul.setCellValueFactory(new PropertyValueFactory<>("type"));
		price_cul.setCellValueFactory(new PropertyValueFactory<>("price"));
		checkbox_cul.setCellValueFactory(new PropertyValueFactory<>("checked"));
		menue_tableview.setItems(getItems(items));
		

	}
}
