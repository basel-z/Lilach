package src.lil.client.lilachgui;

import java.io.IOException;
import java.util.StringTokenizer;

import com.google.gson.Gson;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import src.lil.client.Instance;
import src.lil.models.*;

public class LoginController extends LilachController {

	private String user_id = "";
	private String password = "";

	/************************************************/

	@FXML
	private TextField userid_txt;

	@FXML
	private PasswordField password_txt;

	@FXML
	private Text error_txt;

	@FXML
	private Button loginbutton;

	@FXML
	void handle_id_input(KeyEvent event) {
		user_id = userid_txt.getText();
	}

	@FXML
	public void initialize() {
		this.check_logins();
	}

	@FXML
	void handle_login(ActionEvent event) throws IOException {
		if (user_id.matches("[0-9]+") == false) {
			error_txt.setText("User ID should contain numbers only!");
			error_txt.setVisible(true);
		} else {
			try {
				Instance.resetResponse();
				Instance.getClientConsole().get_client().sendToServer("Login " + user_id + " " + password);
			} catch (Exception e) {
				e.printStackTrace();
			}
			while (Instance.getResponse() == null) {
				System.out.println("Waiting...");
			}
			if (Instance.getResponse().startsWith("successful")) {
				Gson gson = new Gson();
				Instance.set_id(user_id);
				login_btn.setVisible(false);
				sigup_btn.setVisible(false);
				signout_btn1.setVisible(true);
//				StringTokenizer roletok = new StringTokenizer(Instance.getResponse());
//				roletok.nextToken(" ");
				String Role = Instance.getResponse().split("successful")[1].split("SPACE")[0];
				String user = Instance.getResponse().split("successful")[1].split("SPACE")[1];
				Object current_user = null;
				if (Role.contains("StoreManger")) {
					current_user = gson.fromJson(user, StoreManger.class);
				} else if (Role.contains("ChainManger")) {
					current_user = gson.fromJson(user, ChainManger.class);
				}else if (Role.contains("customerService")) {
					current_user = gson.fromJson(user, customerService.class);
				} else if (Role.contains("Employee")) {
					current_user = gson.fromJson(user, Employee.class);
				} else {
					current_user = gson.fromJson(user, Client.class);
				}
				Instance.setCurrentUser(current_user);
				this.handle_menu_butt(null);
			} else {
				error_txt.setText(Instance.getResponse());
			}
		}
	}

	@FXML
	void handle_password_input(KeyEvent event) {
		password = password_txt.getText();
	}

}
