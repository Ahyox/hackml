/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author Ayodeji
 */
public class DBConnection {
    private Connection connection;
    private MysqlDataSource dataSource;
    private PreparedStatement bookings;
    private PreparedStatement insertStatement;
    private PreparedStatement getCountryId;
    private PreparedStatement getHotelId;
    
    private PreparedStatement userNoOfEntry;
    private PreparedStatement userCancelStatusIsHigh;
    private PreparedStatement hightRateFromLocation;
    private PreparedStatement coperateBooking;
    private PreparedStatement locationBookingHistory;
    private PreparedStatement totalCount;
    private PreparedStatement queryStatement;
    private PreparedStatement countBasedOnLocation;
    private int predict;
    private HashMap<Integer, Integer> locations;
    
    
    
    
    public void connect() {
        dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setDatabaseName("hotels");
        dataSource.setPortNumber(3306);
        dataSource.setUser("root");
        dataSource.setPassword("");
            
        try {
            connection = dataSource.getConnection();
            //Allows you to use auto-commit
            connection.setAutoCommit(false);
            //System.out.println("DataSource: "+dataSource);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        crud();
    }
    
    public void crud() {
        try {
            bookings = connection.prepareStatement("select distinct hotelname, country from test_table");
            insertStatement = connection.prepareStatement("insert into hotels (name, country_id) values (?, ?)");
            //insertStatement = connection.prepareStatement("insert into bookings (time, ip_address, total_price, hotel_id, email_address, checkin, checkout, phone, country_id) "
              //      + "values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            
            getCountryId = connection.prepareStatement("select country_id from countries where name = ?");
            getHotelId = connection.prepareStatement("select id from hotels where name = ?");
            
            userNoOfEntry = connection.prepareStatement("select count(*) from bookings where email_address = ?");
            userCancelStatusIsHigh = connection.prepareStatement("select count(status) from bookings where email_address = ? and status = ?");
            hightRateFromLocation = connection.prepareStatement("select count(country_id) from bookings where email_address = ?");
            coperateBooking = connection.prepareStatement("select is_corporate_booking from bookings where email_address = ?");
            
            locationBookingHistory = connection.prepareStatement("select count(country_id) from bookings where email_address = ?");
            
            totalCount = connection.prepareStatement("select count(*) from bookings");
            
            queryStatement = connection.prepareStatement("select email_address from bookings");
            
            countBasedOnLocation = connection.prepareStatement("select country_id, count(*) c from bookings group by country_id having c > 0");
            
            
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void countBasedOnLocation() {
        ResultSet resultSet = null;
        locations = new HashMap<>();
        try {
            resultSet = countBasedOnLocation.executeQuery();
            while (resultSet.next()) {                
                //Location location = new Location();
                //location.setCountryID(resultSet.getInt("country_id"));
                //location.setCountryID(resultSet.getInt(2));
                System.out.println("Country ID: "+resultSet.getInt("country_id"));
                System.out.println("Count: "+resultSet.getInt(2));
                locations.put(resultSet.getInt("country_id"), resultSet.getInt(2));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public int predict(){
        ResultSet resultSet = null;
        predict = 0;
        try {
            resultSet = queryStatement.executeQuery();
            while (resultSet.next()) {                
                predict = alogorithm(resultSet.getString("email_address"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return predict;
    }
    
    public int getCountryId(String name) {
        ResultSet resultSet = null;
        int count = 0;
        try {
            getCountryId.setString(1, name);
            resultSet = getCountryId.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("country_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return count;
    }
    
    public int locationBookingHistory(String email) {
        ResultSet resultSet = null;
        int count = 0;
        try {
            locationBookingHistory.setString(1, email);
            resultSet = locationBookingHistory.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return count;
    }
    
    public int coperateBooking(String email) {
        ResultSet resultSet = null;
        int status = 0;
        try {
            coperateBooking.setString(1, email);
            resultSet = coperateBooking.executeQuery();
            if (resultSet.next()) {
                status = resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return status;
    }
    
    public int userCancelStatusIsHigh(String email) {
        ResultSet resultSet = null;
        int count = 0;
        try {
            userCancelStatusIsHigh.setString(1, email);
            userCancelStatusIsHigh.setInt(2, 7);
            resultSet = userCancelStatusIsHigh.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return count;
    }
    /**
     * Citeria used for prediction
     * @param email
     * @return total number of high rate from location
     */
    public int hightRateFromLocation(String email) {
        ResultSet resultSet = null;
        int count = 0;
        try {
            hightRateFromLocation.setString(1, email);
            resultSet = hightRateFromLocation.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return count;
    }
    
    /**
     * Determins if the user is a new user or not
     * @param email
     * @return count used to determine number of apperance
     */
    public int userNoOfEntry(String email) {
        ResultSet resultSet = null;
        int count = 0;
        try {
            userNoOfEntry.setString(1, email);
            resultSet = userNoOfEntry.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return count;
    }
    
    
    

    /**
     * This is the prediction algorithm based on certain citerias
     * @param email
     * @return 
     */
    public int alogorithm(String email) {
        int entryCount = userNoOfEntry(email);
        int cancelStatus = userCancelStatusIsHigh(email);
        int highRatePerLocation = hightRateFromLocation(email);
        int isCoperate = coperateBooking(email);
        int locationCount = locationBookingHistory(email);
        
        /**
        System.out.println("Entry Count: "+entryCount);
        System.out.println("Cancel Status: "+cancelStatus);
        System.out.println("Location Count: "+locationCount);
        System.out.println("Coperate: "+isCoperate);
        */
        if ( (entryCount <= 1 && cancelStatus >= 1) || (locationCount >= 1 && entryCount <= 1) || (isCoperate == 1) )  {
            ++predict;
        }
        
        return predict;
    }
    
    /**
     *  else if(locationCount >= 1 && entryCount <= 1) {
            ++predict;
        } else if (isCoperate == 1) {
            ++predict;
        }
     */
    
    /**
     * Gets the total count of data on the database
     * @return the total count
     */
    public int getTotalCount() {
        ResultSet resultSet = null;
        int count = 0;
        try {
            resultSet = totalCount.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return count;
    }
    
    
    
}











