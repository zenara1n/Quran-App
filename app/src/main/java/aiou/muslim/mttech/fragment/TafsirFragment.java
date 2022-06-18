package aiou.muslim.mttech.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import aiou.muslim.mttech.R;
import aiou.muslim.mttech.Adapters.TafsirAdapter;
import aiou.muslim.mttech.database.datasource.AyahWordDataSource;
import aiou.muslim.mttech.database.datasource.TafsirKathirEnglishDataSource;
import aiou.muslim.mttech.Models.Tafsir;

import java.util.ArrayList;


public class TafsirFragment extends Fragment {

  public ArrayList<Tafsir> tafsirArrayList;
  long surah_id;
  long verse_id;
  private RecyclerView mRecyclerView;
  private TafsirAdapter tafsirAdapter;
  private RecyclerView.LayoutManager mLayoutManager;

  public TafsirFragment() {}

  public static TafsirFragment newInstance(Bundle bundle) {
    TafsirFragment tafsirFragment = new TafsirFragment();
    tafsirFragment.setArguments(bundle);
    return tafsirFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    surah_id = getArguments().getLong(AyahWordDataSource.AYAHWORD_SURAH_ID);
    verse_id = getArguments().getLong(AyahWordDataSource.AYAHWORD_VERSE_ID);
    tafsirArrayList = getTafsirKathirEnglishArrayList(surah_id, verse_id);
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_tafsir, container, false);
    mRecyclerView = view.findViewById(R.id.recycler_tafsir_view);
    tafsirAdapter = new TafsirAdapter(getActivity(), tafsirArrayList);
    return view;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mRecyclerView.setAdapter(tafsirAdapter);
    mRecyclerView.setHasFixedSize(true);
    mLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
  }

  private ArrayList<Tafsir> getTafsirKathirEnglishArrayList(long surah_id, long verse_id) {
    ArrayList<Tafsir> tafsirArrayList;
    TafsirKathirEnglishDataSource tafsirKathirEnglishDataSource =
        new TafsirKathirEnglishDataSource(getActivity());
    tafsirArrayList =
        tafsirKathirEnglishDataSource.getTafsirKathirEnglishArrayList(surah_id, verse_id);
    return tafsirArrayList;
  }
}
