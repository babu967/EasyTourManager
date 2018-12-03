package com.codex.easytourmanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codex.easytourmanager.event_class.EventInfo;
import com.codex.easytourmanager.event_class.Moments;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddMomentActivity extends AppCompatActivity {
    private EditText momentDetailsEt;
    private ImageView momentImageView;
    private Button loadImageButton, saveMomentButton;
    private Bitmap bitmap;
    private static final int LOAD_IMAGE_REQUEST = 7;
    private static final int LOAD_CAMERA_REQUEST = 8;
    private String imageData;

    private FirebaseUser user;
    private DatabaseReference rootReference;
    private Calendar calendar;
    private String timeDate;
    private EventInfo eventInfo = new EventInfo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_moment);
        momentDetailsEt = findViewById(R.id.moment_details);
        momentImageView = findViewById(R.id.moment_image_view);
        loadImageButton = findViewById(R.id.load_image_button);
        saveMomentButton = findViewById(R.id.save_moment_button);

        timeDate=getTimeDate();
        eventInfo =(EventInfo) getIntent().getSerializableExtra("frommomentfrag");
        //String updateStatus = getIntent().getStringExtra("updateMomentState");
        final Moments moments = (Moments)getIntent().getSerializableExtra("updatemoment");


        //Toast.makeText(this,event.getEventKey() , Toast.LENGTH_SHORT).show();
        user = FirebaseAuth.getInstance().getCurrentUser();
        rootReference = FirebaseDatabase.getInstance().getReference();


        if (moments!=null){

            saveMomentButton.setText("Update Moment");
            loadImageButton.setText("Change Image");
            final EventInfo event = (EventInfo)getIntent().getSerializableExtra("fromadapter");
            momentDetailsEt.setText(moments.getMomentCaption());
            momentImageView.setImageBitmap(moments.retrieveMomentImage());
            imageData = moments.getMomentImage();
            saveMomentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String caption = momentDetailsEt.getText().toString();
                    if (!imageData.isEmpty() && !TextUtils.isEmpty(caption)){
                        String eventKey = event.getEventKey();
                        String key =moments.getMomentKey();
                        Moments updateedMoments = new Moments(imageData,caption,key,timeDate,eventKey);
                        rootReference.child("user").child(user.getUid()).child("moment").child(key).setValue(updateedMoments).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AddMomentActivity.this, "Moment Updated", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(AddMomentActivity.this, "Update failed Try again", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            });
        }else {

            saveMomentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String details = momentDetailsEt.getText().toString();

                    if (!TextUtils.isEmpty(details) && !imageData.isEmpty()) {
                        //Do firebase code here
                        String key = rootReference.child("user").child(user.getUid()).child("moment").push().getKey();
                        String eventKey = eventInfo.getEventKey();
                        Moments moment = new Moments(imageData,details,key,timeDate,eventKey);
                        rootReference.child("user").child(user.getUid()).child("moment").child(key).setValue(moment).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                finish();
                                Toast.makeText(AddMomentActivity.this, "Moment saved", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddMomentActivity.this, "Failed, Try Again", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(AddMomentActivity.this, "Fields can not be empty", Toast.LENGTH_SHORT).show();
                    }


                }
            });


        }






        loadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, LOAD_IMAGE_REQUEST);
            }
        });


        momentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(intent, LOAD_CAMERA_REQUEST);
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == LOAD_IMAGE_REQUEST && resultCode==RESULT_OK){
            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                imageData = encodeImage(bitmap,25);
                momentImageView.setImageBitmap(decodeImage(imageData));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (requestCode==LOAD_CAMERA_REQUEST && resultCode ==RESULT_OK){

            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            imageData = encodeImage(bitmap,100);
            momentImageView.setImageBitmap(decodeImage(imageData));
        }



    }

    public String encodeImage(Bitmap bitmap,int quality) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
    }

    public Bitmap decodeImage(String imageString) {
        byte[] bytes = Base64.decode(imageString, 0);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public String getTimeDate(){

        calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy   hh:mm aa");
        return  simpleDateFormat.format(calendar.getTime());

    }



}

