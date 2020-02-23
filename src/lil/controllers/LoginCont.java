package src.lil.controllers;

import java.sql.SQLException;
import java.util.Map;

import src.lil.Enums.LoginStatus;
import src.lil.Enums.Role;
import src.lil.exceptions.*;

public interface LoginCont {
	public LoginStatus user_login(Integer id,String password);
	public Object check_user(Integer id,String password) throws SQLException, WrongCredentials;
	public Boolean check_connected_users(Integer _id) throws AlreadyLoggedIn;
	public LoginStatus user_logout(Integer id);
}
