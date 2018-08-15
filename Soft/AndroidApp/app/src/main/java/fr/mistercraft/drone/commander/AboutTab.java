package fr.mistercraft.drone.commander;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class AboutTab extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_tab);
        Toolbar toolbar = findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.About);
    }

    public void onTwitter(View view) {
        try {
            Intent twitter = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/jangberry_"));
            startActivity(twitter);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e("inent", "", e);
            }
        }
    }

    public void onGit(View view) {
        try {
            Intent git = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/jangberry"));
            startActivity(git);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e("inent", "", e);
            }
        }
    }

    public void onYt(View view) {
        try {
            Intent yt = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCXFBCtoa3U3kQdtGj-qG8mg"));
            startActivity(yt);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e("inent", "", e);
            }
        }
    }

    public void onEth(View view) {
        try {
            View ethaddr = findViewById(R.id.ethaddr);
            View paypaladdr = findViewById(R.id.Paypal_address);
            if (ethaddr.getVisibility() == View.GONE) {
                ethaddr.setVisibility(View.VISIBLE);
                paypaladdr.setVisibility(View.VISIBLE);
            } else {
                ethaddr.setVisibility(View.GONE);
                paypaladdr.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e("inent", "", e);
            }
        }
    }
}
