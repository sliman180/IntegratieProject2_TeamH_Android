package be.kdg.kandoe.kandoeandroid.pojo.response;

public class Hoofdthema {
    private int id;
    private String naam;
    private String beschrijving;
    private Organisatie organisatie;
    private Gebruiker gebruiker;
//    private List<Subthema> subthemas = new ArrayList<>();
//    private List<Tag> tags = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public Organisatie getOrganisatie() {
        return organisatie;
    }

    public void setOrganisatie(Organisatie organisatie) {
        this.organisatie = organisatie;
    }

    public Gebruiker getGebruiker() {
        return gebruiker;
    }

    public void setGebruiker(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;
    }

//    public List<Subthema> getSubthemas() {
//        return subthemas;
//    }
//
//    public void setSubthemas(List<Subthema> subthemas) {
//        this.subthemas = subthemas;
//    }
//
//    public List<Tag> getTags() {
//        return tags;
//    }
//
//    public void setTags(List<Tag> tags) {
//        this.tags = tags;
//    }
}
