package Model;

/**
 * Created by HP on 5/3/2017.
 */

public class ScanningDetails {
    private String Name;
    private String Description;
    private String image;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}