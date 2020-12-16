package com.example.flashcards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashcards.data.Word;
import com.example.flashcards.data.WordsRepository;

import java.security.InvalidKeyException;
import java.time.Duration;

/**
 * 単語確認画面
 */
public class WordViewActivity extends AppCompatActivity {

    private int mWordId;
    private TextView txtEng;
    private TextView txtJpn;
    private TextView txtDone;
    private Button btnDone;
    private Button btnUndone;

    // データベースに接続されたリポジトリクラスのインスタンスを取得して保持しておく。
    private WordsRepository mRepository = MyApplication.getInstance().getWordsRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_view);

        // 画面上部のActionBarの左端に「←（戻る）」アイコンを表示する。
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.word_view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mnu_edit) {
            startEditActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 「編集」ボタンが押された時の処理
    private void startEditActivity(){
        // 単語IDをインテントに入れて単語編集画面を開く。
        Intent intent = new Intent(getApplicationContext(), WordEditActivity.class);
        intent.putExtra("_id", mWordId);
        startActivity(intent);
    }

    // 指定された単語のデータをデータベースから取得して表示する。
    private void loadWord(int id) {
        Word word = mRepository.getById(id);
        showWord(word);
    }

    // 単語を表示する。
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

    // 「覚えた」ボタンが押された時の処理
    private View.OnClickListener mBtnDoneClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateDone(true);
        }
    };

    // 「忘れた」ボタンが押された時の処理
    private View.OnClickListener mBtnUndoneClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateDone(false);
        }
    };

    private void updateDone(boolean done) {
        try {
            // データベースのdoneフラグを更新する。
            mRepository.updateDone(mWordId, done);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.msg_error, Toast.LENGTH_LONG).show();
        }

        // データベースから最新の情報を取得して画面表示を更新する。
        loadWord(mWordId);
    }

}