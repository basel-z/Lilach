package src.lil.client.lilachgui;

import java.util.List;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import src.lil.Enums.Role;
import src.lil.Enums.SubscriptionType;
import src.lil.client.Instance;

import src.lil.models.Client;
import src.lil.models.Employee;
import src.lil.models.StoreManger;
import src.lil.models.User;
import src.lil.models.customerService;

public class ManageUsersCont extends LilachController {
	@FXML
	private Pane employees_pane;

	@FXML
	private Pane clients_pane;

	@FXML
	private TableView<Client> users_table_id;

	@FXML
	private TableView<Object> employees_table_id;

	@FXML
	private TableColumn<Client, String> name_col;

	@FXML
	private TableColumn<Client, String> id_col;

	@FXML
	private TableColumn<Client, String> email_col;

	@FXML
	private TableColumn<Client, String> balance_col;

	@FXML
	private TableColumn<Client, String> phone_col;

	@FXML
	private TableColumn<Object, String> employee_name_col;

	@FXML
	private TableColumn<Object, String> employee_id_col;

	@FXML
	private TableColumn<Object, String> employee_email_col;

	@FXML
	private TableColumn<Object, String> employees_role_col;

	@FXML
	private TableColumn<Object, String> employee_phone_col;

	@FXML
	private Button clients_butt;

	@FXML
	private Button employees_butt;

	/*********** CHANGE VIEW ITEMS **********************/

	@FXML
	private Pane changes_pane;

	@FXML
	private TextField current_phone_txt;

	@FXML
	private TextField current_bank_txt;

	@FXML
	private TextField current_email_txt;

	@FXML
	private TextField current_password_txt;

	@FXML
	private TextField current_balance_txt;

	@FXML
	private TextField current_address_txt;

	@FXML
	private TextField current_credit_txt;

	@FXML
	private ComboBox<String> role_combobox;

	@FXML
	private ComboBox<String> subscription_combobox;

	@FXML
	private CheckBox block_checkbox;

	@FXML
	private ComboBox<String> store_combobox;

	@FXML
	private Button save_btn;

	@FXML
	private ImageView back_arrow_icon;

	@FXML
	private Label balance_lbl;

	@FXML
	private Label card_lbl;

	@FXML
	private Label role_lbl;

	@FXML
	private Label block_lbl;

	@FXML
	private Label sub_lbl;

	/****************************************************/

	private List<String> addresses;
	private Object obj; // to save the user

	public Role convert_to_role(String st) {
		if (st.equals("Employee")) {
			return Role.Employee;
		} else if (st.equals("Store Manager")) {
			return Role.StoreManger;
		} else {
			return Role.customerService;
		}
	}

	public SubscriptionType convert_to_subscription(String st) {
		if (st.equals("None")) {
			return SubscriptionType.nonSubscription;
		} else if (st.equals("Monthly")) {
			return SubscriptionType.Monthly;
		} else
			return SubscriptionType.Yearly;
	}

	public void fill_sub_type() {
		subscription_combobox.getItems().add("Annually");
		subscription_combobox.getItems().add("None");
		subscription_combobox.getItems().add("Monthly");

	}

	public void fill_role_box() {
		role_combobox.getItems().add("Employee");
		role_combobox.getItems().add("Customers Service");
		role_combobox.getItems().add("Store Manager");

	}

	@SuppressWarnings("unchecked")
	public void fill_stores_box() {
		Gson gson = new Gson();
		try {
			Instance.resetResponse();
			Instance.getClientConsole().get_client().sendToServer("get stores");
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (Instance.getResponse() == null) {
			System.out.println("");
		}
		addresses = gson.fromJson(Instance.getResponse(), List.class);
		for (String string : addresses) {
			store_combobox.getItems().add(string);
		}
	}

	public void get_store_address(String st) {
		for (String string : addresses) {
			if (string.contains(st)) {
				store_combobox.setValue(string);
				return;
			}
		}
	}

	public void start_changes_view() {
		String st = "";
		changes_pane.setVisible(true);
		clients_pane.setVisible(false);
		employees_pane.setVisible(false);
		get_store_address(((User) obj).getStoreId());
		if (client_flag == true) {
			subscription_combobox.setVisible(true);
			block_lbl.setVisible(true);
			balance_lbl.setVisible(true);
			sub_lbl.setVisible(true);
			card_lbl.setVisible(true);
			block_checkbox.setVisible(true);
			current_balance_txt.setVisible(true);
			current_credit_txt.setVisible(true);
			role_lbl.setVisible(false);
			role_combobox.setVisible(false);
			st = ((Client) obj).getSubscriptionType().toString();
			if (st.equals("nonSubscription")) {
				subscription_combobox.setValue("None");
			} else if (st.equals("Monthly")) {
				subscription_combobox.setValue("Monthly");
			} else
				subscription_combobox.setValue("Annually");
			current_balance_txt.setText(String.valueOf(((Client) obj).getBalance()));
			current_address_txt.setText(((Client) obj).getShippingAddress());
			if (((Client) obj).isBlocked()) {
				block_checkbox.setIndeterminate(true);
			}
			current_credit_txt.setText(((Client) obj).getCreditCardNumber());
			back_arrow_icon.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					handle_view_clients_butt(null);
				}

			});
		} else {
			subscription_combobox.setVisible(false);
			block_lbl.setVisible(false);
			balance_lbl.setVisible(false);
			sub_lbl.setVisible(false);
			card_lbl.setVisible(false);
			block_checkbox.setVisible(false);
			current_balance_txt.setVisible(false);
			current_credit_txt.setVisible(false);
			role_lbl.setVisible(true);
			role_combobox.setVisible(true);
			st = ((Employee) obj).getRole().toString();

			if (st.equals("Employee")) {
				role_combobox.setValue("Employee");
			} else if (st.equals("StoreManger")) {
				role_combobox.setValue("Store Manager");
			} else {
				role_combobox.setValue("Customers Service");
			}
			back_arrow_icon.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					handle_view_employees_butt(null);
				}

			});

		}
		current_phone_txt.setText(((User) obj).getPhone());
		current_bank_txt.setText(((User) obj).getBankAccount());
		current_email_txt.setText(((User) obj).getEmail());
		current_password_txt.setText(((User) obj).getPassword());
	}

	/******************** MY VARS ***********************/

	private List<Client> clients;
	private List<Object> employees;
	Gson gson = new Gson();
	Type list_type_client = new TypeToken<List<Client>>() {
	}.getType();
	Type list_type_Object = new TypeToken<List<Object>>() {
	}.getType();
	private ObservableList<Client> cli_data = FXCollections.observableArrayList();
	private ObservableList<Object> emp_data = FXCollections.observableArrayList();
	private boolean client_flag;

	/****************************************************/
	public void update_user_fields() {
		((User) obj).setPhone(current_phone_txt.getText());
		((User) obj).setBankAccount(current_bank_txt.getText());
		((User) obj).setEmail(current_email_txt.getText());
		((User) obj).setPassword(current_password_txt.getText());
		((User) obj).setStoreId(store_combobox.getValue().split("-")[1]);
	}

	public void update_client_fields() {
		((Client) obj).setSubscriptionType(convert_to_subscription(subscription_combobox.getValue()));
		((Client) obj).setShippingAddress(current_address_txt.getText());
		((Client) obj).setCreditCardNumber(current_credit_txt.getText());
		((Client) obj).setBalance(Double.parseDouble(current_balance_txt.getText()));
		((Client) obj).setBlocked(block_checkbox.isSelected());
	}

	public void update_employee_fields() {
		((Employee) obj).setRole(convert_to_role(role_combobox.getValue()));

	}

	public void get_all_clients() {
		try {
			Instance.resetResponse();
			Instance.getClientConsole().get_client().sendToServer("getallclients");
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (Instance.getResponse() == null) {
			System.out.println("Waiting...");
		}
		clients = gson.fromJson(Instance.getResponse(), list_type_client);
		fill_clients_table();
	}

	public void get_all_employees() {
		try {
			Instance.resetResponse();
			Instance.getClientConsole().get_client().sendToServer("getallemployees");
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (Instance.getResponse() == null) {
			System.out.println("Waiting for response...");
		}
		employees = gson.fromJson(Instance.getResponse(), list_type_Object);
		fill_employee_table();
	}

	@SuppressWarnings("unchecked")
	void fill_employee_table() {
		employees_table_id.getColumns().clear();
		employees_table_id.setRowFactory(tv -> {
			TableRow<Object> row = new TableRow<>();
			row.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					if (event.getClickCount() == 2 && (!row.isEmpty())) {
						client_flag = false;
						obj = row.getItem();
						start_changes_view();
					}
				}

			});
			return row;
		});
		employees_table_id.getColumns().addAll(employees_role_col, employee_name_col, employee_id_col,
				employee_email_col, employee_phone_col);
		employees_role_col.setCellValueFactory(new PropertyValueFactory<>("role"));
		employee_name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
		employee_id_col.setCellValueFactory(new PropertyValueFactory<>("userId"));
		employee_email_col.setCellValueFactory(new PropertyValueFactory<>("email"));
		employee_phone_col.setCellValueFactory(new PropertyValueFactory<>("phone"));

		for (Object employee : employees) {
			if (employee.toString().contains("StoreManger")) {
				emp_data.add(gson.fromJson(gson.toJson(employee), StoreManger.class));
			} else if (employee.toString().contains("Employee")) {
				emp_data.add(gson.fromJson(gson.toJson(employee), Employee.class));
			} else {
				emp_data.add(gson.fromJson(gson.toJson(employee), customerService.class));
			}
		}
		employees_table_id.getItems().clear();
		employees_table_id.getItems().setAll(emp_data);

	}

	@FXML
	public void handle_view_employees_butt(ActionEvent a) {
		clients_pane.setVisible(false);
		employees_pane.setVisible(true);
		changes_pane.setVisible(false);
		employees_butt.setStyle("-fx-background-color: #9b274c");
		clients_butt.setStyle("-fx-background-color: #db7092");

	}

	@SuppressWarnings("unchecked")
	public void fill_clients_table() {
		users_table_id.getColumns().clear();
		users_table_id.setRowFactory(tv -> {
			TableRow<Client> row = new TableRow<>();
			if(row.getItem() != null && row.getItem().isBlocked()) {
				System.out.println("YES I DO ENTER THIS");
				row.setStyle("-fx-background-color: #ff0000");
			}
			row.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
				@Override
				public void handle(MouseEvent event) {
					if (event.getClickCount() == 2 && (!row.isEmpty())) {
						
						client_flag = true;
						obj = row.getItem();
						start_changes_view();
					}
				};



			});
			return row;
		});
		users_table_id.getColumns().addAll(name_col, id_col, balance_col, email_col, phone_col);
		name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
		id_col.setCellValueFactory(new PropertyValueFactory<>("userId"));
		balance_col.setCellValueFactory(new PropertyValueFactory<>("balance"));
		email_col.setCellValueFactory(new PropertyValueFactory<>("email"));
		phone_col.setCellValueFactory(new PropertyValueFactory<>("phone"));

		for (Client client : clients) {
			cli_data.add(client);
		}
		
		users_table_id.getItems().clear();
		users_table_id.getItems().setAll(cli_data);
		
	}

	@FXML
	public void handle_submit_butt(ActionEvent e) {
		update_user_fields();
		if (client_flag) {
			update_client_fields();
			users_table_id.getItems().clear();
			users_table_id.getItems().setAll(cli_data);
		} else {
			update_employee_fields();
			employees_table_id.getItems().clear();
			employees_table_id.getItems().setAll(emp_data);
		}
		try {
			Instance.resetResponse();
			Instance.getClientConsole().get_client()
					.sendToServer("update " + ((StoreManger) Instance.getCurrentUser()).getUserId() + " " + client_flag
							+ " " + gson.toJson(obj));
			while (Instance.getResponse() == null) {
				System.out.println("VVaiting...");
			}
			if(obj.getClass().getName().contains("Client")) {
				handle_view_clients_butt(null);
			}else {
				handle_view_employees_butt(null);
			}

		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	@FXML
	public void handle_view_clients_butt(ActionEvent a) {
		clients_pane.setVisible(true);
		employees_pane.setVisible(false);
		changes_pane.setVisible(false);
		clients_butt.setStyle("-fx-background-color: #9b274c");
		employees_butt.setStyle("-fx-background-color: #db7092");
	}

	@FXML
	public void initialize() {
		this.check_logins();
		manageusers_btn.setVisible(false);
		fill_stores_box();
		fill_role_box();
		fill_sub_type();
		get_all_clients();
		get_all_employees();

		handle_view_clients_butt(null);
	}
}
