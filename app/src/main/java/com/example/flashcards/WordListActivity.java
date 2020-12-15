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

import java.util.ArrayList;
import java.util.List;

/**
 * 単語一覧画面
 */
public class WordListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Word> mDataset = new ArrayList<>();
    ArrayAdapter<Word> adapter;
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
        adapter = new MyAdapter(this, R.layout.list_row, mDataset);
        adapter.setNotifyOnChange(true);
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
        loadWordList();
    }

    // 単語データの配列をデータベースから取得。
    private void loadWordList() {
        mDataset = (ArrayList<Word>) mRepository.getList();

        adapter.clear();
        adapter.addAll(mDataset);
        adapter.notifyDataSetChanged();
        adapter.notifyDataSetInvalidated();

        txtCount.setText(String.format("%d", mDataset.size()));
    }

    // 一覧リストの行がタップされた時の処理
    private AdapterView.OnItemClickListener mOnItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Word word = mDataset.get(position);
            Intent intent = new Intent(getApplicationContext(), WordViewActivity.class);
            intent.putExtra("_id", word._id);
            startActivity(intent);
        }
    };

    // 追加ボタンがタップされた時の処理
    private View.OnClickListener mBtnAddOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), WordEditActivity.class);
            intent.putExtra("_id", 0);
            startActivity(intent);
        }
    };

    // 一覧リスト表示用アダプタ
    public static class MyAdapter extends ArrayAdapter<Word> {
        private final List<Word> mDataset;
        private LayoutInflater inflater;
        private int itemLayout;

        public MyAdapter(@NonNull Context context, int layoutResourceId, @NonNull List<Word> data) {
            super(context, layoutResourceId, data);
            this.inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.itemLayout = layoutResourceId;
            this.mDataset = data;
        }


        @Override
        public int getCount() {
            return mDataset.size();
        }

        @Override
        public Word getItem(int position) {
            return mDataset.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mDataset.get(position)._id;
        }

        // 各行内の要素への参照を保持しておくための入れ物となるクラス。
        class ViewHolder {
            TextView txtEng;
            TextView txtDone;
        }

        @Override
        public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                // 行のViewが新規に生成された場合はtxtEng, txtDoneへの参照をViewHolderに入れておく。
                convertView = inflater.inflate(itemLayout, parent, false);
                holder = new ViewHolder();
                holder.txtEng = convertView.findViewById(R.id.txtRowText);
                holder.txtDone = convertView.findViewById(R.id.txtDone);
                convertView.setTag(holder);
            } else {
                // 行のViewが生成済みの場合はViewHolderを取得する。
                holder = (ViewHolder) convertView.getTag();
            }

            // 現在の行に英語とDoneフラグの値を表示する。
            Word word = getItem(position);
            if(word != null){
                holder.txtEng.setText(word.english);
                holder.txtDone.setText(word.getDoneString());
            }
            return convertView;
        }
    }
}