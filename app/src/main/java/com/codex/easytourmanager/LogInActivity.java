package com.codex.easytourmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import static com.codex.easytourmanager.MainActivity.INTERVAL;

public class LogInActivity extends AppCompatActivity {

    private EditText emailET, passwordET;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private long time;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user!=null){
            finish();
            goToMainActivity();
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging In");
        emailET = findViewById(R.id.email_ET);
        passwordET = findViewById(R.id.password_ET);



    }

    public void logIn(View view) {



        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        progressDialog.show();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            auth = FirebaseAuth.getInstance();

            auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                goToMainActivity();
                            }else {

                                progressDialog.dismiss();
                                Log.d("12345","signInWithEmail : Failure",task.getException());
                                Toast.makeText(LogInActivity.this, "E-mail or Password is incorrect", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {


                }
            });
        }
    }

    public void goToSignUp(View view) {
        Intent gotoSignUp = new Intent(this, SignUpActivity.class);
        startActivity(gotoSignUp);

    }

    public void goToMainActivity(){
        Intent intent = new Intent(LogInActivity.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        if (time + INTERVAL > System.currentTimeMillis()) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
        Toast.makeText(this, "Press again to exit app", Toast.LENGTH_SHORT).show();
        time = System.currentTimeMillis();


    }
}
