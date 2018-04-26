package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.framework.junit.ApplicationTest;

import addDepartmentPopup.AddDepartmentPopupBoxController;
import editOrClonePopup.EditOrClonePopupBoxController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditOrClonePopupBoxTest extends ApplicationTest
{

	MockModel model;
	
	@Override
	public void start(Stage stage) throws Exception
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(EditOrClonePopupBoxController.class.getResource("/editOrClonePopup/EditOrClonePopupBox.fxml"));
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
		EditOrClonePopupBoxController cont = loader.getController();
		cont.setModel(model);
	}
	
	@BeforeEach
	public void resetModel()
	{
		model = new MockModel(null, null, null);
	}
	
	@Test
	public void testEditButton()
	{
		assertEquals(0, model.getClosePopupBoxMethodCallCounter());
		assertEquals(0, model.getShowBusinessPlanScreenMethodCallCounter());
		clickOn("#editButton");
		assertEquals(1, model.getShowBusinessPlanScreenMethodCallCounter());
		assertEquals(1, model.getClosePopupBoxMethodCallCounter());
	}
	
	@Test
	public void testCloneButton()
	{
		assertEquals(0, model.getClosePopupBoxMethodCallCounter());
		assertEquals(0, model.getShowNewBPPopupBoxMethodCallCounter());
		clickOn("#cloneButton");
		assertEquals(1, model.getShowNewBPPopupBoxMethodCallCounter());
		assertEquals(1, model.getClosePopupBoxMethodCallCounter());
	}
	
	@Test
	public void testCancelButton()
	{
		assertEquals(0, model.getClosePopupBoxMethodCallCounter());
		clickOn("#cancelButton");
		assertEquals(1, model.getClosePopupBoxMethodCallCounter());
	}
}
