import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DealMatches")
//@WebServlet(urlPatterns = {"/DealMatches"})  //1
public class DealMatches extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        String line = null;
        //String TOMCAT_HOME = System.getProperty("catalina.home"); //2
		String TOMCAT_HOME = System.getProperty("catalina.home");
		int counter = 0;
		
        HashMap<String, Product> selectedproducts = new HashMap<String, Product>();
		
		Utilities utility = new Utilities(request, pw);
        /*Print header footer and other HTML*/
        utility.printHtml("Header.html");
        utility.printHtml("LeftNavigationBar.html");
        pw.print("<div class='col-md-8'>");
        pw.print("<div class='row'>");
		
        try {
            pw.print("<div id='content'>");
            pw.print("<div class='post'>");
            pw.print("<h2 class='title'>");
            pw.print("<a href='#'>BestDeal Price Match </a></h2>");
            pw.print("<div class='entry'>");
            pw.print("<br> <br>");
            pw.print("<h2>We go big on deals!!</h2>");
            pw.print("<br> <br>");
            pw.print("<h3>Price-Match is Guaranteed</h3>");

            HashMap<String,Product> productmap = MySqlDataStoreUtilities.getData();
			
        for (Map.Entry<String, Product> entry : productmap.entrySet()) {
            if (selectedproducts.size() < 2 && !selectedproducts.containsKey(entry.getKey())) {
                BufferedReader reader = new BufferedReader(new FileReader(new File(TOMCAT_HOME + "\\webapps\\Bestdeal\\DealMatches.txt")));
                line = reader.readLine();
                if (line == null) {
                    pw.print("<h2 align='center'>No Offers Found</h2>");
                    break;
                } else {
                    do {
                        if (line.contains(entry.getKey())) {
							counter++;
							System.out.println("Matching Deals on BestBuy | " + line.contains(entry.getKey()));
                            pw.print("<h2>" + line + "</h2>");
                            pw.print("<br>");
                            selectedproducts.put(entry.getKey(), entry.getValue());
                            break;
                        }
                    } while ((line = reader.readLine()) != null);
                }
            }
        }
		 if (counter == 0) {
				pw.print("<div class='row'>");
				pw.print("<div class='row'>");
				pw.print("<div class='row'>");
                pw.print("<h2>No Matches found on BestDeal.</h2>");
            } else {
                pw.print("</br>");
                pw.print("<hr>");
                pw.print("</br>");
                pw.print("<h3>Best Deal Offer Matches</h3>");
                int i = 1;
                int size = selectedproducts.size();//get number of items in respective category and print all out
                for (Map.Entry<String, Product> entry : selectedproducts.entrySet()) {
                    Product products = entry.getValue();
                    pw.print("<div class='col-sm-6 col-md-4'>");
                    pw.print("<div class='thumbnail'>");
                    //pw.print("<img src='images/" + products.getPType() + "/" + products.getImage() + "' alt='...'>");
                    pw.print("<div class='caption'>");
                    pw.print("<p id='productName'>" + products.getName() + "</p>");  //check id name for products
                    pw.print("<p>" + products.getPrice() +"</span></p>");
                    pw.print("<form action='Cart' method='post'>");
                    pw.print("<input type='hidden' name='name' value='" + entry.getKey() + "'>");
                    //pw.print("<input type='hidden' name='type' value='" + products.getPType() + "'>");
                    pw.print("<input type='hidden' name='manufacturer' value='" + products.getManufacturer() + "'>");
                    pw.print("<input type='hidden' name='access' value=''>");
                    pw.print("<input type='submit' class='btnbuy' value='Buy Now'></form></li>");
                    pw.print("</form></br>");
                    pw.print("<li><form method='post' action='ViewReview'>");
                    pw.print("<input type='hidden' name='name' value='" + products.getName() + "'>"); //check the name field
                    pw.print("<input type='hidden' name='manufacturer' value='" + products.getManufacturer() + "'>"); //check manufacturer or retailer 
                    pw.print("<input type='hidden' name='price' value='" + products.getPrice() + "'>");
                    //pw.print("<input type='hidden' name='discount' value='" + products.getDiscount() + "'>");
                    pw.print("<input type='hidden' name='access' value=''>");
                    pw.print("<input type='submit' value='ViewReview' class='btnreview'></form></li>");
                    pw.print("</form>");
                    pw.print("</div>");
                    pw.print("</div>");
                    pw.print("</div>");
                    i++;
                }
            }
		
		}
		catch (Exception e) {
            System.out.print("Error in getData Method: " + e);
        }
    }
}
