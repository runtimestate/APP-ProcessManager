package com.life.android.apmanager;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager.RunningServiceInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.life.android.apmanager.entity.Item;
import com.life.android.apmanager.entity.RunningArrayAdapter;

public class RunningListFragment extends ListFragment {

	private List<RunningServiceInfo> runningService;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.running_list, container, false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Bundle args = this.getArguments();
		if (args == null) {
			return;
		}
		runningService = (List<RunningServiceInfo>) args
				.getSerializable(MainActivity.RUNNING_SERVICE);

		List<Item> values = new ArrayList<Item>();
		for (int i = 0; i < runningService.size(); i++) {
			Item item = new Item();
			item.setClassName(runningService.get(i).service.getClassName());
			item.setPackageName(runningService.get(i).service.getPackageName());
			item.setSelected(Item.UNSELECTED_VALUE);
			values.add(item);
		}
		ArrayAdapter<Item> adapter = new RunningArrayAdapter(getActivity(),
				values);
		setListAdapter(adapter);
	}
}