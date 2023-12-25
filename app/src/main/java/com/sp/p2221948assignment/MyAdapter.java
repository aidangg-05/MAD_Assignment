// MyAdapter.java
package com.sp.p2221948assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<MyDataModel> dataList;
    private Context context;

    public MyAdapter(List<MyDataModel> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyDataModel data = dataList.get(position);

        // Set data to views
        holder.nameTextView.setText(data.getName());

        // Check if imagePath is not null or empty
        if (data.getImagePath() != null && !data.getImagePath().isEmpty()) {
            // Set the image visibility to visible
            holder.imageView.setVisibility(View.VISIBLE);
            // Load the image using an image loading library like Glide or Picasso
            loadImageFromPath(data.getImagePath(), holder.imageView);
        } else {
            // Set the image visibility to invisible
            holder.imageView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImageView);
            nameTextView = itemView.findViewById(R.id.itemNameTextView);
        }
    }

    private void loadImageFromPath(String imagePath, ImageView imageView) {
        if (imagePath != null && !imagePath.isEmpty()) {
            // Set the image visibility to visible
            imageView.setVisibility(View.VISIBLE);

            // Decode the file path into a Bitmap with dimensions scaled to fit the ImageView
            Bitmap bitmap = decodeSampledBitmapFromFile(imagePath, imageView.getWidth(), imageView.getHeight());

            // Set the Bitmap to the ImageView
            imageView.setImageBitmap(bitmap);
        } else {
            // Set the image visibility to invisible
            imageView.setVisibility(View.INVISIBLE);
        }
    }

    private Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
