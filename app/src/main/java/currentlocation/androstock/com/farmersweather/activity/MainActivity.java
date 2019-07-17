package currentlocation.androstock.com.farmersweather.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import currentlocation.androstock.com.farmersweather.Farming.activities.DashboardActivity;
import currentlocation.androstock.com.farmersweather.R;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView emailfield;
    private EditText password;
    private Button goregisterButton;
    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase_users;
    private FirebaseUser userl;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (mDatabase_users != null) {
            mDatabase_users.keepSynced(true);
            // mAuth = FirebaseAuth.getInstance();

        }

        mAuth = FirebaseAuth.getInstance();
        userl = mAuth.getCurrentUser();
        if (userl != null) {
            Intent i=new Intent(MainActivity.this,DashboardActivity.class);
            startActivity(i);
            finish();
        }
        mDatabase_users = FirebaseDatabase.getInstance().getReference("Farmers");//.child("Users");
        // Set up the login form.
        progressDialog = new ProgressDialog(this);
        emailfield = (AutoCompleteTextView) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        Button email_sign_in_button = (Button) findViewById(R.id.login_btn);


        goregisterButton = (Button) findViewById(R.id.reg);
        goregisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToegister();
            }
        });

        email_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSigningIn();
            }
        });
    }

    private void startSigningIn() {
        String email = emailfield.getText().toString().trim();
        String pass = password.getText().toString().trim();


        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
            progressDialog.setMessage("Checking sign in");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isComplete()) {
                        if (task.isSuccessful()) {
                            checkUserExists();
                        } else {
                            Toast.makeText(MainActivity.this, "Sign In Error", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }


                }
            });
        } else {
            Toast.makeText(MainActivity.this, "fill in fields", Toast.LENGTH_LONG).show();
        }
    }

    private void checkUserExists() {
        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabase_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user_id)) {
                    Intent i = new Intent(MainActivity.this, DashboardActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    progressDialog.dismiss();

                } else {
                    progressDialog.dismiss();
                    Intent setUpIntent = new Intent(MainActivity.this, DashboardActivity.class);
                    setUpIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setUpIntent);

                    //Toast.makeText(LoginActivity.this, "you need to set up your account", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void goToegister() {
        startActivity(new Intent(MainActivity.this, Signup.class));

    }
}

