package dao;

import java.sql.*;

public class DataAccessObject {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    ResultSetMetaData metadata = null;
	
	
    // Constructor
    public DataAccessObject() {
		
	}

    // Establish Connection	
	public void connectToDB(String DBName, String user, String password) {
		
		try{  
			String jdbcURL = "jdbc:mysql://localhost:3306/" + DBName + "?useSSL=true&serverTimezone=UTC";
			connect = DriverManager.getConnection(jdbcURL,user, password);
			System.out.println(DBName + " " + user + " " + password);
						
			} catch(Exception e){ 
				System.out.println(e);
				}
		}
	
	// Read from DB
	public void readDataFromDB() {
	
		try {
			statement = connect.createStatement();
			resultSet = statement.executeQuery("select * from artikel");
			metadata = resultSet.getMetaData();
			System.out.println(metadata.toString());
		    int columnCount = metadata.getColumnCount();
		    
		    System.out.println(columnCount);
		    for (int i = 1; i <= columnCount; i++) {
				System.out.print(metadata.getColumnName(i)+"  ");
			}
		    System.out.println("");
			while(resultSet.next()) {
				//System.out.println(resultSet.getString(1)+"  "+resultSet.getString(2)+"  "+resultSet.getString(3));
								
				for (int i = 1;i <= columnCount; i++) {
					System.out.print(resultSet.getString(i)+"  ");
				}
				System.out.println("");
			}
		
			close();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	
	}
	
	
	// write to DB
	public void writeDataToDB() {
		
		
	}
	
	// Close resultSet, statement, connection
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }
		
	
	public static void main(String[] args) {
		//String DBName = "c3rbytes";
		String DBName = "bike";
		String user = "root";
		String password = "root";
		DataAccessObject dao = new DataAccessObject();
		dao.connectToDB(DBName, user, password);
		dao.readDataFromDB();
	}
}


