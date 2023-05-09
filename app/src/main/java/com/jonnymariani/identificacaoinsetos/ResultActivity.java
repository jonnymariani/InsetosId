package com.jonnymariani.identificacaoinsetos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

public class ResultActivity extends AppCompatActivity {

    private ImageView imgResult;
    private TextView txtResult;
    private Button btnWikipedia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        imgResult = (ImageView) findViewById(R.id.img_Result);
        txtResult = (TextView)findViewById(R.id.txt_Result);
        btnWikipedia = (Button) findViewById(R.id.btnWikipedia);

        Bundle extras = getIntent().getExtras();
        String response = extras.getString("result");

        ObjectMapper mapper = new ObjectMapper();
        InsectApiResponse responseAPI = null;

        try {
            responseAPI = mapper.readValue(response, InsectApiResponse.class);

            String nome = responseAPI.getResult().getClassification().getSuggestions().get(0).name;

            txtResult.setText(nome);

            btnWikipedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browser = null;
                    try {
                        browser = ApplicationHelper.AbreWikipedia(nome);
                        startActivity(browser);
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            //Carrega imagem do resultado na view
            String imgUrl = responseAPI.getResult().getClassification().getSuggestions().get(0).getSimilar_images().get(0).url;

            Picasso.get().load(imgUrl).into(imgResult);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}