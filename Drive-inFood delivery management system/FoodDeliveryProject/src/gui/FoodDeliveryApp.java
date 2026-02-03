package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FoodDeliveryApp extends JFrame {
    private JTextField customerField ,phoneField, customItemField, customPriceField;
    private JButton addButton, updateQtyButton, addCustomButton, removeButton, clearButton, confirmButton, paymentButton, saveReceiptButton;
private JComboBox<String> menuBox;
    private JSpinner qtySpinner;
    private JTable cartTable;
    private DefaultTableModel cartModel;
    private JLabel totalLabel;
    private JTextArea receiptArea; 
    private JPanel receiptMainPanel; 
   
    private double total = 0.0;
    private String currentReceiptText = "";
    private JPanel customerPanel;


    public FoodDeliveryApp() {
        setTitle("Drive-in Food Delivery Management System");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Panel
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        add(panel);

        // upoere  Customer info
        customerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // kono Jpanel nai ekhane
Color panelCol = new Color(255, 211, 110);
customerPanel.setBackground(panelCol);

customerPanel.add(new JLabel("Customer Name:"));
customerField = new JTextField(20);
customerPanel.add(customerField);

customerPanel.add(new JLabel("Phone Number:"));
phoneField = new JTextField(11);
customerPanel.add(phoneField);

panel.add(customerPanel, BorderLayout.NORTH);

        panel.add(customerPanel, BorderLayout.NORTH);

        // majhkhane  Menu + Cart
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        // bame side (menu + add)
        JPanel menuPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        Color menuCol = new Color(223, 224, 186);
        menuPanel.setBackground(menuCol);
        menuPanel.setBorder(BorderFactory.createTitledBorder("Menu"));
        
        
        menuPanel.add(new JLabel("Menu Item:"));
        
        String[] menuItems = {"Burger - 150", "Pizza - 250", "Pasta - 200", "Sandwich - 120", "chicken - 125", "Nachos - 220", "Meat Box - 170", "Nuggets - 250"};
         
        menuBox = new JComboBox<>(menuItems);
        menuPanel.add(menuBox);
        
        menuPanel.add(new JLabel("Qty:"));//qty gula jeno interger type thake
        qtySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        menuPanel.add(qtySpinner);
        
        
        Color itemCol = new Color (160, 235, 160);
JButton addButton = new JButton("Add Item");
        addButton.setBackground(itemCol);
        menuPanel.add(addButton);
        
         Color updateCol = new Color (160, 235, 160);
        JButton updateQtyButton = new JButton("Update Quantity");
        updateQtyButton.setBackground(updateCol); 
        menuPanel.add(updateQtyButton);
        
        
        menuPanel.add(new JLabel("Custom Item:"));
        customItemField = new JTextField();
      menuPanel.add(customItemField);
        
        menuPanel.add(new JLabel("Price:"));
        customPriceField = new JTextField();
        menuPanel.add(customPriceField);
        
        Color customBtnCol = new Color (160, 235, 160);
        JButton addCustomButton = new JButton("Add Custom");
addCustomButton.setBackground(customBtnCol); 
        menuPanel.add(addCustomButton);
        
        centerPanel.add(menuPanel);

        // dan side  Cart table with logo
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createTitledBorder("Cart"));
        
        // Add logo at the top of cart panel
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel logoLabel = createLogoLabel();
        logoPanel.add(logoLabel);
        cartPanel.add(logoPanel, BorderLayout.NORTH);
       
        cartModel = new DefaultTableModel(new Object[]{"Item", "Qty", "Price", "Subtotal"}, 0);
        cartTable = new JTable(cartModel);
        
        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);
        
        centerPanel.add(cartPanel);

        // main content panel banay jeno center and receipt
        JPanel mainContentPanel = new JPanel(new BorderLayout(10, 10));
        mainContentPanel.add(centerPanel, BorderLayout.CENTER);

        // Receipt section at upore
        receiptMainPanel = new JPanel(new BorderLayout());
        receiptArea = new JTextArea(8, 0);
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane receiptScrollPane = new JScrollPane(receiptArea);
        
        JPanel receiptPanel = new JPanel(new BorderLayout());
        receiptPanel.setBorder(BorderFactory.createTitledBorder("Receipt"));
        receiptPanel.add(receiptScrollPane, BorderLayout.CENTER);
        
        // Button panel niche receipt
        JPanel receiptButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        paymentButton = new JButton("Mark Payment Completed");
        saveReceiptButton = new JButton("Save Receipt to File");
        receiptButtonPanel.add(paymentButton);
        receiptButtonPanel.add(saveReceiptButton);
        
        receiptMainPanel.add(receiptPanel, BorderLayout.CENTER);
        receiptMainPanel.add(receiptButtonPanel, BorderLayout.SOUTH);
        receiptMainPanel.setVisible(false); // Initially hidden
        
        mainContentPanel.add(receiptMainPanel, BorderLayout.SOUTH);
        panel.add(mainContentPanel, BorderLayout.CENTER);

        
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton removeButton = new JButton("Remove Selected");
        removeButton.setBackground(Color.RED);
        removeButton.setForeground(Color.WHITE);

        
        JButton clearButton = new JButton("Clear Cart");
        clearButton.setBackground(Color.GRAY);

        JButton confirmButton = new JButton("Confirm Order");
        confirmButton.setBackground(Color.GREEN);
        totalLabel = new JLabel("Total: 0.00");
        
        bottomPanel.add(removeButton);
        bottomPanel.add(clearButton);
        bottomPanel.add(totalLabel);
        bottomPanel.add(confirmButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Action Listeners
        addButton.addActionListener(e -> addMenuItem());
        updateQtyButton.addActionListener(e -> updateQuantity());
        addCustomButton.addActionListener(e -> addCustomItem());
        removeButton.addActionListener(e -> removeSelected());
        clearButton.addActionListener(e -> clearCart());
        confirmButton.addActionListener(e -> confirmOrder());
        paymentButton.addActionListener(e -> markPaymentCompleted());
        saveReceiptButton.addActionListener(e -> saveReceiptToFile());
    }

    private JLabel createLogoLabel() {
        JLabel logoLabel;
        //logo ta ke dhorar try korbe
        try {
            
            String logoPath = "src/logo/java projectlogo.png";  
            
            
            
            ImageIcon logoIcon = new ImageIcon(logoPath);
            
            // dekhbe je image load hoise kina
            if (logoIcon.getIconWidth() <= 0) {
                throw new Exception("Image not found at: " + logoPath);
            }
            
            // image er size check kore
            Image img = logoIcon.getImage().getScaledInstance(150, 120, Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(img);
            
            logoLabel = new JLabel(logoIcon);
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
        } catch (Exception e) {
            // jodi path a logo na thake tahole se eta dekhabe
            System.out.println("Logo loading failed: " + e.getMessage());
            logoLabel = new JLabel("FOOD DELIVERY");
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            logoLabel.setFont(new Font("Arial", Font.BOLD, 12));
            logoLabel.setForeground(new Color(255, 100, 100));
            logoLabel.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        }
        
        logoLabel.setToolTipText("Food Delivery System Logo");
        return logoLabel;
    }

    private void addMenuItem() {
        String selected = (String) menuBox.getSelectedItem();
        int qty = (int) qtySpinner.getValue();
        
        if (selected != null) {
            String[] parts = selected.split(" - ");
            String item = parts[0];
            double price = Double.parseDouble(parts[1]);
            
            int existingRow = findItemInCart(item);
            if (existingRow >= 0) {
                int currentQty = Integer.parseInt(cartModel.getValueAt(existingRow, 1).toString());
                int newQty = currentQty + qty;
                double newSubtotal = price * newQty;
                
                cartModel.setValueAt(newQty, existingRow, 1);
                cartModel.setValueAt(newSubtotal, existingRow, 3);
                
                total = total - (price * currentQty) + newSubtotal;
            } else {
                double subtotal = price * qty;
                cartModel.addRow(new Object[]{item, qty, price, subtotal});
                total += subtotal;
            }
            updateTotal();
        }
    }

    private void updateQuantity() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow >= 0) {
            int additionalQty = (int) qtySpinner.getValue();
            
            int currentQty = Integer.parseInt(cartModel.getValueAt(selectedRow, 1).toString());
            double price = Double.parseDouble(cartModel.getValueAt(selectedRow, 2).toString());
            
            int newQty = currentQty + additionalQty;
            double newSubtotal = price * newQty;
            double oldSubtotal = Double.parseDouble(cartModel.getValueAt(selectedRow, 3).toString());
            
            cartModel.setValueAt(newQty, selectedRow, 1);
            cartModel.setValueAt(newSubtotal, selectedRow, 3);
            
            total = total - oldSubtotal + newSubtotal;
            updateTotal();
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item from cart to update!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int findItemInCart(String itemName) {
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            String cartItem = cartModel.getValueAt(i, 0).toString();
            if (cartItem.equals(itemName)) {
                return i;
            }
        }
        return -1;
    }

    private void addCustomItem() {
        String item = customItemField.getText().trim();
        String priceText = customPriceField.getText().trim();
        
        if (!item.isEmpty() && !priceText.isEmpty()) {
            try {
                double price = Double.parseDouble(priceText);
                int qty = (int) qtySpinner.getValue();
                
                int existingRow = findItemInCart(item);
                if (existingRow >= 0) {
                    int currentQty = Integer.parseInt(cartModel.getValueAt(existingRow, 1).toString());
                    int newQty = currentQty + qty;
                    double newSubtotal = price * newQty;
                    
                    cartModel.setValueAt(newQty, existingRow, 1);
                    cartModel.setValueAt(newSubtotal, existingRow, 3);
                    
                    total = total - (price * currentQty) + newSubtotal;
                } else {
                    double subtotal = price * qty;
                    cartModel.addRow(new Object[]{item, qty, price, subtotal});
                    total += subtotal;
                }
                
                updateTotal();
                customItemField.setText("");
                customPriceField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter valid price!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removeSelected() {
        int row = cartTable.getSelectedRow();
        if (row >= 0) {
            double subtotal = Double.parseDouble(cartModel.getValueAt(row, 3).toString());
            total -= subtotal;
            cartModel.removeRow(row);
            updateTotal();
        }
    }

    private void clearCart() {
        cartModel.setRowCount(0);
        total = 0.0;
        updateTotal();
        receiptMainPanel.setVisible(false);
        currentReceiptText = "";
    }

    private void updateTotal() {
        totalLabel.setText("Total: " + String.format("%.2f", total));
    }

    private void confirmOrder() {
        String customer = customerField.getText().trim();
        if (customer.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter customer name!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
String phone = phoneField.getText().trim();
        if(phone.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Enter customer phone number","Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (cartModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Cart is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // receipt dibe timer sathe timestamp
        StringBuilder receipt = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
   

        receipt.append("==*===Drive in Food Delivery Receipt==*===\n");
        receipt.append("Date: ").append(now.format(formatter)).append("\n");
        receipt.append("Customer: ").append(customer).append("\n\n");
        receipt.append("Number ").append(phone).append("\n\n");
        receipt.append(String.format("%-15s %-5s %-7s %-7s\n", "Item", "Qty", "Price", "Subtotal"));
        receipt.append("----------------------------------\n");
        
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            String item = String.valueOf(cartModel.getValueAt(i, 0));
            int qty = Integer.parseInt(cartModel.getValueAt(i, 1).toString());
            double unitPrice = Double.parseDouble(cartModel.getValueAt(i, 2).toString());
            double subtotal = Double.parseDouble(cartModel.getValueAt(i, 3).toString());
            receipt.append(String.format("%-15s %-5d %-7.2f %-7.2f\n", item, qty, unitPrice, subtotal));
        }
        
        receipt.append("\n----------------------------------\n");
        receipt.append("Total: ").append(String.format("%.2f", total)).append("\n");
        receipt.append("Thank you for your order!\n");
        receipt.append("Status: Order Confirmed - Payment Pending");

        // save kore current receipt text
        currentReceiptText = receipt.toString();

        // Show receipt bottom a dekhabe
        receiptArea.setText(currentReceiptText);
        receiptMainPanel.setVisible(true);
        
        // Refresh the layout
        revalidate();
        repaint();
        
        JOptionPane.showMessageDialog(this, "Order confirmed! Receipt shown below.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void markPaymentCompleted() {
        String currentText = receiptArea.getText();
        if (currentText.contains("Payment Pending")) {
            String updatedText = currentText.replace("Status: Order Confirmed - Payment Pending", 
                                                   "Status: Payment Completed ✓");
            receiptArea.setText(updatedText);
            currentReceiptText = updatedText;
            JOptionPane.showMessageDialog(this, "Payment marked as completed!", "Payment Status", JOptionPane.INFORMATION_MESSAGE);
        } else if (currentText.contains("Payment Completed")) {
            JOptionPane.showMessageDialog(this, "Payment already marked as completed!", "Payment Status", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No active order to mark payment for!", "Payment Status", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void saveReceiptToFile() {
        if (currentReceiptText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No receipt to save! Please confirm an order first.", "No Receipt", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // notun file create korbe timstamp er sathe .txt file diye
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fileFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String customerName = customerField.getText().trim().replaceAll("[^a-zA-Z0-9]", "_");
        
        // check kore customer name is not empty
        if (customerName.isEmpty()) {
            customerName = "Customer";
        }
        
        // Create filename with .txt extension korbe
        String filename = "Receipt_" + customerName + "_" + now.format(fileFormatter) + ".txt";

        // File I/O operations shuru ekhan theke proper error handling
        FileWriter writer = null;
        try {
            // Create FileWriter object .txt file lekhar jnno
            writer = new FileWriter(filename);
            
            // complete txt file likhbe ekhane
            writer.write("=====================================\n");
            writer.write("     FOOD DELIVERY RECEIPT FILE     \n");
            writer.write("=====================================\n\n");
            writer.write(currentReceiptText);
            writer.write("\n\n=====================================\n");
            writer.write("Receipt saved on: " + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
            writer.write("File generated by Food Delivery System\n");
            writer.write("=====================================\n");
            
            // Flush the writer jate sob file lekha hoise kina
            writer.flush();
            //text file er vitor egula lekha ashbe
            JOptionPane.showMessageDialog(this, 
                "✅ Receipt successfully saved as TEXT file!\n\n" +
                "📁 File name: " + filename + "\n" +
                "📂 Location: " + System.getProperty("user.dir") + "\n" +
                "💾 File type: Plain Text (.txt)", 
                "File I/O Success", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (IOException e) {
            // I/O operation handle korbe 
            JOptionPane.showMessageDialog(this, 
                "❌ Error during File I/O operation!\n\n" +
                "Unable to save receipt to text file.\n" +
                "Error details: " + e.getMessage() + "\n\n" +
                "Please check:\n" +
                "• File permissions\n" +
                "• Available disk space\n" +
                "• File path accessibility", 
                "File I/O Error", 
                JOptionPane.ERROR_MESSAGE);
                
            // debugging er jonno print stack check kore
            e.printStackTrace();
            
        } finally {
            // sobsomoy jeno  close the FileWriter in finally block
            if (writer != null) {
                try {
                    writer.close();
                    System.out.println("✅ File I/O completed successfully. File closed: " + filename);
                } catch (IOException e) {
                    System.err.println("❌ Error closing file: " + e.getMessage());
                }
            }
        }
    }
}