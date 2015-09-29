package jumb.myapplab.pascal.jumb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class EntryDataSource {

  // Database fields
  private SQLiteDatabase database;
  private MySQLiteHelper dbHelper;
  private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
      MySQLiteHelper.COLUMN_TITLE, MySQLiteHelper.COLUMN_CONTENT, MySQLiteHelper.COLUMN_DATE};

  public EntryDataSource(Context context) {
    dbHelper = new MySQLiteHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public DbEntry createEntry(String title,String content,String date) {
    ContentValues values = new ContentValues();
    values.put(MySQLiteHelper.COLUMN_TITLE, title);
    values.put(MySQLiteHelper.COLUMN_CONTENT, content);
    values.put(MySQLiteHelper.COLUMN_DATE, date);
    long insertId = database.insert(MySQLiteHelper.TABLE_POSTS, null,
        values);
    Cursor cursor = database.query(MySQLiteHelper.TABLE_POSTS,
        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    DbEntry newDbEntry = cursorToEntry(cursor);
    cursor.close();
    return newDbEntry;
  }

  public void deleteEntry(DbEntry id) {
    database.delete(MySQLiteHelper.TABLE_POSTS, MySQLiteHelper.COLUMN_ID
        + " = " + id, null);
  }

  public List<DbEntry> getAllEntries() {
    List<DbEntry> entries = new ArrayList<DbEntry>();

    Cursor cursor = database.query(MySQLiteHelper.TABLE_POSTS,
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
    	DbEntry entry = cursorToEntry(cursor);
      entries.add(entry);
      cursor.moveToNext();
    }
    // make sure to close the cursor
    cursor.close();
    return entries;
  }

  private DbEntry cursorToEntry(Cursor cursor) {
	DbEntry entry = new DbEntry();
    entry.setId(cursor.getLong(0));
    entry.setTitle(cursor.getString(1));
    entry.setContent(cursor.getString(2));
    entry.setDate(cursor.getString(3));
    return entry;
  }
}