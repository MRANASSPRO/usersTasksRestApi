/*
package com.example.userstasksrestapi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import me.saket.bettermovementmethod.BetterLinkMovementMethod;
import com.example.userstasksrestapi.model.User;
import com.example.userstasksrestapi.util.ShareUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class UserDetails extends AppCompatActivity {

    private TextView user_name_details, user_github_url;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareProfile();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle("");

        user_name_details = (TextView) findViewById(R.id.user_name_details);
        user_github_url = (TextView) findViewById(R.id.github_profile_url);

        //getting intent extra
        User user = getIntent().getParcelableExtra("user");


        user_name_details.setText(getString(R.string.user_name_full, user.getLogin()));
        user_github_url.setText(getString(R.string.user_url_full, user.getHtmlUrl()));


        BetterLinkMovementMethod movementMethod = BetterLinkMovementMethod.linkify(Linkify.WEB_URLS, user_github_url);
        movementMethod.setOnLinkClickListener(new BetterLinkMovementMethod.OnLinkClickListener() {
            @Override
            public boolean onClick(TextView textView, String url) {
                getCustomTabIntentInstance().launchUrl(UserDetails.this, Uri.parse(url));
                return true;
            }
        });
    }

    private CustomTabsIntent getCustomTabIntentInstance() {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        return builder.build();
    }

    private void shareProfile() {

        User user = getIntent().getParcelableExtra("user");


        String message = "Check out this awesome developer @" + user.getLogin() + ", " + user.getUrl();

        ShareUtils.shareCustom(message, this);

    }
}
*/
