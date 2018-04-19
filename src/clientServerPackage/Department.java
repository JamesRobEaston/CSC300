package clientServerPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Department implements Serializable
{
	String departmentName;
	ArrayList<BP> plans;
	private static final long serialVersionUID = 6378901024731341769L;
	
	//departmentName == The name for this Department.
	public Department(String departmentName)
	{
		this.departmentName = departmentName;
		plans = new ArrayList<BP>();
	}
	
	public Department() { }

	//Finds and returns the BP with id == bpid.
	//If no BP is found to have an id that matches bpid, then null is returned.
	BP retrieve(String bpid)
	{
		BP plan = null;
		//For each BP in the list of plans
		for(int i =0; i < plans.size(); i++)
		{
			BP currPlan = plans.get(i);
			//Return the plan if the plan's id == bpid
			if((currPlan.getID()).equals(bpid))
			{
				plan = currPlan.copy();
				return plan;
			}
		}
		
		return plan;
	}
	
	//Searches through plans to find a BP with an id equivalent to bp's id attribute.
	//If no matching BP was found, then bp is added to the list of BP.
	void save(BP bp, boolean isAdmin)
	{
		if(bp != null)
		{
			int index = 0;
			BP currPlan = null;
			String neededId = bp.getID();
			
			//Look for a BP with a matching id
			boolean foundMatchingBP = false;
			//While a BP with id matching the given BP hasn't been found
			while( (!foundMatchingBP) && index < plans.size())
			{
				currPlan = plans.get(index);
				String currPlanId = currPlan.getID();
				//If the current id matches the given id, we found the plan
				if(neededId.equals(currPlanId))
				{
					foundMatchingBP = true;
				}
				//Otherwise, continue checking other plans
				else
				{
					index++;
				}
			}
			
			//If a matching BP was found, replace the old one. If not, add the new one to the list.
			if(foundMatchingBP)
			{
				//If the user is an admin or the BP is editable, then we want to save the BP.
				if(isAdmin || plans.get(index).isEditable())
				{
					plans.set(index, bp);
				}
			}
			//Otherwise, add the new BP to the list of plans
			else
			{
				plans.add(bp);
			}
		}
	}
	
	//Iterates through every BP in plans and saves their id, name, and year into a String[][], which is then returned.
	//The data is saved in the order specified above (i.e. String[0][1] is the name in plans.get(0)).
	//Here is a cheat sheet to reference when dealing with the String[][] that is returned:
	//
	//String[0][0] == id 
	//String[0][1] == name
	//String[0][2] == year
	
	public String[][] view()
	{
		String[][] data = new String[plans.size()][3];
		for(int index = 0; index < plans.size(); index++)
		{
			BP currPlan = plans.get(index);
			
			data[index][0] = currPlan.getID();
			data[index][1] = currPlan.getName();
			data[index][2] = currPlan.getYear();
		}
		
		return data;
	}
	
	void delete(BP bp)
	{
		Iterator<BP> iterator = plans.iterator();
		boolean foundBP = false;
		BP businessPlan = null;
		
		while(!foundBP && iterator.hasNext())
		{
			businessPlan = iterator.next();
			if(businessPlan.getID().equals(bp.getID()))
			{
				foundBP = true;
			}
		}
		
		if(foundBP)
		{
			plans.remove(businessPlan);
		}
	}
	
	//Get a Business Plan via an index.
	BP getBusinessPlan(int index)
	{
		return plans.get(index);
	}

	//Basic getters and setters.
	public String getDepartmentName()
	{
		return departmentName;
	}

	public void setDepartmentName(String departmentName)
	{
		this.departmentName = departmentName;
	}

	public ArrayList<BP> getPlans()
	{
		return plans;
	}
	
	public void setPlans(ArrayList<BP> plans)
	{
		this.plans = plans;
	}
	
	public String getName()
	{
		return departmentName;
	}

}
