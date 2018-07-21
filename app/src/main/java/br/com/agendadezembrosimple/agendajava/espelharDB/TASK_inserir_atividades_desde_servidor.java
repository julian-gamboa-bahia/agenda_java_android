package br.com.agendadezembrosimple.agendajava.espelharDB;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import br.com.agendadezembrosimple.agendajava.DB_registro_atividades.DB_registro_atividades;

/**
 * Created by julian on 26/12/17.
 */

public class TASK_inserir_atividades_desde_servidor extends AsyncTask<String, Void, String> {

        private Context mContext;

        public TASK_inserir_atividades_desde_servidor(Context mContext){
            this.mContext=mContext;

        }

        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {


            if (result.contains("CONETADO")) {

                DB_registro_atividades db_registro_atividades = new DB_registro_atividades(mContext);

                result = result.substring(0, result.indexOf("CONETADO"));

                StringTokenizer st_VIRGULA = new StringTokenizer(result, ",");
                String atividade="";
                Integer indice=0;
                while (st_VIRGULA.hasMoreTokens()) {
                    atividade = st_VIRGULA.nextToken();
                    Log.d("Janeiro03",
                            indice+"\n"
                                    + atividade+"\n"
                                    +db_registro_atividades.conferir_ja_registrado(atividade)
                    );
                    indice++;

                    if(!db_registro_atividades.conferir_ja_registrado(atividade))
                    {
                        db_registro_atividades.cadastrar_nova_atividade(
                                atividade,
                                "hora",
                                "foto",
                                "0",
                                "esconder"
                        );
                    }
                }

            }
        }
        public static String GET(String url) {
            InputStream inputStream = null;
            String result = "";
            try {

                // create HttpClient
                HttpClient httpclient = new DefaultHttpClient();

                // make GET request to the given URL
                HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

                // receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();

                // convert inputstream to string
                if (inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";

            } catch (Exception e) {
                Log.e("InputStream", "tentando um get");
            }

            return result;
        }

        private static String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;

        }
        /////////////
    }