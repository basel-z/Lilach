package src.lil.tests;


import org.testng.Assert;
import org.testng.annotations.Test;
import src.lil.models.Order;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class cancelOrderTest {
    @Test
    public void cancelOrderThatShouldntCanceled(){
        try {
            Order cancelOrder = new Order();
            cancelOrder.setId(200);
            Order _cancelOrder = cancelOrder.findOrderById();
            Assert.assertTrue(_cancelOrder== null,"Succeeded to get an order that does not found in db so test failed");
        }
        catch (Exception e){
            // Test passed
            System.out.println(e.getMessage());

        }
    }
    @Test
    public void cancelOrderThatAlreadyDelivered(){
        try {
            Order cancelOrder = new Order();
            cancelOrder.setId(11);
            Order _cancelOrder = cancelOrder.findOrderById();
            Double refundAmount = _cancelOrder.orderTimeDiff();
            Assert.assertEquals(refundAmount, 0.0, 0.1, "Succeeded to cancel order that already delivered so test failed");
        }
        catch (Exception e){
            // Test passed
            System.out.println(e.getMessage());

        }
    }

    @Test
    public void cancelOrderBetween3And1HourBeforeDelivery(){
        try {
            Order cancelOrder = new Order();
            cancelOrder.setShippingDate(DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.now()));
            cancelOrder.setOrderCost("1000");
            LocalTime localTime = LocalTime.now();
            localTime = localTime.minusHours(1);
            localTime = localTime.minusMinutes(30);
            cancelOrder.setShippingHour(DateTimeFormatter.ofPattern("HH:mm:ss").format(localTime));
            Double refundAmount = cancelOrder.orderTimeDiff();
            if(refundAmount < 1){
                Assert.fail("Could not delete order");
            }
            Assert.assertEquals(refundAmount, 500.0, 0.1, "Refund is not as it should be");
        }
        catch (Exception e){
            // Test passed
            System.out.println(e.getMessage());

        }
    }

    @Test
    public void cancelOrder1HourBeforeDelivery(){
        try {
            Order cancelOrder = new Order();
            cancelOrder.setShippingDate(DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.now()));
            cancelOrder.setOrderCost("1000");
            LocalTime localTime = LocalTime.now();
            localTime = localTime.minusMinutes(30);
            cancelOrder.setShippingHour(DateTimeFormatter.ofPattern("HH:mm:ss").format(localTime));
            Double refundAmount = cancelOrder.orderTimeDiff();
            if(refundAmount > 1){Assert.fail("Refund is not as it should be");}
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void cancelOrderMoreThan3Hours(){
        try {
            Order cancelOrder = new Order();
            cancelOrder.setShippingDate(DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.now()));
            cancelOrder.setOrderCost("1000");
            LocalTime localTime = LocalTime.now();
            localTime = localTime.minusHours(10);
            localTime = localTime = localTime.minusMinutes(30);
            cancelOrder.setShippingHour(DateTimeFormatter.ofPattern("HH:mm:ss").format(localTime));
            Double refundAmount = cancelOrder.orderTimeDiff();
            Assert.assertEquals(refundAmount, 1000.0, 0.1, "Refund is not as it should be");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
