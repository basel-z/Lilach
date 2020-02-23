package src.lil.client.lilachgui;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import src.lil.client.Instance;
import src.lil.models.ChainManger;
import src.lil.models.Client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class unblockUserController extends LilachController {

    @FXML
    private Text message;

    @FXML
    private Text messageForError;

    private Client currClient;

    @FXML
    public void initialize() {
        this.check_logins();
        
        messageForError.setText("");
       
        currClient = (Client) Instance.getCurrentUser();
        message.setText("Dear " + currClient.getName() +"\n We're sorry to tell you that you have been blocked\n" +
                "Because of your balance which is : "+ currClient.getBalance() +"\n" +
                "To continue using Lilach you Have to pay the amount by clicking pay\n" +
                "Balance will be credited with this credit card " + currClient.getCreditCardNumber() + "\n" +
                "Thank you ");
    }

    @FXML
    void handle_pay_butt(ActionEvent event) {
        Gson gson = new Gson();
        String command = "PayBalanceForUser" + gson.toJson(currClient);
        try {
            Instance.resetResponse();
            Instance.getClientConsole().get_client().sendToServer(command);
            while (Instance.getResponse() == null) {
                System.out.println("Waiting for respond");
            }
            if(!Instance.getResponse().equals("error")) {
                Client client = (Client) Instance.getCurrentUser();
                client.setBlocked(false);
                Instance.setCurrentUser(client);
                get_scene("MenuPage.fxml", "Welcome to Lilach.");
            }
            else {messageForError.setText("Something went wrong, please pay again!");}
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
