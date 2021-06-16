package com.example.passwordgeneratormanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String LOWER_ALPHABETS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER_ALPHABETS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*_";
    private String new_password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHandler db = new DatabaseHandler(this);

        StringBuilder sb = new StringBuilder(LOWER_ALPHABETS);

        final TextInputLayout password_generated_textfield = findViewById(R.id.password_generated_textfield);
        final TextInputLayout password_length_textfield = findViewById(R.id.password_length_textfield);

        Button generate_password_btn = findViewById(R.id.generate_password_btn);
        Button store_password_btn = findViewById(R.id.store_password_btn);
        Button show_manager_btn = findViewById(R.id.show_manager);

        password_length_textfield.requestFocus();

        CheckBox uppercase = findViewById(R.id.uppercase);
        CheckBox numbers = findViewById(R.id.numbers);
        CheckBox symbols = findViewById(R.id.symbols);

        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        password_generated_textfield.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Password Text Empty", Toast.LENGTH_SHORT).show();
                } else {
                    ClipData clip = ClipData.newPlainText("Generated Password", new_password);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(MainActivity.this, "Copied", Toast.LENGTH_SHORT).show();
                }
            }
        });

        generate_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ip_length = password_length_textfield.getEditText().getText().toString().trim();
                int length = !ip_length.equals("") ? Integer.parseInt(ip_length) : 0;

                if (length == 0) {
                    password_length_textfield.setError("Please specify length for password");
                } else if (length <= 5) {
                    password_length_textfield.setError("Length cannot be less than 6");
                } else if (length >= 33) {
                    password_length_textfield.setError(("Length cannot be more than 32"));
                } else {
                    password_length_textfield.setError(null);

                    new_password = generatePassword(length,
                            uppercase.isChecked(), numbers.isChecked(), symbols.isChecked());

                    password_generated_textfield.getEditText().setText(new_password);
                }
            }
        });

        store_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final View custom_layout = getLayoutInflater().inflate(R.layout.save_password_layout, null);

                builder.setTitle("Save Password");
                builder.setView(custom_layout);

                TextInputLayout title = custom_layout.findViewById(R.id.title_textfield);
                TextInputLayout password = custom_layout.findViewById(R.id.password_textfield);

                Button save_btn = custom_layout.findViewById(R.id.save_btn);
                Button cancel_btn = custom_layout.findViewById(R.id.cancel_btn);

                password.getEditText().setText(new_password);

                AlertDialog dialog = builder.create();
                dialog.show();

                save_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String string_title = title.getEditText().getText().toString();
                        String string_password = password.getEditText().getText().toString();

                        if (string_password.equals("")) {
                            password.setError("Please enter a password");
                        } else if (string_title.equals("")) {
                            string_title = "Not Specified";
                        } else if (string_title.length() > 21) {
                            title.setError("Title/Description should be less than or equal to 20 characters");
                        } else {
                            db.addPassword(new Password(string_title, string_password));
                            Toast.makeText(MainActivity.this, "SAVED!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });

                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        });

        show_manager_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ManagerActivity.class));
            }
        });
    }

    private static String generatePassword(int length,
                                           boolean uppercase, boolean numbers, boolean symbols) {
        Random random = new Random();
        StringBuilder s = new StringBuilder(length);
        StringBuilder characters = new StringBuilder(LOWER_ALPHABETS);

        if (uppercase) characters.append(UPPER_ALPHABETS);
        if (numbers) characters.append(NUMBERS);
        if (symbols) characters.append(SYMBOLS);

        for (int i = 0; i < length; i++)
            s.append(characters.charAt(random.nextInt(characters.length())));

        return s.toString();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        //Checking for fragment count on back stack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Tap again to exit.", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
            return;
        }
    }
}