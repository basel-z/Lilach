package src.lil.client.lilachgui;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.StringTokenizer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import src.lil.Enums.SubscriptionType;
import src.lil.client.Instance;
import src.lil.models.ChainManger;
import src.lil.models.Client;
import src.lil.models.Employee;
import src.lil.models.StoreManger;

public class SignupController extends LilachController {
	@FXML
	private TextField user_id_txt;

	@FXML
	private TextField phone_num_txt;

	@FXML
	private TextField email_txt;

	@FXML
	private TextField bank_acc_txt;

	@FXML
	private TextField credit_txt;

	@FXML
	private ComboBox<String> store_add_box;

	@FXML
	private TextField password_txt;

	@FXML
	private Button register_butt;

	@FXML
	private TextField fullname_txt;

	@FXML
	private ComboBox<String> sub_type_box;

	@FXML
	private Text error_msg;

	@FXML
	private Text id_lable_id;

	@FXML
	private TextField address_txt;
	////////////////////////////////////////

	private List<String> addresses;

	public void fill_sub_type() {
		sub_type_box.getItems().add("None");
		sub_type_box.getItems().add("Monthly");
		sub_type_box.getItems().add("Annually");
	}

	public boolean check_input() {

		if (fullname_txt.getText().isEmpty() || user_id_txt.getText().isEmpty() || phone_num_txt.getText().isEmpty()
				|| email_txt.getText().isEmpty() || password_txt.getText().isEmpty() || bank_acc_txt.getText().isEmpty()
				|| credit_txt.getText().isEmpty() || store_add_box.getValue() == null
				|| sub_type_box.getValue() == null) {
			error_msg.setText("Please fill all fields");
			return false;
		} else if (user_id_txt.getText().matches("[0-9]+") == false) {
			error_msg.setText("User ID should contain numbers only and length of 9!");
			user_id_txt.setStyle("-fx-background-color: yellow;");
			return false;
		} else if (!email_txt.getText().contains(".co") || !email_txt.getText().contains("@")) {
			error_msg.setText("invalid E-Mail.");
			email_txt.setStyle("-fx-background-color: yellow;");
			return false;
		}
		return true;
	}

	////////////////////////////////////////
	@FXML
	public void initialize() {
		this.check_logins();
		Gson gson = new Gson();
		try {
			Instance.resetResponse();
			Instance.getClientConsole().get_client().sendToServer("get stores");
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (Instance.getResponse() == null) {
			System.out.println("fetching stores addresses...");
		}
		Type list_type_Object = new TypeToken<List<String>>() {
		}.getType();

		addresses = gson.fromJson(Instance.getResponse(), list_type_Object);
		for (String string : addresses) {
			store_add_box.getItems().add(string);
		}
		fill_sub_type();
	}

	@FXML
	public void handle_reg_butt(ActionEvent event) {
		Gson gson = new Gson();
		user_id_txt.setStyle("-fx-background-color: white;");
		email_txt.setStyle("-fx-background-color: white;");
		id_lable_id.setFill(Color.BLACK);
		error_msg.setText("");
		if (check_input()) {
			SubscriptionType val;
			if (sub_type_box.getValue().equals("None")) {
				val = SubscriptionType.nonSubscription;
			} else if (sub_type_box.getValue().equals("Monthly")) {
				val = SubscriptionType.Monthly;
			} else {
				val = SubscriptionType.Yearly;
			}
			Client register = new Client(Integer.parseInt(user_id_txt.getText()), fullname_txt.getText(),

					phone_num_txt.getText(), bank_acc_txt.getText(), address_txt.getText(), email_txt.getText(),
					password_txt.getText(), val, credit_txt.getText(), store_add_box.getValue().split("-")[1], 0.0);

			String json = gson.toJson(register);
			try {
				Instance.resetResponse();
				Instance.getClientConsole().get_client().sendToServer("register " + json);
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (Instance.getResponse() == null) {
				System.out.println("Waiting...");
			}
			if (Instance.getResponse().equals("successfull")) {
				try {
					Instance.resetResponse();
					Instance.getClientConsole().get_client().sendToServer(
							"Login " + String.valueOf(register.getUserId()) + " " + register.getPassword());
				} catch (Exception e) {
					e.printStackTrace();
				}
				while (Instance.getResponse() == null) {
					System.out.println("Waiting...");
				}
				if (Instance.getResponse().startsWith("successful")) {
					Instance.set_id(String.valueOf(register.getUserId()));
					login_btn.setVisible(false);
					sigup_btn.setVisible(false);
					signout_btn1.setVisible(true);
					String[] Role = Instance.getResponse().split("SPACE");
					Object current_user = null;

					current_user = gson.fromJson(Role[1], Client.class);

					Instance.setCurrentUser(current_user);
					try {
						this.handle_menu_butt(null);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else if (Instance.getResponse().equals("SQL Exception!")) {
				error_msg.setText("Failed to connect to the DATABASE!");
			} else {
				error_msg.setText("User with same ID already exists!");
				user_id_txt.setStyle("-fx-background-color: yellow;");
			}
		}

	}
}
