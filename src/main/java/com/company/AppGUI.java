package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

public class AppGUI extends JFrame {

    private JPanel mainPanel;
    private JLabel titleLabel;
    private JTextField mCouponTextField;
    private JComboBox mCouponQty;
    private JTextField sCouponTextField;
    private JComboBox sCouponQty;
    private JComboBox itemQty;
    private JTextField costWithQTextField;
    private JButton calculateButton;
    private JTextField costNoQTextField;
    private JTextField productDealTextField;
    private JTextField productCostTextField;
    private JButton saveButton;
    private JButton clearButton;
    private JButton UpdateButton;
    private JTextField receiveBackTextField;
    private JButton deleteButton;
    private JTable targetShopListTable;
    private JTextField taxTextField;
    private JTextField productNameTextField;

    private ItemsDB db;
    private DefaultTableModel tableModel;
    private Vector columnNames;


    AppGUI(ItemsDB db) {

        this.db = db;


        setContentPane(mainPanel);
        pack();
        setTitle(" Target Coupon Shopper Application");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // To center JFrame.

        configureTable();

        setVisible(true);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelected();
            }
        });
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateCost();
            }
        });
        UpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateShopList();
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //This resets all fields back to null.
                productDealTextField.setText(null);
                productNameTextField.setText(null);
                productCostTextField.setText(null);
                itemQty.setSelectedItem(null);
                mCouponTextField.setText(null);
                mCouponQty.setSelectedItem(null);
                sCouponTextField.setText(null);
                sCouponQty.setSelectedItem(null);
                costNoQTextField.setText(null);
                costWithQTextField.setText(null);
                receiveBackTextField.setText(null);

            }
        });
    }

    private void errorDialog(String msg) {
        JOptionPane.showMessageDialog(AppGUI.this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void addProduct() {
        //getting the input and adding to the database.
        String weeklyDeal = productDealTextField.getText();
        String cost_NO_Q = costNoQTextField.getText();
        String costWITH_Q = costWithQTextField.getText();
        String rcvBack = receiveBackTextField.getText();
        double taxCharged = Double.parseDouble(taxTextField.getText());
        double mfr_Q = Double.parseDouble(mCouponTextField.getText());
        double mfrQ_qty = Double.parseDouble(mCouponQty.getSelectedItem().toString());
        double sQ = Double.parseDouble(sCouponTextField.getText());
        double sQ_qty = Double.parseDouble(sCouponQty.getSelectedItem().toString());

        String productName = productNameTextField.getText();
        if (productName.isEmpty()) {
            JOptionPane.showMessageDialog(rootPane, "Please enter a product name.");
            return;
        }
        double cost;

        try {
            cost = Double.parseDouble(productCostTextField.getText());
            if (cost == 0) {
                throw new NumberFormatException("Invalid cost entry.");
            }
        } catch (NumberFormatException nfe) {
            errorDialog("Please enter a price.");
            return;
        }
        int itemCount;
        try {
            itemCount = Integer.parseInt(itemQty.getSelectedItem().toString());
            if (itemCount == 0) {
                throw new NumberFormatException("Enter a qty greater than 1.");
            }
        } catch (NumberFormatException nfe) {
            errorDialog("Please enter a price.");
            return;
        }

        db.addProduct(productName, weeklyDeal, cost, itemCount, mfr_Q, mfrQ_qty, sQ, sQ_qty,
                taxCharged, cost_NO_Q, costWITH_Q, rcvBack);
        {
            JOptionPane.showMessageDialog(saveButton, "Saved Successfully");
            updateTable();
        }
    }

    private void deleteSelected() {
        //delete selected row.
        int i = targetShopListTable.getSelectedRow();
        if (i >= 0) {
            // remove a row from jtable
            tableModel.removeRow(i);
        } else {
            int id = (Integer) tableModel.getValueAt(i, 0);
            db.deleteShopList(id);
            updateTable();
        }
        JOptionPane.showMessageDialog(deleteButton, "Deleted Successfully");
    }

    private void calculateCost() {
        /*converting numbers in order to calculate then
         * inserting the calculated data into appropriate field box.*/
        double tax = Double.parseDouble(taxTextField.getText());

        double cost = Double.parseDouble(productCostTextField.getText());
        double qty = Double.parseDouble(itemQty.getSelectedItem().toString());
        double productCostTotal = cost * qty;

        double taxOnFullPrice = productCostTotal * tax;
        double fullPrice = productCostTotal + taxOnFullPrice;
        costNoQTextField.setText(String.format("$%.2f", fullPrice));

        double manuCoupon = Double.parseDouble(mCouponTextField.getText());
        double mQty = Double.parseDouble(mCouponQty.getSelectedItem().toString());
        double mCouponTotal = manuCoupon * mQty;

        double storeCoupon = Double.parseDouble(sCouponTextField.getText());
        double sQty = Double.parseDouble(sCouponQty.getSelectedItem().toString());
        double sCouponTotal = storeCoupon * sQty;

        double sTotalDiscount = productCostTotal - sCouponTotal;
        double discountTotal = sTotalDiscount - mCouponTotal;

        double taxOnCouponPrice = discountTotal * tax;
        double couponPrice = discountTotal + taxOnCouponPrice;
        costWithQTextField.setText(String.format("$%.2f", couponPrice));
    }

    private void updateShopList() {
        //update each column with data change.
        int i = targetShopListTable.getSelectedRow();
        if (i >= 0) {
            tableModel.setValueAt(productNameTextField.getText(), i, 1);
            tableModel.setValueAt(productDealTextField.getText(), i, 2);
            tableModel.setValueAt(itemQty.getSelectedItem(), i, 3);
            tableModel.setValueAt(mCouponTextField.getText(), i, 4);
            tableModel.setValueAt(mCouponQty.getSelectedItem(), i, 5);
            tableModel.setValueAt(sCouponTextField.getText(), i, 6);
            tableModel.setValueAt(sCouponQty.getSelectedItem(), i, 7);
            tableModel.setValueAt(taxTextField.getText(), i, 8);
            tableModel.setValueAt(costNoQTextField.getText(), i, 9);
            tableModel.setValueAt(costWithQTextField.getText(), i, 10);
            tableModel.setValueAt(receiveBackTextField.getText(), i, 11);

        } else {
            System.out.println("Update error");
        }
    }

    private void configureTable() {
        //Method for the configuring table and editing column 1
        targetShopListTable.setAutoCreateRowSorter(true);
        columnNames = db.getColumnNames();
        Vector data = db.getAllShopList();

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return (col == 0);  // Rating column only.
            }

        };
        targetShopListTable.setModel(tableModel);
    }

    private void updateTable() {

        Vector data = db.getAllShopList();
        tableModel.setDataVector(data, columnNames);

    }


}

