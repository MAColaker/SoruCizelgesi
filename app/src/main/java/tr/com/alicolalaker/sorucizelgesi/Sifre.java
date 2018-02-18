package tr.com.alicolalaker.sorucizelgesi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by MuhammedAli on 15.04.2017.
 */

public class Sifre extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    TextView b1,b2,b3,b4,b5,b6,b7,b8,b9,b0,b_sil,b_iptal;
    EditText et1,et2,et3,et4;
    boolean gece_modu;
    int  sayac=0;
    int kontrol=0;
    int dizi[] = new int[4];
    int sifre[] =new int[4];
    ConstraintLayout arkaplan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gecemodu();

        final SharedPreferences prefSettings = getSharedPreferences("", Context.MODE_PRIVATE);

        if (prefSettings.getString("sifre", "0").toString().trim().equals("0")) {

            setContentView(R.layout.yenileme);

            Thread thread=  new Thread(){
                @Override
                public void run(){
                    try {
                        synchronized(this){
                            wait(800);
                        }
                    }
                    catch(InterruptedException ex){
                    }

                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            };

            thread.start();
        }else {

            setContentView(R.layout.activity_sifre);

            b1 = (TextView) findViewById(R.id.button1);
            b2 = (TextView) findViewById(R.id.button2);
            b3 = (TextView) findViewById(R.id.button3);
            b4 = (TextView) findViewById(R.id.button4);
            b5 = (TextView) findViewById(R.id.button5);
            b6 = (TextView) findViewById(R.id.button6);
            b7 = (TextView) findViewById(R.id.button7);
            b8 = (TextView) findViewById(R.id.button8);
            b9 = (TextView) findViewById(R.id.button9);
            b_sil = (TextView) findViewById(R.id.button_sil);
            b_iptal = (TextView) findViewById(R.id.button_iptal);
            b0 = (TextView) findViewById(R.id.button0);
            et1 = (EditText) findViewById(R.id.sifre_editText1);
            et2 = (EditText) findViewById(R.id.sifre_editText2);
            et3 = (EditText) findViewById(R.id.sifre_editText3);
            et4 = (EditText) findViewById(R.id.sifre_editText4);

            int sifre_al = Integer.parseInt(prefSettings.getString("sifre", "0"));

            int i = 1; //Yazdırılacak olan değişken.
            int a = 1; //Basamak değeri.
            int say = 3;

            while (sifre_al != 0) {
                i = sifre_al % 10;
                sifre_al = sifre_al / 10;
                System.out.println(i * a);
                a = a * 10;
                sifre[say] = i;
                say = say - 1;
            }

            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Buton(1,"1");
                }
            });

            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Buton(2,"2");
                }
            });

            b3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Buton(3,"3");
                }
            });

            b4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Buton(4,"4");
                }
            });

            b5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Buton(5,"5");
                }
            });

            b6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Buton(6,"6");
                }
            });

            b7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Buton(7,"7");
                }
            });

            b8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Buton(8,"8");
                }
            });

            b9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Buton(9,"9");
                }
            });

            b_sil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sil();
                }
            });

            b_iptal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.exit(-1);
                }
            });

            b0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Buton(0,"0");
                }
            });
        }

        if(!prefSettings.getString("sifre", "0").toString().trim().equals("0")){
            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(Sifre.this);
            SharedPreferences.Editor e =sharedPreferences.edit();
            gece_modu = sharedPreferences.getBoolean("gece_modu",false);

            arkaplan = (ConstraintLayout) findViewById(R.id.arkaplan);

            if(gece_modu) {
                renkler("#303030");
            }
            else{
                renkler("#303F9F");
            }
        }
    }

    public void Buton(int sayi1, String sayi2){
        dizi[sayac] = sayi1;
        sayac += 1;
        sonraki(sayi2);
    }

    public void renkler(String kod){
        arkaplan.setBackgroundColor(Color.parseColor(kod));
        b0.setBackgroundColor(Color.parseColor(kod));
        b1.setBackgroundColor(Color.parseColor(kod));
        b2.setBackgroundColor(Color.parseColor(kod));
        b3.setBackgroundColor(Color.parseColor(kod));
        b4.setBackgroundColor(Color.parseColor(kod));
        b5.setBackgroundColor(Color.parseColor(kod));
        b6.setBackgroundColor(Color.parseColor(kod));
        b7.setBackgroundColor(Color.parseColor(kod));
        b8.setBackgroundColor(Color.parseColor(kod));
        b9.setBackgroundColor(Color.parseColor(kod));
        b_iptal.setBackgroundColor(Color.parseColor(kod));
        b_sil.setBackgroundColor(Color.parseColor(kod));
    }

    public void sonraki(String sayi){

        if(sayac==1){

            et1.setFocusable(false);et1.setText(sayi);
            et2.setFocusableInTouchMode(true);
            et2.setFocusable(true);
            et3.setFocusable(false);
            et4.setFocusable(false);

        }else if(sayac==2){

            et1.setFocusable(false);et1.setText("*");
            et2.setFocusable(false);et2.setText(sayi);
            et3.setFocusableInTouchMode(true);
            et3.setFocusable(true);
            et4.setFocusable(false);

        }else if(sayac==3){
            et1.setFocusable(false);
            et2.setFocusable(false);et2.setText("*");
            et3.setFocusable(false);et3.setText(sayi);
            et4.setFocusableInTouchMode(true);
            et4.setFocusable(true);

        }else if(sayac==4){

            et3.setText("*");
            et4.setText(String.valueOf(sayi));

            for(int say=0; say<4; say++)
            {
                if(dizi[say]!=sifre[say]){

                    sayac=0;

                    et1.setFocusable(true);
                    et1.setFocusableInTouchMode(true);
                    et2.setFocusable(false);
                    et3.setFocusable(false);
                    et4.setFocusable(false);

                    et1.setText("");
                    et2.setText("");
                    et3.setText("");
                    et4.setText("");

                    kontrol=1;break;
                }
            }

            if(kontrol==1){

                kontrol=0;
                Toast.makeText(this, "Parola Hatalı", Toast.LENGTH_SHORT).show();

            }else {
                Thread thread=  new Thread(){
                    @Override
                    public void run(){
                        try {
                            synchronized(this){
                                wait(100);
                            }
                        }
                        catch(InterruptedException ex){
                        }

                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                };

                thread.start();
            }
        }
    }

    public void onceki(String sayi){

        if(sayac==0){
            et1.setFocusable(true);et1.setText(sayi);
            et1.setFocusableInTouchMode(true);
            et2.setFocusable(false);
            et3.setFocusable(false);
            et4.setFocusable(false);

        }else if(sayac==1){
            et1.setFocusable(false);
            et2.setFocusable(true);et2.setText(sayi);
            et2.setFocusableInTouchMode(true);
            et3.setFocusable(false);
            et4.setFocusable(false);

        }else if(sayac==2){

            et1.setFocusable(false);
            et2.setFocusable(false);et3.setText(sayi);
            et3.setFocusable(true);
            et3.setFocusableInTouchMode(true);
            et4.setFocusable(false);

        }else if(sayac==3){

            et1.setFocusable(false);
            et2.setFocusable(false);
            et3.setFocusable(false);
            et3.setFocusableInTouchMode(true);
            et4.setFocusable(true);

        }
    }

    public void sil(){

        if(sayac!=0)
        {
            sayac-=1;
            onceki("");
        }

    }

    private void gecemodu() {

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(Sifre.this);
        SharedPreferences.Editor e =sharedPreferences.edit();
        gece_modu = sharedPreferences.getBoolean("gece_modu",false);



        if(gece_modu) {
            setTheme(R.style.GeceModu_NoActionBar);

            e.putString("gecekontrol","1").commit();
        }
        else{
            e.putString("gecekontrol","0").commit();
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        gecemodu();
    }
}