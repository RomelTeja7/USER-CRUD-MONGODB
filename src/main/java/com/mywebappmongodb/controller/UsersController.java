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

        if (userId != null && !userId.isEmpty()) {
            try {
                ObjectId objectId = new ObjectId(userId);

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
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String email = request.getParameter("email");
        String ageStr = request.getParameter("age");

        if (firstName != null && !firstName.isEmpty()
                && lastName != null && !lastName.isEmpty()
                && email != null && !email.isEmpty()
                && ageStr != null && !ageStr.isEmpty()) {
            try {
                int age = Integer.parseInt(ageStr);

                MongoDBConnection mongoDBConnection = new MongoDBConnection();
                MongoCollection<Document> collection = mongoDBConnection.getCollection();

                Document newUser = new Document()
                        .append("first_name", firstName)
                        .append("last_name", lastName)
                        .append("email", email)
                        .append("age", age);

                collection.insertOne(newUser);

                mongoDBConnection.closeConnection();

                response.sendRedirect(request.getContextPath() + "/listDocuments?action=list&successMessage=User+added+successfully");
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/listDocuments?action=list&errorMessage=User+not+found+age");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/listDocuments?action=list&errorMessage=Empty");
        }
    }

    private void editUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String email = request.getParameter("email");
        String ageStr = request.getParameter("age");

        if (userId != null && !userId.isEmpty()) {
            try {
                MongoDBConnection mongoDBConnection = new MongoDBConnection();

                MongoCollection<Document> collection = mongoDBConnection.getCollection();

                ObjectId objectId = new ObjectId(userId);

                Bson filter = Filters.eq("_id", objectId);

                Document updatedDocument = new Document()
                        .append("first_name", firstName)
                        .append("last_name", lastName)
                        .append("email", email)
                        .append("age", Integer.parseInt(ageStr));

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
