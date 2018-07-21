package br.com.agendadezembrosimple.agendajava.DB_registro_atividades;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by julian on 17/12/17.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.IntegerRes;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.agendadezembrosimple.agendajava.adapters.Atividades;
import br.com.agendadezembrosimple.agendajava.adapters.Comentarios;

/**
 * Created by allanromanato on 5/27/15.
 */
public class DB_registro_atividades extends SQLiteOpenHelper {
    private static final String NOME_BANCO = "tarefas_registradas.db";

// Tabela 1 : registro de tarefas

    private static final String TABELA_atividades = "atividades";
    private static final String ATIVIDADES_indice = "indice";
    private static final String ATIVIDADES_atividades = "atividades";
    private static final String ATIVIDADES_hora = "hora";
    private static final String ATIVIDADES_foto = "foto";
    private static final String ATIVIDADES_atividade_passada = "atividade_passada";
    private static final String ATIVIDADES_barcode = "barcode";
    private static final String ATIVIDADES_esconder = "esconder";
    //calendario, para poder esconder aquelas antigas
    private static final String ATIVIDADES_dia = "dia";
    private static final String ATIVIDADES_mes = "mes";
    private static final String ATIVIDADES_ano = "ano";

// Tabela 2 : Cadastro de tarefas

    private static final String TABELA_NOVAS_atividades = "novas_atividades";
    private static final String NOVAS_ATIVIDADES_indice = "indice";
    private static final String NOVAS_ATIVIDADES_atividades = "atividades";
    private static final String NOVAS_ATIVIDADES_hora = "hora";
    private static final String NOVAS_ATIVIDADES_foto = "foto";
    private static final String NOVAS_ATIVIDADES_esconder = "esconder";


// Tabela 3 : comentarios de cada tarefa

    private static final String TABELA_COMENTARIOS_atividades = "comentarios_atividades";
    private static final String COMENTARIOS_ATIVIDADES_indice = "indice";
    private static final String COMENTARIOS_ATIVIDADES_atividade_associada = "atividade_associada";
    private static final String COMENTARIOS_ATIVIDADES_comentario = "comentario";
    private static final String COMENTARIOS_ATIVIDADES_hora = "hora";
    private static final String COMENTARIOS_ATIVIDADES_esconder = "esconder";


    private static final int VERSAO = 1;

    public DB_registro_atividades(Context context){

        super(context, NOME_BANCO,null,VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

//Cria Tabela 1
        String sql = "CREATE TABLE "+TABELA_atividades+"("
                + ATIVIDADES_indice + " integer primary key autoincrement,"
                + ATIVIDADES_atividades + " text,"
                + ATIVIDADES_hora + " text,"
                + ATIVIDADES_foto + " text,"
                + ATIVIDADES_atividade_passada + " text,"
                + ATIVIDADES_barcode + " text,"
                + ATIVIDADES_esconder + " text,"
                + ATIVIDADES_dia + " text,"
                + ATIVIDADES_mes + " text,"
                + ATIVIDADES_ano + " text"
                +")";
        db.execSQL(sql);

        //Cria Tabela 2 NOVAS
        sql = "CREATE TABLE "+TABELA_NOVAS_atividades+"("
                + NOVAS_ATIVIDADES_indice + " integer primary key autoincrement,"
                + NOVAS_ATIVIDADES_atividades + " text,"
                + NOVAS_ATIVIDADES_hora + " text,"
                + NOVAS_ATIVIDADES_foto + " text,"
                + NOVAS_ATIVIDADES_esconder + " text"
                +")";
        db.execSQL(sql);

        //Cria Tabela 3 COMENTARIOS
        sql = "CREATE TABLE "+TABELA_COMENTARIOS_atividades+"("
                + COMENTARIOS_ATIVIDADES_indice + " integer primary key autoincrement,"
                + COMENTARIOS_ATIVIDADES_comentario + " text,"
                + COMENTARIOS_ATIVIDADES_atividade_associada + " text,"
                + COMENTARIOS_ATIVIDADES_hora + " text,"
                + COMENTARIOS_ATIVIDADES_esconder + " text"
                +")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABELA_atividades);
        onCreate(db);
    }

//Dezembro17
    public void inserir_inicio(
        String atividades,
        String hora,
        String foto,
        String atividade_passada,
        String barcode,
        String esconder,
        Integer dia,
        Integer mes,
        Integer ano
        )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ATIVIDADES_atividades,atividades);
        values.put(ATIVIDADES_hora,hora);
        values.put(ATIVIDADES_foto,foto);
        values.put(ATIVIDADES_atividade_passada,atividade_passada);
        values.put(ATIVIDADES_barcode,barcode);
        values.put(ATIVIDADES_esconder,esconder);

        values.put(ATIVIDADES_dia,dia);
        values.put(ATIVIDADES_mes,mes);
        values.put(ATIVIDADES_ano,ano);

        Log.d("Janeiro06","inserir_inicio\n"+dia+"\n"+mes+"\n"+ano+"\n");

        db.insert(TABELA_atividades, null, values);
        db.close();
    }
//Dezembro17
    public List<String> listar_atividades_registradas()
    {
        List<String> lista_temporal=new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query="select "+
                ATIVIDADES_atividades+" , "+
                ATIVIDADES_hora+" , "+
                ATIVIDADES_esconder
                +" from "+TABELA_atividades+" order by indice desc";

        Cursor res =  db.rawQuery(query, null);

        res.moveToFirst();

        //Log.d("Setembro23","\n"+query);

        String atividade="";
        String hora="";
        String esconder="";

        Integer indice=1;

        while(res.isAfterLast() == false){

            atividade=res.getString(0);
            hora=res.getString(1);
            esconder=res.getString(2);

            //lista_temporal.add(indice+") "+atividade+"\n"+hora+"\n"+esconder);
            lista_temporal.add(indice+") "+atividade+"\n"+hora);
            indice++;

            res.moveToNext();
        }
        return lista_temporal;
    }
//Esconder desmarcar_esconder_por_position
    public void esconder_por_position(Integer position)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String query="select "+
                ATIVIDADES_indice+" , "+
                ATIVIDADES_hora
                +" from "+TABELA_atividades+" order by indice desc";

        Cursor res =  db.rawQuery(query, null);
        res.moveToFirst();

        //Construimos uma lista de indices

        List<Integer> indices=new ArrayList<Integer>();

        Integer indice;

        while(res.isAfterLast() == false){

            indice=res.getInt(0);
            indices.add(indice);
            res.moveToNext();
        }

        Integer indice_procurado=indices.get(position);

        String sql="update "+TABELA_atividades+" set "+ATIVIDADES_esconder+"=1 where indice="+indice_procurado;
        db.execSQL(sql);
        db.close();
    }

    public boolean ler_esconder_position(Integer position)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String query="select "+
                ATIVIDADES_indice+" , "+
                ATIVIDADES_hora
                +" from "+TABELA_atividades+" order by indice desc";

        Cursor res =  db.rawQuery(query, null);
        res.moveToFirst();

        //Construimos uma lista de indices

        List<Integer> indices=new ArrayList<Integer>();

        Integer indice;

        while(res.isAfterLast() == false){

            indice=res.getInt(0);
            indices.add(indice);
            res.moveToNext();
        }

        Integer indice_procurado=indices.get(position);

        query="select "+
                ATIVIDADES_esconder+" , "+
                ATIVIDADES_hora
                +" from "+TABELA_atividades+" where "+ATIVIDADES_indice+"="+indice_procurado;

        res =  db.rawQuery(query, null);
        res.moveToFirst();

        indice=res.getInt(0);

        if(indice==1)
        {
            return true;
        }
        else
        {
            return false;
        }

    }
//Dezembro18
public void cadastrar_nova_atividade(
        String atividades,
        String hora,
        String foto,
        String barcode,
        String esconder
)
{
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();

    values.put(NOVAS_ATIVIDADES_atividades,atividades);
    values.put(NOVAS_ATIVIDADES_hora,hora);
    values.put(NOVAS_ATIVIDADES_foto,foto);
    values.put(NOVAS_ATIVIDADES_esconder,esconder);
    db.insert(TABELA_NOVAS_atividades, null, values);
    db.close();
}
//Dezembro18
    public List<String> listar_novas_atividades_registradas()
    {
        List<String> lista_temporal=new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query="select "+
                NOVAS_ATIVIDADES_atividades+"  "
                +" from "+TABELA_NOVAS_atividades+" order by indice desc";

        Cursor res =  db.rawQuery(query, null);

        res.moveToFirst();

        //Log.d("Setembro23","\n"+query);

        String atividade="";

        while(res.isAfterLast() == false){

            atividade=res.getString(0);
            lista_temporal.add(atividade);
            res.moveToNext();
        }
        return lista_temporal;
    }
///Dezembro 19

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABELA_atividades);
        return numRows;
    }

    ///Dezembro 19

    public int numero_comentarios(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABELA_COMENTARIOS_atividades);
        return numRows;
    }

//Dezembro 19
    public String data_ultimo_registro()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String query="select "+
               ATIVIDADES_hora+"  "
                +" from "+TABELA_atividades+" order by indice desc limit 1";

        Cursor res =  db.rawQuery(query, null);

        res.moveToFirst();

        String data_ultimo="";

        while(res.isAfterLast() == false){

            data_ultimo=res.getString(0);
            res.moveToNext();
        }
        return data_ultimo;
    }
//Dezembro20
//desmarcar o item que esteja colocado como escondido
    public void desmarcar_esconder_por_position(Integer position)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String query="select "+
                ATIVIDADES_indice+" , "+
                ATIVIDADES_hora
                +" from "+TABELA_atividades+" order by indice desc";

        Cursor res =  db.rawQuery(query, null);
        res.moveToFirst();

        //Construimos uma lista de indices

        List<Integer> indices=new ArrayList<Integer>();

        Integer indice;

        while(res.isAfterLast() == false){

            indice=res.getInt(0);
            indices.add(indice);
            res.moveToNext();
        }

        Integer indice_procurado=indices.get(position);

        String sql="update "+TABELA_atividades+" set "+ATIVIDADES_esconder+"=0 where indice="+indice_procurado;
        db.execSQL(sql);
        db.close();
    }
    //Dezembro20

    public void inserir_comentario(
            String comentario,
            String hora,
            String esconder,
            String atividade_associada
    )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COMENTARIOS_ATIVIDADES_comentario,comentario);
        values.put(COMENTARIOS_ATIVIDADES_atividade_associada,atividade_associada);
        values.put(COMENTARIOS_ATIVIDADES_hora,hora);
        values.put(COMENTARIOS_ATIVIDADES_esconder,esconder);
        db.insert(TABELA_COMENTARIOS_atividades, null, values);
        db.close();
    }

    //Dezembro20

    public List<String> listar_comentarios()
    {
        List<String> lista_temporal=new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query="select "+
                COMENTARIOS_ATIVIDADES_comentario+" , "+
                COMENTARIOS_ATIVIDADES_atividade_associada+" , "+
                COMENTARIOS_ATIVIDADES_hora
                +" from "+TABELA_COMENTARIOS_atividades+" order by indice desc";

        Cursor res =  db.rawQuery(query, null);

        res.moveToFirst();

        //Log.d("Setembro23","\n"+query);

        String comentario="";
        String atividade_associada="";
        String hora="";

        Integer indice=1;

        while(res.isAfterLast() == false){

            comentario=res.getString(0);
            atividade_associada=res.getString(1);
            hora=res.getString(2);

            lista_temporal.add(indice+") "+comentario+"\n"+atividade_associada+"\n"+hora);
            indice++;

            res.moveToNext();
        }
        return lista_temporal;
    }

// Dezembro25

    public Integer procurar_similar(String lista_tarefas)
    {

        SQLiteDatabase db = this.getReadableDatabase();

        String query="select count(*) "+
                ATIVIDADES_indice
                +" from "+TABELA_atividades+" where "+ATIVIDADES_atividades+" like '%"+lista_tarefas+"%'";

        Cursor mCount= db.rawQuery(query,null);
        mCount.moveToFirst();
        int numero_similares= mCount.getInt(0);
        mCount.close();

        return numero_similares;
    }
//Janeiro01
public List<Atividades>  listar_atividades_registradas_Objetos()
{
    List<Atividades> Objetos_Atividades = new ArrayList<Atividades>();

    SQLiteDatabase db = this.getReadableDatabase();

    String query="select "+
            ATIVIDADES_atividades+" , "+
            ATIVIDADES_hora+" , "+
            ATIVIDADES_esconder
            +" from "+TABELA_atividades+" order by indice desc";

    Cursor res =  db.rawQuery(query, null);

    res.moveToFirst();

    //Log.d("Setembro23","\n"+query);

    String atividade="";
    String hora="";

    while(res.isAfterLast() == false){

        atividade=res.getString(0);
        hora=res.getString(1);

        Atividades nova_atividade=new Atividades(
                atividade,
                hora,
                Objetos_Atividades.size(),
                0,
                true,
                0,
                0
        );
        Objetos_Atividades.add(nova_atividade);
        res.moveToNext();
    }
    return Objetos_Atividades;
}
//Janeiro01
//Janeiro01
public List<Comentarios>  listar_comentarios_Objetos()
{
    List<Comentarios> Objetos_Comentarios = new ArrayList<Comentarios>();

    SQLiteDatabase db = this.getReadableDatabase();

    String query="select "+
            COMENTARIOS_ATIVIDADES_comentario+" , "+
            COMENTARIOS_ATIVIDADES_atividade_associada+" , "+
            COMENTARIOS_ATIVIDADES_hora
            +" from "+TABELA_COMENTARIOS_atividades+" order by indice desc";

    Cursor res =  db.rawQuery(query, null);

    res.moveToFirst();

    //Log.d("Setembro23","\n"+query);

    String atividade="";
    String hora="";
    String comentario="";

    while(res.isAfterLast() == false){

        comentario=res.getString(0);
        atividade=res.getString(1);
        hora=res.getString(2);

        Comentarios novo_comentario=new Comentarios(
                atividade,
                hora,
                Objetos_Comentarios.size(),
                0,
                true,
                0,
                0,
                comentario
        );
        Objetos_Comentarios.add(novo_comentario);
        res.moveToNext();
    }
    return Objetos_Comentarios;
}

//Janeiro03
    public boolean conferir_ja_registrado(String atividade)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String query="select "+
                NOVAS_ATIVIDADES_atividades
                +" from "+TABELA_NOVAS_atividades+" where "+NOVAS_ATIVIDADES_atividades+" like '%" +
                atividade+
                "%'";

        Cursor res =  db.rawQuery(query, null);

        if(res.getCount() <= 0){
            res.close();
            return false;
        }
        res.close();
        return true;
    }



// / / /////
}