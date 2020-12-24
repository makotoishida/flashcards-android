package com.example.flashcards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.flashcards.data.Word;
import com.example.flashcards.data.WordsRepository;
import com.example.flashcards.services.CommonHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 単語一覧画面
 */
public class WordListActivity extends AppCompatActivity {

    private ListView listView;
    private Button btnAdd;
    private TextView txtCount;

    // データベースに接続されたリポジトリクラスのインスタンスを取得して保持しておく。
    private WordsRepository mRepository = MyApplication.getInstance().getWordsRepository();

    // Activityが新規に生成された時の処理
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        WordListViewAdapter adapter = new WordListViewAdapter(this, R.layout.list_row);
        listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(mOnItemClick);

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(mBtnAddOnClick);

        txtCount = findViewById(R.id.txtCount);
    }

    // Activityが新規に生成された、または別画面から戻って来た時の処理
    @Override
    protected void onResume() {
        super.onResume();

        // データベースから全単語を取得して一覧に表示する。
        loadWordList();
    }

    // 単語データの配列をデータベースから取得してリストに表示する。
    private void loadWordList() {
        List<Word> list = (ArrayList<Word>) mRepository.getList();

        WordListViewAdapter adapter = (WordListViewAdapter)listView.getAdapter();
        adapter.clear();
        adapter.addAll(list);

        txtCount.setText(String.format("%d", list.size()));

        // 単語が一つも登録されていない場合、
        if (list.size() == 0) {
            // 「新しい単語を追加しますか？」ダイアログを表示する。
            CommonHelper.showOkCancelDialog(this, getString(R.string.msg_add_word),
                    () -> startNewWordActivity());
        }
    }

    // リストの行がタップされた時の処理
    private AdapterView.OnItemClickListener mOnItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Word word = (Word)listView.getItemAtPosition(position);
            Intent intent = new Intent(getApplicationContext(), WordViewActivity.class);
            intent.putExtra("_id", word._id);
            startActivity(intent);
        }
    };

    // 追加ボタンがタップされた時の処理
    private View.OnClickListener mBtnAddOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startNewWordActivity();
        }
    };

    private void startNewWordActivity() {
        Intent intent = new Intent(getApplicationContext(), WordEditActivity.class);
        intent.putExtra("_id", 0);
        startActivity(intent);
    }

}