package com.example.flashcards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import java.security.InvalidKeyException;
import com.example.flashcards.data.Word;
import com.example.flashcards.data.WordsRepository;
import com.example.flashcards.services.CommonHelper;

import static com.example.flashcards.services.CommonHelper.showErrorDialog;
import static com.example.flashcards.services.CommonHelper.showToast;

public class WordEditActivity extends AppCompatActivity implements TextWatcher {

    private int mWordId;
    private EditText txtEng;
    private EditText txtJpn;
    private CheckBox chkDone;
    private Button btnDelete;
    private Button btnSave;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_edit);

        // Show Back icon in the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(mBtnDeleteClick);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(mBtnSaveClick);

        txtEng = findViewById(R.id.txtEng);
        txtJpn = findViewById(R.id.txtJpn);
        chkDone = findViewById(R.id.chkDone);

        txtEng.setImeOptions(EditorInfo.IME_FLAG_FORCE_ASCII);
        txtEng.addTextChangedListener(this);
        txtJpn.addTextChangedListener(this);

        mHandler = new Handler(this.getMainLooper());
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
        setTitle(word._id == 0 ? R.string.lbl_add_title : R.string.lbl_edit_title);
        txtEng.setText(word.english);
        txtJpn.setText(word.japanese);
        chkDone.setChecked(word.done);

        enableSaveButton();

        if (word._id == 0) btnDelete.setVisibility(View.INVISIBLE);
    }

    private void enableSaveButton(){
        boolean enabled = txtEng.length() > 0 && txtJpn.length() > 0;
        btnSave.setEnabled(enabled);
    }

    private final View.OnClickListener mBtnSaveClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                String eng = txtEng.getText().toString();
                String jpn = txtJpn.getText().toString();
                boolean done = chkDone.isChecked();
                Word word = new Word(mWordId, eng, jpn ,done);
                WordsRepository.getInstance().save(word);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
                showErrorDialog(e, v.getContext());
                return;
            }

            showToast(mHandler, v.getContext(), getString(R.string.msg_saved));
            finish();
        }
    };

    private final View.OnClickListener mBtnDeleteClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CommonHelper.showOkCancelDialog(v.getContext(), getString(R.string.msg_confirm_delete),
                new Runnable() {
                    @Override
                    public void run() {
                        deleteWord();
                    }
                });
        }
    };

    private void deleteWord() {
        try {
            WordsRepository.getInstance().delete(mWordId);
            showToast(mHandler, this, getString(R.string.msg_deleted));

            // Go back directly to the List activity after deleting.
            Intent intent = new Intent(getApplicationContext(), WordListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);        // CLEARS THE STACK!
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog(e, this);
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Don't delete this method.
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Don't delete this method.
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Update Save button's enabled prop each time the text boxes changes.
        enableSaveButton();
    }
}