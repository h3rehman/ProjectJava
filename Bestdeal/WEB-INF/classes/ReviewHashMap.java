import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ReviewHashMap")

public class ReviewHashMap extends HttpServlet {

    public static HashMap<String, ArrayList<Review>> reviews = new HashMap<String, ArrayList<Review>>();

    public ReviewHashMap() {

    }
}
