package com.android.talk.parkingtracker.Activity;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.android.talk.parkingtracker.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputEditText name, email, contact, carPlate;

    private Spinner city;


    private FirebaseStorage storage;

    private StorageReference reference;

    private FirebaseDatabase database;

    private DatabaseReference myRef;

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

        storage = FirebaseStorage.getInstance("gs://parkingapp-22de1.appspot.com");
        reference = storage.getReference();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_left);
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                assert user != null;
                String userId = user.getUid();

                database = FirebaseDatabase.getInstance();
                myRef = database.getReference().child("User").child(userId);

                myRef.child("car_number").setValue(carPlate.getText().toString());
                myRef.child("city").setValue(city.getSelectedItem().toString());
                myRef.child("name").setValue(name.getText().toString());
                myRef.child("number").setValue(contact.getText().toString());

                Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


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



    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Use cancel button", Toast.LENGTH_SHORT).show();
    }
}
