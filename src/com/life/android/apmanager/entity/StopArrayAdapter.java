package com.life.android.apmanager.entity;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.life.android.apmanager.R;
import com.life.android.apmanager.service.ItemDataSource;

public class StopArrayAdapter extends ArrayAdapter<Item> {

	private ItemDataSource itemDataSource;
	private List<Item> list;
	private Activity context;

	public StopArrayAdapter(Activity context, List<Item> list) {
		super(context, R.layout.stop_item, list);
		this.context = context;
		this.list = list;
	}

	static class ViewHolder {
		protected TextView className;
		protected TextView removeFromStop;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		itemDataSource = new ItemDataSource(context);
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.stop_item, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.className = (TextView) view.findViewById(R.id.className);
			viewHolder.removeFromStop = (TextView) view
					.findViewById(R.id.removeFromStop);
			viewHolder.removeFromStop
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							Item element = (Item) viewHolder.removeFromStop
									.getTag();
							Item elementTemp = itemDataSource.getItem(element
									.getClassName());
							if (elementTemp != null) {
								itemDataSource.deleteItem(element);
								list.remove(element);
								notifyDataSetChanged();
							}
						}
					});
			view.setTag(viewHolder);
			viewHolder.removeFromStop.setTag(list.get(position));
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).removeFromStop.setTag(list
					.get(position));
		}

		ViewHolder holder = (ViewHolder) view.getTag();
		holder.className.setText(list.get(position).getPackageName() + "/"
				+ list.get(position).getClassName());
		return view;
	}
}