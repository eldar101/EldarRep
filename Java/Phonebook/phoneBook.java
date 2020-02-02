//This class defines the phone book itself properly, using a TreeMap as we learned in 16.11

package q2;

import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class phoneBook {

    private TreeMap<String, String> phoneBook;

    // This method creates an empty phone book

    public phoneBook() {
        this.phoneBook = new TreeMap<String, String>();
    } //end of phoneBook


    //This method creates a phone book from an existing map
    public phoneBook(TreeMap<String, String> phoneBook) {
        this.phoneBook = phoneBook;
    } //end of phoneBook


    //This method adds a new contact to the phone book
    public void add(String name, String phone) throws IllegalNameException { // checks if name has error
        name = this.validate(name); //validates name
        this.phoneBook.put(name, phone); //adds the contact
    } //end of add


    // This method removes a contact from the phone book

    public boolean remove(String name) {

        name = name.trim(); //trims from TreeMap
        Object entry = this.phoneBook.remove(name); //removes
        if (entry != null) return true; //varifies it was removed
        return false;
    } //end of remove


    // This method updates the existing contact

    public boolean update(String name, String newName, String newPhone) throws IllegalNameException {

        name = name.trim(); //trim
        newName = newName.trim();
        if (!newName.equals(name)) newName = this.validate(newName); //validates
        if (this.phoneBook.remove(name) == null) return false; //if empty
        this.phoneBook.put(newName, newPhone); //updates
        return true;
    } //end of update


    //This method saves this phone book to a text file, as we learned in the book

    public void saveToFile(String fileName) throws IOException {

        FileWriter fileWriter = new FileWriter(fileName);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        for (Map.Entry<String, String> entry : phoneBook.entrySet()) {
            String name = entry.getKey();
            String phone = entry.getValue();

            bufferedWriter.write(name + "=" + phone + "\n");
        }
        bufferedWriter.close();
    } //end of saveToFile


    // This method loads Phone book from a file,

    public void loadFromFile(String filePath) throws IOException, IllegalNameException {

        BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
        String line = "";
        phoneBook = new TreeMap<>();

        while ((line = reader.readLine()) != null) {
            if (line.contains("=")) {
                String[] strings = line.split("=");
                this.add(strings[0].trim(), strings[1].trim());
            }
        }
        reader.close();
    } //end of loadFromFile


    //This method finds a target string

    public TreeMap filter(String target) {

        target = target.trim();
        if (target.equals("") || target.equals(null)) return new TreeMap(this.phoneBook);
        String fromKey = this.findFirstProperKey(target);
        String toKey = this.findLastKeyToFind(target);
        if (fromKey == null) return new TreeMap();
        return new TreeMap(this.phoneBook.subMap(fromKey, true, toKey, true));
    } //end of filter


    // This method retrieves the name and phone

    public String getPhone(String name) {
        return phoneBook.get(name);

    } //end of getPhone


    //This method finds first value that equals or bigger than the key name
    private String findFirstProperKey(String stringToFind) {

        Set<String> names = phoneBook.keySet();
        for (String key : names) {
            int correctChars = 0;
            for (int i = 0; i < stringToFind.length() && i < key.length(); i++) { //searches in a loop
                if (stringToFind.charAt(i) == key.charAt(i)) correctChars++;
                if (correctChars == stringToFind.length()) return key;
            }
        }
        return null;
    } //end of findFirstProperKey


    //This method returns  the first smaller key than the smallest bigger value than stringToFind

    private String findLastKeyToFind(String stringToFind) {

        Set<String> names = phoneBook.keySet();
        String properKey = null;
        for (String key : names) {
            int correctChars = 0;
            for (int i = 0; i < stringToFind.length() && i < key.length(); i++) {
                if (stringToFind.charAt(i) == key.charAt(i)) correctChars++;
                if (correctChars == stringToFind.length()) properKey = key;
            }
        }
        return properKey;
    } //end of findLastKeyToFind


    //This method validates a name
    private String validate(String name) throws IllegalNameException {
        name = name.trim();
        if (name.equals("") || name.equals(null)) //name can't be empty
            throw new IllegalNameException("Name must contain at least one character");
        if (name.contains("=") || name.contains("\\")) //illegal characters
            throw new IllegalNameException("The name can't contain '=' and '\\'.");
        if (this.phoneBook.containsKey(name))
            throw new IllegalNameException("The name already exists in the phone book."); //name already exists
        return name;
    } //end of


    // basic toString method

    public String toString() {
        return "phoneBook{" + phoneBook +
                '}';
    } //end of toString
} //end of class phoneBook
