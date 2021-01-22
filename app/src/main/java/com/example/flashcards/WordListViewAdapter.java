package com.example.flashcards;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import com.example.flashcards.data.Word;


// 一覧リスト表示用アダプタ
public class WordListViewAdapter extends RecyclerView.Adapter<WordListViewAdapter.ViewHolder> {
    private static final String TAG = "WordListViewAdapter";
    protected List<Word> mDataset = new ArrayList<Word>(){};

    public void updateDataset(List<Word> newDataset) {
        mDataset.clear();
        mDataset.addAll(newDataset);
        notifyDataSetChanged();

        // デバッグ出力。
        for (int i = 0; i < mDataset.size(); i++) {
            Word w = mDataset.get(i);
            Log.d(TAG, String.format("%s, %s, %d", w.english, w.japanese, w.sort_order ));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Word word = mDataset.get(position);
        holder.txtEng.setText(word.english);
        holder.txtDone.setText(word.getDoneString());

        holder.itemView.setOnClickListener(v -> onItemClick(word));
    }

    @Override
    public long getItemId(int position) {
        return mDataset.get(position)._id;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // 各行内の要素への参照を保持しておくための入れ物となるクラス。
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtEng;
        TextView txtDone;

        public ViewHolder(@NonNull View v) {
            super(v);
            txtEng = v.findViewById(R.id.txtRowText);
            txtDone = v.findViewById(R.id.txtDone);
        }
    }

    // タップされたときの処理が必要な場合はこのメソッドをオーバーライドすること。
    public void onItemClick(Word word) {
    }

    // 並び替えされたときの処理（e.g. データベースの更新など）がさらに必要な場合はこのメソッドをオーバーライドすること。
    public void onItemMoved(int fromPosition, int toPosition) {

        // 内部的に持っているリストの順序を変更する。
        mDataset.add(toPosition, mDataset.remove(fromPosition));

        // リスト内のアイテムが移動されたことをRecyclerViewに通知する。
        notifyItemMoved(fromPosition, toPosition);
    }

}
