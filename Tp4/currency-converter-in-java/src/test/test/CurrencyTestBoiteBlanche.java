package test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import currencyConverter.Currency;

public class CurrencyTestBoiteBlanche {
    @Test
    public void test() {
        double convert = Currency.convert(30.00, 0.80);
        assertEquals(24.00, convert, 0.001);
    }
    
}