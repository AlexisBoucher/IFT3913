import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import currencyConverter.MainWindow;
import currencyConverter.Currency;


public class MainWindowTestBoiteBlanche {

    private ArrayList<Currency> currencies = Currency.init();


    @Test
    public void testUsdToEur() {
            double convert = MainWindow.convert("US Dollar", "Euro", currencies, 300.00);
            assertEquals(279.00,convert, 0.001);
    }
    @Test
    public void testUsdToNotAMoney() {
            double convert = MainWindow.convert("US Dollar", "Not a Money", currencies, 300.00);
            assertEquals(0,convert, 0.001);
    }
    @Test
    public void testNotAMoneyToEur() {
            double convert = MainWindow.convert("Not a Money", "Euro", currencies, 300.00);
            assertEquals(0,convert, 0.001);
    }
}
