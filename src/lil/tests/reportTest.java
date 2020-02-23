//package src.lil.tests;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.io.FileReader;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//
//import org.junit.jupiter.api.Test;
//import org.testng.Assert;
//
//import src.lil.models.Report;
//
//class reportTest {
//
//    @Test
//    public void sendingValidReport(){
//        try {
//
//    		String pattern = "MMddyyyyHHmmss";
//    		DateFormat df = new SimpleDateFormat(pattern);
//            Report validReport = new Report();
//            validReport.prepareMonthlyReport();
//    		String monthly_path="C:\\Users\\ali\\git\\SoftwareEngeneering2020\\src\\lil\\models\\reports\\MonthlyReports\\MonthlyReport01182020195314.txt";
//    	    FileReader fr = new FileReader(monthly_path);
//    	    int i,count=0;
//    	    while ((i=fr.read()) != -1)
//    	       count ++;
//          Assert.assertFalse(count > 0,"Succeeded to send report");
//          fr.close();
//
//        }
//        catch (Exception e){
//            // Test passed
//            System.out.println(e.getMessage());
//        }
//    }
//}
