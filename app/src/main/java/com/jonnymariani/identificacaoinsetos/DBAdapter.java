package com.jonnymariani.identificacaoinsetos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

    public static final String KEY_ROWID = "_id";

    private static final String DATABASE_TABLE_HISTORICO = "historico";
    private static final String DATABASE_TABLE_CONFIG = "config";


    // Colunas historico
    public static final String KEY_NOME = "nome";
    public static final String KEY_DATA = "data";

    public static final String[] COLUNAS_HISTORICO = {KEY_ROWID, KEY_NOME, KEY_DATA};

    // Colunas config
    public static final String KEY_PARAM = "param";
    public static final String KEY_VALUE = "value";

    public static final String[] COLUNAS_CONFIG = {KEY_ROWID, KEY_PARAM, KEY_VALUE};

    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "IDENTIFINSETOSDB";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CRIA_HISTORICO = "create table " + DATABASE_TABLE_HISTORICO +
            "(_id integer primary key autoincrement, " +
            "nome text not null, " +
            "data text not null);";

    private static final String DATABASE_CRIA_CONFIG = "create table " + DATABASE_TABLE_CONFIG +
            "(_id integer primary key autoincrement, " +
            "param text not null, " +
            "value text not null);";

    private final Context contexto;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.contexto = ctx;
        DBHelper = new DatabaseHelper(contexto);
    }
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CRIA_CONFIG);
                db.execSQL(DATABASE_CRIA_HISTORICO);

                this.addSetting(db, "ABRIR_NA_CAMERA", "FALSE");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void addSetting(SQLiteDatabase db, String param, String value) {
            ContentValues values = new ContentValues();
            values.put(KEY_PARAM, param);
            values.put(KEY_VALUE, value);
            db.insert(DATABASE_TABLE_CONFIG, null, values);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Atualizando a base de dados a partir da versao " + oldVersion
                    + " para " + newVersion + ",isso irá destruir todos os dados antigos");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_CONFIG);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_HISTORICO);
            onCreate(db);
        }
    }
    public String MontaRowID(long id){
        return KEY_ROWID + "=" + id;
    }


    // *******************************************************************************
    //--- abre a base de dados ---
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //--- fecha a base de dados ---
    public void close() {
        DBHelper.close();
    }



    //  Insere historico
    public long insereHistorico(String nome, String data) {
        ContentValues dados = new ContentValues();
        dados.put(KEY_NOME, nome);

        dados.put(KEY_DATA, data);

        return db.insert(DATABASE_TABLE_HISTORICO, null, dados);
    }

//    Exclui historico
    public boolean excluiHistorico(long id) {
        return db.delete(DATABASE_TABLE_HISTORICO, MontaRowID(id), null) > 0;
    }

    // Lista todas categorias
    public Cursor listarHistorico() {
        return db.query(DATABASE_TABLE_HISTORICO, COLUNAS_HISTORICO, null, null, null, null, KEY_DATA + " DESC");
    }

    //Busca historico por ID
    public Cursor BuscaHistoricoPorID(long id) throws SQLException {
        String linhaAcessada = MontaRowID(id);
        Cursor mCursor = db.query(DATABASE_TABLE_HISTORICO, COLUNAS_HISTORICO, linhaAcessada, null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //Busca Historico por nome
    public Cursor BuscaHistoricoPorNome(String nome) throws SQLException {
        return db.query(DATABASE_TABLE_HISTORICO, COLUNAS_HISTORICO, KEY_NOME + " = '" + nome + "'", null, null, null, null, null);
    }

    //Busca config por param
    public Cursor BuscaConfig(String param) throws SQLException {
        Cursor mCursor = db.query(DATABASE_TABLE_CONFIG, COLUNAS_CONFIG, KEY_PARAM + " = '" + param + "'", null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //Atualiza historico
    public boolean atualizaConfig(long id, String param, String value) {
        ContentValues dados = new ContentValues();
        String linhaAcessada = MontaRowID(id);

        dados.put(KEY_PARAM, param);
        dados.put(KEY_VALUE, value);

        return db.update(DATABASE_TABLE_CONFIG, dados, linhaAcessada, null) > 0;
    }
    public boolean atualizaHistorico(long id, String nome, String data) {
        ContentValues dados = new ContentValues();
        String linhaAcessada = MontaRowID(id);

        dados.put(KEY_NOME, nome);
        dados.put(KEY_DATA, data);

        return db.update(DATABASE_TABLE_HISTORICO, dados, linhaAcessada, null) > 0;
    }

    public void limparHistorico(){
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_HISTORICO);
        db.execSQL(DATABASE_CRIA_HISTORICO);
    }
}