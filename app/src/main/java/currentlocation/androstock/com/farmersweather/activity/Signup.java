package currentlocation.androstock.com.farmersweather.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import currentlocation.androstock.com.farmersweather.Farming.activities.DashboardActivity;
import currentlocation.androstock.com.farmersweather.R;


public class Signup extends AppCompatActivity {

    private EditText usernameInput;
    private EditText EmailnameInput;
    private EditText field;
    private EditText Password;
    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference("Farmers");
        mProgress=new ProgressDialog(this);
        usernameInput=(EditText)findViewById(R.id.nameInput);
        EmailnameInput=(EditText)findViewById(R.id.EMAIL_input);
        //field=(EditText)findViewById(R.id.districtInput);
        Password=(EditText)findViewById(R.id.passwordInput);

        Button regbutton=(Button)findViewById(R.id.registerButton);
        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegistering();
            }
        });
        Button bb=(Button)findViewById(R.id.login);
        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(Signup.this,MainActivity.class);
                startActivity(i);

            }
        });

    }

    private void startRegistering() {

        final String username=usernameInput.getText().toString().trim();
      // final String districts=field.getText().toString().trim();
       final String userpassword=Password.getText().toString().trim();
       final String useremail=EmailnameInput.getText().toString().trim();


        if(!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(useremail)&&!TextUtils.isEmpty(userpassword)){
            mProgress.setMessage("Registering...");
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(useremail,userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isComplete()){
                        if(task.isSuccessful()){

                            String user_id=mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb= mDatabase.child(user_id);
                            currentUserDb.child("User_Name").setValue(username);
                            currentUserDb.child("User_password").setValue(userpassword);
                           // currentUserDb.child("district").setValue(districts);

                            mProgress.dismiss();

                            Intent mainIntent=new Intent(Signup.this,DashboardActivity.class);
                            mainIntent.addFlags(mainIntent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainIntent);

                        }else {
                            Toast.makeText(Signup.this, "Registration failed try again later", Toast.LENGTH_LONG).show();
                            mProgress.dismiss();
                        }
                    }

                }
            });




        }

    }


}
