package com.wumart.wumartpda.nfcdemo;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;

/**
 * 1.子类需要在onCreate方法中做Activity初始化。
 * 2.子类需要在onNewIntent方法中进行NFC标签相关操作。
 * 当launchMode设置为singleTop时，第一次运行调用onCreate方法，
 * 第二次运行将不会创建新的Activity实例，将调用onNewIntent方法
 * 所以我们获取intent传递过来的Tag数据操作放在onNewIntent方法中执行
 * 如果在栈中已经有该Activity的实例，就重用该实例(会调用实例的onNewIntent())
 * 只要NFC标签靠近就执行
 */
public abstract class BaseNFCActivity extends AppCompatActivity {
    private NfcAdapter mNfcAdapter;
    private PendingIntent pendingIntent;

    @Override
    protected void onStart() {
        super.onStart();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //一旦截获NFC消息，就会通过PendingIntent调用窗口
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }
}