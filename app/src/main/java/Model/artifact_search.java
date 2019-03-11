package Model;

/**
 * Created by HP on 5/2/2017.
 */

public class artifact_search {
    private int id;
    private String image;
    private String name;
    private String Muesum_name;
    private String description;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMuesum_name() {
        return Muesum_name;
    }

    public void setMuesum_name(String muesum_name) {
        Muesum_name = muesum_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
