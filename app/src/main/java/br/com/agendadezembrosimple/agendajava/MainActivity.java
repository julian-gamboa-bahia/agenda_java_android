package br.com.agendadezembrosimple.agendajava;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import br.com.agendadezembrosimple.agendajava.DB_registro_atividades.DB_registro_atividades;
import br.com.agendadezembrosimple.agendajava.DB_registro_atividades.listar_Atividades_registradas_funcoes;
import br.com.agendadezembrosimple.agendajava.adapters.Atividades;
import br.com.agendadezembrosimple.agendajava.adapters.Lista_Atividades_Adapter;
import br.com.agendadezembrosimple.agendajava.comentarios.listar_comentarios_funcoes;
import br.com.agendadezembrosimple.agendajava.espelharDB.Servidor_DB_registro_NOVA_atividades;
import br.com.agendadezembrosimple.agendajava.espelharDB.Servidor_TABELA_comentario;
import br.com.agendadezembrosimple.agendajava.fotografar.RelacionarImagem;
import br.com.agendadezembrosimple.agendajava.imagenes.ItemListActivity;

public class MainActivity extends AppCompatActivity {

    //Dezembro 21: Com o intuito de programar de forma ordenada é preciso definir estás constantes
    // de configuração
    String REGISTRAR_ATIVIDADE = "Registrar atividade";
    final int REGISTRAR_ATIVIDADE_CASE=0;
    String PROCURAR_SIMILARES = "Procurar Similares";
    final int PROCURAR_SIMILARES_CASE=1;
    String FAZER_COMENTARIO = "Fazer Comentário";
    final int FAZER_COMENTARIO_CASE=2;
    String ELIMINAR_ATIVIDADE = "Eliminar atividade";
    final int ELIMINAR_ATIVIDADE_CASE=3;
    String FOTOGRAFAR = "Fotografar/Galeria";
    final int FOTOGRAFAR_CASE=4;
    String LER_BARCODE = "Ler BarCODE";
    final int LER_BARCODE_CASE_CASE=5;
    String FINALIZAR_ATIVIDADE = "Finalizar";
    final int FINALIZAR_ATIVIDADE_CASE=6;

    String temporizar_tarefa = "Sem tarefa temporizada";

//Será usado um array adapter diferente

    List<Atividades> Objetos_Atividades = new ArrayList<Atividades>();

    //Menu do lado esquerdo

    private static final int MENU_ESQUERDO_ATIVIDADES_REGISTRADAS = R.id.atividades_registradas;
    private static final int MENU_ESQUERDO_ATIVIDADES_FALTANTES = R.id.atividades_faltantes;
    private static final int MENU_ESQUERDO_NOVA_ATIVIDADE = R.id.nova_atividade;
    private static final int MENU_ESQUERDO_VER_COMENTARIOS = R.id.ver_comentarios;
    private static final int MENU_ESQUERDO_VER_IMAGENES = R.id.ver_imagenes;

//Funções especiais

    String OPERACOES_ULTIMA_TAREFA = " Ultima tarefa cadastrada:";
    String ANTI_LUPINHA_VER_TODOS=" Ver todos";
    String VER_MAIS_FREQUENTES=" Ver mais frequentes";

    //Cronometro
    long startTime;
    long countUp;

    String[] lista_tarefas = {
            "Lavar Pratos",
            "Lavar Roupa",
            "Banho",
            "Alimentar Princesinha Pintadinha",
            "Jogar Xadrez",
            "Ver \"El Chapo\"",
            "Tomar cafe",
            "Esquentar Arroz"
    };

    Integer contagem_lista=0;

    ListView simpleListView;

    String ultima_tarefa_registrada = "";

    final Activity activity=this;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    preparar_lista();
                    return true;
                case R.id.navigation_registradas:
//MENU_ESQUERDO_ATIVIDADES_REGISTRADAS
                    intent = new Intent(activity,listar_Atividades_registradas_funcoes.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_comentarios:
//MENU_ESQUERDO_VER_COMENTARIOS:
                    intent = new Intent(activity,listar_comentarios_funcoes.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        preparar_lista();
    }

    //Dezembro 17

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_direito, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent;
        switch (item.getItemId()) {
//atividades_registradas:
// Aqui temos uma lista elemental das tarefas já registradas no DB
            case MENU_ESQUERDO_ATIVIDADES_REGISTRADAS:
                intent = new Intent(this, listar_Atividades_registradas_funcoes.class);
                startActivity(intent);
                return true;
            case MENU_ESQUERDO_ATIVIDADES_FALTANTES:
                return true;
            case MENU_ESQUERDO_NOVA_ATIVIDADE:
                registrar_nova_atividade();
                return true;
            case MENU_ESQUERDO_VER_COMENTARIOS:
                intent = new Intent(this,
                        listar_comentarios_funcoes.class);
                startActivity(intent);
                return true;
            case MENU_ESQUERDO_VER_IMAGENES:
                intent = new Intent(this,
                        ItemListActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


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

    //Dezembro18
//Dezembro20
    //Uma vez Cadastrada a nova atividade pode-se registrar a mesma de forma imediata
    public void registrar_nova_atividade() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Registrar Nova Atividade");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String atividade = input.getText().toString();

                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                String agora = sdf.format(cal.getTime());
                //Vemos o texto de cada iteme o tempo já cálculado
                Toast.makeText(MainActivity.this,
                        "Registrando como nova ATIVIDADE \n" + atividade + "\n" + agora
                        , Toast.LENGTH_SHORT).show();

                cadastrar_nova_atividade(
                        atividade,
                        agora,
                        "foto",
                        "0");
                janela_tela_inicial_tarefas(atividade);
//Espelhamos no servidor
                String url = Config.Servidor_DB_registro_NOVA_atividades + "?atividade=" + atividade + "&hora_celular=" + agora;
                new Servidor_DB_registro_NOVA_atividades().execute(url);
                preparar_lista();

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
//Dezembro18

    public void cadastrar_nova_atividade(
            String atividades,
            String hora,
            String foto,
            String esconder
    ) {
        DB_registro_atividades db_registro_atividades = new DB_registro_atividades(getBaseContext());
        db_registro_atividades.cadastrar_nova_atividade(
                atividades,
                hora,
                foto,
                "0",
                esconder
        );
    }

//Dezembro30
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

    //Dezembro19
    public void buscar_similares(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Procurar Atividade");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
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
//Dezembro


    public void janela_tela_inicial_tarefas(final String atividade) {

        final String[] str = {
                REGISTRAR_ATIVIDADE,
                PROCURAR_SIMILARES,
                FAZER_COMENTARIO,
                ELIMINAR_ATIVIDADE,
                FOTOGRAFAR,
                LER_BARCODE,
                FINALIZAR_ATIVIDADE
        };


        final AlertDialog.Builder builderDialog = new AlertDialog.Builder(this);
        //TITULO
        builderDialog.setTitle(atividade);

        final Activity activity = this;

        int count = str.length;

        boolean[] is_checked = new boolean[count];

        builderDialog.setSingleChoiceItems(str, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });

        startTime = SystemClock.elapsedRealtime();

        final TextView tempos = (TextView) findViewById(R.id.tempos);
//Ativamos o cronometro:
        final Chronometer simpleChronometer = (Chronometer) findViewById(R.id.chronometer1);

        simpleChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                countUp = (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000;
                tempos.setText(temporizar_tarefa + " " + (countUp / 60) + ":" + (countUp % 60));
            }
        });

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

                                Calendar cal = Calendar.getInstance();
                                //SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//Colocando o dia
                                String agora = sdf.format(cal.getTime());

                                switch (i) {
                                    case REGISTRAR_ATIVIDADE_CASE:
//Vemos o texto de cada iteme o tempo já cálculado
//REGISTRAR_ATIVIDADE
                                        String numeroAtividade = preparar_DB(
                                                atividade,
                                                agora,
                                                "foto",
                                                "atividade_passada",
                                                "barcode");
                                        Toast.makeText(MainActivity.this,
                                                atividade + "\n" + agora + "\n" + "Atividade Numero:" + numeroAtividade
                                                , Toast.LENGTH_SHORT).show();
                                        ultima_tarefa_registrada = atividade;
                                        temporizar_tarefa = atividade;
                                        simpleChronometer.setBase(SystemClock.elapsedRealtime());
                                        simpleChronometer.start();
                                        preparar_lista();
                                        break;
                                    case PROCURAR_SIMILARES_CASE:
                                        Intent listar_Atividades_afim = new Intent(activity, listar_Atividades_afim.class);
//Dado que colocamos "(quantidade)" é preciso desconsiderar ele
                                        String procurando_filtrado = atividade.substring(atividade.indexOf(")") + 2, atividade.length());
                                        //Log.d("Dezembro29","\n"+procurando_filtrado+"\n"+procurando_filtrado.length());
                                        listar_Atividades_afim.putExtra("procurar", procurando_filtrado);
                                        startActivity(listar_Atividades_afim);
                                        break;
//FAZER_COMENTARIO
                                    case FAZER_COMENTARIO_CASE:
                                        agora = obter_dia() + " " + agora;
                                        janela_comentario(
                                                agora,
                                                "0",
                                                atividade
                                        );
                                        break;
//FOTOGRAFAR
                                    case ELIMINAR_ATIVIDADE_CASE:
                                        Intent relacionarImagem = new Intent(activity,
                                                RelacionarImagem.class);
                                        relacionarImagem.putExtra("relacionar", atividade);
                                        startActivity(relacionarImagem);
                                        break;
                                    case FOTOGRAFAR_CASE:
                                        break;
                                    case LER_BARCODE_CASE_CASE:
                                            break;
                                    case FINALIZAR_ATIVIDADE_CASE:
                                        preparar_DB(
                                                atividade+"(finalizado)",
                                                agora,
                                                "foto",
                                                "atividade_passada",
                                                "barcode");
                                        break;
                                }

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
                    }
                });
        AlertDialog alert = builderDialog.create();
        alert.show();
    }

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

        Log.d("Dezembro28", "\n" + month + "\n" + year + "\n" + dayOfMoth + "\n" + dayOfYear);

        return dia + " mês: " + month + " , dia: " + dayOfMoth + ", " + year;
    }

    //Dezembro20
    //FAZER_COMENTARIO
    public void janela_comentario(
            final String agora,
            final String esconder,
            final String atividade

    ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Fazer coméntario");
        final Activity activity = this;

// Set up the input
        final EditText input = new EditText(this);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("Comentar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String comentario = input.getText().toString();

                DB_registro_atividades db_registro_atividades = new DB_registro_atividades(getBaseContext());
                db_registro_atividades.inserir_comentario(
                        comentario,
                        agora,
                        esconder,
                        atividade
                );
//espelahando
                String url = Config.Servidor_TABELA_comentario + "?atividade=" + atividade
                        + "&hora_celular=" + agora
                        + "&comentario=" + comentario;

                new Servidor_TABELA_comentario().execute(url);

                Intent listar_comentarios = new Intent(activity,
                        listar_comentarios_funcoes.class);
                listar_comentarios.putExtra("procurar", atividade);
                startActivity(listar_comentarios);
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
    public void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            //openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }
//Dezembro29


    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        }
    }
//Dezembro30

    public void preparar_lista()
    {
        Objetos_Atividades.clear();

        //Num esquema pouco usual colocamos o primeiro item como informátivo:
        //Truqe: colocar um espaço Vazio antes para facilitar a identificação desta atividade como apenas informativa
        //É importante limpar esta lista para EVITAR dupla lista
        colocar_funcoes_especiais();

        //Pegamos as informações tanto da lista como do DB
        carregar_Objetos_Atividades();

        colocar_adapter();
    }
//Dezembro30
    public void colocar_adapter()
    {

        ListView simpleListView=findViewById(R.id.simpleListView);

        ListAdapter customAdapter = new Lista_Atividades_Adapter(
                this,
                R.layout.itemlistrow,
                Objetos_Atividades);

        simpleListView.setAdapter(customAdapter);
//////end da função
    }
//Dezembro30

    public void operacoes_cada_item(View v)
    {
        Object[] itemToExpandir = (Object[]) v.getTag();

        String atividade = (String) itemToExpandir[0];

        janela_tela_inicial_tarefas(atividade);
    }

    public void operacoes_cada_informacao_pre_item(View v)
    {
        Object[] itemToExpandir = (Object[]) v.getTag();
        String atividade = (String) itemToExpandir[0];
        //No caso de tocar na "ultima tarefa", podemo invocar aquela Activity onde se liste todo
        // De forma imediata ativamos a procura especifica
        if (atividade.contains(OPERACOES_ULTIMA_TAREFA)) {
            Intent listar_Atividades_afim = new Intent(this, listar_Atividades_afim.class);
            Integer inicio = atividade.indexOf("\n");
            String procurar = atividade.substring(inicio + 1);
        //Dezembro29
            String procurando_filtrado = procurar.substring(procurar.indexOf(")") + 2, procurar.length());
            listar_Atividades_afim.putExtra("procurar", procurando_filtrado);

            startActivity(listar_Atividades_afim);
        }
        //Dezembro31
        if (atividade.contains(ANTI_LUPINHA_VER_TODOS)) {
            preparar_lista();
            Log.d("Dezembro31", "\n" +
                    "ANTI_LUPINHA_VER_TODOS");
        }
//Janeiro01
        if (atividade.contains(VER_MAIS_FREQUENTES)) {
            //Limpamos a lista
            Objetos_Atividades.clear();
            //colocamos as informações tanto da
            // Lista INICIAL como do DB
            carregar_Objetos_Atividades();
            ordenar_Atividades_quantidade_registros_feitos();
            colocar_adapter();
        }

    }

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
    public void ordenar_Atividades_quantidade_registros_feitos()
    {
        // Sorting
        Collections.sort(Objetos_Atividades, new Comparator<Atividades>() {
            @Override
            public int compare(Atividades fruit2, Atividades fruit1)
            {

                return  fruit1.quantidade_registros_feitos.compareTo(fruit2.quantidade_registros_feitos);
            }
        });
//Numeramos de forma exata:
        contagem_lista=1;
        for (Atividades Objetos_Atividade  : Objetos_Atividades) {
            Objetos_Atividade.setContador_lista(contagem_lista++);
        }
    }


//Janeiro01
// Funções especiais
    public void colocar_funcoes_especiais()
    {
        DB_registro_atividades db_registro_atividades = new DB_registro_atividades(getBaseContext());

        Atividades nova_atividade=new Atividades(
                " Ultimo registro em: \n" + db_registro_atividades.data_ultimo_registro(),
                "",
                Objetos_Atividades.size(),
                0,
                true,
                0,
                0
        );
        Objetos_Atividades.add(nova_atividade);
        // e o segundo como indicador de quantidade
        nova_atividade=new Atividades(
                " Total de atividades registradas: \n" + db_registro_atividades.numberOfRows(),
                "",
                Objetos_Atividades.size(),
                0,
                true,
                0,
                0
        );
        Objetos_Atividades.add(nova_atividade);

        //Deszembro20
        // Procura de forma automatica tarefas similares,
        // mas antes é preciso fazer que estas primeiras linhas informativas
        // Deixem de ser atendidas pelo OnClick Geral
        if (ultima_tarefa_registrada.length() > 0) {

            nova_atividade=new Atividades(
                    OPERACOES_ULTIMA_TAREFA + "\n" + ultima_tarefa_registrada,
                    "",
                    Objetos_Atividades.size(),
                    0,
                    true,
                    0,
                    0
            );
            Objetos_Atividades.add(nova_atividade);
        }

//                VER_MAIS_FREQUENTES

        nova_atividade=new Atividades(
                VER_MAIS_FREQUENTES,
                "",
                Objetos_Atividades.size(),
                0,
                true,
                0,
                0
        );
        Objetos_Atividades.add(nova_atividade);

    }
//Janeiro01
    public void carregar_Objetos_Atividades()
    {
        DB_registro_atividades db_registro_atividades = new DB_registro_atividades(getBaseContext());

        //Criamos uma lista de tarefa que combine tanto o inicial como o que esteja no DB
//Primeiro as que estão na lista inicial
        Integer quantidade = 0;

        contagem_lista=1;

        for (Integer i = 0; i < lista_tarefas.length; i++) {

            quantidade = db_registro_atividades.procurar_similar(lista_tarefas[i]);

            Atividades nova_atividade=new Atividades(
                    lista_tarefas[i],
                    "",
                    Objetos_Atividades.size(),
                    quantidade,
                    true,
                    0,
                    contagem_lista++
            );

            Objetos_Atividades.add(nova_atividade);
        }
//Segundo, aquelas que estejam no DB
        db_registro_atividades = new DB_registro_atividades(getBaseContext());

        List<String> listar_novas_atividades_registradas = new ArrayList<String>();
        listar_novas_atividades_registradas = db_registro_atividades.listar_novas_atividades_registradas();

        for (Integer i = 0; i < listar_novas_atividades_registradas.size(); i++) {
            quantidade = db_registro_atividades.procurar_similar(listar_novas_atividades_registradas.get(i));

            Atividades nova_atividade=new Atividades(
                    listar_novas_atividades_registradas.get(i),
                    "",
                    Objetos_Atividades.size(),
                    quantidade,
                    true,
                    0,
                    contagem_lista++
            );
            Objetos_Atividades.add(nova_atividade);

        }
    }
///////////////////////
}
