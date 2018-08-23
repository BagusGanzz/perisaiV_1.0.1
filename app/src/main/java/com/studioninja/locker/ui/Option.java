package com.studioninja.locker.ui;

public class Option implements Comparable<Option> {
	String name;
	String data;
	

	String path;

	public Option(String n, String d, String p) {
		name = n;
		data = d;
		path = p;
	}

	public String getName() {
		return name;
	}

	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}

	public String getPath() {
		return path;
	}

	@Override
	public int compareTo(Option o) {
		if (this.name != null)
			return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
		else
			throw new IllegalArgumentException();
	}
}
