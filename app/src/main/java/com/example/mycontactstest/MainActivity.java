package com.example.mycontactstest;

import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Insert;
import androidx.room.Room;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.nio.channels.AsynchronousChannelGroup;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MyContactsDatabase myContactsDatabase;
    private ArrayList<Contact> contactArrayList;
    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        contactAdapter = new ContactAdapter();
        recyclerView.setAdapter(contactAdapter);

        myContactsDatabase = Room.databaseBuilder(getApplicationContext(),
                MyContactsDatabase.class, "ContactsDb").build();

        loadContacts();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    //loadContacts
    private void loadContacts() {
        new GetAllContactsAsyncTask().execute();
    }
    //DeleteContacts
    private void DeleteContacts(Contact contact){
        new DeleteContactAsyncTask().execute(contact);
    }
    //AddContact
    private void AddContacts(Contact contact){
        new AddContactAsyncTask().execute(contact);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetAllContactsAsyncTask extends AsyncTask<Void, Void ,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            contactArrayList = (ArrayList<Contact>) myContactsDatabase
                    .getContactDao()
                    .getAllContacts();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            contactAdapter.setContactArrayList(contactArrayList);
        }
    }

    private class DeleteContactAsyncTask extends AsyncTask<Contact, Void, Void>{

        @Override
        protected Void doInBackground(Contact... contacts) {
            myContactsDatabase.getContactDao().deleteContact(contacts[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadContacts();
        }
    }
    private class AddContactAsyncTask extends AsyncTask<Contact, Void, Void>{

        @Override
        protected Void doInBackground(Contact... contacts) {
            myContactsDatabase.getContactDao().insertContact(contacts[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadContacts();
        }
    }
    private class UpdateContactAsyncTask extends AsyncTask<Contact, Void, Void>{

        @Override
        protected Void doInBackground(Contact... contacts) {
            myContactsDatabase.getContactDao().updateContact(contacts[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadContacts();
        }
    }
}
