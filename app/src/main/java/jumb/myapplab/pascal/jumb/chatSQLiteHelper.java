package jumb.myapplab.pascal.jumb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Pascal on 31.12.2014.
 */
public class chatSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_CHAT = "chatlist";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FROM = "msgfrom";
    public static final String COLUMN_MSG = "message";
    public static final String COLUMN_ISSELF = "isself";
    public static final String COLUMN_PRIVATEMSG = "privatemsg";


    public static final String DATABASE_NAME = "chat.db";
    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_CHAT + " ( " + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_FROM
            + " text not null, " + COLUMN_MSG
            + " text not null, " + COLUMN_ISSELF
            + " text not null, " + COLUMN_PRIVATEMSG
            + " text not null);";

    public chatSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static void create(SQLiteDatabase database){
        new chatSQLiteHelper(null).onCreate(database);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void cleanChatTable(SQLiteDatabase database){
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT);
        create(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT);
        onCreate(db);
    }
}
