package com.company;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

public class ItemsDB {

    private static final String DB_CONNECTION_URL = "jdbc:sqlite:targetQ.db";

    private static final String PRODUCT_NAME_COL = "product_name";
    private static final String WEEKLY_DEAL_COL = "weekly_deal";
    private static final String COST_COL = "cost";
    private static final String PRODUCT_QTY_COL = "product_qty";
    private static final String MANU_COUPON_COL = "mfr_coupon";
    private static final String MANU_QTY_COL = "mfr_q_qty";
    private static final String STORE_COUPON_COL = "store_coupon";
    private static final String STORE_QTY_COL = "store_q_qty";
    private static final String TAX_COL = "tax";
    private static final String COST_B4_COL = "cost_no_q";
    private static final String COST_AFTER_COL = "cost_with_q";
    private static final String RECEIVE_COL = "receive";

    private static final String CREATE_SHOPLIST_TABLE = "CREATE TABLE IF NOT EXISTS targetShopView (product_name TEXT , " +
            "weekly_deal TEXT, cost INTEGER, product_qty INTEGER, mfr_coupon INTEGER, mfr_q_qty INTEGER, " +
            "store_coupon INTEGER, store_q_qty INTEGER, tax INTEGER, cost_no_q INTEGER, " +
            "cost_with_q INTEGER, receive INTEGER)";

    private static final String GET_ALL_SHOPLIST = "SELECT * FROM targetShopView";
    //private static final String EDIT_SHOP_LIST = "UPDATE targetShopView SET product_name = ? WHERE ID = ?";
    private static final String ADD_TO_SHOPLIST = "INSERT INTO targetShopView (product_name, weekly_deal, cost, product_qty, " +
            "mfr_coupon, mfr_q_qty, store_coupon, store_q_qty, tax, cost_no_q, cost_with_q, receive) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

    ItemsDB() { createTable(); }

    private void createTable() {

        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_SHOPLIST_TABLE);

        } catch (SQLException e) {
            if (e.getMessage().contains("(table targetQ already exists)")) {
            } else {
            }
            throw new RuntimeException(e);
        }
    }

    Vector getColumnNames() {

        Vector colNames = new Vector();
        colNames.add("Product Name ");
        colNames.add("Weekly Deal");
        colNames.add("Cost");
        colNames.add("Product Qty");
        colNames.add("Mfr Coupon");
        colNames.add("MfrQ Qty");
        colNames.add("Store Coupon");
        colNames.add("StoreQ Qty");
        colNames.add("Tax");
        colNames.add("Cost No Q");
        colNames.add("Cost WITH Q");
        colNames.add("Receive back");


        return colNames;
    }
    Vector<Vector> getAllShopList() {

        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery(GET_ALL_SHOPLIST);

            Vector<Vector> vectors = new Vector<>();

            String productName, deal;
            int cost, prodQty, mfrQ, mfrQty, storeQ, storeQqty, taxfee, costNoQ, costWitQ, receivesBack;

            while (rs.next()) {

                productName = rs.getString(PRODUCT_NAME_COL);
                deal = rs.getString(WEEKLY_DEAL_COL);
                cost = rs.getInt(COST_COL);
                prodQty = rs.getInt(PRODUCT_QTY_COL);
                mfrQ = rs.getInt(MANU_COUPON_COL);
                mfrQty = rs.getInt(MANU_QTY_COL);
                storeQ = rs.getInt(STORE_COUPON_COL);
                storeQqty = rs.getInt(STORE_QTY_COL);
                taxfee = rs.getInt(TAX_COL);
                costNoQ = rs.getInt(COST_B4_COL);
                costWitQ = rs.getInt(COST_AFTER_COL);
                receivesBack = rs.getInt(RECEIVE_COL);

                Vector v = new Vector();
                v.add(productName); v.add(deal); v.add(cost); v.add(prodQty);v.add(mfrQ);v.add(mfrQty);
                v.add(storeQ); v.add(storeQqty); v.add(taxfee); v.add(costNoQ); v.add(costWitQ); v.add(receivesBack);

                vectors.add(v);
            }

            return vectors;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void addProduct(String product_name, String weekly_deal, double cost, double product_qty, double mfr_coupon, double mfr_q_qty,
                           double store_coupon, double store_q_qty, double tax, String cost_no_q, String cost_with_q, String receive) {

            try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
                 PreparedStatement preparedStatement = connection.prepareStatement(ADD_TO_SHOPLIST)) {

                preparedStatement.setString(1, product_name);
                preparedStatement.setString(2, weekly_deal);
                preparedStatement.setDouble(3, cost);
                preparedStatement.setDouble(4, product_qty);
                preparedStatement.setDouble(5, mfr_coupon);
                preparedStatement.setDouble(6, mfr_q_qty);
                preparedStatement.setDouble(7, store_coupon);
                preparedStatement.setDouble(8, store_q_qty);
                preparedStatement.setDouble(9, tax);
                preparedStatement.setString(10, cost_no_q);
                preparedStatement.setString(11, cost_with_q);
                preparedStatement.setString(12, receive);

                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }



