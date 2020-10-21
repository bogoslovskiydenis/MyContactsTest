package com.example.mycontactstest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>{

    private ArrayList<Contact> contactArrayList;

    public void setContactArrayList(ArrayList<Contact> contactArrayList) {
        this.contactArrayList = contactArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item,parent, false);

        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactArrayList.get(position);

        holder.firstName.setText(contact.getFirstName());
        holder.lastName.setText(contact.getLastName());
        holder.email.setText(contact.getEmail());
        holder.phoneNumber.setText(contact.getPhoneNumber());

    }

    @Override
    public int getItemCount() {
        return contactArrayList.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder{

        private TextView firstName;
        private TextView lastName;
        private  TextView email;
        private TextView phoneNumber;

        public ContactViewHolder(@NonNull View itemView) {

            super(itemView);

            firstName = itemView.findViewById(R.id.firstNameTextView);
            firstName = itemView.findViewById(R.id.lastNameTextView);
            firstName = itemView.findViewById(R.id.emailNameTextView);
            firstName = itemView.findViewById(R.id.phoneNumberTextView);


        }
    }
}
