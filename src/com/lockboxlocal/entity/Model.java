package com.lockboxlocal.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Model {

    Connection conn = null;

    public ArrayList<String> getBoxes() {

        ArrayList<String> result = new ArrayList<String>();

        try {

            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery("select (boxName) from Boxes");

            while(resultSet.next()) {

                result.add(resultSet.getString("boxName"));

            }

            resultSet.close();
            stmt.close();

        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    public Model() {

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");

            //create the table which will contain all lockboxes.
            Statement tableStatement = conn.createStatement();
            String query = "create table if not exists Boxes " +
                    "(boxName TEXT PRIMARY KEY NOT NULL, " +
                    "content TEXT, " +
                    "locked INTEGER NOT NULL, " +
                    "relockTimestamp INTEGER NOT NULL," +
                    " unlockTimestamp INTEGER, " +
                    "unlockDelay INTEGER NOT NULL, " +
                    "relockDelay INTEGER NOT NULL )";

            tableStatement.executeUpdate(query);
            tableStatement.close();

        } catch(Exception e) {
            e.printStackTrace();
        }

    }

}
