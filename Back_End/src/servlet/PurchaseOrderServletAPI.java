package servlet;


import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;


@WebServlet(urlPatterns = {"/pages/purchase"})
public class PurchaseOrderServletAPI extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String option = req.getParameter("option");
        switch (option) {
            case "generateOrderID":
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "1234");
                    PreparedStatement pstm = connection.prepareStatement("SELECT oid FROM Orders ORDER BY oid DESC LIMIT 1");
                    ResultSet rst = pstm.executeQuery();

                    resp.addHeader("Access-Control-Allow-Origin", "*");

                    resp.addHeader("Content-Type", "application/json");


                    JsonArrayBuilder allOrders = Json.createArrayBuilder();
                    while (rst.next()) {
                        String oId = rst.getString(1);

                        JsonObjectBuilder orderIdObject = Json.createObjectBuilder();
                        orderIdObject.add("oid", oId);


                        allOrders.add(orderIdObject.build());
                    }

                    resp.getWriter().print(allOrders.build());

                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "getOrders":
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "1234");
                    PreparedStatement pstm = connection.prepareStatement("select * from Orders");
                    ResultSet rst = pstm.executeQuery();

                    resp.addHeader("Access-Control-Allow-Origin", "*");

                    resp.addHeader("Content-Type", "application/json");



                    JsonArrayBuilder allOrders = Json.createArrayBuilder();
                    while (rst.next()) {
                        String oid = rst.getString(1);
                        String cusId = rst.getString(2);
                        String date = rst.getString(3);
                        String subTotal = rst.getString(4);
                        String discount = rst.getString(5);

                        JsonObjectBuilder orderObject = Json.createObjectBuilder();
                        orderObject.add("oid", oid);
                        orderObject.add("cusId", cusId);
                        orderObject.add("date", date);
                        orderObject.add("subTotal", subTotal);
                        orderObject.add("discount", discount);

                        allOrders.add(orderObject.build());
                    }

                    resp.getWriter().print(allOrders.build());

                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "getOrderDetails":
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "1234");
                    PreparedStatement pstm = connection.prepareStatement("select * from OrderDetails");
                    ResultSet rst = pstm.executeQuery();

                    resp.addHeader("Access-Control-Allow-Origin", "*");

                    resp.addHeader("Content-Type", "application/json");


                    JsonArrayBuilder allOrderDetails = Json.createArrayBuilder();
                    while (rst.next()) {
                        String oid = rst.getString(1);
                        String cusId = rst.getString(2);
                        String cusName = rst.getString(3);
                        String code = rst.getString(4);
                        String description = rst.getString(5);
                        String qty = rst.getString(6);
                        String price = rst.getString(7);

                        JsonObjectBuilder orderDetailObject = Json.createObjectBuilder();
                        orderDetailObject.add("oid", oid);
                        orderDetailObject.add("cusId", cusId);
                        orderDetailObject.add("cusName", cusName);
                        orderDetailObject.add("code", code);
                        orderDetailObject.add("description", description);
                        orderDetailObject.add("qty", qty);
                        orderDetailObject.add("price", price);

                        allOrderDetails.add(orderDetailObject.build());
                    }

                    resp.getWriter().print(allOrderDetails.build());

                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
        }

    }


    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "PUT");
        resp.addHeader("Access-Control-Allow-Headers", "content-type");
    }
}