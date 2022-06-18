package aiou.muslim.mttech.Models;

public class NamesModel {

    private String name, transliteration, meaning;

    public NamesModel() {
    }

    public NamesModel(String name, String transliteration, String meaning) {
        this.name = name;
        this.transliteration = transliteration;
        this.meaning = meaning;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTransliteration() {
        return transliteration;
    }

    public void setTransliteration(String transliteration) {
        this.transliteration = transliteration;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}
