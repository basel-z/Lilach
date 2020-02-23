package src.lil.client.lilachgui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import src.lil.client.Instance;
import src.lil.models.Item;
import src.lil.models.Order;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class MenuController extends LilachController{

	public Item boque_items;
	ObservableList<String>combo_list=FXCollections.observableArrayList("White","Red","Blue");

	private Item tmp_item=new Item();

	@FXML
	private Label added_label;

	@FXML
	private TextField type_field;

	@FXML
	private TextField dominant_field;

	@FXML
	private TextField price_field;

	@FXML
	private TextField image_field;

	@FXML
	private CheckBox can_boquet_box;

	@FXML
	private Button add_item;
	@FXML
	private ComboBox<String> combo_box;
	@FXML
	private ImageView go_btn;
	@FXML
	private Label finish_order_label;
	@FXML
	private TableView<Item> menue_tableview;

	@FXML
	private TableColumn<Item, Button> create_boque_cul;

	@FXML
	private Button creat_boque_btn;

	@FXML
	private AnchorPane anchor_bar;

	@FXML
	private TableColumn<Item, String> id_cul;

	@FXML
	private TableColumn<Item, ImageView> pic_cul;

	@FXML
	private TableColumn<Item, Double> price_cul;

	@FXML
	private TableColumn<Item, String> type_cul;

	@FXML
	private TableColumn<Item,CheckBox> checkbox_cul;


	@FXML
	private Button add_cart_order;

	@FXML
	private TableColumn<Item,String>Type_cul;

	@FXML
	private TextField to_field;

	@FXML
	private TextField from_field;

	private FilteredList<Item> filtered_list;

	private ObservableList<Item>original_list;

	@FXML
	private Button add_item_btn;


	@FXML
	private Button delete_item_btn;
	@FXML
	private Pane add_popup;

	@FXML
	private Circle popup_close;

	@FXML
	void handle_order_cart(MouseEvent event) throws IOException {
		List<Item>tmp =getCheckedItems();
		selected_items.addAll(tmp);

	}

	@FXML
	public void initialize() {
		this.check_logins();
		displayItems();
	}


	private void displayItems() {
//		Gson gson=new Gson();
		List<Item> items;
		items=Item.filterItems("false",1);
		combo_box.setValue("None");
		combo_box.setItems(combo_list);
		id_cul.setCellValueFactory(new PropertyValueFactory<>("id"));
		pic_cul.setCellValueFactory(new PropertyValueFactory<Item,ImageView>("object_image"));
		type_cul.setCellValueFactory(new PropertyValueFactory<>("type"));
		price_cul.setCellValueFactory(new PropertyValueFactory<>("price"));
		checkbox_cul.setCellValueFactory(new PropertyValueFactory<>("checked"));
		create_boque_cul.setCellValueFactory(new PropertyValueFactory<>("flowers_number"));
		boque_items=new Item();
		menue_tableview.setItems(getItems(items));
		original_list= menue_tableview.getItems();
		if (Instance.getCurrentUser() != null  && Instance.getCurrentUser().getClass().getName().contains("ChainManger")){
			view_reports_btn.setVisible(true);
			add_item_btn.setVisible(true);
			delete_item_btn.setVisible(true);
		}else{
			view_reports_btn.setVisible(false);
			add_item_btn.setVisible(false);
			delete_item_btn.setVisible(false);
		}
		add_popup.setVisible(false);
		menue_tableview.setEffect(null);
	}


	public ObservableList<Item>getItems(List getFromDB){
		ObservableList<Item> items = FXCollections.observableArrayList();
		if (Instance.getCurrentUser() == null){
			for(Object item : getFromDB){
				Item toItem = (Item)item;
				toItem.flowers_number.setOnAction(e -> {
					tmp_item.getFlowersInItem().add(menue_tableview.getSelectionModel().getSelectedItem());
					tmp_item.setPrice(tmp_item.getItemPrice()+toItem.getId());
				});
				toItem.checked.setVisible(false);
				finish_order_label.setVisible(false);
				if(toItem.getType().equals("FLOWER")) {
					toItem.getFlowers_number().setVisible(true);
				} else{
					toItem.getFlowers_number().setVisible(false);
				}
			}
		}
		items.addAll(getFromDB);

		return items;
	}

	public void set_mouse_hand(){
		add_cart_order.getScene().setCursor(Cursor.HAND);
	}

	public void handle_go_btn(MouseEvent mouseEvent) throws IOException {
		filtered_list= new FilteredList<>(original_list);
		Label error_msg = new Label("Wrong parameters!");
		error_msg.maxHeight(17);
		error_msg.maxWidth(160);
		try{
			if(combo_box.getValue().equalsIgnoreCase("None")){
					if (to_field.getText().isEmpty() && from_field.getText().isEmpty()) {
						filtered_list = new FilteredList<>(original_list);
						return;
					}if (!to_field.getText().isEmpty() && from_field.getText().isEmpty()) {
					filtered_list.setPredicate(
							t -> {
								if (t.getPrice() <= Double.parseDouble(to_field.getText()))
									return true;
								return false; // or true
							}
					);
					menue_tableview.setItems(filtered_list);
				} else if (to_field.getText().isEmpty() && !from_field.getText().isEmpty()) {
					filtered_list.setPredicate(
							t -> {
								if (t.getPrice() <= Double.parseDouble(from_field.getText()))
									return true;
								return false; // or true
							}
					);
					menue_tableview.setItems(filtered_list);
				} else if (Double.parseDouble(to_field.getText()) < Double.parseDouble(from_field.getText())) {
					anchor_bar.getChildren().add(error_msg);
					return;
				} else {
					filtered_list.setPredicate(
							t -> {
								if (t.getPrice() <= Double.parseDouble(to_field.getText()) && t.getPrice() >= Double.parseDouble(from_field.getText()))
									return true;
								return false; // or true
							}
					);
					menue_tableview.setItems(filtered_list);
				}
			}else{
				if (to_field.getText().isEmpty() && from_field.getText().isEmpty()) {
					filtered_list.setPredicate(
							t -> {
								if (t.getDominantColor().equalsIgnoreCase(combo_box.getValue()))
									return true;
								return false; // or true
							}
					);
					menue_tableview.setItems(filtered_list);
					return;
				}else if (!to_field.getText().isEmpty() && from_field.getText().isEmpty()) {
					filtered_list.setPredicate(
							t -> {
								if (t.getPrice()<= Double.parseDouble(to_field.getText()) && t.getDominantColor().equalsIgnoreCase(combo_box.getValue()))
									return true;
								return false; // or true
							}
					);
					menue_tableview.setItems(filtered_list);
				} else if (to_field.getText().isEmpty() && !from_field.getText().isEmpty()) {
					filtered_list.setPredicate(
							t -> {
								if (t.getPrice()>= Double.parseDouble(from_field.getText()) && t.getDominantColor().equalsIgnoreCase(combo_box.getValue()))
									return true;
								return false; // or true
							}
					);
					menue_tableview.setItems(filtered_list);
				} else if (Double.parseDouble(to_field.getText()) < Double.parseDouble(from_field.getText())) {
					anchor_bar.getChildren().add(error_msg);
					return;
				} else {
					filtered_list.setPredicate(
							t -> {
								if (t.getPrice() <= Double.parseDouble(to_field.getText()) && t.getPrice() >= Double.parseDouble(from_field.getText()) && t.getDominantColor().equalsIgnoreCase(combo_box.getValue()))
									return true;
								return false; // or true
							}
					);
					menue_tableview.setItems(filtered_list);
				}
			}
		}catch (NumberFormatException e) {
			anchor_bar.getChildren().add(error_msg);
			e.printStackTrace();
		}

	}

	public ObservableList<Item>getCheckedItems(){
		ObservableList<Item> items_to_order = FXCollections.observableArrayList();
		for(Object item : menue_tableview.getItems()){
			Item toItem = (Item)item;
			if(toItem.getChecked().isSelected())
				items_to_order.add(toItem);
				toItem.setChecked(new CheckBox());
		}
		return items_to_order;
	}

	public ObservableList<Item>getCoustomCheckedItems(){
		ObservableList<Item> items_to_order = FXCollections.observableArrayList();
//		for(Object item : menue_tableview.getItems()){
//			Item toItem = (Item)item;
//			if(toItem.getFlowers_number().isSelected())
//				items_to_order.add(toItem);
//			toItem.setChecked(new CheckBox());
////		}
//
		return items_to_order;
	}

	public void handle_creat_boq(MouseEvent mouseEvent) {
		Iterator it = menue_tableview.getItems().iterator();
		int last_id=0;
		while (it.hasNext())
			last_id= ((Item)it.next()).getId();
		tmp_item.setId(last_id+1);
		tmp_item.setPrice(100.0);
		tmp_item.setType("CUSTOM");
		selected_items.add(tmp_item);
	}

	public void handle_delete_btn(MouseEvent mouseEvent) throws SQLException, Order.AlreadyExists {
		Item tmp = menue_tableview.getSelectionModel().getSelectedItem();
		menue_tableview.getItems().removeAll(tmp);
		Item.delete(tmp.getId());
	}

	public void handle_add_item(MouseEvent mouseEvent) {
		show_add_popup();
	}

	private void show_add_popup() {
		BoxBlur blur = new BoxBlur();
		blur.setHeight(582);
		blur.setWidth(927);
		add_popup.setVisible(true);
		menue_tableview.setEffect(blur);
	}

	public void handle_popup_close(MouseEvent mouseEvent) {
		add_popup.setVisible(false);
		type_field.setText("");
		dominant_field.setText("");
		price_field.setText("");
		image_field.setText("");
		can_boquet_box.setSelected(false);
		menue_tableview.setEffect(null);
	}

	public void handle_add_item_btn(MouseEvent mouseEvent) throws SQLException, Order.AlreadyExists, InterruptedException, IOException {
		Item tmp = new Item(type_field.getText(),dominant_field.getText(),Double.parseDouble(price_field.getText()),image_field.getText(),can_boquet_box.isSelected());
		tmp.insert();
		added_label.setVisible(true);
		add_popup.setVisible(false);
		type_field.setText("");
		dominant_field.setText("");
		price_field.setText("");
		image_field.setText("");
		added_label.setVisible(false);
		can_boquet_box.setSelected(false);
		menue_tableview.setEffect(null);
		tmp.checked=new CheckBox();
		tmp.flowers_number = new Button("Add to boquete");
		tmp.flowers_number.setStyle("-fx-background-color: #FFA500");
		menue_tableview.getItems().add(tmp);
		menue_tableview.refresh();
	}


}

