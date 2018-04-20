package editOrClonePopup;

import java.awt.event.ActionEvent;
import java.io.IOException;

import addDepartmentPopup.AddDepartmentPopupBoxController;
import applicationFiles.BPApplication;
import businessPlanView.BusinessPlanScreenController;
import clientServerPackage.ClientProxy;
import newBPPopup.NewBPPopupBoxController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Model;

public class EditOrClonePopupBoxController
{

	Stage popupStage;
	FXMLLoader loader;
	Scene editOrCloneScene;
	Model model;

	@FXML
	private Button editButton;

	@FXML
	private Button cloneButton;

	@FXML
	private Button cancelButton;

	public EditOrClonePopupBoxController()
	{
	}

	public EditOrClonePopupBoxController(Model model)
	{
		loader = new FXMLLoader();
		loader.setLocation(AddDepartmentPopupBoxController.class.getResource("AddDepartmentPopupBox.fxml"));
		try
		{
			editOrCloneScene = new Scene(loader.load());
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		popupStage = new Stage();
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("Edit or Clone Business Plan");
		popupStage.setResizable(false);
		popupStage.setScene(editOrCloneScene);
	}

	@FXML
	void cloneBusinessPlan(ActionEvent event) throws IOException
	{
		new NewBPPopupBoxController(model, true);
		popupStage.close();
	}

	@FXML
	void closePopup(ActionEvent event)
	{
		popupStage.close();
	}

	@FXML
	void editBusinessPlan(ActionEvent event)
	{
		new BusinessPlanScreenController(model);
		popupStage.close();

	}

	public void show()
	{
		popupStage.show();
	}
}
