package org.lucashill.example.supermarket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Supermarket test class
 *
 * Various unit tests for the supermarket class. Tests checkout functionality given
 * various well or poorly formed strings in addition to other edge cases and scenarios.
 *
 * @author Lucas Hill
 * @version 1.0
 */
@Test
@ContextConfiguration("classpath:SupermarketTest-context.xml")
public class SupermarketTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private Supermarket supermarket;

    /**
     * Tests basic functionality of the checkout method, a given string (that should have a discount applied)
     * is parsed and total cost calculated.
     */
    @Test
    public void basicCheckoutTest() {
        final int checkoutTotal = supermarket.checkout("ABBACBBAB");
        Assert.assertEquals(checkoutTotal, 240, "Checkout total did not match correct value");
    }

    /**
     * Tests a checkout string which does not reach the threshold for achieving a discount.
     */
    @Test
    public void saleAmountUnreachedTest() {
        final int checkoutTotal = supermarket.checkout("ABACBBAB");
        Assert.assertEquals(checkoutTotal, 290, "Checkout total did not match correct value");
    }

    /**
     * Tests a checkout string which will have multiple discounts applied.
     */
    @Test
    public void multipleDiscountsApply() {
        final int checkoutTotal = supermarket.checkout("BBBBBCBBBBBBBBBB");
        Assert.assertEquals(checkoutTotal, 480, "Checkout total did not match correct value");
    }

    /**
     * Tests input string with extra whitespace. Requirements state this string must have whitespace
     * ignored.
     */
    @Test
    public void extraWhitespaceInputTest() {
        final int checkoutTotal = supermarket.checkout("  ABBACBBAB ");
        Assert.assertEquals(checkoutTotal, 240, "Checkout total did not match correct value");
    }

    /**
     * Tests and empty input string. Requirements state this case must return zero.
     */
    @Test
    public void inputWithZeroLengthString() {
        final int checkoutTotal = supermarket.checkout("");
        Assert.assertEquals(checkoutTotal, 0, "Checkout total did not match correct value");
    }

    /**
     * Tests an input string with a character which is not in the price map (ie that item does not exist.
     * The requirement for this case is an exception to be thrown.
     */
    @Test (expectedExceptions = IllegalArgumentException.class)
    public void inputWithBadValueTest() {
        supermarket.checkout("ABBACZBBAB");
    }

    /**
     * Tests the unusual case where the total cost would exceed the maximum value for an integer (perhaps
     * one of our customers sells jets?). In this case requirements state an exception must be thrown.
     */
    @Test (expectedExceptions = ArithmeticException.class)
    public void inputWillExceedIntegerMax(){
        final Map<Character, Integer> itemCostMap = new HashMap<Character, Integer>();
        itemCostMap.put('A', 1000000000);

        final Supermarket expensiveSupermarket = new Supermarket(itemCostMap, null);
        expensiveSupermarket.checkout("AAA");

    }
}
