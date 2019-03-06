package net.htlgrieskirchen.pos3.iarthofer16woche21;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
private static final String TAG = "Woche21";
    private Logic l = new Logic();

    private Spinner spinnerCategory;
    private TextView cashTextView;
    private ListView listView;

    MyAdapter listViewAdapter;
    ArrayAdapter<String> categoryAdapter;

    public ArrayList<Rechnung> bills;
    private List<String> categories;
    private double currentAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinnerCategory = findViewById(R.id.category_dropdown);
        cashTextView  = findViewById(R.id.cashTextView);
        listView = findViewById(R.id.listView);

        bills = new ArrayList<>();
        listViewAdapter = new MyAdapter(this , bills);
        listView.setAdapter(listViewAdapter);

        categories = new ArrayList<>();
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(categoryAdapter);

        fillCategorys();
        fillSpinner();
        readCsv();
        currentAmount = l.calculateCash(bills);
        cashTextView.setText("CA$H " + currentAmount);
    }

    public void datePicker(View view) {
        Log.d(TAG, "datePicker");

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        TextView viewById = findViewById(R.id.date_textView);
                        viewById.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public void onClick(View view) {
        Log.d(TAG, "onClick");

        TextView categoryTV = (TextView) findViewById(R.id.customCategory_textView);
        TextView amountTV = (TextView) findViewById(R.id.amount_textView);
        TextView dateTV = (TextView) findViewById(R.id.date_textView);
        Spinner typeSP = (Spinner) findViewById(R.id.ausgabeEingaben_spinner);


        Rechnung r;
        String category;

        if(categoryTV.getText().equals("custom Category")){
            category = String.valueOf(spinnerCategory.getSelectedItem());
        }else{
            category = String.valueOf(spinnerCategory.getSelectedItem());
            addCategory(category);
        }

        String amount = String.valueOf(amountTV.getText());
        String date = String.valueOf(dateTV.getText());
        String type = String.valueOf(typeSP.getSelectedItem());

        try{

            r = l.makeRechnung(category, amount, date, type);
            currentAmount += r.getAmount();
            bills.add(r);

            listViewAdapter.notifyDataSetChanged();

            writeCsv();

            cashTextView.setText("CA$H " + currentAmount);

        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Falsche Eingabe", Toast.LENGTH_LONG).show();
        }

        dateTV.setText("");
        amountTV.setText("");
        categoryTV.setText("");
    }

    private void fillCategorys(){
        Log.d(TAG, "fillCategorys");

        try(BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("categories.csv")))){
            String line = br.readLine();
            String[] splitted = line.split(",");
            for(int i = 0; i < splitted.length; i++){
                categories.add(splitted[i]);
            }
        }catch(Exception e){
            throw new IllegalArgumentException();
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("categories.csv")))) {
            String nextLine = "";
            while((nextLine = br.readLine()) != null){
                categories.add(nextLine);
            }

            categoryAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void fillSpinner() {
        Log.d(TAG, "fillSpinner");

        Spinner spinner = findViewById(R.id.ausgabeEingaben_spinner);
        String[] items = new String []{"Ausgaben", "Einnahmen"};
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
    }

    private void addCategory(String category){
        Log.d(TAG, "addCategory");

        if(!categories.contains(category)){
            categories.add(category);
            String fileName = "categries.csv";

            try (PrintWriter out = new PrintWriter(new OutputStreamWriter(openFileOutput(fileName, MODE_APPEND)))) {
                out.println(category);
                out.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }


            categoryAdapter.notifyDataSetChanged();
        }
    }

    private void writeCsv(){
        Log.d(TAG, "writeCsv");

        String fileName = "data.csv";

        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(openFileOutput(fileName, MODE_PRIVATE)))) {
                out.println(bills.get(bills.size()-1).toString());
                out.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readCsv(){
        Log.d(TAG, "readCsv");

        bills.clear();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("data.csv")))) {
            String nextLine = "";
            while((nextLine = br.readLine()) != null){
                String[] splitted = nextLine.split(",");

                String date = splitted[2].split("=")[1];
                String amount = splitted[1].split("=")[1];
                String category = splitted[0].split("=")[1];

                Rechnung r = new Rechnung(category, Double.parseDouble(amount), date);
                //has Problems here
                bills.add(r);
            }

            currentAmount = l.calculateCash(bills);
            listViewAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO CSV richtig lesen
}
