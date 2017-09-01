import java.io.PrintWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/AjaxUtilities"})
public class AjaxUtility extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    public static HashMap<String, Product> getData() {
        HashMap<String, Product> hm = new HashMap<>();
        try {
            Connection conn = MySqlDataStoreUtilities.getConnection();
            Statement stmt = conn.createStatement();
            String selectCustomerQuery = "select * from Products";
            ResultSet rs = stmt.executeQuery(selectCustomerQuery);	////rs.getString("image"),rs.getString("pCondition"), rs.getDouble("discount"), rs.getString("pType")
            while (rs.next()) {																	
                Product p = new Product(rs.getString("name"), rs.getInt("Price"),rs.getString("manufacturer"),rs.getString("image"), rs.getString("Category"));
                hm.put(rs.getString("productId"), p);
            }
        } catch (Exception e) {
            System.out.println("Error in Ajax Utility getData | " + e);
        }
        return hm;
    }

    public StringBuilder readData(String searchId) {
        StringBuilder sb = new StringBuilder();
        
        HashMap<String, Product> data;
        try {
            //sb.append("<?xml version='1.0' encoding='UTF-8'?>");
            data = getData();
            System.out.println("Readdata size | " + data.size());
            Iterator it = data.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pi = (Map.Entry) it.next();
                Product p = (Product) pi.getValue();
                if (p.getName().toLowerCase().startsWith(searchId)) {
                    System.out.println("Readdata searchId is : " + searchId);
                    sb.append("\r\n<product>");
                    sb.append("\r\n<productId>").append(p.getName()).append("</productId>");
                    sb.append("\r\n<name>").append(p.getName()).append("</name>");
                    sb.append("\r\n</product>");
                }
            }
            //sb.append("<\r\n/products>");
        } catch (Exception e) {
            System.out.println("Error inside Ajax Utility readData : " + e);
        }
       
        return sb;
    }
}
