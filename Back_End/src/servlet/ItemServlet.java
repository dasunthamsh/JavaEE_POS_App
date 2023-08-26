package servlet;/*
    @author Dasun
*/

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.sql.*;

@WebServlet(urlPatterns = "/item")
public class ItemServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");


            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop","root","1234");
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM item");
            ResultSet rst = pstm.executeQuery();


            JsonArrayBuilder allCustomer = Json.createArrayBuilder();

            while (rst.next()){

                JsonObjectBuilder customer = Json.createObjectBuilder(); //  values tika json ekak widiyata bild karala denawa

                String id = rst.getString("code");
                String name = rst.getString("name");
                String price = rst.getString("price");
                String qyt = rst.getString("qyt");

                customer.add("code",id);
                customer.add("name",name);
                customer.add("price",price);
                customer.add("qyt",qyt);
                allCustomer.add(customer.build());

            }



            resp.addHeader("Content-Type","application/json");

            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("status","OK");
            job.add("message","successfully loaded");
            job.add("data",allCustomer.build());
            resp.getWriter().print(job.build());





        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String itemId = req.getParameter ("code");
        String itemCode = req.getParameter ("name");
        String price = req.getParameter ("price");
        String qyt = req.getParameter ("qyt");

        PrintWriter writer = resp.getWriter ();


        try {
            Class.forName ("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection ("jdbc:mysql://localhost:3306/shop", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement ("insert into item values(?,?,?,?)");

            pstm.setObject (1, itemId);
            pstm.setObject (2, itemCode);
            pstm.setObject (3, price);
            pstm.setObject (4, qyt);
            if (pstm.executeUpdate () > 0) {

                resp.addHeader("Content-Type","application/json");
                JsonObjectBuilder cussAdd= Json.createObjectBuilder ();
                cussAdd.add ("state","200");
                cussAdd.add ("massage"," Customer Added Succuss");
                cussAdd.add ("data","");
                resp.setStatus (200);
                writer.print(cussAdd.build());

            }
        } catch (SQLException | ClassNotFoundException e) {

            resp.addHeader("Content-Type","application/json");
            JsonObjectBuilder obj=Json.createObjectBuilder ();
            obj.add ("state","");
            obj.add ("massage",e.getMessage ());
            obj.add ("data","");
            resp.setStatus (400);
            writer.print(obj.build());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        JsonReader reader = Json.createReader(req.getReader());
        JsonObject customer = reader.readObject();
        String code = customer.getString("code");
        String name = customer.getString("name");
        String price = customer.getString("price");
        String qyt = customer.getString("qyt");

        try {

            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop","root","1234");

            PreparedStatement pstm = connection.prepareStatement("UPDATE item SET name=?,price=?,qyt=? WHERE code =? ");
            pstm.setObject(4, code);
            pstm.setObject(1, name);
            pstm.setObject(2, price);
            pstm.setObject(3, qyt);
            boolean b = pstm.executeUpdate() > 0;

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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        String code = req.getParameter("code");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop","root","1234");
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM item WHERE code = ?");
            pstm.setObject(1, code);
            boolean b = pstm.executeUpdate() > 0;

            if(b){
                JsonObjectBuilder deleteCustomer = Json.createObjectBuilder();
                deleteCustomer.add("state","ok");
                deleteCustomer.add("message","item delete successful");
                deleteCustomer.add("data","");
            } else {
                throw new RemoteException("something wrong try agne");
            }

        }catch (RuntimeException e){
            JsonObjectBuilder deleteCustomer = Json.createObjectBuilder();
            deleteCustomer.add("state","ok");
            deleteCustomer.add("message","item delete successful");
            deleteCustomer.add("data","");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(deleteCustomer.build());

        }catch (ClassNotFoundException | SQLException e){
            JsonObjectBuilder deleteCustomer = Json.createObjectBuilder();
            deleteCustomer.add("state","ok");
            deleteCustomer.add("message","item delete successful");
            deleteCustomer.add("data","");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(deleteCustomer.build());
        }
    }
    }

