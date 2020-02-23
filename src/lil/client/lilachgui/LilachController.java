package src.lil.client.lilachgui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.lil.client.Instance;
import src.lil.models.Client;
import src.lil.models.Complain;
import src.lil.models.Item;

import java.io.IOException;

public abstract class LilachController {

	public static ObservableList<Item> selected_items = FXCollections.observableArrayList();

	@FXML
	protected AnchorPane main_anchor_pane;

	@FXML
	protected Button menu_btn;

	@FXML
	protected Button myorders_btn;

	@FXML
	protected Button complain_btn;

	@FXML
	protected Button manageusers_btn;

	@FXML
	protected Button sigup_btn;

	@FXML
	protected Button login_btn;

	@FXML
	protected ListView<?> menu_items_list;

	@FXML
	protected Button view_reports_btn;

	@FXML
	protected ImageView logo;

	@FXML
	protected ImageView cart_id;

	@FXML
	protected Button signout_btn1;

	@FXML
	protected Button block_Users;

	@FXML
	protected Label finish_order_label;

	@FXML
	protected ImageView bg_image;

	/***************************************************/
	public void show_client_butt() {
		complain_btn.setVisible(true);
		myorders_btn.setVisible(true);
		cart_id.setVisible(true);
		manageusers_btn.setVisible(false);
	}

	public void show_manager_butt() {
		complain_btn.setVisible(true);
		cart_id.setVisible(false);
		myorders_btn.setVisible(false);
	}

	public void show_sign_out_butt() {
		signout_btn1.setVisible(true);
		login_btn.setVisible(false);
		sigup_btn.setVisible(false);
	}

	public void hide_sign_out_butt() {
		signout_btn1.setVisible(false);
		login_btn.setVisible(true);
		sigup_btn.setVisible(true);
	}

	public void get_scene(String file_name, String Title) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource(file_name));
		Scene scene = new Scene(pane);
		Stage stage = (Stage) main_anchor_pane.getScene().getWindow();

		if (Instance.getCurrentUser() != null) {
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent event) {
					try {
						Instance.resetResponse();
						Instance.getClientConsole().get_client().sendToServer("Logout " + Instance.get_id());
					} catch (Exception e) {
						e.printStackTrace();
					}
					while (Instance.getResponse() == null) {
					}
					Instance.getClientConsole().get_client().quit();
				}
			});
		}
		stage.setScene(scene);
		stage.setTitle(Title);
		stage.show();
	}

	/***************************************************/
	@FXML
	public void handle_reports_btn(ActionEvent actionEvent) throws IOException {
		get_scene("ReportViewer.fxml", "Welcome to Lilach.");
	}
	@FXML
	public void handle_login_butt(ActionEvent event) throws IOException {
		get_scene("LoginPage.fxml", "Login");
	}

	@FXML
	public void handle_signup_butt(ActionEvent event) throws IOException {
		get_scene("SignupPage.fxml", "Sign up");
	}

	@FXML
	public void handle_complain_butt(ActionEvent event) throws IOException {
		if (Instance.getCurrentUser().getClass().getName().contains("customerService")) {
			complain_btn.setText("Complains Handle");
			get_scene("customerServiceView.fxml", "Complains Handle!");
		} else {
			complain_btn.setText("Complain");
			get_scene("ComplainPage.fxml", "Complain!");
		}
	}

	@FXML
	public void handle_manage_butt(ActionEvent event) throws IOException {
		get_scene("ManageUsersPage.fxml", "User managment.");
	}

	@FXML
	public void handle_menu_butt(ActionEvent event) throws IOException {
		if (Instance.getCurrentUser() != null && Instance.getCurrentUser().getClass().getName().contains("Client")) {
		
			Client client = (Client) Instance.getCurrentUser();
			if (client.isBlocked()) {
				get_scene("unblockUser.fxml", "Please pay your balance");
			} else {
				this.check_logins();
				get_scene("MenuPage.fxml", "Welcome to Lilach.");
			}
		} else if (Instance.getCurrentUser() != null
				&& Instance.getCurrentUser().getClass().getName().contains("StoreManger")) {
			get_scene("ManageStoreView.fxml", "Welcome to Lilach.");
		} else {
			this.check_logins();
			get_scene("MenuPage.fxml", "Welcome to Lilach.");
		}
	}

	@FXML
	public void handle_my_order_butt(ActionEvent event) throws IOException {
		get_scene("OrderHistory.fxml", "My orders history");
	}

	@FXML
	public void handle_cart_click(MouseEvent event) throws IOException {

		get_scene("myOrdersPage.fxml", "Cart");
	}

	@FXML
	void handle_block_users(ActionEvent event) throws IOException {
		get_scene("block_users.fxml", "Block_user");
	}

	public void check_logins() {


		view_reports_btn.setVisible(false);

		if (Instance.getCurrentUser() == null) {

			signout_btn1.setVisible(false);
			complain_btn.setVisible(false);
			myorders_btn.setVisible(false);
			manageusers_btn.setVisible(false);
			cart_id.setVisible(false);
			block_Users.setVisible(false);
			finish_order_label.setVisible(false);
			hide_sign_out_butt();
		} else if (Instance.getCurrentUser().getClass().getName().contains("Client")) {
			if (((Client) Instance.getCurrentUser()).isBlocked()) {
				myorders_btn.setVisible(false);
				show_sign_out_butt();
				finish_order_label.setVisible(false);
				manageusers_btn.setVisible(false);
				cart_id.setVisible(false);
				return;
			}
			show_client_butt();
			show_sign_out_butt();
			finish_order_label.setVisible(true);

		} else if (Instance.getCurrentUser().getClass().getName().contains("ChainManger")) {
			view_reports_btn.setVisible(true);
			block_Users.setVisible(true);
			finish_order_label.setVisible(false);
			complain_btn.setVisible(false);
			manageusers_btn.setVisible(true);
			show_manager_butt();
			show_sign_out_butt();
		} else {
			complain_btn.setLayoutX(273);
			block_Users.setVisible(false);
			manageusers_btn.setVisible(false);
			finish_order_label.setVisible(false);
			complain_btn.setVisible(true);
			complain_btn.setText("Handle complain");
			show_manager_butt();
			show_sign_out_butt();
		}

	}

	@FXML
	public void handle_signout_butt() throws IOException {
		try {
			Instance.resetResponse();
			Instance.getClientConsole().get_client().sendToServer("Logout " + Instance.get_id());
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (Instance.getResponse() == null) {
			// System.out.println("Waiting for response...");
		}
		if (Instance.getResponse().equals("successful")) {
			Instance.setCurrentUser(null);
			get_scene("LilachMainScene.fxml", "Welcome to Lilach.");
		}
	}
}
