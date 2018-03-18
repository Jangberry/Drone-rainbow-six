package fr.mistercraft.drone.commander;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import java.net.URI;

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
            Intent twitter = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/mistercraft385"));
            startActivity(twitter);
        } catch (Exception e) {
            Log.e("inent", "", e);
        }
    }

    public void onGit(View view) {
        try {
            Intent git = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/mistercraft38/drone-rainbow-six"));
            startActivity(git);
        } catch (Exception e) {
            Log.e("inent", "", e);
        }
    }

    public void onYt(View view) {
        try {
            Intent yt = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCXFBCtoa3U3kQdtGj-qG8mg"));
            startActivity(yt);
        } catch (Exception e) {
            Log.e("inent", "", e);
        }
    }

    public void onEth(View view) {
        try {
            View ethaddr = findViewById(R.id.ethaddr);
            if (ethaddr.getVisibility() == View.GONE) {
                ethaddr.setVisibility(View.VISIBLE);
            } else {
                ethaddr.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e("inent", "", e);
        }
    }
}
