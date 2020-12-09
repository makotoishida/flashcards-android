package com.example.flashcards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashcards.data.Word;
import com.example.flashcards.data.WordsRepository;

import java.security.InvalidKeyException;
import java.time.Duration;

public class WordViewActivity extends AppCompatActivity {

    private int mWordId;
    private TextView txtEng;
    private TextView txtJpn;
    private TextView txtDone;
    private Button btnDone;
    private Button btnUndone;
    private Button btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_view);

        // Show Back icon in the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(mBtnEditClick);

        txtEng = findViewById(R.id.txtEng);
        txtJpn = findViewById(R.id.txtJpn);
        txtDone = findViewById(R.id.txtDone);

        btnDone = findViewById(R.id.btnDone);
        btnDone.setOnClickListener(mBtnDoneClick);
        btnUndone = findViewById(R.id.btnUndone);
        btnUndone.setOnClickListener(mBtnUndoneClick);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Finish this activity and go back to the previous screen.
        finish();
        return super.onSupportNavigateUp();
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
            txtDone.setText("");
            return;
        }

        txtEng.setText(word.english);
        txtJpn.setText(word.japanese);
        txtDone.setText(word.getDoneString());

        btnDone.setEnabled(!word.done);
        btnUndone.setEnabled(word.done);
    }

    private View.OnClickListener mBtnEditClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), WordEditActivity.class);
            intent.putExtra("_id", mWordId);
            startActivity(intent);

        }
    };

    private View.OnClickListener mBtnDoneClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                WordsRepository.getInstance().updateDone(mWordId, true);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
                Toast.makeText(v.getContext(), R.string.msg_error, Toast.LENGTH_LONG).show();
            }
            loadWord(mWordId);
        }
    };

    private View.OnClickListener mBtnUndoneClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                WordsRepository.getInstance().updateDone(mWordId, false);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
                Toast.makeText(v.getContext(), R.string.msg_error, Toast.LENGTH_LONG).show();
            }
            loadWord(mWordId);
        }
    };

}