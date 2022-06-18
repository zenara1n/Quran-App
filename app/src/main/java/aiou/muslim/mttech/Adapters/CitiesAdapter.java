package aiou.muslim.mttech.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import aiou.muslim.mttech.Activities.SettingsActivity;
import aiou.muslim.mttech.Models.CitiesModel;
import aiou.muslim.mttech.R;
import aiou.muslim.mttech.SharedData.SharedClass;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.MyAdapter> {

    Context context;
    List<CitiesModel> data;
    private LayoutInflater inflater;
    Dialog dialog;

    public CitiesAdapter(Context context, List<CitiesModel> data, Dialog dialog) {
        this.context = context;
        this.data = data;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (inflater == null) {
            inflater = LayoutInflater.from(viewGroup.getContext());
        }
        View view = inflater.inflate(R.layout.custom_spinner_cell, viewGroup, false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter myAdapter, int i) {
        myAdapter.countrynametxt.setText(data.get(i).getCity());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        TextView countrynametxt;

        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            countrynametxt = itemView.findViewById(R.id.countrynametxt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("cityis", data.get(getAdapterPosition()).getCity());
                    SharedClass.setLocationFlag(context, data.get(getAdapterPosition()).getCountry(),
                            data.get(getAdapterPosition()).getCity(), data.get(getAdapterPosition()).getLatitude(),
                            data.get(getAdapterPosition()).getLongitude(), 1);
                    SettingsActivity.txtCountry.setText(data.get(getAdapterPosition()).getCountry());
                    SettingsActivity.txtCity.setText(data.get(getAdapterPosition()).getCity());
                    SettingsActivity.txtLocation.setText(data.get(getAdapterPosition()).getLatitude()+","+data.get(getAdapterPosition()).getLongitude());
                    SettingsActivity.locationLayout.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
            });

        }
    }
}
