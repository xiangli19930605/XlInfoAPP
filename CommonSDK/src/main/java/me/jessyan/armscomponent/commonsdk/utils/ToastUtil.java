package me.jessyan.armscomponent.commonsdk.utils;

import android.content.Context;
import android.widget.Toast;

import me.jessyan.armscomponent.commonsdk.app.MyApplication;


/**
 * Created by hasee on 2016/12/19.
 */

public class ToastUtil {

    private static String oldMsg;
    protected static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;
    public static int TPYE_SUCCESS = 1;
    public static int TPYE_FAILURE = 0;



    public static void showToast(Context context, String s) {
        if (toast == null) {
            toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }

        oneTime = twoTime;
    }

    public static void showToast(String s) {
        if (toast == null) {
            toast = Toast.makeText(MyApplication.getInstance(), s, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }
        oldMsg = s;
        oneTime = twoTime;
    }



}
