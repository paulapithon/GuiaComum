package com.gov.guia.guiacomumdorecife.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gov.guia.guiacomumdorecife.GuiaComumApplication;
import com.gov.guia.guiacomumdorecife.R;
import com.gov.guia.guiacomumdorecife.model.BtnMapa;
import com.gov.guia.guiacomumdorecife.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MapaActivity extends AppCompatActivity {

    @BindView(R.id.btns_mapa)
    RelativeLayout mBotoesMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        ButterKnife.bind(this);

        setupDatabase();
    }

    private void setupDatabase() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child(Constants.DATABASE_MAPA).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                BtnMapa btn = dataSnapshot.getValue(BtnMapa.class);
                criarBtnMapa(btn, dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {  }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }

    private void criarBtnMapa (final BtnMapa btn, final String index) {

        //TODO criar pasta com dimensões
        //Seta os parametros do layout
        RelativeLayout.LayoutParams parametros = new RelativeLayout.LayoutParams(200,200);
        parametros.setMargins(
                (int)(mBotoesMapa.getWidth() * btn.getX()),
                (int)(mBotoesMapa.getHeight() * btn.getY()),
                0,
                0
        );

        //Cria botao programaticamente
        ImageButton mapaBtn = new ImageButton(this);
        Glide.with(this).load(btn.getIcone()).into(mapaBtn);
        mapaBtn.setLayoutParams(parametros);
        mapaBtn.setBackgroundColor(Color.TRANSPARENT);
        mapaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO adicionar esse código dentro do diálogo
                Intent intent = new Intent(MapaActivity.this, LivrosActivity.class);
                intent.putExtra(Constants.DATABASE_INDEX, index);
                startActivity(intent);
            }
        });
        mBotoesMapa.addView(mapaBtn);
        //Adiciona na lista da aplicação
        GuiaComumApplication.addToHashMap(index, btn);

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
