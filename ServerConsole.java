// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
import src.lil.common.ChatIF;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * This class constructs the UI for a chat server.  It implements the
 * chat interface in order to activate the display() method.
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @author Benjamin Bergman
 * @version Oct 2009
 */
public class ServerConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the server that created this EchoServer.
   */
  EchoServer server;

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ServerConsole UI.
   *
   * @param port The port to connect on.
   */
  public ServerConsole(int port) 
  {
    try 
    {
      server = new EchoServer(port, this);
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
    
    try
    {
      server.listen(); //Start listening for connections
    }
    catch (Exception ex)
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }



  //Instance methods ************************************************
  /**
   * This method waits for input from the console.  Once it is
   * received, it sends it to the client's message handler.
   */
  public void accept()
  {
    try
    {
      BufferedReader fromConsole =
        new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true)
      {
        message = fromConsole.readLine();
        server.handleMessageFromServerUI(message);
      }
    }
    catch (Exception ex)
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message)
  {
    System.out.println("> " + message);
  }

  /**
   * This method is responsible for the creation of the Server UI.
   *
   * @param args The port to connect to.
   */
  public static void main(String[] args) {
    int port = 0;  //The port number

    try {
      port = Integer.parseInt(args[0]);
    } catch (Throwable e) {
      port = DEFAULT_PORT;
    }
    ServerConsole server = new ServerConsole(port);
    server.accept();  //Wait for console data

    Calendar today = Calendar.getInstance();
    today.set(Calendar.HOUR_OF_DAY, 2);
    today.set(Calendar.MINUTE, 0);
    today.set(Calendar.SECOND, 0);

    // Every night at 2AM run messaging service
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        ScheduledOps.messageAboutComplain();
      }
    }, today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));

    // check delivery each 1 minute
    Timer timer1 = new Timer();
    timer1.schedule(new TimerTask() {
      public void run() {
        ScheduledOps.sendAboutDelivery();
      }
    }, 0, 60 * 1000);


    // update balances every 1 month
    Timer timer2 = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        ScheduledOps.updateBalanceForMonth();
      }
    }, today.getTime(), TimeUnit.MILLISECONDS.convert(30, TimeUnit.DAYS));

    // update balances every 1 year
    Timer timer3 = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        ScheduledOps.updateBalanceForYear();
      }
    }, today.getTime(), TimeUnit.MILLISECONDS.convert(365, TimeUnit.DAYS));

  }
  //Class methods ***************************************************
}
//End of ServerConsole class