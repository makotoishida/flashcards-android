package com.example.flashcards;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.flashcards.data.DatabaseHelper;
import com.example.flashcards.data.WordsRepository;

public class MyApplication extends Application {

    // SQLiteデータベースへの接続を保持するインスタンス変数。
    private DatabaseHelper mDbHelper = null;

    // 自分自身のインスタンスを保持するスタティック変数。
    private static MyApplication sInstance;

    // アプリケーションのどこからでも参照可能なように自分自身のインスタンスへの参照を返す。
    // 　これによって MyApplication.getInstance().getWordsRepositoy().getList() のようにDBアクセスが可能になる。
    public static MyApplication getInstance() {
        return sInstance;
    }

    // アプリケーション開始時に一度だけ呼ばれる処理。
    @Override
    public void onCreate() {
        super.onCreate();

        // 自分自身への参照をスタティック変数として保持しておく。
        sInstance = this;
    }

    // アプリケーションのプロセスが終了する時に呼ばれる処理。
    @Override
    public void onTerminate() {
        // アプリケーション終了時にデータベース接続を閉じる。
        mDbHelper.close();
        mDbHelper = null;

        super.onTerminate();
    }

    // データベースへの接続を返す。
    private SQLiteDatabase getDb() {
        if (mDbHelper == null) {
            // まだインスタンスが生成されていない場合のみ新規インスタンスを生成。
            mDbHelper = new DatabaseHelper(this);
        }

        // 未Openの場合はOpenし、Open済みの場合は既存の接続を返す。
        return mDbHelper.getWritableDatabase();
    }

    // Wordsリポジトリクラスのインスタンスを返す。
    // 　アプリケーションのどこからでも MyApplication.getInstance().getWordsRepositoy().getList() のようにDBアクセスが可能。
    public WordsRepository getWordsRepository() {
        return new WordsRepository(getDb());
    }

}
