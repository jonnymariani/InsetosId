package com.jonnymariani.identificacaoinsetos;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class HistoricoAdapter extends BaseAdapter {


    private Context context;
    private Cursor c;
    String root = Environment.getExternalStorageDirectory().toString() + "/imagens_id_insetos";

    public HistoricoAdapter(Context _context, Cursor _c) {
        this.context = _context;
        this.c = _c;
    }

    public int getCount() {
        return c.getCount();
    }

    public Object getItem(int position) {
        c.moveToPosition(position);
        return c;
    }

    public long getItemId(int position) {
        c.moveToPosition(position);

        int indexId = c.getColumnIndex("_id");
        long id = c.getLong(indexId);
        return id;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        c.moveToPosition(position);

        int indexNome = c.getColumnIndex("nome");
        String strNome = c.getString(indexNome);

        // Cria uma instância do layout XML para os objetos correspondentes na View

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row_historico, null);


        TextView nome = (TextView) view.findViewById(R.id.txtRowNomeInseto);
        nome.setText(strNome);

        // Imagem
        ImageView img = (ImageView) view.findViewById(R.id.imgInseto);


        File imgFile = new File(root + File.separator + strNome + ".jpg");

        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            img.setImageBitmap(bitmap);
        }

        return view;
    }
}
