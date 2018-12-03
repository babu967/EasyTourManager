package com.codex.easytourmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codex.easytourmanager.event_class.UserInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {
    private TextView nameEdit, emailEdit, passEdit, contactEdit, addressEdit;
    private ImageView proPicView;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference rootReference;

    ProgressDialog dialog;

    private String imageData;
    private static final int IMAGE_REQUEST_CODE = 1;
    private String name, email, password, contactNo, address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Signing Up");

        auth = FirebaseAuth.getInstance();
        rootReference = FirebaseDatabase.getInstance().getReference();
        nameEdit = findViewById(R.id.name_edit);
        emailEdit = findViewById(R.id.email_edit);
        passEdit = findViewById(R.id.password_edit);
        contactEdit = findViewById(R.id.contact_edit);
        addressEdit = findViewById(R.id.address_edit);
        proPicView = findViewById(R.id.propic_view);
        Bitmap bitmap = ((BitmapDrawable) proPicView.getDrawable()).getBitmap();
        imageData = encodeImage(bitmap,Bitmap.CompressFormat.JPEG,50);

    }
    public void signUpUser(View view) {

        name = nameEdit.getText().toString();
        email = emailEdit.getText().toString();
        password = passEdit.getText().toString();
        contactNo = contactEdit.getText().toString();
        address = addressEdit.getText().toString();

        dialog.show();
        user = auth.getCurrentUser();
        if (user == null) {
            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(contactNo) && !TextUtils.isEmpty(address) && proPicView.getDrawable() != null) {
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()){

                                    user = auth.getCurrentUser();
                                    if (user != null) {
                                        dialog.dismiss();
                                        Toast.makeText(SignUpActivity.this, user.getEmail(), Toast.LENGTH_SHORT).show();
                                        createUserData();

                                    }else {

                                        dialog.dismiss();
                                        Log.d("Response","Failed to create user :"+task.getException().getMessage());
                                    }

                                }


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(SignUpActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                dialog.dismiss();
                Toast.makeText(this, "Fields can not be empty", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void goToLogIn(View view) {
        Intent gotoSignIn = new Intent(this, LogInActivity.class);
        startActivity(gotoSignIn);
    }


    public void chooseImage(View view) {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    imageData = encodeImage(bitmap, Bitmap.CompressFormat.JPEG, 25);
                    proPicView.setImageBitmap(decodeImage(imageData));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public String encodeImage(Bitmap bitmap, Bitmap.CompressFormat compressFormat, int quality) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(compressFormat, quality, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

    }

    public Bitmap decodeImage(String imageData) {

        byte[] bytes = Base64.decode(imageData, 0);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public void createUserData() {

        String key = rootReference.child("user").child(user.getUid()).push().getKey();
        UserInformation userInformation = new UserInformation(name, contactNo, email, address, key, imageData);
        rootReference.child("user").child(user.getUid()).child("info").setValue(userInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, "Failed ,Please try again", Toast.LENGTH_SHORT).show();
            }
        });

        //rootReference.child("user").child(user.getUid());
        //Toast.makeText(this, user.getUid(), Toast.LENGTH_SHORT).show();

    }
}
