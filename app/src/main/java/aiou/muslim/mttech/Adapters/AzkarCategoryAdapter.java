package aiou.muslim.mttech.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

import aiou.muslim.mttech.Manager.DBManagerAzkar;
import aiou.muslim.mttech.Models.AzkarModel;
import aiou.muslim.mttech.R;

public class AzkarCategoryAdapter extends RecyclerView.Adapter<AzkarCategoryAdapter.MyAdapter> {

    DBManagerAzkar dbManagerAzkar;
    Context context;
    List<String> dataar;
    List<String> dataen;
    private LayoutInflater inflater;

    public AzkarCategoryAdapter(Context context, List<String> dataar, List<String> dataen) {
        this.context = context;
        this.dataar = dataar;
        this.dataen = dataen;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (inflater == null) {
            inflater = LayoutInflater.from(viewGroup.getContext());
        }
        View view = inflater.inflate(R.layout.azkar_category_cell, viewGroup, false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter myAdapter, int i) {
        myAdapter.arabictxt.setText(dataar.get(i));
        myAdapter.engtxt.setText(dataen.get(i));
    }

    @Override
    public int getItemCount() {
        return dataar.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        TextView arabictxt, engtxt;

        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            arabictxt = itemView.findViewById(R.id.arabictxt);
            engtxt = itemView.findViewById(R.id.engtxt);

            dbManagerAzkar = new DBManagerAzkar(context);
            dbManagerAzkar.open();
            try {
                dbManagerAzkar.copyDataBase();
            }catch (IOException e){
                e.printStackTrace();
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAzkar(dbManagerAzkar.getAzkar(dataen.get(getAdapterPosition())), dataen.get(getAdapterPosition()));
                }
            });

        }
    }

    public void showAzkar(List<AzkarModel> data, String cat) {
        final Dialog dialogView = new Dialog(context);
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogView.setCancelable(true);
        dialogView.setContentView(R.layout.azkar_layout_single);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogView.getWindow().getAttributes());
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90);
        lp.width = width;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogView.getWindow().setAttributes(lp);

        final RecyclerView azkarRV = dialogView.findViewById(R.id.azkarRV);
        azkarRV.setLayoutManager(new LinearLayoutManager(context));
        AzkarAdapter azkarAdapter = new AzkarAdapter(context, data);
        azkarRV.setAdapter(azkarAdapter);

        dialogView.show();
    }

}
