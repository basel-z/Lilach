package src.lil.client.lilachgui;

import java.net.URL;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.lil.client.Instance;

public class LilachMain extends Application{

	
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		URL url = getClass().getResource("LilachMainScene.fxml"); 
		AnchorPane pane = FXMLLoader.load(url);
		Scene scene = new Scene(pane);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Welcome to Lilach.");
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				Instance.getClientConsole().get_client().quit();
			}
		});
		primaryStage.show();
	}

}
