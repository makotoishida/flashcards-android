package com.example.flashcards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.flashcards.data.Word;
import com.example.flashcards.data.WordsRepository;

public class WordEditActivity extends AppCompatActivity {

    private int mWordId;
    private Button btnDelete;
    private Button btnSave;
    private EditText txtEng;
    private EditText txtJpn;
    private CheckBox chkDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_edit);

        // Show Back icon button in the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnDelete = findViewById(R.id.btnDelete);
        btnSave = findViewById(R.id.btnSave);
        txtEng = findViewById(R.id.txtEng);
        txtJpn = findViewById(R.id.txtJpn);
        chkDone = findViewById(R.id.chkDone);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Back to the previous screen when 'Back' button is pressed.
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        mWordId = intent.getIntExtra("_id", 0);
        loadWord(mWordId);
    }

    private void loadWord(int id) {
        Word word = WordsRepository.getInstance().getById(id);
        showWord(word);
    }

    private void showWord(Word word) {
        if (word == null) {
            txtEng.setText("");
            txtJpn.setText("");
            chkDone.setChecked(false);
            return;
        }

        txtEng.setText(word.english);
        txtJpn.setText(word.japanese);
        chkDone.setChecked(word.done);
    }

}