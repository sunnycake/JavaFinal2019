package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;

public class DecimalPlacesInTable extends JFrame {
    public static void main(String[] args) {
        DecimalPlacesInTable frame = new DecimalPlacesInTable();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    public DecimalPlacesInTable() {
        Object[] columnNames = { "cost","tax", "cost_no_q", "cost_with_q"};
        Object[][] data = {
                { "abc", new Double( 850.503 ), 5 },
                { "def", new Double( 36.23254 ), 6 },
                { "ghi", new Double( 8.3 ), 7 },
                { "jkl", new Double( 246.0943 ), 23 }};

        JTable table = new JTable(data, columnNames);

        // Tell the table what to use to render our column of doubles

        table.getColumnModel().getColumn(1).setCellRenderer(
                new DecimalFormatRenderer() );
        getContentPane().add(new JScrollPane(table));
    }

    /**
     Here is our class to handle the formatting of the double values
     */

    static class DecimalFormatRenderer extends DefaultTableCellRenderer {
        private static final DecimalFormat formatter = new DecimalFormat( "#.00" );

        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            // First format the cell value as required

            value = formatter.format((Number)value);

            // And pass it on to parent class

            return super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column );
        }
    }
}

