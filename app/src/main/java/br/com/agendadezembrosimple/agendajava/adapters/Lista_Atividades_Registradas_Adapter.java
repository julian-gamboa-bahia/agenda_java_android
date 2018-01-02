package br.com.agendadezembrosimple.agendajava.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import br.com.agendadezembrosimple.agendajava.R;

/**
 * Created by julian on 30/12/17.
 */

public class Lista_Atividades_Registradas_Adapter extends ArrayAdapter<Atividades> {

    public Lista_Atividades_Registradas_Adapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public Lista_Atividades_Registradas_Adapter(Context context, int resource, List<Atividades> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        ViewHolder holder = null;
        holder = new ViewHolder();

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_lista_atividades_registradas, null);
        }

        Atividades atividade = getItem(position);

        if (atividade != null) {
            Button operacoes_button=(Button) v.findViewById(R.id.operacoes_button);
            TextView atividade_text_view = (TextView) v.findViewById(R.id.atividade_text_view);
            TextView hora_text_view = (TextView) v.findViewById(R.id.hora_text_view);
            TextView numeracao_text_view=(TextView) v.findViewById(R.id.numeracao_text_view);
            String texto_atividade=atividade.getAtividade();


            if (atividade_text_view != null) {
                if(texto_atividade.substring(0,1).contains(" "))
                {
                    operacoes_button.setVisibility(View.GONE);
                    hora_text_view.setVisibility(View.GONE);
                    numeracao_text_view.setVisibility(View.GONE);

                }
                else
                {
                    operacoes_button.setVisibility(View.VISIBLE);
                    hora_text_view.setVisibility(View.VISIBLE);
                    numeracao_text_view.setVisibility(View.VISIBLE);
                    numeracao_text_view.setText(atividade.getContador_lista()+")");
                }

                atividade_text_view.setText(texto_atividade);
            }

            if (hora_text_view != null) {
                hora_text_view.setText(atividade.getHora());
            }
//Colocamos o TAG
            holder.operacoes_button=(Button) v.findViewById(R.id.operacoes_button);
            holder.atividade_text_view=(TextView) v.findViewById(R.id.atividade_text_view);

            Object[] informacoes_ROW =  {
                    texto_atividade,
            };

            holder.operacoes_button.setTag(informacoes_ROW);
            holder.atividade_text_view.setTag(informacoes_ROW);
        }
        return v;
    }
    //HOLDER

    private class ViewHolder {
        Button operacoes_button;
        TextView atividade_text_view;
        int posicao;
    }

}