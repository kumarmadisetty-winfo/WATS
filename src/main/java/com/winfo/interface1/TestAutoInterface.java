package com.winfo.interface1;

import java.net.MalformedURLException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface TestAutoInterface extends Remote{  

	String runAutomation(String[] args) throws RemoteException, MalformedURLException, SQLException;
	
}  
