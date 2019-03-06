package net.htlgrieskirchen.pos3.iarthofer16woche21;

import java.util.List;

public class Logic {


    public Rechnung makeRechnung(String category, String amountString, String dateString, String type) {
        Rechnung r = null;

        double amount = 0;

        try {
            if (dateString.equals("")) {
                throw new IllegalArgumentException();
            }

            if (type.equals("Ausgaben")) {
                amount = Double.parseDouble(amountString) * (-1);
            } else {
                amount = Double.parseDouble(amountString);
            }

            r = new Rechnung(category, amount, dateString);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }

        return r;
    }

    public double calculateCash(List<Rechnung> bills) {
        if (bills.isEmpty()) {
            return 0.0;
        }
        double cash = 0.0;
        for (Rechnung r : bills) {
            cash += r.getAmount();
        }

        return cash;
    }
}
