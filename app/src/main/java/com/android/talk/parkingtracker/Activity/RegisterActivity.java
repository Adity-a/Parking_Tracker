package com.android.talk.parkingtracker.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.talk.parkingtracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private TextInputEditText name, email, password, phoneNumber, carNumber;
    private Spinner city;
    private FirebaseStorage storage;
    private StorageReference reference;
    private Uri imageUri;
    private File destination = null;
    private String strName, strEmail, strPassword, strPhoneNumber, strCarNumber, strCity;
   /* private KeyGenerator keyGenerator;
    private SecretKey secretKey;
    private byte[] IV = new byte[16];
    private SecureRandom random;*/

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button btnNewRegister = findViewById(R.id.registerBtnRegister);
        name = findViewById(R.id.registerEtName);
        email = findViewById(R.id.registerEtEmail);
        password = findViewById(R.id.registerEtPassword);
        phoneNumber = findViewById(R.id.registerEtPhone);
        carNumber = findViewById(R.id.registerEtCatPlateNumber);
        city = findViewById(R.id.registerSpinnerCity);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance("gs://parkingapp-5e7e5.appspot.com");
        reference = storage.getReference();

        btnNewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extractData();
                if (verifyData()) {
                    mAuth.createUserWithEmailAndPassword(strEmail, strPassword)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String userId = user.getUid();

                                        DateFormat dateFormatDate = new SimpleDateFormat("dd-MM-yyyy", Locale.CANADA);
                                        DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss", Locale.CANADA);
                                        Calendar cal = Calendar.getInstance();
                                        String date = dateFormatDate.format(cal.getTime());
                                        String time = dateFormatTime.format(cal.getTime());


                                        // Write a message to the database
                                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://parkingapp-5e7e5-default-rtdb.firebaseio.com/");
                                        DatabaseReference myRef = database.getReference();
                                        HashMap<String, String> map = new HashMap<>();
                                        map.put("car_number", strCarNumber);
                                        map.put("city", strCity);
                                        map.put("email", strEmail);
                                        map.put("lastLoginDate", date);
                                        map.put("lastLoginTime", time);
                                        map.put("name", strName);
                                        map.put("number", strPhoneNumber);
                                        myRef.child("User").child(userId).setValue(map);
                                        Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.transition.animation_resource, R.transition.animation_resource);
                                        finish();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                } else {
                    Toast.makeText(RegisterActivity.this, "Error in the form", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Key generation for AES Algo
        /*try {
            keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            secretKey = keyGenerator.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        random = new SecureRandom();
        random.nextBytes(IV);*/

    }

    /* public static byte[] encrypt(byte[] plaintext, SecretKey key, byte[] IV) throws Exception {
             Cipher cipher = Cipher.getInstance("AES");
             SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
             IvParameterSpec ivSpec = new IvParameterSpec(IV);
             cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
             byte[] cipherText = cipher.doFinal(plaintext);
             return cipherText;
         }
         public static String decrypt(byte[] cipherText, SecretKey key, byte[] IV) {
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


    void extractData() {
       /* try{
            byte[] encrypt1 = encrypt(Objects.requireNonNull(name.getText()).toString().getBytes(), secretKey, IV);
            byte[] encrypt2 = encrypt(Objects.requireNonNull(password.getText()).toString().getBytes(), secretKey, IV);
            byte[] encrypt3 = encrypt(Objects.requireNonNull(phoneNumber.getText()).toString().getBytes(), secretKey, IV);
            byte[] encrypt4 = encrypt(Objects.requireNonNull(carNumber.getText()).toString().getBytes(), secretKey, IV);
            byte[] encrypt5 = encrypt(Objects.requireNonNull(city.getSelectedItem()).toString().getBytes(), secretKey, IV);
            strName = new String(encrypt1, StandardCharsets.UTF_8);
            strEmail = Objects.requireNonNull(email.getText()).toString();
            strPassword = new String(encrypt2, StandardCharsets.UTF_8);
            strPhoneNumber = new String(encrypt3, StandardCharsets.UTF_8);
            strCarNumber = new String(encrypt4, StandardCharsets.UTF_8);
            strCity = new String(encrypt5, StandardCharsets.UTF_8);
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/
        strName = name.getText().toString();
        strEmail = email.getText().toString();
        strPassword = password.getText().toString();
        strPhoneNumber = phoneNumber.getText().toString();
        strCarNumber = carNumber.getText().toString();
        strCity = city.getSelectedItem().toString();
    }

    boolean verifyData() {
        boolean isDataTrue = true;

        if (strName.isEmpty() || strEmail.isEmpty() || strPassword.isEmpty() || strPhoneNumber.isEmpty() || strCarNumber.isEmpty() || strCity.isEmpty()) {
            isDataTrue = false;
        }

        return isDataTrue;
    }
}
