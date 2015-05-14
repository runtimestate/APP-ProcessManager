package com.life.android.apmanager;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.life.android.apmanager.entity.Item;
import com.life.android.apmanager.service.ItemDataSource;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity implements OnClickListener {

	public static String RUNNING_PROCESS = "runningProcesses";
	public static String RUNNING_SERVICE = "runningServiceInfo";

	private Button stopButton;
	private Button runningButton;
	private ActivityManager manager;

	private List<RunningAppProcessInfo> runningProcesses;
	private List<RunningServiceInfo> runningService;

	private ItemDataSource itemDataSource;

	private boolean doubleBackToExitPressedOnce;

	static class ViewHolder {
		protected StopListFragment stopListFragment;
		protected RunningListFragment runningListFragment;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		stopButton = (Button) findViewById(R.id.stopButton);
		stopButton.setOnClickListener(this);
		runningButton = (Button) findViewById(R.id.runningButton);
		runningButton.setOnClickListener(this);

		stopButton.callOnClick();
	}

	@Override
	public void onClick(View paramView) {
		ViewHolder viewHolder = new ViewHolder();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		switch (paramView.getId()) {
		case R.id.stopButton:
			killService();

			viewHolder.stopListFragment = new StopListFragment();

			transaction
					.replace(R.id.listContainer, viewHolder.stopListFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			break;
		case R.id.runningButton:
			runningService = manager.getRunningServices(Integer.MAX_VALUE);

			viewHolder.runningListFragment = new RunningListFragment();
			Bundle args = new Bundle();
			args.putParcelableArrayList(RUNNING_SERVICE,
					(ArrayList<? extends Parcelable>) runningService);
			viewHolder.runningListFragment.setArguments(args);

			transaction.replace(R.id.listContainer,
					viewHolder.runningListFragment);
			transaction.commit();
			break;
		}
		paramView.setTag(viewHolder);
	}

	@Override
	public void onBackPressed() {
		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();
			return;
		}

		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, "Please click BACK again to exit",
				Toast.LENGTH_SHORT).show();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;
			}
		}, 2000);
	}

	private void killService() {
		runningProcesses = manager.getRunningAppProcesses();
		itemDataSource = new ItemDataSource(this);
		final List<Item> services = itemDataSource.getAllItems();

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				for (Item service : services) {
					Log.i("MainActivity", service.getPackageName());
					forceStop(service.getPackageName());

					manager.killBackgroundProcesses(service.getPackageName());
				}
			}
		}, 0, 50000);
	}

	private void forceStop(String packageName) {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("su");

			OutputStream out = process.getOutputStream();
			String cmd = "am force-stop " + packageName + " \n";
			out.write(cmd.getBytes());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (process == null) {
			return;
		}

		try {
			process.getOutputStream().close();
			process = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
