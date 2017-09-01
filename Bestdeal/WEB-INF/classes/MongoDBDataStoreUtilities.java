import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class MongoDBDataStoreUtilities
{
static DBCollection myReviews;
public static void getConnection()
{
	try {
MongoClient mongo;
mongo = new MongoClient("localhost", 27017);
DB db = mongo.getDB("CustomerReviews");
myReviews= db.getCollection("myReviews");
}
catch (Exception e) {
            System.out.print("Error in connecting with MongoClient: " + e);
        }

}
//Storing reviews 

public void storeReview(String userName,String productName,String productType,int reviewRating,String reviewDate, String reviewTitle, String reviewDesc)
{ HashMap<String, ArrayList<Review>> reviews= new HashMap<String, ArrayList<Review>>();
try
{reviews=MongoDBDataStoreUtilities.selectReview(productName);}
catch(Exception e)
{
	System.out.println("Error in storeReview: " + e);
} 
if(!reviews.containsKey(productName)){
ArrayList<Review> arr = new ArrayList<Review>();
reviews.put(productName, arr);
}
ArrayList<Review> listReview = reviews.get(productName);
Review review = new Review(userName,productName,productType,reviewRating,reviewDate,reviewTitle,reviewDesc);
listReview.add(review);
try
{
MongoDBDataStoreUtilities.insertReview(userName,productName,productType,reviewRating,reviewDate,reviewTitle,reviewDesc);
}
catch(Exception e)
{
	System.out.println("Error in insertReview: " + e);
} 
}

//Selecting review data into Hashmap. 
public static HashMap<String, ArrayList<Review>> selectReview(String productName)
{
getConnection();
HashMap<String, ArrayList<Review>> reviewHashmap=new HashMap<String, ArrayList<Review>>();
DBCursor cursor = myReviews.find();
while (cursor.hasNext())
{
BasicDBObject obj = (BasicDBObject) cursor.next();
if(! reviewHashmap.containsKey(obj.getString("productName")))
{
ArrayList<Review> arr = new ArrayList<Review>();
reviewHashmap.put(obj.getString("productName"), arr);
}
ArrayList<Review> listReview = reviewHashmap.get(obj.getString("productName"));
Review review =new Review(obj.getString("userName"),obj.getString("productName"),obj.getString("productType"),
obj.getInt("reviewRating"),obj.getString("reviewDate"),obj.getString("reviewTitle"),obj.getString("reviewDesc"));
listReview.add(review);
}
return reviewHashmap;
}

//Writing reviews into MangoDB

	public static void insertReview(String userName,String productName,String productType,int reviewRating,String reviewDate, String reviewTitle, String reviewDesc)
	{
	getConnection();
	BasicDBObject doc = new BasicDBObject("title", "myReviews").

	append("userName", userName).
	append("productName", productName).
	append("productType", productType).
	append("reviewRating", reviewRating).
	append("reviewDate", reviewDate).
	append("reviewTitle", reviewTitle).
	append("reviewDesc", reviewDesc);

	myReviews.insert(doc);
}

//select review and return as HM
    public static long reviewCount(String productName) {
        getConnection();
        long count = 0;
        BasicDBObject query = new BasicDBObject();
        query.put("productName", productName);

        try {
            count = myReviews.count(query);
        } catch (Exception e) {
            System.out.println("Error inside reviewCount: " + e);
        }
        return count;
    }
	
	/*
	//trending class for topLikedProducts
	public static HashMap<String, ArrayList<Review>> topLikedProducts() {
        getConnection();
        BasicDBObject query = new BasicDBObject();
        query.put("reviewRating", 5);
        DBCursor cursor = null;
        try {
            cursor = myReviews.find(query);
        } catch (Exception e) {
        }

        HashMap<String, ArrayList<Review>> reviewHM = new HashMap<>();
        try {
            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();

                if (!reviewHM.containsKey(obj.getString("productName"))) { 

                    ArrayList<Review> arr = new ArrayList<>();
                    reviewHM.put(obj.getString("productName"), arr);
                }

                ArrayList<Review> listReview = reviewHM.get(obj.getString("productName"));
                Review review = new Review(obj.getString("productName"), obj.getString("productType"), obj.getString("userName"),
                obj.getString("reviewTitle"), obj.getString("reviewDesc"), obj.getInt("reviewRating"), obj.getString("reviewDate")); //shows a error here.

                listReview.add(review);
            }
        } catch (Exception e) {
            System.out.println("SelectReview | Check 2: " + e);
        } finally {
            cursor.close();
        }
        return reviewHM;
    }
	*/

	//trending class for topReviewedProducts
    public static AggregationOutput topReviewedProducts() {
        getConnection();
        DBObject groupFields = new BasicDBObject("_id", "$productName");
        groupFields.put("count", new BasicDBObject("$sum", 1));
        DBObject group = new BasicDBObject("$group", groupFields);
        DBObject sortFields = new BasicDBObject("count", -1);
        DBObject sort = new BasicDBObject("$sort", sortFields);

        AggregationOutput output = myReviews.aggregate(group, sort);

        return output;
    }


}