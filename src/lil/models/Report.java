package src.lil.models;


import java.util.Map;

import com.google.protobuf.MapEntry;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import src.lil.common.DBConnection;
import src.lil.controllers.ReportInterface;
import src.lil.models.Order.NotFound;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Report implements ReportInterface {
	public Integer getComplains_count;
	private File monthlyReport,quarterReport,complainsReport;
	private Date lastUpdated = null;
	private String report_name,type;
	public CheckBox to_compare;
	public Button view_report;
	private Double monthly_income;
	private int  complains_count;
	private File file_content;
	public Report() {

	}

	public Report(ResultSet rs) throws SQLException{
		//super();
		this.fillFieldsFromResultSet(rs);
	}

	public static List<Report> getReports() {
		//filters
			List<Report> reports=new ArrayList<Report>();
			try (Connection db = DBConnection.getInstance().getConnection();){
				try(ResultSet rs = db.prepareStatement("SELECT * FROM reports").executeQuery()){
					while (rs.next()) {
						Report repo = new Report(rs);reports.add(repo);
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
					return null;
				}
				db.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return null;
			}
			return reports;
		}


	public void fillFieldsFromResultSet(ResultSet rs) throws SQLException{
		setReport_name(rs.getString("report_name"));
		setReport_type(rs.getString("report_type"));
		setMonthly_income(rs.getDouble("monthly_income"));
		setTo_compare(new CheckBox());
		setView_report(new Button("View"));
		setComplains_count(rs.getInt("complains_count"));
		getView_report().setStyle("-fx-background-color: #FFA500");
		File reports_directory;
		if(getReport_type().equals("Monthly")) {
			setFile_content(new File("reports/MonthlyReports/"+getReport_name().replace("/","")+".txt"));
		}else{
			setFile_content(new File("reports/QuarterReports/"+getReport_name().replace("/","")+".txt"));
		}
	}

	public CheckBox getTo_compare(){
		return this.to_compare;
	}
	public void setTo_compare(CheckBox cb){
		this.to_compare = cb;
	}
	public void addReport(Double monthlyIncome,int complainsCount){

	}
	public void prepareMonthlyReport() throws SQLException, NotFound, IOException{
		
		String sql= null;
		String pattern = "ddMMyyyymm";
		String user_id,item,order_date,contact_phone,delivery_location,contact_name,order_price,store_id = null;
		int order_id;
		// Create an instance of SimpleDateFormat used for formatting 
		// the string representation of date according to the chosen pattern
		DateFormat df = new SimpleDateFormat(pattern);
		this.report_name=df.format(new Date())+".txt";
		String monthly_path="src/lil/models/reports/MonthlyReports/"+report_name;
		this.type="Monthly";
//		Open a text file and create an output stream to Store the income data  
		File targetFile = new File(monthly_path);
		targetFile.createNewFile();
		FileWriter fileWriter = new FileWriter(targetFile);

//	    Read Orders IDs and the amount of $ paid from the result set
	    try (Connection conn = DBConnection.getInstance().getConnection()){
	    	  try {
	    		  Statement stmt = conn.createStatement();
	    		  sql = "SELECT * FROM orders";
	    		  ResultSet rs = stmt.executeQuery(sql);

	    		  fileWriter.write("Order date ----- " + " ----- Order id ----- ----- Item ----- ----- Total Cost ----- ----- Delivery Location\n");
	    		  while(rs.next()) {
					  if ("1".equals(rs.getString("store_id"))) {
						  user_id = rs.getString("user_id");
						  order_id = rs.getInt("order_Id");
						  contact_phone = rs.getString("receiver_phone");
						  delivery_location = rs.getString("delivery_location");
						  store_id = rs.getString("store_id");
						  contact_name = rs.getString("contact_name");
						  order_date = rs.getString("order_Date");
						  order_price = rs.getString("order_price");
						  String finalOrderInfo = " | " + order_date + " | " + order_id + " | " + store_id + " | " + order_price + " | " + delivery_location +"\n";
						  try {
							  fileWriter.write(finalOrderInfo);
						  } catch (IOException e) {
							  // TODO Auto-generated catch block
							  fileWriter.write(e.toString());
						  }
					  }
				  }
	    		    try {
	    		    	fileWriter.write("COMPLAINS REPORT:\n");
	    		    	fileWriter.write(usingBufferedReader(this.complainsReport.toString()));
	    		    } catch (IOException e) {
	    		        // TODO Auto-generated catch block
	    		        e.printStackTrace();
	    		    }
    			  fileWriter.close();
    			  rs.close();
    			  stmt.close();
    			  conn.close();
	    		  this.monthlyReport=targetFile;
	            }catch(SQLException se) {
	            	fileWriter.write(se.toString());
	            	fileWriter.write("SQLException: ");
	            	fileWriter.write(se.getMessage());
	            	fileWriter.write("SQLState: ");
	            	fileWriter.write(se.getSQLState());
	            	fileWriter.write("VendorError: ");
	            	fileWriter.write(se.getErrorCode());
	            }
	    }catch(SQLException se) {
        	fileWriter.write(se.toString());
        	fileWriter.write("SQLException: ");
        	fileWriter.write(se.getMessage());
        	fileWriter.write("SQLState: ");
        	fileWriter.write(se.getSQLState());
        	fileWriter.write("VendorError: ");
        	fileWriter.write(se.getErrorCode());
        }
	}

	public void prepareComplainsReport() throws SQLException, NotFound, IOException{
		
		String sql= null;
		Date date;
		String pattern = "ddMMyyyymm";

		// Create an instance of SimpleDateFormat used for formatting 
		// the string representation of date according to the chosen pattern
		DateFormat df = new SimpleDateFormat(pattern);
		int complain_id;
		String complain_title,complain_text,store_adress,dateAsString=null,contact_phone,contact_email;
		String path="src/lil/models/reports/ComplainsReports/"+df.format(new Date())+".txt";
//		Open a text file and create an output stream to Store the income data
		File targetFile = new File(path);
		targetFile.createNewFile();
		FileWriter fileWriter = new FileWriter(targetFile);
		
//	    Read Orders IDs and the amount of $ paid from the result set
	    	 try {
	    		  Connection conn = DBConnection.getInstance().getConnection();
	    		  Statement stmt = conn.createStatement();
	    		  sql = "SELECT * FROM complains";
	    		  ResultSet rs = stmt.executeQuery(sql);
	    		  while(rs.next()) {
	    			  complain_id = rs.getInt("complain_id");
	    			  complain_title = rs.getString("complain_title");
	    			  date = rs.getDate("complain_date");
	    			  if(date!=null) 
	    				  dateAsString = df.format(date);
	    			  complain_text = rs.getString("complain_text");
	    			  store_adress = rs.getString("store_adress");
	    			  contact_email = rs.getString("contact_email");
	    			  contact_phone = rs.getString("contact_phone");
	    			  String finalOrderInfo = "Complain date: " + dateAsString + " Complain id: " + complain_id + " Complain title: " + complain_title + " Store adress: " + store_adress + " Phone: " + contact_phone + " Email: " + contact_email
	    					  +"\n" + "Complain content: " + complain_text + "\n";
	    			  try {
	    			  fileWriter.write(finalOrderInfo);
	    			  fileWriter.write("------------------------------------\n");
	    			  } catch (IOException e) {
						// TODO Auto-generated catch block
	    				  fileWriter.write(e.toString());
					  }
	    		  }
	    		  this.complainsReport=targetFile;
    			  fileWriter.close();
    			  rs.close();
    			  stmt.close();
    			  conn.close();	 
	            }catch(SQLException se) {
	            	fileWriter.write(se.toString());
	            	fileWriter.write("SQLException: ");
	            	fileWriter.write(se.getMessage());
	            	fileWriter.write("SQLState: ");
	            	fileWriter.write(se.getSQLState());
	            	fileWriter.write("VendorError: ");
	            	fileWriter.write(se.getErrorCode());
	            }

	    }


	public void prepareQuarterReport() throws SQLException, NotFound, IOException{

		// Create an instance of SimpleDateFormat used for formatting 
		// the string representation of date according to the chosen pattern    
		String pattern = "ddMMyyyy";
		DateFormat df = new SimpleDateFormat(pattern);
		//Maps that contain the store name as a key, and as a value it contains either a certain store income/ratio
		Float total_store_income=Float.parseFloat("0");
		int subscribers_count=0;
		Map monthly_stores_income=new HashMap(),monthly_stores_complains=new HashMap(),stores_complains_ratio=new HashMap();
		double complains_compare_rati1,complain_compare_ratio2,quarter_income_ratio1=0,quarter_income_ratio2=0;
		String sql= null;
		
//		Open a text file and create an output stream to store income data  
		File targetFile = new File("src/lil/models/reports/QuarterReports/"+df.format(new Date())+".txt");
		targetFile.createNewFile();
		FileWriter fileWriter = new FileWriter(targetFile);

    	 try {
    		  Connection conn = DBConnection.getInstance().getConnection();
    		  Statement stmt = conn.createStatement();
    		  
    		  sql = "SELECT * FROM reports";
    		  ResultSet rs = stmt.executeQuery(sql);
    		  while(rs.next()){
    		  	if(rs.getString(3).equals("Monthly")) {
					monthly_stores_income.put(rs.getString(2).replace("/", ""), rs.getFloat(4));
					total_store_income += rs.getFloat((4));
					monthly_stores_complains.put(rs.getString(2).replace("/", ""), rs.getInt(5));
				}
    		  }

    		  sql = "SELECT * FROM clients";
    		  rs=stmt.executeQuery(sql);
			 while (rs.next())
    		  	subscribers_count+=1;

			  try {
				  fileWriter.write("Quarter report for last 3 months. Date updated: 01/02/2020\n\n");
				  fileWriter.write("Monthly Stores Income: \n");
				  fileWriter.write("Date     Income\n");
				  Iterator iterator1 = monthly_stores_income.entrySet().iterator();
				  List<Double> ratio_elements = new ArrayList();
				  while (iterator1.hasNext()) {
					  Map.Entry pair = (Map.Entry)iterator1.next();
					  fileWriter.write(pair.getKey()+"         "+pair.getValue()+"\n");
					  ratio_elements.add(Double.parseDouble(pair.getValue().toString()));
				  }
				  quarter_income_ratio1=ratio_elements.get(1)/ratio_elements.get(0);
				  quarter_income_ratio2=ratio_elements.get(2)/ratio_elements.get(1);
				  fileWriter.write("\nTotal network income: "+ total_store_income+"\n");
				  fileWriter.write("Income Ratio: \n"+"First/Second: "+quarter_income_ratio1+ " Third/Second: "+quarter_income_ratio2+"\n");

				  fileWriter.write("\nAll monthly Reports:\n");
				  
			  } catch (IOException e) {
				// TODO Auto-generated catch block
				  fileWriter.write(e.toString());
			  }
    		  this.quarterReport=targetFile;
			  fileWriter.close();
			  rs.close();
			  stmt.close();
			  conn.close();	 
            }catch(SQLException se) {
            	fileWriter.write(se.toString());
            	fileWriter.write("SQLException: ");
            	fileWriter.write(se.getMessage());
            	fileWriter.write("SQLState: ");
            	fileWriter.write(se.getSQLState());
            	fileWriter.write("VendorError: ");
            	fileWriter.write(se.getErrorCode());
            	fileWriter.close();
            }
	}
	
	private static String usingBufferedReader(String filePath) 
	{
	    StringBuilder contentBuilder = new StringBuilder();
	    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
	    {
	 
	        String sCurrentLine;
	        while ((sCurrentLine = br.readLine()) != null) 
	        {
	            contentBuilder.append(sCurrentLine).append("\n");
	        }
	    } 
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	    return contentBuilder.toString();
	}
	
	public void sendStoreMonthlyReport() {
		  Properties props = System.getProperties();
	        String host = "smtp.gmail.com";
	        props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.smtp.host", host);
	        props.put("mail.smtp.user", "lilach.ltd");
	        props.put("mail.smtp.password", "");
	        props.put("mail.smtp.port", "587");
	        props.put("mail.smtp.auth", "true");

	        Session session = Session.getDefaultInstance(props);
	        MimeMessage message = new MimeMessage(session);

	        try {
	            String from="lilach.ltd@gmail.com";
	            String[] gmail2={"aliawidat1@gmail.com"};
	            message.setFrom(new InternetAddress(from));
	            InternetAddress[] toAddress = new InternetAddress[gmail2.length];

	            // To get the array of addresses
	            for( int i = 0; i < gmail2.length; i++ ) {
	                toAddress[i] = new InternetAddress(gmail2[i]);
	            }

	            for (InternetAddress address : toAddress) {
	                message.addRecipient(Message.RecipientType.TO, address);
	            }

	            message.setSubject("Store Monthly Report!");
	            message.setText("You're strore's monthly orders and income report, is here!\n"+ usingBufferedReader(this.monthlyReport.toString()));
	            Transport transport = session.getTransport("smtp");
	            transport.connect(host, "lilach.ltd@gmail.com", "umsrnjzmyvmkttyh");
	            transport.sendMessage(message, message.getAllRecipients());
	            transport.close();

	        } catch (MessagingException ae) {
	            ae.printStackTrace();
	        }
	   }
	public void sendQuarterReport() {
		  Properties props = System.getProperties();
	        String host = "smtp.gmail.com";
	        props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.smtp.host", host);
	        props.put("mail.smtp.user", "lilach.ltd");
	        props.put("mail.smtp.password", "");
	        props.put("mail.smtp.port", "587");
	        props.put("mail.smtp.auth", "true");

	        Session session = Session.getDefaultInstance(props);
	        MimeMessage message = new MimeMessage(session);

	        try {
	            String from="lilach.ltd@gmail.com";
	            String[] gmail2={"aliawidat1@gmail.com"};
	            message.setFrom(new InternetAddress(from));
	            InternetAddress[] toAddress = new InternetAddress[gmail2.length];

	            // To get the array of addresses
	            for( int i = 0; i < gmail2.length; i++ ) {
	                toAddress[i] = new InternetAddress(gmail2[i]);
	            }

	            for (InternetAddress address : toAddress) {
	                message.addRecipient(Message.RecipientType.TO, address);
	            }

	            message.setSubject("Network Quarter Report!");
	            message.setText("Dear network manager, quarter report numbers are here!\n"+ usingBufferedReader(quarterReport.toString()));
	            Transport transport = session.getTransport("smtp");
	            transport.connect(host, "lilach.ltd@gmail.com", "umsrnjzmyvmkttyh");
	            transport.sendMessage(message, message.getAllRecipients());
	            transport.close();
	        } catch (MessagingException ae) {
	            ae.printStackTrace();
	        }
	   }

	public File getMonthlyReport() { return this.monthlyReport; }
	public File getQuarterReport() { return this.quarterReport; }
	public File getComplainsReport() { return this.complainsReport; }
	public Double getMonthlyIncome() {
		return this.monthly_income;
	}
	public String getReport_name(){
		return this.report_name;
	}
	public String getReport_type() {
		return this.type;
	}
    public Integer getComplains_count(){return this.complains_count;}
	public Button getView_report() { return view_report; }
	public File getFile_content(){return this.file_content;}

	public void setFile_content(File con){ this.file_content=con;}
	public void setReport_name(String report_name) { this.report_name = report_name; }
	public void setView_report(Button viewReport){ this.view_report=viewReport; }
	public void setMonthly_income(Double monthly_income) { this.monthly_income = monthly_income; }
	public void setReport_type(String st) {
		this.type = st;
	}
	public void setComplains_count(int complains_count) { this.complains_count = complains_count; }
}
