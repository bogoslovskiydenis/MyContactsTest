package com.example.mycontactstest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private ArrayList<Contact> contactArrayList = new ArrayList<>();
    private MainActivity mainActivity;
    public ContactAdapter(ArrayList<Contact> contactArrayList, MainActivity mainActivity){
        this.contactArrayList = contactArrayList;
        this.mainActivity = mainActivity;
    }


    public void setContactArrayList(ArrayList<Contact> contactArrayList) {
        this.contactArrayList = contactArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ContactViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactArrayList.get(position);

        holder.firstNameEditTextView.setText(contact.getFirstName());
        holder.lastNameTextView.setText(contact.getLastName());
        holder.emailTextView.setText(contact.getEmail());
        holder.phoneNumberTextView.setText(contact.getPhoneNumber());

    }

    @Override
    public int getItemCount() {
        return contactArrayList.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        private TextView firstNameEditTextView;
        private TextView lastNameTextView;
        private TextView emailTextView;
        private TextView phoneNumberTextView;

        public ContactViewHolder(@NonNull View itemView) {

            super(itemView);

            firstNameEditTextView = itemView.findViewById(R.id.firstNameEditTextView);
            lastNameTextView = itemView.findViewById(R.id.lastNameTextView);
            emailTextView = itemView.findViewById(R.id.emailNameTextView);
            phoneNumberTextView = itemView.findViewById(R.id.phoneNumberTextView);


        }
    }
}
