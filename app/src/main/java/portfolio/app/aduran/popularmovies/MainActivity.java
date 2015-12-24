package portfolio.app.aduran.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import portfolio.app.aduran.popularmovies.dummy.DummyContent;
import portfolio.app.aduran.popularmovies.interfaces.OnListFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, MoviePosterFragment.newInstance(2))
                    .commit();
        }

    }


    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
