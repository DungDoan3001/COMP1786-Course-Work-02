package com.dungdoan.contactappcw;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.HolderContact> {
    private Context context;
    private ArrayList<ContactModel> contactModelArrayList;
    DbHelper dbHelper;

    public ContactAdapter(Context context, ArrayList<ContactModel> contactModelArrayList) {
        this.context = context;
        this.contactModelArrayList = contactModelArrayList;

        dbHelper = new DbHelper(context);
    }

    @NonNull
    @Override
    public HolderContact onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_data, parent, false);

        return new HolderContact(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderContact holder, int position) {
        ContactModel contactModel = contactModelArrayList.get(position);

        String id = contactModel.getID();
        String name = contactModel.getName();
        String phoneNumber = contactModel.getPhoneNumber();
        String address = contactModel.getAddress();
        String email = contactModel.getEmail();
        String image = contactModel.getImage();

        holder.contactName.setText(name);
        holder.contactPhoneNumber.setText(phoneNumber);
        holder.contactAddress.setText(address);
        holder.contactEmail.setText(email);

        if(image.toString().equals("null")) {
            holder.imageView.setImageResource(R.drawable.person_image_vector);
        } else {
            holder.imageView.setImageURI(Uri.parse(image));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ContactDetails.class);
                intent.putExtra("CONTACT_ID", id);
                context.startActivity(intent);
            }
        });

        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreDialog(id, name, phoneNumber, address, email, image);
            }
        });
    }

    private void showMoreDialog(String id, String name, String phoneNumber, String address, String email, String image) {
        String[] options = {"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(which == 0) {
                    Intent intent = new Intent(context, ContactAddAndUpdate.class);

                    intent.putExtra("ID", id);
                    intent.putExtra("NAME", name);
                    intent.putExtra("PHONE_NUMBER", phoneNumber);
                    intent.putExtra("ADDRESS", address);
                    intent.putExtra("EMAIL", email);
                    intent.putExtra("IMAGE", image);
                    intent.putExtra("ID_EDIT_MODE", true);

                    context.startActivity(intent);
                } else {
                    dbHelper.deleteContact(id);
                    Toast.makeText(context, String.format("Delete contact: %s successfully!", name), Toast.LENGTH_SHORT).show();
                    ((MainActivity)context).onResume();
                }
            }
        });
        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return contactModelArrayList.size();
    }

    class HolderContact extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView contactName, contactPhoneNumber, contactAddress, contactEmail;
        ImageButton moreBtn;

        public HolderContact(@NonNull View itemView) {
            super(itemView);

            contactName = itemView.findViewById(R.id.contact_item_name);
            contactPhoneNumber = itemView.findViewById(R.id.contact_item_phone_number);
            contactAddress = itemView.findViewById(R.id.contact_item_address);
            contactEmail = itemView.findViewById(R.id.contact_item_email);
            imageView = itemView.findViewById(R.id.contact_item_image);
            moreBtn = itemView.findViewById(R.id.contact_item_more_btn);
        }
    }
}
