package com.wumart.wumartpda.nfcdemo;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Auth: Sunshine
 * Desc:
 * Time: 2018/5/16.
 */
public class ReadNFCMUActivity extends BaseNFCActivity {
    private TextView nfcTxtTv;
    private String mTagText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        nfcTxtTv = findViewById(R.id.tv_nfc_txt);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String[] techList = tag.getTechList();
        if (techList.length > 0) {
            boolean haveMifareUltralight = false;
            for (String tech : techList) {
                if (tech.indexOf("MifareUltralight") >= 0) {
                    haveMifareUltralight = true;
                    break;
                }
            }
            if (!haveMifareUltralight) {
                Toast.makeText(this, "不支持MifareUltralight数据格式", Toast.LENGTH_SHORT).show();
                return;
            }
            mTagText = readTag(tag);
            if (!TextUtils.isEmpty(mTagText)) {
                nfcTxtTv.setText(mTagText);
            }
        }
    }

    private String readTag(Tag tag) {
        MifareUltralight mifareUltralight = MifareUltralight.get(tag);
        if (mifareUltralight == null) {
            return null;
        }
        try {
            mifareUltralight.connect();
            byte[] data = mifareUltralight.readPages(4);
            return new String(data, Charset.forName("GB2312"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                mifareUltralight.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}