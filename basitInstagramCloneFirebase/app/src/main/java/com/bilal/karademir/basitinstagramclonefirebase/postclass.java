package com.bilal.karademir.basitinstagramclonefirebase;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class postclass extends ArrayAdapter<String> {

    private final ArrayList<String> userEmail;
    private final ArrayList<String > userComment;
    private final ArrayList<String > userImage;
    private final ArrayList<String> zaman;
    private final Activity context;

    public postclass(ArrayList<String> userEmail,ArrayList<String > userComment,ArrayList<String > userImage,ArrayList<String > zaman,Activity context){
        super(context,R.layout.custom_view,userEmail);

        this.userEmail = userEmail;
        this.userComment = userComment;
        this.userImage = userImage;
        this.context = context;
        this.zaman = zaman;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.custom_view,null,true);
        TextView userMailText = customView.findViewById(R.id.textViewUserEmailCustomView);
        TextView usercomentText = customView.findViewById(R.id.textViewCommentCustomView);
        ImageView userImageText = customView.findViewById(R.id.imageViewCustomView);
        TextView userZaman = customView.findViewById(R.id.textViewTarihCustomView);
        userMailText.setText(userEmail.get(position));
        usercomentText.setText(userComment.get(position));
        userZaman.setText(zaman.get(position));
        Picasso.get().load(userImage.get(position)).into(userImageText);



        return customView;
    }
}
