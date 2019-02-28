package net.htlgrieskirchen.pos3.iarthofer16woche21;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        filleCategorys();
        fillSpinner();
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
    }

    private void filleCategorys(){
        List<String> categories = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(getResources().getAssets().open("categories.csv")))) {
            String nextLine = br.readLine();
            String[] splitted = nextLine.split(",");

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, splitted);
            Spinner spinner = findViewById(R.id.category_dropdown);
            spinner.setAdapter(adapter);

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
