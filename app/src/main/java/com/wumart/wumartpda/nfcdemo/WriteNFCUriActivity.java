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

/**
 * Auth: Sunshine
 * Desc:
 * Time: 2018/5/16.
 */
public class WriteNFCUriActivity extends BaseNFCActivity {
    private String mUri = "http://www.baidu.com";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag nfcTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Ndef ndef = Ndef.get(nfcTag);
        if (ndef != null) {
            NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{createUriRecord(mUri)});
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

    private NdefRecord createUriRecord(String mUri) {
        if (mUri == null || mUri.equals("")) {
            return null;
        }
        byte prefix = 0;
        for (Byte b : UriPrefix.URI_PREFIX_MAP.keySet()) {
            String prefixStr = UriPrefix.URI_PREFIX_MAP.get(b).toLowerCase();
            if (prefixStr.equals("")) {
                continue;
            }
            if (mUri.toLowerCase().startsWith(prefixStr)) {
                prefix = b;
                mUri = mUri.substring(prefixStr.length());
                break;
            }
        }
        byte[] data = new byte[1 + mUri.length()];
        data[0] = prefix;
        System.arraycopy(mUri, 0, data, 1, mUri.length());
        NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_URI, new byte[0], data);
        return ndefRecord;
    }

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