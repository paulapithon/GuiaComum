package com.gov.guia.guiacomumdorecife.view.guia;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.gov.guia.guiacomumdorecife.GuiaComumApplication;
import com.gov.guia.guiacomumdorecife.R;
import com.gov.guia.guiacomumdorecife.model.Mapa;
import com.gov.guia.guiacomumdorecife.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapaActivity extends AppCompatActivity {

    @BindView(R.id.btns_mapa)
    RelativeLayout mBotoesMapa;
    @BindView(R.id.mapa_popup)
    View mMapaPopup;
    @BindView(R.id.dialog_titulo)
    TextView mTituloDialogo;
    @BindView(R.id.dialogo_btn)
    ImageButton mBtnDialogo;
    @BindView(R.id.elevation_dialog)
    View mElevation;

    private Mapa currentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        ButterKnife.bind(this);

        setupDatabase();
        setUI();
    }

    private void setupDatabase() {

        FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_MAPA)
                .addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Mapa btn = dataSnapshot.getValue(Mapa.class);
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

    private void criarBtnMapa (final Mapa btn, final String index) {

        //Cria botao programaticamente
        ImageButton mapaBtn = new ImageButton(this);
        Glide.with(this).load(btn.getIcone()).into(mapaBtn);
        mapaBtn.setLayoutParams(getParametrosLayout(btn, false));
        mapaBtn.setBackgroundColor(Color.TRANSPARENT);
        mapaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarDialogo(btn);
                GuiaComumApplication.setsCurrentMap(index);
            }
        });
        mBotoesMapa.addView(mapaBtn);

        //Adiciona na lista da aplicação
        if (!GuiaComumApplication.getsBotoesMapa().containsKey(index)) {
            GuiaComumApplication.getsBotoesMapa().put(index, btn);
        }

    }

    private void criarDialogo (Mapa btn) {

        //Verificar se é botão atual para sumir com dialog
        if (btn.equals(currentBtn)) {
            mMapaPopup.setVisibility(View.GONE);
            currentBtn = null;
        } else {
            mMapaPopup.setLayoutParams(getParametrosLayout(btn, true));
            mMapaPopup.setVisibility(View.VISIBLE);
            currentBtn = btn;
        }

        //Setar dialog com texto e listeners de botão atual
        mTituloDialogo.setText(btn.getNome().toUpperCase());
        mBtnDialogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapaActivity.this, LivrosActivity.class);
                startActivity(intent);
            }
        });

    }

    private RelativeLayout.LayoutParams getParametrosLayout (Mapa btn, boolean isDialog) {

        //Tamanho da tela em dps
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float logicalDensity = metrics.density;

        double width = btn.getX();
        double height = btn.getY();
        int size = (int) Math.ceil(70 * logicalDensity);
        if (isDialog) {
            width -= 0.06;
            height -= 0.15;
            size = (int) Math.ceil(200 * logicalDensity);
        }

        RelativeLayout.LayoutParams parametros = new RelativeLayout.LayoutParams(size, size);
        parametros.setMargins(
                (int)(mBotoesMapa.getWidth() * width),
                (int)(mBotoesMapa.getHeight() * height),
                0,
                0
        );
        return parametros;

    }

    private void setUI () {
        mMapaPopup.bringToFront();

        //Eleva dialog apenas se for uma versão suportada
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mElevation.setElevation(5f);
            mElevation.setTranslationZ(5f);
        }
    }

    @OnClick(R.id.btn_voltar)
    public void onBack () {
        onBackPressed();
    }

}
