package playground.jkzhou.filetransfer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import playground.jkzhou.filetransfer.controller.FileListAdapter;
import playground.jkzhou.filetransfer.controller.ProgressUIController;
import playground.jkzhou.filetransfer.files.ContentManager;
import playground.jkzhou.filetransfer.files.IPublisher;
import playground.jkzhou.filetransfer.files.ISubscriber;
import playground.jkzhou.filetransfer.message.MessageReceiver;
import playground.jkzhou.filetransfer.message.handler.UIMessageHandler;
import playground.jkzhou.filetransfer.utils.AppUtils;
import playground.jkzhou.filetransfer.web.WebManager;
import playground.jkzhou.filetransfer.web.handler.FileRequestHandler;


public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	private static final int READ_REQ_CODE = 42;
	private static final String TAG = "MainActivity";
	private ProgressUIController progressControl;
	private CoordinatorLayout topLayout;
	private UIMessageHandler uiMsgHandler = new UIMessageHandler();
	private IPublisher<Uri> uriPublisher;
	private WebManager webMgr;
	private FileListAdapter listAdapter;

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent result) {
		if (READ_REQ_CODE == reqCode && Activity.RESULT_OK == resultCode) {
			if (result != null) {
				ClipData selections = result.getClipData();
				Uri selectedUri = result.getData();
				if (selections != null) {
					for (int i = 0; i < selections.getItemCount(); i++) {
						ClipData.Item item = selections.getItemAt(i);
						uriPublisher.publish(item.getUri());
					}
				} else if (selectedUri != null) {
					uriPublisher.publish(selectedUri);
				} else {
					Log.d(TAG, "onActivityResult: 0 selection");
				}
			}
		}
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
			pickImage();
		} else if (id == R.id.nav_slideshow) {

		} else if (id == R.id.nav_manage) {

		} else if (id == R.id.nav_share) {

		} else if (id == R.id.nav_send) {

		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initUI();
		initWebManager();
		initPublisher();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		webMgr.stopServer();
		AppUtils.trimCache(this);
	}

	private void initPublisher() {
		uriPublisher = new ContentManager(this);
		uriPublisher.addSubscriber(new ISubscriber() {
			@Override
			public void notifyUpdate() {
				MainActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						List<Uri> updated = uriPublisher.get();
						Log.d(TAG, "notifyUpdate: published: " + updated);
						listAdapter.refresh(updated);
					}
				});
			}
		});
	}

	private void initUI() {
		setContentView(R.layout.activity_main);

		/* Config progress bar */
		this.progressControl = new ProgressUIController(new ProgressDialog(this));

		/* Config status bar */
		this.topLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

		/* Config tool bar */
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		/* Config Fab button */
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				String msg = "Host IP: " + AppUtils.getDeviceWifiIP(MainActivity.this);
				Toast toast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();
				Log.d(TAG, msg);
			}
		});
		fab.setEnabled(true);

		/* Config drawer */
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		/* Config navigation view */
		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		ListView fileListView = (ListView) findViewById(R.id.list_view_main);
		listAdapter = new FileListAdapter(this, R.layout.file_list_item, new ArrayList<Uri>());
		fileListView.setAdapter(listAdapter);

		showServerStatus(false);
	}

	private void initWebManager() {
		uiMsgHandler.setReceiver(new MessageReceiver() {
			@Override
			public void receive(Object message) {
				Toast toast = Toast.makeText(MainActivity.this, (String) message, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();
			}
		});

		progressControl.initAndShow(ProgressDialog.STYLE_HORIZONTAL, false, "Loading ...");

		new Thread(new Runnable() {
			Context app = MainActivity.this;
			int progress = 0;

			@Override
			public void run() {
				final List<String> assets = AppUtils.scanAssets(app, WebManager.WEB_BASE_DIR);
				String cacheDir = AppUtils.getCacheDir(app);

				/* Before execution */
				progressControl.setMax(assets.size() + 1);

				for (int i = 0; i < assets.size(); i++) {
					AppUtils.copyFile(app, assets.get(i), cacheDir + File.separator + assets.get(i));

					progressControl.update(++progress);
					//Log.d(TAG, "Load resource: " + i + "/" + (assets.size() - 1));
				}

				webMgr = WebManager.getInstance();
				webMgr.setStaticFilesPath(cacheDir + File.separator + WebManager.WEB_BASE_DIR);
				webMgr.setUiMessenger(uiMsgHandler);
				new FileRequestHandler(webMgr).register();
				progressControl.dismiss();
				Log.d(TAG, "Load resource: completed");
			}
		}).start();
	}

	private void pickImage() {
		// Open a file via system file browser
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

		// Only show results that are openable, e.g. files
		intent.addCategory(Intent.CATEGORY_OPENABLE);

		// Allow multiple selections
		intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

		// Add more specific filter, shows images only
		intent.setType("*/*");

		startActivityForResult(intent, READ_REQ_CODE);
	}

	private void showServerStatus(boolean isOn) {
		final Snackbar bar = Snackbar.make(topLayout, "", Snackbar.LENGTH_INDEFINITE);

		View view = bar.getView();
		// change snackBar text color
		int snackBarTextId = android.support.design.R.id.snackbar_text;
		TextView textView = (TextView) view.findViewById(snackBarTextId);

		if (isOn) {
			textView.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.lightGreen));
			bar.setText("Web Server is ready")
					.setAction("Turn OFF", new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							bar.dismiss();
							webMgr.stopServer();
							showServerStatus(false);
						}
					}).show();
		} else {
			textView.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.pink));
			bar.setText("Web server is OFF")
					.setAction("Turn ON", new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							bar.dismiss();
							webMgr.startServer();
							showServerStatus(true);
						}
					}).show();
		}
	}

}
