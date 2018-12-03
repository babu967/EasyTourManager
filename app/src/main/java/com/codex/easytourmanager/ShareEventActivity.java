package com.codex.easytourmanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.codex.easytourmanager.fragments.ExpenseFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class ShareEventActivity extends AppCompatActivity {

    String eventKey;
    ImageView qrView;
    Button doneButton;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_event);
        qrView = findViewById(R.id.qr_view);
        doneButton =findViewById(R.id.done_btn);
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        Intent intent = getIntent();
        eventKey = intent.getStringExtra("eventData");

        String qrData = userId+ " "+ eventKey;

        Toast.makeText(this, qrData, Toast.LENGTH_SHORT).show();
        generateQrCOde(qrData);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ExpenseFragment expenseFragment = new ExpenseFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.exp_moment_container_layout, expenseFragment);
                transaction.commit();
            }
        });


    }

    public void generateQrCOde(String string){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(string,BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap= encoder.createBitmap(bitMatrix);
            qrView.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
