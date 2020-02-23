package src.lil.client.lilachgui;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import src.lil.client.Instance;
import src.lil.models.ChainManger;
import src.lil.models.Employee;
import src.lil.models.User;

import java.util.*;

public class BlockUsersController extends LilachController {

    @FXML
    private Text user_balance;

    @FXML
    private ComboBox<String> users_with_balance;
    @FXML
    private Text messageToManger;

    private Map<String, String> balances = new HashMap<>();
    @FXML
    public void initialize() {
        this.check_logins();
        user_balance.setText("Balance: ");
        messageToManger.setText("");
        Gson gson = new Gson();
        String command = "GetAllBalances" + gson.toJson((ChainManger) Instance.getCurrentUser());
        try {
            Instance.resetResponse();
            Instance.getClientConsole().get_client().sendToServer(command);
            List<String> Users = new ArrayList<>();
            while (Instance.getResponse() == null) {
                System.out.println("Waiting for respond");
            }
            if(!Instance.getResponse().equals("error")) {
                Users = Arrays.asList(Instance.getResponse().split(","));
                List<String> UsersToShow = new ArrayList<>();
                for(String user: Users){
                    UsersToShow.add(user.split("-")[0]);
                    balances.put(user.split("-")[0].split(" ~ ID: ")[1],user.split("-")[1]);
                }
                users_with_balance.getItems().addAll(FXCollections.observableList(UsersToShow));
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    @FXML
    void handle_block_butt(ActionEvent event) {
        String id = users_with_balance.getValue().split(" ~ ID: ")[1];
        Gson gson = new Gson();
        String command = "BlockClientById" + gson.toJson((ChainManger) Instance.getCurrentUser()) + "@" + id;
        try {
            Instance.resetResponse();
            Instance.getClientConsole().get_client().sendToServer(command);
            while (Instance.getResponse() == null) {
                System.out.println("Waiting for respond");
            }
            if(!Instance.getResponse().equals("error")) {
                this.initialize();
            }
            else messageToManger.setText("Something Failed\n Please Block again");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    @FXML
    void handle_user_change(ActionEvent event) {
        String id = users_with_balance.getValue().split(" ~ ID: ")[1];
        user_balance.setText("Balance: " + balances.get(id));
    }

}
