package com.life.android.apmanager;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.life.android.apmanager.entity.Item;
import com.life.android.apmanager.entity.StopArrayAdapter;
import com.life.android.apmanager.service.ItemDataSource;

public class StopListFragment extends ListFragment {

	private ItemDataSource itemDataSource;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.stop_list, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		itemDataSource = new ItemDataSource(getActivity());

		ArrayAdapter<Item> adapter = new StopArrayAdapter(getActivity(),
				itemDataSource.getAllItems());
		setListAdapter(adapter);
	}

}