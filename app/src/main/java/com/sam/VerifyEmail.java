package com.sam;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class VerifyEmail extends AppCompatActivity {

    EditText changeEmailid;
    TextView emailVerify;
    Button changeEmail, verifyEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        changeEmailid = (EditText) findViewById(R.id.etChangeEmail);
        emailVerify = (TextView) findViewById(R.id.tvEmailVerification);
        changeEmail = (Button) findViewById(R.id.bChangeEmail);
        verifyEmail = (Button) findViewById(R.id.bVerifyEmail);
        changeEmailid.setVisibility(View.GONE);
        emailVerify.setVisibility(View.VISIBLE);

        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");
        changeEmailid.setText(email);
        emailVerify.setText("A verification email will be sent to " + email + ". Follow that link to verify your email address.");

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEmailid.setVisibility(View.VISIBLE);
                emailVerify.setVisibility(View.GONE);
            }
        });

        verifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().getCurrentUser().updateEmail(changeEmailid.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(VerifyEmail.this, "Email Sent", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(VerifyEmail.this, HomeActivity.class));
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(VerifyEmail.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
