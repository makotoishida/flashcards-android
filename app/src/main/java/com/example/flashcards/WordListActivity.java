package com.example.flashcards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.flashcards.data.Word;
import com.example.flashcards.data.WordsRepository;
import com.example.flashcards.services.CommonHelper;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

/**
 * 単語一覧画面
 */
public class WordListActivity extends AppCompatActivity {
    private final static String TAG = "WordListActivity";

    private RecyclerView listView;
    private Button btnAdd;
    private TextView txtCount;

    // データベースに接続されたリポジトリクラスのインスタンスを取得して保持しておく。
    private WordsRepository mRepository = MyApplication.getInstance().getWordsRepository();

    // Activityが新規に生成された時の処理
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        listView = findViewById(R.id.list);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setHasFixedSize(true);
        listView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));  // 区切り線を表示
        listView.setAdapter(mAdapter);

        ItemTouchHelper itemTouchHelper  = new ItemTouchHelper(new MyCallback());
        itemTouchHelper.attachToRecyclerView(listView);

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
        adapter.updateDataset(list);

        txtCount.setText(String.format("%d", list.size()));

        // 単語が一つも登録されていない場合、
        if (list.size() == 0) {
            // 「新しい単語を追加しますか？」ダイアログを表示する。
            CommonHelper.showOkCancelDialog(this, getString(R.string.msg_add_word),
                    () -> startNewWordActivity());
        }
    }

    private WordListViewAdapter mAdapter = new WordListViewAdapter(){
        // 行がタップされたときの処理を指定。
        @Override
        public void onItemClick(Word word) {
            super.onItemClick(word);

            Intent intent = new Intent(getApplicationContext(), WordViewActivity.class);
            intent.putExtra("_id", word._id);
            startActivity(intent);
        }

        @Override
        public void onItemMoved(int fromPosition, int toPosition) {
            super.onItemMoved(fromPosition, toPosition);

            Log.d(TAG, String.format("Moved from %d, To %d", fromPosition, toPosition));

            try {
                mRepository.updateSortOrder(mDataset, fromPosition, toPosition);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }

            mDataset.add(toPosition, mDataset.remove(fromPosition));
            this.notifyItemMoved(fromPosition, toPosition);
        }
    };

    // 追加ボタンがタップされた時の処理
    private View.OnClickListener mBtnAddOnClick = v -> startNewWordActivity();

    private void startNewWordActivity() {
        Intent intent = new Intent(getApplicationContext(), WordEditActivity.class);
        intent.putExtra("_id", 0);
        startActivity(intent);
    }

    private class MyCallback extends ItemTouchHelper.SimpleCallback {
        public MyCallback() {
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.ACTION_STATE_IDLE);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            final int fromPos = viewHolder.getAdapterPosition();
            final int toPos = target.getAdapterPosition();
            mAdapter.onItemMoved(fromPos, toPos);
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        }
    }

}