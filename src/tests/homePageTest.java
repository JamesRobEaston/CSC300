package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.framework.junit.ApplicationTest;

import homePage.homePageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class homePageTest extends ApplicationTest
{
	MockModel model;
	homePageController hpc;
	@Override
	public void start(Stage primaryStage) throws Exception
	{

		model = new MockModel(null, null, null);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(homePageController.class.getResource("/homePage/HomePageView.fxml"));
		try
		{
			Scene homePage = new Scene(loader.load());
			hpc = loader.getController();
			hpc.setModel(model);
			primaryStage.setScene(homePage);
			primaryStage.show();
		
		} catch (IOException e)
		{

			e.printStackTrace();
		}
		
	
		
	}
	
	@BeforeEach
	public void resetModel()
	{
		model = new MockModel(null, null, null);
	}
	
	
	public void testViewAll()
	{
		clickOn("#viewAll_but");
		assertEquals(model.showViewAllBPScreenMethodCallCounter, 1);
		
	}
	
	public void testLocalCopy()
	{
		clickOn("#loadlc_but");
		assertEquals(model.loadLocalCopyMethodCallCounter, 1);
	}
	
	public void testAddNewUser()
	
	{
		clickOn("#addUser_but");
		assertEquals(model.showAddUserPopupBoxMethodCallCounter, 1);
	}
	
	public void testAddNewDepartment()
	{
		clickOn("#addDept_but");
		assertEquals(model.showAddDepartmentPopupBoxMethodCallCounter, 1);
		
	}
	
	public void testNewPlan()
	{
		clickOn("#newBP_but");
		assertEquals(model.showNewBPPopupBoxMethodCallCounter, 1);
		
	}
	
	public void testLogOut()
	{
		clickOn("#logout_but");
		assertEquals(model.showLoginMethodCallCounter, 1);
		
	}
	
	
	@Test
	public void testValidLoginInvalidServer()
	{
		testViewAll();
		testLocalCopy();
		testAddNewUser();
		testAddNewDepartment();
		testNewPlan();
		testLogOut();
		
	}

	
	
	
}
