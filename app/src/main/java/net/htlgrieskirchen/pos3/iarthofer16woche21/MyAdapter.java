package net.htlgrieskirchen.pos3.iarthofer16woche21;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<Rechnung> {

    public MyAdapter(Context context, ArrayList<Rechnung> bills) {
        super(context, 0, bills);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Rechnung r = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.three_col_row, null);
        }
        TextView category = (TextView) convertView.findViewById(R.id.TextView01);
        TextView amount = (TextView) convertView.findViewById(R.id.TextView02);
        TextView date = (TextView) convertView.findViewById(R.id.TextView03);

        category.setText(r.getCategory());
        amount.setText(String.valueOf(r.getAmount()));
        date.setText(String.valueOf(r.getDate()));

        return convertView;
    }

    static class ViewHolder {
        TextView text1;
        TextView text2;
        TextView text3;
    }
}
