package com.model;

public class Profil {

   // private long id;
   // private String nazwa;
    private double kalorie;
    private double bialko;
    private double wegle;
    private double tluszcz;
    private My_user owner;

    public My_user getOwner() {
        return owner;
    }

    public void setOwner(My_user owner) {
        this.owner = owner;
    }





    /*public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }*/

    public double getKalorie() {
        return kalorie;
    }

    public void setKalorie(double kalorie) {
        this.kalorie = kalorie;
    }

    public double getBialko() {
        return bialko;
    }

    public void setBialko(double bialko) {
        this.bialko = bialko;
    }

    public double getWegle() {
        return wegle;
    }

    public void setWegle(double wegle) {
        this.wegle = wegle;
    }

    public double getTluszcz() {
        return tluszcz;
    }

    public void setTluszcz(double tluszcz) {
        this.tluszcz = tluszcz;
    }

    @Override
    public String toString() {
        return "Profil{" +
                //"id=" + id +
               // ", nazwa='" + nazwa + '\'' +
                ", kalorie=" + kalorie +
                ", bialko=" + bialko +
                ", wegle=" + wegle +
                ", tluszcz=" + tluszcz +
                ", owner=" + owner +
                '}';
    }
}
