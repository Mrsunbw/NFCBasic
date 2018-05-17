package com.wumart.wumartpda.nfcdemo;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends BaseNFCActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private boolean checkNfcAdapter() {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "此设备不支持NFC", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, "此设备未开启NFC", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void openNFCRunAppAct(View view) {
        if (checkNfcAdapter()) {
            startActivity(new Intent(this, NFCRunAppActivity.class));
        }
    }

    public void openNFCRunMSNAct(View view) {
        if (checkNfcAdapter()) {
            startActivity(new Intent(this, NFCRunMSNActivity.class));
        }
    }

    public void openReadNFCTxtAct(View view) {
        if (checkNfcAdapter()) {
            startActivity(new Intent(this, ReadNFCTxtActivity.class));
        }
    }

    public void openWriteNFCTxtAct(View view) {
        if (checkNfcAdapter()) {
            startActivity(new Intent(this, WriteNFCTxtActivity.class));
        }
    }

    public void openReadNFCUriAct(View view) {
        if (checkNfcAdapter()) {
            startActivity(new Intent(this, ReadNFCUriActivity.class));
        }
    }

    public void openWriteNFCUriAct(View view) {
        if (checkNfcAdapter()) {
            startActivity(new Intent(this, WriteNFCUriActivity.class));
        }
    }

    public void openReadNFCMUAct(View view) {
        if (checkNfcAdapter()) {
            startActivity(new Intent(this, ReadNFCMUActivity.class));
        }
    }

    public void openWriteNFCMUAct(View view) {
        if (checkNfcAdapter()) {
            startActivity(new Intent(this, WriteNFCMUActivity.class));
        }
    }
}