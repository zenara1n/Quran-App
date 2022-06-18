package aiou.muslim.mttech.Adapters;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import aiou.muslim.mttech.R;

public class SpinnerCustomAdapter extends BaseAdapter {

    Context context;
    List<String> countries;

    public SpinnerCustomAdapter(Context context, List<String> countries) {
        this.context = context;
        this.countries = countries;
    }

    @Override
    public int getCount() {
        return countries.size();
    }

    @Override
    public Object getItem(int position) {
        return countries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = View.inflate(context, R.layout.custom_spinner_cell, null);

        TextView codeTxt = rowView.findViewById(R.id.countrynametxt);
        codeTxt.setText(countries.get(position));

        return rowView;
    }
}
