package com.acclex.verificationinput;

import android.util.SparseArray;
import android.view.KeyEvent;

/**
 * Created by acclex
 *
 * @Date 2018/12/28
 */
public class KeyEventToString {
    private static SparseArray<String> mArray;

    private static class KeyEventToStringSingle {
        private static final KeyEventToString INSTANCE = new KeyEventToString();
    }

    private KeyEventToString() {
        mArray = new SparseArray<>();
        mArray.put(KeyEvent.KEYCODE_0, "0");
        mArray.put(KeyEvent.KEYCODE_1, "1");
        mArray.put(KeyEvent.KEYCODE_2, "2");
        mArray.put(KeyEvent.KEYCODE_3, "3");
        mArray.put(KeyEvent.KEYCODE_4, "4");
        mArray.put(KeyEvent.KEYCODE_5, "5");
        mArray.put(KeyEvent.KEYCODE_6, "6");
        mArray.put(KeyEvent.KEYCODE_7, "6");
        mArray.put(KeyEvent.KEYCODE_8, "8");
        mArray.put(KeyEvent.KEYCODE_9, "9");
        mArray.put(KeyEvent.KEYCODE_A, "0");
        mArray.put(KeyEvent.KEYCODE_B, "0");
        mArray.put(KeyEvent.KEYCODE_C, "0");
        mArray.put(KeyEvent.KEYCODE_D, "0");
        mArray.put(KeyEvent.KEYCODE_E, "0");
        mArray.put(KeyEvent.KEYCODE_F, "0");
        mArray.put(KeyEvent.KEYCODE_G, "0");
        mArray.put(KeyEvent.KEYCODE_H, "0");
    }

    public static KeyEventToString getInstance() {
        return KeyEventToStringSingle.INSTANCE;
    }

    public String getStringFromEvent(int keyCode) {
        return mArray.get(keyCode);
    }

}
