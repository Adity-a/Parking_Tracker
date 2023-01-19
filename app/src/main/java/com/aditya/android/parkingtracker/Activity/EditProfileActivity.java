package com.aditya.android.parkingtracker.Activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.aditya.android.parkingtracker.R;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText name, email, contact, carPlate;

    private Spinner city;

    private Button save, cancel;

    String strName = "", strEmail = "", strContact = "", strCarPlate = "", strCity = "";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        name = findViewById(R.id.userProfileEditEtNameData);
        email = findViewById(R.id.userProfileEditEtEmailData);
        contact = findViewById(R.id.userProfileEditEtContactNumberData);
        carPlate = findViewById(R.id.userProfileEditEtCarNumberData);
        city = findViewById(R.id.userProfileEditSpinnerCityData);
        save = findViewById(R.id.profileEditBtnSave);
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_left);
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        save.setOnClickListener(this);

        Intent intent = getIntent();
        strName = intent.getStringExtra("name");
        strEmail = intent.getStringExtra("email");
        strContact = intent.getStringExtra("contactNumber");
        strCarPlate = intent.getStringExtra("carNumber");
        strCity = intent.getStringExtra("city");

        name.setText(strName);
        email.setText(strEmail);
        contact.setText(strContact);
        carPlate.setText(strCarPlate);

        city.setSelection(getIndex(city, strCity));

    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.profileEditBtnSave) {//Save to firebase
            FirebaseUser user = mAuth.getCurrentUser();
            assert user != null;
            String userId = user.getUid();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference().child("User").child(userId);

            myRef.child("car_number").setValue(carPlate.getText().toString());
            myRef.child("city").setValue(city.getSelectedItem().toString());
            myRef.child("name").setValue(name.getText().toString());
            myRef.child("number").setValue(contact.getText().toString());

            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Use cancel button", Toast.LENGTH_SHORT).show();
    }
}
