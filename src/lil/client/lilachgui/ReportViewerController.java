package src.lil.client.lilachgui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import src.lil.models.Report;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.concurrent.atomic.AtomicReference;


public class ReportViewerController extends LilachController{

	@FXML
	private Circle report_view_close;
	@FXML
	private  TextArea report_content;


	@FXML
	private Pane view_content_pormpt;

	@FXML
	public Button report_search_btn;
	@FXML
	public  TableView<Report> reports_table;

	private ObservableList<Report>original_list;

	@FXML
	private DatePicker date_picker;

	@FXML
	private TableColumn<Report, String> report_name_cul;

	@FXML
	private TableColumn<Report, String> report_type_cul;

	@FXML
	private TableColumn<Report, Button> view_content_cul;

	@FXML
	private TableColumn<Report, CheckBox> compare_check_cul;

	@FXML
	private Button compare_btn;

	@FXML
	private LineChart<String, Number> Income_chart;

	@FXML
	private LineChart<String, Integer> complains_chart;

	@FXML
	private Button clear_charts_btn;
	private FilteredList<Report> filtered_list;
	@FXML
	public void initialize() {

		this.check_logins();
		displayReports();


	}

	public void displayReports(){
		List<Report> reportsList ;
		reportsList= Report.getReports();
		report_name_cul.setCellValueFactory(new PropertyValueFactory<>("report_name"));
		report_type_cul.setCellValueFactory(new PropertyValueFactory<>("report_type"));
		view_content_cul.setCellValueFactory(new PropertyValueFactory<>("view_report"));
		compare_check_cul.setCellValueFactory(new PropertyValueFactory<>("to_compare"));
		reports_table.setItems(getObservedReports(reportsList));
		original_list=reports_table.getItems();
	}

	private ObservableList<Report> getObservedReports(List reportsList) {
			javafx.collections.ObservableList<Report> reports = FXCollections.observableArrayList();

			for(Object report : reportsList) {

				Report toReport = (Report) report;

				toReport.getView_report().setOnAction(event -> {
					view_content_pormpt.setVisible(true);
					report_content.setVisible(true);
					List<String> lines;
					if(toReport.getReport_type().equals("Monthly")) {
						 lines = read(new File("src/lil/client/lilachgui/reports/MonthlyReports/01112019.txt"));
					}else {
						 lines = read(new File("src/lil/client/lilachgui/reports/QuarterReports/01112019.txt"));
					}
					for(String line : lines){
							report_content.appendText(line);
					}
				});
			}
			reports.addAll(reportsList);

			return reports;
		}


	private String getMonth(Report rep) {
		if (rep.getReport_name().contains("01/01/"))
			return "Jan";
		if (rep.getReport_name().contains("01/02"))
			return "Feb";
		if (rep.getReport_name().contains("01/03"))
			return "March";
		if (rep.getReport_name().contains("01/04"))
			return "Apr";
		if (rep.getReport_name().contains("01/05"))
			return "May";
		if (rep.getReport_name().contains("/0106"))
			return "Jun";
		if (rep.getReport_name().contains("01/07"))
			return "Jul";
		if (rep.getReport_name().contains("01/08"))
			return "Aug";
		if (rep.getReport_name().contains("01/09"))
			return "Sept";
		if (rep.getReport_name().contains("01/10"))
			return "Oct";
		if (rep.getReport_name().contains("01/11"))
			return "Nov";
		if (rep.getReport_name().contains("01/12"))
			return "Dec";
		return "";
	}

	@FXML
	public void handle_compare_btn(ActionEvent event) {


		XYChart.Series series = new XYChart.Series();

		ObservableList <Report> selected = getSelectedReports();

		for(Report rep : selected) {
			Report toReport = (Report)rep;
			CategoryAxis xAxes = new CategoryAxis();
			XYChart.Data data = new XYChart.Data(getMonth(toReport),toReport.getMonthlyIncome());
			series.getData().add(data);
		}
		Income_chart.getData().addAll(series);

	}
	public ObservableList<Report>getSelectedReports(){
		ObservableList<Report> selected_reports=FXCollections.observableArrayList();
		for(Object report : reports_table.getItems()){
			Report toReport = (Report)report;
			if(toReport.getTo_compare().isSelected()){
				selected_reports.add(toReport);
			}
		}
		return selected_reports;
	}


	@FXML
	public void handle_report_search(ActionEvent event) {

		filtered_list= new FilteredList<Report>(original_list);

		Label error_msg = new Label("Wrong parameters!");
		error_msg.maxHeight(17);
		error_msg.maxWidth(160);
			if(date_picker.getValue().equals(null)){
				reports_table.setItems(original_list);
				return;
			}
			filtered_list.setPredicate(t -> {
				if(t.getReport_name().substring(2,9).equals(date_picker.getValue().toString().substring(2,9)))
					return true;
				else
					return false;
			});
		reports_table.setItems(filtered_list);
	}

	@FXML
	public void handle_clear_charts(ActionEvent mouseEvent) {
		Income_chart.getData().clear();
	}

	public TextArea getTexArea(){
		return this.report_content;
	}
	public void closeFileContent(){
		view_content_pormpt.setVisible(false);
	}

	public List<String> read(File file) {
		List<String> lines = new ArrayList<String>();
		String line;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));

			while ((line = br.readLine()) != null) {
				lines.add(line+"\n");
			}
			br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return lines;
	}
}
