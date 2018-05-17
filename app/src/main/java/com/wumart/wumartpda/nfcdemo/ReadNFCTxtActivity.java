package com.wumart.wumartpda.nfcdemo;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Auth: Sunshine
 * Desc:
 * Time: 2018/5/16.
 */
public class ReadNFCTxtActivity extends BaseNFCActivity {
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
        //1.获取Tag对象
        Tag nfcTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        //2.获取Ndef的实例
        Ndef ndef = Ndef.get(nfcTag);
        if (ndef != null) {
            mTagText = ndef.getType() + "\nmaxsize:" + ndef.getMaxSize() + "bytes\n\n";
            readNfcTag(intent);
            nfcTxtTv.setText(mTagText);
        } else {
            Toast.makeText(this, "获取NFC标签失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 读取NFC标签文本数据
     */
    private void readNfcTag(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] ndefMsgs = null;
            int contentSize = 0;
            if (rawMsgs != null) {
                ndefMsgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    ndefMsgs[i] = (NdefMessage) rawMsgs[i];
                    contentSize += ndefMsgs[i].toByteArray().length;
                }
            }
            try {
                if (ndefMsgs != null) {
                    NdefRecord record = ndefMsgs[0].getRecords()[0];
                    String textRecord = parseTextRecord(record);
                    mTagText += textRecord + "\n\ntext\n" + contentSize + " bytes";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解析NDEF文本数据，从第三个字节开始，后面的文本数据
     */
    private String parseTextRecord(NdefRecord ndefRecord) {
        //判断数据是否为NDEF格式
        //判断TNF
        if (ndefRecord.getTnf() != NdefRecord.TNF_WELL_KNOWN) {
            return null;
        }
        //判断可变的长度的类型
        if (!Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
            return null;
        }
        try {
            //获得字节数组，然后进行分析
            byte[] payload = ndefRecord.getPayload();
            //下面开始NDEF文本数据第一个字节，状态字节
            //判断文本是基于UTF-8还是UTF-16的，取第一个字节"位与"上16进制的80，16进制的80也就是最高位是1，
            //其他位都是0，所以进行"位与"运算后就会保留最高位
            String textEncoding = ((payload[0] & 0x80) == 0) ? "UTF-8" : "UTF-16";
            //3f最高两位是0，第六位是1，所以进行"位与"运算后获得第六位
            int languageCodeLength = payload[0] & 0x3f;
            //下面开始NDEF文本数据第二个字节，语言编码
            //获得语言编码
            String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            //下面开始NDEF文本数据后面的字节，解析出文本
            String textRecord = new String(payload, languageCodeLength + 1,
                    payload.length - languageCodeLength - 1, textEncoding);
            return textRecord;
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}