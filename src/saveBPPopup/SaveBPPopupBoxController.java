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
import viewAllBPView.ViewAllBPScreenController;

public class SaveBPPopupBoxController
{	
	public static Stage saveBPPopupBox;
	Model model;
	
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

	@FXML
	void cancel(ActionEvent event)
	{
		saveBPPopupBox.close();
	}

	@FXML
	void discard(ActionEvent event)
	{
		homePageController homePage = new homePageController(model);
		model.notify(homePage.getScene());
		saveBPPopupBox.close();
	}

	@FXML
	void save(ActionEvent event)
	{
		homePageController homePage = new homePageController(model);
		model.setLocalCopy(model.getBusinessPlan());
		model.saveLocalPlanXML();
		model.notify(homePage.getScene());
		saveBPPopupBox.close();

	}

}