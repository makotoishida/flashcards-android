package com.example.flashcards.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.InvalidKeyException;
import java.util.List;

import static com.example.flashcards.data.WordsRepository.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    // データーベース名
    static final String DATABASE_NAME = "words.db";

    // データベースのバージョン (カラムの追加などで変更を加えた場合にカウントアップする)
    static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * テーブルの作成 & 初期データの投入
     */
    @Override
    public void onCreate( SQLiteDatabase db ) {
        // テーブルを作成
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + COL_ID                + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL "
                + ", " + COL_ENGLISH    + " TEXT NOT NULL "
                + ", " + COL_JAPANESE   + " TEXT NOT NULL "
                + ", " + COL_DONE       + " INTEGER NOT NULL DEFAULT 0 "
                + ", " + COL_SORTORDER  + " INTEGER NOT NULL DEFAULT 0 "
                + ");"
        );

        // ここで初期データ挿入可能

    }


    /**
     * データベースのバージョンアップ時の処理
     */
    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
        if( oldVersion == 1 && newVersion == 2 ){

            // 表示順を保持するためのカラムを追加。
            db.execSQL("ALTER TABLE " + TABLE_NAME
                + " ADD COLUMN " + COL_SORTORDER + " INTEGER NOT NULL DEFAULT 0");

            // 既存レコードがあれば表示順を昇順にセットして更新。
            WordsRepository repository = new WordsRepository(db);
            List<Word> list = repository.getList();
            int count = 0;
            for (Word w: list) {
                try {
                    w.sort_order = count++;
                    repository.save(w);
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
