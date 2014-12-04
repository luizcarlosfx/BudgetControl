package com.imersaovisual.budgetcontrol.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.imersaovisual.budgetcontrol.repository.utils.DatabaseOpenHelper;
import com.imersaovisual.budgetcontrol.repository.utils.DatabaseTable;

import java.util.List;

public abstract class SQLiteRepository<TEntity>
{
	private DatabaseOpenHelper dbHelper;

	protected SQLiteDatabase database;

	public SQLiteRepository(Context context)
	{
		dbHelper = DatabaseOpenHelper.getInstance(context);
		dbHelper.registerTable(getDatabaseModel());
	}

	protected SQLiteDatabase getDatabase()
	{
		return this.database;
	}

	public void openDB()
	{
		database = dbHelper.getWritableDatabase();
	}

	public void closeDB()
	{
		dbHelper.close();
	}

	public abstract DatabaseTable getDatabaseModel();

	public abstract TEntity save(TEntity value);

	public abstract List<TEntity> find(String selection,
			String selectionArgs[], String groupBy, String having,
			String orderBy, String limit);

	public abstract List<TEntity> findAll();

	public abstract int delete(int id);

	public abstract int delete(String where, String... args);
}
