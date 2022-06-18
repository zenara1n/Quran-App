package aiou.muslim.mttech.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import aiou.muslim.mttech.R;
import aiou.muslim.mttech.intrface.OnItemClickListener;
import aiou.muslim.mttech.Models.Surah;

import java.util.ArrayList;

public class SurahAdapter extends RecyclerView.Adapter<SurahAdapter.SurahViewHolder> {

  OnItemClickListener mItemClickListener;
  private ArrayList<Surah> surahArrayList;
  private Context context;

  public SurahAdapter(ArrayList<Surah> surahArrayList, Context context) {
    this.surahArrayList = surahArrayList;
    this.context = context;
  }

  @Override
  public SurahAdapter.SurahViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_surah, parent, false);
    SurahAdapter.SurahViewHolder viewHolder = new SurahAdapter.SurahViewHolder(view);

    return viewHolder;
  }

  @Override
  public void onBindViewHolder(SurahAdapter.SurahViewHolder holder, int position) {

    Surah surah = surahArrayList.get(position);
    holder.surah_idTextView.setText(surah.getId() + ".");
    holder.surah_idTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
    holder.translateTextView.setText(surah.getNameTranslate());
    holder.arabicTextView.setText(surah.getNameArabic());

    if (position % 2 == 0) {
      holder.row_surah.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

    } else {
      holder.row_surah.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
    }
  }

  @Override
  public long getItemId(int position) {

    return surahArrayList.get(position).getId();
  }

  public Object getItem(int position) {

    return surahArrayList.get(position);
  }

  @Override
  public int getItemCount() {
    return surahArrayList.size();
  }

  public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
    this.mItemClickListener = mItemClickListener;
  }

  public class SurahViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener // current clickListerner
  {
    public TextView translateTextView;

    public TextView surah_idTextView;

    public TextView arabicTextView;
    public RelativeLayout row_surah;

    public SurahViewHolder(View view) {
      super(view);
      translateTextView = view.findViewById(R.id.translate_textView);
      arabicTextView = view.findViewById(R.id.arabic_textView);
      surah_idTextView = view.findViewById(R.id.surah_idTextView);
      row_surah = view.findViewById(R.id.row_surah);

      view.setOnClickListener(this); // current clickListerner
    }

    @Override
    public void onClick(View v) {
      if (mItemClickListener != null) {
        mItemClickListener.onItemClick(v, getLayoutPosition());
      }
    }
  }
}
