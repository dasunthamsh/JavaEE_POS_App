package servlet;/*
    @author Dasun
*/

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.*;

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.addHeader("Access-Control-Allow-Origin","*");

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop","root" , "1234");
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM cuistomer");
            ResultSet resultSet = pstm.executeQuery();

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

            while (resultSet.next()){

                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

                objectBuilder.add("id", resultSet.getString("id"));
                objectBuilder.add("name",resultSet.getString("name"));
                objectBuilder.add("address",resultSet.getString("address"));
                objectBuilder.add("salary",resultSet.getString("salary"));

                arrayBuilder.add(objectBuilder.build());

            }

            resp.addHeader("Content-Type","application/json");

            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("status","OK");
            job.add("message","successfully loaded");
            job.add("data",arrayBuilder.build());
            resp.getWriter().print(job.build());


        }catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        resp.addHeader("Access-Control-Allow-Origin","*");

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop","root","1234");
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO customer VALUES (?,?,?,?)");

            preparedStatement.setObject(1 , req.getParameter("id"));
            preparedStatement.setObject(2 , req.getParameter("name"));
            preparedStatement.setObject(3 , req.getParameter("address"));
            preparedStatement.setObject(4, req.getParameter("salary"));
            boolean b = preparedStatement.executeUpdate()>0;

            if(b){
                JsonObjectBuilder addCustomer = Json.createObjectBuilder(); // customer updated message
                addCustomer.add("state","ok");
                addCustomer.add("message","update successful");
                addCustomer.add("data","");

                resp.getWriter().print(addCustomer.build());

            }

        }catch (ClassNotFoundException | RuntimeException | SQLException e) {
            JsonObjectBuilder addCustomer = Json.createObjectBuilder(); // customer updated message
            addCustomer.add("state", "ok");
            addCustomer.add("message", e.getLocalizedMessage());
            addCustomer.add("data", "");
            resp.setStatus(500);



        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        resp.addHeader("Access-Control-Allow-Origin","*"); // *(all) mark eka hari allow karanna ona hodt eka hari danna puluwan // origin (url) dekakin data access karanna me header eka use karanawa

        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        String salary = req.getParameter("salary");


        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop","root","1234");



            PreparedStatement pstm = connection.prepareStatement("INSERT INTO customer VALUES (?,?,?,?)");
            pstm.setObject(1, id);
            pstm.setObject(2, name);
            pstm.setObject(3, address);
            pstm.setObject(4, salary);

            boolean b = pstm.executeUpdate() > 0;

            if(b){

                JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();

                jsonObjectBuilder.add("state","ok");     // customer save message
                jsonObjectBuilder.add("message","successfully added");
                jsonObjectBuilder.add("data","");
                resp.setStatus(200);
                resp.getWriter().print(jsonObjectBuilder.build());

            }

            //  resp.sendRedirect("customer");

        } catch (SQLException e) {
            JsonObjectBuilder error = Json.createObjectBuilder();
            error.add("state","error");
            error.add("message",e.getLocalizedMessage());
            error.add("data","");

            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // same to this code resp.setStatus(500);
            // resp.setStatus(500);


            resp.getWriter().print(error.build());

        }catch (ClassNotFoundException e){

            JsonObjectBuilder error = Json.createObjectBuilder();
            error.add("state","error");
            error.add("message",e.getLocalizedMessage());
            error.add("data","");
            resp.getWriter().print(error.build());
            resp.setStatus(500);


        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        resp.addHeader("Access-Control-Allow-Origin","*"); // *(all) mark eka hari allow karanna ona hodt eka hari danna puluwan // origin (url) dekakin deata access karanna me header eka use karanawa
        resp.setContentType("application/json");
        String id = req.getParameter("id");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop","root","1234");
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM customer WHERE id = ?");
            pstm.setObject(1, id);
            boolean b = pstm.executeUpdate() > 0;

            if(b){
                JsonObjectBuilder deleteCustomer = Json.createObjectBuilder();
                deleteCustomer.add("state","ok");
                deleteCustomer.add("message","customer delete successful");
                deleteCustomer.add("data","");
            } else {
                throw new RemoteException("something wrong try agne");
            }

        }catch (RuntimeException e){
            JsonObjectBuilder deleteCustomer = Json.createObjectBuilder();
            deleteCustomer.add("state","ok");
            deleteCustomer.add("message","customer delete successful");
            deleteCustomer.add("data","");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(deleteCustomer.build());

        }catch (ClassNotFoundException | SQLException e){
            JsonObjectBuilder deleteCustomer = Json.createObjectBuilder();
            deleteCustomer.add("state","ok");
            deleteCustomer.add("message","customer delete successful");
            deleteCustomer.add("data","");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(deleteCustomer.build());
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.addHeader("Access-Control-Allow-Origin","*");
        resp.addHeader("Access-Control-Allow-Methods","DELETE,PUT");
        resp.addHeader("Access-Control-Allow-Headers","Content-Type");
    }
}
