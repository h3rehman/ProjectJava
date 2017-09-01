import java.io.IOException;
import java.io.PrintWriter;
import java.io.*;


public class Product implements Serializable {
	
	private String name;
	private int price;
	private String manufacturer;
	private String image;
	private String Category;
	
	
	public Product(String name, int price, String manufacturer) {
		this.name=name;
		this.price=price;
		this.manufacturer= manufacturer;
		this.image=image;
		this.Category=Category;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
		
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public String getCategory() {
		return Category;
	}

	public void setCategory(String Category) {
		this.Category = Category;
	}


}	