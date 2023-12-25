// MainActivity.java
package com.sp.p2221948assignment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.util.Log;

import com.sp.p2221948assignment.R;

import com.sp.p2221948assignment.MyDataModel;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<MyDataModel> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Show the overflow menu icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(null);

        // Initialize RecyclerView and data list
        recyclerView = findViewById(R.id.recyclerView);
        dataList = new ArrayList<>();
        adapter = new MyAdapter(dataList, this);

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);

        // Call a method to fetch data from the database and update the list
        loadDataFromDatabase();
    }

    // Add a method to fetch data from the database and update the list
    private void loadDataFromDatabase() {
        // Fetch data from the database and update dataList
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Replace "yourTableName" with the actual name of your table
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, null);

        // Check if the cursor is not null before using it
        if (cursor != null) {
            // Check if the cursor has columns
            if (cursor.moveToFirst()) {
                do {
                    int nameColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME);
                    int latitudeColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_LATITUDE);
                    int longitudeColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_LONGITUDE);
                    int descriptionColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION);
                    int imagePathColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE_PATH);

                    // Check if column indices are valid
                    if (nameColumnIndex != -1 && latitudeColumnIndex != -1 && longitudeColumnIndex != -1
                            && descriptionColumnIndex != -1 && imagePathColumnIndex != -1) {
                        String name = cursor.getString(nameColumnIndex);
                        double latitude = cursor.getDouble(latitudeColumnIndex);
                        double longitude = cursor.getDouble(longitudeColumnIndex);
                        String description = cursor.getString(descriptionColumnIndex);
                        String imagePath = cursor.getString(imagePathColumnIndex);

                        Log.d("MainActivity", "Image Path from database: " + imagePath);

                        MyDataModel data = new MyDataModel(name, latitude, longitude, description, imagePath);
                        dataList.add(data);
                    } else {
                        // Handle the case where the columns do not exist
                        // For example, log an error or handle it gracefully
                    }
                } while (cursor.moveToNext());
            }

            // Close the cursor
            cursor.close();
        }

        // Close the database
        db.close();

        // Notify the adapter that data has changed
        adapter.notifyDataSetChanged();
    }

    // Inflate the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option, menu);
        return true;
    }

    // Handle menu item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.about) {
            // Handle About menu item click
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
            return true;
        } else if (itemId == R.id.add) {
            // Handle Add menu item click
            Intent addIntent = new Intent(this, AddActivity.class);
            startActivity(addIntent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}
