package com.sam;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    String fName,pwd,rPwd,eid,post;
    EditText facultyName,password,rPassword,email;
    Button signup;
    TextView signin;
    Spinner postsSpinner;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        facultyName = (EditText) findViewById(R.id.etFacultyName);
        password = (EditText) findViewById(R.id.etPwd);
        rPassword= (EditText) findViewById(R.id.etRePwd);
        email= (EditText) findViewById(R.id.etEmail);
        signup= (Button) findViewById(R.id.bSignup);
        postsSpinner= (Spinner) findViewById(R.id.sPost);
        signin = (TextView) findViewById(R.id.tvSignin);

        final String[] postsList = getApplicationContext().getResources().getStringArray(R.array.posts);
        ArrayAdapter<String> postsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, postsList);
        postsSpinner.setAdapter(postsAdapter);

        postsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    post=postsList[position];
                } else {
                    post=null;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mAuth=FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Applying Listener to Signup Button
        signup.setOnClickListener(this);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        fName=facultyName.getText().toString();
        pwd=password.getText().toString();
        rPwd=rPassword.getText().toString();
        eid=email.getText().toString();

        if(pwd.equals(rPwd)) {
            mAuth.createUserWithEmailAndPassword(eid,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        final FacultyInfo info = new FacultyInfo(eid,fName,pwd,post,"false");
                        mDatabase.child("MJCET/faculty/").child(mAuth.getCurrentUser().getUid()).setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    mDatabase.child("MJCET/admin/requests/").child(mAuth.getCurrentUser().getUid()).setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Bundle bundle = new Bundle();
                                                bundle.putString("email",eid);
                                                startActivity(new Intent(SignupActivity.this, VerifyEmail.class).putExtras(bundle));
                                                finish();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(SignupActivity.this, "Error in registering faculty.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(SignupActivity.this, "Error in registering faculty.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Password not matching.", Toast.LENGTH_SHORT).show();
        }
    }
}
