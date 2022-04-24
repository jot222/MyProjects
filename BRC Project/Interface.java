import java.util.Scanner;

import java.sql.*;
import java.util.Date;


class Interface{
    public static void main(String[] args){
        boolean valid_login = false;
        Scanner myScan = new Scanner(System.in);
        
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
                System.out.println("Connection successfully made");
                valid_login = true;
                System.out.println("--------------------------------------------------------------------");
                System.out.println("Welcome to Big River Crossing!\nIf you are a BRC store manager enter 1");
                System.out.println("If you are an existing customer enter 2\nIf you are a new customer enter 3");
                boolean valid_input = false;
                while(!valid_input){
                    if(myScan.hasNextInt() == false){
                        System.out.println("Please enter a valid integer from 1-3");
                        myScan.next();
                    }
                    else{
                        int in = myScan.nextInt();
                        switch(in){
                            case 1:
                                System.out.println("\n\n\n\n\n--------------------------------------------------------");
                                System.out.println("Store Manager Interface");
                                valid_input = true;
                                managerInterface(s);
                                break;
                            case 2:
                                System.out.println("\n\n\n\n\n--------------------------------------------------------");
                                System.out.println("Customer Login");
                                valid_input = true;
                                customerInterface(s);
                                break;
                            case 3:
                                System.out.println("\n\n\n\n\n--------------------------------------------------------");
                                System.out.println("New Customer Signup");
                                valid_input = true;
                                newCustomerInterface(s);
                                break;
                            default:
                                System.out.println("Please make sure you enter an integer from 1 to 3: ");
                        }
                    }
                }
                try { s.close(); } catch (Exception e) { System.out.println("Error closing statement");}
                try { con.close(); } catch (Exception e) { System.out.println("Error closing connection");}

            }catch(Exception e){System.out.println("Invalid login, please try again or press ctrl+c to quit");}
          }
          myScan.close();
          
    }//End of main

    public static void managerInterface(Statement s){
        Scanner myScan =  new Scanner(System.in);
        int storeNum = 0;
        do{
            System.out.println("Please enter your store number below(Currently from 1 to 6)");
            if(myScan.hasNextInt() == false){
                System.out.println("Please enter a valid integer for the store number");
                myScan.next();
            }
            else{storeNum = myScan.nextInt();}
        }while(storeNum < 1 || storeNum > 6);
        String q = "select location from store where ID = " + storeNum;
        try {
            ResultSet result = s.executeQuery(q);
            result.next();
            System.out.println("You have selected the " + result.getString("Location") + " Store");

            int managerChoice =0;
            while(managerChoice != 9){
                System.out.println("\n\n------------------------------------------------");
                System.out.println("Enter (1) to check inventory\nEnter (2) to order more inventory\nEnter (3) to add new products to the database\nEnter 9 or ctrl-c to quit");
                if(myScan.hasNextInt() == false){
                    System.out.println("Please enter a valid integer");
                    myScan.next();
                }
                else{managerChoice = myScan.nextInt();}
                switch(managerChoice){
                    case 1:
                        int lookup=0;
                        System.out.println("\n\n\n\n\n\n\n INVENTORY LOOKUP");
                        System.out.println("Enter '0' to check inventory of entire store\nEnter product ID to check inventory of a specific product");
                        if(myScan.hasNextInt() == false){
                            System.out.println("Please enter a valid integer");
                            myScan.next();
                        }
                        else{lookup = myScan.nextInt();}
                        if(lookup == 0){
                            q = "select store_id, product.product_id, name, quantity from store_has, product where store_has.product_id = product.product_id and store_id = " +storeNum + " order by quantity desc";
                        }
                        else{
                            q= "select store_id, product.product_id, name, quantity from store_has, product where store_has.product_id = "+ lookup + " and product.product_id = "+lookup +" and store_id = " +storeNum;
                        }
                        try{
                            result = s.executeQuery(q);
                            if(!result.next()){System.out.println("There is no product with that product ID");}
                            else{
                                System.out.println(String.format("%-12s %-12s %-30s %-12s", "Store_ID", "Product_ID", "Name", "Quantity"));
                                do{
                                    System.out.println(String.format("%-12s %-12s %-30s %12s", result.getString("store_id"), result.getString("product_id"), result.getString("name"), result.getString("Quantity")));
                                }while(result.next());
                            }
                        }catch(Exception e){System.out.println("Error Looking Up Inventory");}

                        break;
                    case 2:
                        recommendOrders(s, storeNum);
                        System.out.println("\nORDER INVENTORY\nEnter the product id that you want to order");
                        int pid = 0;
                        int quan = 0;
                        String pname = "";
                        if(myScan.hasNextInt() == false){
                            System.out.println("Please enter a valid integer for product_id");
                            myScan.next();
                        }
                        else{pid = myScan.nextInt();}

                        System.out.println("Enter the quantity that you would like to order");
                        if(myScan.hasNextInt() == false){
                            System.out.println("Please enter a valid integer for quantity");
                            myScan.next();
                        }
                        else{quan = myScan.nextInt();}

                        try{
                            q = "select name from product where product_id = "+pid;
                            result = s.executeQuery(q);
                            if(!result.next()){System.out.println("There is no product with that product ID");}
                            else{
                                pname = result.getString("name");
                            }
                        }catch(Exception e){System.out.println("Error ordering that product");}
                        
                        try{
                            q = "update store_has set quantity = (quantity + "+quan+") where product_id = "+pid+" and store_id = "+storeNum;
                            result = s.executeQuery(q);
                            if(!result.next()){System.out.println("Cannot order more of a product that we don't carry");}
                            else{
                                System.out.println("Successfully ordered " +quan+" units of "+pname);
                            }
                        }catch(Exception e){System.out.println("Error ordering that product");}
                        



                        break;
                    case 3:
                        Scanner scan = new Scanner(System.in);
                        int prodID =0;
                        String product="";
                        try{
                            ResultSet r = s.executeQuery("select max(product_id) from prod_brand");
                            r.next();
                            prodID = r.getInt(1) +1;
                            System.out.println("This new product will have id "+prodID);
                        }catch(Exception e){System.out.println("Error with determining product ID");}

                        System.out.println("\n\n\n\n\nORDER NEW PRODUCTS\n Select the Vendor by entering the name of the vendor from the list below");
                        System.out.println("-Amazon\n-Clorox\n-General Mills\n-Google\n-Johnson and Johnson\n-Kraft\n-Nike\n-Pepsico\n-Proctor and Gamble\n-Samsung\n-Whirlpool");
                        String vendor = scan.nextLine();
                        try{
                            ResultSet rs = s.executeQuery("select * from brand_vendor where vendor ="+quoteStr(vendor));

                            if(!rs.next()){
                                System.out.println("Invalid Vendor. Make sure the vendor is part of the list");
                                break;
                            }
                            else{
                                System.out.println("Please select the brand by entering the name of the brand from the list below");
                                do{
                                    System.out.println("-"+ rs.getString("brand"));
                                }while(rs.next());
                                String brand = scan.nextLine();
                                System.out.println("Enter the name of the new product you wish to order");
                                product = scan.nextLine();
                                System.out.println("What would you like to set the price to?");
                                double price = scan.nextDouble();

                                s.executeQuery("insert into product(name) values(" + quoteStr(product) +")");
                                s.executeQuery("insert into prod_brand values (" + prodID + ","+quoteStr(brand)+")");
                                for(int i=1; i<=6; i++){
                                    try{
                                        s.executeQuery("insert into store_has values("+i+","+prodID+","+price+","+0+")");
                                    }catch(Exception e){System.out.println("Error adding to store_has");}
                                }
                            }
                            
                            System.out.println("Succesfully entered new product: "+ product +" into the database");
                        }catch(Exception e){System.out.println("Error ordering new products. Make sure the brand and vendor exist");}

                        break; 
                    case 9:
                        System.out.println("Logging off, have a great day!");
                        break;
                    default:
                        System.out.println("Invalid input. Enter an integer from 1 to 3");
                }
            }
        } catch (Exception e) {System.out.println("Error entering new product");}

    }//End of managerInterface



    public static void customerInterface(Statement s){
        Scanner myScan = new Scanner(System.in);
        int custID = 0;
        int storeID = 0;
        String fName = " ";
        System.out.println("\n\nEnter your customer_id to continue");
        while(myScan.hasNextInt() == false){
            System.out.println("Please enter a Integer for customer ID");
            myScan.next();
        }
        custID = myScan.nextInt();

        try {
            ResultSet result = s.executeQuery("select * from customer where ID = "+custID);
            if(!result.next()){System.out.println("No customer with that ID. Try signing up");}
            else{
                fName = result.getString("first_name");
                System.out.print("Welcome " + fName + ", What store would you like to shop from?\n");
                ResultSet r = s.executeQuery("select id,location from store");
                System.out.println(String.format("%-12s %-12s", "Store_ID", "Location"));
                r.next();
                do{
                    System.out.println(String.format("%-12s %-12s", r.getInt("ID"), r.getString("location")));
                }while(r.next());

                do{
                    System.out.println("Enter the valid store number you wish to shop from(Currently from 1 to 6)");
                    if(myScan.hasNextInt() == false){
                        System.out.println("Please enter a valid integer for the store number");
                        myScan.next();
                    }
                    else{storeID = myScan.nextInt();}
                }while(storeID < 1 || storeID > 6);


                boolean valid_input = false;
                while(!valid_input){
                    System.out.println("Thanks "+fName + " you have selected store " +storeID + "\nWhat would you like to do?");
                    System.out.println("Enter 1 to view the catalog of this store\nEnter 2 to create a new cart\nEnter 3 to continue with a previous cart\nEnter 4 to quit");
                    
                    if(myScan.hasNextInt() == false){
                        System.out.println("Please enter a valid integer from 1-4");
                        myScan.next();
                    }
                    else{
                        int in = myScan.nextInt();
                        switch(in){
                            case 1:
                                System.out.println("\n\n\n\n\nVIEW CATALOG");
                                try {
                                    r = s.executeQuery("select product.product_id, name, price from store_has, product where store_has.product_id = product.product_id and store_id = " +storeID);
                                    System.out.println(String.format("%-12s %-30s %-12s", "Product_ID", "Name", "Price"));
                                    r.next();
                                    do{
                                        System.out.println(String.format("%-12s %-30s %-12s", r.getString("product_id"), r.getString("Name"), r.getBigDecimal("price")));
                                    }while(r.next());
 
                                } catch (Exception e) {System.out.println("Error displaying catalog");}

                               // valid_input = true;
                                break;
                            case 2:
                                System.out.println("\n\n\n\n\nNEW CART");
                                Scanner scan = new Scanner(System.in);

                                try{
                                    System.out.println("Enter your payment method(Cash,Credit Card, Debit Card, or Gift Card)");//Make sure matches
                                    String payment = scan.nextLine();
                                    String type = "";
                                    if(storeID == 1){
                                        type = "'Online'";
                                    }
                                    else{type = "'Physical'";}

                                    s.executeQuery("insert into cart (type, payment_method) values("+type+","+quoteStr(payment)+")");
                                    ResultSet rs = s.executeQuery("select max(cart_id) from cart");
                                    rs.next();
                                    int cartID = rs.getInt(1);
                                    System.out.println("Your new cart id is "+ cartID);
                                    s.executeQuery("insert into cust_cart values("+custID+","+cartID+")");
                                    s.executeQuery("insert into cart_store values("+cartID+","+storeID+")");

                                    shop(s,storeID,custID,cartID);

                                }catch(Exception e){System.out.println("Error Creating New Cart");}

                                valid_input = true;
                                break;
                             case 3:
                                System.out.println("\n\n\n\n\n CONTINUE SHOPPING\n");
                                ResultSet rs = s.executeQuery("select * from cust_cart where customer_id = "+custID);
                                if(rs.next()){
                                    shop(s,storeID,custID,rs.getInt("cart_id"));
                                    valid_input = true;
                                }
                                else{System.out.println("Could not find a cart for that user. Try creating one");}
                                break;
                            case 4:
                                System.out.println("Thank you for shopping with BRC, "+fName+" have a great day!");
                                valid_input = true;
                                break;
                            default:
                                System.out.println("Invalid input");
                        }
                    }
                }

            }

        } catch (Exception e) {System.out.println("Customer Interface Error");}
    }//End of customer interface/method




    public static void newCustomerInterface(Statement s){
        int c = 0;
        Scanner myScan = new Scanner(System.in);
        System.out.println("Welcome to Big River Crossing Enter your first name to begin the signup process or press ctrl+c to quit");
        String fname = myScan.nextLine();
        System.out.println("Nice to meet you " + fname + " enter your last name to continue");
        String lname = myScan.nextLine();
        System.out.println("Perfect! Now enter your address");
        String address = myScan.nextLine();
        try {
            String q = "insert into customer(first_name, last_name, address) values(" +quoteStr(fname)+","+ quoteStr(lname)+","+quoteStr(address)+")";
            s.executeQuery(q);            
            System.out.println("Welcome to BRC "+fname+" "+lname);
            ResultSet result = s.executeQuery("select id from customer order by id desc");
            result.next();
            int newID = result.getInt("ID");
            System.out.println("Your customer ID is " +newID +"\n Would you like to join our rewards program? Enter 1 for yes and anything other number for no");
            while(myScan.hasNextInt() == false){
                System.out.println("Please enter a valid integer");
                myScan.next();
            }
            c = myScan.nextInt();
            long phone =0;
            switch(c){
                case(1):
                    System.out.println("Thank you for signing up for our rewards program.");
                    while(phone == 0){
                        System.out.println("Please enter your 10 digit phone number below in the form xxxxxxxxxx");
                        phone = myScan.nextLong();   //Add check for this
                    }
                    
                    try{
                        Date myDate = new Date();
                        q = "insert into rewards_account values(" + newID + ","+phone+"," + quoteStr(myDate.toString()) + ")";
                        s.executeQuery(q);
                        System.out.println("Thank you for signing up for our rewards program!");
                    }catch(Exception e){System.out.println("Error. Could not sign up rewards account");}


                default:
                    System.out.println("\n\n\n\n\nRedirecting to the login page\nYour customer id is "+newID);
                    customerInterface(s);
            }
            
        } catch (Exception e) {System.out.println("Error Signing Up New Customer");}

    }//End of new customer method
    


    public static void recommendOrders(Statement s, int storeNum){
        
        try {
            String q = "select store_id, name, product.product_id, quantity from store_has, product where store_id = "+storeNum+" and store_has.product_id = product.product_id order by quantity";
            ResultSet result = s.executeQuery(q);
            System.out.println("\n\n\n\nWe recommend that you order more of the following as they all have 2 or less units in stock");
            while(result.next()){
                if(result.getInt("quantity") < 3){
                    System.out.println("Product Name: "+ result.getString("name") + " (ID: " + result.getString("product_id") +")");
                }
            }
            
        } catch (Exception e) {System.out.println("Error Recommending Orders");}
    }//End of recommendOrders






    public static void shop(Statement s, int storeNum, int custID, int cartID){
        Scanner myScan = new Scanner(System.in);
        System.out.println("\nCART ID: "+cartID);
        System.out.println("To add items to your cart press (1)\n" +
                            "To browse products press (2)\n" + 
                            "To remove a particular item from your cart press (3)\n" +
                            "To empty your cart press (4)\n" + 
                            "To save and quit press (5)\n" +
                            "To check out press (6)\n" + 
                            "To view the items in your cart press (7)"
        );
        int choice = 0;
        if(myScan.hasNextInt() == false){
            System.out.println("Please enter a valid integer");
            myScan.next();
        }
        else{choice = myScan.nextInt();}
        switch(choice){
            case(1):
                int pid;
                int quantity=0;
                int storeQuantity=0;
                System.out.println("\n\nAdd items\n Please enter the productID of a product you wish to add to your cart");
                pid = myScan.nextInt();


                while(storeQuantity <= quantity){

                    try{
                        ResultSet r = s.executeQuery("select quantity from store_has where product_id = "+pid+" and store_id = "+storeNum);
                        if(r.next()){
                            storeQuantity = r.getInt("quantity");
                        }
                        else{
                            System.out.println("Product_ID does not exist");
                            break;
                        }
                    }catch(Exception e){System.out.println("Error Adding to Cart");}



                    System.out.println("Enter the quantity of the item you wish to add( Must be less than store quantity of "+storeQuantity+")");
                    quantity = myScan.nextInt();
                    if(quantity > storeQuantity){
                        System.out.println("Cannot add that many units to your cart. Please try again so that your quantity is less than "+storeQuantity);
                    }
                    else{
                        try{
                            ResultSet result = s.executeQuery("select * from store_has,product where store_id="+storeNum+" and product.product_id="+pid+" and store_has.product_id = product.product_id");
                            result.next();
                            s.executeQuery("insert into cart_has values("+cartID+","+pid+","+result.getDouble("price")+","+quantity+")");
                            s.executeQuery("update store_has set quantity = quantity - "+quantity+" where product_id="+pid+" and store_id="+storeNum);
                            System.out.println("Added "+quantity+" units of product "+pid);
                        }catch(Exception e){System.out.println("Error adding to your cart. Please make sure the product exists and there is sufficient qunatity");}
                    }
                }
                
                shop(s, storeNum, custID, cartID);
                break;
            case(2):
                Scanner scan = new Scanner(System.in);
                System.out.println("\n\n\nTo browse by category press (1)\nTo view entire catalog enter any other integer");
                while(!scan.hasNextInt()) {
                    System.out.println("Please enter an integer");
                    scan.next();
                }
                int c = scan.nextInt();
                if(c==1){
                    Scanner scan2 = new Scanner(System.in);
                    System.out.println("\n\n\nDEPARTMENTS\n-Food\n-Cleaner\n-Clothes\n-Drug\n-Electronics\n-Home goods");
                    System.out.println("Enter the department name you wish to browse in");
                    String dept = scan2.nextLine().toLowerCase();
                    try{
                        ResultSet result = s.executeQuery("select * from type_category where general_category = "+quoteStr(dept));
                        if(!result.next()){
                            System.out.println("Invalid dept name");
                            shop(s, storeNum, custID, cartID);
                            break;
                        }
                        System.out.println("SUBCATEGORIES");
                        while(result.next()){
                            System.out.println("-"+result.getString("specialized_category"));
                        }

                        System.out.println("Enter the name of the subcategory in which you would like to browse");
                        String subCat = scan2.nextLine().toLowerCase();
                        ResultSet rs = s.executeQuery("select product_id, name, prod_type from prod_type,product where prod_id = product_id and prod_type ="+quoteStr(subCat));
                      
                        System.out.println("\nPRODUCTS\n----------------------------------------");
                        System.out.println(String.format("%-12s %-30s %-12s", "Product ID","Name", "Product Type"));
                        while(rs.next()){
                            System.out.println(String.format("%-8s %-30s %-12s", rs.getString("product_id"),rs.getString("name"),rs.getString("prod_type")));
                        }

                    }catch(Exception e){System.out.println("Error Browsing Categories ");}

                }
                else{
                    try {
                        System.out.println("\n\nCurrent Store Inventory\n");
                        ResultSet result = s.executeQuery("select product.product_id, name, price, quantity from store_has, product where store_has.product_id = product.product_id and store_id = "+storeNum);    
     
                        System.out.println(String.format("%-12s %-40s %-30s %-12s", "Product ID", "Name", "Price", "Quantity"));
                        result.next();
                        do{
                            System.out.println(String.format("%-12s %-40s %-30s %12s", result.getString("product_id"), result.getString("name"), result.getString("price"), result.getString("Quantity")));
                        }while(result.next());
     
                    } catch (Exception e) {System.out.println("Error Displaying Inventory");}
                }
                
                shop(s, storeNum, custID, cartID);
                break;
            case(3):
                System.out.println("\n\n\n\nThese are the contents of your cart");
                printCartContents(s, cartID);
                System.out.println("\n\nEnter the ID of the product you wish to delete");
                while(!myScan.hasNextInt()) {
                    System.out.println("Please enter an integer");
                    myScan.next();
                }
                int delID = myScan.nextInt();

                try{
                    s.executeQuery("delete from cart_has where product_id = "+ delID +" and cart_id = "+cartID);
                    System.out.println("Successfully removed all products with product_ID "+ delID);
                }catch(Exception e){System.out.println("CANNOT DELETE AN ITEM THAT IS NOT IN YOUR CART");}
                shop(s,storeNum,custID,cartID);
                break;
            case(4):
                try{
                    s.executeQuery("delete from cart_has where cart_id = "+cartID);
                    System.out.println("\n\n\nYour Cart has been emptied\n\n\n");
                }catch(Exception e){System.out.println("Error emptying cart");}
                shop(s, storeNum, custID, cartID);
                break;
            case(5):
                System.out.println("Save and quit\nThanks for using BRC. Come back soon to finish shopping!");
                break;
            case(6):
                System.out.println("\n\n\nCheckout\n------------------------------------------------------------------------------------------------------");
                printCartContents(s, cartID);
                double total = 0;
                try{
                    ResultSet rs = s.executeQuery("select * from cart_has where cart_id = "+cartID);
                    if(!rs.next()){
                        System.out.println("You have nothing in your cart, have a nice day!");
                    }
                    else{
                        do{
                            total += (rs.getDouble("price") * rs.getInt("cart_quantity"));
                        }while(rs.next());
                        s.executeQuery("delete from cart_has where cart_id = "+cartID);
                        s.executeQuery("delete from cart_store where cart_id = "+cartID);
                        s.executeQuery("delete from cust_cart where cart_id ="+cartID);
                        s.executeQuery("delete from cart where cart_id = "+cartID);
                        System.out.println("Your total is "+total+"\nThank you for shopping with BRC, and have a fantastic day!");
                    }
                }catch(Exception e){System.out.println("Error Checking Out. Please try again later");}

                break;
            case(7):
                System.out.println("\n\n\nYour Cart's Contents");
                printCartContents(s, cartID);
                shop(s,storeNum,custID,cartID);
                break;
            default:
                System.out.println("Please make a valid selection");
                shop(s, storeNum, custID, cartID);
        }
        
    }//End of shop method


    public static void printCartContents(Statement s, int cartID){
                try {
                    ResultSet rs = s.executeQuery("select * from cart_has natural join product where cart_id = "+cartID);
                    System.out.println(String.format("%-12s %-30s %-30s %-30s", "Product ID", "Name", "Price", "Quantity in Cart"));
                    while(rs.next()){
                        System.out.println(String.format("%-12s %-30s %-30s %-30s", rs.getString("product_id"), rs.getString("name"), rs.getString("price"), rs.getString("cart_quantity")));
                    }
                } catch (Exception e) {System.out.println("Error Printing Cart Contents");}
    }

    public static String quoteStr(String s){
        return "'" + s + "'";
    }


}