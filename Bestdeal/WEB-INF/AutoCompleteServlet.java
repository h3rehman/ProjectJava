import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/autocomplete"})  //1
//@WebServlet("/autocomplete")

public class AutoCompleteServlet extends HttpServlet {
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Inside AutoComplete | doGet | ");
        String action = request.getParameter("action");

        if ("complete".equals(action)) {
            complete(request, response);
        }

        if ("lookup".equals(action)) {
            lookup(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    public void complete(HttpServletRequest request, HttpServletResponse response) {
        try {

            StringBuilder sb = null;
            boolean namesAdded = false;
            String action = request.getParameter("action"); 
            String searchId = request.getParameter("searchId"); 
            System.out.println("Inside AutoComplete | Complete | " + action);
            if (action.equals("complete")) {
                if (!searchId.equals("")) {
                    AjaxUtility a = new AjaxUtility();
                    sb = a.readData(searchId);
                    if (sb != null || !sb.equals("")) {
                        namesAdded = true;
                    }
                    if (namesAdded) {
                        response.setContentType("text/xml");
                        response.setHeader("Cache-Control", "no-cache");
                        response.getWriter().write("\r\n<products>" + sb.toString() + "\r\n</products>");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error inside auto complete | " + e);
        }
    }
		
	public void lookup(HttpServletRequest request, HttpServletResponse response) {
        ResultSet rs;
        response.setContentType("text/html");

        String action = request.getParameter("action"); 
        String searchId = request.getParameter("searchId"); 
        System.out.println("Inside AutoComplete | LookUp | " + action);
        if (action.equals("lookup") && !searchId.equals("")) {

            HashMap<String, Product> productHM = new HashMap<>();
            try {
                Connection conn = MySqlDataStoreUtilities.getConnection();
                PreparedStatement pst = conn.prepareStatement("SELECT * FROM Products WHERE name =?");
                pst.setString(1, searchId);

                rs = pst.executeQuery();	//, rs.getString("pCondition"), rs.getDouble("discount"), rs.getString("pType"), rs.getString("image")
                while (rs.next()) {
                    //product exists
                    Product p = new Product(rs.getString("name"), rs.getInt("price"),rs.getString("manufacturer"));
                    productHM.put(rs.getString("productId"), p);
                }
            }//end of try
            catch (Exception e) {
                System.out.println("AutoComplete lookup " + e);
            }
			//TODO 8:32 PM
            //print out product for selected item
            try {
                PrintWriter pw = response.getWriter();
                Utilities utility = new Utilities(request, pw);

                utility.printHtml("Header.html");
                utility.printHtml("LeftNavigationBar.html");
                pw.print("<div class='col-md-8'>");
                pw.print("<div class='row'>");

                int i = 1;
                int size = productHM.size();//get number of items in respective category and print all out
                System.out.println("AutoComplete | lookup | HM size | " + size);
                for (Map.Entry<String, Product> entry : productHM.entrySet()) {
                    Product products = entry.getValue();
                    pw.print("<div class='col-sm-6 col-md-4'>");
                    pw.print("<div class='thumbnail'>");
                    //pw.print("<img src='images/" + products.getPType() + "/" + products.getImage() + "' alt='...'>");  //insert images in DB
                    pw.print("<div class='caption'>");
                    pw.print("<p id='product_name'>" + products.getName() + "</p>");
                    pw.print("<p>$" + products.getPrice() + "<span style='color:red;'>");
                    pw.print("<form action='Cart' method='post'>");
                    pw.print("<input type='hidden' name='name' value='" + entry.getKey() + "'>");
                    //pw.print("<input type='hidden' name='type' value='" + products.getPType() + "'>");
                    pw.print("<input type='hidden' name='maker' value='" + products.getManufacturer() + "'>");
                    pw.print("<input type='hidden' name='access' value=''>");
                    pw.print("<button type='submit' class='btnbuy'>Buy Now</button>");
                    pw.print("</form></br>");
                    pw.print("<form action='ViewReview' method='post'>");
                    pw.print("<input type='hidden' name='name' value='" + products.getName() + "'>");
                    //pw.print("<input type='hidden' name='type' value='" + products.getPType() + "'>");
                    pw.print("<input type='hidden' name='maker' value='" + products.getManufacturer() + "'>");
                    pw.print("<input type='hidden' name='price' value='" + products.getPrice() + "'>");
                    //pw.print("<input type='hidden' name='discount' value='" + products.getDiscount() + "'>");
                    pw.print("<input type='hidden' name='access' value=''>");
                    pw.print("<button type='submit' value='ViewReview' class='btnreview'>Reviews</button>");
                    pw.print("</form>");
                    pw.print("</div>");
                    pw.print("</div>");
                    pw.print("</div>");
                    i++;
                }
            } catch (Exception e) {
                System.out.println("AutoComplete | lookup end For Loop: " + e);
            }
        }

    }
		
	
}
