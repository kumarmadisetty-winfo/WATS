package com.winfo.service;

public class Configuration {

	public static final int REMOTE_PORT = 8880;
	public static final String REMOTE_ID = "RMI";
	public static final String REMOTE_HOST = "192.168.1.203";

	private Configuration() {
		throw new IllegalStateException("Utility class");
	}
}