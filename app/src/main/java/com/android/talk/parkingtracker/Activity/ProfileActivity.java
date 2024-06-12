package com.android.talk.parkingtracker.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.talk.parkingtracker.Model.User;
import com.android.talk.parkingtracker.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextInputEditText name, email, contactNumber, carNumber, city;
    private Button edit;
    private FirebaseAuth mAuth;
    /*private KeyGenerator keyGenerator;
    private SecretKey secretKey;
    private byte[] IV = new byte[16];
    private SecureRandom random;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        name = findViewById(R.id.userProfileTvNameData);
        email = findViewById(R.id.userProfileTvEmailData);
        contactNumber = findViewById(R.id.userProfileTvContactNumberData);
        carNumber = findViewById(R.id.userProfileTvCarNumberData);
        city = findViewById(R.id.userProfileTvCityData);
        edit = findViewById(R.id.profileBtnEdit);
       /* try {
            keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            secretKey = keyGenerator.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        random = new SecureRandom();
        random.nextBytes(IV);*/


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userId = user.getUid();
        Log.d("ProfileActivity", "userID: " + userId);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User").child(userId);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user1 = dataSnapshot.getValue(User.class);
                assert user1 != null;
                Log.d("ProfileActivity", user1.getEmail());
               /* Log.d("RegisterActivity", String.valueOf(secretKey));
                Log.d("RegisterActivity", Arrays.toString(IV));
                try{
                    String decrypt = decrypt(user1.getName().getBytes(),secretKey,IV);
                    String decrypt1 = decrypt(user1.getEmail().getBytes(),secretKey,IV);
                    String decrypt2 = decrypt(user1.getNumber().getBytes(),secretKey,IV);
                    String decrypt3 = decrypt(user1.getCar_number().getBytes(),secretKey,IV);
                    String decrypt4 = decrypt(user1.getCity().getBytes(),secretKey,IV);

                    name.setText(decrypt);
                    email.setText(decrypt1);
                    contactNumber.setText(decrypt2);
                    carNumber.setText(decrypt3);
                    city.setText(decrypt4);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }*/

                name.setText(user1.getName());
                email.setText(user1.getEmail());
                contactNumber.setText(user1.getNumber());
                carNumber.setText(user1.getCar_number());
                city.setText(user1.getCity());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("name", name.getText().toString());
                intent.putExtra("email", email.getText().toString());
                intent.putExtra("contactNumber", contactNumber.getText().toString());
                intent.putExtra("carNumber", carNumber.getText().toString());
                intent.putExtra("city", city.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_right);
                finish();
            }
        });
    }
   /* public static String decrypt(byte[] cipherText, SecretKey key, byte[] IV) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(IV);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decryptedText = cipher.doFinal(cipherText);
            return new String(decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

   /* public static byte[] encrypt(byte[] plaintext, SecretKey key, byte[] IV) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] cipherText = cipher.doFinal(plaintext);
        return cipherText;
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_right);
    }
}
