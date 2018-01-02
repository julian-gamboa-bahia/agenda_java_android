package br.com.agendadezembrosimple.agendajava.comentarios;

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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import br.com.agendadezembrosimple.agendajava.Config;
import br.com.agendadezembrosimple.agendajava.DB_registro_atividades.DB_registro_atividades;
import br.com.agendadezembrosimple.agendajava.R;
import br.com.agendadezembrosimple.agendajava.adapters.Atividades;
import br.com.agendadezembrosimple.agendajava.adapters.Comentarios;
import br.com.agendadezembrosimple.agendajava.adapters.Lista_Atividades_Registradas_Adapter;
import br.com.agendadezembrosimple.agendajava.adapters.Lista_Comentarios_Adapter;
import br.com.agendadezembrosimple.agendajava.espelharDB.Servidor_DB_registro_NOVA_atividades;

public class listar_comentarios_funcoes extends AppCompatActivity {

    String AGRUPAR="Agrupar por (Atividade Similar)";
    final int AGRUPAR_CASE=0;
    String AGREGAR_COMENTARIO="Agregar Comentario (Atividade Similar)";
    final int AGREGAR_COMENTARIO_CASE=1;
    String FINALIZAR="Finalizar";
    final int FINALIZAR_CASE=2;

    List<Comentarios> Objetos_Comentarios = new ArrayList<Comentarios>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_comentarios_funcoes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//Colorimos o Toolbar para poder
        toolbar.setBackgroundColor((Color.GREEN));
        preparar_lista();
    }

    //Dezembro17

    ListView simpleListView;

    public void preparar_lista() {
//Identificamos
        final DB_registro_atividades db_registro_atividades = new DB_registro_atividades(getBaseContext());

        Objetos_Comentarios= db_registro_atividades.listar_comentarios_Objetos();

        for(Integer k=0;k<Objetos_Comentarios.size(); k++)
        {
            Objetos_Comentarios.get(k).setContador_lista(k+1);
            Log.d("Janeiro01","Atividade do Objeto "+k+"\n"+Objetos_Comentarios.get(k).getAtividade()
                    +"\n"+
                    "Posicao do Objeto "+k+"\n"+Objetos_Comentarios.get(k).getPosicao()
                    +"\n"+
                    "Quantidade Registros feitos "+k+"\n"+Objetos_Comentarios.get(k).getQuantidade_registros_feitos()
                    +"\n"+
                    "mostar "+k+"\n"+Objetos_Comentarios.get(k).getMostrar()+"\n"+
                    "comentario "+k+"\n"+Objetos_Comentarios.get(k).getComentario()+"\n"
            );
        }

        //Log.d("Dezembro17",lista_temporal.toString());



        colocar_adapter();


    }
//Dezembro17
void janela_agrupar_por_atividade(
        final String atividade,
        final Activity activity,
        final boolean escondido
) {

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
                                case  AGRUPAR_CASE:
//Filtramos pela atividade
//Deixando apenas aquelas que tenha aquela atividade
//                                    Log.d("Dezembro21","atividade\n"+atividade);


                                    preparar_lista();
                                    break;
                                case AGREGAR_COMENTARIO_CASE:
                                    janela_comentario(
                                            agora,
                                            "0",
                                            atividade
                                    );
                                    break;
                                case FINALIZAR_CASE:
                                    preparar_DB(
                                            atividade+"(finalizado)",
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
//Dezembro21
public void janela_comentario(
        final String agora,
        final String esconder,
        final String atividade

)
{
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Fazer coméntario");
    final Activity activity=this;

// Set up the input
    final EditText input = new EditText(this);
    builder.setView(input);

// Set up the buttons
    builder.setPositiveButton("Comentar", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String comentario=input.getText().toString();

            DB_registro_atividades db_registro_atividades = new DB_registro_atividades(getBaseContext());
            db_registro_atividades.inserir_comentario(
                    comentario,
                    agora,
                    esconder,
                    atividade
            );

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
    public void colocar_adapter()
    {
        simpleListView = (ListView) findViewById(R.id.lista_comentarios);

//filtrar_Objetos_Atividades();

        ListAdapter customAdapter = new Lista_Comentarios_Adapter(
                this,
                R.layout.item_lista_comentarios,
                Objetos_Comentarios);

        simpleListView.setAdapter(customAdapter);
    }
//////////////////
}
