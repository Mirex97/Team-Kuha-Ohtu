package vinkkikirjasto.luokat;

import java.util.List;

public class Pohja {

    private int id;
    private String otsikko;
    private String tyyppi;
    private String kommentti;
    private List<Tag> tags;

    public Pohja(int id, String otsikko, String tyyppi, String kommentti, List<Tag> tags) {
        this.id = id;
        this.otsikko = otsikko;
        this.tyyppi = tyyppi;
        this.kommentti = kommentti;
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOtsikko() {
        return otsikko;
    }

    public void setOtsikko(String otsikko) {
        this.otsikko = otsikko;
    }

    public String getTyyppi() {
        return tyyppi;
    }

    public void setTyyppi(String tyyppi) {
        this.tyyppi = tyyppi;
    }

    public String getKommentti() {
        return kommentti;
    }

    public void setKommentti(String kommentti) {
        this.kommentti = kommentti;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    

}
