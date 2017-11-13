package com.gov.guia.guiacomumdorecife.view.participe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gov.guia.guiacomumdorecife.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TermosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termos);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_voltar)
    public void onBack () {
        onBackPressed();
    }

}
