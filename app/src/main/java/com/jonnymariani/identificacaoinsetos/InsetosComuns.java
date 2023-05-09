package com.jonnymariani.identificacaoinsetos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InsetosComuns {

    private List<Inseto> lista = new ArrayList<Inseto>();
    private Context contexto;

    public InsetosComuns(Context _cx) {
        this.contexto = _cx;
    }

    public List<Inseto> getLista() {

        AdicionaInsetos();
        return lista;
    }

    private void AdicionaInsetos()
    {
        this.lista.add(CriaInseto("Escorpião", R.drawable.escorpiao));
        this.lista.add(CriaInseto("Aranha", R.drawable.aranha));
    }

    private Inseto CriaInseto(String nome, int idImg)
    {
        Inseto inseto = new Inseto();
        inseto.setNome(nome);

        Bitmap img = BitmapFactory.decodeResource(contexto.getResources(), idImg);
        inseto.setBitmap(img);

        return inseto;
    }

}

class Inseto {

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    String nome;
    Bitmap bitmap;





}