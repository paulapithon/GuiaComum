package com.gov.guia.guiacomumdorecife.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gov.guia.guiacomumdorecife.R;

import butterknife.OnClick;

public class CreditosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditos);
    }

    @OnClick(R.id.btn_voltar)
    public void onBack () {
        onBackPressed();
    }
}
