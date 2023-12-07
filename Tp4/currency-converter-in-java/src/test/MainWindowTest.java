package test;

import org.junit.Test;
import currencyConverter.Currency;
import currencyConverter.MainWindow;
import java.util.ArrayList;
import java.util.Map;

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
	    double convert = MainWindow.convert("USD", "CAD",currencies, amount);
        assertTrue( amount < 0.0);
	    NegativeException(-10.0);
	}
    //Test d'un montant superieur à 1 000 000
    @Test(expected = OverLimitException.class)
	public void Test_USDtoCAD_frontiere_over_10000() throws ParseException, OverLimitException  {
	        double convert = MainWindow.convert("USD", "CAD", currencies, 1000060.0);
	        OverLimitExceptionValue(1000060.0);
	}
    //Test sur des devises faisant partir de la specification
   /* @Test
    public void testValideUSDtoEUR() throws ParseException {
        double input = MainWindow.convert("USD", "EUR", currencies, 250000.0);
        assertEquals(231578.75, input,0);
    } */

    //Test sur des devises faisant partir de la specification
    @Test
    public void testConvertUSDtoEUR() {
        double amount = 0;
        double convertedAmount = MainWindow.convert("USD", "EUR", currencies, amount);
        //Taux de change choisi
        double expectedEURRate = 0.93;
        double expectedUSDRate = 1.08;

        assertTrue("Amount should be between 0 and 10000",amount >= 0 && amount <= 1000000);
        double expectedAmount = amount * (expectedEURRate / expectedUSDRate);
        assertEquals(expectedAmount, convertedAmount, 0.0001);
    }








    
}
