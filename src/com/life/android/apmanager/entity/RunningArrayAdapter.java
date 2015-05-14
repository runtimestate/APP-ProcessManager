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

public class RunningArrayAdapter extends ArrayAdapter<Item> {

	private ItemDataSource itemDataSource;
	private List<Item> list;
	private Activity context;

	public RunningArrayAdapter(Activity context, List<Item> list) {
		super(context, R.layout.running_item, list);
		this.context = context;
		this.list = list;
	}

	static class ViewHolder {
		protected TextView className;
		protected TextView addToStop;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		itemDataSource = new ItemDataSource(context);
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.running_item, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.className = (TextView) view.findViewById(R.id.className);
			viewHolder.addToStop = (TextView) view.findViewById(R.id.addToStop);
			viewHolder.addToStop.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Item element = (Item) viewHolder.addToStop.getTag();
					Item elementTemp = itemDataSource.getItem(element
							.getClassName());
					if (elementTemp == null) {
						itemDataSource.createItem(element.getClassName(),
								element.getPackageName(), Item.SELECTED);
					}
				}
			});
			view.setTag(viewHolder);
			viewHolder.addToStop.setTag(list.get(position));
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).addToStop.setTag(list.get(position));
		}

		ViewHolder holder = (ViewHolder) view.getTag();
		holder.className.setText(list.get(position).getPackageName() + "/"
				+ list.get(position).getClassName());
		return view;
	}
}