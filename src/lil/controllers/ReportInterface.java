package src.lil.controllers;
import java.io.IOException;
import java.sql.SQLException;
import src.lil.models.Order.NotFound;

public interface ReportInterface {
	
	public void prepareComplainsReport() throws SQLException, NotFound, IOException;
	public void prepareMonthlyReport() throws SQLException, NotFound, IOException;
	public void prepareQuarterReport() throws SQLException, NotFound, IOException;
	public void sendStoreMonthlyReport();
	public void sendQuarterReport();
}
