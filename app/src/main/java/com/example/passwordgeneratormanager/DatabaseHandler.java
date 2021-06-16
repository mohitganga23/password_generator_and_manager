package com.example.passwordgeneratormanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "PasswordManager";
    private static final String TABLE_PASSWORDS = "passwordTable";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_PASSWORD = "password";


    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PASSWORDS_TABLE = "CREATE TABLE " + TABLE_PASSWORDS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_PASSWORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSWORDS);
        onCreate(db);
    }

    void addPassword(Password password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, password.getTitle());
        values.put(KEY_PASSWORD, password.getPassword());

        db.insert(TABLE_PASSWORDS, null, values);
        db.close();
    }

    public List<Password> getAllPasswords() {
        List<Password> passwordList = new ArrayList<Password>();
        String select_query = "SELECT * FROM " + TABLE_PASSWORDS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_query, null);

        if (cursor.moveToFirst()) {
            do {
                Password password = new Password();
                password.setId(Integer.parseInt(cursor.getString(0)));
                password.setTitle(cursor.getString(1));
                password.setPassword(cursor.getString(2));
                passwordList.add(password);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return passwordList;
    }

    public int updatePassword(Password password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, password.getTitle());
        values.put(KEY_PASSWORD, password.getPassword());

        return db.update(TABLE_PASSWORDS, values, KEY_ID + " = ? ",
                new String[]{String.valueOf(password.getId())});
    }

    public void deletePassword(Password password) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_PASSWORDS, KEY_ID + " = ? ",
                new String[]{String.valueOf(password.getId())});

        db.close();
    }

    public int getCount() {
        String count_query = "SELECT * FROM " + TABLE_PASSWORDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(count_query, null);
        cursor.close();

        return  cursor.getCount();
    }
}
