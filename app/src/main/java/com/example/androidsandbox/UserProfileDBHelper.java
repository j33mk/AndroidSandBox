package com.example.androidsandbox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserProfileDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "mycovid19.db";
    public static final String TABLE_NAME = "user_profile_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "RegisteredAt";
    public static final String COL_3 = "PersonName";
    public static final String COL_4 = "PhoneNumber";
    public static final String COL_5 = "RegisteredAt";

    public UserProfileDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,UniqueIDAssigned TEXT,PersonName TEXT,PhoneNumber TEXT,RegisteredAt TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public boolean insertData(String uniqueId, String personName, String phoneNum, String registeredAt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, uniqueId);
        contentValues.put(COL_3, personName);
        contentValues.put(COL_4, phoneNum);
        contentValues.put(COL_5, registeredAt);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }

    public boolean updateData(String id, String uniqueId, String personName, String phoneNum, String registeredAt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, uniqueId);
        contentValues.put(COL_3, personName);
        contentValues.put(COL_4, phoneNum);
        contentValues.put(COL_5, registeredAt);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return true;
    }
}
