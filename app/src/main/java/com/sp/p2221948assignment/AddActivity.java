package com.sp.p2221948assignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;
import android.net.Uri;
import android.database.Cursor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;

// Import statements...

public class AddActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICKER = 2;

    private ImageView imageView;
    private Button addImageButton;
    private EditText nameEditText, latitudeEditText, longitudeEditText, descriptionEditText;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        imageView = findViewById(R.id.imageView);
        addImageButton = findViewById(R.id.addImageButton);
        nameEditText = findViewById(R.id.enter_name);
        latitudeEditText = findViewById(R.id.enter_latitude);
        longitudeEditText = findViewById(R.id.enter_longitude);
        descriptionEditText = findViewById(R.id.editTextText6);

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickerOptions();
            }
        });

        Button doneButton = findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataToDatabase();
            }
        });
    }

    private void showImagePickerOptions() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                if (options[item].equals("Take Photo")) {
                    openCamera();
                } else if (options[item].equals("Choose from Gallery")) {
                    openGallery();
                } else if (options[item].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    private void openCamera() {
        // Intent to open the camera for capturing an image
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void openGallery() {
        // Intent to open the image picker
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICKER) {
                // Handle the result from the image picker
                if (data != null && data.getData() != null) {
                    // Get the selected image URI
                    Uri imageUri = data.getData();

                    // Convert URI to file path
                    imagePath = getRealPathFromUri(imageUri);

                    // Display the image in the ImageView
                    imageView.setImageURI(imageUri);
                    imageView.setVisibility(View.VISIBLE);
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Handle the result from the camera
                if (data != null && data.getExtras() != null) {
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(imageBitmap);
                    imageView.setVisibility(View.VISIBLE);

                    // Save the image to a file and get its path
                    imagePath = saveImageToFile(imageBitmap);
                }
            }
        }
    }





    private void saveDataToDatabase() {
        String name = nameEditText.getText().toString();
        String latitude = latitudeEditText.getText().toString();
        String longitude = longitudeEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        Log.d("AddActivity", "Image Path before saving to DB: " + imagePath);

        // Save the data to the database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_LATITUDE, latitude);
        values.put(DatabaseHelper.COLUMN_LONGITUDE, longitude);
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);
        values.put(DatabaseHelper.COLUMN_IMAGE_PATH, imagePath); // Assuming imagePath is set when an image is selected

        long newRowId = db.insert(DatabaseHelper.TABLE_NAME, null, values);

        // Close the database
        db.close();

        if (newRowId != -1) {
            // Data was successfully inserted
            String successMessage = "Data saved to the database with ID: " + newRowId;
            Log.d("Database", successMessage);
            Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();

            // Start MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            // Optionally, you can finish the current activity if you don't want it to remain in the back stack
            finish();
        } else {
            // Data insertion failed
            Log.e("Database", "Error saving data to the database");
            Toast.makeText(this, "Error saving data to the database", Toast.LENGTH_SHORT).show();
        }
    }

    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }

        // If the cursor is null or doesn't contain data, use the Uri's path
        return uri.getPath();
    }

    private String saveImageToFile(Bitmap bitmap) {
        // For simplicity, I'll provide a placeholder implementation
        File file = new File(getExternalFilesDir(null), "image_file.jpg");

        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // Handle the FileNotFoundException appropriately (log it, show a message, etc.)
            return null;  // or throw a custom exception if needed
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the IOException appropriately (log it, show a message, etc.)
            return null;  // or throw a custom exception if needed
        }
    }



}