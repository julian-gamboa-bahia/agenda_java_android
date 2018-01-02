package br.com.agendadezembrosimple.agendajava.DB_registro_atividades;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.agendadezembrosimple.agendajava.Config;
import br.com.agendadezembrosimple.agendajava.R;
import br.com.agendadezembrosimple.agendajava.adapters.Atividades;
import br.com.agendadezembrosimple.agendajava.adapters.Lista_Atividades_Adapter;
import br.com.agendadezembrosimple.agendajava.adapters.Lista_Atividades_Registradas_Adapter;
import br.com.agendadezembrosimple.agendajava.espelharDB.Servidor_DB_registro_NOVA_atividades;
import br.com.agendadezembrosimple.agendajava.listar_Atividades_afim;

public class listar_Atividades_registradas_funcoes extends AppCompatActivity {

    final String AGRUPAR="Agrupar por (Atividade Similar)";
    final int AGRUPAR_CASE=0;
    String AGREGAR_COMENTARIO="Agregar Comentario (Atividade Similar)";
    final int AGREGAR_COMENTARIO_CASE=1;
    String FINALIZAR="Finalizar";
    final int FINALIZAR_CASE=2;

    List<Atividades> Objetos_Atividades = new ArrayList<Atividades>();

    String ANTI_LUPINHA_VER_TODOS=" Ver todos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historico_atividades_funcoes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.GRAY);

        preparar_lista();
    }

    //Janeiro01

    Integer contagem_lista=0;

    ListView simpleListView;

    public void preparar_lista() {
//Identificamos
        simpleListView = (ListView) findViewById(R.id.lista_historico_tarefas);

        final DB_registro_atividades db_registro_atividades = new DB_registro_atividades(getBaseContext());

//Janeiro01 Colocamos no objeto de atividades

        Objetos_Atividades=db_registro_atividades.listar_atividades_registradas_Objetos();
        /////////

        for(Integer k=0;k<Objetos_Atividades.size(); k++)
        {
            Objetos_Atividades.get(k).setContador_lista(k+1);
            Log.d("Janeiro01","Atividade do Objeto "+k+"\n"+Objetos_Atividades.get(k).getAtividade()
                    +"\n"+
                    "Posicao do Objeto "+k+"\n"+Objetos_Atividades.get(k).getPosicao()
                    +"\n"+
                    "Quantidade Registros feitos "+k+"\n"+Objetos_Atividades.get(k).getQuantidade_registros_feitos()
                    +"\n"+
                    "mostar "+k+"\n"+Objetos_Atividades.get(k).getMostrar()
            );
        }

        colocar_adapter();
    }
//Dezembro17
void janela_esconder(
        final Integer position,
        final Activity activity,
        final boolean escondido,
        final String atividade
) {
    //Colocamos de forma dinamica a lista de tarefas que será executada:

    final String[] str={
            AGRUPAR,
            AGREGAR_COMENTARIO,
            FINALIZAR
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

                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
                        String agora=sdf.format(cal.getTime());

                        if (checked) {
                            switch (i)
                            {
                                case AGRUPAR_CASE:
//Marcar como esconder
                                    DB_registro_atividades db_registro_atividades = new DB_registro_atividades(activity);
                                    if(escondido)
                                    {
                                        db_registro_atividades.desmarcar_esconder_por_position(position);
                                    }
                                    else
                                    {
                                        db_registro_atividades.esconder_por_position(position);
                                    }
                                    break;
                                case AGREGAR_COMENTARIO_CASE:
                                    break;
                                case FINALIZAR_CASE:
                                    preparar_DB(
                                            atividade+" (finalizado)",
                                            agora,
                                            "foto",
                                            "atividade_passada",
                                            "barcode");
                                    break;
                            }

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
//Dezembro29
    //Aqui é onde colocamos a nova atividade

    public String preparar_DB(
            String atividades,
            String hora,
            String foto,
            String atividade_passada,
            String barcode
    ) {

        hora = obter_dia() + "  " + hora;
        DB_registro_atividades db_registro_atividades = new DB_registro_atividades(getBaseContext());
        //db_registro_atividades.numberOfRows();
        db_registro_atividades.inserir_inicio(
                atividades,
                hora,
                foto,
                atividade_passada,
                barcode,
                "0"
        );
        //Dezembro26
        String url = Config.Servidor_DB_registro_atividades + "?atividade=" + atividades + "&hora_celular=" + hora;
        new Servidor_DB_registro_NOVA_atividades().execute(url);
        return "" + db_registro_atividades.numberOfRows();
    }
//Dezembro29
    //Dezembro20 Colocamos o dia certinho Para facilitar a leitura das informações

    public String obter_dia() {
        String dia = "";
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int dayOfMoth = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        switch (day) {
            case Calendar.MONDAY:
                dia = "Segunda-Feira";
                break;
            case Calendar.TUESDAY:
                dia = "Terça-Feira";
                break;
            case Calendar.WEDNESDAY:
                dia = "Quarta-Feira";
                break;
            case Calendar.THURSDAY:
                dia = "Quinta-Feira";
                break;
            case Calendar.FRIDAY:
                dia = "Sexta-Feira";
                break;
            case Calendar.SATURDAY:
                dia = "Sábado";
                break;
            case Calendar.SUNDAY:
                dia = "Domingo";
                break;
        }

        return dia + " mês: " + month + " , dia: " + dayOfMoth + ", " + year;
    }
//Janeiro01
// Onclicks
    public void operacoes_cada_item(View v)
    {
        Object[] itemToExpandir = (Object[]) v.getTag();

        String atividade = (String) itemToExpandir[0];

        janela_esconder(
        0,
        this,
        false,
        atividade);
    }

    public void operacoes_cada_informacao_pre_item(View v)
    {
        Object[] itemToExpandir = (Object[]) v.getTag();
        String atividade = (String) itemToExpandir[0];
    }
//
//Janeiro01
public void buscar_similares(View v) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Procurar Atividade");

    final EditText input = new EditText(this);

    builder.setView(input);

    builder.setPositiveButton("Procurar", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String procurando = input.getText().toString();
            filtrar(procurando);
        }
    });
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    });

    builder.show();
}

//Onclick da lupinha
    public void filtrar(String procurando) {

        preparar_lista();

        for (Integer i = 0; i < Objetos_Atividades.size(); i++) {
//comparamos todo em Minusculas
            if (Objetos_Atividades.get(i).getAtividade().toLowerCase().contains(procurando.toLowerCase())) {
                Objetos_Atividades.get(i).setMostrar(true);
            }
            else
            {
                Objetos_Atividades.get(i).setMostrar(false);
            }
        }
//Dezembro31
// Domingo Colocamos uma função no final
        Atividades funcao_anti_lupinha=new Atividades(
                ANTI_LUPINHA_VER_TODOS,
                "",
                Objetos_Atividades.size(),
                0,
                true,
                0,
                0
        );
        Objetos_Atividades.add(funcao_anti_lupinha);
        filtrar_Objetos_Atividades();
        colocar_adapter();
    }
//
public void filtrar_Objetos_Atividades()
{
    List<Atividades> deleteCandidates = new ArrayList<>();
    // Pass 1 - collect delete candidates
    for (Atividades Objetos_Atividade  : Objetos_Atividades) {
        if (!Objetos_Atividade.getMostrar()) {
            deleteCandidates.add(Objetos_Atividade);
        }
    }
    // Pass 2 - delete
    for (Atividades deleteCandidate : deleteCandidates) {
        Objetos_Atividades.remove(deleteCandidate);
    }
//Alteramos a numeracao para
// poder CONTAR
    contagem_lista=1;
    for (Atividades Objetos_Atividade  : Objetos_Atividades) {
        Objetos_Atividade.setContador_lista(contagem_lista++);
    }
}
//Janeiro01
    public void  colocar_adapter()
    {
        simpleListView = (ListView) findViewById(R.id.lista_historico_tarefas);

//filtrar_Objetos_Atividades();

        ListAdapter customAdapter = new Lista_Atividades_Registradas_Adapter(
                this,
                R.layout.item_lista_atividades_registradas,
                Objetos_Atividades);

        simpleListView.setAdapter(customAdapter);
    }

/// / ///////////
}
