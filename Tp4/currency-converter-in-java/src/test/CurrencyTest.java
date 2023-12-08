package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import currencyConverter.Currency;

public class CurrencyTest {
    
    @Test
    public void testPositiveAmount() {
        double convert = Currency.convert(200.0, 1.5);
        assertEquals(300.0, convert, 0.001);
    }
    @Test
    public void testNegativeAmount() {
        // En assumant que la methode retourne null
        Double convert = Currency.convert(-50.0, 1.5);
         assertEquals(-75.0, convert, 0.001);
    }
 
    @Test
    public void testZeroValues() {
        double amount = Currency.convert(0.0, 1.2);
        assertEquals(0.0, amount, 0.001);
    }

    @Test
    public void testExchangeValueZero() {
        double result = Currency.convert(200.0, 0.0);
        assertEquals(0.0, result, 0.001);
    }
    @Test
    public void testBoundaryValues() {
        // 1 car zero est déjà fait 
        double amount1 = Currency.convert(1.0, 2.0);
        assertEquals(2.0, amount1, 0.001);

        double amount2 = Currency.convert(1000000.0, 2.0);
        assertEquals(2000000.0, amount2, 0.001);
    }
    @Test
    public void testNegativeExchangeValue() {
        double result = Currency.convert(200.0, -1.5);
        assertEquals(-300.0, result, 0.001);
    }

}
