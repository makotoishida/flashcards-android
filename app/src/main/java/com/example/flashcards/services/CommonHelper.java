package com.example.flashcards.services;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class CommonHelper {

    // Toastを表示する。
    // 　参考：https://developer.android.com/guide/topics/ui/notifiers/toasts?hl=ja
    public static void showToast(final Handler handler, final Context context, final int msgId) {
        handler.post(() -> {
            String msg = context.getString(msgId);
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        });
    }

    // Snackbarを表示する。
    // 　参考：https://developer.android.com/training/snackbar?hl=ja
    public static void showSnackbar(final Handler handler, final View view, final int msgId) {
        handler.post(() -> Snackbar.make(view, msgId, Snackbar.LENGTH_LONG).show());
    }

    // 例外の内容をダイアログで表示する。
    public static void showErrorDialog(final Exception e, final Context context) {
        new AlertDialog.Builder(context)
                .setMessage(e.getMessage())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    // OK/Cancelダイアログを表示する。
    public static void showOkCancelDialog(final Context context, final String msg, final Runnable callback) {
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        callback.run();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }


}
