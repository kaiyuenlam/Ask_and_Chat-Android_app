package com.example.askchat.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.askchat.fragment.chatfunc.MessageModel;

import java.util.ArrayList;
import java.util.List;

public class FavorPostDatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "FavorPostDatabase";
    private static final String TABLE_NAME = "PostTable";
    //columns
    private static final String KEY_ID = "id";
    private static final String PostID = "Message";

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PostID + " TEXT)";

    public FavorPostDatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertPost(String postID) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PostID, postID);
        database.insert(TABLE_NAME, null, contentValues);
        database.close();
    }

    public List<String> getAllPost() {
        List<String> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndex(PostID)));
            } while (cursor.moveToNext());
        }
        database.close();
        cursor.close();
        return list;
    }

    public void deletePost(String postID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, PostID + "= ?", new String[] {postID});
    }


}
