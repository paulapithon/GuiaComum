package com.guia.guiacomumdorecife.view.guia;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.guia.guiacomumdorecife.GuiaComumApplication;
import com.guia.guiacomumdorecife.R;
import com.guia.guiacomumdorecife.model.Mapa;
import com.guia.guiacomumdorecife.util.Constants;
import com.guia.guiacomumdorecife.view.custom.PopInterpolator;

import java.util.HashMap;

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
    @BindView(R.id.loading_bar)
    ProgressBar mLoadingBar;
    @BindView(R.id.loading_image)
    ImageView mLoadingImage;

    private Mapa currentBtn;
    private boolean isLoaded;
    private HashMap<Mapa, ImageButton> btnsAtuais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        ButterKnife.bind(this);

        setUI();
    }

    private void setUI () {

        mLoadingBar.setVisibility(View.VISIBLE);
        mLoadingImage.setVisibility(View.VISIBLE);

        mMapaPopup.bringToFront();
        mMapaPopup.setAlpha(0f);
        mMapaPopup.setVisibility(View.GONE);

        btnsAtuais = new HashMap<>();

        //Eleva dialog apenas se for uma versão suportada
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mElevation.setElevation(5f);
            mElevation.setTranslationZ(5f);
        }

        isLoaded = true;

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        setupDatabase();
    }

    private void setupDatabase() {

        FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_MAPA)
                .addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (isLoaded) {
                    Mapa btn = dataSnapshot.getValue(Mapa.class);

                    //Adiciona na lista da aplicação
                    if (!GuiaComumApplication.getsBotoesMapa().containsKey(dataSnapshot.getKey())) {
                        GuiaComumApplication.getsBotoesMapa().put(dataSnapshot.getKey(), btn);
                    } else {
                        btn = GuiaComumApplication.getsBotoesMapa().get(dataSnapshot.getKey());
                    }
                    criarBtnMapa(btn, dataSnapshot.getKey());

                    mLoadingBar.setVisibility(View.GONE);
                    mLoadingImage.setVisibility(View.GONE);
                }
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

        if (!btnsAtuais.containsKey(btn)) {
            //Cria botao programaticamente
            final ImageButton mapaBtn = new ImageButton(this);
            Glide.with(this)
                    .load(btn.getIcone())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            animarBtn(mapaBtn);
                            return false;
                        }
                    })
                    .into(mapaBtn);

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
            btnsAtuais.put(btn, mapaBtn);
        }

    }

    private void animarBtn (ImageButton mapaBtn) {
        //Seta interpolator para botão quicar
        Animation expandIn = AnimationUtils.loadAnimation(this, R.anim.pop_out);
        PopInterpolator interpolator = new PopInterpolator(0.2, 20);
        expandIn.setInterpolator(interpolator);
        expandIn.setFillAfter(true);
        mapaBtn.startAnimation(expandIn);
    }

    private void criarDialogo (Mapa btn) {

        //Verificar se é botão atual para sumir com dialog
        if (btn.equals(currentBtn)) {
            mMapaPopup.animate().alpha(0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mMapaPopup.setVisibility(View.GONE);
                }
            });
            currentBtn = null;
        } else {
            mMapaPopup.setLayoutParams(getParametrosLayout(btn, true));
            mMapaPopup.setAlpha(0f);
            mMapaPopup.setVisibility(View.VISIBLE);
            mMapaPopup.animate().alpha(1f).setDuration(200).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mMapaPopup.setVisibility(View.VISIBLE);
                }
            });
            currentBtn = btn;
        }

        //Setar dialog com texto e listeners de botão atual
        mTituloDialogo.setText(btn.getNome().toUpperCase());
        mBtnDialogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapaActivity.this, LivrosActivity.class);
                startActivity(intent);
                mMapaPopup.animate().alpha(0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mMapaPopup.setVisibility(View.GONE);
                    }
                });
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
            width -= 0.065;
            if (height > 0.5) { height -= 0.15; }
            else { height += 0.07; }

            size = (int) Math.ceil(210 * logicalDensity);
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

    @OnClick(R.id.btn_voltar)
    public void onBack () {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isLoaded = false;
    }
}
