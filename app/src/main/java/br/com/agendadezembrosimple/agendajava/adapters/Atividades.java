package br.com.agendadezembrosimple.agendajava.adapters;

/**
 * Created by julian on 30/12/17.
 * Criamos uma classe que será usada para colocar as atividades
 */

public class Atividades {
    private String atividade;
    private String hora;
    private Integer posicao;
    private Integer indice_DB;
    public Integer quantidade_registros_feitos;
    private boolean mostrar;
    private Integer contador_lista;

    public Atividades(
            String atividade,
            String hora,
            Integer posicao,
            Integer quantidade_registros_feitos,
            boolean mostrar,
            Integer indice_DB,
            Integer contador_lista
    )
    {
        this.atividade=atividade;
        this.hora=hora;
        this.posicao=posicao;
        this.quantidade_registros_feitos=quantidade_registros_feitos;
        this.mostrar =mostrar;
        this.indice_DB=indice_DB;
        this.contador_lista=contador_lista;
    }

    public void setAtividade(String atividade)
    {
        this.atividade=atividade;
    }
    public void setHora(String hora)
    {
        this.hora=hora;
    }
    public void setPosicao(Integer posicao)
    {
        this.posicao=posicao;
    }
    public void setQuantidade_registros_feitos(Integer quantidade_registros_feitos)
    {
        this.quantidade_registros_feitos=quantidade_registros_feitos;
    }
    public void setMostrar(boolean mostrar)
    {
        this.mostrar = mostrar;
    }
    public void setIndice_DB(Integer indice_DB)
    {
        this.indice_DB=indice_DB;
    }

    public void setContador_lista(Integer contador_lista){
        this.contador_lista=contador_lista;
    }
////Agora recuperamos as informações
    public String getAtividade()
    {
        return this.atividade;
    }
    public String getHora()
    {
        return this.hora;
    }
    public Integer getPosicao()
    {
        return this.posicao;
    }
    public Integer getQuantidade_registros_feitos()
    {
        if(this.quantidade_registros_feitos==null)
        {
            return 0;
        }
        else
        {
            return this.quantidade_registros_feitos;
        }
    }
    public boolean getMostrar()
    {
        return this.mostrar;
    }
    public Integer getIndice_DB()
    {
        return this.indice_DB;
    }
    public Integer getContador_lista()
    {
        return this.contador_lista;
    }
//////// //////////////
}
