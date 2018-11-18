
package vinkkikirjasto.luokat;

import java.util.ArrayList;

public class Base extends Pohja {
   
    private String isbn;
    private String kirjoittaja;
    private String url;
    private String author;
    private String nimi;
    private String kuvaus;
    
    public Base(int id, String otsikko, String tyyppi, String kommentti, String isbn, String kirjoittaja, ArrayList<Tag> tags) {
        super(id, otsikko, tyyppi, kommentti, tags);
        this.isbn = isbn;
        this.kirjoittaja = kirjoittaja;
    }
    
    public Base(int id, String otsikko, String tyyppi, String kommentti, String url, ArrayList<Tag> tags){
        super(id, otsikko, tyyppi, kommentti, tags);
        this.url = url;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getKirjoittaja() {
        return kirjoittaja;
    }

    public String getUrl() {
        return url;
    }

    public String getAuthor() {
        return author;
    }

    public String getNimi() {
        return nimi;
    }

    public String getKuvaus() {
        return kuvaus;
    }
    
    
    
    @Override
    public String toString() {
        
        String palautettava = "";
        palautettava += "Otsikko: " + this.getOtsikko() + "\n";
        palautettava += "Tyyppi: " + this.getTyyppi() + "\n";
        
        
        if (this.getTyyppi().equals("Kirja")) {
            palautettava += "Kirjoittaja: " + this.getKirjoittaja() + "\n";
            palautettava += "ISBN: " + this.getIsbn() + "\n";
            palautettava += "Kurssit: [INSERT KURSSIT HERE]\n";
            
        }
        
        palautettava += "TAGIT: [INSERT TAGS HERE]\n";
        
        
        return palautettava;
    }
    
    
    
}
