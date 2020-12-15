package com.example.flashcards.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.flashcards.MyApplication;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

public class WordsRepository {

    private static WordsRepository sInstance = new WordsRepository();

    private WordsRepository() {}

    public static WordsRepository getInstance() {
        return sInstance;
    }

    private SQLiteDatabase getDb() {
        return MyApplication.getInstance().getDb();
    }

    public List<Word> getList() {
        ArrayList<Word> list = new ArrayList<>();

        final Cursor cursor = getDb().rawQuery("SELECT * FROM words ORDER BY _id", null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String eng = cursor.getString(cursor.getColumnIndex("english"));
            String jpn = cursor.getString(cursor.getColumnIndex("japanese"));
            int done = cursor.getInt(cursor.getColumnIndex("done"));
            final Word word = new Word(id, eng, jpn, done != 0);
            list.add(word);
        }
        cursor.close();

        return list;
    }

    public Word getById(int id) {
        if (id == 0){
            return new Word(0, "", "");
        }

        Word word = null;
        String[] args = { Integer.toString(id) };
        final Cursor cursor = getDb().rawQuery("SELECT * FROM words WHERE _id = ?", args);
        if (cursor.moveToNext()){
            String eng = cursor.getString(cursor.getColumnIndex("english"));
            String jpn = cursor.getString(cursor.getColumnIndex("japanese"));
            int done = cursor.getInt(cursor.getColumnIndex("done"));
            word = new Word(id, eng, jpn, done != 0);
        }
        cursor.close();

        return word;
    }

    public void updateDone(int id, boolean done) throws InvalidKeyException {
        Word word = getById(id);
        if (word == null) {
            throw new InvalidKeyException("");
        }

        String sql = "UPDATE words SET done = ? WHERE _id = ? ";
        String [] args = { done ? "1" : "0", Integer.toString(id)};
        getDb().execSQL(sql, args);
    }

    public void save(Word word) throws InvalidKeyException {
        if (word._id == 0) {
            String sql = "INSERT INTO words (english, japanese, done) VALUES (?, ?, ?) ";
            String[] args = { word.english, word.japanese, word.done ? "1" : "0" };
            getDb().execSQL(sql, args);
            return;
        }

        Word existing = getById(word._id);
        if (existing == null) {
            throw new InvalidKeyException("");
        }

        String sql = "UPDATE words SET english = ?, japanese = ?, done =? WHERE _id = ? ";
        String[] args = { word.english, word.japanese, word.done ? "1" : "0", Integer.toString(word._id) };
        getDb().execSQL(sql, args);
    }

    public void delete(int id) throws Exception {
        Word existing = getById(id);
        if (existing == null) return;

        String sql = "DELETE FROM words WHERE _id = ? ";
        String [] args = { Integer.toString(id)};
        getDb().execSQL(sql, args);
    }

}
