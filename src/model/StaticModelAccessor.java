package model;

public class StaticModelAccessor
{
	public static Model model;
	
	public static Model getModel()
	{
		return model;
	}
	
	public static void setModel(Model model)
	{
		StaticModelAccessor.model=model;
	}
}
