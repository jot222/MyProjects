import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.sql.*;
import java.util.Scanner;
import java.util.Date;

class CreateProducts{
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

        List<String> genCategories = Arrays.asList("food", "cleaner", "clothes", "drug", "electronics", "home goods");

        List<String> foodSubCategories = Arrays.asList("meal","drink", "produce", "snack", "ingredient");
        List<String> cleanerSubCategories = Arrays.asList("kitchen", "bathroom cleaner", "general purpose cleaner", "laundry");
        List<String> clothesSubCategories = Arrays.asList("shirt", "pants", "hat", "shoes", "dress", "accessories");
        List<String> drugSubCategories = Arrays.asList("medicine", "supplement", "oral care", "baby care");
        List<String> electronicsSubCategories = Arrays.asList("audio", "video", "computer", "phone", "power");
        List<String> homeSubCategories = Arrays.asList("furniture", "appliance");


        List<String> pepsicoBrands = Arrays.asList("Pepsi","Gatorade","Doritos", "Quaker");
        List<String> generalMillsBrands = Arrays.asList("Yoplait", "Green Giant", "Hamburger Helper", "Cheerios");
        List<String> kraftBrands = Arrays.asList("Oscar-Mayer", "Planters", "Jello", "Kool-Aid");
        List<String> cloroxBrands = Arrays.asList("Clorox", "Glad", "Burts Bees");
        List<String> proctorAndGambleBrands = Arrays.asList("Tide", "Gain", "Pampers", "Mr Clean");
        List<String> nikeBrands = Arrays.asList("Nike", "Jordan", "Converse");
        List<String> johnsonAndJohnsonBrands = Arrays.asList("Tylenol", "Listerine");
        List<String> googleBrands = Arrays.asList("Pixel", "Google Home");
        List<String> amazonBrands = Arrays.asList("Kindle", "Amazon Fire");
        List<String> samsungBrands = Arrays.asList("Samsung");
        List<String> whirlpoolBrands = Arrays.asList("Whirlpool", "KitchenAid");

          




        List<String> vendors = Arrays.asList("Pepsico", "General Mills", "Kraft", "Clorox", "Proctor and Gamble", "Nike", "Johnson and Johnson",
        "Google", "Amazon", "Samsung", "Whirlpool");
        for(String vendor : vendors){
            String q = "insert into vendor values("+quoteStr(vendor)+")";
            s.executeQuery(q);
        }

        try{
        for(String brand : pepsicoBrands){
            String q1 = "insert into brand values(" + quoteStr(brand)+ ")";
            s.executeQuery(q1);
            String q2 = "insert into brand_vendor values("+ quoteStr(brand)+ ",'Pepsico')";
            s.executeQuery(q2);
        }
        }catch(Exception e){e.printStackTrace();}
        for(String brand : generalMillsBrands){
            String q1 = "insert into brand values(" + quoteStr(brand)+ ")";
            s.executeQuery(q1);
            String q2 = "insert into brand_vendor values("+ quoteStr(brand)+ ",'General Mills')";
            s.executeQuery(q2);
        }
        for(String brand : kraftBrands){
            String q1 = "insert into brand values(" + quoteStr(brand)+ ")";
            s.executeQuery(q1);
            String q2 = "insert into brand_vendor values("+ quoteStr(brand)+ ",'Kraft')";
            s.executeQuery(q2);
        }
        for(String brand : cloroxBrands){
            try{
            String q1 = "insert into brand values(" + quoteStr(brand)+ ")";
            s.executeQuery(q1);
            String q2 = "insert into brand_vendor values("+ quoteStr(brand)+ ",'Clorox')";
            s.executeQuery(q2);
            }catch(Exception e){System.out.println("Error with clorox brands");}
        }
        for(String brand : proctorAndGambleBrands){
            String q1 = "insert into brand values(" + quoteStr(brand)+ ")";
            s.executeQuery(q1);
            String q2 = "insert into brand_vendor values("+ quoteStr(brand)+ ",'Proctor and Gamble')";
            s.executeQuery(q2);
        }
        for(String brand : nikeBrands){
            String q1 = "insert into brand values(" + quoteStr(brand)+ ")";
            s.executeQuery(q1);
            String q2 = "insert into brand_vendor values("+ quoteStr(brand)+ ",'Nike')";
            s.executeQuery(q2);
        }
        for(String brand : johnsonAndJohnsonBrands){
            String q1 = "insert into brand values(" + quoteStr(brand)+ ")";
            s.executeQuery(q1);
            String q2 = "insert into brand_vendor values("+ quoteStr(brand)+ ",'Johnson and Johnson')";
            s.executeQuery(q2);
        }
        for(String brand : googleBrands){
            String q1 = "insert into brand values(" + quoteStr(brand)+ ")";
            s.executeQuery(q1);
            String q2 = "insert into brand_vendor values("+ quoteStr(brand)+ ",'Google')";
            s.executeQuery(q2);
        }
        for(String brand : amazonBrands){
            String q1 = "insert into brand values(" + quoteStr(brand)+ ")";
            s.executeQuery(q1);
            String q2 = "insert into brand_vendor values("+ quoteStr(brand)+ ",'Amazon')";
            s.executeQuery(q2);
        }
        for(String brand : samsungBrands){
            String q1 = "insert into brand values(" + quoteStr(brand)+ ")";
            s.executeQuery(q1);
            String q2 = "insert into brand_vendor values("+ quoteStr(brand)+ ",'Samsung')";
            s.executeQuery(q2);
        }
        for(String brand : whirlpoolBrands){
            String q1 = "insert into brand values(" + quoteStr(brand)+ ")";
            s.executeQuery(q1);
            String q2 = "insert into brand_vendor values("+ quoteStr(brand)+ ",'Whirlpool')";
            s.executeQuery(q2);
        }

        for(String category : genCategories){
            String q = "insert into general_category values("+ quoteStr(category) + ")";
            try {
                ResultSet result = s.executeQuery(q);
              } catch (Exception e) {System.out.println("Invalid insert. Could not add to the GENERAL_CATEGORY table");}
        }
        System.out.println("General_Category table populated");

        for(String x : foodSubCategories){
            String q1 = "insert into specialized_type values(" + quoteStr(x) + ")";
            String q2 = "insert into type_category values(" + quoteStr(x) +",'food')";
            s.executeQuery(q1);
            s.executeQuery(q2);
        }
        for(String x : cleanerSubCategories){
            String q1 = "insert into specialized_type values(" + quoteStr(x) + ")";
            String q2 = "insert into type_category values(" + quoteStr(x) +",'cleaner')";
            s.executeQuery(q1);
            s.executeQuery(q2);
        }
        for(String x : clothesSubCategories){
            String q1 = "insert into specialized_type values(" + quoteStr(x) + ")";
            String q2 = "insert into type_category values(" + quoteStr(x) +",'clothes')";
            s.executeQuery(q1);
            s.executeQuery(q2);
        }
        for(String x : drugSubCategories){
            String q1 = "insert into specialized_type values(" + quoteStr(x) + ")";
            String q2 = "insert into type_category values(" + quoteStr(x) +",'drug')";
            s.executeQuery(q1);
            s.executeQuery(q2);
        }
        for(String x : electronicsSubCategories){
            String q1 = "insert into specialized_type values(" + quoteStr(x) + ")";
            String q2 = "insert into type_category values(" + quoteStr(x) +",'electronics')";
            s.executeQuery(q1);
            s.executeQuery(q2);
        }
        for(String x : homeSubCategories){
            String q1 = "insert into specialized_type values(" + quoteStr(x) + ")";
            String q2 = "insert into type_category values(" + quoteStr(x) +",'home goods')";
            s.executeQuery(q1);
            s.executeQuery(q2);
        }
        System.out.println("specialized_type table populated");
        System.out.println("type_category table populated");


        ///////////////////////////////////////////////////
        //Inserting Products
        //////////////////////////////////////////////////
        
        int counter =1;
        try{
        List<String> pepsiProducts = Arrays.asList("20 oz Pepsi Max", "2 ltr Pepsi");
        for(String x : pepsiProducts){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Pepsi'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'drink'" + ")");
            counter++;
        }
        }catch(Exception e){e.printStackTrace();}
        List<String> gatoradeProducts = Arrays.asList("12 pack assorted gatorades", "32 fl oz Cool Rush Gatorade");
        for(String x : gatoradeProducts){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Gatorade'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'drink'"+")");
            counter++;
        }
        List<String> doritosProducts = Arrays.asList("Cool Ranch Doritos", "Nacho Cheese Doritos");
        for(String x : doritosProducts){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Doritos'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'snack'" + ")");
            counter++;
        }
        for(String x : Arrays.asList("Quaker Oatemeal Box", "Quaker Rice Cakes")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Quaker'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'snack'" +")");
            counter++;
        }
        for(String x : Arrays.asList("Yoplait Cherry Yogurt", "Yoplait Vanilla Greek Yogurt")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Yoplait'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'snack'" + ")");
            counter++;
        }
        for(String x : Arrays.asList("Frozen Green Beans","Mixed Vegetables")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Green Giant'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'produce'" + ")");
            counter++;
        }
        for(String x : Arrays.asList("Hamburger Helper Lasagna")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Hamburger Helper'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'meal'" + ")");
            counter++;
        }
        for(String x : Arrays.asList("Cherrios Box","Honey Nut Cheerios Box")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Cheerios'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'snack'" + ")");
            counter++;
        }
        for(String x : Arrays.asList("Amazon Fire Stick","Amazon Fire Tablet")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Amazon Fire'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'video'" + ")");
            counter++;
        }
        for(String x : Arrays.asList("Listerine Original Mouthwash","Listerine Whitening Strips")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Listerine'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'oral care'" + ")");
            counter++;
        }
        for(String x : Arrays.asList("Burts Bees Lip Balm","Burts Bees Skin Mositurizer")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Burts Bees'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'oral care'" + ")");
            counter++;
        }
        for(String x : Arrays.asList("Clorox Bleach","Clorox Fabric Softener")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Clorox'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'laundry'" + ")");
            counter++;
        }
        for(String x : Arrays.asList("Chuck Taylor Shoes Green","Converse All Stars Red")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Converse'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'shoes'" + ")");
            counter++;
        }
        for(String x : Arrays.asList("Gain dryer sheets","Gain Laundry Detergent")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Gain'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'laundry'" + ")");
            counter++;
        }
        for(String x : Arrays.asList("Glad X-Large Trash Bags","Glad Freezer Bags")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Glad'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'kitchen'" + ")");
            counter++;
        }
        for(String x : Arrays.asList("Google Home Dot","Google Home Speaker")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Google Home'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'audio'" + ")");
            counter++;
        }
        for(String x : Arrays.asList("Jello Cups Assorted","Lime Jello Mix", "Cherry Jello Mix")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Jello'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'snack'" + ")");
            counter++;
        }
        for(String x : Arrays.asList("Jordan 1 Low","Jordan 11 Mid")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Jordan'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'shoes'" + ")");
            counter++;
        }
        s.executeQuery("insert into product(name) values('Jordan basketball shorts')");
        s.executeQuery("insert into prod_brand values (" + counter + ","+"'Jordan'"+")");
        s.executeQuery("insert into prod_type values (" + counter + "," + "'pants'" + ")");
        counter++;

        for(String x : Arrays.asList("Jordan 1 Low","Jordan 11 Mid")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Jordan'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'shoes'" + ")");
            counter++;
        }
        for(String x : Arrays.asList("Kindle Original","Kindle Backlight")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Kindle'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'video'" + ")");
            counter++;
        }
        for(String x : Arrays.asList("Large Refrigerator","Dishwasher")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'KitchenAid'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'appliance'" + ")");
            counter++;
        }
        for(String x : Arrays.asList("Kool-Aid Juice Boxes","1 gal Cherry Kool-Aid")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Kool-Aid'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'drink'" + ")");
            counter++;
        }
        for(String x : Arrays.asList("Mr Clean Magic Eraser","Mr Clean Toilet Cleaner")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Mr Clean'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'shoes'" + ")");
            counter++;
        }
        for(String x : Arrays.asList("12 pk Nike Crew Socks","Nike Sweat Bands")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Nike'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'accessories'" + ")");
            counter++;
        }
        s.executeQuery("insert into product(name) values('Nike AirMax 720')");
        s.executeQuery("insert into prod_brand values (" + counter + ","+"'Nike'"+")");
        s.executeQuery("insert into prod_type values (" + counter + "," + "'shoes'" + ")");
        counter++;

        for(String x : Arrays.asList("12 pk Hot Dogs","6 pack Italian Sausages")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Oscar-Mayer'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'meal'" + ")");
            counter++;
        }
        for(String x : Arrays.asList("20 Pack Diapers","40 Pack Pull-Up Diapers")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Pampers'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'baby care'" + ")");
            counter++;
        }
        for(String x : Arrays.asList("Pixel 4 128GB","Pixel 4 64GB")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Pixel'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'phone'" + ")");
            counter++;
        }
        s.executeQuery("insert into product(name) values('Pixel Buds')");
        s.executeQuery("insert into prod_brand values (" + counter + ","+"'Pixel'"+")");
        s.executeQuery("insert into prod_type values (" + counter + "," + "'audio'" + ")");
        counter++;

        for(String x : Arrays.asList("Cocktail Peanuts","Cashews", "Mixed Nuts")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Planters'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'snack'" + ")");
            counter++;
        }
        for(String x : Arrays.asList("Home Speaker","Samsung Wired Earbuds")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Samsung'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'audio'" + ")");
            counter++;
        }
        s.executeQuery("insert into product(name) values('Samsung Charging Bank')");
        s.executeQuery("insert into prod_brand values (" + counter + ","+"'Samsung'"+")");
        s.executeQuery("insert into prod_type values (" + counter + "," + "'power'" + ")");
        counter++;

        for(String x : Arrays.asList("Tide Laundry Pods","Tide Stain Remover")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Tide'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'laundry'" + ")");
            counter++;
        }
        for(String x : Arrays.asList("Tylenol Children","Tylenol PM","Tylenol Extra Strength")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Tylenol'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'medicine'" + ")");
            counter++;
        }
        for(String x : Arrays.asList("Whirlpool Large Dishwasher","Medium Sized Oven")){
            s.executeQuery("insert into product(name) values(" + quoteStr(x) +")");
            s.executeQuery("insert into prod_brand values (" + counter + ","+"'Whirlpool'"+")");
            s.executeQuery("insert into prod_type values (" + counter + "," + "'appliance'" + ")");
            counter++;
        }
        System.out.println("Products Created");


        //Creating stores


        try{
            s.executeQuery("insert into store(location, phone_number, type) values('Online', 2151112222, 'Online')");
            s.executeQuery("insert into store(location, phone_number, type) values('Philadelphia', 2152343333, 'Physical')");
            s.executeQuery("insert into store(location, phone_number, type) values('St Louis', 2158675309, 'Physical')");
            s.executeQuery("insert into store(location, phone_number, type) values('Chicago', 2150103302, 'Physical')");
            s.executeQuery("insert into store(location, phone_number, type) values('Cleveland', 2153452209, 'Physical')");
            s.executeQuery("insert into store(location, phone_number, type) values('Tampa', 2153334444, 'Physical')");
            System.out.println("Store table populated");

            ResultSet result = s.executeQuery("select count(*) from store");
            result.next();
            int storeCount = result.getInt(1);
            result = s.executeQuery("select count(*) from product");
            result.next();
            int prodCount = result.getInt(1);
            Random rand = new Random();

            for(int i=1; i <= storeCount; i++){
                for(int prodID =1; prodID<=prodCount; prodID++){

                    double price = rand.nextInt(10000) /100.0;
                    s.executeQuery("Insert into store_has values("+i+","+prodID+","+price+","+ rand.nextInt(45)+")");
                }

            }


        }catch(Exception e){e.printStackTrace();}



        
      }catch(Exception e){System.out.println("Invalid login, please try again or press ctrl+c to quit");}
    } 
  }//End of main


  public static String quoteStr(String s){
      return "'" + s + "'";
  }

  

}//End of class