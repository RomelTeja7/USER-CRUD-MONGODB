package com.mywebappmongodb.controller;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mywebappmongodb.model.MongoDBConnection;
import com.mywebappmongodb.model.Users;
import org.bson.Document;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

@WebServlet("/listDocuments")
public class UsersController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "list":
                listUsers(request, response);
                break;
            default:
                listUsers(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "add":
                addUser(request, response);
                break;
            case "edit":
                editUser(request, response);
                break;
            case "delete":
                deleteUser(request, response);
                break;
            default:
                listUsers(request, response);
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            MongoDBConnection mongoDBConnection = new MongoDBConnection();
            MongoCollection<Document> collection = mongoDBConnection.getCollection();

            List<Document> documents = new ArrayList<>();
            MongoCursor<Document> cursor = collection.find().iterator();

            while (cursor.hasNext()) {
                documents.add(cursor.next());
            }

            mongoDBConnection.closeConnection();

            request.setAttribute("documents", documents);
            request.getRequestDispatcher("/listDocuments.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/listDocuments?action=list");
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/listDocuments?action=list&errorMessage=Not-Connect");
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userId = request.getParameter("userId");

        Users user = new Users();
        user.setUserId(userId);

        if (user.getUserId() != null && !user.getUserId().isEmpty()) {
            try {
            ObjectId objectId = new ObjectId(user.getUserId());

                MongoDBConnection mongoDBConnection = new MongoDBConnection();
                MongoCollection<Document> collection = mongoDBConnection.getCollection();

                Bson filter = Filters.eq("_id", objectId);

                DeleteResult deleteResult = collection.deleteOne(filter);

                if (deleteResult.getDeletedCount() > 0) {
                    response.sendRedirect(request.getContextPath() + "/listDocuments?action=list&successMessage=User+deleted+successfully");
                } else {
                    response.sendRedirect(request.getContextPath() + "/listDocuments?action=list&errorMessage=User+not+found+or+already+deleted");
                }

                mongoDBConnection.closeConnection();
            } catch (IllegalArgumentException e) {
                response.sendRedirect(request.getContextPath() + "/listDocuments?action=list&errorMessage=Invalid+ID");
            }
        }
    }

    private void addUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Users user = new Users();
        user.setFirstName(request.getParameter("first_name"));
        user.setLastName(request.getParameter("last_name"));
        user.setEmail(request.getParameter("email"));
        String age = request.getParameter("age");
        int ageSt = Integer.parseInt(age);
        user.setAge(ageSt);
        try {

            MongoDBConnection mongoDBConnection = new MongoDBConnection();
            MongoCollection<Document> collection = mongoDBConnection.getCollection();

            Document newUser = new Document()
                    .append("first_name", user.getFirstName())
                    .append("last_name", user.getLastName())
                    .append("email", user.getEmail())
                    .append("age", user.getAge());

            collection.insertOne(newUser);

            mongoDBConnection.closeConnection();

            response.sendRedirect(request.getContextPath() + "/listDocuments?action=list&successMessage=User+added+successfully");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/listDocuments?action=list&errorMessage=User+not+found+age");
        }
    }

    private void editUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userId = request.getParameter("userId");

        Users user = new Users();
        user.setUserId(userId);

        user.setFirstName(request.getParameter("first_name"));
        user.setLastName(request.getParameter("last_name"));
        user.setEmail(request.getParameter("email"));
        String age = request.getParameter("age");
        int ageSt = Integer.parseInt(age);
        user.setAge(ageSt);

        if (user.getUserId() != null && !user.getUserId().isEmpty()) {
            try {
                MongoDBConnection mongoDBConnection = new MongoDBConnection();

                MongoCollection<Document> collection = mongoDBConnection.getCollection();

                ObjectId objectId = new ObjectId(userId);

                Bson filter = Filters.eq("_id", objectId);

                Document updatedDocument = new Document()
                        .append("first_name", user.getFirstName())
                        .append("last_name", user.getLastName())
                        .append("email", user.getEmail())
                        .append("age", user.getAge());

                UpdateResult updateResult = collection.replaceOne(filter, updatedDocument);

                if (updateResult.getModifiedCount() > 0) {
                    response.sendRedirect(request.getContextPath() + "/listDocuments?action=list&successMessage=User+updated+successfully");
                } else {
                    response.sendRedirect(request.getContextPath() + "/listDocuments?action=list&errorMessage=User+not+found+or+already+updated");
                }

                mongoDBConnection.closeConnection();
            } catch (IllegalArgumentException e) {
                response.sendRedirect(request.getContextPath() + "/listDocuments?action=list&errorMessage=Invalid+ID");
            }
        }
    }
}
