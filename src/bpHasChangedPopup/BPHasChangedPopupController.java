package bpHasChangedPopup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class BPHasChangedPopupController
{
	Stage stage;
	
	@FXML
	void closePopup(ActionEvent event)
	{
		stage.close();
	}

	public Stage getStage()
	{
		return stage;
	}

	public void setStage(Stage stage)
	{
		this.stage = stage;
	}
}
