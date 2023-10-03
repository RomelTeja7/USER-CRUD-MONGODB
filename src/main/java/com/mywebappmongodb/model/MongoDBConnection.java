package com.mywebappmongodb.model;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

public class MongoDBConnection {

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public MongoDBConnection() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("MyWebApp");
        collection = database.getCollection("Users");
    }

    public MongoCollection<Document> getCollection() {
        return collection;
    }

    public void closeConnection() {
        mongoClient.close();
    }
}
