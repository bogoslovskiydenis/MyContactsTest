package com.example.mycontactstest;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        contactAdapter = new ContactAdapter(contactArrayList , MainActivity.this);
        recyclerView.setAdapter(contactAdapter);

        myContactsDatabase = Room.databaseBuilder(getApplicationContext(),
                MyContactsDatabase.class, "ContactsDb").build();

        loadContacts();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAndEditContact(false, null, -1);

            }
        });
    }

    public void addAndEditContact(boolean isUpdate, Contact contact, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.add_edit_contact, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);

        //создаем поля
        TextView contactTitleTextView = view.findViewById(R.id.contactTitleTextView);
        EditText firstNameEditTextView = view.findViewById(R.id.firstNameEditTextView);
        EditText lastNameEditTextView = view.findViewById(R.id.lastNameEditTextView);
        EditText emailEditTextView = view.findViewById(R.id.emailEditTextView);
        EditText phoneNumberEditTextView = view.findViewById(R.id.phoneNumberEditTextView);
        //устанавливаем диалоги
        contactTitleTextView.setText(!isUpdate ? "Add Contact" : "Edit contact");

        if (isUpdate && contact != null) {
            firstNameEditTextView.setText(contact.getFirstName());
            lastNameEditTextView.setText(contact.getLastName());
            emailEditTextView.setText(contact.getEmail());
            phoneNumberEditTextView.setText(contact.getPhoneNumber());
        }

        builder.setCancelable(false)
                .setPositiveButton(isUpdate ? "Update" : "Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (TextUtils.isEmpty(firstNameEditTextView.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter first name", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(lastNameEditTextView.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter last name", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(emailEditTextView.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter email ", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(phoneNumberEditTextView.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter phone number", Toast.LENGTH_LONG).show();
                } else {
                    if (isUpdate && contact != null) {
                        updateContact(
                                firstNameEditTextView.getText().toString(),
                                lastNameEditTextView.getText().toString(),
                                emailEditTextView.getText().toString(),
                                phoneNumberEditTextView.getText().toString()
                                , position);
                    } else {
                        addContact(
                                firstNameEditTextView.getText().toString(),
                                lastNameEditTextView.getText().toString(),
                                emailEditTextView.getText().toString(),
                                phoneNumberEditTextView.getText().toString()
                        );
                    }
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //loadContacts
    private void loadContacts() {
        new GetAllContactsAsyncTask().execute();
    }

    //DeleteContacts
    private void DeleteContacts(Contact contact) {
        new DeleteContactAsyncTask().execute(contact);
    }

    //AddContact
    private void addContact(String firstName, String lastName, String email, String phoneNumber) {
        Contact contact = new Contact(0, firstName, lastName, email, phoneNumber);

        new AddContactAsyncTask().execute(contact);
    }

    private void updateContact(String firstName, String lastName, String email, String phoneNumber, int position) {
        Contact contact = contactArrayList.get(position);
        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setEmail(email);
        contact.setPhoneNumber(phoneNumber);

        new UpdateContactAsyncTask().execute(contact);

        contactArrayList.set(position, contact);
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

    private class GetAllContactsAsyncTask extends AsyncTask<Void, Void, Void> {

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

    private class DeleteContactAsyncTask extends AsyncTask<Contact, Void, Void> {

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

    private class AddContactAsyncTask extends AsyncTask<Contact, Void, Void> {

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

    private class UpdateContactAsyncTask extends AsyncTask<Contact, Void, Void> {

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
