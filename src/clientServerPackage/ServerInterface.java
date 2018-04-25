package clientServerPackage;
import java.rmi.*;
import java.util.ArrayList;

public interface ServerInterface extends Remote {

	String authenticate(String username, String password) throws RemoteException;
	String[][] view(String userToken) throws RemoteException;
	BP retrieve(String userToken, String bpid) throws RemoteException;
	void save(String userToken, BP bpid) throws RemoteException;
	boolean addUser(String userToken, String username, String password, String departmentName, boolean isAdmin) throws RemoteException;
	boolean addDepartment(String userToken, String departmentName) throws RemoteException;
	boolean isAdmin(String userToken) throws RemoteException;
	ArrayList<Department> getAllDepartments() throws RemoteException;
	void delete(String userToken, BP bp) throws RemoteException;
	Department getDepartment(String userToken) throws RemoteException;
	void saveToAdminDepartment(String userToken,BP bp) throws RemoteException;
	void saveBPToDepartment(String userToken, BP bp, String dept) throws RemoteException;
	String[][] view(String userToken, Department dept) throws RemoteException;
	BP retrieve(String userToken, String bpid, Department dept) throws RemoteException;
	ArrayList<ConcreteClient> getUsers() throws RemoteException;
}
