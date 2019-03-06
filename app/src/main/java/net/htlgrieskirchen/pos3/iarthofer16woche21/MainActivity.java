package net.htlgrieskirchen.pos3.iarthofer16woche21;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Spinner spinnerCategory;
    List<Rechnung> bills;
    ArrayAdapter<Rechnung> listViewAdapter;
    ListView listView;
    ArrayAdapter categoryAdapter;
    String[] categories;
    TextView cashTextView;
    int currentAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bills = new ArrayList<>();
        spinnerCategory = findViewById(R.id.category_dropdown);
        cashTextView  = findViewById(R.id.cashTextView);

        fillCategorys();
        fillSpinner();
        listViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bills);
        listView = findViewById(R.id.listView);
        listView.setAdapter(listViewAdapter);
        readCsv();
    }

    public void datePicker(View view) {

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
        Rechnung r = null;
        TextView categoryTV = (TextView) findViewById(R.id.customCategory_textView);
        String category;
        if(categoryTV.getText().equals("custom Category")){
            //TODO custom Category is always used
            category = String.valueOf(spinnerCategory.getSelectedItem());

        }else{
            category = String.valueOf(categoryTV.getText());
            addCategory(category);
        }
        TextView amountTV = (TextView) findViewById(R.id.amount_textView);
        double amount = 0;
        TextView dateTV = (TextView) findViewById(R.id.date_textView);

        try{
            String date = String.valueOf(dateTV.getText());
            if(date.equals("")){
                throw new IllegalArgumentException();
            }

            Spinner type = (Spinner) findViewById(R.id.ausgabeEingaben_spinner);
            if(String.valueOf(type.getSelectedItem()).equals("Ausgaben")){
                amount = Double.parseDouble(String.valueOf(amountTV.getText())) * (-1);
                currentAmount -= amount;
            }else{
                amount = Double.parseDouble(String.valueOf(amountTV.getText()));
                currentAmount += amount;
            }

            r = new Rechnung(category, amount, date);
            bills.add(r);

            listViewAdapter.notifyDataSetChanged();

            writeCsv();

            amountTV.setText("CA$H " + currentAmount);

        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Falsche Eingabe", Toast.LENGTH_LONG).show();
        }

        dateTV.setText("");
        amountTV.setText("euro");
        categoryTV.setText("custom Category");
    }

    private void fillCategorys(){
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getResources().getAssets().open("categories.csv")))) {
            String nextLine = br.readLine();
            categories = nextLine.split(",");

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories);

            spinnerCategory.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void fillSpinner() {
        Spinner spinner = findViewById(R.id.ausgabeEingaben_spinner);
        String[] items = new String []{"Ausgaben", "Einnahmen"};
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
    }

    private void addCategory(String category){
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "categories.csv";
        String filePath = baseDir + File.separator + fileName;
        File f = new File(filePath );

        String nextLine = "";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(getResources().getAssets().open("categories.csv")))) {
            nextLine = br.readLine();
            categories = nextLine.split(",");
            categories[categories.length] = category;

            categoryAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try (BufferedWriter br = new BufferedWriter(new FileWriter(filePath))) {
            br.write(nextLine + "," + category);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeCsv(){
        String fileName = "data.csv";

        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(openFileOutput(fileName, MODE_APPEND)))) {
                out.println(bills.get(bills.size()-1).toString());
                out.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readCsv(){
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

            listViewAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO csv lesen

    //TODO aktueller Stand anzeigen



    //TODO logs

    //TODO trennen von Logic und Android

    //TODO Unit tests
}
