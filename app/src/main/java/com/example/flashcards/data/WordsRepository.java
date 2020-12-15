package com.example.flashcards.data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.flashcards.MyApplication;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

public class WordsRepository {

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

        // tryの括弧内でCursorを生成することで自動的にcloseされる。
        // 　参考：https://mslgt.hatenablog.com/entry/2018/04/05/073400
        try(final Cursor cursor = mDb.rawQuery("SELECT * FROM words ORDER BY _id", null)){
            while (cursor.moveToNext()){
                final Word word = buildWordFromCursor(cursor);
                list.add(word);
            }
        }

        return list;
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
        try (final Cursor cursor = mDb.rawQuery("SELECT * FROM words WHERE _id = ?", args)) {
            // 主キーで絞っているため結果は1行か0行かのどちらかなのでwhileでループする必要はない。
            if (cursor.moveToFirst()){
                word = buildWordFromCursor(cursor);
            }
        }

        return word;
    }

    private Word buildWordFromCursor(Cursor c) {
        int id = c.getInt(c.getColumnIndex("_id"));
        String eng = c.getString(c.getColumnIndex("english"));
        String jpn = c.getString(c.getColumnIndex("japanese"));
        int done = c.getInt(c.getColumnIndex("done"));
        final Word word = new Word(id, eng, jpn, intToBoolean(done));
        return word;
    }

    // idで指定された単語のdoneフラグを更新する。
    public void updateDone(int id, boolean done) throws InvalidKeyException, SQLException {
        Word word = getById(id);
        if (word == null) {
            throw new InvalidKeyException("");
        }

        String sql = "UPDATE words SET done = ? WHERE _id = ? ";
        String [] args = { boolToString(word.done), Integer.toString(id)};
        mDb.execSQL(sql, args);
    }

    // 単語を保存する。idが0の場合は新規追加、0以外の場合は更新処理を行う。
    public void save(Word word) throws InvalidKeyException, SQLException {
        if (word._id == 0) {
            String sql = "INSERT INTO words (english, japanese, done) VALUES (?, ?, ?) ";
            String[] args = { word.english, word.japanese, word.done ? "1" : "0" };
            mDb.execSQL(sql, args);
            return;
        }

        Word existing = getById(word._id);
        if (existing == null) {
            throw new InvalidKeyException("");
        }

        String sql = "UPDATE words SET english = ?, japanese = ?, done =? WHERE _id = ? ";
        String[] args = { word.english, word.japanese, word.done ? "1" : "0", Integer.toString(word._id) };
        mDb.execSQL(sql, args);
    }

    // idで指定された単語を削除する。単語が見つからない場合は何もしない。
    public void delete(int id) throws SQLException {
        Word existing = getById(id);
        if (existing == null) return;

        String sql = "DELETE FROM words WHERE _id = ? ";
        String [] args = { Integer.toString(id)};
        mDb.execSQL(sql, args);
    }

}
