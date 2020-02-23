//package src.lil.models;
//
//
//import static org.junit.Assert.assertEquals;
//
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import org.junit.jupiter.api.Test;
//import src.lil.common.DBConnection;
//import src.lil.models.Order.NotFound;
//import org.junit.jupiter.api.BeforeEach;
//
//
//class ComplainTest {
//
//	@BeforeEach
//	void setUp() throws Exception {
//	}
//
//	@Test
//	void testAddComplain() throws SQLException, NotFound, IOException {
//		Complain comp = new Complain();
//		comp.addComplain("fuck you", "Fuck you text Fuck you", "sa3eed@gmail.com", "0525554658", "Haifa 420");
//		 
//		Connection conn = DBConnection.getInstance().getConnection();
//		Statement stmt = conn.createStatement();
//		
//		 String sql = "SELECT * FROM complains WHERE 'complain_id' = 3";
//		 ResultSet rs = stmt.executeQuery(sql);
//		 assertEquals(rs.getInt("complain_id"),4);
//		 rs.close();
//		 stmt.close();
//		 conn.close();
//	}
//
//}
