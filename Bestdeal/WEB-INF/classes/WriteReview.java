import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(urlPatterns = {"/WriteReview"})
public class WriteReview extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();


        /* Through Http Request name,type,maker is retrieved.*/
        Utilities utility = new Utilities(request, pw);
        MongoDBDataStoreUtilities mongoDBUtilities = new MongoDBDataStoreUtilities();

        String userName = utility.username();
        String productName = (String) request.getParameter("productName");
        String productType = (String) request.getParameter("type");
        int reviewRating = Integer.parseInt(request.getParameter("reviewRating").trim());
        String reviewDate = request.getParameter("reviewDate");
        String reviewTitle = request.getParameter("reviewTitle");
        String reviewDesc = request.getParameter("reviewDesc");
        String retailer = (String) request.getParameter("retailer");
        String price = (String) request.getParameter("price");
        String discount = (String) request.getParameter("discount");


        mongoDBUtilities.storeReview(userName,productName,productType,reviewRating,reviewDate,reviewTitle,reviewDesc);
    }

    protected void writeAReview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);
        if (!utility.isLoggedin()) {
            HttpSession session = request.getSession(true);
            session.setAttribute("login_msg", "To write a review, please Log in.");
            response.sendRedirect("Login");
            return;
        }

        String productName = (String) request.getParameter("name");
        String type = (String) request.getParameter("type");
        String retailer = (String) request.getParameter("maker");
        String price = (String) request.getParameter("price");
        String discount = (String) request.getParameter("discount");

        utility.printHtml("Header.html");
        utility.printHtml("LeftNavigationBar.html");
        pw.print("<div class='col-md-8'>");

        //display product information.
        pw.print("</br>");
        pw.print("<table class='table'>");
        pw.print("<tbody>");
        pw.print("<tr>");
        pw.print("<td>Product Name</td><td>'" + productName + "'</td>");
        pw.print("</tr>");
        pw.print("<tr>");
        pw.print("<td>Product type</td><td>'" + type + "'</td>");
        pw.print("</tr>");
        pw.print("<tr>");
        pw.print("<td>Product retailer</td><td>'" + retailer + "'</td>");
        pw.print("</tr>");
        pw.print("<tr>");
        pw.print("<td>Product price</td><td>'" + price + "'</td>");
        pw.print("</tr>");
        pw.print("<tr>");
        pw.print("<td>Product discount</td><td>'" + discount + "'</td>");
        pw.print("</tr>");
        pw.print("</tbody>");
        pw.print("</table>");

        //review form
        pw.print("<form class='form-horizontal' method='post' action='WriteReview'");
        pw.print("<fieldset>");
        pw.print("<div class='form-group'>");
        pw.print("<label for='title'>Title</label>");
        pw.print("<input id='title' name='reviewTitle' type='text' placeholder='One word for your Comment' class='form-control input-md' required=''>");
        pw.print("</div>");
        pw.print("<div class='form-group'>");
        pw.print("<label for='desc'>Comment</label>");
        pw.print("<textarea class='form-control' id='desc' name='reviewDesc'></textarea>");
        pw.print("</div>");
        pw.print("<div class='form-group'>");
        pw.print("<label for='reviewRating'>Rating</label>");
        pw.print("<select name='reviewRating' class='form-control'>");
        pw.print("<option>1</option>");
        pw.print("<option>2</option>");
        pw.print("<option>3</option>");
        pw.print("<option>4</option>");
        pw.print("<option>5</option>");
        pw.print("</select>");
        pw.print("</div>");
        pw.print("<div class='form-group'>");
        pw.print("<label for='date'>Date</label>");
        pw.print("<input id='date' name='reviewDate' type='date' class='form-control input-md' required=''>");
        pw.print("</div>");
        pw.print("<div class='form-group'>");
        pw.print("<div class='col-md-4'>");
        pw.print("<input type='hidden' name='productName' value='" + productName + "'>");
        pw.print("<input type='hidden' name='type' value='" + type + "'>");
        pw.print("<input type='hidden' name='retailer' value='" + retailer + "'>");
        pw.print("<input type='hidden' name='price' value='" + price + "'>");
        pw.print("<input type='hidden' name='discount' value='" + discount + "'>");
        pw.print("<button id='submit' name='submit' class='btn btn-success'>Post my review</button>");
        pw.print("</div>");
        pw.print("</div>");
        pw.print("</fieldset>");
        pw.print("</form>");
        pw.print("</div>");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);

        writeAReview(request, response);
    }
}
