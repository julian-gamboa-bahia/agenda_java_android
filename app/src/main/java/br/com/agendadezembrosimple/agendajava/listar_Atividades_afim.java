package br.com.agendadezembrosimple.agendajava;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.agendadezembrosimple.agendajava.DB_registro_atividades.DB_registro_atividades;

public class listar_Atividades_afim extends AppCompatActivity {

    ArrayList<String> lista_tarefas_total_filtrado=new ArrayList<String>();
    String procurando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historico_atividades_funcoes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        procurando= getIntent().getExtras().getString("procurar");

        if(procurando==null)
        {
            procurando="";
        }

        Log.d("Dezembro28","\n"+procurando+"\n"+procurando.length());

        preparar_lista();
    }

    //Dezembro17


    ListView simpleListView;

    public void preparar_lista() {
//Identificamos
        simpleListView = (ListView) findViewById(R.id.lista_historico_tarefas);

        final DB_registro_atividades db_registro_atividades = new DB_registro_atividades(getBaseContext());
        List<String> lista_temporal=new ArrayList<String>();
        lista_temporal= db_registro_atividades.listar_atividades_registradas();

        for(Integer i=0;i<lista_temporal.size();i++)
        {
            if(lista_temporal.get(i).contains(procurando))
            {
                lista_tarefas_total_filtrado.add(lista_temporal.get(i));
            }
        }

        //Log.d("Dezembro17",lista_temporal.toString());

        ArrayAdapter<String> simpleAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, lista_tarefas_total_filtrado);
        simpleListView.setAdapter(simpleAdapter);


        final Activity activity=this;
        //OnClick
        simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//Este position será usado para Esconder no DB
                view.setBackgroundColor(Color.GREEN);
                if(db_registro_atividades.ler_esconder_position(position))
                {
                    view.setBackgroundColor(Color.BLACK);
                }
                db_registro_atividades.esconder_por_position(position);
                janela_desmarcar_esconder(position,activity);
            }
        });

    }
//Dezembro17
static void janela_desmarcar_esconder(
        Integer position,
        Activity activity
) {
    //Colocamos de forma dinamica a lista de tarefas que será executada:

    final String[] str={
            "Desmarcar Esconder",
            "Analisar"
    };

    final AlertDialog.Builder builderDialog = new AlertDialog.Builder(activity);
    //TITULO
    builderDialog.setTitle("Ações");

    int count = str.length;

    boolean[] is_checked = new boolean[count];



    builderDialog.setSingleChoiceItems(str, 0, new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            // TODO Auto-generated method stub
        }
    });

////////
    // cria botão
    // OK
    builderDialog.setPositiveButton("Aceitar",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    ListView listAlert = ((AlertDialog) dialog).getListView();
                    // make selected item in the comma seprated string
// verificmos se estamos chekando alguma
                    for (int i = 0; i < listAlert.getCount(); i++) {
// reporta apenas os checados
                        boolean checked = listAlert.isItemChecked(i);

                        if (checked) {
                            Log.d("Dezembro18","janela_definir_desconto\n"+str[i]);
                        }
                    }
                }
            });
// cria botão
// CANCEL

    builderDialog.setNegativeButton("Cancelar",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(activity,"Por que? Cancelou pedido..",Toast.LENGTH_SHORT).show();
                }
            });
    AlertDialog alert = builderDialog.create();
    alert.show();
}
// //////////
}
