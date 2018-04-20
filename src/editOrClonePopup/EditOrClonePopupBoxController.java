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

public class EditOrClonePopupBoxController {

	Stage popupStage;
	FXMLLoader loader;
	Scene editOrCloneScene;
	ClientProxy client;
	BPApplication application;
	
    @FXML
    private Button editButton;

    @FXML
    private Button cloneButton;

    @FXML
    private Button cancelButton;

    public EditOrClonePopupBoxController() {}
    
    public EditOrClonePopupBoxController(ClientProxy client, BPApplication application) throws IOException
	{
    	this.client = client;
    	this.application = application;
    	loader = new FXMLLoader();
		loader.setLocation(AddDepartmentPopupBoxController.class.getResource("AddDepartmentPopupBox.fxml"));
		editOrCloneScene = new Scene(loader.load());
		
		popupStage = new Stage();
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("Edit or Clone Business Plan");
		popupStage.setResizable(false);
		popupStage.setScene(editOrCloneScene);
    }
    
    @FXML
    void cloneBusinessPlan(ActionEvent event) throws IOException {
    	new NewBPPopupBoxController(client, application, true);
    	popupStage.close();
    }

    @FXML
    void closePopup(ActionEvent event) {
    	popupStage.close();
    }

    @FXML
    void editBusinessPlan(ActionEvent event) {
    	new BusinessPlanScreenController(client.getBusinessPlan(), client, application);
    	popupStage.close();

    }

}
