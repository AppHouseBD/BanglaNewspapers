package com.apphousebd.banglanewspapers.mainUI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.apphousebd.banglanewspapers.R;

public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener {

    private String facebookUrl;

    private TextView facebookAbd, facebookShaafi, facebookMohi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendMain("mailto:apphousebd@gmail.com" +
                        "?subject=FeedBack:(Your topic)");
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        facebookAbd = (TextView) findViewById(R.id.about_us_facebook_abd);
        facebookAbd.setOnClickListener(this);

        facebookShaafi = (TextView) findViewById(R.id.about_us_facebook_shaafi);
        facebookShaafi.setOnClickListener(this);

        facebookMohi = (TextView) findViewById(R.id.about_us_facebook_mohi);
        facebookMohi.setOnClickListener(this);
    }

    private void sendMain(String sub) {
        String mailto = sub;

        Intent sendMail = new Intent(Intent.ACTION_SENDTO);
        sendMail.setType("text/plain");
        sendMail.setData(Uri.parse(mailto));
        if (sendMail.resolveActivity(getPackageManager()) != null) {
            startActivity(sendMail);
        } else {
            Toast.makeText(AboutUsActivity.this, "No app found to complete the process!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.about_us_facebook_abd:
                facebookUrl = facebookAbd.getText().toString();
                break;
            case R.id.about_us_facebook_shaafi:
                facebookUrl = facebookShaafi.getText().toString();
                break;
            case R.id.about_us_facebook_mohi:
                facebookUrl = facebookMohi.getText().toString();
                break;

        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www." + facebookUrl));
        startActivity(intent);
    }
}
