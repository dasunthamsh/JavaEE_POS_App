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

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.addHeader("Access-Control-Allow-Origin","*");
        resp.addHeader("Access-Control-Allow-Methods","DELETE,PUT");
        resp.addHeader("Access-Control-Allow-Headers","Content-Type");
    }
}
