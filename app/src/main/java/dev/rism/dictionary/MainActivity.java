package dev.rism.dictionary;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ldoublem.loadingviewlib.LVBlock;
import com.ldoublem.loadingviewlib.LVFunnyBar;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    TextView tvmeaning;
    EditText etword;
    Button bgo;
    LVBlock block;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        block= (LVBlock) findViewById(R.id.lvblock);
        tvmeaning=(TextView) findViewById(R.id.tvmeaning);
        etword=(EditText) findViewById(R.id.editText);
        bgo= (Button) findViewById(R.id.bgo);
        bgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                block.startAnim();
                block.setVisibility(View.VISIBLE);
                new Background().execute(etword.getText().toString().trim());
                InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(etword.getWindowToken(),0);
            }
        });
    }
    class Background extends AsyncTask<String,Integer,String[]>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String[] doInBackground(String... params) {
            String json = null;
            String a[];String b;
            try {
                InputStream is = getApplicationContext().getAssets().open("dictionary.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            Soundex ob=new Soundex();
            b=ob.soundex(params[0]);
            a=new String[]{json,params[0],b};
            return a;
        }

        @Override
        protected void onPostExecute(String s[]) {
            super.onPostExecute(s);
           int i;
            try {
                final Soundex soundex=new Soundex();
                final JSONObject jsonObject = new JSONObject(s[0]);
                String meaning=jsonObject.getString(s[1].toUpperCase());
                                   tvmeaning.setText(meaning);
                   block.stopAnim();
                block.setVisibility(View.INVISIBLE);

                }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
class Background2 extends AsyncTask<String,Integer,String>
{
    @Override
    protected String doInBackground(String... params) {
        try{
        JSONObject jsonObject = new JSONObject(params[0]);
        Iterator<?> keys = jsonObject.keys();
        Soundex soundex1=new Soundex();
        while( keys.hasNext() ) {
            String key = (String)keys.next();
            if (params[1].compareTo(soundex1.soundex(key)) ==0)
            {
                return key;
            }
        }}
        catch (Exception e){e.printStackTrace();}
        return params[1];
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s!=null)
        {
            tvmeaning.setText("Did You Mean :"+s);
        }
    }
}
}
