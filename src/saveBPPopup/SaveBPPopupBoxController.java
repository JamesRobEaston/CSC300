package saveBPPopup;

import clientServerPackage.BP;
import clientServerPackage.ClientProxy;
import homePage.homePageController;

import java.io.IOException;

import applicationFiles.BPApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Model;
import model.ModelInterface;
import viewAllBPView.ViewAllBPScreenController;

public class SaveBPPopupBoxController
{	
	public static Stage saveBPPopupBox;
	Model model;
	boolean shouldCloseOnAction;
	
	public SaveBPPopupBoxController() {}
	
	public SaveBPPopupBoxController(Model model)
	{
		this.model = model;

		// Create the stage to ask the user
		saveBPPopupBox = new Stage();
		saveBPPopupBox.setWidth(500.0);
		saveBPPopupBox.setHeight(350.0);
		saveBPPopupBox.initModality(Modality.APPLICATION_MODAL);
		saveBPPopupBox.setTitle("");
		saveBPPopupBox.setResizable(false);
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ViewAllBPScreenController.class.getResource("viewAllBPView/BusinessPlanScreen.fxml"));
		
		try
		{
			saveBPPopupBox.setScene(new Scene(loader.load()));
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
	}
	
	public void setModel(Model model)
	{
		this.model = model;
	}

	@FXML
	void cancel(ActionEvent event)
	{
		model.closePopupBox();
	}

	@FXML
	void discard(ActionEvent event)
	{
		if(shouldCloseOnAction)
		{
			model.closeApplication();
		}
		else
		{
			model.showHome();
		}
		model.closePopupBox();
	}

	@FXML
	void save(ActionEvent event)
	{
		model.save();
		if(shouldCloseOnAction)
		{
			model.closeApplication();
		}
		else
		{
			model.showHome();
		}
		model.closePopupBox();

	}

	public boolean isShouldCloseOnAction()
	{
		return shouldCloseOnAction;
	}

	public void setShouldCloseOnAction(boolean shouldCloseOnAction)
	{
		this.shouldCloseOnAction = shouldCloseOnAction;
	}

}