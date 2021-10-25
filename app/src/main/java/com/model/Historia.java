package com.model;

import java.util.Date;

public class Historia {

    private long id;
    private Date kiedy;
    private My_user user;
    private Produkt produkt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getKiedy() {
        return kiedy;
    }

    public void setKiedy(Date kiedy) {
        this.kiedy = kiedy;
    }

    public My_user getUser() {
        return user;
    }

    public void setUser(My_user user) {
        this.user = user;
    }

    public Produkt getProdukt() {
        return produkt;
    }

    public void setProdukt(Produkt produkt) {
        this.produkt = produkt;
    }

}
