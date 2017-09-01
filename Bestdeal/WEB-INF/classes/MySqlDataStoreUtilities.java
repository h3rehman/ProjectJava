import java.io.PrintWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.sql.*;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.*;
//import java.lang.StringBuffer;


public class MySqlDataStoreUtilities {
	
	static Connection conn = null;
	public static Connection getConnection()
	{
	try
	{
	Class.forName("com.mysql.jdbc.Driver").newInstance();
	conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bestdealdb","root","root");
	}
	catch(Exception e)
	{}
	return conn;
	}
	
	//Utility select user ()
	public static HashMap<String, User> selectUser(){
		ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
		
		String query = "SELECT * FROM Registration";
		
		try {
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			User user = null;
				HashMap<String, User> hm=new HashMap<String, User>();
			while (rs.next())
			{
				user = new User(rs.getString("username"), rs.getString("password"), rs.getString("usertype"));
						hm.put(rs.getString("username"), user);
			
			}
			return hm;		
		}
		catch (SQLException e){
			System.out.println(e);
			return null;
		}
		finally {
			DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
			
		
	}
	//Utility function for storing users
		public static void insertUser(User user){
		ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
		
		String query = "INSERT INTO Registration (username, password, usertype) "
		+ "VALUES (?, ?, ?)";
		
		try{
			ps = connection.prepareStatement(query);
			ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getUsertype());
				ps.executeUpdate();
		}
			catch (SQLException e) {
            System.out.println(e);
			}
			finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
	
	}
		// get all the product data from table to a Hashmap
		public static HashMap<String, Product> getData(){
		ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
		
		String productquery = "SELECT * FROM Products";
		
		try {
			ps = connection.prepareStatement(productquery);
			rs = ps.executeQuery();
			Product product = null;
				HashMap<String, Product> hm=new HashMap<String, Product>();
			while (rs.next())
			{
				product = new Product(rs.getString("name"), rs.getInt("Price"),rs.getString("manufacturer"), rs.getString("image"), rs.getString("Category"));
						hm.put(rs.getString("name"), product);
			
			}
			return hm;		
		}
		catch (SQLException e){
			System.out.println("Error in getting Product Hashmap " + e);
			return null;
		}
		finally {
			DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
			
		
	}
	
	//
	
	public StringBuffer readData(String searchId){
		StringBuffer sb = new StringBuffer();
		HashMap<String,Product> data;
		data=getData();
		//Iterator it = data.entrySet().iterator();
		Iterator it = (Iterator) data.entrySet().iterator();
		//while (((java.util.Iterator<Entry<String, Product>>) it).hasNext())
		while (it.hasNext())
	{	
		Map.Entry pi = (Map.Entry)it.next();
		Product p=(Product)pi.getValue();
		if (p.getName().toLowerCase().startsWith(searchId))
	{
		sb.append("<product>");
		//sb.append("<id>" + p.getId() + "</id>");
		sb.append("<productName>" + p.getName() + "</ productName >");
		sb.append("</ product >");
	}
	}
	return sb;
	}	
	
	
	
	
		//create getData function similar to the one below and then call it on the home page. 
		/*
		public static HashMap<String, Product> getData(){
		
		HashMap <String, ArrayList<Product>> products = new HashMap <String, ArrayList<Product>>();
				
		ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        
		String getProductsQuery = "Select * from Products";
		
		PreparedStatement pst = null;
        ResultSet rs = null;
			
		try {
			pst = connection.prepareStatement(getProductsQuery);
			rs = pst.executeQuery();
			ArrayList<Product> productList = new ArrayList<Product>();
			
			while (rs.next())
			{
				if(!products.containsKey(rs.getString("name")))
				{
					ArrayList<Product> arr = new ArrayList<Product>();
					
				products.put(rs.getString("name"), arr);
			}
			ArrayList<Product> listProducts = products.get(rs.getString("name"));
			Product item = new Product(rs.getString("name"), rs.getInt("Price"),rs.getString("manufacturer"));
				listProducts.add(item);
		}
		return products;
		}
		catch (SQLException e){
			System.out.println("Error in getting Product Hashmap " + e);
			return null;
		}
		finally {
			DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(pst);
            pool.freeConnection(connection);
        }
			
		
	}
				*/

	
			
		//select orders 
		public static HashMap<Integer, ArrayList<OrderPayment>> selectOrder(){
		
		HashMap <Integer, ArrayList<OrderPayment>> orderPayments = new HashMap <Integer, ArrayList<OrderPayment>>();
				
		ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        
		String selectOrderQuery = "SELECT * FROM CustomerOrders";
		
		PreparedStatement pst = null;
        ResultSet rs = null;
		
		try {
			pst = connection.prepareStatement(selectOrderQuery);
			rs = pst.executeQuery();
			ArrayList<OrderPayment> orderList = new ArrayList<OrderPayment>();
			
			while (rs.next())
			{
				if(!orderPayments.containsKey(rs.getInt("OrderId")))
				{
					ArrayList<OrderPayment> arr = new ArrayList<OrderPayment>();
					
				orderPayments.put(rs.getInt("OrderId"), arr);
			}
			ArrayList<OrderPayment> listOrderPayment = orderPayments.get(rs.getInt("OrderId"));
			OrderPayment order = new OrderPayment(rs.getInt("orderId"), rs.getString("username"),
			rs.getString("orderName"), rs.getDouble("orderPrice"), rs.getString("userAddress"), rs.getString("creditCardNo"));
				listOrderPayment.add(order);
		}
		return orderPayments;
		}
		catch (SQLException e){
			System.out.println(e);
			return null;
		}
		finally {
			DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(pst);
            pool.freeConnection(connection);
        }
			
		
	}
	
	public static void insertOrder(int orderId,String username,String orderName){
	try
	{
	//Class.forName("com.mysql.jdbc.Driver").newInstance();
	conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bestdealdb","root","root");
	String insertIntoCustomerOrderQuery = "INSERT INTO customerOrders(OrderId,userName,orderName)" + "VALUES (?,?,?);";
	PreparedStatement pst = conn.prepareStatement(insertIntoCustomerOrderQuery);
	pst.setInt(1,orderId);
	pst.setString(2,username);
	pst.setString(3,orderName);
	pst.execute();
	}
	catch (SQLException e) {
            System.out.println(e);
		}
	
}	
	//User exist method. 
	public static boolean userExist (String username, String password){
		ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT username, password FROM Registration"
                + "WHERE userName = ? AND password = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, username);
			ps.setString(2, password);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
		
}