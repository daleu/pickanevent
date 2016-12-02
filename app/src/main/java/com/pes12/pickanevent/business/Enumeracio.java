package com.pes12.pickanevent.business;

/**
 * Created by p4 on 29/11/16.
 */

public enum Enumeracio {

    TestEnum(1, "Testeig", "DEFAULT_TESTEIG"),
    TestEnum2(2, "Testeig", "DEFAULT_TESTEIG");

    private int codi;
    private String nom;
    private String literal;

    Enumeracio(int codi, String nom, String literal) {
        this.codi = codi;
        this.nom = nom;
        this.literal = literal;
    }

    public static Enumeracio getByCode(int codi) {
        for (Enumeracio e : Enumeracio.values()) {
            if (e.getCodi() == codi)
                return e;
        }
        return null;
    }

    public int getCodi() {
        return codi;
    }

    public void setCodi(int codi) {
        this.codi = codi;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
        this.literal = literal;
    }
}
