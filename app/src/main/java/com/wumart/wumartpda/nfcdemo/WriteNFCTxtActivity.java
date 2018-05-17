package com.wumart.wumartpda.nfcdemo;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Auth: Sunshine
 * Desc:
 * Time: 2018/5/16.
 */
public class WriteNFCTxtActivity extends BaseNFCActivity {
    private String mText = "NFC-NewText-123";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mText == null || mText.equals("")) {
            return;
        }
        Tag nfcTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (nfcTag != null) {
            NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{createRecordTxt(mText)});
            boolean result = writeTag(ndefMessage, nfcTag);
            if (result) {
                Toast.makeText(this, "数据写入成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "数据写入失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "获取NFC标签失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 创建NdefRecord数据
     */
    private NdefRecord createRecordTxt(String mText) {
        byte[] langBytes = Locale.CHINA.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Charset utfEncoding = Charset.forName("UTF-8");
        //将文本转换为UTF-8格式
        byte[] textBytes = mText.getBytes(utfEncoding);
        //设置状态字节编码最高位数为0
        int utfBit = 0;
        //定义状态字节
        char status = (char) (utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        //设置第一个状态字节，先将状态码转换成字节
        data[0] = (byte) status;
        //设置语言编码，使用数组拷贝方法，从0开始拷贝到data中，拷贝到data的1到langBytes.length的位置
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        //设置文本字节，使用数组拷贝方法，从0开始拷贝到data中，拷贝到data的1 + langBytes.length
        //到textBytes.length的位置
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
        //通过字节传入NdefRecord对象
        //NdefRecord.RTD_TEXT：传入类型 读写
        NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT, new byte[0], data);
        return ndefRecord;
    }

    /**
     * 写入数据
     */
    private boolean writeTag(NdefMessage ndefMessage, Tag nfcTag) {
        try {
            Ndef ndef = Ndef.get(nfcTag);
            ndef.connect();
            ndef.writeNdefMessage(ndefMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}