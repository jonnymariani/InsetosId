package com.jonnymariani.identificacaoinsetos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

// Adapter utilizado para exibir as informações dos Estados no ListView.

public class ComunsAdapter extends BaseAdapter {
	private Context context;
	private List<Inseto> insetoList;

	public ComunsAdapter(Context _context, List<Inseto> _insetoList){
		this.context = _context;
		this.insetoList = _insetoList;
	}        
	
	public int getCount() {        
			return insetoList.size();
	}    
    
	public Object getItem(int position) {
		return insetoList.get(position);
	}     
    
	public long getItemId(int position) {        
		return position;   
	}     
  
	public View getView(int position, View convertView, ViewGroup parent) {
		// Recupera o estado da posição atual
		Inseto inseto = insetoList.get(position);
		LayoutInflater inflater = (LayoutInflater) 	context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.list_row_historico, null);
		

		TextView nome = (TextView)view.findViewById(R.id.txtRowNomeInseto);
		nome.setText(inseto.getNome());

		ImageView img = (ImageView)view.findViewById(R.id.imgInseto);
		img.setImageBitmap(inseto.getBitmap());
		return view;
	}

}