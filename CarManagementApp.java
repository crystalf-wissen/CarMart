import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.sql.*;
import javax.sql.*;
import javax.sql.rowset.*;

class Company {
    public static String readCompany() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Enter Car Company: ");
            try {
                String company = br.readLine();
                Pattern p1 = Pattern.compile("^[A-Za-z\\s-]+$");
                Matcher m1 = p1.matcher(company);
                if (!m1.matches()) {
                    throw new IllegalArgumentException("Invalid Company: Only letters, spaces, and hyphens allowed.");
                }
                return company;
            } catch (IOException e) {
                System.out.println("Error reading input.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

class Model {
    public static String readModel() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Enter your Model: ");
            try {
                String model = br.readLine();
                Pattern p1 = Pattern.compile("^[A-Za-z0-9\\s-]+$");
                Matcher m1 = p1.matcher(model);
                if (!m1.matches()) {
                    throw new IllegalArgumentException("Only letters, numbers, spaces, and hyphens allowed.");
                }
                return model;
            } catch (IOException e) {
                System.out.println(e);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

class Seater {
    public static int readSeat(int min, int max) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Enter Seater between " + min + " and " + max);
            try {
                int seater = Integer.parseInt(br.readLine());
                if (seater < min || seater > max) {
                    throw new IllegalArgumentException("Seater must be between " + min + " and " + max);
                }
                return seater;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number");
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}

class Fuel {
    public static String readFuelType() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Enter Fuel Type Petrol/Diesel/Electric: ");
            try {
                String fuelType = br.readLine().toLowerCase();
                if (!fuelType.equals("petrol") && !fuelType.equals("diesel") && !fuelType.equals("electric")) {
                    throw new IllegalArgumentException("Invalid fuel type! Choose Petrol, Diesel, or Electric.");
                }
                return fuelType;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println("Error reading input.");
            }
        }
    }
}

class Price {
    public static double readPrice() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Enter Car Price: ");
            try {
                double price = Double.parseDouble(br.readLine());
                if (price <= 0) {
                    throw new IllegalArgumentException("Must be positive");
                }
                return price;
            } catch (IOException e) {
                System.out.println("Invalid input");
            } catch (NumberFormatException e){
                System.out.println("Enter a number");
            }
        }
    }
}

abstract class Car {
    private int carID;
    private String company;
    private String type;
    private String model;
    private int seater;
    private String fuelType;
    private double price;
    private boolean sold;

    Car(String type) {
        this.type = type;
        this.company = Company.readCompany();
        this.model = Model.readModel();
        this.seater = Seater.readSeat(2,8);
        this.fuelType = Fuel.readFuelType();
        this.price = Price.readPrice();
        this.sold = false;
    }

    public String getType() {
        return type;
    }
    public String getModel() {
        return model;
    }
    public int getSeater() {
        return seater;
    }
    public String getFuelType() {
        return fuelType;
    }
    public double getPrice() {
        return price;
    }
    public boolean isSold() {
        return sold;
    }
    public String getCompany() {
        return company;
    }

    public String toString() {
        return "CarID: " + carID + ", Company: " + company + ", Type: " + type + ", Model: " + model + ", Seater: " + seater + ", Fuel: " + fuelType + ", Price: " + price + ", Sold: " + sold;
    }

}

class Hatchback extends Car {
    Hatchback() {
        super("Hatchback");
    }
}

class Sedan extends Car {
    Sedan() {
        super("Sedan");
    }
}

class SUV extends Car {
    SUV() {
        super("SUV");
    }
}

interface CarFactory {
    Car createCar();
}

class HatchbackFactory implements CarFactory {
    public Car createCar() {
        return new Hatchback();
    }
}

class SedanFactory implements CarFactory {
    public Car createCar() {
        return new Sedan();
    }
}

class SUVFactory implements CarFactory {
    public Car createCar() {
        return new SUV();
    }
}

class CarFactoryProducer {
    public static CarFactory getFactory(String type) {
        switch (type) {
            case "hatchback":
                return new HatchbackFactory();
            case "sedan":
                return new SedanFactory();
            case "suv":
                return new SUVFactory();
            default:
                throw new IllegalArgumentException("Unknown car type: " + type);
        }
    }
}

class InvalidChoiceException extends Exception {
    public InvalidChoiceException() {
        super("Invalid choice entered!");
    }
    public void displayMessage(int maxChoice) {
        System.out.println("Please enter a number between 1 and " + maxChoice);
    }
}

class Menu {
    private static int maxChoice;

    public static int readChoice(int max) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        maxChoice = max;
        while (true) {
            System.out.print("Enter your choice: ");
            try {
                int choice = Integer.parseInt(br.readLine());
                if (choice < 1 || choice > maxChoice) {
                    throw new InvalidChoiceException();
                }
                return choice;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number only");
            } catch (InvalidChoiceException e) {
                e.displayMessage(maxChoice);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}

interface CarDAO
{
    void createCar(Car car);
    void searchCar(int option, String param);
    void sellCar(int option);
    void updateCar(int eid);
}

 class DatabaseUtil {

    private static Connection con;
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "tiger";

    private DatabaseUtil() {
    }

    public static Connection getConnection() {
        try {
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection(DB_URL, USER, PASS);
                 System.out.println("Database connection created.");
            }
            return con;
        } catch (Exception e) {
            System.out.println("Error getting the connection " + e.getMessage());
            return null;
        }
    }

     public static void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("Database connection closed.");
            }
        } catch (Exception e) {
            System.out.println("Error closing the connection " + e.getMessage());
        } finally {
            con = null;
        }
    }
}

class CarDAOImpl implements CarDAO{

    public CarDAOImpl() {
    }

    public void createCar(Car newCar) {
        String query = "insert into car (company, type, model, seater, fuelType, price, sold) values (?, ?, ?, ?, ?, ?, ?)";
        Connection con = DatabaseUtil.getConnection();
        try (PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, newCar.getCompany());
            pstmt.setString(2, newCar.getType());
            pstmt.setString(3, newCar.getModel());
            pstmt.setInt(4, newCar.getSeater());
            pstmt.setString(5, newCar.getFuelType());
            pstmt.setDouble(6, newCar.getPrice());
            pstmt.setBoolean(7, newCar.isSold());

            pstmt.execute();

            System.out.println("Car Saved Sucessfully!");

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void searchCar(int option, String param) {
        ResultSet rs = null;
        Connection con = DatabaseUtil.getConnection();
        try (PreparedStatement pstmt = null) {
            String query = "";
            switch (option) {
                case 1:
                    query = "select * from car where sold = false";
                    rs = con.prepareStatement(query).executeQuery();
                    displayResultSet(rs);
                    break;
                case 2:
                    query = "select * from car where company ilike ?";
                    try (PreparedStatement pstmtCompany = con.prepareStatement(query)) {
                        pstmtCompany.setString(1, "%" + param + "%"); 
                        rs = pstmtCompany.executeQuery();
                        displayResultSet(rs);
                    }
                    break;
                case 3:
                    query = "select * from car where type ilike ?";
                    try (PreparedStatement pstmtType = con.prepareStatement(query)) {
                        pstmtType.setString(1, "%" + param + "%");
                        rs = pstmtType.executeQuery();
                        displayResultSet(rs);
                    }
                    break;
                case 4:
                    StringTokenizer tokenizer = new StringTokenizer(param, "-");
                    if (tokenizer.countTokens() == 2) {
                        try {
                            double minPrice = Double.parseDouble(tokenizer.nextToken());
                            double maxPrice = Double.parseDouble(tokenizer.nextToken());
                            query = "select * from car where price >= ? and price <= ?";
                            try (PreparedStatement pstmtPrice = con.prepareStatement(query)) {
                                pstmtPrice.setDouble(1, minPrice);
                                pstmtPrice.setDouble(2, maxPrice);
                                rs = pstmtPrice.executeQuery();
                                displayResultSet(rs);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid price range format. Use '17000-21000'");
                        }
                    } else {
                        System.out.println("Invalid price range format. Use '20000-30000'");
                    }
                    break;
                default:
                    System.out.println("Invalid search option.");
                    return;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void displayResultSet(ResultSet rs) throws SQLException {
        if (!rs.isBeforeFirst() ) {
            System.out.println("No cars found");
            return;
        }
        while (rs.next()) {
            System.out.println("-------------------------");
            System.out.println("Car ID: " + rs.getInt("carID"));
            System.out.println("Company: " + rs.getString("company"));
            System.out.println("Type: " + rs.getString("type"));
            System.out.println("Model: " + rs.getString("model"));
            System.out.println("Seater: " + rs.getInt("seater"));
            System.out.println("Fuel Type: " + rs.getString("fuelType"));
            System.out.println("Price: " + rs.getDouble("price"));
            System.out.println("Sold: " + rs.getBoolean("sold"));
            System.out.println("-------------------------");
        }
    }

    public void updateCar(int eid) {
        Connection con = DatabaseUtil.getConnection();
        String query = "select * from car where carID = ?";
        try(PreparedStatement pstmt = con.prepareStatement(query)){
            pstmt.setInt(1, eid);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                System.out.println("Enter the new price: ");
                double newPrice = Price.readPrice();
                String newQuery = "update car set price = ? where carID = ?";
                try(PreparedStatement pstmtNew = con.prepareStatement(newQuery)){
                    pstmtNew.setDouble(1, newPrice);
                    pstmtNew.setInt(2, eid);
                    pstmtNew.execute();
                }
                System.out.println("Car price updated!!! ");
            } else{
                System.out.println("This particular car doesn't exist ");
            }
        } catch(Exception e){
            e.printStackTrace();
        }  
    }

    public void sellCar(int option) {
        Connection con = DatabaseUtil.getConnection();
        ResultSet rs = null;
        try (PreparedStatement pstmt = null) {
            String query = "";
            switch (option) {
                case 1:
                    query = "select * from car where sold = true";
                    rs = con.prepareStatement(query).executeQuery();
                    displayResultSet(rs);
                    break;
                case 2:
                    query = "update car set sold = true where sold = false";
                    con.prepareStatement(query).execute();
                    System.out.println("Sucessfully sold");
                    break;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

public class CarManagementApp {
    public static void main(String[] args) {
        Connection con = DatabaseUtil.getConnection();
        if(con == null) {
           System.out.println("Failed to connect to the database.");
           return;
        }
        CarDAO carDAO = new CarDAOImpl();
        int ch1 = 0, ch2 = 0, ch3 = 0;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        do {
            System.out.println("-------------------------------------");
            System.out.println("1. Add");
            System.out.println("2. Search");
            System.out.println("3. Update");
            System.out.println("4. Sold");
            System.out.println("5. Exit");
            System.out.println("-------------------------------------");
            ch1 = Menu.readChoice(5);

            switch (ch1) {
                case 1:
                    do {
                        System.out.println("---------------------------------------------");
                        System.out.println("1. Create Hatchback");
                        System.out.println("2. Create Sedan");
                        System.out.println("3. Create SUV");
                        System.out.println("4. Back");
                        System.out.println("--------------------------------------------");
                        ch2 = Menu.readChoice(4);

                        try {
                            CarFactory factory = null;
                            Car newCar = null;

                            switch (ch2) {
                                case 1:
                                    factory = CarFactoryProducer.getFactory("hatchback");
                                    break;
                                case 2:
                                    factory = CarFactoryProducer.getFactory("sedan");
                                    break;
                                case 3:
                                    factory = CarFactoryProducer.getFactory("suv");
                                    break;
                                case 4:
                                    continue;
                            }

                            if (factory != null) {
                                newCar = factory.createCar();
                                carDAO.createCar(newCar);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } while (ch2 != 4);
                    break;
                case 2:
                    System.out.println("Search By: ");
                    {
                        do {
                            System.out.println("---------------------------------------------");
                            System.out.println("1. ALL");
                            System.out.println("2. Company");
                            System.out.println("3. Type");
                            System.out.println("4. Price Range (min-max)");
                            System.out.println("5. Back");
                            System.out.println("--------------------------------------------");
                            ch3 = Menu.readChoice(5);
                            String param = "";

                            switch (ch3) {
                                case 1:
                                    try {
                                        carDAO.searchCar(1, param);
                                    } catch (Exception e) {
                                        System.out.println(e);
                                    }
                                    break;
                                case 2:
                                    System.out.println("Enter Company to search: ");
                                    param = Company.readCompany();
                                    try {
                                        carDAO.searchCar(2, param);
                                    } catch (Exception e) {
                                        System.out.println(e);
                                    }
                                    break;
                                case 3:
                                    System.out.println("Enter Car Type (Hatchback, Sedan, SUV) to search: ");
                                    
                                    try {
                                        param = br.readLine();
                                        carDAO.searchCar(3, param);
                                    } catch (Exception e) {
                                        System.out.println(e);
                                    }
                                    break;
                                case 4:
                                    System.out.println("Enter Price Range(20000-30000): ");
                                   
                                    try {
                                        param = br.readLine();
                                        carDAO.searchCar(4, param);
                                    } catch (Exception e) {
                                        System.out.println(e);
                                    }
                                    break;
                            }
                        } while (ch3 != 5);
                    }
                    break;
                case 3:
                    System.out.println("Enter the ID to update: ");
                    try {
                        int cid =  Integer.parseInt(br.readLine());
                        carDAO.updateCar(cid);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    break;
                case 4:
                    {
                        do {
                            System.out.println("---------------------------------------------");
                            System.out.println("1. ALL");
                            System.out.println("2. Update");
                            System.out.println("3. Back");
                            System.out.println("--------------------------------------------");
                            ch3 = Menu.readChoice(3);

                            switch (ch3) {
                                case 1:
                                    try {
                                        carDAO.sellCar(1);
                                    } catch (NumberFormatException e) {
                                        System.out.println(e);
                                    }
                                    break;
                                case 2:
                                    try {
                                        carDAO.sellCar(2);
                                    } catch (NumberFormatException e) {
                                        System.out.println(e);
                                    }
                                    break;
                            }
                        } while (ch3 != 5);
                    }
                    break;
            }
        } while (ch1 != 5);
        DatabaseUtil.closeConnection();
    }
}