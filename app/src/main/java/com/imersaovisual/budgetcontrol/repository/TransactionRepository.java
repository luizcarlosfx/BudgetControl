package com.imersaovisual.budgetcontrol.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.imersaovisual.budgetcontrol.model.Transaction;
import com.imersaovisual.budgetcontrol.repository.utils.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionRepository extends SQLiteRepository<Transaction>
{
	public static final String TABLE_NAME = "account_transaction";

	public static final String ID = "id";

    public static final String DESCRIPTION = "description";

    public static final String DATE = "date";

    public static final String VALUE = "value";


	private static final String[] COLUMNS =
	{ ID, VALUE, DATE, DESCRIPTION };

	private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
			+ " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + VALUE + " REAL, " + DATE
			+ " INTEGER, " + DESCRIPTION + " TEXT " + ")";

	public TransactionRepository(Context context)
	{
		super(context);
	}

	@Override
	public Transaction save(Transaction transaction)
	{
		openDB();

		ContentValues values = new ContentValues();

		if (transaction.getId() != null)
		{
			values.put(ID, transaction.getId());
		}

		values.put(VALUE, transaction.getValue());

        values.put(DESCRIPTION, transaction.getDescription());

		values.put(DATE, transaction.getDate().getTime());

		SQLiteDatabase database = getDatabase();

		int id = (int) database.insertWithOnConflict(TABLE_NAME, null, values,
				SQLiteDatabase.CONFLICT_REPLACE);

		transaction.setId(id);

		closeDB();

		return transaction;
	}

	@Override
	public int delete(int id)
	{
		return delete(ID + "=?", id + "");
	}

	@Override
	public int delete(String where, String... args)
	{
		openDB();

		int rows = database.delete(TABLE_NAME, where, args);

		closeDB();

		return rows;
	}

	@Override
	public DatabaseTable getDatabaseModel()
	{
		DatabaseTable table = new DatabaseTable(TABLE_NAME, CREATE_TABLE);

		return table;
	}

	@Override
	public List<Transaction> findAll()
	{
		return find(null, null, null, null, null, null);
	}

	@Override
	public List<Transaction> find(String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, String limit)
	{
        openDB();

        Cursor cursor = database.query(TABLE_NAME, COLUMNS, selection, selectionArgs, groupBy,
                having, orderBy, limit);

        return readTransactions(cursor);
	}

	private List<Transaction> readTransactions(Cursor cursor)
	{
		List<Transaction> transactions = new ArrayList<Transaction>();

		while (cursor.moveToNext())
		{
			int id = cursor.getInt(cursor.getColumnIndex(ID));

			double value = cursor.getDouble(cursor.getColumnIndex(VALUE));

			long dateLong = cursor.getLong(cursor.getColumnIndex(DATE));

			Date date = new Date(dateLong);

			String description = cursor.getString(cursor
					.getColumnIndex(DESCRIPTION));

			Transaction transaction = new Transaction(id, description,
					date,  value);

			transactions.add(transaction);
		}

		return transactions;
	}
}
