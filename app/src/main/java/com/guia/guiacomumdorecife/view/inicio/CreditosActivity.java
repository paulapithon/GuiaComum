package com.guia.guiacomumdorecife.view.inicio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.guia.guiacomumdorecife.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreditosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditos);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_voltar)
    public void onBack () {
        onBackPressed();
    }
}
