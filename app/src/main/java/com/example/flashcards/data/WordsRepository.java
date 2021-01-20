package com.example.flashcards.data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.flashcards.MyApplication;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

public class WordsRepository {
    private final static String TAG = "WordsRepository";

    public final static String TABLE_NAME = "words";
    public final static String COL_ID = "_id";
    public final static String COL_ENGLISH = "english";
    public final static String COL_JAPANESE = "japanese";
    public final static String COL_DONE = "done";
    public final static String COL_SORTORDER = "sort_order";

    // SQLiteデータベースへの接続を保持するインスタンス。
    SQLiteDatabase mDb;

    // リポジトリ生成時に必ずSQLiteデータベースへの接続を受け取る。
    public WordsRepository(SQLiteDatabase db) {
        mDb = db;
    }

    // intからbooleanに変換する。
    public static boolean intToBoolean(int n) {
        return n != 0;
    }

    // booleanからintに変換する。
    public static int boolToInteger(boolean b) {
        return b ? 1 : 0;
    }

    // booleanからStringに変換する。
    public static String boolToString(boolean b) {
        return Integer.toString(boolToInteger(b));
    }

    // 全ての単語を返す。
    public List<Word> getList() {
        ArrayList<Word> list = new ArrayList<>();

        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_SORTORDER + ", " + COL_ID;

        // tryの括弧内でCursorを生成することで自動的にcloseされる。
        // 　参考：https://mslgt.hatenablog.com/entry/2018/04/05/073400
        try(final Cursor cursor = mDb.rawQuery(sql, null)){
            while (cursor.moveToNext()){
                final Word word = buildWordFromCursor(cursor);
                list.add(word);
            }
        }

        return list;
    }

    // sort_orderの最大値を返す。
    public int getMaxSortOrder() {
        int maxValue = 0;
        String sql = "SELECT MAX(sort_order) AS max_sort_order FROM " + TABLE_NAME;
        try(final Cursor cursor = mDb.rawQuery(sql, null)){
            if (cursor.moveToFirst()){
                maxValue = cursor.getInt(0);
            }
        }
        return maxValue;
    }

    // idで指定された単語を返す。idが0の場合は新規インスタンスを生成して返す。idが見つからない場合はnullを返す。
    public Word getById(int id) {
        if (id == 0){
            return new Word(0, "", "");
        }

        Word word = null;
        String[] args = { Integer.toString(id) };

        // tryの括弧内でCursorを生成することで自動的にcloseされる。
        // 　参考：https://mslgt.hatenablog.com/entry/2018/04/05/073400
        try (final Cursor cursor = mDb.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ID + " = ?", args)) {
            // 主キーで絞っているため結果は1行か0行かのどちらかなのでwhileでループする必要はない。
            if (cursor.moveToFirst()){
                word = buildWordFromCursor(cursor);
            }
        }

        return word;
    }

    private Word buildWordFromCursor(Cursor c) {
        int id = c.getInt(c.getColumnIndex(COL_ID));
        String eng = c.getString(c.getColumnIndex(COL_ENGLISH));
        String jpn = c.getString(c.getColumnIndex(COL_JAPANESE));
        int done = c.getInt(c.getColumnIndex(COL_DONE));
        int sort_order = c.getInt(c.getColumnIndex(COL_SORTORDER));

        final Word word = new Word(id, eng, jpn, intToBoolean(done), sort_order);
        return word;
    }

    // idで指定された単語のdoneフラグを更新する。
    public void updateDone(int id, boolean done) throws InvalidKeyException, SQLException {
        Word word = getById(id);
        if (word == null) {
            throw new InvalidKeyException("");
        }

        String sql = "UPDATE " + TABLE_NAME + " SET " + COL_DONE + " = ? WHERE (" + COL_ID + " = ?) ";
        String [] args = { boolToString(done), Integer.toString(id)};
        mDb.execSQL(sql, args);
    }

    public void updateSortOrder(List<Word> list, int fromPosition, int toPosition) throws InvalidKeyException {

        Word moving = list.get(fromPosition);

        mDb.beginTransaction();
        try {
            if (fromPosition < toPosition) {
                // 下方向に移動された場合
                int n = list.get(fromPosition).sort_order;
                for (int i = fromPosition + 1; i <= toPosition; i++) {
                    Word w = list.get(i);
                    w.sort_order = n++;
                    Log.d(TAG, String.format("Updating: %s, %s, %d", w.english, w.japanese, w.sort_order));
                    save(w);
                }
                moving.sort_order = n;

            } else if (fromPosition > toPosition) {
                // 上方向に移動された場合
                int n = list.get(toPosition).sort_order;
                moving.sort_order = n++;
                for (int i = toPosition; i < fromPosition; i++) {
                    Word w = list.get(i);
                    w.sort_order = n++;
                    Log.d(TAG, String.format("Updating: %s, %s, %d", w.english, w.japanese, w.sort_order));
                    save(w);
                }
            }

            Log.d(TAG, String.format("Moving: %s, %s, %d", moving.english, moving.japanese, moving.sort_order));
            save(moving);

            mDb.setTransactionSuccessful();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            mDb.endTransaction();
        }
    }

    // 単語を保存する。idが0の場合は新規追加、0以外の場合は更新処理を行う。
    public void save(Word word) throws InvalidKeyException, SQLException {
        if (word._id == 0) {
            int newSortOrder = getMaxSortOrder() + 1;

            String sql = "INSERT INTO " + TABLE_NAME + " ("
                    + COL_ENGLISH + ", "
                    + COL_JAPANESE + ", "
                    + COL_DONE + ", "
                    + COL_SORTORDER
                    + ") VALUES (?, ?, ?, ?) ";
            String[] args = { word.english, word.japanese, boolToString(word.done), Integer.toString(newSortOrder) };
            mDb.execSQL(sql, args);
            return;
        }

        Word existing = getById(word._id);
        if (existing == null) {
            throw new InvalidKeyException("");
        }

        String sql = "UPDATE " + TABLE_NAME + " SET "
                + COL_ENGLISH + " = ?, "
                + COL_JAPANESE + " = ?, "
                + COL_DONE + " = ?, "
                + COL_SORTORDER + " = ? "
                + " WHERE (" + COL_ID + " = ?) ";
        String[] args = { word.english, word.japanese, boolToString(word.done), Integer.toString(word.sort_order)
                        , Integer.toString(word._id) };
        mDb.execSQL(sql, args);
    }

    // idで指定された単語を削除する。単語が見つからない場合は何もしない。
    public void delete(int id) throws SQLException {
        Word existing = getById(id);
        if (existing == null) return;

        String sql = "DELETE FROM " + TABLE_NAME + " WHERE (" + COL_ID + " = ?) ";
        String [] args = { Integer.toString(id)};
        mDb.execSQL(sql, args);
    }

}
