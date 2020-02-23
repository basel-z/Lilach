package src.lil.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import src.lil.Enums.SubscriptionType;
import src.lil.models.Client;

public class createClientTest {
    @Test
    public void createClientThatShouldWork(){
        try {
            Client client = new Client(2095959595,"Nimer Shiech", "05275668421","","","z.basel324@gmail.com","4066", SubscriptionType.Monthly,"","2",0.0);
            Assert.assertTrue(client.register(),"Failed to create a valid client");
        }
        catch (Exception e){
            Assert.fail("Failed to create a valid client");
        }
    }

    @Test
    public void createClientAlreadyRegister(){
        try {
            Client client = new Client(2095959595,"Nimer Shiech", "05275668421","","","z.basel324@gmail.com","4066", SubscriptionType.Monthly,"","2",0.0);
            Assert.assertTrue(!client.register(),"Succeeded to create client with the same ID");
        }
        catch (Exception e){
            if(!e.getMessage().equals("AlreadyExists"))
                Assert.fail("Succeeded to create client with the same ID");
        }
    }

    @Test
    public void createClientIllegalId(){
        try {
            Client client = new Client(54534,"Nimer Shiech", "05275668421","","","z.basel324@gmail.com","4066", SubscriptionType.Monthly,"","2",0.0);
            Assert.assertTrue(!client.register(),"Succeeded to create client with the illegal ID");
        }
        catch (Exception e){
            if(!e.getMessage().equals("Illegal id, less than 9 digits"))
                Assert.fail("Succeeded to create client with the illegal ID");
        }
    }

    @Test
    public void createClientIllegalArguments(){
        try {
            Client client = new Client(545341561,"Nimer Shiech", "05275668421","","","z.basel324@gmail.com","4066", SubscriptionType.Monthly,"","2",0.0);
            Assert.assertTrue(!client.register(),"Succeeded to create client with the illegal arguments");
        }
        catch (Exception e){
            if(!e.getMessage().equals("Illegal arguments, all data should be inserted"))
                Assert.fail("Succeeded to create client with the illegal arguments");
        }
    }
}
