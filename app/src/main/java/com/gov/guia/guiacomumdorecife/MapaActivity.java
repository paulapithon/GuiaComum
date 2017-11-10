package com.gov.guia.guiacomumdorecife;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MapaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        ButterKnife.bind(this);
    }

    //TODO create imagebuttons programatically
    @OnClick(R.id.btn_0)
    public void onLivro0 () {
        startActivity(new Intent(this, LivrosActivity.class));
    }

    @OnClick(R.id.btn_1)
    public void onLivro1 () {
        startActivity(new Intent(this, LivrosActivity.class));
    }

    @OnClick(R.id.btn_2)
    public void onLivro2 () {
        startActivity(new Intent(this, LivrosActivity.class));
    }

    @OnClick(R.id.btn_3)
    public void onLivro3 () {
        startActivity(new Intent(this, LivrosActivity.class));
    }

    @OnClick(R.id.btn_4)
    public void onLivro4 () {
        startActivity(new Intent(this, LivrosActivity.class));
    }

    @OnClick(R.id.btn_5)
    public void onLivro5 () {
        startActivity(new Intent(this, LivrosActivity.class));
    }

    @OnClick(R.id.btn_6)
    public void onLivro6 () {
        startActivity(new Intent(this, LivrosActivity.class));
    }

    @OnClick(R.id.btn_7)
    public void onLivro7 () {
        startActivity(new Intent(this, LivrosActivity.class));
    }

    @OnClick(R.id.btn_8)
    public void onLivro8 () {
        startActivity(new Intent(this, LivrosActivity.class));
    }

    @OnClick(R.id.btn_9)
    public void onLivro9 () {
        startActivity(new Intent(this, LivrosActivity.class));
    }

    @OnClick(R.id.btn_10)
    public void onLivro10 () {
        startActivity(new Intent(this, LivrosActivity.class));
    }

    @OnClick(R.id.btn_11)
    public void onLivro11 () {
        startActivity(new Intent(this, LivrosActivity.class));
    }


    @OnClick(R.id.btn_voltar)
    public void onBack () {
        onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
