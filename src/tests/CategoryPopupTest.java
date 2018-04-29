package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.framework.junit.ApplicationTest;

import addDepartmentPopup.AddDepartmentPopupBoxController;
import businessPlanClasses.Category;
import businessPlanClasses.PlanDesign;
import categoryPopupBox.CategoryPopupBoxController;
import clientServerPackage.BP;
import clientServerPackage.ClientProxy;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class CategoryPopupTest extends ApplicationTest
{

	MockModel model;
	CategoryPopupBoxController cont;
	
	@Override
	public void start(Stage stage) throws Exception
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(CategoryPopupBoxController.class.getResource("/categoryPopupBox/CategoryPopupBox.fxml"));
		try
		{
			Scene scene = new Scene(loader.load());
			stage.setTitle("Edit or Clone Business Plan");
			stage.setResizable(false);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		model = new MockModel(new ClientProxy(), null, new BP("test","test","test"));
		cont = loader.getController();
		cont.setModel(model);
	}
	
	@BeforeEach
	public void resetModel()
	{
		model = new MockModel(new ClientProxy(), null, new BP("test","test","test"));
		cont.setModel(model);
	}
	
	@Test
	public void testCancelButton()
	{
		model = new MockModel(new ClientProxy(), null, new BP("test","test","test"));
		cont.setModel(model);
		assertEquals(0, model.getClosePopupBoxMethodCallCounter());
		clickOn("#cancelButton");
		assertEquals(1, model.getClosePopupBoxMethodCallCounter());
	}
	
	@Test
	public void testCreateCustomPlan()
	{
		model = new MockModel(new ClientProxy(), null, new BP("test","test","test"));
		cont.setModel(model);
		clickOn("#newCategoryButton");
		clickOn("#newCategoryButton");
		clickOn("#newCategoryButton");
		clickOn("#newCategoryButton");
		push(KeyCode.SHIFT, KeyCode.TAB);
		push(KeyCode.SHIFT, KeyCode.TAB);
		write("3rd Element");
		push(KeyCode.SHIFT, KeyCode.TAB);
		push(KeyCode.SHIFT, KeyCode.TAB);
		write("2nd Element");
		push(KeyCode.SHIFT, KeyCode.TAB);
		push(KeyCode.SHIFT, KeyCode.TAB);
		write("1st Element");
		push(KeyCode.SHIFT, KeyCode.TAB);
		push(KeyCode.SHIFT, KeyCode.TAB);
		write("ShouldBeDeleted");
		push(KeyCode.TAB);
		push(KeyCode.ENTER);
		clickOn("Delete");
		clickOn("#submitButton");
		checkCorrectSubmission(new String[]{"1st Element","2nd Element","3rd Element"});
	}
	
	@Test
	public void testCreateCentrePlan()
	{
		model = new MockModel(new ClientProxy(), null, new BP("test","test","test"));
		cont.setModel(model);
		clickOn("#centreButton");
		clickOn("#submitButton");
		checkCorrectSubmission(new String[]{"Department","Goal","Student Learning Objective"});
	}
	
	@Test
	public void testCreateVMOSAPlan()
	{
		model = new MockModel(new ClientProxy(), null, new BP("test","test","test"));
		cont.setModel(model);
		clickOn("#VMOSAButton");
		clickOn("#submitButton");
		checkCorrectSubmission(new String[]{"Mission","Objective","Strategy","Action Plan"});
	}

	private void checkCorrectSubmission(String[] strings)
	{
		ArrayList<Category> catList = model.client.getLocalCopy().getDesign().getCategoryList();
		Iterator<Category> catIter = catList.iterator();
		int i = 0;
		while(catIter.hasNext() && (i<strings.length))
		{
			assertEquals(catIter.next().getName(),strings[i]);
			i++;
		}
		if(catIter.hasNext())
		{
			fail("Extra categories added.");
		}
		if(strings.length>i+1)
		{
			fail("Not all categories added.");
		}
	}

}
