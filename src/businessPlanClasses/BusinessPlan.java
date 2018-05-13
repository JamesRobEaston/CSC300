package businessPlanClasses;

import java.util.*;

import clientServerPackage.AdminClient;
import clientServerPackage.ClientProxyInterface;

import java.io.*;

public class BusinessPlan implements Serializable
{

	private static final long serialVersionUID = 5650597021644898758L;
	ArrayList<TreeBuilder> Histroy; // list of root nodes that contain copies of previous plans
	PlanDesign design;
	TreeBuilder tree;
	boolean isEditable = true;
	String year;

	public BusinessPlan(String year, ArrayList<Category> categories, String planName)
	{
		this.Histroy = new ArrayList<TreeBuilder>();
		this.tree = new TreeBuilder(categories, planName);
		this.design = new PlanDesign();
		this.year = year;
	}
	
	public BusinessPlan(String year, String planName)
	{
		this.Histroy = new ArrayList<TreeBuilder>();
		this.design = new PlanDesign();
		this.tree = new TreeBuilder(planName);
		this.year = year;
	}

	public BusinessPlan()
	{

	}

	// must be called before adding anything to the tree
	public void setCategoryList()
	{
		this.tree.setCategories(this.design.getCategoryList());
	}

	// this is how we save business plans to an array list
	public void addToHistory()
	{
		Histroy.add(tree);
	}

	public void setPlan(PlanDesign plan)
	{
		this.design = plan;
	}

	// this dynamically adds statements to our statement tree
	public Statement addStatement(String data, String categoryName, Statement parent)
	{
		int categoryMin;
		int categoryMax;
		int rank = 0;
		int parentRank = 0;
		Statement statement = new Statement();
		Category category = new Category();

		// this gets the min, max, and category object of the statement's category
		for (int i = 0; i < this.design.getCategoryList().size(); i++)
		{
			if (this.design.getCategoryList().get(i).name.equals(parent))
			{
				parentRank = this.design.getCategoryList().get(i).getRank();
			}
			if (this.design.getCategoryList().get(i).name.equals(categoryName))
			{
				categoryMin = this.design.getCategoryList().get(i).getMin();
				categoryMax = this.design.getCategoryList().get(i).getMax();
				category = this.design.getCategoryList().get(i);
				rank = this.design.getCategoryList().get(i).getRank();
			}
		}
		// number of statements in specified category increases by 1
		category.incrementStatement();
		// only lets you add the statement if the #statements in that category isn't
		// already full
		if ((category.max >= category.statementCount) && ((parentRank + 1 == rank)))
		{
			statement = this.tree.addStatement(data, categoryName, parent);
			return statement;
		} else
		{
			// if we don't add the statement, we have to decrement the count
			category.decrementStatement();
			System.out.println("Could not add statement");
		}
		return statement;
	}

	public void removeStatement(Statement statement)
	{

		Category category = statement.getType();
		// only allows us to remove a statement if the number of statements in that
		// category < min
		if (category.getStatementCount() > category.getMin())
		{
			this.tree.removeStatement(statement);
			category.decrementStatement();
		} else
		{
			System.out.println("Too few statements to remove one.");
		}

	}

	// changes the data in the given statement
	public void editStatement(Statement statement, int dataIndex, String data)
	{
		this.tree.editStatement(statement, dataIndex, data);
	}

	// adds category to PlanDesign object (PlanInterface object)
	public void addCategory(String name, int pos, int min, int max)
	{
		this.design.addCategory(name, pos, min, max);
	}

	public boolean isEditable()
	{
		return isEditable;
	}

	// Changes the editable status of the plan ONLY if the client calling it is an
	// AdminClient
	public void changeEditable(ClientProxyInterface client)
	{
		if (client instanceof AdminClient)
		{
			if (!isEditable)
			{
				isEditable = true;
			} else
			{
				isEditable = false;
			}
		} else
		{
			System.out.println("You do not have permission to change restrictions on this plan.");
		}
	}

	public String getYear()
	{
		return year;
	}

	public void setYear(String year)
	{
		this.year = year;
	}

	// Resets doubly-linked statements after being read from XML (JAXB does NOT like
	// doubly-linked objects)
	public void renewStatementRefs()
	{
		if (this.tree != null)
		{
			this.tree.getRoot().resetParentAfterRead();
		}
		for (int i = 0; i < Histroy.size(); i++)
		{
			TreeBuilder currTree = Histroy.get(i);
			currTree.getRoot().resetParentAfterRead();
		}
	}

	public ArrayList<TreeBuilder> getHistroy()
	{
		return Histroy;
	}

	public void setHistroy(ArrayList<TreeBuilder> histroy)
	{
		Histroy = histroy;
	}

	public PlanDesign getDesign()
	{
		return design;
	}

	public void setDesign(PlanDesign design)
	{
		this.design = design;
	}

	public TreeBuilder getTree()
	{
		return tree;
	}

	public void setTree(TreeBuilder tree)
	{
		this.tree = tree;
	}

	public void setEditable(boolean isEditable)
	{
		this.isEditable = isEditable;
	}
}
