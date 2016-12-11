package playground.jkzhou.filetransfer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.util.List;

import playground.jkzhou.filetransfer.server.SparkStarter;
import playground.jkzhou.filetransfer.utils.AppUtils;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private CoordinatorLayout appLayout;
    private SparkStarter sparkStarter;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sparkStarter = SparkStarter.getInstance();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String msg = "Host IP: " + AppUtils.getDeviceWifiIP(MainActivity.this);
                Snackbar.make(fab, msg, Snackbar.LENGTH_LONG).show();
                Log.d(TAG, msg);
            }
        });
        fab.setEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        prepareServer();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            startServer();
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sparkStarter.stop();
        AppUtils.trimCache(this);
    }

    private void startServer() {
        sparkStarter.start();
        Snackbar.make(appLayout, "Web server is ready", Snackbar.LENGTH_LONG)
                .setAction("OFF", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        stopServer();
                    }
                }).show();
    }

    private void stopServer() {
        sparkStarter.stop();
        Snackbar.make(appLayout, "Web server is OFF", Snackbar.LENGTH_LONG).show();
    }

    private void prepareServer() {
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setMessage("Loading resources ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setCancelable(false);
        progressBar.setProgress(0);
        progressBar.show();

        new Thread(new Runnable() {
            Context app = MainActivity.this;
            int progress = 0;

            @Override
            public void run() {
                final List<String> assets = AppUtils.scanAssets(app, SparkStarter.WEB_BASE_DIR);
                String cacheDir = AppUtils.getCacheDir(app);

                mHandler.post(new Runnable() {
                    /* Before execution */
                    @Override
                    public void run() {
                        progressBar.setMax(assets.size() + 1);
                        progressBar.setProgress(++progress);
                    }
                });

                for (int i = 0; i < assets.size(); i++) {
                    AppUtils.copyFile(app, assets.get(i), cacheDir + File.separator + assets.get(i));

                    mHandler.post(new Runnable() {
                        /* Task executing */
                        @Override
                        public void run() {
                            progressBar.setProgress(++progress);
                        }
                    });
                    Log.d(TAG, "Load resource: " + i + "/" + (assets.size() - 1));
                }

                sparkStarter.setStaticFileLocation(cacheDir + File.separator + SparkStarter.WEB_BASE_DIR);
                mHandler.post(new Runnable() {
                    /* Task Completed */
                    @Override
                    public void run() {
                        progressBar.dismiss();
                    }
                });
                Log.d(TAG, "Load resource: completed");
            }
        }).start();
    }

}
