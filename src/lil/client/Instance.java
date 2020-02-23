package src.lil.client;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import src.lil.models.User;


public class Instance {
    public static ClientConsole clientConsole;
    private static Object currentUser = null;
    private static String id = null;
    private static String response = null;
    private static Object tempReturnValue = null;
    private static String jsonReturnValue = null;
    
    public static void set_id(String _id) {
    	id = _id;
    }
    public static String get_id() {
    	return id;
    }
    public static void reset_id() {
    	id = null;
    }
    public static boolean isVarListener() {
        return varListener.get();
    }

    public static SimpleBooleanProperty varListenerProperty() {
        return varListener;
    }

    public static void setVarListener(boolean varListener) {
        Instance.varListener.set(varListener);
    }

    private static SimpleBooleanProperty varListener = new SimpleBooleanProperty(false);
    public static Object getTempReturnValue() {
        Object temp = tempReturnValue;
        resetTempReturnValue();
        return temp;
    }

    public static void setTempReturnValue(Object tempReturnValue) {
        resetTempReturnValue();
        Instance.tempReturnValue = tempReturnValue;
    }

    public static void resetTempReturnValue(){
        tempReturnValue = null;
    }

    public static ClientConsole getClientConsole() {
        return clientConsole;
    }

    public static void setClientConsole(ClientConsole clientConsole) {
        Instance.clientConsole = clientConsole;
    }

    public static Object getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(Object currentUser) {
        Instance.currentUser = currentUser;
        if (Instance.isVarListener()){
            Instance.varListenerProperty().set(false);
        } else {
            Instance.varListenerProperty().set(true);
        }
    }

    public static String getResponse() {
        return response;
    }

    public static void resetResponse(){
        Instance.response = null;
    }

    public static void setResponse(String response) {
        Instance.response = response;
    }

    public static String getJsonReturnValue() {
        return jsonReturnValue;
    }

    public static void setJsonReturnValue(String jsonReturnValue) {
        Instance.jsonReturnValue = jsonReturnValue;
    }
}
