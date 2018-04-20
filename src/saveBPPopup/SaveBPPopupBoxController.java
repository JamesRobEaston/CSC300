package saveBPPopup;

import clientServerPackage.BP;
import clientServerPackage.ClientProxy;
import guiFiles.screens.HomeScreen;

import java.io.IOException;

import applicationFiles.BPApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import viewAllBPView.ViewAllBPScreenController;

public class SaveBPPopupBoxController
{
	
	ClientProxy client;
	BPApplication application;
	
	public static Stage saveBPPopupBox;
	public static BP businessPlan;
	
	public SaveBPPopupBoxController(ClientProxy client, BPApplication application)
	{
		this.client = client;
		this.application = application;

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
		application.notify(HomeScreen.homeScreen);
		saveBPPopupBox.close();
	}

	@FXML
	void save(ActionEvent event)
	{
		client.setBusinessPlan(businessPlan);
		client.saveLocalPlanXML();
		application.notify(HomeScreen.homeScreen);
		saveBPPopupBox.close();

	}

}