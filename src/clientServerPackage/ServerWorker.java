package clientServerPackage;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.TimeUnit;

//This class is just meant to be used to start the ConcreteServer whenever it has been saved to a file.
public class ServerWorker
{
	
	//A thread to save the server every two minutes
	class SaveThread extends Thread
	{
		
		public SaveThread()
		{
			
		}
	
		public void run()
		{
			while(true)
			{
				try
				{
					TimeUnit.MINUTES.sleep(2);
					saveAll();
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	private final String SERIALIZED_FILE_NAME;
	ConcreteServer server;

	public ServerWorker()
	{
		SERIALIZED_FILE_NAME = "Server.xml";
	}

	public ServerWorker(String fileName)
	{
		SERIALIZED_FILE_NAME = fileName;
	}

	//Deserialize ConcreteServer.xml, which holds data on the ConcreteServer
	//If ConcreteServer.xml is not found, then a new ConcreteServer is returned.
	public ConcreteServer deserialize(String adminUsername, String adminPassword)
	{
		XMLDecoder decoder = null;
		try
		{
			decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(SERIALIZED_FILE_NAME)));
		} catch (FileNotFoundException e)
		{
			System.out.println("ERROR: File " + SERIALIZED_FILE_NAME + " not found");
		}

		//Create the ConcreteServer from ConcreteServer.xml
		try 
		{
			server = (ConcreteServer) decoder.readObject();
		} 
		//Or instantiate a new ConcreteServer
		catch(Exception e)
		{
			server = new ConcreteServer(adminUsername, adminPassword);	
		}
		
		if(decoder != null)
		{
			decoder.close();
		}
		
		return server;
	}
	
	//Start the registry, adding a given ConcreteServer
	public void startRegistry(ConcreteServer concreteServer)
	{
		try
		{
			ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(concreteServer, 0);
			
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.bind("Server", stub);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//Called by an independent thread, which is created when the Server is constructed.
	//Writes this Server object to Server.xml.
	public void saveAll()
	{
		// Serialize Server and save to file.
		XMLEncoder encoder = null;
		try
		{
			encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("Server.xml")));
			encoder.writeObject(server);
			encoder.close();
		}
		catch(FileNotFoundException fileNotFound) {}
		
	}

	public static void main(String[] args)
	{
		//The initial admin's username and password.
		String initAdminUsername = "admin";
		String initAdminPassword = "admin";
		
		//Start up the ConcreteServer.
		ServerWorker serverStarter = new ServerWorker();
		ConcreteServer concreteServer = serverStarter.deserialize(initAdminUsername, initAdminPassword);
		serverStarter.startRegistry(concreteServer);
		
		SaveThread saver = serverStarter.new SaveThread();
		saver.start();
	}

}


