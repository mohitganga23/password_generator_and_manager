package com.example.passwordgeneratormanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ManagerActivity extends AppCompatActivity {

    List<Password> passwordList;
    PasswordAdapter passwordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Saved Passwords");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView empty_text = findViewById(R.id.empty_text);
        empty_text.setText("NO PASSWORDS SAVED!");
        empty_text.setVisibility(View.GONE);

        RecyclerView recyclerView = findViewById(R.id.password_recycler_view);
        DatabaseHandler db = new DatabaseHandler(this);

        passwordList = new ArrayList<>();
        passwordList = db.getAllPasswords();

        if (passwordList.size() == 0) {
            empty_text.setVisibility(View.VISIBLE);
        } else {
            empty_text.setVisibility(View.GONE);
        }


        for (Password p : passwordList) {
            String log = "ID : " + p.getId() + "\tTitle : " + p.getTitle() +
                    "Password : " + p.getPassword();
            Log.d("DATA", log);
        }

        passwordAdapter = new PasswordAdapter(passwordList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(passwordAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}