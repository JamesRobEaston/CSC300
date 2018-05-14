package clientServerPackage;
import java.rmi.*;
import java.util.ArrayList;

public interface ServerInterface extends Remote {

	String authenticate(String username, String password) throws RemoteException;
	String[][] view(String userToken) throws RemoteException;
	BP retrieve(String userToken, String bpid) throws RemoteException;
	void save(String userToken, BP bpid, BPChangeObserver stub) throws RemoteException;
	boolean addUser(String userToken, String username, String password, String departmentName, boolean isAdmin) throws RemoteException;
	boolean addDepartment(String userToken, String departmentName) throws RemoteException;
	boolean isAdmin(String userToken) throws RemoteException;
	ArrayList<Department> getAllDepartments() throws RemoteException;
	void delete(String userToken, BP bp) throws RemoteException;
	Department getDepartment(String userToken) throws RemoteException;
	void saveToAdminDepartment(String userToken,BP bp, BPChangeObserver stub) throws RemoteException;
	void saveBPToDepartment(String userToken, BP bp, String dept, BPChangeObserver stub) throws RemoteException;
	String[][] view(String userToken, Department dept) throws RemoteException;
	BP retrieve(String userToken, String bpid, Department dept) throws RemoteException;
	ArrayList<ConcreteClient> getUsers() throws RemoteException;
	public void subscribeToBP(BP bp, BPChangeObserver stub) throws RemoteException;
	public void unSubscribeFromBP(BP bp, BPChangeObserver stub) throws RemoteException;
	public void disperseText(String userToken, BP bp, String message, BPChangeObserver stub) throws RemoteException;
}
