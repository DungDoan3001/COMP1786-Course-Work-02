package com.dungdoan.contactappcw;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {
    private SQLiteDatabase database;

    public DbHelper(@Nullable Context context) {
        super(context, DatabaseConstants.DB_NAME, null, DatabaseConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ContactConstants.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int newVersion) {
        sqLiteDatabase.execSQL(ContactConstants.DROP_TABLE_QUERY);
        Log.w(this.getClass().getName(), ContactConstants.TABLE_NAME + " database upgrade to version " + newVersion + " - old data lost");

        onCreate(sqLiteDatabase);
    }

    public long addContact(String name, String phoneNumber, String address, String email, String image) {
        database = getWritableDatabase();
        ContentValues values =new ContentValues();

        values.put(ContactConstants.CONTACT_NAME, name);
        values.put(ContactConstants.CONTACT_PHONE_NUMBER, phoneNumber);
        values.put(ContactConstants.CONTACT_ADDRESS, address);
        values.put(ContactConstants.CONTACT_EMAIL, email);
        values.put(ContactConstants.CONTACT_IMAGE, image);

        long result = database.insertOrThrow(ContactConstants.TABLE_NAME, null, values);
        database.close();

        return result;
    }

    public void updateContact(String id, String name, String phoneNumber, String address, String email, String image) {
        database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ContactConstants.CONTACT_NAME, name);
        values.put(ContactConstants.CONTACT_PHONE_NUMBER, phoneNumber);
        values.put(ContactConstants.CONTACT_ADDRESS, address);
        values.put(ContactConstants.CONTACT_EMAIL, email);
        values.put(ContactConstants.CONTACT_IMAGE, image);

        database.update(ContactConstants.TABLE_NAME, values, ContactConstants.CONTACT_ID + " = ?", new String[]{id});
        database.close();
    }

    public ArrayList<ContactModel> getAllContacts() {
        database = getReadableDatabase();

        ArrayList<ContactModel> contactModelArrayList = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + ContactConstants.TABLE_NAME, null);

        if(cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int idColumnIndex = cursor.getColumnIndex(ContactConstants.CONTACT_ID);
                @SuppressLint("Range") int nameColumnIndex = cursor.getColumnIndex(ContactConstants.CONTACT_NAME);
                @SuppressLint("Range") int phoneNumberColumnIndex = cursor.getColumnIndex(ContactConstants.CONTACT_PHONE_NUMBER);
                @SuppressLint("Range") int addressColumnIndex = cursor.getColumnIndex(ContactConstants.CONTACT_ADDRESS);
                @SuppressLint("Range") int emailColumnIndex = cursor.getColumnIndex(ContactConstants.CONTACT_EMAIL);
                @SuppressLint("Range") int imageColumnIndex = cursor.getColumnIndex(ContactConstants.CONTACT_IMAGE);

                ContactModel contactModel = new ContactModel(
                        String.valueOf(cursor.getInt(idColumnIndex)),
                        cursor.getString(nameColumnIndex),
                        cursor.getString(phoneNumberColumnIndex),
                        cursor.getString(addressColumnIndex),
                        cursor.getString(emailColumnIndex),
                        cursor.getString(imageColumnIndex)
                );

                contactModelArrayList.add(contactModel);
            } while (cursor.moveToNext());
        }

        database.close();
        return contactModelArrayList;
    }

    public ArrayList<ContactModel> searchContact(String query) {
        database = getReadableDatabase();
        ArrayList<ContactModel> contactModelArrayList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + ContactConstants.TABLE_NAME + " WHERE " +
                            ContactConstants.CONTACT_NAME + " LIKE '%" + query + "%' OR " +
                            ContactConstants.CONTACT_PHONE_NUMBER + " LIKE '%" + query + "%' OR " +
                            ContactConstants.CONTACT_ADDRESS + " LIKE '%" + query + "%' OR " +
                            ContactConstants.CONTACT_EMAIL + " LIKE '%" + query + "%'";

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int idColumnIndex = cursor.getColumnIndex(ContactConstants.CONTACT_ID);
                @SuppressLint("Range") int nameColumnIndex = cursor.getColumnIndex(ContactConstants.CONTACT_NAME);
                @SuppressLint("Range") int phoneNumberColumnIndex = cursor.getColumnIndex(ContactConstants.CONTACT_PHONE_NUMBER);
                @SuppressLint("Range") int addressColumnIndex = cursor.getColumnIndex(ContactConstants.CONTACT_ADDRESS);
                @SuppressLint("Range") int emailColumnIndex = cursor.getColumnIndex(ContactConstants.CONTACT_EMAIL);
                @SuppressLint("Range") int imageColumnIndex = cursor.getColumnIndex(ContactConstants.CONTACT_IMAGE);

                ContactModel contactModel = new ContactModel(
                        String.valueOf(cursor.getInt(idColumnIndex)),
                        cursor.getString(nameColumnIndex),
                        cursor.getString(phoneNumberColumnIndex),
                        cursor.getString(addressColumnIndex),
                        cursor.getString(emailColumnIndex),
                        cursor.getString(imageColumnIndex)
                );

                contactModelArrayList.add(contactModel);
            } while (cursor.moveToNext());
        }
        database.close();
        return contactModelArrayList;
    }

    public int getContactCount() {
        database = getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + ContactConstants.TABLE_NAME, null);

        int count = cursor.getCount();

        cursor.close();

        return count;
    }

    public void deleteContact(String id) {
        database = getReadableDatabase();
        database.delete(ContactConstants.TABLE_NAME, ContactConstants.CONTACT_ID + " = ?", new String[]{id});
        database.close();
    }

    public void deleteAllContact() {
        database = getWritableDatabase();
        database.execSQL("DELETE FROM " + ContactConstants.TABLE_NAME);
        database.close();
    }
    public void deleteAllTables() {
        Cursor cursor = database.rawQuery("SELECT * FROM sqlite_master WHERE type='table'", null);

        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String tableName = cursor.getString(0);

                if(!tableName.equals("android_metadata") && !tableName.equals("sqlite_sequence")) {
                    database.execSQL("DROP TABLE IF EXISTS " + tableName);
                }

                cursor.moveToNext();
            }
        }
        cursor.close();
    }

}
