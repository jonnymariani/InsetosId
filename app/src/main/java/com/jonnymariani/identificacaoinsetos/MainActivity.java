package com.jonnymariani.identificacaoinsetos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;

//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.navigation.ui.AppBarConfiguration;
//import androidx.navigation.ui.NavigationUI;

import com.jonnymariani.identificacaoinsetos.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class MainActivity extends AppCompatActivity {

//    private AppBarConfiguration appBarConfiguration;

    private static final int CAMERA_ACTION_CODE = 1;
    ActivityResultLauncher<Intent> activityResultLauncherCamera, activityResultLauncherGallery;
    private RequestQueue mRequestQueue;
    private JsonObjectRequest mJsonRequest;
    private String url = "https://insect.mlapi.ai/api/v1/identification";
    private  String apiKey = "KFKOOt9krolSPrGsUdFM8WVxQkkb79I14RFW70G18ho4qmutJP";
    private String base64Img;
    ListView listViewHistorico;
    private ActivityMainBinding binding;
    String root = Environment.getExternalStorageDirectory().toString() + "/imagens_id_insetos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        listViewHistorico = (ListView) findViewById(R.id.listViewHistorico);
        listViewHistorico.setOnItemClickListener(listViewClickListener);
        DBAdapter conexaoDB = new DBAdapter(MainActivity.this);
        conexaoDB.open();

        Cursor c =  conexaoDB.listarHistorico();
        listViewHistorico.setAdapter(new HistoricoAdapter(getApplicationContext(), c));
        conexaoDB.close();


        activityResultLauncherCamera =  registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null){
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
//                    imgResult.setImageBitmap(bitmap);
                    base64Img = encodeImage(bitmap);

                    try {
                        BuscarNaApi();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        activityResultLauncherGallery =  registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == RESULT_OK && result.getData() != null){

                    Uri selectedImage = result.getData().getData();

                    final InputStream imageStream;

                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    final Bitmap bitmap = BitmapFactory.decodeStream(imageStream);

                    base64Img = encodeImage(bitmap);

//                    imgResult.setImageURI(selectedImage);

                    try {
                        BuscarNaApi();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }

            }
        });


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CharSequence[] options = {getResources().getString(R.string.tirarFoto) , getResources().getString(R.string.galeria) , getResources().getString(R.string.cancelar) };
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals(getResources().getString(R.string.tirarFoto)))
                        {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            if (cameraIntent.resolveActivity(getPackageManager()) != null)
                            {
                                activityResultLauncherCamera.launch(cameraIntent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.erroCameraNaoEncontrada), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if (options[item].equals(getResources().getString(R.string.galeria)))
                        {
                            Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            activityResultLauncherGallery.launch(intent);
                        }
                        else if (options[item].equals(getResources().getString(R.string.cancelar))) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });


        conexaoDB.open();
        Cursor c2 =  conexaoDB.BuscaConfig("ABRIR_NA_CAMERA");
        conexaoDB.close();

        int indexValue = c2.getColumnIndex("value");
        String configValue = c2.getString(indexValue);

        boolean AbrirCamera = configValue.equals("TRUE");

        if (AbrirCamera){
            binding.fab.performClick();
        }

    }

    @Override
    protected void onResume(){
        //sempre que executar onResume, irá fazer uma busca no banco de dados
        //e vai atualizar a tela de exibição dos livros cadastrados
        super.onResume();
        new ObtemHistorico().execute();
    }

    private class ObtemHistorico extends AsyncTask<Object, Object, Cursor> {
        DBAdapter conexaoDB = new DBAdapter(MainActivity.this);
        @Override
        protected Cursor doInBackground(Object... params){
            conexaoDB.open(); //abre a base de dados
            return conexaoDB.listarHistorico(); //retorna todos
        }
        // usa o cursor retornado pelo doInBackground
        @Override
        protected void onPostExecute(Cursor result){
            listViewHistorico.setAdapter(new HistoricoAdapter(getApplicationContext(), result));
            conexaoDB.close();
        }
    }

    AdapterView.OnItemClickListener listViewClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {

            DBAdapter conexaoDB = new DBAdapter(MainActivity.this);
            conexaoDB.open();
            Cursor c = conexaoDB.BuscaHistoricoPorID(id);
            conexaoDB.close();

            int index = c.getColumnIndex("nome");
            String nome = c.getString(index);

            try {
                Intent defaultBrowser = ApplicationHelper.AbreWikipedia(nome);
                startActivity(defaultBrowser);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    };

    private void BuscarNaApi() throws JSONException {

        binding.fab.setEnabled(false);
        ProgressDialog dialogLoading = ProgressDialog.show(MainActivity.this, "",
                getResources().getString(R.string.carregando), true);

        mRequestQueue = Volley.newRequestQueue(this);

        Map<String, String[]> params = new HashMap<String, String[]>();
        params.put("images", new String[]{base64Img});

        JSONObject json = new JSONObject(params);
        json.put("similar_images", Boolean.TRUE);

        mJsonRequest = new JsonObjectRequest(Request.Method.POST,
                url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("JSONPost", response.toString());

                        ObjectMapper mapper = new ObjectMapper();
                        DBAdapter conexaoDB = new DBAdapter(MainActivity.this);

                        InsectApiResponse responseAPI = null;
                        try {
                            //Pega resultado da API
                            responseAPI = mapper.readValue(response.toString(), InsectApiResponse.class);
                            String imgUrl = responseAPI.getResult().getClassification().getSuggestions().get(0).getSimilar_images().get(0).url;
                            String nome = responseAPI.getResult().getClassification().getSuggestions().get(0).name;

                            conexaoDB.open();
                            //Busca se já existe nome no historico
                            Cursor cursor = conexaoDB.BuscaHistoricoPorNome(nome);

                            //Se não existe adiciona
                            if (!cursor.moveToFirst() || cursor.getCount() == 0){
                                SalvarImagem(imgUrl, nome);
                                conexaoDB.insereHistorico(nome, Long.toString(System.currentTimeMillis()));
                            }
                            else {
                                //Se existe, atualiza a data
                                cursor.moveToFirst();
                                int indexNome = cursor.getColumnIndex("nome");
                                int indexId = cursor.getColumnIndex("_id");

                                String nomeAtual = cursor.getString(indexNome);
                                Long idAtual = cursor.getLong(indexId);

                                conexaoDB.atualizaHistorico(idAtual, nomeAtual, Long.toString(System.currentTimeMillis()));
                            }


                            conexaoDB.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        Intent intencao = new Intent(getApplicationContext(), ResultActivity.class);
                        intencao.putExtra("result", response.toString());

                        startActivity(intencao);

                        binding.fab.setEnabled(true);

                        dialogLoading.dismiss();

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("JSONPost", "Error: " + error.getMessage());
                //pDialog.hide();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                //add params <key,value>
                headers.put("Api-Key", apiKey);

                return headers;
            }

        };

        mRequestQueue.add(mJsonRequest);
    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d("Base64", "BASE64: " + encImage);
        return encImage;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intencao = new Intent(this, SettingActivity.class);
            startActivity(intencao);
        }

        if(id == R.id.action_comuns)
        {
            Intent intencao = new Intent(this, ComunsActivity.class);
            startActivity(intencao);
        }

        return super.onOptionsItemSelected(item);
    }

    private void SalvarImagem(String url,  String nome) throws IOException {

        //Permite usar web na thread principal, nesse caso como está durante o loading não tem problema
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bitmap bitmap = null;
        InputStream inputStream;
        //Cria bitmap a partir da url
        try {
            inputStream = new java.net.URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File myDir = new File(root);

        //Se a pasta não existir, cria
        if (!myDir.exists()) {
            boolean mkdir = myDir.mkdirs();
        }

        //Salva imagem
        String fname = nome + ".jpg";
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        File f = new File(root + File.separator + fname);
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();


    }

}