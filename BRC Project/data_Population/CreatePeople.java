//This program populates the following tables
//customer
//rewards_account
//cart
//cust_cart


import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.sql.*;
import java.util.Scanner;
import java.util.Date;

class CreatePeople{
  public static void main(String[] args){
    Scanner myScan = new Scanner(System.in);
    Random myRand = new Random();
    boolean valid_login = false;

    try{
      Class.forName("oracle.jdbc.driver.OracleDriver");
    }catch(Exception e){e.printStackTrace();}

    while(valid_login == false){
      System.out.println("Enter your username: ");
      String username = myScan.nextLine();
      System.out.println("Enter your password: ");
      String pw = myScan.nextLine();

      try(
      Connection con = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241", username, pw);
      Statement s = con.createStatement();
      ){
        System.out.println("Connection successfullly made!");
        valid_login = true;

        List<String> fnames = Arrays.asList(
        "Emma",
        "Olivia",
        "Noah",
        "Liam",
        "Sophia",
        "Mason",
        "Ava",
        "Jacob",
        "William",
        "Isabella",
        "Ethan",
        "Mia",
        "James",
        "Alexander",
        "Michael",
        "Benjamin",
        "Elijah",
        "Daniel",
        "Aiden",
        "Logan",
        "Matthew",
        "Abigail",
        "Lucas",
        "Jackson",
        "Emily",
        "David",
        "Oliver",
        "Jayden",
        "Joseph",
        "Charlotte",
        "Gabriel",
        "Samuel",
        "Carter",
        "Anthony",
        "John",
        "Harper",
        "Dylan",
        "Luke",
        "Henry",
        "Madison",
        "Andrew",
        "Isaac",
        "Amelia",
        "Christopher",
        "Joshua",
        "Elizabeth",
        "Sofia",
        "Wyatt",
        "Sebastian",
        "Owen",
        "Evelyn",
        "Avery",
        "Caleb",
        "Nathan",
        "Ryan",
        "Jack",
        "Hunter",
        "Levi",
        "Christian",
        "TJ",
        "Julian",
        "Landon",
        "Chloe",
        "Ella",
        "Robert",
        "Grace",
        "Jonathan",
        "Victoria",
        "Isaiah",
        "Aubrey",
        "Charles",
        "Thomas",
        "Aaron",
        "Scarlett",
        "Eli",
        "Zoey",
        "Connor",
        "Jeremiah",
        "Cameron",
        "Addison",
        "Lily",
        "Josiah",
        "Lillian",
        "Adrian",
        "Natalie",
        "Hannah",
        "Aria",
        "Layla",
        "Colton",
        "Brooklyn",
        "Jordan",
        "Dario",
        "Nicholas",
        "Robert",
        "Angel",
        "Alexa",
        "Hudson",
        "Zoe",
        "Lincoln"
        );

        List<String> lnames = Arrays.asList("SMITH",
        "JOHNSON",
        "WILLIAMS",
        "BROWN",
        "JONES",
        "GARCIA",
        "MILLER",
        "DAVIS",
        "RODRIGUEZ",
        "MARTINEZ",
        "HERNANDEZ",
        "LOPEZ",
        "GONZALEZ",
        "WILSON",
        "ANDERSON",
        "THOMAS",
        "TAYLOR",
        "MOORE",
        "JACKSON",
        "MARTIN",
        "LEE",
        "PEREZ",
        "THOMPSON",
        "WHITE",
        "HARRIS",
        "SANCHEZ",
        "CLARK",
        "RAMIREZ",
        "LEWIS",
        "ROBINSON",
        "WALKER",
        "YOUNG",
        "ALLEN",
        "KING",
        "WRIGHT",
        "SCOTT",
        "TORRES",
        "NGUYEN",
        "HILL",
        "COVINGTON",
        "GREEN",
        "ADAMS",
        "NELSON",
        "BAKER",
        "HALL",
        "RIVERA",
        "CAMPBELL",
        "TAULANE",
        "EMBIID",
        "SHINA",
        "SARIC",
        "MCCONNELL",
        "SIMMONS",
        "TONEY"
        );

        List<String> streetslist = Arrays.asList("Main", "Broad", "1st", "Second", "Adams", "Lantern", "Phillips", "Toronto", "Aiken", "Timber",
        "Hart", "Bale", "Kane", "Celso", "Kelly", "Mourinho", "Packard", "Selfridge", "Easton", "Elm");


        List<String> cart_types = Arrays.asList("Physical", "Online");
        List<String> payment_types = Arrays.asList("Credit Card", "Debit Card", "Gift Card", "Cash");


        int fsize = fnames.size();
        int lsize = lnames.size();
        int ssize = streetslist.size();
        for(int i=1; i<=100; i++){
          String first_name = fnames.get(myRand.nextInt(fsize)).toUpperCase();
          String last_name = lnames.get(myRand.nextInt(lsize));
          String address = myRand.nextInt(999) +" " + streetslist.get(myRand.nextInt(ssize)) + " St";

          String q = "insert into customer(first_name, last_name, address) values(" + quoteStr(first_name) + "," + quoteStr(last_name) + "," + quoteStr(address) +")";
          try {
            ResultSet result = s.executeQuery(q);
          } catch (Exception e) {System.out.println("Invalid insert. Could not add to the CUSTOMER table");}
        }
        System.out.println("Customer table populated");

        for(int i=1; i<=10; i++){
          int cust_id = myRand.nextInt(100);
          Date myDate = new Date();
          String q = "insert into rewards_account values(" + cust_id + ",1234567890," + quoteStr(myDate.toString()) + ")";
          try {
            ResultSet result = s.executeQuery(q);
          } catch (Exception e) {System.out.println("Invalid insert. Could not add to the REWARDS_ACCOUNT table");}
        }
        System.out.println("Rewards_account table populated");


        for(int i=1; i<=50; i++){
          String type = cart_types.get(myRand.nextInt(cart_types.size()));
          String payment = payment_types.get(myRand.nextInt(payment_types.size()));
          String q = "insert into cart (type, payment_method) values(" + quoteStr(type) +"," + quoteStr(payment) + ")";
          String q2 = "insert into cust_cart values(" + i + "," + i + ")";
          String q3 = "insert into cart_store values(" + i + "," + ((i%6)+1) +")";
          try {
            ResultSet result = s.executeQuery(q);
          } catch (Exception e) {System.out.println("Invalid insert. Could not add to the CART table");}
          try {
            ResultSet result2 = s.executeQuery(q2);
          } catch (Exception e) {System.out.println("Invalid insert. Could not add to the CUST_CART table");}
          try{
            ResultSet result3 = s.executeQuery(q3);
          } catch(Exception e){System.out.println("Invalid insert. Could not add to CART_STORE table\nq3 was " + q3);}
        }
        System.out.println("Cart table populated");
        System.out.println("Cust_cart table populated");




      }catch(Exception e){System.out.println("Invalid login, please try again or press ctrl+c to quit");}
    }


  }//End of main

  public static String quoteStr(String s){
      return "'" + s + "'";
  }

}
