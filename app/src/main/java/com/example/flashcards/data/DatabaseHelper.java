package com.example.flashcards.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // データーベース名
    static final String DATABASE_NAME = "words.db";

    // データベースのバージョン (カラムの追加などで、変更を加えた場合にカウントアップする)
    static final int DATABASE_VERSION = 1;

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
            "CREATE TABLE IF NOT EXISTS words ("
                + "_id          INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL"
                + ", english    TEXT NOT NULL"
                + ", japanese   TEXT NOT NULL"
                + ", done       INTEGER NOT NULL"
                + ");"
        );

        // ここで、初期データ挿入可能


    }


    /**
     * データベースのバージョンアップ時の処理
     */
    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
//        if( oldVersion == 1 && newVersion == 2 ){
//            db.execSQL("");
//        }
    }


}
