package com.lockboxlocal.entity;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

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

            stmt.close();

        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Returns a box with a given name. Returns null if no
     * such box exists.
     * @param name The name of the box to retrieve.
     */
    public Lockbox getBox(String name) {

        Lockbox result = null;

        try {

            PreparedStatement stmt = conn.prepareStatement("select * from Boxes where boxName = ?");
            stmt.setString(1, name);

            ResultSet rset = stmt.executeQuery();

            if(rset.next()) {

                String tempName = rset.getString("boxName");
                String contents = rset.getString("content");
                int locked = rset.getInt("locked");
                long relockTimestamp = rset.getLong("relockTimestamp");
                Long unlockTimestamp = rset.getLong("unlockTimestamp");
                long unlockDelay = rset.getLong("unlockDelay");
                long relockDelay = rset.getLong("relockDelay");

                result = new Lockbox(tempName, contents, locked, relockTimestamp, unlockTimestamp, unlockDelay, relockDelay);

            }

            rset.close();
            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    /**
     * Updates the data of a lockbox in the database.
     * @param lockbox the lockbox to update.
     */
    public void updateBox(Lockbox lockbox) {

        try {

            PreparedStatement stmt = conn.prepareStatement("update Boxes set " +
                    "content = ?," +
                    "locked = ?," +
                    "relockTimestamp = ?," +
                    "unlockTimestamp = ?" +
                    " where boxName = ?");

            stmt.setString(1, lockbox.contents);
            stmt.setInt(2, lockbox.locked);
            stmt.setLong(3, lockbox.relockTimestamp);

            if(lockbox.unlockTimestamp == null) {
                stmt.setNull(4, Types.INTEGER);
            } else {
                stmt.setLong(4, lockbox.unlockTimestamp);
            }

            stmt.setString(5, lockbox.name);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Exports the database to a file.
     * @param filePath The file path where the file will be stored.
     * The structure of the file consists of a series of lines.
     * Each line represents a box. Each line contains a box's
     * data, separated by underscores.
     * Returns whether the export was successful.
     */
    public boolean exportDB(String filePath) {

        try {

            BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath));

            PreparedStatement stmt = conn.prepareStatement("select * from Boxes");
            ResultSet rset = stmt.executeQuery();

            //stores every line of data in the file.
            //each line represents a box's individual data(name, contents, etc.)
            ArrayList<String> dataLines = new ArrayList<String>();

            while(rset.next()) {

                //stores the data in an individual line.
                ArrayList<String> dataList = new ArrayList<String>();

                dataList.add(rset.getString("boxName"));
                dataList.add(rset.getString("content"));
                dataList.add(String.valueOf(rset.getInt("locked")));
                dataList.add(String.valueOf(rset.getInt("relockTimestamp")));
                dataList.add(String.valueOf(rset.getInt("unlockTimestamp")));
                dataList.add(String.valueOf(rset.getInt("unlockDelay")));
                dataList.add(String.valueOf(rset.getInt("relockDelay")));

                String line = String.join("_", dataList);
                dataLines.add(line);

            }

            String fileContents = String.join("\n", dataLines);
            writer.write(fileContents);
            writer.flush();
            writer.close();

            stmt.close();
            rset.close();
            return true;

        } catch(Exception e) {
            e.printStackTrace();
            return false;
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
