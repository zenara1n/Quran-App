package aiou.muslim.mttech.Models;

public class AzkarModel {

    private int id;
    private String subcat_name, category_name, azkarfield, fav, cateng, translation;

    public AzkarModel() {
    }

    public AzkarModel(int id, String subcat_name, String category_name, String azkarfield, String fav, String cateng, String translation) {
        this.id = id;
        this.subcat_name = subcat_name;
        this.category_name = category_name;
        this.azkarfield = azkarfield;
        this.fav = fav;
        this.cateng = cateng;
        this.translation = translation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubcat_name() {
        return subcat_name;
    }

    public void setSubcat_name(String subcat_name) {
        this.subcat_name = subcat_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getAzkarfield() {
        return azkarfield;
    }

    public void setAzkarfield(String azkarfield) {
        this.azkarfield = azkarfield;
    }

    public String getFav() {
        return fav;
    }

    public void setFav(String fav) {
        this.fav = fav;
    }

    public String getCateng() {
        return cateng;
    }

    public void setCateng(String cateng) {
        this.cateng = cateng;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
