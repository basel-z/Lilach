package src.lil.client.lilachgui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import src.lil.client.Instance;
import src.lil.models.Employee;
import src.lil.models.customerService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class customerServiceController extends LilachController{
    @FXML
    private Text complain_title;

    @FXML
    private Text complain;

    @FXML
    private Button login_btn;

    @FXML
    private ImageView logo;

    @FXML
    private Button complain_btn;

    @FXML
    private Button manageusers_btn;

    @FXML
    private Button menu_btn;

    @FXML
    private Button myorders_btn;

    @FXML
    private ImageView cart_id;

    @FXML
    private Button signout_btn1;

    @FXML
    private Button sigup_btn;

    @FXML
    private ComboBox<String> all_titles;

    @FXML
    private TextArea reply_text;

    @FXML
    private TextField refund;
    @FXML
    private Text messageToEmployee;


    @FXML
    public void initialize() {
        complain_title.setText("Complain title: ");
        complain.setText("Complain : \n");
        reply_text.setText("");
        refund.setText("");
        messageToEmployee.setText("");
        Gson gson = new Gson();
        this.check_logins();
        complain_btn.setText("Handle Complains");
        String command = "GetAllComplains" + gson.toJson((Employee)Instance.getCurrentUser());
        try {
            Instance.resetResponse();
            Instance.getClientConsole().get_client().sendToServer(command);
            List<String> titles = new ArrayList<>();
            while (Instance.getResponse() == null) {
                    System.out.println("Waiting for respond");
                }
            if(!Instance.getResponse().equals("error")) {
                titles = Arrays.asList(Instance.getResponse().split(","));
                all_titles.getItems().addAll(FXCollections.observableList(titles));
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    @Override
    public void handle_complain_butt(ActionEvent event) throws IOException {

    }
    @FXML
    void submit_complain_reply(ActionEvent event) {
        String id = all_titles.getValue().split(" ~ ID: ")[1];
        try {
            Gson gson = new Gson();
            Instance.resetResponse();
            String _refund = refund.getText();
            if(_refund.isEmpty()) _refund = "0.0";
            else {
                try{
                    Double.parseDouble(_refund);
                }
                catch (Exception e){
                    messageToEmployee.setText("Please enter refund in digits only");
                    return;
                }
            }
            String command = "SetComplainReply" + id + ";" +gson.toJson((Employee)Instance.getCurrentUser())+ ";" +  reply_text.getText()+ ";" + _refund;
            Instance.getClientConsole().get_client().sendToServer(command);
            while (Instance.getResponse() == null) {
                System.out.println("Waiting for respond");
            }
            if(Instance.getResponse().equals("AllDone")) {
                get_scene("customerServiceView.fxml", "Complains Handle!");
            }
            else messageToEmployee.setText("Please try to submit again");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void handle_change_complain(ActionEvent event) {
        String id = all_titles.getValue().split(" ~ ID: ")[1];
        try {
            Gson gson = new Gson();
            Instance.resetResponse();
            String command = "GetComplainById" + id + ";" +gson.toJson((Employee)Instance.getCurrentUser());
            Instance.getClientConsole().get_client().sendToServer(command);
            while (Instance.getResponse() == null) {
                System.out.println("Waiting for respond");
            }
            if(!Instance.getResponse().equals("error")) {
                JsonObject jsonObject = gson.fromJson(Instance.getResponse(), JsonObject.class);
                complain_title.setText("Complain title: " + jsonObject.get("complain_title").getAsString());
                complain.setText("Complain : \n" + jsonObject.get("complain_text").getAsString());
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
