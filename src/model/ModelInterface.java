package model;

import java.io.Serializable;
import java.util.ArrayList;

import applicationFiles.BPApplication;
import businessPlanClasses.Statement;
import clientServerPackage.BP;
import clientServerPackage.ClientProxy;
import clientServerPackage.Department;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import newBPPopup.NewBPPopupBoxController;

public interface ModelInterface extends Serializable
{

	boolean authenticate(TextField userName_input, PasswordField pass_input, TextField serv_input, Button login_but);

	void notify(Scene scene);

	boolean addDepartment(String departmentName);

	ArrayList<Department> getAllDepartments();

	ClientProxy getClient();

	Department getDepartment();

	boolean isAdmin();

	BPApplication getApplication();

	void setApplication(BPApplication application);

	BP getBusinessPlan();

	void setModelBusinessPlan(BP businessPlan);

	Department getAdminDepartment();

	void setAdminDepartment(Department adminDepartment);

	void setClient(ClientProxy client);

	void setLocalCopy(BP businessPlan2);

	void setBusinessPlan(BP businessPlan2);

	void saveLocalPlanXML();

	void submitPlan();

	boolean addUser(String username, String password, String name, boolean b);

	void retrieve(String bpid);

	void showHome();

	void showLogin();

	void showViewAllBPScreen();

	void showBusinessPlanScreen(Statement statement);

	void showBusinessPlanScreen();

	void closePopupBox();

	void showAddUserPopupBox();

	void showAddDepartmentPopupBox();

	void showNewBPPopupBox(boolean isClone);

	void showCategoryPopupBox();

	void showSaveBPPopupBox(boolean shouldCloseOnAction);

	void showEditOrClone();

	void loadLocalCopy();

	void saveBPToDepartment(BP businessPlan2, String departmentName);
	
	void createNewBP(String id, String year, String department, boolean isClone, NewBPPopupBoxController cont);

	void closeApplication();
	
	void notifyBusinessPlanChanged();

}