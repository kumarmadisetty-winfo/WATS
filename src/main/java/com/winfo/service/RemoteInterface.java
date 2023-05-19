package com.winfo.service;

import java.net.MalformedURLException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface RemoteInterface extends Remote {
	public String capitalize(String str) throws RemoteException;

	public String runAutomation(String x) throws RemoteException, MalformedURLException, SQLException;
}
