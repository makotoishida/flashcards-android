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

public class WordListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Word> mDataset = new ArrayList<Word>();
    ArrayAdapter<Word> adapter;
    private Button btnAdd;
    private TextView txtCount;

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

    @Override
    protected void onResume() {
        super.onResume();
        loadWordList();
    }

    private void loadWordList() {
        mDataset = (ArrayList<Word>) WordsRepository.getInstance().getList();

        adapter.clear();
        adapter.addAll(mDataset);
        adapter.notifyDataSetChanged();
        adapter.notifyDataSetInvalidated();

        txtCount.setText(String.format("%d", mDataset.size()));
    }

    private AdapterView.OnItemClickListener mOnItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Word word = mDataset.get(position);
            Intent intent = new Intent(getApplicationContext(), WordViewActivity.class);
            intent.putExtra("_id", word._id);
            startActivity(intent);
        }
    };

    private View.OnClickListener mBtnAddOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), WordEditActivity.class);
            intent.putExtra("_id", 0);
            startActivity(intent);
        }
    };


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

        class ViewHolder {
            TextView txtEng;
            TextView txtDone;
        }

        @Override
        public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(itemLayout, parent, false);
                holder = new ViewHolder();
                holder.txtEng = convertView.findViewById(R.id.txtRowText);
                holder.txtDone = convertView.findViewById(R.id.txtDone);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Word word = getItem(position);
            if(word != null){
                holder.txtEng.setText(word.english);
                holder.txtDone.setText(word.getDoneString());
            }
            return convertView;
        }
    }
}