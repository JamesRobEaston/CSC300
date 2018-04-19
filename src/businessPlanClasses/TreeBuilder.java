package businessPlanClasses;

import java.io.Serializable;

import java.util.*;

public class TreeBuilder implements Serializable
{
	private static final long serialVersionUID = 3071338868387233771L;
	ArrayList<Category> categories;
	// the root is how Organization accesses the tree
	Statement root;

	public TreeBuilder(ArrayList<Category> categories, String id)
	{
		this.categories = categories;
		this.root = new Statement(categories.get(0), null, new ArrayList<String>(), id);
	}
	
	public TreeBuilder(String id)
	{
		this.root = new Statement(new Category(0, 0, id, 0), null, new ArrayList<String>(), id);
	}
	
	public TreeBuilder() {}

	public Statement addStatement(ArrayList<String> data, String categoryName, Statement parent)
	{
		Statement statement = new Statement();
		// set statement's category
		for (Category c : this.categories)
		{
			if (categoryName.equals(c.name))
			{
				statement.setType(c);
			}
		}
		// set statement's data
		statement.setData(data);
		if (parent != null)
		{
			statement.setParent(parent);
			parent.addChild(statement);
		}

		return statement;
	}
	
	public Statement addStatement(String data, String categoryName, Statement parent)
	{
		Statement statement = new Statement();
		// set statement's category
		for (Category c : this.categories)
		{
			if (categoryName.equals(c.name))
			{
				statement.setType(c);
			}
		}
		// set statement's data
		statement.addData(data);
		if (parent != null)
		{
			statement.setParent(parent);
			parent.addChild(statement);
		}

		return statement;
	}

	// calls terminate() function in Statement class
	public void removeStatement(Statement statement)
	{
		statement.terminate();
	}

	public void editStatement(Statement statement, int index, String data)
	{
		statement.editData(index, data);
	}

	public ArrayList<Category> getCategories()
	{
		return categories;
	}
	
	public TreeBuilder copy()
	{
		ArrayList<Category> categoriesCopy = new  ArrayList<Category>();
		for(Category category : categories)
		{
			categoriesCopy.add(category.copy());
		}
		
		TreeBuilder copy = new TreeBuilder(categoriesCopy, null);
		copy.setRoot(root.copy());
		
		return copy;
	}

	public void setCategories(ArrayList<Category> categories)
	{
		this.categories = categories;
	}

	public Statement getRoot()
	{
		return root;
	}

	public void setRoot(Statement root)
	{
		this.root = root;
	}

}
