package com.imersaovisual.budgetcontrol.repository.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public class DatabaseOpenHelper extends SQLiteOpenHelper
{
	private final static String DATABASE_NAME = "Costs";
	
	private final static int DATABASE_VERSION = 1;
	
	private Set<DatabaseTable> tables = new HashSet<DatabaseTable>();
	
	private static DatabaseOpenHelper instance;
	
	public static DatabaseOpenHelper getInstance(Context context)
	{
		if(instance == null) instance = new DatabaseOpenHelper(context);
		
		return instance;
	}
	
	private DatabaseOpenHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		Log.i("MYDB", "ONCREATE");
		
		for (DatabaseTable table : tables)
		{
			Log.i("MYDB", "CREATING TABLE " + table.getName());
			db.execSQL(table.getCreateStatement());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		for (DatabaseTable table : tables)
		{
			db.execSQL(table.getUpdateStatement());
		}
	}
	
	public void registerTable(DatabaseTable table)
	{
		tables.add(table);
	}
}
