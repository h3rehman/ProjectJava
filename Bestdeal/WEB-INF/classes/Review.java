import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;


@WebServlet("/Review")
public class Review extends HttpServlet {

    private String productName;
    private String productType;
    private String userName;
    private String reviewTitle;
    private String reviewDesc;
    private int reviewRating;
    private String reviewDate;

    public Review(String userName,String productName,String productType,int reviewRating,String reviewDate, String reviewTitle, String reviewDesc) {
        this.userName = userName;
        this.productName = productName;
        this.productType = productType;
        this.reviewRating = reviewRating;
        this.reviewDate = reviewDate;
        this.reviewTitle = reviewTitle;
        this.reviewDesc = reviewDesc;
    }
	
	//return userName
	
    public String getUserName() {
        return userName;
    }

    //set userName
	
    public void setUserName(String userName) {
        this.userName = userName;
    }

    
     //return productName
     
    public String getProductName() {
        return productName;
    }

    //set productName
	
    public void setProductName(String productName) {
        this.productName = productName;
    }

    //return productType
	
    public String getProductType() {
        return productType;
    }

    //set productType
	
    public void setProductType(String productType) {
        this.productType = productType;
    }

   
    //return reviewTitle
	
    public String getReviewTitle() {
        return reviewTitle;
    }

    //set reviewTitle 
	
    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    //return reviewDesc 
	
    public String getReviewDesc() {
        return reviewDesc;
    }

    //set reviewDesc
	
    public void setReviewDesc(String reviewDesc) {
        this.reviewDesc = reviewDesc;
    }

    //return reviewRating 
	
    public int getReviewRating() {
        return reviewRating;
    }

    //set reviewRating 
	
    public void setReviewRating(int reviewRating) {
        this.reviewRating = reviewRating;
    }

	
	//return reviewDate 

    public String getReviewDate() {
        return reviewDate;
    }

    //set reviewDate 
	
    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

   

}
