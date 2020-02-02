//This method sets up the GUI

package  q2;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.TreeMap;

//method
public class GUI extends JPanel {
    private JTable _table;
    private phoneBook phoneBook;
    private JTextField _filterField;
    private JScrollPane _scrollPane;
    DefaultTableModel _model;
    private JFrame _editDialog;

//constructor
    public GUI () {
        super();
        phoneBook = new phoneBook();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        createTable();
        _scrollPane = new JScrollPane(_table);
        add(_scrollPane);

        JPanel topLevel = new JPanel();
        JPanel q1 = new JPanel();
        JPanel q2 = new JPanel();
        JPanel q3 = new JPanel();
        JPanel q4 = new JPanel();

        JLabel filterLable = new JLabel("Filter Names:");
        q1.add(filterLable);
        _filterField = new JTextField(15);
        filterLable.setLabelFor(_filterField);
        setFilterFieldListeners();

        q1.add(_filterField);
        JLabel emptyLable = new JLabel("                                                                          ");
        q1.add(emptyLable);

        JLabel addNewContactLable = new JLabel("Add New Contact:");
        q2.add(addNewContactLable);

        JLabel addNewName = new JLabel("                  Enter New Contact Name: ");
        JTextField nameToAdd = new JTextField(15);
        addNewName.setLabelFor(nameToAdd);
        JLabel addNewPhone = new JLabel("       Enter New Contact Phone: ");
        JTextField phoneToAdd = new JTextField(15);
        addNewPhone.setLabelFor(phoneToAdd);
        JButton addNewEntry = new JButton("Add New Contact");
        JButton saveButton= new JButton("Save To File");
        JButton loadButton= new JButton("Load From File");

        addNewEntry.addActionListener(
                new ContactActionListener(nameToAdd,phoneToAdd){});
        saveButton.addActionListener(
                new ContactActionListener(){});
        loadButton.addActionListener(
                new ContactActionListener(){});

        q2.add(addNewName);
        q2.add(nameToAdd);
        q3.add(addNewEntry);
        q3.add(addNewPhone);
        q3.add(phoneToAdd);
        q4.add(saveButton);
        q4.add(loadButton);
        JLabel info = new JLabel(" *For editing and deleting a contact click on it.");
        q4.add(info);

        topLevel.add(q1);
        topLevel.add(q2);
        topLevel.add(q3);
        topLevel.add(q4);
        GridLayout secondHalf = new GridLayout(4,1);
        topLevel.setLayout(secondHalf);
        add(topLevel);

        topLevel.setVisible(true);
        q1.setVisible(true);
        q2.setVisible(true);
        q3.setVisible(true);
        q4.setVisible(true);

        GridLayout mainGridLayout = new GridLayout(2,1);
        setLayout(mainGridLayout);
    } //end of GUI

    //This method  sets the listeners to the filter field

    private void setFilterFieldListeners(){
        _filterField.getDocument().addDocumentListener(
                new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        updateModelAndRepaintTable();
                    }

                    public void insertUpdate(DocumentEvent e) {
                        updateModelAndRepaintTable();
                    }

                    public void removeUpdate(DocumentEvent e) {
                        updateModelAndRepaintTable();
                    }
                }
        );
    } //end of setFilterFieldListeners

    // Creates the table

    private void createTable(){
        modelUpdater(phoneBook.filter(getTextFromFilter()));
        _table = new JTable();
        updateTable();

        _table.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        if(e.getValueIsAdjusting()==false){
                            if (_editDialog!=null) _editDialog.setVisible(false);
                            int selectedRow = _table.getSelectedRow();
                            if(selectedRow>=0) openEditDialog((String)_table.getValueAt(selectedRow,0));
                        }
                    }
                }
        );
    } //end of createTable


   //This method closes previous contact editor and opening a new one for the contact name
    private void openEditDialog(String contactName) {

        _editDialog = new JFrame("Edit Contact");
        _editDialog.setPreferredSize(new Dimension(500, 100));

        JPanel topPanel =new JPanel();
        JLabel name = new JLabel("Name:");
        JTextField newName = new JTextField(15);
        name.setLabelFor(newName);
        newName.setText(contactName);

        JLabel phone = new JLabel("Phone:");
        JTextField newPhone = new JTextField(15);
        phone.setLabelFor(newPhone);
        newPhone.setText(phoneBook.getPhone(contactName));

        topPanel.add(name);
        topPanel.add(newName);
        topPanel.add(phone);
        topPanel.add(newPhone);

        JPanel secPanel = new JPanel();
        JButton saveButton= new JButton("Save Changes And Close");
        JButton deleteButton= new JButton("Delete Contact And Close");

        secPanel.add(saveButton);
        secPanel.add(deleteButton);

        GridLayout mainGridLayout = new GridLayout(2,1);
        _editDialog.setLayout(mainGridLayout);

        _editDialog.add(topPanel);
        _editDialog.add(secPanel);
        _editDialog.setPreferredSize(new Dimension(500, 100));
        _editDialog.pack();
        _editDialog.setVisible(true);

        saveButton.addActionListener(new ContactActionListener(contactName,newName,newPhone));
        deleteButton.addActionListener(new ContactActionListener(contactName,newName,newPhone));

    } //end of openEditDialog


    // buttons action listener inner class

    class ContactActionListener implements ActionListener{

        private String oldName;
        private JTextField newPhone;
        private JTextField newName;

        ContactActionListener(){

        } //end of ContactActionListener

        //action listener for the add contact button

        ContactActionListener(JTextField name, JTextField phone) {
            this.newName = name;
            this.newPhone = phone;

        } //end of ContactActionListener

        // action listener for the editor buttons (save changes and delete changes)


        ContactActionListener (String name, JTextField newName, JTextField phone){
            this.newName = newName;
            this.oldName = name;
            this.newPhone = phone;
        } //end of ContactActionListener


        // This method will run if one of the listeners will be triggered

        public void actionPerformed(ActionEvent e) {

            if (((JButton)e.getSource()).getText()=="Add New Contact"){

                try {
                    phoneBook.add(newName.getText(),newPhone.getText());
                    updateModelAndRepaintTable();
                } catch (IllegalNameException e1) {
                    JOptionPane.showMessageDialog(null,e1.getMessage(),"Error",1);
                }
            }else

            if (((JButton)e.getSource()).getText()=="Save Changes And Close"){
                try {
                    phoneBook.update(oldName,newName.getText(),newPhone.getText());
                    updateModelAndRepaintTable();
                    _editDialog.setVisible(false);

                } catch (IllegalNameException e1) {
                    JOptionPane.showMessageDialog(null,e1.getMessage(),"Error",1);
                }
            }else

            if (((JButton)e.getSource()).getText()=="Delete Contact And Close"){
                phoneBook.remove(oldName);
                updateModelAndRepaintTable();
                _editDialog.setVisible(false);
            }else

            if(((JButton)e.getSource()).getText()=="Load From File"){
                try {
                    phoneBook.loadFromFile("phoneBook.txt");
                    updateModelAndRepaintTable();
                }catch (Exception e1){
                    JOptionPane.showMessageDialog(null,"Can't load file "+e1.getMessage(),"Error",1);
                }
            }else

            if(((JButton)e.getSource()).getText()=="Save To File"){
                try {
                    phoneBook.saveToFile("phoneBook.txt");
                }catch (Exception e1){
                    JOptionPane.showMessageDialog(null,"Can't save file "+e1.getMessage(),"Error",1);
                }
            }

        }
    } //end of actionPerformed



    // This method updates the model and repaints it

    protected void updateModelAndRepaintTable(){

        if(_table!=null) _table.clearSelection();

        modelUpdater(phoneBook.filter(getTextFromFilter()));

        if (_model!=null){
            _model.fireTableDataChanged();
        }
        if(_table!=null){
            updateTable();
        }
        if(_scrollPane!=null){
            _scrollPane.revalidate();
            _scrollPane.repaint();
        }
        revalidate();
        repaint();
    } //end of getTextFromFilter


    //validates the filter existence and returns its text

    private String getTextFromFilter(){
        if(_filterField==null) return "";
        return  _filterField.getText();
    } //end of getTextFromFilter


    // This method updates the model to be suited to the Treemap

    private void modelUpdater(TreeMap filteredphoneBook){

        createModel();
        cleanTable();
        Set<String> names = filteredphoneBook.keySet();
        for (String kye: names){
            Object[] row={kye,filteredphoneBook.get(kye)};
            _model.addRow(row);
        }

    } //end of modelUpdater


    //This method sets the latest model to the table and repaints it with the all needed properties

    private void updateTable(){
        _table.setModel(_model);
        _table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        _table.setFillsViewportHeight(true);
        _table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _table.setRowSelectionAllowed(true);
        _table.setCellSelectionEnabled(true);
        _table.setCellSelectionEnabled(true);
        _table.revalidate();
        _table.repaint();
    } //end of updateTable


    // This method creates ne model if it not exists

    private void createModel(){
        if(_model==null){
            String[] cols = {"Name","Phone"};
            _model = new DefaultTableModel(cols, 0){
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        }
    } //end of createModel


    // This method cleans the model from all the rows

    private void cleanTable(){

        if(_model==null) return;
        while (_model!=null&& _model.getRowCount()>0){
            _model.removeRow(0);
        }

    } //end of cleanTable

    // This method paints and creates the gui

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Contact List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setOpaque(true);
        frame.setContentPane(this);

        frame.pack();
        frame.setVisible(true);
    } //end of createAndShowGUI

} //end of class GUI
