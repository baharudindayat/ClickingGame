package com.example.taa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class DBConnector {
    // objects to hold the data
    private TableView tableView;
    private ObservableList<ObservableList> queryData;

    // initialize the objects
    public DBConnector() {
        queryData = null;
        tableView = null;
    }

    // get the status of the result
    public boolean getStatus() { return queryData.size() > 0; }

    // get the table contents
    public TableView getTable() { return tableView; }

    // get the data contents
    public ObservableList<ObservableList> getData() { return queryData; }

    // start the database connection
    public void start(String dbCommand) {
        // try to connect to the database
        try (Connection dbConnection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/db_oop?serverTimezone=UTC",
                "root", "");
             Statement statement = dbConnection.createStatement();) {

            // execute the command to get the query data
            ResultSet queryResult = statement.executeQuery(dbCommand);

            // initialize the objects
            queryData = FXCollections.observableArrayList();
            tableView = new TableView();

            // read all the columns and create the exact column in table view
            for (int i = 0; i < queryResult.getMetaData().getColumnCount(); i++) {
                TableColumn<ObservableList, String> col = new TableColumn<>(queryResult.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new PropertyValueFactory<>(queryResult.getMetaData().getColumnName(i + 1)));
                tableView.getColumns().addAll(col);
            }

            // read all the rows and fill the objects with the data from the database
            while (queryResult.next()) {
                ObservableList<String> queryRow = FXCollections.observableArrayList();
                for (int i = 1; i <= queryResult.getMetaData().getColumnCount(); i++) {
                    queryRow.add(queryResult.getString(i));
                }
                queryData.add(queryRow);
            }

            // fill the table view with the data
            tableView.setItems(queryData);
            queryResult.close();
        }
        // if something is wrong, it will be found here
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void insert(String dbCommand) {
        // try to connect to the database
        try (Connection dbConnection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/db_oop?serverTimezone=UTC",
                "root", "");
             Statement statement = dbConnection.createStatement();) {

            // execute the command to update the table
            statement.executeUpdate(dbCommand);
        }
        // if something is wrong, it will be found here
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(String dbCommand) {
        // try to connect to the database
        try (Connection dbConnection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/db_oop?serverTimezone=UTC",
                "root", "");
             Statement statement = dbConnection.createStatement();) {

            // execute the command to update the table
            statement.executeUpdate(dbCommand);
        }
        // if something is wrong, it will be found here
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(String dbCommand) {
        // try to connect to the database
        try (Connection dbConnection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/db_oop?serverTimezone=UTC",
                "root", "");
             Statement statement = dbConnection.createStatement();) {

            // execute the command to update the table
            statement.executeUpdate(dbCommand);
        }
        // if something is wrong, it will be found here
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
