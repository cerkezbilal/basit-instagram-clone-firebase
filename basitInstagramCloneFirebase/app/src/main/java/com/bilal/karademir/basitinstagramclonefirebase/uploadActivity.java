package com.bilal.karademir.basitinstagramclonefirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class uploadActivity extends AppCompatActivity {

    Button buttonUpload;
    EditText editTextComment;
    ImageView imageViewPost;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    Uri selectedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        tanimla();
          firebaseDatabase = FirebaseDatabase.getInstance();
          myRef = firebaseDatabase.getReference();
          mAuth = FirebaseAuth.getInstance();
          mStorageRef = FirebaseStorage.getInstance().getReference();






    }

    public void tanimla(){


        buttonUpload = findViewById(R.id.buttonUpload);
        imageViewPost = findViewById(R.id.imageViewPost);
        editTextComment = findViewById(R.id.editTextComment);

    }

    public void upload(View view){
        UUID uuid = UUID.randomUUID();
        final String imageName = "images/"+uuid+".jpg";

       StorageReference storageReference = mStorageRef.child(imageName);
       storageReference.putFile(selectedImage).addOnSuccessListener(uploadActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

               //download url

               StorageReference newReference  = FirebaseStorage.getInstance().getReference(imageName);
               newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri uri) {
                       String downloadURL = uri.toString();

                       FirebaseUser user = mAuth.getCurrentUser();

                       String userEmail = user.getEmail();

                       String userComment = editTextComment.getText().toString();

                       Date simdikiZaman = new Date();

                       DateFormat df = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                       String zaman =df.format(simdikiZaman);





                       UUID uuid1 = UUID.randomUUID();
                       String uuidString = uuid1.toString();

                       myRef.child("Posts").child(uuidString).child("useremail").setValue(userEmail);
                       myRef.child("Posts").child(uuidString).child("comment").setValue(userComment);
                       myRef.child("Posts").child(uuidString).child("downloadurl").setValue(downloadURL);
                       myRef.child("Posts").child(uuidString).child("zaman").setValue(zaman);

                       Toast.makeText(uploadActivity.this, "Resim Yüklendi", Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(getApplicationContext(),feedActivity.class);
                       startActivity(intent);
                       finish();


                   }
               });



               //username,comment

           }
       }).addOnFailureListener(uploadActivity.this, new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {

               Toast.makeText(uploadActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

           }
       });



    }

    public void selectImage(View view){

        //izin istiyoruz galeriye girmek için

        if(ContextCompat.checkSelfPermission(uploadActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(uploadActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);

        }

    }

    //izin var mı yoksa ilk defa mı oluşturuldu onu kontrol ediyoruz

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Zaten izin verildiyse önceden yapılacaklar

        if(requestCode ==1){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);

            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //Resim seçtikten sonra olacaklar için


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //resim seçildiyse
        if(requestCode==2 && resultCode==RESULT_OK&&data != null){


            selectedImage = data.getData();//resmin galeri adresi

            //Sdk kontrolü

            try {

                if(Build.VERSION.SDK_INT>=28){
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),selectedImage);
                    Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                    imageViewPost.setImageBitmap(bitmap);
                }else {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                    imageViewPost.setImageBitmap(bitmap);
                }

            }catch (Exception e){

                e.printStackTrace();
            }




        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}


