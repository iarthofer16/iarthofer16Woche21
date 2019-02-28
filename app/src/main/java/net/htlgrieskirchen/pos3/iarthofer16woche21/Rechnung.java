package net.htlgrieskirchen.pos3.iarthofer16woche21;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Rechnung {
    private String category;
    private double amount;
    private LocalDate date;

    public Rechnung(String category, double amount, LocalDate date){
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public Rechnung(String category, String amount, String date){
        this.category = category;
        try{
            this.amount = Double.parseDouble(amount);
        }catch(Exception e){
            throw new IllegalArgumentException();
        }
        String[] dateParts = date.split("-");
        this.date = LocalDate.of(Integer.parseInt(dateParts[2]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[0]));
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Rechnung{" +
                "category='" + category + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}
