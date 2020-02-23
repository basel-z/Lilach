//package src.lil.models;
//
//
//import src.lil.Enums.Role;
//import src.lil.Enums.SubscriptionType;
//import org.testng.annotations.Test;
//
//
//public class Test {
//
//    @Test
//    public void addClient() throws Exception{
//        Client addToDb = new Client(4444,"Basel","0527333603","2515188",
//                "Osfia","zaadsa@gmail.com","4066", SubscriptionType.Monthly,"");
//        if(!(addToDb.register())) {System.out.println("Failed");}
//    }
//
//    @Test
//    public void addEmployee() throws Exception{
//        StoreManger storeManger = new StoreManger(31,"name","phone","?","email@email.com","", Role.StoreManger);
//        if(!(storeManger.register())) {System.out.println("Failed to Add");}
//
//    }
//
//    @Test
//    public void changeClientAndCreateChainManger() throws Exception{
//        Client addToDb = new Client(313440,"Basel","0527333603","2515188",
//                "Osfia","zaadsa@gmail.com","4066", SubscriptionType.Monthly,"1234");
//        ChainManger baselTheBoss = new ChainManger(1,"BaselTheBoss","444","","","");
//        if(!baselTheBoss.register()){System.out.println("Failed To Register");};
//        if(!baselTheBoss.updateUser(addToDb)){System.out.println("Failed");}
//    }
//
//    @Test
//    public void checkAddClientAndThenDelete() throws Exception{
//        Client addToDb = new Client(3136760,"Bassdfsdagdafgel","0527333603","2515188",
//                "Osfia","zaadsa@gmail.com","4066", SubscriptionType.Monthly,"");
//        if(!(addToDb.register())) {System.out.println("Failed to Add");}
//        ChainManger baselTheBoss = new ChainManger(1,"BaselTheBoss","444","","","");
//        if(!baselTheBoss.DeleteUser(addToDb.getUserId(), true)){System.out.println("Failed to Delete");}
//    }
//
//    @Test
//    public void checkAddEmployeeAndThenDelete()  throws Exception{
//        StoreManger storeManger = new StoreManger(3,"name","phone","?","email@email.com","", Role.StoreManger);
//        if(!(storeManger.register())) {System.out.println("Failed to Add");}
//        ChainManger baselTheBoss = new ChainManger(1,"BaselTheBoss","444","","","");
//        if(!baselTheBoss.DeleteUser(storeManger.getUserId(), false)){System.out.println("Failed to Delete");}
//    }
//
//    @Test
//    public void checkGetEmployeeByID() throws Exception{
//        ChainManger baselTheBoss = new ChainManger(1,"BaselTheBoss","444","","","");
//        StoreManger storeManger = new StoreManger(31,"name","phone","?","email@email.com","", Role.StoreManger);
//        Employee e = baselTheBoss.getEmployeeById(31);
//        if(!e.equals(storeManger)){System.out.println("Failed to get sameUser");}
//    }
//
//    @Test
//    public void checkGetClientByID() throws Exception{
//        ChainManger baselTheBoss = new ChainManger(1,"BaselTheBoss","444","","","");
//        Client client = new Client(313440,"Basel","0527333603","2515188",
//                "Osfia","zaadsa@gmail.com","4066", SubscriptionType.Monthly,"1234");
//        Client c = baselTheBoss.getClientById(313440);
//        if(!c.equals(client)){System.out.println("Failed to get sameUser");}
//    }
//
//    @Test
//    public void checkChangeRuleAndSetUserAbilityToOrder(){
//        ChainManger baselTheBoss = new ChainManger(1,"BaselTheBoss","444","","","");
//        baselTheBoss.changeRule(31,Role.Employee);
//        baselTheBoss.setUserAbilityToOrder(313440,false);
//    }
//
////    @Test
////    public void checkEmployeeUpdatePrice(){
////        ChainManger baselTheBoss = new ChainManger(1,"BaselTheBoss","444","","","");
////        Item item = new Item(1,)
////
////    }
//}
