package com.jonnymariani.identificacaoinsetos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ComunsActivity extends AppCompatActivity {

    ListView listaEstados;
    List<Inseto> insetos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comuns);

        InsetosComuns insetosComuns = new InsetosComuns(this);
        insetos= insetosComuns.getLista();
        listaEstados = (ListView) findViewById(R.id.listviewComuns);
        listaEstados.setAdapter(new ComunsAdapter(getApplicationContext(), insetos));

        listaEstados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Inseto inseto = insetos.get(position);

                try {
                    Intent defaultBrowser = ApplicationHelper.AbreWikipedia(inseto.getNome());
                    startActivity(defaultBrowser);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }
}