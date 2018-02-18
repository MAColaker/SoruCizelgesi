package tr.com.alicolalaker.sorucizelgesi;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by MuhammedAli on 31.03.2017.
 */

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int HAKKINDA_PENCERESI = 1;
    private static final int DERS_PENCERESI = 2;
    private static final int TARIH_PENCERESI = 3;
    private static final int DUZENLE_PENCERESI = 4;
    private Dialog pencere = null;
    private long id;
    FloatingActionButton fab;
    TableLayout tablo;
    TextView tv;
    RelativeLayout rl1;
    SharedPreferences ayarlar;
    int hedef=100 , boyut = 14;
    boolean gece_modu;
    AlertDialog.Builder builder_olustur;
    EditText parola_olustur,parola_kaldir,parola_tekrar,parola_eski, parola_yeni, parola_yeni_tekrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gecemodu();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View mesaj = null;
        tv= (TextView) findViewById(R.id.tv);
        tablo = (TableLayout) findViewById(R.id.tablo);


        ayarlar = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ayarlariYap();

        try{
            Listele();
        }catch (Exception e){
            tv.setText(getResources().getString(R.string.kayıt_yok));
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(boyut);
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DERS_PENCERESI);
            }
        });
    }

    private void gecemodu() {

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
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

    private void ayarlariYap() {

        String hedefsoru = ayarlar.getString("hedef","100");
        hedef = Integer.valueOf(hedefsoru.toString());

        String pozisyon = ayarlar.getString("yazi_boyutu","1");
        switch (Integer.valueOf(pozisyon)){
            case 0:
                boyut = 12;
                break;
            case 1:
                boyut = 14;
                break;
            case 2:
                boyut = 18;
                break;
            case 3:
                boyut = 24;
                break;
        }

        try{
            Listele();
        }catch (Exception e){
            tv.setTextSize(boyut);
            tv.setText(getResources().getString(R.string.kayıt_yok));
            tv.setTextColor(Color.BLACK);
        }
        ayarlar.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        ayarlariYap();
        gecemodu();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.sifrex);
        final SharedPreferences prefSettings = getSharedPreferences("", Context.MODE_PRIVATE);
        if(!prefSettings.getString("sifre", "0").toString().trim().equals("0")){
            item.setTitle(getResources().getString(R.string.parola_ayarları));
        }else {
            item.setTitle(getResources().getString(R.string.parola_olustur));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        final SharedPreferences prefSettings = getSharedPreferences("",Context.MODE_PRIVATE);
        switch (id){
            case R.id.action_settings:
                Intent intent=new Intent(getApplicationContext(),Ayarlar.class);
                startActivity(intent);
                return true;

            case R.id.ekle:
                showDialog(DERS_PENCERESI);
                return true;

            case R.id.tarih:
                showDialog(TARIH_PENCERESI);
                return true;

            case R.id.paylas:
                paylas(tv.getText());
                return true;

            case R.id.sifrex:

                String kontrol = prefSettings.getString("sifre", String.valueOf(0));
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                if(kontrol.toString().trim().equals("0")){

                    builder.setTitle(getResources().getString(R.string.parola_olustur));
                    builder.setMessage(getResources().getString(R.string.parola_olus));
                    builder.setIcon(android.R.drawable.ic_lock_lock);
                    builder.setNeutralButton(getResources().getString(R.string.iptal),new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){

                        }
                    });
                    builder.setPositiveButton(getResources().getString(R.string.parola_olustur),new DialogInterface.OnClickListener(){

                        public void onClick(DialogInterface dialog, int id){

                            builder_olustur = new AlertDialog.Builder(MainActivity.this);
                            builder_olustur.setIcon(android.R.drawable.ic_lock_lock);
                            builder_olustur.setTitle(getResources().getString(R.string.parola_olustur));
                            LayoutInflater inflater = getLayoutInflater();
                            View dialoglayout = inflater.inflate(R.layout.parola_olustur, null);
                            parola_olustur = (EditText) dialoglayout.findViewById(R.id.parola_oluıstur);
                            parola_tekrar = (EditText) dialoglayout.findViewById(R.id.parola_tekrar);
                            parola_olustur.addTextChangedListener(parola_olustur_watcher);
                            parola_tekrar.addTextChangedListener(parola_tekrar_watcher);
                            builder_olustur.setView(dialoglayout);
                            builder_olustur.show();
                        }

                    });



                }else {

                    builder.setTitle(getResources().getString(R.string.parola_ayarları));
                    builder.setIcon(android.R.drawable.ic_lock_lock);
                    builder.setPositiveButton(getResources().getString(R.string.parolayi_degistir),new DialogInterface.OnClickListener(){

                        public void onClick(DialogInterface dialog, int id){

                            AlertDialog.Builder builder_değiş = new AlertDialog.Builder(MainActivity.this);
                            builder_değiş.setTitle(getResources().getString(R.string.parolayi_degistir));
                            builder_değiş.setIcon(android.R.drawable.ic_lock_lock);
                            LayoutInflater inflater = getLayoutInflater();
                            View dialoglayout = inflater.inflate(R.layout.parola_degis, null);
                            parola_eski= (EditText) dialoglayout.findViewById(R.id.parola_eski);
                            parola_yeni= (EditText) dialoglayout.findViewById(R.id.parola_yeni);
                            parola_yeni_tekrar= (EditText) dialoglayout.findViewById(R.id.parola_yeni_tekrar);
                            parola_eski.addTextChangedListener(parola_eski_watcher);
                            parola_yeni.addTextChangedListener(parola_yeni_watcher);
                            parola_yeni_tekrar.addTextChangedListener(parola_yeni_tekrar_watcher);
                            builder_değiş.setView(dialoglayout);
                            builder_değiş.show();

                        }

                    });
                    builder.setNegativeButton(getResources().getString(R.string.parolayi_kaldir),new DialogInterface.OnClickListener(){

                        public void onClick(DialogInterface dialog, int id){

                            AlertDialog.Builder builder_kaldır = new AlertDialog.Builder(MainActivity.this);
                            builder_kaldır.setTitle(getResources().getString(R.string.parolayi_kaldir));
                            builder_kaldır.setIcon(android.R.drawable.ic_lock_lock);
                            LayoutInflater inflater = getLayoutInflater();
                            View dialoglayout = inflater.inflate(R.layout.parola_kaldir, null);
                            parola_kaldir= (EditText) dialoglayout.findViewById(R.id.parola_kaldir);
                            parola_kaldir.addTextChangedListener(parola_kaldir_watcher);
                            builder_kaldır.setView(dialoglayout);
                            builder_kaldır.show();
                        }
                    });
                }
                builder.show();

                return true;

            case R.id.hakkinda:

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.hakkinda, null);
                alertDialog.setView(dialogView);

                alertDialog.setTitle(getResources().getString(R.string.hakkinda));

                alertDialog.setIcon(R.drawable.ic_perm_device_information);

                alertDialog.setNeutralButton(R.string.tamam,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dialog.cancel();
                    }
                });

                alertDialog.show();

                return true;

            case R.id.tum_veriyi_sil:
                TumKayitlariSil();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {


        switch (id){
            case DERS_PENCERESI:
                pencere=getPencereEkle();
                break;

            case HAKKINDA_PENCERESI:
                break;

            case TARIH_PENCERESI:
                pencere=getIkiTarih();
                break;

            case DUZENLE_PENCERESI:
                pencere=getPencereDuzenle();
                break;

            default:
                pencere=null;
        }

        return pencere;
    }

    private void TumKayitlariSil(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setTitle(getResources().getString(R.string.tumunu_sil));

        alertDialog.setMessage(getResources().getString(R.string.tum_kayitlari_sil));

        alertDialog.setIcon(android.R.drawable.ic_delete)

        .setPositiveButton(getResources().getString(R.string.sil),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        Veritabani db=new Veritabani(getApplicationContext());
                        db.Sil();
                        try {
                            Listele();
                        }catch (Exception e){
                            tv.setText(getResources().getString(R.string.kayıt_yok));
                            tv.setTextColor(Color.BLACK);
                            tv.setTextSize(boyut);
                        }
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.tum_kayitlar_silindi), Toast.LENGTH_SHORT).show();
                    }
                })

        .setNegativeButton(getResources().getString(R.string.iptalB),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,    int which) {

                        dialog.cancel();
                    }
                });

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.cancel();
            }
        });


        alertDialog.show();
    }

    private Dialog getPencereEkle() {


        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.ders_ekle,null);

        Button kaydet= (Button) layout.findViewById(R.id.kaydet);
        Button iptal= (Button) layout.findViewById(R.id.iptal);
        final EditText soruSayisi= (EditText) layout.findViewById(R.id.et);
        final Spinner spinner= (Spinner) layout.findViewById(R.id.sp);
        final DatePicker datePicker= (DatePicker) layout.findViewById(R.id.dp);

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.kayit_ekle));
        builder.setView(layout);
        builder.setIcon(android.R.drawable.ic_input_add);

        final AlertDialog pencere=builder.create();


        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    int gun=datePicker.getDayOfMonth();
                    int ay=datePicker.getMonth()+1;
                    int yil=datePicker.getYear();

                    Date date = null;

                    java.text.DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                    try {
                        date = df.parse(gun+"/"+ay+"/"+yil);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long tarih = date.getTime();

                    int pozisyon=spinner.getSelectedItemPosition();
                    String ders=(String) spinner.getItemAtPosition(pozisyon);

                    int soru_sayisi = Integer.valueOf(soruSayisi.getText().toString());

                    Ogrenci ogrenci = new Ogrenci(ders,soru_sayisi,tarih);

                    Veritabani db = new Veritabani(getApplicationContext());
                    boolean soruekleme = db.KayitEkle(ogrenci);


                    if(soruekleme){
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.soru_eklendi), Toast.LENGTH_SHORT).show();
                        Listele();
                        soruSayisi.setText("");
                    }
                    else
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.hata_olustu), Toast.LENGTH_SHORT).show();



                    pencere.dismiss();

                }catch (Exception e){
                    Snackbar.make(v, getResources().getString(R.string.soru_girin), Snackbar.LENGTH_LONG).show();
                }


            }
        });


        iptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pencere.cancel();
            }
        });


        return pencere;
    }

    private Dialog getPencereDuzenle() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.ders_ekle,null);

        Button kaydet= (Button) layout.findViewById(R.id.kaydet);
        Button iptal= (Button) layout.findViewById(R.id.iptal);
        final EditText soruSayisi= (EditText) layout.findViewById(R.id.et);
        final Spinner spinner= (Spinner) layout.findViewById(R.id.sp);
        final DatePicker datePicker= (DatePicker) layout.findViewById(R.id.dp);

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.kayit_duzenleme));
        builder.setIcon(R.drawable.ic_border_color);
        builder.setView(layout);

        final AlertDialog pencere=builder.create();


        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    int gun=datePicker.getDayOfMonth();
                    int ay=datePicker.getMonth()+1;
                    int yil=datePicker.getYear();

                    Date date = null;

                    java.text.DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                    try {
                        date = df.parse(gun+"/"+ay+"/"+yil);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long tarih = date.getTime();

                    int pozisyon=spinner.getSelectedItemPosition();
                    String ders=(String) spinner.getItemAtPosition(pozisyon);

                    int soru_sayisi = Integer.valueOf(soruSayisi.getText().toString());

                    Ogrenci ogrenci = new Ogrenci(ders,soru_sayisi,tarih);

                    Veritabani db = new Veritabani(getApplicationContext());
                    boolean kayitduzenleme = db.Guncelle(id,tarih,ders,soru_sayisi);


                    if(kayitduzenleme){
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.kayit_guncel), Toast.LENGTH_SHORT).show();
                        Listele();
                        soruSayisi.setText("");

                    }
                    else
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.hata_olustu), Toast.LENGTH_SHORT).show();



                    pencere.dismiss();

                }catch (Exception e){
                    Snackbar.make(v, getResources().getString(R.string.soru_girin), Snackbar.LENGTH_LONG).show();
                }


            }
        });

        iptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pencere.cancel();
            }
        });

        return pencere;
    }

    private void Listele() {

        tablo.removeAllViews();

        Veritabani db = new Veritabani(getApplicationContext());

        List<Ogrenci> ogrenciList=new ArrayList<Ogrenci>();
        ogrenciList=db.Listeleme();

        long en_kucuk = ogrenciList.get(ogrenciList.size()-1).getTarih();
        long en_buyuk = ogrenciList.get(0).getTarih();

        Date fark = new Date(en_buyuk-en_kucuk);
        int fark_gun = ((fark.getYear() % 70)* 365) + (fark.getMonth() * 30) + (fark.getDate() -1);
        fark_gun++;

        int toplam_saru=0;
        for(Ogrenci ogrenci:ogrenciList){

            toplam_saru=toplam_saru+ogrenci.getSoruSayisi();
        }

        tv.setTextSize(25);
        int ortalama_soru = toplam_saru/fark_gun;

        if(ortalama_soru>=hedef){
            tv.setText("Tebrikler. Günlük ortalama "+hedef+ " soru hedefinizi aştınız.\nToplam çözülen soru sayısı: "+toplam_saru+
            "\nGünlük ortalama soru sayısı: "+ortalama_soru);
            tv.setTextColor(Color.parseColor("#4CAF50"));
        }else {
            tv.setText("Maalesef günlük ortalama "+hedef+" soru hedefinizi geçemediniz.\nToplam çözülen soru sayısı: "+toplam_saru+
                    "\nGünlük ortalama soru sayısı: "+ortalama_soru);
            tv.setTextColor(Color.RED);
        }

        for(final Ogrenci ogrenci:ogrenciList){
            final TableRow satir = new TableRow(getApplicationContext());
            satir.setOrientation(TableRow.HORIZONTAL);
            satir.setPadding(10,10,10,10);
            satir.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
            satir.setGravity(Gravity.CENTER);


            final TextView tv_tarih = new TextView(this);
            tv_tarih.setPadding(2,30,2,30);
            tv_tarih.setTextColor(Color.BLACK);
            tv_tarih.setTextSize(boyut);

            java.text.DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date(ogrenci.getTarih());
            tv_tarih.setText(df.format(date)+"   ");

            final TextView tv_ders = new TextView(this);
            tv_ders.setPadding(2,30,2,30);
            tv_ders.setTextColor(Color.BLACK);
            tv_ders.setTextSize(boyut);
            tv_ders.setText(ogrenci.getDersler()+"   ");

            final TextView tv_soru = new TextView(this);
            tv_soru.setPadding(2,30,2,30);
            tv_soru.setTextColor(Color.BLACK);
            tv_soru.setTextSize(boyut);
            tv_soru.setText(String.valueOf(ogrenci.getSoruSayisi()));

            satir.addView(tv_tarih);
            satir.addView(tv_ders);
            satir.addView(tv_soru);

            tablo.addView(satir);

            satir.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    id=ogrenci.getId();

                    satir.setBackgroundResource(android.R.drawable.dialog_holo_dark_frame);
                    tv_tarih.setTextColor(Color.WHITE);
                    tv_ders.setTextColor(Color.WHITE);
                    tv_soru.setTextColor(Color.WHITE);

                    final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(MainActivity.this);
                    View sheetView = getLayoutInflater().inflate(R.layout.contex_menu, null);
                    mBottomSheetDialog.setContentView(sheetView);
                    mBottomSheetDialog.show();

                    LinearLayout duzenle = (LinearLayout) sheetView.findViewById(R.id.duzenle_menu);
                    LinearLayout sil = (LinearLayout) sheetView.findViewById(R.id.sil_menu);

                    duzenle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            showDialog(DUZENLE_PENCERESI);

                            satir.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
                            tv_tarih.setTextColor(Color.BLACK);
                            tv_ders.setTextColor(Color.BLACK);
                            tv_soru.setTextColor(Color.BLACK);

                            mBottomSheetDialog.dismiss();
                        }
                    });

                    sil.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Veritabani db=new Veritabani(getApplicationContext());
                            db.Sil(id);
                            try {
                                Listele();
                            }catch (Exception e){
                                tv.setText(getResources().getString(R.string.kayıt_yok));
                                tv.setTextColor(Color.BLACK);
                                tv.setTextSize(boyut);
                            }

                            mBottomSheetDialog.dismiss();
                        }
                    });

                    mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            satir.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
                            tv_tarih.setTextColor(Color.BLACK);
                            tv_ders.setTextColor(Color.BLACK);
                            tv_soru.setTextColor(Color.BLACK);
                        }
                    });

                    return true;
                }
            });

        }
    }

    private Dialog getIkiTarih() {

        LayoutInflater inflater= LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.tarihler,null);

        Button kaydet = (Button) layout.findViewById(R.id.kaydet2);
        Button iptal = (Button) layout.findViewById(R.id.iptal2);
        final DatePicker dp_ilk = (DatePicker) layout.findViewById(R.id.dp_ilk);
        final DatePicker dp_son = (DatePicker) layout.findViewById(R.id.dp_son);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.ikitarih));
        builder.setIcon(R.drawable.ic_event);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();

        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                int gun_ilk = dp_ilk.getDayOfMonth();
                int ay_ilk = dp_ilk.getMonth() + 1;
                int yil_ilk = dp_ilk.getYear();



                Date date = null;
                try {
                    date = df.parse(gun_ilk + "/" + ay_ilk + "/" + yil_ilk);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long tarih_ilk = date.getTime();



                int gun_son = dp_son.getDayOfMonth();
                int ay_son = dp_son.getMonth() + 1;
                int yil_son = dp_son.getYear();



                Date date_son = null;
                try {
                    date_son = df.parse(gun_son + "/" + ay_son + "/" + yil_son);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long tarih_son = date_son.getTime();


                Veritabani db=new Veritabani(getApplicationContext());


                List<Ogrenci> ogrenciList=new ArrayList<Ogrenci>();
                ogrenciList=db.IkiTarihArasi(tarih_ilk,tarih_son);

                tablo.removeAllViews();

                tv.setTextSize(boyut);
                tv.setVisibility(View.GONE);

                try {
                    for(final Ogrenci ogrenci:ogrenciList){
                        final TableRow satir = new TableRow(getApplicationContext());
                        satir.setOrientation(TableRow.HORIZONTAL);
                        satir.setPadding(10,10,10,10);
                        satir.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
                        satir.setGravity(Gravity.CENTER);


                        final TextView tv_tarih = new TextView(getApplicationContext());
                        tv_tarih.setPadding(2,30,2,30);
                        tv_tarih.setTextSize(boyut);
                        tv_tarih.setTextColor(Color.BLACK);

                        df = new SimpleDateFormat("dd/MM/yyyy");
                        date = new Date(ogrenci.getTarih());
                        tv_tarih.setText(df.format(date)+"   ");

                        final TextView tv_ders = new TextView(getApplicationContext());
                        tv_ders.setPadding(2,30,2,30);
                        tv_ders.setTextColor(Color.BLACK);
                        tv_ders.setTextSize(boyut);
                        tv_ders.setText(ogrenci.getDersler()+"   ");

                        final TextView tv_soru = new TextView(getApplicationContext());
                        tv_soru.setPadding(2,30,2,30);
                        tv_soru.setTextColor(Color.BLACK);
                        tv_soru.setTextSize(boyut);
                        tv_soru.setText(String.valueOf(ogrenci.getSoruSayisi()));

                        satir.addView(tv_tarih);
                        satir.addView(tv_ders);
                        satir.addView(tv_soru);

                        tablo.addView(satir);

                        satir.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {

                                id=ogrenci.getId();

                                satir.setBackgroundResource(android.R.drawable.dialog_holo_dark_frame);
                                tv_tarih.setTextColor(Color.WHITE);
                                tv_ders.setTextColor(Color.WHITE);
                                tv_soru.setTextColor(Color.WHITE);

                                final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(MainActivity.this);
                                View sheetView = getLayoutInflater().inflate(R.layout.contex_menu, null);
                                mBottomSheetDialog.setContentView(sheetView);
                                mBottomSheetDialog.show();

                                LinearLayout duzenle = (LinearLayout) sheetView.findViewById(R.id.duzenle_menu);
                                LinearLayout sil = (LinearLayout) sheetView.findViewById(R.id.sil_menu);

                                duzenle.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        showDialog(DUZENLE_PENCERESI);

                                        satir.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
                                        tv_tarih.setTextColor(Color.BLACK);
                                        tv_ders.setTextColor(Color.BLACK);
                                        tv_soru.setTextColor(Color.BLACK);

                                        mBottomSheetDialog.dismiss();
                                    }
                                });

                                sil.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Veritabani db=new Veritabani(getApplicationContext());
                                        db.Sil(id);
                                        try {
                                            Listele();
                                        }catch (Exception e){
                                            tv.setText(getResources().getString(R.string.kayıt_yok));
                                            tv.setTextColor(Color.BLACK);
                                            tv.setTextSize(boyut);
                                        }

                                        mBottomSheetDialog.dismiss();
                                    }
                                });

                                mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        satir.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
                                        tv_tarih.setTextColor(Color.BLACK);
                                        tv_ders.setTextColor(Color.BLACK);
                                        tv_soru.setTextColor(Color.BLACK);
                                    }
                                });

                                return true;
                            }
                        });

                    }
                }catch (Exception e){
                    tv.setVisibility(View.VISIBLE);
                    tv.setText(df.format(date)+" ile "+df.format(date_son)+" Tarihleri Arasında"+
                            "\n"+getResources().getString(R.string.kayıt_yok));
                    tv.setTextColor(R.attr.colorPrimary);
                }

                fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setImageResource(R.drawable.ic_autorenew);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tv.setVisibility(View.VISIBLE);
                        fab.setImageResource(R.drawable.ic_add_x);

                        try {
                            Listele();
                        }catch (Exception e){
                            tv.setText(getResources().getString(R.string.kayıt_yok));
                            tv.setTextColor(Color.BLACK);
                            tv.setTextSize(boyut);
                        }

                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showDialog(DERS_PENCERESI);
                            }
                        });
                    }
                });

                dialog.dismiss();

            }
        });


        iptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        return dialog;
    }

    private void paylas(CharSequence mesaj){
        Intent paylasIntent = new Intent(Intent.ACTION_SEND);
        paylasIntent.setType("text/plain");
        paylasIntent.putExtra(Intent.EXTRA_TEXT,mesaj);
        startActivity(Intent.createChooser(paylasIntent,getResources().getString(R.string.paylas)));
    }

    /**---------------------------------------------------------------------------------------*/

    TextWatcher parola_olustur_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if(parola_olustur.getText().toString().length()==4){

                parola_tekrar.setVisibility(parola_tekrar.VISIBLE);
                parola_olustur.setFocusable(false);
                parola_tekrar.setFocusable(true);
                parola_tekrar.setFocusableInTouchMode(true);
            }
            else
            {
                parola_tekrar.setVisibility(parola_tekrar.GONE);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };



    TextWatcher parola_tekrar_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = prefSettings.edit();

            if(parola_olustur.getText().toString().equals(parola_tekrar.getText().toString())){

                Toast.makeText(MainActivity.this, getResources().getString(R.string.parola_olusturuldu), Toast.LENGTH_SHORT).show();

                editor.putString("sifre",parola_tekrar.getText().toString()).commit();

                Intent intent = new Intent(getApplicationContext(),Sifre.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    TextWatcher parola_kaldir_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = prefSettings.edit();


            if(parola_kaldir.getText().toString().trim().equals(prefSettings.getString("sifre","null"))){

                Toast.makeText(MainActivity.this, getResources().getString(R.string.parola_kaldirildi), Toast.LENGTH_SHORT).show();

                editor.putString("sifre","0").commit();

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
            else if(parola_kaldir.getText().toString().length()==4){

                parola_kaldir.setText("");

            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    TextWatcher parola_eski_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = prefSettings.edit();

            if(parola_eski.getText().toString().trim().equals(prefSettings.getString("sifre","0"))){

                parola_yeni.setVisibility(parola_yeni.VISIBLE);
                parola_eski.setFocusable(false);
                parola_yeni.setFocusable(true);
                parola_yeni.setFocusableInTouchMode(true);
            }
            else
            {
                parola_yeni.setVisibility(parola_yeni.GONE);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    TextWatcher parola_yeni_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if(parola_yeni.getText().toString().length()==4){

                parola_yeni_tekrar.setVisibility(parola_yeni.VISIBLE);
                parola_yeni.setFocusable(false);
                parola_yeni_tekrar.setFocusable(true);
                parola_yeni_tekrar.setFocusableInTouchMode(true);
            }
            else
            {
                parola_yeni_tekrar.setVisibility(parola_yeni.GONE);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    TextWatcher parola_yeni_tekrar_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);

            final SharedPreferences.Editor editor = prefSettings.edit();

            if(parola_yeni_tekrar.getText().toString().trim().equals(parola_yeni.getText().toString().trim())){

                Toast.makeText(MainActivity.this, getResources().getString(R.string.parola_degistirildi), Toast.LENGTH_SHORT).show();
                editor.putString("sifre",parola_yeni_tekrar.getText().toString()).commit();

                Intent intent = new Intent(getApplicationContext(),Sifre.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

}