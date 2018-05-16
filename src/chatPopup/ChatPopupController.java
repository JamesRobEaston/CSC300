package chatPopup;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.Model;

public class ChatPopupController
{

	@FXML
	public VBox messagePane;

	@FXML
	public TextField messageInput;

	@FXML
	public Button sendButton;
	
	Model model;

	@FXML
	public void sendMessage(ActionEvent event)
	{
		model.sendMessage(messageInput.getText());
		messageInput.setText("");
	}
	
	public void receiveText(String sender, String message)
	{
		if(Platform.isFxApplicationThread())
		{

			Label messageLabel = new Label(sender + ": " + message);
			messagePane.getChildren().add(messageLabel);
		}
		else
		{
			Platform.runLater(() ->
			{

				Label messageLabel = new Label(sender + ": " + message);
				messagePane.getChildren().add(messageLabel);
			});
		}
	}

	public Model getModel()
	{
		return model;
	}

	public void setModel(Model model)
	{
		this.model = model;
	}
}