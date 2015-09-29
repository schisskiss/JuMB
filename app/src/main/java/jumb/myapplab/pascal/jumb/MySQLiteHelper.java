package jumb.myapplab.pascal.jumb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_POSTS = "posts";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_TITLE = "title";
  public static final String COLUMN_CONTENT = "content";
  public static final String COLUMN_DATE = "date";

  private static final String DATABASE_NAME = "entries.db";
  private static final int DATABASE_VERSION = 2;

  // Database creation sql statement
  private static final String DATABASE_CREATE = "create table "
      + TABLE_POSTS + "(" + COLUMN_ID
      + " integer primary key autoincrement, " + COLUMN_TITLE
      + " text not null, " + COLUMN_CONTENT
      + " text not null, " + COLUMN_DATE
      + " text not null);";

  public MySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  private static void create(SQLiteDatabase database){
		new chatSQLiteHelper(null).onCreate(database);
	}
  
  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }
  
  public static void cleanTable(SQLiteDatabase database){
	  database.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
	    create(database);
  }
  
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(MySQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
    onCreate(db);
  }

} 