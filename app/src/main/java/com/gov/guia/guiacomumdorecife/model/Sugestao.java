package com.gov.guia.guiacomumdorecife.model;

/**
 * Created by Paula Pithon on 12/11/2017.
 */

public class Sugestao {

    private String nome;
    private String email;
    private String descricao;

    private String imagem;
    private String audio;
    private String local;

    public Sugestao (String nome, String email, String descricao, String imagem, String audio, String local) {
        this.nome = nome;
        this.email = email;
        this.descricao = descricao;
        this.imagem = imagem;
        this.audio = audio;
        this.local = local;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getEmail() {
        return email;
    }

    public String getImagem() {
        return imagem;
    }

    public String getAudio() {
        return audio;
    }

    public String getLocal() {
        return local;
    }
}
