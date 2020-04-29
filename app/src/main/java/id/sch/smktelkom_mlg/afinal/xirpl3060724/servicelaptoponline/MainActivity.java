package id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Arrays;
import java.util.List;

import id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline.adapter.TabLayoutAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int RC_SIGN_IN = 123;
    TabLayoutAdapter layoutAdapter = new TabLayoutAdapter(getSupportFragmentManager());
    long cacheExpiration = 0;
    FirebaseRemoteConfig mFirbeaseConfig;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView tv_navdraw;
    private ProgressDialog progressDialog;
    private String urlApks, messageApk, urlWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Dashboard");

        //using for tabLayout
        viewPager = findViewById(R.id.vp_center);
        tabLayout = findViewById(R.id.slideTab);

        TabLayoutAdapter layoutAdapter = new TabLayoutAdapter(getSupportFragmentManager());
        viewPager.setAdapter(layoutAdapter);
        tabLayout.setupWithViewPager(viewPager);

        progressDialog = new ProgressDialog(this);
        mFirbeaseConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();
        mFirbeaseConfig.setConfigSettings(remoteConfigSettings);
        mFirbeaseConfig.setDefaults(R.xml.remote_config_defaults);


        mFirbeaseConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mFirbeaseConfig.activateFetched();
                            urlApks = String.valueOf(mFirbeaseConfig.getString("link_apk_release"));
                            messageApk = String.valueOf(mFirbeaseConfig.getString("mesagge_app"));
                            urlWebsite = String.valueOf(mFirbeaseConfig.getString("url_website"));
                        }
                    }
                });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();

        MenuItem nav_log = menu.findItem(R.id.nav_share);
        tv_navdraw = headerView.findViewById(R.id.tv_navdraw);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //checking
        if (user != null) {
            nav_log.setTitle("Sign Out");
            tv_navdraw.setText(user.getDisplayName());
        } else {
            nav_log.setTitle("Sign In");
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            viewPager.setAdapter(layoutAdapter);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()
        );

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (id == R.id.nav_service) {

            Intent intent = new Intent(this, CenterActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_problem) {

            Intent intent = new Intent(this, ArticleActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_website) {
            String url = urlWebsite;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);

        } else if (id == R.id.nav_share) {
            if (user != null) {
                progressDialog.setMessage("Logging Out");
                progressDialog.show();
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                NavigationView navigationView = findViewById(R.id.nav_view);
                                View headerView = navigationView.getHeaderView(0);
                                tv_navdraw = headerView.findViewById(R.id.tv_navdraw);
                                Menu menu = navigationView.getMenu();
                                MenuItem nav_log = menu.findItem(R.id.nav_share);

                                progressDialog.hide();

                                Toast.makeText(getApplication(), "You has been Logged Out", Toast.LENGTH_SHORT).show();
                                nav_log.setTitle("Sign In");
                                tv_navdraw.setText("Guest");
                            }
                        });
            } else {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .setTheme(R.style.AppTheme)
                                .build(), RC_SIGN_IN
                );
            }
        } else if (id == R.id.nav_send) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, messageApk + ": " + urlApks);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        View headerView = navigationView.getHeaderView(0);
        MenuItem nav_log = menu.findItem(R.id.nav_share);
        tv_navdraw = headerView.findViewById(R.id.tv_navdraw);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    nav_log.setTitle("Sign Out");
                    tv_navdraw.setText(user.getDisplayName());
                } else {
                    nav_log.setTitle("Sign In");
                }
            } else {
                Toast.makeText(this, "Sorry. You can't Login Now", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
