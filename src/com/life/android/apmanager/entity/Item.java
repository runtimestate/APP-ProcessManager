package com.life.android.apmanager.entity;

import java.io.Serializable;

public class Item implements Serializable {

	private static final long serialVersionUID = 9058618938182048323L;

	public static final String TABLE_NAME = "sys_item";

	public static final String _ID = "_id";
	public static final String CLASS_NAME = "_className";
	public static final String PACKAGE_NAME = "_packageName";
	public static final String SELECTED = "_selected";

	public static final String SELECTED_VALUE = "1";
	public static final String UNSELECTED_VALUE = "0";

	private Long id;
	private String className;
	private String packageName;
	private String selected;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}
}
