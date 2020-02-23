package src.lil.client.lilachgui;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import src.lil.client.Instance;

import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;

public class ComplainsCont extends LilachController{
    @FXML
    private TextField email;

    @FXML
    private TextField phone;

    @FXML
    private TextField store;

    @FXML
    private TextField order_id;

    @FXML
    private TextField client_id;

    @FXML
    private TextField complain_title;

    @FXML
    private TextArea complain;

    @FXML
    private Button submit_complain;

    @FXML
    private Text message_to_client;

    @FXML
	   public void initialize() {
		this.check_logins();
	}


    @FXML
    void handle_submit_complain(ActionEvent event) throws IOException {
        try{
            if(email.getText().isEmpty() || complain_title.getText().isEmpty() || complain.getText().isEmpty()){
                if(email.getText().isEmpty()) email.setStyle("-fx-background-color: yellow;");
                if(complain_title.getText().isEmpty()) complain_title.setStyle("-fx-background-color: yellow;");
                if(complain.getText().isEmpty()) complain.setStyle("-fx-background-color: yellow;");
                message_to_client.setText("Please fill in the yellow fields");
                return;
            }
            JsonObject myData = new JsonObject();
            myData.addProperty("contact_email",email.getText());
            myData.addProperty("contact_phone",phone.getText());
            myData.addProperty("complain_title",complain_title.getText());
            myData.addProperty("complain_text",complain.getText());
            myData.addProperty("store_adress",store.getText());
            myData.addProperty("order_Id",order_id.getText());
            myData.addProperty("user_id",client_id.getText());
            Gson gson = new Gson();
            String element = gson.toJson(myData);
            element = "SubmitComplain" + element;
            Instance.getClientConsole().get_client().sendToServer(element);
            message_to_client.setFill(new Color(0,1,0,1));
            message_to_client.setText("Complain sent successfully :)");
            submit_complain.setVisible(false);
        }
       catch (Exception e){
           message_to_client.setText("Something went wrong ! please Try again");
       }
    }
}
