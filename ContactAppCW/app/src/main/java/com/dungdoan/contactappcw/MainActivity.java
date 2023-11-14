package com.dungdoan.contactappcw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton addContactBtn;
    private RecyclerView contactRecyclerView;
    private DbHelper dbHelper;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();

        addContactBtn = findViewById(R.id.add_contact_btn);
        contactRecyclerView = findViewById(R.id.contact_recycler_view);

        dbHelper = new DbHelper(this);

        loadContacts();

        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ContactAddAndUpdate.class);
                intent.putExtra("IS_EDIT_MODE", false);
                startActivity(intent);
            }
        });
    }

    public void loadContacts() {
        ContactAdapter contactAdapter = new ContactAdapter(MainActivity.this, dbHelper.getAllContacts());
        contactRecyclerView.setAdapter(contactAdapter);

        actionBar.setSubtitle("Total contacts: " + dbHelper.getContactCount());
    }

    private void searchContact(String query) {
        ContactAdapter contactAdapter = new ContactAdapter(MainActivity.this, dbHelper.searchContact(query));
        contactRecyclerView.setAdapter(contactAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadContacts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchContact(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchContact(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.delete_action) {
            dbHelper.deleteAllContact();
            Toast.makeText(this, "Delete All Contacts Successfully!", Toast.LENGTH_SHORT).show();
            loadContacts();
        }

        return super.onOptionsItemSelected(item);
    }
}