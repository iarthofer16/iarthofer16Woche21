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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Spinner spinnerCategory;
    List<Rechnung> bills = new ArrayList<>();
    ArrayAdapter<Rechnung> listViewAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinnerCategory = findViewById(R.id.category_dropdown);
        fillCategorys();
        fillSpinner();
        listViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bills);
        listView = findViewById(R.id.listView);
        listView.setAdapter(listViewAdapter);
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
        Rechnung r;
        String category = String.valueOf(spinnerCategory.getSelectedItem());
        TextView amountTV = (TextView) findViewById(R.id.amount_textView);
        String amount = String.valueOf(amountTV.getText());
        TextView dateTV = (TextView) findViewById(R.id.date_textView);
        String date = String.valueOf(dateTV.getText());
        r = new Rechnung(category, amount, date);
        bills.add(r);
        dateTV.setText("");
        amountTV.setText("");
        listViewAdapter.notifyDataSetChanged();
    }

    private void fillCategorys(){
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getResources().getAssets().open("categories.csv")))) {
            String nextLine = br.readLine();
            String[] splitted = nextLine.split(",");

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, splitted);

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
}
