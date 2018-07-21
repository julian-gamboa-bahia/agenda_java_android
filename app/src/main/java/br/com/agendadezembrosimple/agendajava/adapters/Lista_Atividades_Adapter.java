package br.com.agendadezembrosimple.agendajava.adapters;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import br.com.agendadezembrosimple.agendajava.R;

/**
 * Created by julian on 30/12/17.
 */

public class Lista_Atividades_Adapter extends ArrayAdapter<Atividades> {

    public Lista_Atividades_Adapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public Lista_Atividades_Adapter(Context context, int resource, List<Atividades> items) {
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
            v = vi.inflate(R.layout.itemlistrow, null);
        }

        Atividades atividade = getItem(position);

        if (atividade != null) {
            Button operacoes_button=(Button) v.findViewById(R.id.operacoes_button);
            TextView atividade_text_view = (TextView) v.findViewById(R.id.atividade_text_view);
            TextView quantidade_text_view = (TextView) v.findViewById(R.id.quantidade_text_view);

            TextView numeracao_text_view = (TextView) v.findViewById(R.id.numeracao_text_view);
            ImageButton gravar_image = (ImageButton) v.findViewById(R.id.gravar);

            String texto_atividade=atividade.getAtividade();


            if (atividade_text_view != null) {
                if(texto_atividade.substring(0,1).contains(" "))
                {
                    operacoes_button.setVisibility(View.GONE);
                    quantidade_text_view.setVisibility(View.GONE);
                    numeracao_text_view.setVisibility(View.GONE);
                    gravar_image.setVisibility(View.GONE);

                }
                else
                {
                    operacoes_button.setVisibility(View.VISIBLE);
                    quantidade_text_view.setVisibility(View.VISIBLE);
                    numeracao_text_view.setVisibility(View.VISIBLE);
                    gravar_image.setVisibility(View.VISIBLE);
                    numeracao_text_view.setText(atividade.getContador_lista()+")");
                }

                atividade_text_view.setText(texto_atividade);
            }

            if (quantidade_text_view != null) {
                quantidade_text_view.setText(atividade.getQuantidade_registros_feitos()+"");
            }
//Colocamos o TAG
            holder.operacoes_button=(Button) v.findViewById(R.id.operacoes_button);
            holder.atividade_text_view=(TextView) v.findViewById(R.id.atividade_text_view);
            holder.gravar_image=(ImageButton) v.findViewById(R.id.gravar);

            boolean gravando=false;
            Object[] informacoes_ROW =  {
                    texto_atividade,
                    gravar_image,     //Para poder controlar a gravação de audio
                    gravando     //Para poder controlar a gravação de audio
            };

            holder.operacoes_button.setTag(informacoes_ROW);
            holder.atividade_text_view.setTag(informacoes_ROW);
            holder.gravar_image.setTag(informacoes_ROW);
        }
        return v;
    }
    //HOLDER

    private class ViewHolder {
        Button operacoes_button;
        TextView atividade_text_view;
        ImageButton gravar_image;
        int posicao;
    }

}