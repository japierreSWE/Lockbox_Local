package com.lockboxlocal.entity;

import java.sql.*;
import java.util.ArrayList;

public class Model {

    Connection conn = null;

    /**
     * Retrieves and returns all boxes from the database.
     * @return A list of the boxes' names.
     */
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

    /**
     * Removes a box with a given name from the database.
     * @param boxName the box to delete from the database.
     */
    public void deleteBox(String boxName) {

        try {

            PreparedStatement stmt = conn.prepareStatement("delete from Boxes where boxName = ?");
            stmt.setString(1, boxName);
            stmt.executeUpdate();
            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Returns true if a box with a given name already
     * exists in the database. Returns false otherwise.
     */
    public boolean boxExists(String name) {

        boolean result = false;

        try {

            PreparedStatement stmt = conn.prepareStatement("select * from Boxes where boxName = ?");
            stmt.setString(1, name);

            ResultSet rset = stmt.executeQuery();

            result = rset.next();

            rset.close();
            stmt.close();

        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    /**
     * Inserts a lockbox into the database.
     * @param name Name of the lockbox to create.
     * @param contents Contents of this lockbox.
     * @param unlockDelay The unlock delay of this lockbox.
     * @param relockDelay The relock delay of this lockbox.
     */
    public void createBox(String name, String contents, int unlockDelay, int relockDelay) {

        try {

            PreparedStatement stmt = conn.prepareStatement("insert into Boxes " +
                    "(boxName, content, locked, relockTimestamp, unlockTimestamp, unlockDelay, relockDelay)" +
                    "values" +
                    "(?,?,?,?,?,?,?)");

            stmt.setString(1, name);
            stmt.setString(2, contents);
            stmt.setInt(3, 1);
            stmt.setInt(4, 0);
            stmt.setNull(5, Types.INTEGER);

            stmt.setInt(6, unlockDelay);
            stmt.setInt(7, relockDelay);

            stmt.executeUpdate();

        } catch(Exception e) {
            e.printStackTrace();
        }

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
