package applicationFiles;

import businessPlanClasses.*;
import clientServerPackage.*;
import javafx.application.Application;

import java.rmi.RemoteException;
import java.rmi.registry.*;

class GUITest
{
	public static void main(String[] args)
	{	
		BPApplication app = new BPApplication();
		app.launch();
	}
}
