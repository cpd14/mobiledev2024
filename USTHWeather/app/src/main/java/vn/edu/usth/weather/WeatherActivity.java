package vn.edu.usth.weather;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class WeatherActivity extends AppCompatActivity {
    private static final String TAG = "WeatherActivities";
    ViewPager2 viewpager2;
    TabLayout tabLayout;
    MediaPlayer mediaPlayer;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weather);
        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);

        final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
// This method is executed in main thread
                String content = msg.getData().getString("server_response");
                Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
            }
        };
        Thread t = new Thread(() -> {
// this method is run in a worker thread
            try {
// wait for 5 seconds to simulate a long network access
                Thread.sleep(5000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
// Assume that we got our data from server
            Bundle bundle = new Bundle();
            bundle.putString("server_response", "some sample json here");
// notify main thread
            Message msg = new Message();
            msg.setData(bundle);
            handler.sendMessage(msg);
        });
        t.start();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Log.i(TAG, "onCreate: ");
        //Viewpager2
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        PagerAdapter adapter = new PagerAdapter(this);
        viewpager2.setAdapter(adapter);
        viewpager2.setOffscreenPageLimit(3);

        //Tab layout
        tabLayout = findViewById(R.id.tabLayout);

        // Link TabLayout and ViewPager2 with TabLayoutMediator
        new TabLayoutMediator(tabLayout, viewpager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Ha Noi");
                    break;
                case 1:
                    tab.setText("Paris");
                    break;
                case 2:
                    tab.setText("Madrid");
                    break;
            }
        }).attach();

        //media play
        playAudio();

    }

    private void playAudio() {
        // Create a MediaPlayer instance with the MP3 file in res/raw
        mediaPlayer = MediaPlayer.create(this, R.raw.music);

        // Check if MediaPlayer was successfully created
        if (mediaPlayer != null) {
            /* Start playing the audio */
            mediaPlayer.start();

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            // do something when search is pressed
            Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_settings) {
            // do something when settings is pressed
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_reload) {
            // do something when reload is pressed
            Toast.makeText(this, "Reload clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer resources when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        Log.i(TAG, "onDestroy called");
    }

}
