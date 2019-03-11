package Model;

/**
 * Created by HP on 4/9/2017.
 */
public class PlacesDetails {
   // private String Name;
    private int ID;
    private String Ticket_Price;
    private String Kind;
    private String weather_type;
    private String area;
    private String namme;
    private String address;
    private String tourism_type;
    private String description;
    private String image;

    public String getTicket_Price() {
        return Ticket_Price;
    }

    public void setTicket_Price(String ticket_Price) {
        Ticket_Price = ticket_Price;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTourism_type() {
        return tourism_type;
    }

    public void setTourism_type(String tourism_type) {
        this.tourism_type = tourism_type;
    }

   /* public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }*/

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }


    public String getKind() {
        return Kind;
    }

    public void setKind(String kind) {
        Kind = kind;
    }

    public String getWeather_type() {
        return weather_type;
    }

    public void setWeather_type(String weather_type) {
        this.weather_type = weather_type;
    }

    public String getNamme() {
        return namme;
    }

    public void setNamme(String namme) {
        this.namme = namme;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
