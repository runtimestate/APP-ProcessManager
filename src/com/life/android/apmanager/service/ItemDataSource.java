/**
 * This file is part of ${project_name}.
 * 
 * ${project_name} is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * ${project_name} is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * ${project_name}. If not, see <http://www.gnu.org/licenses/>.
 * 
 * @see http://www.gnu.org/licenses/lgpl.txt
 * @author art <runtimestate@gmail.com>
 */
package com.life.android.apmanager.service;

import static android.provider.BaseColumns._ID;
import static com.life.android.apmanager.entity.Item.CLASS_NAME;
import static com.life.android.apmanager.entity.Item.PACKAGE_NAME;
import static com.life.android.apmanager.entity.Item.SELECTED;
import static com.life.android.apmanager.entity.Item.TABLE_NAME;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.life.android.apmanager.dao.SQLiteHelper;
import com.life.android.apmanager.entity.Item;

public class ItemDataSource {

	private static String[] allColumns = { _ID, CLASS_NAME, PACKAGE_NAME,
			SELECTED };
	private static String ORDER_BY = CLASS_NAME + " DESC";

	private SQLiteDatabase database;
	private SQLiteHelper dbHelper;

	public ItemDataSource(Context context) {
		dbHelper = new SQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Item createItem(String className, String packageName, String selected) {
		Item newItem = null;
		try {
			open();
			ContentValues values = new ContentValues();
			values.put(CLASS_NAME, className);
			values.put(PACKAGE_NAME, packageName);
			values.put(SELECTED, selected);
			long insertId = database.insert(TABLE_NAME, null, values);
			Cursor cursor = database.query(TABLE_NAME, allColumns, _ID + " = "
					+ insertId, null, null, null, null);
			cursor.moveToFirst();
			newItem = cursorToItem(cursor);
			cursor.close();
		} finally {
			close();
		}
		return newItem;
	}

	public void deleteItem(Item item) {
		try {
			open();
			String className = item.getClassName();
			database.delete(TABLE_NAME, CLASS_NAME + " = '" + className + "'",
					null);
		} finally {
			close();
		}
	}

	public boolean updateItem(Item item) {
		boolean flag = false;
		try {
			open();
			ContentValues values = new ContentValues();
			values.put(_ID, item.getId());
			values.put(CLASS_NAME, item.getClassName());
			values.put(PACKAGE_NAME, item.getPackageName());
			values.put(SELECTED, item.getSelected());
			flag = database.update(TABLE_NAME, values,
					_ID + " = " + item.getId(), null) > 0;
		} finally {
			close();
		}
		return flag;
	}

	public Item getItem(Long id) {
		Item item = null;
		try {
			open();
			Cursor cursor = database.query(TABLE_NAME, allColumns, _ID + " = "
					+ id, null, null, null, null);
			cursor.moveToFirst();
			item = cursorToItem(cursor);
			cursor.close();
		} finally {
			close();
		}
		return item;
	}

	public Item getItem(String className) {
		Item item = null;
		try {
			open();
			Cursor cursor = database.query(TABLE_NAME, allColumns, CLASS_NAME
					+ " = '" + className + "'", null, null, null, null);
			cursor.moveToFirst();
			item = cursorToItem(cursor);
			cursor.close();
		} finally {
			close();
		}
		return item;
	}

	public List<Item> getAllItems() {
		List<Item> items = new ArrayList<Item>();
		try {
			open();

			Cursor cursor = database.query(TABLE_NAME, allColumns, null, null,
					null, null, ORDER_BY);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Item item = cursorToItem(cursor);
				items.add(item);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
		} finally {
			close();
		}
		return items;
	}

	private Item cursorToItem(Cursor cursor) {
		if (cursor.getCount() < 1) {
			return null;
		}
		Item item = new Item();
		item.setId(cursor.getLong(0));
		item.setClassName(cursor.getString(1));
		item.setPackageName(cursor.getString(2));
		item.setSelected(cursor.getString(3));
		return item;
	}

}
