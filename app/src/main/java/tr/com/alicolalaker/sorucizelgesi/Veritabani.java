package tr.com.alicolalaker.sorucizelgesi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MuhammedAli on 31.03.2017.
 */

public class Veritabani extends SQLiteOpenHelper{

    private static final String VERITABANI_ISMI = "veritabani";
    private static final int VERITABANI_VERSIYONU = 1;
    private static final String TABLO_ISMI = "soru_cizelgesi";

    private static final String ID ="_id";
    private static final String DERS ="ders";
    private static final String SORUSAYISI ="soru_sayisi";
    private static final String TARIH ="tari";

    public Veritabani(Context context) {
        super(context, VERITABANI_ISMI, null, VERITABANI_VERSIYONU);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String tablo_olustur="CREATE TABLE "+TABLO_ISMI+
                " ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                DERS+" TEXT, "+
                SORUSAYISI+" INTEGER NOT NULL, "+
                TARIH+" INTEGER NOT NULL );";

        db.execSQL(tablo_olustur);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLO_ISMI);
        onCreate(db);
    }

    public boolean KayitEkle(Ogrenci ogrenci) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DERS,ogrenci.getDersler());
        cv.put(SORUSAYISI,ogrenci.getSoruSayisi());
        cv.put(TARIH,ogrenci.getTarih());

        long id = db.insert(TABLO_ISMI,null,cv);

        db.close();

        if (id==-1)
            return false;
        else
            return true;
    }

    public List<Ogrenci> Listeleme() {
        SQLiteDatabase db = this.getReadableDatabase();

        String [] sutunlar = new String[]{DERS,SORUSAYISI,TARIH,ID};

        Cursor c =db.query(TABLO_ISMI,sutunlar,null,null,null,null,TARIH+" desc");

        int derssirano=c.getColumnIndex(DERS);
        int sorusirano=c.getColumnIndex(SORUSAYISI);
        int tarihsirano=c.getColumnIndex(TARIH);
        int idsirano=c.getColumnIndex(ID);

        List<Ogrenci> ogrenciList = new ArrayList<Ogrenci>();

        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            Ogrenci ogrenci = new Ogrenci();

            ogrenci.setDersler(c.getString(derssirano));
            ogrenci.setSoruSayisi(c.getInt(sorusirano));
            ogrenci.setTarih(c.getLong(tarihsirano));
            ogrenci.setId(c.getLong(idsirano));

            ogrenciList.add(ogrenci);

        }

        db.close();

        return ogrenciList;
    }

    public void Sil(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLO_ISMI,ID+"="+id,null);
        db.close();
    }

    public void Sil() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLO_ISMI,null,null);
        db.close();
    }

    public boolean Guncelle(long id,long tarih,String ders,int soru){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DERS,ders);
        cv.put(TARIH,tarih);
        cv.put(SORUSAYISI,soru);

        long idx = db.update(TABLO_ISMI,cv,ID+"="+id,null);

        db.close();

        if (idx==-1)
            return false;
        else
            return true;
    }

    public List<Ogrenci> IkiTarihArasi(long tarih_ilk, long tarih_son) {

        SQLiteDatabase db = this.getReadableDatabase();

        String [] sutunlar = new String[]{DERS,SORUSAYISI,TARIH,ID};
        String ilk_tarih = String.valueOf(tarih_ilk);
        String son_tarih = String.valueOf(tarih_son);
        String [] tarihler = new String[]{ilk_tarih,son_tarih};

        Cursor c=db.query(TABLO_ISMI, sutunlar,TARIH+ " BETWEEN ? AND ?", tarihler, null, null,TARIH+" desc");

        int derssirano=c.getColumnIndex(DERS);
        int sorusirano=c.getColumnIndex(SORUSAYISI);
        int tarihsirano=c.getColumnIndex(TARIH);
        int idsirano=c.getColumnIndex(ID);

        List<Ogrenci> ogrenciList = new ArrayList<Ogrenci>();

        /**
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            Ogrenci ogrenci = new Ogrenci();

            ogrenci.setDersler(c.getString(derssirano));
            ogrenci.setSoruSayisi(c.getInt(sorusirano));
            ogrenci.setTarih(c.getLong(tarihsirano));
            ogrenci.setId(c.getLong(idsirano));

            ogrenciList.add(ogrenci);

        }
         */

        if(c.moveToFirst()){

            do{
                Ogrenci ogrenci = new Ogrenci();

                ogrenci.setDersler(c.getString(derssirano));
                ogrenci.setSoruSayisi(c.getInt(sorusirano));
                ogrenci.setTarih(c.getLong(tarihsirano));
                ogrenci.setId(c.getLong(idsirano));

                ogrenciList.add(ogrenci);
            }while (c.moveToNext());

        }else {
            ogrenciList = null;
        }


        db.close();

        return ogrenciList;
    }
}
