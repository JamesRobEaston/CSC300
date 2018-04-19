package businessPlanClasses;

public class Category implements java.io.Serializable
{

	private static final long serialVersionUID = 5235697394687146369L;
	// minimum and maximum number of statements allowed for the category
	int min;
	int max;
	String name;
	// the rank indicates which level in the tree that the category falls under
	int rank;
	// statementCount is the number of statements that currently exist under this
	// category
	// this allows us to send error messages when we exceed the max # of statements
	int statementCount;

	public Category()
	{
		this.statementCount = 0;
	}

	public Category(int min, int max, String name, int rank)
	{
		super();
		this.min = min;
		this.max = max;
		this.name = name;
		this.rank = rank;
		this.statementCount = 0;
	}



	public int getMin()
	{
		return this.min;
	}

	public int getStatementCount()
	{
		return this.statementCount;
	}

	public int getMax()
	{
		return this.max;
	}

	public String getName()
	{
		return this.name;
	}

	public int getRank()
	{
		return this.rank;
	}

	public void setMin(int Min)
	{
		this.min = Min;
		return;
	}

	// increase the statementCount by 1
	public void incrementStatement()
	{
		this.statementCount++;
	}

	// decrease the statementCount by 1
	public void decrementStatement()
	{
		this.statementCount--;
	}

	public void setMax(int Max)
	{
		this.max = Max;
		return;
	}

	public void setName(String Name)
	{
		this.name = Name;
		return;
	}

	public void setRank(int Rank)
	{
		this.rank = Rank;
		return;
	}
	
	public void setStatementCount(int statementCount)
	{
		this.statementCount = statementCount;
	}
	
	public Category copy()
	{
		Category copy = new Category(min, max, name, rank);
		copy.setStatementCount(statementCount);
		
		return copy;
	}
}
