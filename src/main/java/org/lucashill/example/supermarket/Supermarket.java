package org.lucashill.example.supermarket;

import java.util.List;
import java.util.Map;

/**
 * Supermarket
 *
 * Class which provides functionality for a supermarket. Particularly the ability to calculate
 * the total cost for a list of items.
 *
 * @author Lucas Hill
 * @version 1.0
 */
public class Supermarket {
    private Map<Character, Integer> itemCostMap;
    private Map<Character, List<Integer>> itemDiscountMap;

    /**
     * Constructor for the supermarket class. Provides class with needed information about item costs and discounts.
     *
     * @param itemCostMap A map which relates an item character to its cost
     * @param itemDiscountMap A map which relates an item character to its discount rules
     */
    public Supermarket(Map<Character, Integer> itemCostMap, Map<Character, List<Integer>> itemDiscountMap) {
        this.itemCostMap = itemCostMap;
        this.itemDiscountMap = itemDiscountMap;
    }

    /**
     * The checkout method takes a provided list of characters (as a string), parses them, and then calculates
     * a total cost for the items given the pricing rules for the Supermarket.
     * @param items A string of items represented as individual characters
     * @return The total cost of the provided list of items
     */
    public int checkout(String items) {
        items = items.trim();
        int totalCost = 0;

        //Iterate through each of the items in the supermarket
        for(Character character : itemCostMap.keySet()) {

            //Count instances of item within the provided string
            final int numChars = countInstanceOfChar(character, items);

            int charCost;
            //Item is not in discount map and thus can be calculated directly based on price
            if(null == itemDiscountMap || null == itemDiscountMap.get(character)) {
                charCost = calculateCostForChar(character, numChars);
            } else { //Item is in discount map and discount must be considered when calculating
                charCost = calculateCostForDiscountChar(character, numChars);
            }

            //Increment total cost
            if(safeAdd(totalCost, charCost)) {
                totalCost = totalCost + charCost;
            } else {
                throw new ArithmeticException("Total checkout value greater than max integer value");
            }

            //Remove item instances from string
            items = items.replace(character.toString(), "");
        }

        //If string has other item characters not in the item cost map, the string cannot be parsed
        if(!items.equals("")) {
            throw new IllegalArgumentException("Input string was malformed, a character in the string was not in the price map");
        }

        return totalCost;
    }

    /**
     *
     * @param character A single character representing an item
     * @param numChars The number of occurrences of this character, used for calculating price
     * @return The total cost of this many items
     */
    private int calculateCostForChar(char character, int numChars) {
        //Find the price in the cost map
        final int charPrice = itemCostMap.get(character);
        int charCost;

        //Calculate the cost simply based on the number of occurrences
        if(safeMultiply(numChars, charPrice)) {
            charCost = numChars * charPrice;
        } else {
            throw new ArithmeticException("Total checkout value greater than max integer value");
        }

        return charCost;
    }

    /**
     *
     * @param character A single character representing an item
     * @param numChars The number of occurrences of this character, used for calculating price
     * @return The total cost of this many items
     */
    private int calculateCostForDiscountChar(char character, int numChars) {
        //Find the price in the cost map
        final int charPrice = itemCostMap.get(character);

        //Find the discount rules for the item
        final List<Integer> discountForChar = itemDiscountMap.get(character);
        //Determine the number discounts achieved (ie buy 5, pay 3 with 10 items, would get you 2 discounts)
        final int numDiscounts = Math.floorDiv(numChars, discountForChar.get(0));
        int discountCost;

        //Determine the costs of the discounted portion by multiplying the number of discounts by the discounted value
        if(safeMultiply(numDiscounts, discountForChar.get(1))) {
            int temp = numDiscounts*discountForChar.get(1);
            if(safeMultiply(temp, charPrice)) {
                discountCost = temp*charPrice;
            } else {
                throw new ArithmeticException("Total checkout value greater than max integer value");
            }
        } else {
            throw new ArithmeticException("Total checkout value greater than max integer value");
        }

        //There may be leftover non discounted instances of the item, the cost of those can be calculated
        //the same way as a non discounted item
        final int nonDiscountCost = calculateCostForChar(character, numChars % discountForChar.get(0));

        //The total cost is calculated from the sum of the discounted and non discounted portions
        int totalCharCost;
        if(safeAdd(discountCost, nonDiscountCost)) {
            totalCharCost = discountCost + nonDiscountCost;
        } else {
            throw new ArithmeticException("Total checkout value greater than max integer value");
        }
        return totalCharCost;
    }

    /**
     * A helper method for counting instances of characters in a string
     *
     * @param character A single character representing an item
     * @param word A word to count instances of character in
     * @return Number of instances of character in word
     */
    private static int countInstanceOfChar(char character, String word) {
        int count = 0;
        for(char subChar : word.toCharArray()) {
            if(subChar == character) {
                ++count;
            }
        }
        return count;
    }

    /**
     * A helper method for determining whether two integers can be safely summed without overflowing
     * @param firstValue First integer value to test in sum
     * @param secondValue Second integer value to test in sum
     * @return True if integers can be summed safely
     */
    private static boolean safeAdd(int firstValue, int secondValue) {
        return ((long) firstValue) + secondValue <= Integer.MAX_VALUE;
    }

    /**
     * A helper method for determining whether two integers can be safely multiplied without overflowing
     * @param firstValue First integer value to test in multiplication
     * @param secondValue Second integer value to test in multiplication
     * @return True if integers can be multiplied safely
     */
    private static boolean safeMultiply(int firstValue, int secondValue) {
        return ((long) firstValue) * secondValue <= Integer.MAX_VALUE;
    }

}
