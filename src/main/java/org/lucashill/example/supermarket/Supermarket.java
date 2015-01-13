package org.lucashill.example.supermarket;

import java.util.List;
import java.util.Map;


public class Supermarket {
    private Map<Character, Integer> itemCostMap;
    private Map<Character, List<Integer>> itemDiscountMap;

    public Supermarket(Map<Character, Integer> itemCostMap, Map<Character, List<Integer>> itemDiscountMap) {
        this.itemCostMap = itemCostMap;
        this.itemDiscountMap = itemDiscountMap;
    }

    public int checkout(String items) {
        items = items.trim();

        int totalCost = 0;
        for(Character character : itemCostMap.keySet()) {

            final int numChars = countInstanceOfChar(character, items);

            int charCost;
            if(null == itemDiscountMap || null == itemDiscountMap.get(character)) {
                charCost = calculateCostForChar(character, numChars);
            } else {
                charCost = calculateCostForDiscountChar(character, numChars);
            }

            if(safeAdd(totalCost, charCost)) {
                totalCost = totalCost + charCost;
            } else {
                throw new ArithmeticException("Total checkout value greater than max integer value");
            }

            items = items.replace(character.toString(), "");
        }

        if(!items.equals("")) {
            throw new IllegalArgumentException("Input string was malformed, a character in the string was not in the price map");
        }

        return totalCost;
    }

    private static int countInstanceOfChar(char character, String word) {
        int count = 0;
        for(char subChar : word.toCharArray()) {
            if(subChar == character) {
                ++count;
            }
        }
        return count;
    }

    private int calculateCostForChar(char character, int numChars) {
        final int charPrice = itemCostMap.get(character);
        int charCost;

        if(safeMultiply(numChars, charPrice)) {
            charCost = numChars * charPrice;
        } else {
            throw new ArithmeticException("Total checkout value greater than max integer value");
        }

        return charCost;
    }

    private int calculateCostForDiscountChar(char character, int numChars) {
        final int charPrice = itemCostMap.get(character);

        final List<Integer> discountForChar = itemDiscountMap.get(character);
        final int numDiscounts = Math.floorDiv(numChars, discountForChar.get(0));
        int discountCost = 0;

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

        int nonDiscountCost = 0;
        if(safeMultiply(numChars % discountForChar.get(0), charPrice)) {
            nonDiscountCost = numChars % discountForChar.get(0)*charPrice;
        } else {
            throw new ArithmeticException("Total checkout value greater than max integer value");
        }

        int charCost;
        if(safeAdd(discountCost, nonDiscountCost)) {
            charCost = discountCost + nonDiscountCost;
        } else {
            throw new ArithmeticException("Total checkout value greater than max integer value");
        }
        return charCost;
    }

    private static boolean safeAdd(int firstValue, int secondValue) {
        return ((long) firstValue) + secondValue <= Integer.MAX_VALUE;
    }

    private static boolean safeMultiply(int firstValue, int secondValue) {
        return ((long) firstValue) * secondValue <= Integer.MAX_VALUE;
    }

}
