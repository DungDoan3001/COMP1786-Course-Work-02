package com.dungdoan.contactappcw;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.google.android.material.textfield.TextInputEditText;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ContactAddAndUpdate extends AppCompatActivity {
    private ImageView contactImage;
    private TextInputEditText contactName, contactPhoneNumber, contactEmail, contactAddress;
    private Button addContactBtn;
    private Uri imageUri;
    private ActionBar actionBar;

    // Internal use
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;
    private String[] cameraPermission;
    private String[] storagePermission;
    private boolean isEditMode = false;
    String id, name, phoneNumber, address, email;
    private DbHelper dbHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add_and_update);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Add contact");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        contactImage = findViewById(R.id.contact_input_image);
        contactName = findViewById(R.id.contact_input_name);
        contactPhoneNumber = findViewById(R.id.contact_input_phone_number);
        contactAddress = findViewById(R.id.contact_input_address);
        contactEmail = findViewById(R.id.contact_input_email);
        addContactBtn = findViewById(R.id.add_input_contact_btn);

        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("IS_EDIT_MODE", false);

        if(isEditMode) {
            actionBar.setTitle("Update contact");

            id = intent.getStringExtra("ID");
            name = intent.getStringExtra("NAME");
            phoneNumber = intent.getStringExtra("PHONE_NUMBER");
            address = intent.getStringExtra("ADDRESS");
            email = intent.getStringExtra("EMAIL");
            imageUri = Uri.parse(intent.getStringExtra("IMAGE"));

            contactName.setText(name);
            contactPhoneNumber.setText(phoneNumber);
            contactAddress.setText(address);
            contactEmail.setText(email);

            if(imageUri.toString().equals("null")){
                //no image, set to default image
                contactImage.setImageResource(R.drawable.person_image_vector);
            } else {
                contactImage.setImageURI(imageUri);
            }
        }

        dbHelper = new DbHelper(this);

        cameraPermission = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_MEDIA_IMAGES};
        storagePermission = new String[]{android.Manifest.permission.READ_MEDIA_IMAGES};

        contactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePickDialog();
            }
        });

        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();
                Intent mainIntent = new Intent(ContactAddAndUpdate.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });
    }

    private void inputData() {
        name = contactName.getText().toString().trim();
        phoneNumber = contactPhoneNumber.getText().toString().trim();
        address = contactAddress.getText().toString().trim();
        email = contactEmail.getText().toString().trim();

        boolean validateInput = validateInput();

        if(validateInput) {
            if(isEditMode) {
                dbHelper.updateContact(id, name, phoneNumber, address, email, String.valueOf(imageUri));
                Toast.makeText(this, "Update Contact Successfully", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.addContact(name, phoneNumber, address, email, String.valueOf(imageUri));
                Toast.makeText(this, "Successfully Add Contact: " + name, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInput() {
        boolean validateResult = true;

        if(name.trim().isEmpty()) {
            contactName.setError("Name of contact is required");
            validateResult = false;
        }

        if(phoneNumber.trim().isEmpty()) {
            contactPhoneNumber.setError("Phone number of contact is required");
            validateResult = false;
        }

        if(address.trim().isEmpty()) {
            contactAddress.setError("Address of contact is required");
            validateResult = false;
        }

        if(email.trim().isEmpty()) {
            contactEmail.setError("Email of contact is required");
            validateResult = false;
        }

        return validateResult;
    }

    private void imagePickDialog() {
        String[] options = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick image from");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0) {
                    boolean cameraPermission = checkCameraPermission();
                    if(!cameraPermission) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                } else if (i == 1) {
                    boolean storagePermission = checkStoragePermission();
                    if(!storagePermission) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    private boolean checkCameraPermission() {
        boolean cameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == (PackageManager.PERMISSION_GRANTED);

        return cameraPermission && storagePermission;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        pickFromCameraLauncher.launch(cameraIntent);
    }

    ActivityResultLauncher<Intent> pickFromCameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult activityResult) {
            int resultCode = activityResult.getResultCode();

            if(resultCode == RESULT_OK) {
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CropImageOptions cropImageOptions = new CropImageOptions();
                cropImageOptions.imageSourceIncludeCamera = true;
                cropImageOptions.imageSourceIncludeGallery = true;
                CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(imageUri, cropImageOptions);
                cropImage.launch(cropImageContractOptions);

            }
        }
    });

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if(grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Camera & Storage permissions are required", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;

            case STORAGE_REQUEST_CODE: {
                if(grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(storageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Storage permission is required", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    ActivityResultLauncher<CropImageContractOptions> cropImage = registerForActivityResult(new CropImageContract(), result -> {
        if(result.isSuccessful()) {
            Uri imageCropped = result.getUriContent();
            imageUri = imageCropped;
            contactImage.setImageURI(imageCropped);
        } else {
            Exception err = result.getError();
            Toast.makeText(this, "Error: " + err , Toast.LENGTH_SHORT).show();
        }
    });

    private boolean checkStoragePermission() {
        boolean storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return storagePermission;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        pickFromGalleryLauncher.launch(galleryIntent);
    }

    ActivityResultLauncher<Intent> pickFromGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult activityResult) {
            int resultCode = activityResult.getResultCode();

            if(resultCode == RESULT_OK) {
                Intent data = activityResult.getData();

                CropImageOptions cropImageOptions = new CropImageOptions();
                cropImageOptions.imageSourceIncludeGallery = true;
                cropImageOptions.imageSourceIncludeCamera = true;

                CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(data.getData(), cropImageOptions);
                cropImage.launch(cropImageContractOptions);
            }
        }
    });
}
