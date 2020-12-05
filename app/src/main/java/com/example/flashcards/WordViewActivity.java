package com.example.flashcards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.flashcards.data.Word;

public class WordViewActivity extends AppCompatActivity {

    Button btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_view);

        // ActionBarに「戻る」アイコンを表示する。
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Word word = mDataset.get(position);
                Intent intent = new Intent(getApplicationContext(), WordEditActivity.class);
//                intent.putExtra("_id", word._id);
                startActivity(intent);

            }
        });
    }

}