package clientServerPackage;

import businessPlanClasses.*;
import java.io.*;
import java.util.ArrayList;

//An extension for BusinessPlans, to give them an ID
public class BP extends BusinessPlan
{
	private static final long serialVersionUID = 5650842134649347758L;
	String ID;
	String name;
	Department department;
	
	public BP()
	{
		
	}

	public BP(String year, String name)
	{
		super(year, name);
		this.name = name;
		ID = name + " " + year;
	}

	public String getID()
	{
		return ID;
	}

	public void setID(String iD)
	{
		ID = iD;
	}
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public BP copy()
	{
		BP copy = new BP();
		
		copy.setTree(getTree().copy());
		copy.setDesign(getDesign().copy());
		
		ArrayList<TreeBuilder> histroyCopy = new ArrayList<TreeBuilder>();
		for(TreeBuilder histroy : getHistroy())
		{
			histroyCopy.add(histroy.copy());
		}
		copy.setHistroy(histroyCopy);
		
		copy.setEditable(isEditable());
		copy.setID(getID());
		
		return copy;
	}

	public Department getDepartment()
	{
		return department;
	}

	public void setDepartment(Department department)
	{
		this.department = department;
	}
	
}
