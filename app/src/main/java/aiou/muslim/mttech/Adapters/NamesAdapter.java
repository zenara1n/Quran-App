package aiou.muslim.mttech.Adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import aiou.muslim.mttech.Models.NamesModel;
import aiou.muslim.mttech.R;

public class NamesAdapter extends RecyclerView.Adapter<NamesAdapter.MyAdapter> {

    private LayoutInflater inflater;
    Context context;
    List<NamesModel> data;

    public NamesAdapter(Context context, List<NamesModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater==null){
            inflater=LayoutInflater.from(parent.getContext());
        }
        View rowView = inflater.inflate(R.layout.name_cell, parent, false);
        return new MyAdapter(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        holder.engTxt.setText(data.get(position).getTransliteration());
        holder.arabicTxt.setText(data.get(position).getName());
        holder.translationTxt.setText(data.get(position).getMeaning());

        String txt = data.get(position).getName() +" "+ data.get(position).getMeaning();

        int pos = position+1;
        holder.txtNum.setText(String.valueOf(pos));

        holder.btnCopy1.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(data.get(position).getTransliteration(),txt);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
        });

        holder.btnShare1.setOnClickListener(v -> {
            Intent intent2 = new Intent(); intent2.setAction(Intent.ACTION_SEND);
            intent2.setType("text/plain");
            intent2.putExtra(Intent.EXTRA_TEXT, txt);
            context.startActivity(Intent.createChooser(intent2, "Share via"));
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {

        TextView engTxt, arabicTxt, translationTxt, txtNum;
        ImageView btnCopy1, btnShare1;

        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            txtNum = itemView.findViewById(R.id.txtNum);
            engTxt = itemView.findViewById(R.id.engTxt);
            arabicTxt = itemView.findViewById(R.id.arabictxt);
            translationTxt = itemView.findViewById(R.id.translationTxt);
            btnCopy1 = itemView.findViewById(R.id.btnCopy1);
            btnShare1 = itemView.findViewById(R.id.btnShare1);
        }
    }
}
