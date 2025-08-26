import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ContactManagerGui extends JFrame {
    private DefaultTableModel tableModel;
    private JTable contactTable;
    private final String FILE_NAME = "contacts.txt";

    public ContactManagerGui() {
        setTitle("Contact Management System");
        setSize(500, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(new String[] {"Name","Phone","Email"},0);
        contactTable = new JTable(tableModel);
        loadContactsFromFile();

        JScrollPane scrollPane = new JScrollPane(contactTable);
        JButton addButton = new JButton("Add Button");
        JButton editButton = new JButton("Edit Button");
        JButton deleteButton = new JButton("Delete Button");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        addButton.addActionListener(e->addContact());
        editButton.addActionListener(e->editContact());
        deleteButton.addActionListener(e->deleteContact());
    }
    private void addContact() {
        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        Object[] message = {
            "Name", nameField,
            "Phone", phoneField,
            "Email", emailField
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Add Contact", JOptionPane.OK_CANCEL_OPTION);
        if (option==JOptionPane.OK_OPTION) {
            tableModel.addRow(new Object[]{nameField.getText(), phoneField.getText(), emailField.getText()});
            saveContactsToFile();
        }
    }
    private void editContact() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow==-1) {
            JOptionPane.showMessageDialog(this, "Select a contact to edit");
            return;
        }
        String name = tableModel.getValueAt(selectedRow, 0).toString();
        String phone = tableModel.getValueAt(selectedRow, 1).toString();
        String email = tableModel.getValueAt(selectedRow, 2).toString();
        JTextField nameField = new JTextField(name);
        JTextField phoneField = new JTextField(phone);
        JTextField emailField = new JTextField(email);
        Object[] message = {
            "Name", nameField,
            "Phone", phoneField,
            "Email", emailField
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Edit Contact", JOptionPane.OK_CANCEL_OPTION);
        if (option==JOptionPane.OK_OPTION) {
            tableModel.setValueAt(nameField.getText(), selectedRow, 0);
            tableModel.setValueAt(phoneField.getText(), selectedRow, 1);
            tableModel.setValueAt(emailField.getText(), selectedRow, 2);
            saveContactsToFile();
        }
    }
    private void deleteContact() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow==-1) {
            JOptionPane.showMessageDialog(this, "Select a contact to delete");
            return;

        }
        tableModel.removeRow(selectedRow);
        saveContactsToFile();
    }
    private void loadContactsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line= reader.readLine()) !=null) {
                String[] parts = line.split(",");
                tableModel.addRow(parts);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    private void saveContactsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i=0;i<tableModel.getRowCount();i++) {
                writer.write(tableModel.getValueAt(i, 0)+","+
                             tableModel.getValueAt(i, 1)+","+
                             tableModel.getValueAt(i, 2));
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving contacts" +e.getMessage());
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            new ContactManagerGui().setVisible(true);
        });
    }
}