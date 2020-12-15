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

/**
 * 単語編集画面
 */
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

        // 画面上部のActionBarの左端に「←（戻る）」アイコンを表示する。
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
        // このActivityを終了して前の画面に戻る。
        finish();
        return super.onSupportNavigateUp();
    }

    // Activityが新規に生成された、または別画面から戻って来た時の処理
    @Override
    protected void onResume() {
        super.onResume();

        // 渡されたインテントから単語IDを得る。
        Intent intent = getIntent();
        mWordId = intent.getIntExtra("_id", 0);

        // 指定された単語のデータをデータベースから取得して表示する。
        loadWord(mWordId);
    }

    // 指定された単語のデータをデータベースから取得して表示する。
    private void loadWord(int id) {
        Word word = WordsRepository.getInstance().getById(id);
        showWord(word);
    }

    // 単語を表示する。
    private void showWord(Word word) {
        setTitle(word._id == 0 ? R.string.lbl_add_title : R.string.lbl_edit_title);
        txtEng.setText(word.english);
        txtJpn.setText(word.japanese);
        chkDone.setChecked(word.done);

        enableSaveButton();

        // 新規追加の場合は削除ボタンを非表示にする。
        btnDelete.setVisibility(word._id == 0 ? View.INVISIBLE : View.VISIBLE);
    }

    // 保存ボタンが押せるか否かの制御。
    private void enableSaveButton(){
        // 英語と日本語の両方に1文字以上入力されていれば保存ボタンを有効にする。
        boolean enabled = txtEng.length() > 0 && txtJpn.length() > 0;
        btnSave.setEnabled(enabled);
    }

    // 保存ボタンが押された時の処理
    private final View.OnClickListener mBtnSaveClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveWord();
        }
    };

    // 単語をデータベースに保存する。
    private void saveWord() {
        try {
            String eng = txtEng.getText().toString();
            String jpn = txtJpn.getText().toString();
            boolean done = chkDone.isChecked();
            Word word = new Word(mWordId, eng, jpn ,done);

            WordsRepository.getInstance().save(word);

            showToast(mHandler, this.getApplicationContext(), R.string.msg_saved);
            finish();

        } catch (InvalidKeyException e) {
            e.printStackTrace();
            showErrorDialog(e, this);
        }
    }

    // 削除ボタンが押された時の処理
    private final View.OnClickListener mBtnDeleteClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 処理開始前に「削除してよろしいですか？」の確認ダイアログを表示する。
            CommonHelper.showOkCancelDialog(v.getContext(), getString(R.string.msg_confirm_delete),
                new Runnable() {
                    @Override
                    public void run() {
                        deleteWord();
                    }
                });
        }
    };

    // 単語をデータベースから削除する。
    private void deleteWord() {
        try {
            WordsRepository.getInstance().delete(mWordId);
            showToast(mHandler, this, R.string.msg_deleted);
            backToList();
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog(e, this);
        }
    }

    // 単語一覧画面に戻る。
    private void backToList(){
        // 削除後、一つ前の画面ではなく単語一覧画面に直接戻るためにここではfinish()ではなく
        // FLAG_ACTIVITY_CLEAR_TOP フラグ付きのインテントを使って画面遷移する。
        // 　参考：https://qiita.com/naoty_k/items/f733ff8a69a141331bba
        Intent intent = new Intent(getApplicationContext(), WordListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }


    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* TextWatcherをimplementするたのダミー。下の処理のために必要。 */ }
    @Override public void onTextChanged(CharSequence s, int start, int before, int count) { /* TextWatcherをimplementするたのダミー。下の処理のために必要。 */ }

    @Override
    public void afterTextChanged(Editable s) {
        // テキストボックスの内容が変更される度に保存ボタンの有効化条件をチェックして更新する。
        enableSaveButton();
    }
}