package tests;

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
import model.Model;
import model.ModelInterface;

public class MockModel extends Model
{

	public MockModel(ClientProxy client, BPApplication application, BP businessPlan)
	{
		super(client, application, businessPlan);
	}

	public int authenticateMethodCallCounter = 0;;
	@Override
	public boolean authenticate(TextField userName_input, PasswordField pass_input, TextField serv_input,
			Button login_but)
	{
		authenticateMethodCallCounter++;
		return super.authenticate(userName_input, pass_input, serv_input, login_but);
	}

	public int notifyMethodCallCounter = 0;;
	@Override
	public void notify(Scene scene)
	{
		notifyMethodCallCounter++;
	}

	public int addDepartmentMethodCallCounter = 0;;
	@Override
	public boolean addDepartment(String departmentName)
	{
		addDepartmentMethodCallCounter++;
		return super.addDepartment(departmentName);
	}

	public int getAllDepartmentsMethodCallCounter = 0;
	@Override
	public ArrayList<Department> getAllDepartments()
	{
		getAllDepartmentsMethodCallCounter++;
		return super.getAllDepartments();
	}

	public int getClientMethodCallCounter = 0;
	@Override
	public ClientProxy getClient()
	{
		getClientMethodCallCounter++;
		return super.getClient();
	}

	public int getDepartmentMethodCallCounter = 0;
	@Override
	public Department getDepartment()
	{
		getDepartmentMethodCallCounter++;
		return super.getDepartment();
	}

	public int isAdminMethodCallCounter = 0;
	@Override
	public boolean isAdmin()
	{
		isAdminMethodCallCounter++;
		return super.isAdmin();
	}

	public int getApplicationMethodCallCounter = 0;
	@Override
	public BPApplication getApplication()
	{
		getApplicationMethodCallCounter++;
		return super.getApplication();
	}

	public int setApplicationMethodCallCounter = 0;
	@Override
	public void setApplication(BPApplication application)
	{
		setApplicationMethodCallCounter++;
		super.setApplication(application);
	}

	public int getBusinessPlanMethodCallCounter = 0;
	@Override
	public BP getBusinessPlan()
	{
		getBusinessPlanMethodCallCounter++;
		return super.getBusinessPlan();
	}

	public int setModelBusinessPlanMethodCallCounter = 0;
	@Override
	public void setModelBusinessPlan(BP businessPlan)
	{
		setModelBusinessPlanMethodCallCounter++;
		super.setModelBusinessPlan(businessPlan);
	}

	public int getAdminDepartmentMethodCallCounter = 0;
	@Override
	public Department getAdminDepartment()
	{
		getAdminDepartmentMethodCallCounter++;
		return super.getAdminDepartment();
	}

	public int setAdminDepartmentMethodCallCounter = 0;
	@Override
	public void setAdminDepartment(Department adminDepartment)
	{
		setAdminDepartmentMethodCallCounter++;
		super.setAdminDepartment(adminDepartment);
	}

	public int setClientMethodCallCounter = 0;
	@Override
	public void setClient(ClientProxy client)
	{
		setClientMethodCallCounter++;
		super.setClient(client);
	}

	public int setLocalCopyMethodCallCounter = 0;
	@Override
	public void setLocalCopy(BP businessPlan2)
	{
		setLocalCopyMethodCallCounter++;
		super.setLocalCopy(businessPlan2);
	}

	public int setBusinessPlanMethodCallCounter = 0;
	@Override
	public void setBusinessPlan(BP businessPlan2)
	{
		setBusinessPlanMethodCallCounter++;
		super.setBusinessPlan(businessPlan2);
	}

	public int saveLocalPlanXMLMethodCallCounter = 0;
	@Override
	public void saveLocalPlanXML()
	{
		saveLocalPlanXMLMethodCallCounter++;
		super.saveLocalPlanXML();
	}

	public int submitPlanMethodCallCounter = 0;
	@Override
	public void submitPlan()
	{
		submitPlanMethodCallCounter++;
		super.submitPlan();
	}

	public int addUserMethodCallCounter = 0;
	@Override
	public boolean addUser(String username, String password, String name, boolean b)
	{
		addUserMethodCallCounter++;
		return super.addUser(username, password, name, b);
	}

	public int retrieveMethodCallCounter = 0;
	@Override
	public void retrieve(String bpid)
	{
		retrieveMethodCallCounter++;
		super.retrieve(bpid);
	}

	public int showHomeMethodCallCounter = 0;
	@Override
	public void showHome()
	{
		showHomeMethodCallCounter++;
	}

	public int showLoginMethodCallCounter = 0;
	@Override
	public void showLogin()
	{
		showLoginMethodCallCounter++;
	}

	public int showViewAllBPScreenMethodCallCounter = 0;
	@Override
	public void showViewAllBPScreen()
	{
		showViewAllBPScreenMethodCallCounter++;
	}

	public int showBusinessPlanScreenMethodCallCounter = 0;
	@Override
	public void showBusinessPlanScreen(Statement statement)
	{
		showBusinessPlanScreenMethodCallCounter++;
	}

	@Override
	public void showBusinessPlanScreen()
	{
		showBusinessPlanScreenMethodCallCounter++;
	}

	public int closePopupBoxMethodCallCounter = 0;
	@Override
	public void closePopupBox()
	{
		closePopupBoxMethodCallCounter++;
	}

	public int showAddUserPopupBoxMethodCallCounter = 0;
	@Override
	public void showAddUserPopupBox()
	{
		showAddUserPopupBoxMethodCallCounter++;
	}

	public int showAddDepartmentPopupBoxMethodCallCounter = 0;
	@Override
	public void showAddDepartmentPopupBox()
	{
		showAddDepartmentPopupBoxMethodCallCounter++;
	}

	public int showNewBPPopupBoxMethodCallCounter = 0;
	@Override
	public void showNewBPPopupBox(boolean isClone)
	{
		showNewBPPopupBoxMethodCallCounter++;
	}

	public int showCategoryPopupBoxMethodCallCounter = 0;
	@Override
	public void showCategoryPopupBox()
	{
		showCategoryPopupBoxMethodCallCounter++;
	}

	public int showSaveBPPopupBoxMethodCallCounter = 0;
	@Override
	public void showSaveBPPopupBox()
	{
		showSaveBPPopupBoxMethodCallCounter++;
	}

	public int showEditOrCloneMethodCallCounter = 0;
	@Override
	public void showEditOrClone()
	{
		showEditOrCloneMethodCallCounter++;
	}

	public int loadLocalCopyMethodCallCounter = 0;
	@Override
	public void loadLocalCopy()
	{
		loadLocalCopyMethodCallCounter++;
		super.loadLocalCopy();
	}

	public int saveBPToDepartmentMethodCallCounter = 0;
	@Override
	public void saveBPToDepartment(BP businessPlan2, String departmentName)
	{
		saveBPToDepartmentMethodCallCounter++;
		super.saveBPToDepartment(businessPlan2, departmentName);
	}

}
