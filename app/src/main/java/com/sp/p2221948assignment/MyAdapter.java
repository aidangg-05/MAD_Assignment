package com.sp.p2221948assignment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<MyDataModel> dataList;

    public MyAdapter(List<MyDataModel> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyDataModel data = dataList.get(position);

        holder.nameTextView.setText(data.getName());

        // Load and display the image
        loadImageFromDatabase(data.getImagePath(), holder.imageView);
    }

    private void loadImageFromDatabase(String imagePath, ImageView imageView) {
        // Use your custom method to load the image from the database
        byte[] imageByteArray = loadImageFromDatabase(imagePath);

        // Decode the byte array and set the image to the ImageView
        if (imageByteArray != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            imageView.setImageBitmap(bitmap);
        } else {
            // Handle the case where the image couldn't be loaded
            imageView.setImageResource(R.drawable.ic_error_placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }

    // Your custom method to load the image byte array from the database
    private byte[] loadImageFromDatabase(String imagePath) {
        // Implement this method based on your database logic
        // Example: return databaseHelper.getImageData(imagePath);
        return null;
    }
}
