package businessPlanClasses;

import java.io.Serializable;
import java.util.*;

public class PlanDesign implements Serializable
{

	private static final long serialVersionUID = -741167926963449578L;
	ArrayList<Category> categoryList;

	public PlanDesign()
	{
		this.categoryList = new ArrayList<Category>();
	}

	// set category name, rank/position, min required, and max allowed
	public void addCategory(String name, int pos, int min, int max)
	{
		Category c = new Category();
		c.setName(name);
		c.setMin(min);
		c.setMax(max);
		c.setRank(pos);
		this.categoryList.add(c);
	}

	// orders categories from top ranking (lowest actual integer) to lowest ranking
	// (biggest integer)
	public void orderCategories()
	{
		int minIndex;
		for (int i = 0; i < this.categoryList.size(); i++)
		{
			minIndex = i;
			for (int j = i + 1; j < this.categoryList.size(); j++)
			{
				if (this.categoryList.get(j).rank < this.categoryList.get(minIndex).rank)
				{
					minIndex = j;
				}
			}
			this.swap(i, minIndex);
		}
	}

	// helper function for the sort algorithm in orderCategories()
	public void swap(int j, int min)
	{
		Category temp = this.categoryList.get(j);
		this.categoryList.set(j, this.categoryList.get(min));
		this.categoryList.set(min, temp);
	}

	public ArrayList<Category> getCategoryList()
	{
		return categoryList;
	}

	public void setCategoryList(ArrayList<Category> categoryList)
	{
		this.categoryList = categoryList;
	}

	public PlanDesign copy()
	{
		ArrayList<Category> categoryListCopy = new ArrayList<Category>();
		for(Category category : categoryList)
		{
			categoryListCopy.add(category.copy());
		}
		
		PlanDesign copy = new PlanDesign();
		copy.setCategoryList(categoryListCopy);
		return copy;
	}

}