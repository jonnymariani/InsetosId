package com.jonnymariani.identificacaoinsetos;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    Switch switchCfgAbrirCamera;
    Button limparHistorico;

    long idConfigAbrirCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);



        switchCfgAbrirCamera = (Switch) findViewById(R.id.swtCfgAbrirCamera);
        limparHistorico = (Button) findViewById(R.id.btnLimparHistorico);

        DBAdapter conexaoDB = new DBAdapter(SettingActivity.this);
        conexaoDB.open();
        Cursor c =  conexaoDB.BuscaConfig("ABRIR_NA_CAMERA");
        conexaoDB.close();

        int indexParam = c.getColumnIndex("param");
        int indexValue = c.getColumnIndex("value");

        String configParam = c.getString(indexParam);
        String configValue = c.getString(indexValue);
        long configId = c.getLong(0);

        idConfigAbrirCamera = configId;



        boolean AbrirCamera = configValue.equals("TRUE");

        switchCfgAbrirCamera.setChecked(AbrirCamera);


        switchCfgAbrirCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean checked = switchCfgAbrirCamera.isChecked();

                conexaoDB.open();
                conexaoDB.atualizaConfig(idConfigAbrirCamera, "ABRIR_NA_CAMERA", checked ? "TRUE" : "FALSE" );
                conexaoDB.close();

            }
        });

        limparHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conexaoDB.open();
                conexaoDB.limparHistorico();
                conexaoDB.close();

                Toast.makeText(SettingActivity.this, getResources().getString(R.string.cfgLimparHistoricoSucesso), Toast.LENGTH_SHORT).show();
            }
        });
    }
}