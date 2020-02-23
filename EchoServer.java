
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.*;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import src.lil.client.Instance;

import javafx.util.Pair;


import src.lil.models.*;

import src.ocsf.server.*;

import src.lil.Enums.LoginStatus;
import src.lil.common.ChatIF;
import src.lil.models.*;
import src.lil.models.Order.AlreadyExists;
import src.ocsf.server.AbstractServer;
import src.ocsf.server.ConnectionToClient;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer {
	// Class variables *************************************************

	/**
	 * Log in instance.
	 */
	private Login _login = new Login();
	private Map<String, Integer> store_addresses;
	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ChatIF serverUI;
	static private final String DB = "Bc2JdnNE0Y";
	static private final String DB_URL = "jdbc:mysql://remotemysql.com/" + DB + "?useSSL=false";
	static private final String USER = "Bc2JdnNE0Y";
	static private final String PASS = "PxQYQKGhjQ";
	static private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public EchoServer(int port) {
		super(port);
	}

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port     The port number to connect on.
	 * @param serverUI The interface type variable.
	 */
	public EchoServer(int port, ChatIF serverUI) throws IOException {
		super(port);
		this.serverUI = serverUI;
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) throws Exception {
			Gson gson = new Gson();
			if(String.valueOf(msg).startsWith("SubmitComplain")){
				String complainAsString = String.valueOf(msg).split("SubmitComplain")[1];
				Complain complain = gson.fromJson(complainAsString,Complain.class);
				complain.addComplain();
				return;
			}
			if(String.valueOf(msg).startsWith("SubmitPurchase")) {
				String OrderAsString = String.valueOf(msg).split("SubmitPurchase")[1];
				Order submitOrder = gson.fromJson(OrderAsString, Order.class);
				submitOrder.insertIntoOrders();
				client.sendToClient("successfull");
				return;
			}
		if(String.valueOf(msg).startsWith("TryToDelete")) {
			String OrderAsString = String.valueOf(msg).split("TryToDelete")[1];
			Order tryDelete = gson.fromJson(OrderAsString, Order.class);
			double dele = tryDelete.orderTimeDiff();
			if(dele > 0.0){
				tryDelete.DeleteOrder();
				client.sendToClient("successfull" + "This order hsa been canceled " + " your refund is:" +dele);
				return;
			}
			client.sendToClient("you cant cancel this order");
			return;
		}
			if(msg.toString().startsWith("GetAllComplains")){
				try {
					String complainAsString = String.valueOf(msg).split("GetAllComplains")[1];
					customerService service = gson.fromJson(complainAsString,customerService.class);
					String returnValue = "";
					for(Complain complain: service.getComplains()){
						returnValue += complain.getComplain_title() + " ~ ID: " + complain.getComplainId() +",";
					}
					returnValue = returnValue.substring(0, returnValue.length() - 1);
					client.sendToClient(returnValue);
					return;
				}
				catch (Exception e){
					client.sendToClient("error");
				}
			}
			if(msg.toString().startsWith("GetAllBalances")){
				try {
					String complainAsString = String.valueOf(msg).split("GetAllBalances")[1];
					ChainManger chainManger = gson.fromJson(complainAsString, ChainManger.class);
					client.sendToClient(chainManger.getAllUsersWithBalance());
					return;
				}
				catch (Exception e){
					client.sendToClient("error");
				}
			}
			if(msg.toString().startsWith("BlockClientById")){
				try {
					String complainAsString = String.valueOf(msg).split("BlockClientById")[1].split("@")[0];
					String id = String.valueOf(msg).split("BlockClientById")[1].split("@")[1];
					ChainManger chainManger = gson.fromJson(complainAsString, ChainManger.class);
					if(chainManger.setUserAbilityToOrder(Integer.parseInt(id),true))
						client.sendToClient("AllGood");
					else
						client.sendToClient("error");
					return;
				}
				catch (Exception e){
					client.sendToClient("error");
				}
			}
			if(msg.toString().startsWith("PayBalanceForUser")){
				try {
					String complainAsString = String.valueOf(msg).split("PayBalanceForUser")[1];
					Client client1 = gson.fromJson(complainAsString, Client.class);
					if(client1.pay(client1.getBalance()))
						client.sendToClient("AllGood");
					else
						client.sendToClient("error");
					return;
				}
				catch (Exception e){
					client.sendToClient("error");
				}
			}
		if(msg.toString().startsWith("GetComplainById")){
			try {
				String temp = String.valueOf(msg).split("GetComplainById")[1];
				String id = temp.split(";")[0];
				customerService service = gson.fromJson(temp.split(";")[1],customerService.class);
				String returnValue = gson.toJson(service.getComplainById(Integer.parseInt(id)));
				client.sendToClient(returnValue);
				return;
			}
			catch (Exception e){
				client.sendToClient("error");
			}
		}
		if(msg.toString().startsWith("SetComplainReply")){
			try {
				String temp = String.valueOf(msg).split("SetComplainReply")[1];
				String id = temp.split(";")[0];
				customerService service = gson.fromJson(temp.split(";")[1],customerService.class);
				Complain complain = service.getComplainById(Integer.parseInt(id));
				if(service.replyComplain(complain,temp.split(";")[2],Double.parseDouble(temp.split(";")[3])))client.sendToClient("AllDone");
				else client.sendToClient("error");
				return;
			}
			catch (Exception e){
				client.sendToClient("error");
			}
		}
		if (msg.toString().startsWith("Login ")) {

			Integer user_id;
			String password;
			StringTokenizer login_tokens = new StringTokenizer(msg.toString(), " ");
			login_tokens.nextToken();
			user_id = Integer.parseInt(login_tokens.nextToken());
			password = login_tokens.nextToken();
			LoginStatus status = this._login.user_login(user_id, password);
			if (status == LoginStatus.AlreadyIn) {
				try {
					client.sendToClient("User already signed in!");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (status == LoginStatus.WrongCrad) {
				try {
					client.sendToClient("Wrong ID or Passowrd! try again:");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					Object user = _login.get_object(user_id);
					String json = gson.toJson(user);
					String rule =  user.getClass().getName();
					client.sendToClient("successful " +rule + "SPACE" +  json);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (msg.toString().startsWith("Logout ")) {
			StringTokenizer login_tokens = new StringTokenizer(msg.toString(), " ");
			login_tokens.nextToken();
			Integer user_id = Integer.parseInt(login_tokens.nextToken());
			try {
				this._login.disconnect_user(user_id);
				client.sendToClient("successful");
			} catch (Exception e) {
				try {
					client.sendToClient("successful");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else if (msg.toString().startsWith("get stores")) {
			store_addresses = Store.get_store_addresses();
			List<String> addresses = new ArrayList<String>();
			for (Map.Entry<String, Integer> entry : store_addresses.entrySet()) {
				addresses.add(entry.getKey() + "-" + entry.getValue());
			}
			String json = gson.toJson(addresses);
			try {
				client.sendToClient(json);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

			else if (msg.toString().startsWith("getstoreid ")) {
			try {
				client.sendToClient(store_addresses.get(msg.toString().substring(11, msg.toString().length())));
			} catch (IOException e) {
				e.printStackTrace();
			}


		} else if (msg.toString().startsWith("register ")) {
			Client reg = gson.fromJson(msg.toString().substring("register ".length(), msg.toString().length()),
					Client.class);
			try {
				reg.register();
				client.sendToClient("successfull");
			} catch (AlreadyExists e) {
				client.sendToClient("User with same ID already exists!");
			} catch (SQLException e) {
				client.sendToClient("SQL Exception!");
			} catch (Exception e) {

				client.sendToClient(e.toString());
			}
		} else if (msg.toString().equals("getallclients")) {
			List<Client> clients_list = User.get_all_clients();
			String json = gson.toJson(clients_list);
			client.sendToClient(json);
		}else if (msg.toString().equals("getallemployees")) {
			List<Object> employees_list = User.get_all_employees();
			client.sendToClient(gson.toJson(employees_list));
		}else if (msg.toString().startsWith("update")) {

			String[] parms = msg.toString().split(" ");
			
		
			ChainManger manager = (ChainManger) _login.get_object(Integer.parseInt(parms[1]));
			if(parms[2].equals("true")) {
				manager.updateUser(gson.fromJson(parms[3], Client.class));
			}else {
				manager.updateUser(gson.fromJson(parms[3], Employee.class));
			}
			
			client.sendToClient("successful.");
			return;
		}else if(msg.toString().startsWith("items Update all in :")) {
			String[] client_msg = msg.toString().split(":");
			Store curr_store = new Store();
			curr_store.set_id(Integer.parseInt(client_msg[1]));
			if(curr_store.change_price_all_items((int)Double.parseDouble(client_msg[2]))) {
				client.sendToClient("successful.");
				return;
			}
			client.sendToClient("ERROR!");
			return;
		}
		else if(msg.toString().startsWith("item update :")){
			String[] client_msg = msg.toString().split(":");
			Store curr_store = new Store();
			curr_store.set_id(Integer.parseInt(client_msg[1]));
			Type list_type_Object = new TypeToken<List<Integer>>() {
			}.getType();
			List<Integer> item_ids = gson.fromJson(client_msg[2],list_type_Object);
			if(curr_store.update_items_price(item_ids, Integer.parseInt(client_msg[3]))) {
				client.sendToClient("successful.");
				return;
			}
			client.sendToClient("ERROR!");
			return;
			
		}

		else if (msg.toString().startsWith("#login ")) {
			if (client.getInfo("loginID") != null) {
				try {
					client.sendToClient("You are already logged in.");
				} catch (IOException e) {
				}
				return;
			}
			client.setInfo("loginID", msg.toString().substring(7));
		} else {
			if (client.getInfo("loginID") == null) {
				try {
					client.sendToClient("You need to login before you can chat.");
					client.close();
				} catch (IOException e) {
				}
				return;
			}
			System.out.println("Message received: " + msg + " from \"" + client.getInfo("loginID") + "\" " + client);
			this.sendToAllClients(client.getInfo("loginID") + " > reqeste is being processed by the server: " + msg);
			String[] arguments = ((String) msg).split("\\s+");
			Connection connection = null;
			Statement statement = null;
			try {
				/* RUNTIME ERROR */
				Class.forName(JDBC_DRIVER);
				connection = DriverManager.getConnection(DB_URL, USER, PASS);
				statement = connection.createStatement();
				System.out.println(arguments[0]);
				if (arguments[0].equalsIgnoreCase("displayItems")) {
					String sql = "SELECT * FROM items";
					ResultSet rs = statement.executeQuery(sql);
					this.sendToAllClients("\n====================\n");
					this.sendToAllClients("ID  | Type |  Price\n");
					while (rs.next()) {
						int item_Id = rs.getInt("item_Id");
						String item_type = rs.getString("item_type");
						int item_price = rs.getInt("item_price");
						this.sendToAllClients("--------------------\n");
						this.sendToAllClients("" + item_Id + " " + item_type + " " + item_price + "\n");
					}
					rs.close();
				} else if (arguments[0].equalsIgnoreCase("PriceUpdate")) {
					int item_price = Integer.parseInt(arguments[2]);
					int item_Id = Integer.parseInt(arguments[1]);
					PreparedStatement updatePrice = connection
							.prepareStatement("UPDATE items SET item_price=? ,updated=? WHERE item_Id=?");
					updatePrice.setInt(1, item_price);
					updatePrice.setInt(2, 1);
					updatePrice.setInt(3, item_Id);
					updatePrice.executeUpdate();
					this.sendToAllClients(
							"Server Updated price in database for item:" + item_Id + " to:" + item_price + "\n");
				} else if (arguments[0].equalsIgnoreCase("getUpdatedInfo")) {
					String sql = "SELECT * FROM items WHERE updated=1";
					ResultSet rs = statement.executeQuery(sql);
					this.sendToAllClients("\n====================\n");
					this.sendToAllClients("ID   Type   Price\n");
					while (rs.next()) {
						int item_Id = rs.getInt("item_Id");
						String item_type = rs.getString("item_type");
						int item_price = rs.getInt("item_price");
						this.sendToAllClients("--------------------\n");

						this.sendToAllClients("" + item_Id + " " + item_type + " " + item_price + "\n");
					}
					rs.close();
				} else {
					this.sendToAllClients("Server couldn't recognize your command!\n");
				}

				statement.close();
				connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
				System.out.println("SQLException: " + se.getMessage());
				System.out.println("SQLState: " + se.getSQLState());
				System.out.println("VendorError: " + se.getErrorCode());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (statement != null)
						statement.close();
					if (connection != null)
						connection.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
		}
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI
	 */
	public void handleMessageFromServerUI(String message) {
		if (message.charAt(0) == '#') {
			runCommand(message);
		} else {
			// send message to clients
			serverUI.display(message);
			this.sendToAllClients("SERVER MSG> " + message);
		}
	}

	/**
	 * This method executes server commands.
	 *
	 * @param message String from the server console.
	 */
	private void runCommand(String message) {
		// run commands
		// a series of if statements

		if (message.equalsIgnoreCase("#quit")) {
			quit();
		} else if (message.equalsIgnoreCase("#stop")) {
			stopListening();
		} else if (message.equalsIgnoreCase("#close")) {
			try {
				close();
			} catch (IOException e) {
			}
		} else if (message.toLowerCase().startsWith("#setport")) {
			if (getNumberOfClients() == 0 && !isListening()) {
				// If there are no connected clients and we are not
				// listening for new ones, assume server closed.
				// A more exact way to determine this was not obvious and
				// time was limited.
				int newPort = Integer.parseInt(message.substring(9));
				setPort(newPort);
				// error checking should be added
				serverUI.display("Server port changed to " + getPort());
			} else {
				serverUI.display("The server is not closed. Port cannot be changed.");
			}
		} else if (message.equalsIgnoreCase("#start")) {
			if (!isListening()) {
				try {
					listen();
				} catch (Exception ex) {
					serverUI.display("Error - Could not listen for clients!");
				}
			} else {
				serverUI.display("The server is already listening for clients.");
			}
		} else if (message.equalsIgnoreCase("#getport")) {
			serverUI.display("Currently port: " + Integer.toString(getPort()));
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	/**
	 * Run when new clients are connected. Implemented by Benjamin Bergman, Oct 22,
	 * 2009.
	 *
	 * @param client the connection connected to the client
	 */
	protected void clientConnected(ConnectionToClient client) {
		// display on server and clients that the client has connected.
		String msg = "A Client has connected";
		System.out.println(msg);
		this.sendToAllClients(msg);
	}

	/**
	 * Run when clients disconnect. Implemented by Benjamin Bergman, Oct 22, 2009
	 *
	 * @param client the connection with the client
	 */
	synchronized protected void clientDisconnected(ConnectionToClient client) {
		// display on server and clients when a user disconnects
		String msg = client.getInfo("loginID").toString() + " has disconnected";

		System.out.println(msg);
		this.sendToAllClients(msg);
	}

	/**
	 * Run when a client suddenly disconnects. Implemented by Benjamin Bergman, Oct
	 * 22, 2009
	 *
	 * @param client    the client that raised the exception
	 * @param Throwable the exception thrown
	 */
	synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
		String msg = client.getInfo("loginID").toString() + " has disconnected";

		System.out.println(msg);
		this.sendToAllClients(msg);
	}

	/**
	 * This method terminates the server.
	 */
	public void quit() {
		try {
			close();
		} catch (IOException e) {
		}
		System.exit(0);
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the server instance (there is
	 * no UI in this phase).
	 *
	 * @param args[0] The port number to listen on. Defaults to 5555 if no argument
	 *                is entered.
	 */
	public static void main(String[] args) {
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}

		EchoServer sv = new EchoServer(port);

		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}
}
//End of EchoServer class