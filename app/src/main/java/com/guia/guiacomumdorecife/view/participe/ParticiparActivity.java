package com.guia.guiacomumdorecife.view.participe;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.guia.guiacomumdorecife.R;
import com.guia.guiacomumdorecife.util.Constants;
import com.guia.guiacomumdorecife.util.ParticiparService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParticiparActivity extends AppCompatActivity {

    final private int SELECIONAR_FOTO = 123;
    final private int SELECIONAR_AUDIO = 321;
    final private int SELECIONAR_LOCAL = 444;
    final private int AGUARDAR_PERMISSAO = 554;

    @BindView(R.id.edit_nome)
    EditText mEditNome;
    @BindView(R.id.edit_email)
    EditText mEditEmail;
    @BindView(R.id.edit_descricao)
    EditText mEditDescricao;
    @BindView(R.id.termos_aceitar)
    CheckBox mCheckBox;
    @BindView(R.id.btn_local_atual)
    ImageButton mLocalAtualBtn;
    @BindView(R.id.btn_gravar_audio)
    ImageButton mGravarAudioBtn;
    @BindView(R.id.btn_tirar_foto)
    ImageButton mTirarFotoBtn;

    private Uri imageUri;
    private Uri audioUri;
    private boolean hasLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participar);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_local_atual)
    public void onLocalAtual() {
        pedirPermissao(true);
    }

    @OnClick(R.id.btn_tirar_foto)
    public void onTirarFoto () {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECIONAR_FOTO);
    }

    @OnClick(R.id.btn_gravar_audio)
    public void onGravarAudio () {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECIONAR_AUDIO);
    }

    private void pedirPermissao (boolean isLocal) {
        if (isLocal) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        SELECIONAR_LOCAL);

            } else {
                hasLocation = true;
                mLocalAtualBtn.setColorFilter(getResources().getColor(R.color.rosa_escuro), PorterDuff.Mode.SRC_ATOP);
            }
        } else {
            if ((imageUri != null || audioUri != null) &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        AGUARDAR_PERMISSAO
                );
            } else {
                salvarArquivos();
            }
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(resultCode == RESULT_OK) {
            if (requestCode == SELECIONAR_FOTO) {
                imageUri = intent.getData();
                mTirarFotoBtn.setColorFilter(getResources().getColor(R.color.rosa_escuro), PorterDuff.Mode.SRC_ATOP);
            } else if (requestCode == SELECIONAR_AUDIO) {
                audioUri = intent.getData();
                mGravarAudioBtn.setColorFilter(getResources().getColor(R.color.rosa_escuro), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) { return; }
        }

        if (requestCode == SELECIONAR_LOCAL) {
            mLocalAtualBtn.setColorFilter(getResources().getColor(R.color.rosa_escuro), PorterDuff.Mode.SRC_ATOP);
        }
        else if (requestCode == AGUARDAR_PERMISSAO) { salvarArquivos(); }

    }

    private void salvarArquivos() {
        if (mCheckBox.isChecked() &&
                !mEditNome.getText().toString().equals("") &&
                !mEditEmail.getText().toString().equals("")) {

            Intent intent = new Intent(this, ParticiparService.class);

            Bundle args = new Bundle();
            args.putString(Constants.SERVICO_NOME, mEditNome.getText().toString());
            args.putString(Constants.SERVICO_EMAIL, mEditEmail.getText().toString());
            if (!mEditDescricao.getText().toString().equals("")) {
                args.putString(Constants.SERVICO_DESCRICAO, mEditDescricao.getText().toString());
            }
            if (audioUri!= null) { args.putString(Constants.SERVICO_AUDIO, audioUri.toString()); }
            if (audioUri != null) { args.putString(Constants.SERVICO_IMAGEM, imageUri.toString()); }
            args.putBoolean(Constants.SERVICO_LOCALIZACAO, hasLocation);

            intent.putExtra(Constants.SERVICO_BUNDLE, args);
            startService(intent);
        }
        finish();
    }

    @OnClick(R.id.btn_compartilhar)
    public void onAceitar () {
        pedirPermissao(false);
    }

    @OnClick(R.id.termos_ver)
    public void onTermos () {
        startActivity(new Intent(this, TermosActivity.class));
    }

    @OnClick(R.id.btn_voltar)
    public void onBack () {
        onBackPressed();
    }

}
