package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.framework.junit.ApplicationTest;

import addDepartmentPopup.AddDepartmentPopupBoxController;
import categoryPopupBox.CategoryPopupBoxController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.concurrent.TimeUnit;

public class CategoryPopupTest extends ApplicationTest
{

	MockModel model;
	
	@Override
	public void start(Stage stage) throws Exception
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(CategoryPopupBoxController.class.getResource("/categoryPopupBox/CategoryPopupBox.fxml"));
		try
		{
			Scene editOrCloneScene = new Scene(loader.load());
			stage.setTitle("Edit or Clone Business Plan");
			stage.setResizable(false);
			stage.setScene(editOrCloneScene);
			stage.show();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		model = new MockModel(null, null, null);
		CategoryPopupBoxController cont = loader.getController();
		cont.setModel(model);
	}
	
	@BeforeEach
	public void resetModel()
	{
		model = new MockModel(null, null, null);
	}
	
	@Test
	public void testCancelButton()
	{
		assertEquals(0, model.getClosePopupBoxMethodCallCounter());
		clickOn("#cancelButton");
		assertEquals(1, model.getClosePopupBoxMethodCallCounter());
	}
	
	@Test
	public void testCreateCustomPlan()
	{
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
		clickOn("#centreButton");
		clickOn("#submitButton");
		checkCorrectSubmission(new String[]{"Organization","Department","Goal","Student Learning Objective"});
	}
	
	@Test
	public void testCreateVMOSAPlan()
	{
		clickOn("#VMOSAButton");
		clickOn("#submitButton");
		checkCorrectSubmission(new String[]{"Vision","Mission","Objective","Strategy","Action Plan"});
	}

	private void checkCorrectSubmission(String[] strings)
	{
		// TODO check that the created design matches the input
		
	}

}
