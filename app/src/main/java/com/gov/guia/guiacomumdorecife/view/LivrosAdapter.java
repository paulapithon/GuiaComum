package com.gov.guia.guiacomumdorecife.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gov.guia.guiacomumdorecife.R;
import com.gov.guia.guiacomumdorecife.model.Livro;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Paula Pithon on 10/11/2017.
 */

public class LivrosAdapter extends ArrayAdapter<Livro> {

    @BindView(R.id.titulo_livro)
    TextView mTituloLivro;

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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = ((LivrosActivity)contexto).getLayoutInflater().inflate(recurso, null);
            ButterKnife.bind(this, convertView);
        }

        mTituloLivro.setText(livros.get(position).getNome());

        return convertView;
    }
}
