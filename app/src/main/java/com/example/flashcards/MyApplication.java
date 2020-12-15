package com.example.flashcards;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.flashcards.data.DatabaseHelper;
import com.example.flashcards.data.WordsRepository;

public class MyApplication extends Application {

    private DatabaseHelper mDbHelper = null;

    private static MyApplication sInstance;

    public static MyApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    @Override
    public void onTerminate() {
        // アプリケーションのプロセスが終了するときにデータベース接続を閉じる。
        mDbHelper.close();
        mDbHelper = null;

        super.onTerminate();
    }

    // データベースへの接続を返す。（未Openの場合はOpenし、Open済みの場合は既存の接続を返す。）
    private SQLiteDatabase getDb() {
        if (mDbHelper == null) {
            mDbHelper = new DatabaseHelper(this);
        }
        return mDbHelper.getWritableDatabase();
    }

    // Wordsリポジトリクラスのインスタンスを返す。
    public WordsRepository getWordsRepository() {
        return new WordsRepository(getDb());
    }

}
