package jumb.myapplab.pascal.jumb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pascal on 31.12.2014.
 */
public class chatEntryDataSource {

    // Database fields
    private SQLiteDatabase chatdatabase;
    private chatSQLiteHelper chatdbHelper;
    private String[] allColumns = { chatSQLiteHelper.COLUMN_ID,
            chatSQLiteHelper.COLUMN_FROM, chatSQLiteHelper.COLUMN_MSG, chatSQLiteHelper.COLUMN_ISSELF, chatSQLiteHelper.COLUMN_PRIVATEMSG};

    public chatEntryDataSource(Context context) {
        chatdbHelper = new chatSQLiteHelper(context);
    }

    public void open() throws SQLException {
        chatdatabase = chatdbHelper.getWritableDatabase();
    }

    public void close() {
        chatdbHelper.close();
    }

    public chatDbEntry createChatEntry(String msgfrom,String message,String isself, String privatemsg) {
        ContentValues values = new ContentValues();
        values.put(chatSQLiteHelper.COLUMN_FROM, msgfrom);
        values.put(chatSQLiteHelper.COLUMN_MSG, message);
        values.put(chatSQLiteHelper.COLUMN_ISSELF, isself);
        values.put(chatSQLiteHelper.COLUMN_PRIVATEMSG, privatemsg);

        long insertId = chatdatabase.insert(chatSQLiteHelper.TABLE_CHAT, null, values);
        Cursor cursor = chatdatabase.query(chatSQLiteHelper.TABLE_CHAT,
                allColumns, chatSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        chatDbEntry newDbEntry = cursorToEntry(cursor);
        cursor.close();
        return newDbEntry;
    }

    public void deleteEntry(DbEntry id) {
        chatdatabase.delete(chatSQLiteHelper.TABLE_CHAT, chatSQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<chatDbEntry> getAllChatEntries() {
        List<chatDbEntry> entries = new ArrayList<chatDbEntry>();

        Cursor cursor = chatdatabase.query(chatSQLiteHelper.TABLE_CHAT,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            chatDbEntry entry = cursorToEntry(cursor);
            entries.add(entry);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return entries;
    }

    private chatDbEntry cursorToEntry(Cursor cursor) {
        chatDbEntry entry = new chatDbEntry();
        entry.setId(cursor.getLong(0));
        entry.setFrom(cursor.getString(1));
        entry.setMessage(cursor.getString(2));
        entry.setIsSelf(cursor.getString(3));
        entry.setPrivatemsg(cursor.getString(4));
        return entry;
    }
}
