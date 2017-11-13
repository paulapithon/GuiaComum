package com.gov.guia.guiacomumdorecife.view.guia;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gov.guia.guiacomumdorecife.R;
import com.gov.guia.guiacomumdorecife.model.Livro;
import com.gov.guia.guiacomumdorecife.util.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Paula Pithon on 10/11/2017.
 */

public class LivrosAdapter extends ArrayAdapter<Livro> {

    @BindView(R.id.titulo_livro)
    TextView mTituloLivro;
    @BindView(R.id.btn_livro)
    ImageButton mBtnLivro;

    private List<Livro> livros;
    private Context contexto;
    private int recurso;

    public LivrosAdapter(@NonNull Context context, int resource, @NonNull List<Livro> objects) {
        super(context, resource, objects);
        contexto = context;
        recurso = resource;
        livros = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = ((LivrosActivity)contexto).getLayoutInflater().inflate(recurso, null);
            ButterKnife.bind(this, convertView);
        }

        mTituloLivro.setText(livros.get(position).getNome().toUpperCase());
        mBtnLivro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(contexto, ConteudoActivity.class);
                intent.putExtra(Constants.DATABASE_LIVRO_INDEX, livros.get(position));
                contexto.startActivity(intent);
            }
        });

        return convertView;
    }

}
