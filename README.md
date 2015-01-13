## Synopsis

A maven project for a simple supermarket. Given a list of configurable item costs and discounts, the supermarket can
calculate the total cost of a list of items.

## Configuration

The SupermarketTest class is setup to run with a Spring autowired bean for the Supermarket class. The spring context
configuration for the SupermarketTest (called "SupermarketTest-context.xml") shows examples of possible configurations
of a given Supermarket.

For example, given 3 products (A, B, C), which cost $20, $50, and $30 respectively. In addition if you buy 5 of product
B, you get them for the price of 3. The configuration for this rule-set would look something like:

For the price mapping:

    <util:map id="itemPriceMap1">
        <entry key="A" value="20"/>
        <entry key ="B" value="50"/>
        <entry key ="C" value="30"/>
    </util:map>

For the discount rules:

    First we map the discounted item to a pricing rule:

    <util:map id="discountMap1">
        <entry key="B" value-ref="discountType1"/>
    </util:map>

    Next we define the pricing rule as buy 5, get for price of 3:

    <util:list id="discountType1" value-type="java.lang.Integer">
        <value>5</value>
        <value>3</value>
    </util:list>


## Installation

Normal maven lifecycle: "mvn clean install" etc...

## Tests

Tests can be run on the command line with "mvn test" or through your IDE.

## Contributors

Lucas Hill

