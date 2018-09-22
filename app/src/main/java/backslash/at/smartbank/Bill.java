package backslash.at.smartbank;

public class Bill {
    public int Id;
    public String Description;
    public Double Price;
    public String Image;
    public String Title;

    public Bill(int id, String description, Double price, String image, String title) {
        Id = id;
        Description = description;
        Price = price;
        Image = image;
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public Double getPrice() {
        return Price;
    }

    public String getImage() {
        return Image;
    }

    public String getTitle() {
        return Title;
    }
}
