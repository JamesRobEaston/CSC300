package clientServerPackage;

import java.io.Serializable;
import java.rmi.*;

public interface BPChangeObserver extends Remote, Serializable
{
	public void notifyBusinessPlanChanged() throws RemoteException;
	public String getUserToken() throws RemoteException;
	public void receiveText(String sender, String message) throws RemoteException;
}
