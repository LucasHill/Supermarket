package org.lucashill.example.supermarket;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SupermarketTest {
    private final static Supermarket supermarket = new Supermarket();

    @Test
    public void testCheckout() {
        final int checkoutTotal = supermarket.checkout("ABBACBBAB");
        Assert.assertEquals(240, checkoutTotal, "Checkout total did not match correct value");
    }

}
