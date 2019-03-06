package net.htlgrieskirchen.pos3.iarthofer16woche21;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LogicTest {

    @Test
    public void makeRechnungTrue() {
        Logic instance = new Logic();
        String dateString = "05-03-2019";
        String amountString = "17.0";
        String category = "Essen";
        String type = "Ausgaben";

        Rechnung actResult = instance.makeRechnung(category, amountString, dateString, type);
        Rechnung expResult = new Rechnung(category, Double.valueOf(amountString)*(-1), dateString);

        assertEquals(expResult, actResult);
    }

    @Test
    public void makeRechnungDateWrong() {
        Logic instance = new Logic();
        String dateString = "";
        String amountString = "17.0";
        String category = "Essen";
        String type = "Ausgaben";

        try {
            Rechnung actResult = instance.makeRechnung(category, amountString, dateString, type);
            fail();
        }catch(IllegalArgumentException e){
            assertTrue(true);
        }

    }

    @Test
    public void makeRechnungAmountWrong() {
        Logic instance = new Logic();
        String dateString = "05-03-2019";
        String amountString = "asd";
        String category = "Essen";
        String type = "Ausgaben";

        try {
            Rechnung actResult = instance.makeRechnung(category, amountString, dateString, type);
            fail();
        }catch(IllegalArgumentException e){
            assertTrue(true);
        }
    }

    @Test
    public void calculateCash() {
        Rechnung r1 = new Rechnung("Essen", 10.0, "05-03-2019");
        Rechnung r2 = new Rechnung("Essen", -10.0, "05-03-2019");
        Rechnung r3 = new Rechnung("Essen", 5.0, "05-03-2019");
        Rechnung r4 = new Rechnung("Essen", 10.0, "05-03-2019");

        List<Rechnung> bills = new ArrayList<Rechnung>();
        bills.add(r1);
        bills.add(r2);
        bills.add(r3);
        bills.add(r4);

        Logic instance = new Logic();
        double actResult = instance.calculateCash(bills);
        double expResult = 15.0;

        assertEquals(expResult, actResult, 0.0001);
    }
}