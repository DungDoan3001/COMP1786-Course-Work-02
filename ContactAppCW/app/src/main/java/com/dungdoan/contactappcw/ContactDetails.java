package com.dungdoan.contactappcw;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactDetails extends AppCompatActivity {
    private ImageView imageView;
    private TextView contactName, contactPhoneNumber, contactAddress, contactEmail;
    private ActionBar actionBar;
    private String contactId;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Contact details");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        contactId = intent.getStringExtra("CONTACT_ID");

        dbHelper = new DbHelper(this);

        imageView = findViewById(R.id.contact_image);
        contactName = findViewById(R.id.contact_name);
        contactPhoneNumber = findViewById(R.id.contact_phone_number);
        contactAddress = findViewById(R.id.contact_address);
        contactEmail = findViewById(R.id.contact_email);

        showContactDetails();
    }

    private void showContactDetails() {
        String selectQuery = String.format("SELECT * FROM %s WHERE %s = \"%s\"", ContactConstants.TABLE_NAME, ContactConstants.CONTACT_ID, contactId);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int idColumnIndex = cursor.getColumnIndex(ContactConstants.CONTACT_ID);
                @SuppressLint("Range") int nameColumnIndex = cursor.getColumnIndex(ContactConstants.CONTACT_NAME);
                @SuppressLint("Range") int phoneNumberColumnIndex = cursor.getColumnIndex(ContactConstants.CONTACT_PHONE_NUMBER);
                @SuppressLint("Range") int addressColumnIndex = cursor.getColumnIndex(ContactConstants.CONTACT_ADDRESS);
                @SuppressLint("Range") int emailColumnIndex = cursor.getColumnIndex(ContactConstants.CONTACT_EMAIL);
                @SuppressLint("Range") int imageColumnIndex = cursor.getColumnIndex(ContactConstants.CONTACT_IMAGE);

                String id = String.valueOf(cursor.getInt(idColumnIndex));
                String name = cursor.getString(nameColumnIndex);
                String phoneNumber = cursor.getString(phoneNumberColumnIndex);
                String address = cursor.getString(addressColumnIndex);
                String email = cursor.getString(emailColumnIndex);
                String image = cursor.getString(imageColumnIndex);

                contactName.setText(name);
                contactPhoneNumber.setText(phoneNumber);
                contactAddress.setText(address);
                contactEmail.setText(email);

                if(image.equals("null")) {
                    this.imageView.setImageResource(R.drawable.person_image_vector);
                } else {
                    this.imageView.setImageURI(Uri.parse(image));
                }
            } while (cursor.moveToNext());
        }
        database.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return super.onSupportNavigateUp();
    }
}