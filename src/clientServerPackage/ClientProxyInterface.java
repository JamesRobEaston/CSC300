package clientServerPackage;

import java.io.Serializable;
import java.rmi.*;

public interface ClientProxyInterface extends Remote, Serializable
{
	public void notifyBusinessPlanChanged() throws RemoteException;
	public String getUserToken() throws RemoteException;
	public void retrieve(String bpid) throws RemoteException;
	public void retrieve(String bpid, Department dept) throws RemoteException;
	public void save() throws RemoteException;
	public void saveToAdminDepartment() throws RemoteException;
	public void submitPlan() throws RemoteException;
}