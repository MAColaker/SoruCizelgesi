package tr.com.alicolalaker.sorucizelgesi;

/**
 * Created by MuhammedAli on 31.03.2017.
 */

public class Ogrenci {
    private String Dersler;
    private int SoruSayisi;
    private long Tarih;
    private long id;

    public Ogrenci() {
    }

    public Ogrenci(String ders, int soru, long tarih) {
        setDersler(ders);
        setSoruSayisi(soru);
        setTarih(tarih);
    }

    public void setId(long id){this.id=id;}

    public long getId(){return id;}

    public String getDersler() {
        return Dersler;
    }

    public void setDersler(String dersler) {
        Dersler = dersler;
    }

    public int getSoruSayisi() {
        return SoruSayisi;
    }

    public void setSoruSayisi(int soruSayisi) {
        SoruSayisi = soruSayisi;
    }

    public long getTarih() {
        return Tarih;
    }

    public void setTarih(long tarih) {
        Tarih = tarih;
    }
}
