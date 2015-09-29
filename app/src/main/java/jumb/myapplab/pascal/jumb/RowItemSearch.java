package jumb.myapplab.pascal.jumb;

public class RowItemSearch {
    private String ID;
    private String Vorname;
    private String Nachname;
    private String Strasse;
    private String Plz;
    private String Ort;
    private String Festnetz;
    private String Mobil;
    private String Email;
    private String Geburtsdatum;
     
    public RowItemSearch(String ID, String Vorname, String Nachname, String Strasse, String Plz, String Ort, String Festnetz, String Mobil, String Email, String Geburtsdatum) {
        this.ID = ID;
        this.Vorname = Vorname;
        this.Nachname = Nachname;
        this.Strasse = Strasse;
        this.Plz = Plz;
        this.Ort = Ort;
        this.Festnetz = Festnetz;
        this.Mobil = Mobil;
        this.Email = Email;
        this.Geburtsdatum = Geburtsdatum;
    }

    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }

    public String getVorname() {
        return Vorname;
    }
    public void setVorname(String Vorname) {
        this.Vorname = Vorname;
    }
    
    public String getNachname() {
        return Nachname;
    }
    public void setNachname(String Nachname) {
        this.Nachname = Nachname;
    }
    
    public String getStrasse() {
        return Strasse;
    }
    public void setStrasse(String Strasse) {
        this.Strasse = Strasse;
    }
    
    public String getPlz() {
        return Plz;
    }
    public void setPlz(String Plz) {
        this.Plz = Plz;
    }
    
    public String getOrt() {
        return Ort;
    }
    public void setOrt(String Ort) {
        this.Ort = Ort;
    }
    
    public String getFestnetz() {
        return Festnetz;
    }
    public void setFestnetz(String Festnetz) {
        this.Festnetz = Festnetz;
    }
    
    public String getMobil() {
        return Mobil;
    }
    public void setMobil(String Mobil) {
        this.Mobil = Mobil;
    }
    
    public String getEmail() {
        return Email;
    }
    public void setEmail(String Email) {
        this.Email = Email;
    }
    
    public String getGeburtsdatum() {
        return Geburtsdatum;
    }
    public void setGeburtsdatum(String Geburtsdatum) {
        this.Geburtsdatum = Geburtsdatum;
    }
     
}