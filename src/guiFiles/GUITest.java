package guiFiles;

import businessPlanClasses.*;
import clientServerPackage.*;
import guiFiles.PopupBoxes.*;
import guiFiles.screens.*;
import javafx.application.Application;

import java.rmi.RemoteException;
import java.rmi.registry.*;

class GUITest
{
	public static void main(String[] args)
	{	
		BPApplication application = new BPApplication();
		application.launch();
	}
}
