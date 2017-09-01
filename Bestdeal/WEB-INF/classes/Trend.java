import com.mongodb.AggregationOutput;
import com.mongodb.DBObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/Trend"})
public class Trend extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);

        utility.printHtml("Header.html");
        utility.printHtml("LeftNavigationBar.html");
        pw.print("<div class='col-md-8'>");
        pw.print("<div class='row'>");

		
        //Print the most reviewed products
        pw.println("<h2>Top 5 Most Reviewed Products of BestDeal</h2>");
        try {
            AggregationOutput output = MongoDBDataStoreUtilities.topReviewedProducts();
            //get all key : max rating hashmap

            pw.print("<ol>");
            for (DBObject dbObject : output.results()) {
                pw.print("<li>" + dbObject.get("_id").toString() + " " + dbObject.get("count").toString() + "</li>");
            }
            pw.print("</ol>");

        } catch (Exception e) {
            System.out.println("Trending | Error: " + e);
        }
				
        pw.print("</hr>");
        pw.print("</div>");
        pw.print("</div>");

    }

	
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
	
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
	
}
