package com.jonnymariani.identificacaoinsetos;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class  ApplicationHelper {

    public static Intent PreparaNavegador(String url){
        Uri uri = Uri.parse(url);

        Intent defaultBrowser = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER);
        defaultBrowser.setData(uri);
        return defaultBrowser;

    }

    public static Intent AbreWikipedia(String nome) throws UnsupportedEncodingException {

        String nomeEncoded = URLEncoder.encode(nome, StandardCharsets.UTF_8.toString());
        String url = "https://pt.wikipedia.org/wiki/Special:Search?search=" + nomeEncoded;

        Intent defaultBrowser = ApplicationHelper.PreparaNavegador(url);

        return defaultBrowser;
    }


}
