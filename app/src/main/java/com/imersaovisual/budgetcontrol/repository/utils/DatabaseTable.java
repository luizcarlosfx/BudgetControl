package com.imersaovisual.budgetcontrol.repository.utils;


public class DatabaseTable
{
	private String name;
	private String createStatement;
	private String updateStatement;
	
	public DatabaseTable(String name, String createStatement, String updateStatement)
	{
		super();
		this.name = name;
		this.createStatement = createStatement;
		this.updateStatement = updateStatement;
	}
	
	public DatabaseTable(String name, String createStatement)
	{
		this(name, createStatement, String.format("DROP TABLE IF EXISTS %s; %s" , name, createStatement));
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCreateStatement()
	{
		return createStatement;
	}

	public void setCreateStatement(String createStatement)
	{
		this.createStatement = createStatement;
	}

	public String getUpdateStatement()
	{
		return updateStatement;
	}

	public void setUpdateStatement(String updateStatement)
	{
		this.updateStatement = updateStatement;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DatabaseTable other = (DatabaseTable) obj;
		if (name == null)
		{
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
