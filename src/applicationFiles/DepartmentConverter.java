package applicationFiles;

import clientServerPackage.Department;
import javafx.util.StringConverter;

public class DepartmentConverter<T extends Department> extends StringConverter<T>
{
	
	@Override
	public T fromString(String name)
	{
		return (T) new Department(name);
	}

	@Override
	public String toString(T department)
	{
		return ((Department) department).getName();
	}
	
}

