package aiou.muslim.mttech.Activities;

import android.app.FragmentManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import aiou.muslim.mttech.R;
import aiou.muslim.mttech.fragment.TafsirFragment;


public class TafsirActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tafsir);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    Bundle bundle = this.getIntent().getExtras();

    if (savedInstanceState == null) {
      FragmentManager fragmentManager = getFragmentManager();

      fragmentManager
          .beginTransaction()
          .replace(R.id.main_container, TafsirFragment.newInstance(bundle))
          .commit();
    }
  }
}
