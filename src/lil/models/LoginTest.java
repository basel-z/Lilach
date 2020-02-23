//
//package src.lil.models;
//
//import static org.junit.Assert.*;
//
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import junit.framework.TestCase;
//import src.lil.Enums.LoginStatus;
//import src.lil.Enums.Role;
//
//public class LoginTest extends TestCase {
//
//	Login new_login = new Login();
//	Map<Integer,Object> connected_map = new HashMap<Integer, Object>();
//	protected void setUp() {
//		new_login.user_login(4444,"4066", connected_map);
//	}
//	@Test
//	public void test_login() throws SQLException {
////		Assert.assertTrue(new_login.user_login(4444,"4066", connected_map)==LoginStatus.Successful);
//		Assert.assertTrue(new_login.user_login(4444,"4066",connected_map)==LoginStatus.AlreadyIn);
//		Assert.assertTrue(connected_map.size()==1);
//		Assert.assertTrue(new_login.user_login(1,"",connected_map)==LoginStatus.Successful);
//		Assert.assertTrue(new_login.user_login(44,"44",connected_map)==LoginStatus.WrongCrad);
//		Assert.assertTrue(connected_map.size()==2);
//		Assert.assertTrue(connected_map.containsKey(1)==true);
//		Assert.assertTrue(new_login.user_logout(4444, connected_map)== LoginStatus.Successful);
//		Assert.assertTrue(new_login.user_logout(1, connected_map)==LoginStatus.Successful);
//		Assert.assertTrue(new_login.user_logout(1, connected_map)==LoginStatus.NotSignedIn);
//	}
//}
//
