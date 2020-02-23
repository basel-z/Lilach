package src.lil.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import src.lil.models.Order;

import java.sql.SQLException;

public class createOrderTest {
    @Test
    public void createOrderShouldPassWithDelivery() throws SQLException, Order.AlreadyExists, Order.NotFound {
        try {
            Order order = new Order(2,"Basel","052","20:33:00","22-03-2021",false,"",true,"Osfia","28-01-2020","1");
            order.addToCart(3);
            Assert.assertTrue(order.insertIntoOrders(),"Failed to create an order");
        }
        catch (Exception e){
            Assert.fail("Failed to create an order");
        }
    }

    @Test
    public void createOrderWithEmptyCart() throws SQLException, Order.AlreadyExists, Order.NotFound {
        try {
            Order order = new Order(2,"Basel","052","20:33","22-03-2021",false,"",true,"Osfia","28-01-2020","1");
            Assert.assertTrue(order.insertIntoOrders(),"Succeeded to create an order with an empty cart ");
        }
        catch (Exception e){
            if(!e.getMessage().equals("cart is empty")){
                Assert.fail("Succeeded to create an order with an empty cart ");
            }
        }
    }

    @Test
    public void createOrderWithNoUser() throws SQLException, Order.AlreadyExists, Order.NotFound {
        try {
            Order order = new Order(0,"Basel","052","20:33","22-03-2021",false,"",true,"Osfia","28-01-2020","1");
            order.addToCart(3);
            Assert.assertTrue(order.insertIntoOrders(),"Succeeded to create an order with user is Undefined");
        }
        catch (Exception e){
            if(!e.getMessage().equals("user is Undefined")){
                Assert.fail("Succeeded to create an order with user is Undefined ");
            }
        }
    }

    @Test
    public void createOrderShouldPassWithGreeting() throws SQLException, Order.AlreadyExists, Order.NotFound {
        try {
            Order order = new Order(2,"Basel","052","20:33","22-03-2021",true,"Happy Birthday Darling :)",true,"Osfia","28-01-2020","1");
            order.addToCart(3);
            Assert.assertTrue(order.insertIntoOrders(),"Failed to create an order");
        }
        catch (Exception e){
            Assert.fail("Failed to create an order");
        }
    }


}