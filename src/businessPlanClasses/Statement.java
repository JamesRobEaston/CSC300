package businessPlanClasses;

import java.util.*;

public class Statement implements java.io.Serializable
{

	private static final long serialVersionUID = 8922619098294069959L;
	Category type;
	Statement parent;
	ArrayList<Statement> children;
	ArrayList<String> data;
	ArrayList<String> comments;
	String id;

	public Statement(Category type, Statement parent, ArrayList<String> data, ArrayList<String> comments, String id)
	{
		this.type = type;
		this.parent = parent;
		this.children = new ArrayList<Statement>();
		if(data != null)
		{
			this.data = data;
		}
		else
		{
			this.data = new ArrayList<String>();
		}
		if(comments != null)
		{
			this.comments = comments;
		}
		else
		{
			this.comments = new ArrayList<String>();
		}
		
		this.id = id;
	}

	public Statement()
	{
	}

	public Category getType()
	{
		return this.type;
	}

	public Statement getParent()
	{
		return this.parent;
	}

	public ArrayList<String> getData()
	{
		return this.data;
	}

	public void setType(Category cat)
	{
		this.type = cat;
		return;
	}

	public void setParent(Statement par)
	{
		this.parent = par;
		return;
	}

	public void setData(ArrayList<String> dat)
	{
		this.data = dat;
		return;
	}

	public ArrayList<Statement> getChildren()
	{
		return children;
	}

	public void setChildren(ArrayList<Statement> children)
	{
		this.children = children;
	}

	// adds statement to current statement's children list
	public void addChild(Statement chi)
	{
		this.children.add(chi);
		return;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	// sets the current statement's data members to null
	// also sets all other statements below the current statement to null
	public void terminate()
	{
		Queue<Statement> statementQueue = new LinkedList<Statement>();
		statementQueue.add(this);
		while (statementQueue.size() > 0)
		{
			Statement curr = statementQueue.remove();
			for (Statement child : curr.children)
			{
				statementQueue.add(child);
			}
			curr.children = null;
			curr.parent = null;
			curr.data = null;
		}

	}

	public void resetParentAfterRead()
	{
		for (int i = 0; i < children.size(); i++)
		{
			children.get(i).setParent(this);
		}
	}
	
	public void addComment(String comment)
	{
		comments.add(comment);
	}
	
	public void removeComment(int index)
	{
		if (index < comments.size())
		{
			comments.remove(index);
		}
	}

	public void addData(String statement)
	{
		data.add(statement);
	}

	public void removeData(int index)
	{
		if (index < data.size())
		{
			data.remove(index);
		}
	}

	public void editData(int index, String statement)
	{
		if (index < data.size())
		{
			data.set(index, statement);
		}
	}
	
	public Statement copy()
	{
		Category typeCopy = type.copy();
		Statement parentCopy = null;
		ArrayList<Statement> childrenCopy = new ArrayList<Statement>();
		for(Statement child: children)
		{
			Statement childCopy = child.copy();
			childCopy.setParent(this);
			childrenCopy.add(childCopy);
		}
		
		ArrayList<String> dataCopy = new ArrayList<String>();
		for(int i = 0; i < data.size(); i++)
		{
			dataCopy.add(data.get(i));
		}
		
		ArrayList<String> commentsCopy = new ArrayList<String>();
		for(int i = 0; i < comments.size(); i++)
		{
			commentsCopy.add(comments.get(i));
		}
		
		Statement copy = new Statement(typeCopy, parentCopy, dataCopy, commentsCopy, id);
		copy.setChildren(childrenCopy);
		
		return copy;
	}

	public ArrayList<String> getComments()
	{
		return comments;
	}

	public void setComments(ArrayList<String> comments)
	{
		this.comments = comments;
	}
	
	

}
