
/**
 *assignment3.java
 * this is my third assignment 
 * 
 * @author Sinovuyo Mlanjeni (219220376)
 */
package za.ac.cputassignment3project;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;



public class SerializedFileClass {
    private ObjectInputStream rd;
    FileWriter fw;
    BufferedWriter bw;
    OutputStreamWriter output;
    ArrayList<Customer>c = new ArrayList<Customer>();
    ArrayList<Supplier>s = new ArrayList<Supplier>();
    
    public void openFileRead(){
        try{
            rd= new ObjectInputStream(new FileInputStream("stakeholder.ser"));
            System.out.println("***ser file creates and open for reading***");
        }
        catch (IOException ioe){
            System.out.println("Error opening ser file:"+ ioe.getMessage());
            System.exit(1);
        }
    }
    public void readFileSer(){
        try{
            while(true){
                Object record = rd.readObject();
                String cu = "Customer";
                String su= "Supplier";
                String name = record.getClass().getSimpleName();
                if (name.equals(cu)){
                    c.add((Customer)record);
                }else if (name.equals(su)){
                    s.add((Supplier)record);
               
                }else{
                    System.out.println("No record");
                }
            }
        }
        catch(EOFException eofe){
            System.out.println("File reached end here");
        }
        catch (ClassNotFoundException ioe){
            System.out.println("Class error reading ser file:" +ioe);
           
        }
        catch (IOException ioe){
            System.out.println(" Error reading ser file:" +ioe);
           
        }
    }
     public void readCloseFile(){
        try{
            rd.close( ); 
        }
        catch (IOException ioe){
            System.out.println("error closing ser file: " + ioe.getMessage());
            System.exit(1);
        }
    }
    //2b sorting the customer arraylist
     public void sortCustomerArrayList(){
         String [] IdSort = new String[c.size()];
         ArrayList<Customer> Csort = new ArrayList<Customer>();
         int count = c.size();
         for (int i = 0; i < count; i++) {
             IdSort[i]= c.get(i).getStHolderId();
     }
         Arrays.sort(IdSort);
         for (int i =0; i<count; i++){
             for (int k = 0; i < count; k++) {
                 if (IdSort[i].equals(c.get(k).getStHolderId())){
                    Csort.add(c.get(k));
             }
         }
     }
      c.clear();
      c = Csort;
     }
     //calculate the age of each customer
     public int getAge(String dob){
         String [] difference = dob.split("-");
         LocalDate born = LocalDate.of(Integer.parseInt(difference[0]), Integer.parseInt(difference[1]), Integer.parseInt(difference[2]));
         LocalDate curr = LocalDate.now();
         Period p = Period.between(born, curr);
         int age = p.getYears();
         return age;
    }
     //Re-format 
     public String formatDob(String dob){
         DateTimeFormatter cFormat = DateTimeFormatter.ofPattern("dd MM yyy");
         LocalDate born = LocalDate.parse(dob);
         String formatted = born.format(cFormat);
         return formatted;
     }
     
     //DISPLAY THE CUSTOMERTEXT
     public void displayCustomerText(){
         try{
             fw= new FileWriter("customerOutFile.txt");
             bw = new BufferedWriter(fw);
             bw.write(String.format("%s \n","==============================Customers============================="));
            bw.write(String.format("%-15s %-15s %-15s %-15s %-15s\n", "ID","Name","Surname","Date of Birth","Age"));
            bw.write(String.format("%s \n","===================================================================="));
            for (int i = 0; i < c.size(); i++) {
                bw.write(String.format("%-15s %-15s %-15s %-15s %-15s\n", c.get(i).getStHolderId(), c.get(i).getFirstName(), c.get(i).getSurName(), formatDob(c.get(i).getDateOfBirth()),getAge(c.get(i).getDateOfBirth())));
            }
            bw.write(String.format("%s\n"," "));
            bw.write(String.format("%s",rent()));
         }
         catch(IOException fnfe){
             System.out.println(fnfe);
             System.exit(1);
         }
         try{
             bw.close();
         }
         catch (IOException ioe){
            System.out.println("error closing text file: " + ioe.getMessage());
            System.exit(1);
        }
     }
     public String rent(){
        int count = c.size();
        int canRent = 0;
        int notRent = 0;
        for (int i = 0; i < count; i++) {
            if (c.get(i).getCanRent()){
                canRent++;
            }else {
                notRent++;
            }
        }
        String l =String.format("Number of customers who can rent : %4s\nNumber of customers who cannot rent : %s\n", canRent, notRent);
        return l;
    }
     //supplier
     
     //sort suppliers
     public void sortSuppliers(){
        String[] IdSort = new String[s.size()];
        ArrayList<Supplier> Ssort= new ArrayList<Supplier>();
        int count = s.size();
        for (int i = 0; i < count; i++) {
            IdSort[i] = s.get(i).getName();
        }
        Arrays.sort(IdSort);
        
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                if (IdSort[i].equals(s.get(j).getName())){
                    Ssort.add(s.get(j));
                }
            }
        }
        s.clear();
        s = Ssort;
     }
     public void displaySupplierText(){
        try{
            fw = new FileWriter("supplierOutFile.txt", true);
            bw = new BufferedWriter(fw);
            bw.write("===============================SUPPLIERS==============================\n");
            bw.write(String.format("%-15s %-20s %-15s %-15s \n", "ID","Name","Prod Type","Description"));
            bw.write("======================================================================\n");
            for (int i = 0; i < s.size(); i++) {
                bw.write(String.format("%-15s %-20s %-15s %-15s \n", s.get(i).getStHolderId(), s.get(i).getName(), s.get(i).getProductType(),s.get(i).getProductDescription()));
            }
        }
        catch(IOException fnfe )
        {
            System.out.println(fnfe);
            System.exit(1);
        }
        try{
            bw.close( );
        }
        catch (IOException ioe){
            System.out.println("error closing text file: " + ioe.getMessage());
            System.exit(1);
        }
    } 
     public static void main(String[] args) {
        SerializedFileClass sol = new SerializedFileClass();
        sol.openFileRead();
        sol.readFileSer();
        sol.readCloseFile();
        sol.sortCustomerArrayList();
        sol.sortSuppliers();
        sol.displayCustomerText();
        sol.displaySupplierText();
     } 
        
     
    }
     

  
    
