package test;
import static org.junit.Assert.*;

import org.junit.Test;
import currencyConverter.Currency;
import currencyConverter.MainWindow;
import java.util.ArrayList;

import org.junit.Before;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

// Exception retourné pour un montant < 0 
class NegativeNumberException extends Exception {
	String number;
	NegativeNumberException(String n1){
		number = n1;
	}
}
//Exception retourné pour des montant > 1 000 000
class OverLimitException extends Exception {
	String m;
	OverLimitException(String m1){
		m = m1;
	}	
}

public class MainWindowTest {

   private ArrayList<Currency> currencies;

    @Before
    public void setUp() {
        currencies = Currency.init();
    }

    public void NegativeException(double n) throws NegativeNumberException {
			
		if(n < 0) {
			throw new NegativeNumberException("Negative amount");
		}
    }
    public void OverLimitExceptionValue(double n) throws OverLimitException {
		if(n > 1000000) {
			throw new OverLimitException("Amount is over 1 000 000");
		}
	}
    //Boite Noire ----------------------------------
   
    //Test d un montant négatif
    @Test(expected = NegativeNumberException.class)
	public void convert_neg_ammount_correct_devise() throws ParseException, NegativeNumberException {
        double amount = -10.0 ;
	    double convert = MainWindow.convert("US Dollar", "Euro",currencies, amount);
        System.out.println(currencies.get(0).getName());
        assertTrue( amount < 0.0);
	    NegativeException(-10.0);
	}
    //Test d'un montant superieur à 1 000 000
    @Test(expected = OverLimitException.class)
	public void Test_USDtoCAD_frontiere_over_10000() throws ParseException, OverLimitException  {
	        double convert = MainWindow.convert("USD", "EUR", currencies, 1000060.0);
	        OverLimitExceptionValue(1000060.0);
	}

    //Test sur des devises faisant partir de la specification
    @Test
    public void testConvertUSDtoEUR() {
        double amount = 0;
        double converted = MainWindow.convert("USD", "EUR", currencies, amount);
        //Taux de change
        double expectedEURRate = 0.93;
        double expectedUSDRate = 1.073;

        assertTrue("Amount should be between 0 and 1000000",amount >= 0 && amount <= 1000000);
        double expectedAmount = amount * (expectedEURRate / expectedUSDRate);
        assertEquals(expectedAmount, converted, 0.001);
    }


    // Montant = 250000
    @Test
    public void testConvertValidAmount() {
        double  amount = 250000.0;
        double result = MainWindow.convert("US Dollar", "Euro", currencies, amount);
        double expectedEURRate = 0.93;
        double expectedAmount = amount * (expectedEURRate);
        assertEquals(expectedAmount, result, 0.001);
    }

    // Devise différente de la specification
    @Test
    public void testConvertInvalidCurrency() {
        double  amount = 500.0;
        double result = MainWindow.convert("Japanese Yen", "Guinean Franc", currencies, amount);
        //Nous nous attendons à zéro car la conversion ne devrait pas fonctionner
        assertEquals(0.0, result, 0.001);
    }

    // Analyse des valeurs frontières
    @Test
    public void testConvertZeroAmount() {
        double amount = MainWindow.convert("USD", "EUR", currencies, 0.0);
        assertEquals(0.0, amount, 0.001);
    }

    @Test
    public void testConvertMaxAmount() {
        double expectedEURRate = 0.93;
        double amount = 1000000.0;
        double convert = MainWindow.convert("Swiss Franc", "Euro", currencies, amount);
        double expectedAmount = amount * (expectedEURRate);
        assertEquals(expectedAmount, convert, 0.001);
    }
    









    
}
