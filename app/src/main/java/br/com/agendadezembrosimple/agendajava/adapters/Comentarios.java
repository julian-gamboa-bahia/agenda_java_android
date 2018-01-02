package br.com.agendadezembrosimple.agendajava.adapters;

/**
 * Created by julian on 30/12/17.
 * Criamos uma classe que será usada para colocar as atividades
 */

public class Comentarios extends Atividades{

    private String comentario;

    public Comentarios(                String atividade,
                                       String hora,
                                       Integer posicao,
                                       Integer quantidade_registros_feitos,
                                       boolean mostrar,
                                       Integer indice_DB,
                                       Integer contador_lista,
                                       String comentario)
    {

        super(
                 atividade,
                 hora,
                 posicao,
                 quantidade_registros_feitos,
                 mostrar,
                 indice_DB,
                contador_lista
    );
        this.comentario=comentario;
    }
    public void setComentario(String comentario)
    {
        this.comentario=comentario;
    }

////Agora recuperamos as informações
    public String getComentario()
    {
        return this.comentario;
    }
//////// //////////////
}
