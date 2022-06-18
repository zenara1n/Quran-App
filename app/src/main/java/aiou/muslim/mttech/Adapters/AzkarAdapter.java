package aiou.muslim.mttech.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import aiou.muslim.mttech.Models.AzkarModel;
import aiou.muslim.mttech.R;

public class AzkarAdapter extends RecyclerView.Adapter<AzkarAdapter.MyAdapter> {

    Context context;
    List<AzkarModel> data;
    private LayoutInflater inflater;

    public AzkarAdapter(Context context, List<AzkarModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (inflater == null) {
            inflater = LayoutInflater.from(viewGroup.getContext());
        }
        View view = inflater.inflate(R.layout.azkar_cell, viewGroup, false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter myAdapter, int i) {
        myAdapter.arabictxt.setText(data.get(i).getAzkarfield());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        TextView arabictxt;

        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            arabictxt = itemView.findViewById(R.id.arabictxt);
        }
    }

}
