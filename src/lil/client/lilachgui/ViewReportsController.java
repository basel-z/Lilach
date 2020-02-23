
package src.lil.client.lilachgui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import src.lil.models.Item;
import src.lil.models.Report;

import java.io.*;
import java.util.List;

public class ViewReportsController extends LilachController{


    @FXML
    private DatePicker date_picker;

    @FXML
    private Button search_btn;

    @FXML
    private TextField store_id_comp;

    @FXML
    private TableColumn<Report, String> rep_name_cul;

    @FXML
    private TableColumn<Report, String> report_type_cul;

    @FXML
    private TableColumn<Report, Button> view_cont_cul;

    @FXML
    private TableColumn<Report, CheckBox> compare_cul;

    @FXML
    private Button compare_btn;

    @FXML
    private StackedBarChart<Report, Double> income_chart;
    @FXML
    private StackedBarChart<?, ?> complains_chart;
    @FXML
    private TableView<Report>reports_table;


    @FXML
    public void initialize() {
        fillReportsDetails();
    }

    private void fillReportsDetails() {
        //		Gson gson=new Gson();
        List<Item> items=null;

        rep_name_cul.setCellValueFactory(new PropertyValueFactory<>("name"));
        report_type_cul.setCellValueFactory(new PropertyValueFactory<>("type"));
        view_cont_cul.setCellValueFactory(new PropertyValueFactory<>("content"));
        compare_cul.setCellValueFactory(new PropertyValueFactory<>("compared_check"));
        reports_table.setItems(getReports(items));
    }

    private ObservableList<Report> getReports(List<Item> items) {
            return FXCollections.observableArrayList();
    }

    @FXML
    void handle_compare_btn(MouseEvent event) {

    }

    @FXML
    void handle_search_btn(MouseEvent event) {

    }

    @FXML
    void handle_signout_butt(ActionEvent event) {

    }


    private String readFile(File file){
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));

            String text;
            while ((text = bufferedReader.readLine()) != null) {
                stringBuffer.append(text);
            }

        } catch (FileNotFoundException ex) {

        } catch (IOException ex) {

        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {

            }
        }
        return stringBuffer.toString();
    }
}






