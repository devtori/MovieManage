//////////////////////////////////////////////////////////////////////////////////
// Written by Team14 in Database class (20471-01)
//
// - 1315003 ChaeWon Kang
// - 1315034 SeoYoung Sohn
// - 1315068 HeeJeong Cho
//
//////////////////////////////////////////////////////////////////////////////////



//importing libraries needed 
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.*;
import javax.swing.table.*;



//Main Function
public class team14 {

	
//////////////////////////////////// frame 1 ///////////////////////////////////
//Written by. "1315034 SeoYoung Sohn"                                       
//
//Designing the first Screen of <Movie management system>					
//		
/////////////////////////////////////////////////////////////////////////////////
	
    public static void main (String [] args) {
          test_Frame_team14 tf = new test_Frame_team14();		//first frame

           tf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// program exit when window closed
           tf.setSize(500, 400);	// frame size set
           tf.setLocationRelativeTo(null);
           tf.setVisible(true);		// make frame visible
          }
    }




class test_Frame_team14 extends JFrame implements ActionListener{

	
//////////////////////////////// JDBC and sql queries //////////////////////////////////
//Written by. "1315068 HeeJeong Cho"                                   
//
//Connecting JDBC, and methods needed to import datas from database 				
//
/////////////////////////////////////////////////////////////////////////////////
	
	// stating information that are needed to connect to database server
	public static String driverName = "com.mysql.jdbc.Driver"; 
	public static String dbURL = "jdbc:mysql://localhost:3306/team14";
	public static String dbID = "team14";
	public static String dbPW = "team14";
	
	//method needed for importing movie data from data table DBCOURSE_Movies
	public static void SHOW_MOVIE(String str1, JTextArea text){ 
		try{
			String SQL = "SELECT * FROM DBCOURSE_Movies WHERE MovieName = '"+str1+"';";
			Class.forName(driverName);
			Connection con = DriverManager.getConnection(dbURL,dbID,dbPW);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			while(rs.next()){
				// get the needed sources by executing the sql Query
	            String MovieName = rs.getString("MovieName");
	            String Genre = rs.getString("Genre");
	            String ReleaseDate = rs.getString("ReleaseDate");
	            float Evaluation = rs.getFloat("Evaluation");
	            int Age = rs.getInt("Age");
	            String Goods = rs.getString("Goods");
	            int RunningTime = rs.getInt("RunningTime");
	            String MainActor = rs.getString("MainActor");
	            String Director = rs.getString("Director");
	            
	            // append the text to the given JTextArea to show the information
	            text.append(" <<<<<<<<<< "+MovieName+" >>>>>>>>>");
	            text.append("\n Release date : "+ReleaseDate);
	            text.append("\n\n Main actor : "+MainActor);
	            text.append("\n Director : "+Director);
	            text.append("\n\n Genre : "+Genre);
	            text.append("\n Running time : "+RunningTime);
	            text.append("\n Age : "+Age);
	            text.append("\n\n Evaluation : "+Evaluation);
	         }
			con.close();
	    }
		catch(Exception e1){
			e1.printStackTrace();
		}
	}
	
	// method needed for including items in to JComboBox
	public static void ITEM1_INCLUDE(JComboBox f1, JComboBox f2, JComboBox f3){
		try{
			//individually select items because of DISTINCT
			String SQL1 = "SELECT TheaterName FROM DBCOURSE_Theaters;";
			String SQL2 = "SELECT DISTINCT Genre as UniqueGenre FROM DBCOURSE_Movies;";
			String SQL3 = "SELECT DISTINCT Country as UniqueCountry FROM DBCOURSE_Movies;";
			Class.forName(driverName);
			Connection con = DriverManager.getConnection(dbURL,dbID,dbPW);
			Statement stmt = con.createStatement();
			
			//add the data from database to the given JComboBox
			ResultSet rs1 = stmt.executeQuery(SQL1);
			while(rs1.next()){
	            String TheaterName = rs1.getString("TheaterName");
	            f1.addItem(TheaterName);
	         }
			ResultSet rs2 = stmt.executeQuery(SQL2);
			while(rs2.next()){
	            String Genre = rs2.getString("UniqueGenre");
	            f2.addItem(Genre);
	         }
			ResultSet rs3 = stmt.executeQuery(SQL3);
			while(rs3.next()){
	            String Country = rs3.getString("UniqueCountry");
	            f3.addItem(Country);
	         }
			con.close();
	    }
		catch(Exception e1){
			e1.printStackTrace();
		}	
		
	}
	
	//method needed to find a movie 
	public static void MOVIE_SEARCH(String str1, String str2, String str3, JTextArea text){
		try{
			//using nested queries from created views to find the right movie
			String SQL = "SELECT MovieName, ScreenNum, StartTime, LeftSeat"+
					" FROM DBCOURSE_Screens as S, (SELECT MovieName as SelectMovie FROM DBCOURSE_GENRECOUNTRY WHERE Genre ='"+str2+"' and Country = '"+str3+"') as A"+ 
					" WHERE S.TheaterName = '"+str1+"' and S.MovieName = A.SelectMovie;";
		
			Class.forName(driverName);
			Connection con = DriverManager.getConnection(dbURL,dbID,dbPW);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			while(rs.next()){
				String MovieName = rs.getString("MovieName");
				String ScreenNum = rs.getString("ScreenNum");
				String StartTime = rs.getString("StartTime");
				String LeftSeat = rs.getString("LeftSeat");
				text.append(" <<<<<<<<<< "+ MovieName+" >>>>>>>>>> \n");
				text.append(" Theater : "+str1+" / Screen # : "+ScreenNum+"\n");
				text.append(" StartTime : "+StartTime+"\n");
				text.append(" LeftSeat : "+LeftSeat+"\n\n");
				}
				con.close();
			}
		
		catch(Exception e1){
			e1.printStackTrace();
			}	
		}
	
	//method needed BEFORE inserting movie information - because Actors & Directors are parent keys
	
	public static void INSERT_PERSON(String str1, String str2, String str3, String str4, String str5, String str6, String str7){
		try{
			String SQL1 = "insert into DBCOURSE_Actors values ('"+str1+"', '"+str2+"', '"+str3+"', '"+str4+"');";
			String SQL2 = "insert into DBCOURSE_Directors values ('"+str5+"', '"+str6+"', '"+str7+"');";

			Class.forName(driverName);
			Connection con = DriverManager.getConnection(dbURL,dbID,dbPW);
			Statement stmt = con.createStatement();
			stmt.executeUpdate(SQL1);
			stmt.executeUpdate(SQL2);
			con.close();
			}
		
		catch(Exception e1){
			e1.printStackTrace();
			}
	}
	
	//method need to insert movie information - make sure it is executed after INSERT_PERSON
	
	public static void INSERT_MOVIE(String str1, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10){
		try{
			String SQL = "insert into DBCOURSE_Movies values ('"+str1+"','"+str2+"','"+str3+"','"+str4+"',"+Float.valueOf(str5)+","+Integer.valueOf(str6)+",'"+str7+"',"+Integer.valueOf(str8)+",'"+str9+"','"+str10+"');";
		
			Class.forName(driverName);
			Connection con = DriverManager.getConnection(dbURL,dbID,dbPW);
			Statement stmt = con.createStatement();
			stmt.executeUpdate(SQL);
			con.close();
			}
		
		catch(Exception e1){
			e1.printStackTrace();
			}	
	}
	
	//method need for deleting movie information

	public static void MOVIE_DELETE(String MovieName,String sql){
		try{
			String SQL0 = "START TRANSACTION;"; // In case of canceling the deletion, start transaction
			String SQL1 = "DELETE FROM DBCOURSE_Goods Where RelatedMovie = '"+ MovieName +"';";
			String SQL2 = "DELETE FROM DBCOURSE_Ticket Where MovieName = '"+ MovieName +"';";
			String SQL3 = "DELETE FROM DBCOURSE_Screens Where MovieName = '"+ MovieName +"';";
			String SQL4 = "DELETE FROM DBCOURSE_Movies Where MovieName = '"+ MovieName +"';";

			Class.forName(driverName);
			Connection con  = DriverManager.getConnection(dbURL,dbID,dbPW);
			Statement stmt = con.createStatement();
			stmt.executeUpdate(SQL0);
			stmt.executeUpdate(SQL1);
			stmt.executeUpdate(SQL2);
			stmt.executeUpdate(SQL3);
			stmt.executeUpdate(SQL4);
			stmt.executeUpdate(sql); //if sql = "COMMIT;", delete. if sql = "ROLLBACK;", cancel deletion
			con.close();
	    }
		catch(Exception e1){
			e1.printStackTrace();
		}
	}
	
	//method needed to search tickets from data table DBCOURSE_TicketInfo
	
	public static void TICKET_SEARCH(String CP, int PW, JTextArea text){
		String Name = null;
		String ReservedNum = null;
		String MovieName = null;
		String TheaterName = null;
		String ScreenNum = null;
		String StartTime = null;
		
		try{
			String SQL = " SELECT * FROM DBCOURSE_TicketInfo "+
			         " WHERE PW = " + PW + " and CP = '" + CP + " ';";

			Class.forName(driverName);
			Connection con  = DriverManager.getConnection(dbURL,dbID,dbPW);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			
			while(rs.next()){
	            Name = rs.getString("Name");
	            ReservedNum = rs.getString("ReservedNum");
	            MovieName = rs.getString("MovieName");
	            TheaterName = rs.getString("TheaterName");
	            ScreenNum = Integer.toString(rs.getInt("ScreenNum"));
	            StartTime = rs.getString("StartTime");
	         }
			text.append(" ===== Checked Reservation ===== ");
			text.append("\n\n Customer name : " + Name);
			text.append("\n Reserved number : " + ReservedNum);
			text.append("\n\n Movie name : " + MovieName);
			text.append("\n Theater name : " + TheaterName);
			text.append("\n Screen number : " + ScreenNum);
			text.append("\n Start time : " + StartTime);
			
			con.close();
	    }
		catch(Exception e1){
			e1.printStackTrace();
		}	
	}
	
	//method made to add items into the given JComboBoxes
	
	public static void ITEM5_INCLUDE(JComboBox f1, JComboBox f2, JComboBox f3, JComboBox f4){
		try{
			String SQL1 = "SELECT MovieName FROM DBCOURSE_Movies;";
			String SQL2 = "SELECT DISTINCT Genre FROM DBCOURSE_Movies;";
			String SQL3 = "SELECT DISTINCT Age FROM DBCOURSE_Movies;";
			String SQL4 = "SELECT DISTINCT Goods FROM DBCOURSE_Movies;";
			//individually select items because of DISTINCT
			
			Class.forName(driverName);
			Connection con = DriverManager.getConnection(dbURL,dbID,dbPW);
			Statement stmt = con.createStatement();
			ResultSet rs1 = stmt.executeQuery(SQL1);
			while(rs1.next()){
	            String MovieName = rs1.getString("MovieName");
	            f1.addItem(MovieName);
	         }
			ResultSet rs2 = stmt.executeQuery(SQL2);
			while(rs2.next()){
				String Genre = rs2.getString("Genre");
	            f2.addItem(Genre);
	         }
			ResultSet rs3 = stmt.executeQuery(SQL3);
			while(rs3.next()){
				String Age = rs3.getString("Age");
	            f3.addItem(Age);
	         }
			ResultSet rs4 = stmt.executeQuery(SQL4);
			while(rs4.next()){
				String Goods = rs4.getString("Goods");
	            f4.addItem(Goods);
	         }
			con.close();
	    }
		catch(Exception e1){
			e1.printStackTrace();
		}	
	}
	
	//method needed to show text message for updating
	
	public static void UPDATE_TEXT(String str1, String str2, String str3, int str4, String str5, int str6, JTextArea text){
		String Genre = null;
        String Country = null;
        int Age = 0;
        String Goods = null;
        int RunningTime = 0;
        
		try{
			String SQL = "SELECT Genre, Country, Age, Goods, RunningTime FROM DBCOURSE_Movies WHERE MovieName = '"+str1+"';";

			Class.forName(driverName);
			Connection con  = DriverManager.getConnection(dbURL,dbID,dbPW);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			while(rs.next()){
				Genre = rs.getString("Genre");
	            Country = rs.getString("Country");
	            Age = rs.getInt("Age");
	            Goods = rs.getString("Goods");
	            RunningTime = rs.getInt("RunningTime");
	         }
			text.append("\n Changing Genre");
			text.append("\n "+Genre+" >>>>>>>>>> "+str2);
			text.append("\n\n Changing Country");
			text.append("\n "+Country+" >>>>>>>>>> "+str3);
			text.append("\n\n Changing Age");
			text.append("\n "+Age+" >>>>>>>>>> "+str4);
			text.append("\n\n Changing Goods");
			text.append("\n "+Goods+" >>>>>>>>>> "+str5);
			text.append("\n\n Changing Run time");
			text.append("\n "+RunningTime+" >>>>>>>>>> "+str6);

			con.close();
	    }
		catch(Exception e1){
			e1.printStackTrace();
		}
	}
	
	//method needed for actually updating(changing) the datas in data table DBCOURSE_Movies
	
	public static void UPDATE_RESERVATION(String str1, String str2, String str3, int str4, String str5, int str6, String sql){
		try{
			String SQL0 = "START TRANSACTION;"; // In case of canceling the update, start transaction
			String SQL1 = "UPDATE DBCOURSE_Movies "+ 
					" SET Genre = '"+str2+"', Country = '"+str3+"', Age = "+str4+", Goods = '"+str5+"', RunningTime = "+str6+
					" WHERE MovieName = '"+str1+"';";
			
			Class.forName(driverName);
			Connection con  = DriverManager.getConnection(dbURL,dbID,dbPW);
			Statement stmt = con.createStatement();
			stmt.executeUpdate(SQL0);
			stmt.executeUpdate(SQL1);
			stmt.executeUpdate(sql); //if sql = "COMMIT;", delete. if sql = "ROLLBACK;", cancel update
			con.close(); 
	    }
		catch(Exception e1){
			e1.printStackTrace();
		}
	}
	
	// method made to add items into the given JComboBoxes

	public static void ITEM6_INCLUDE(JComboBox f1, JComboBox f2, JComboBox f3, JComboBox f4){
		try{
			String SQL1 = "SELECT DISTINCT Gender FROM DBCOURSE_Actors;";
			String SQL2 = "SELECT DISTINCT Country FROM DBCOURSE_Actors;";
			String SQL3 = "SELECT DISTINCT Gender FROM DBCOURSE_Directors;";
			String SQL4 = "SELECT DISTINCT Country FROM DBCOURSE_Directors;";
			//individually select items because of DISTINCT
			
			Class.forName(driverName);
			Connection con = DriverManager.getConnection(dbURL,dbID,dbPW);
			Statement stmt = con.createStatement();
			ResultSet rs1 = stmt.executeQuery(SQL1);
			while(rs1.next()){
	            String AGender = rs1.getString("Gender");
	            f1.addItem(AGender);
	         }
			ResultSet rs2 = stmt.executeQuery(SQL2);
			while(rs2.next()){
				String ACountry = rs2.getString("Country");
	            f2.addItem(ACountry);
	         }
			ResultSet rs3 = stmt.executeQuery(SQL3);
			while(rs3.next()){
				String DGender = rs3.getString("Gender");
	            f3.addItem(DGender);
	         }
			ResultSet rs4 = stmt.executeQuery(SQL4);
			while(rs4.next()){
				String DCountry = rs4.getString("Country");
	            f4.addItem(DCountry);
	         }
			con.close();
	    }
		catch(Exception e1){
			e1.printStackTrace();
		}	
	}
	
	//method needed for finding information from data table DBCOURSE_Actors
	
	public static void ACTOR_SEARCH(String str1, String str2, JTextArea text){
		try{
			String SQL = "SELECT Name FROM DBCOURSE_Actors WHERE Gender = '"+str1+"' and Country = '"+str2+"'";
			Class.forName(driverName);
			Connection con = DriverManager.getConnection(dbURL,dbID,dbPW);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			while(rs.next()){
	            String AName = rs.getString("Name");
	            text.append(AName+"\n\n");
	         }
			con.close();
	    }
		catch(Exception e1){
			e1.printStackTrace();
		}	
		
	}
	
	//method needed for finding information from data table DBCOURSE_Directors
	
	public static void DIRECTOR_SEARCH(String str1, String str2, JTextArea text){
		try{
			String SQL = "SELECT Name FROM DBCOURSE_Directors WHERE Gender = '"+str1+"' and Country = '"+str2+"'";
			Class.forName(driverName);
			Connection con = DriverManager.getConnection(dbURL,dbID,dbPW);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			while(rs.next()){
	            String AName = rs.getString("Name");
	            text.append(AName+"\n\n");
	         }
			con.close();
	    }
		catch(Exception e1){
			e1.printStackTrace();
		}	
		
	}
	
	// method needed for including items in to JComboBox
	
	public static void THEATER_INCLUDE(JComboBox SelectionField){
		try{
			String SQL = "SELECT TheaterName FROM DBCOURSE_Theaters";

			Class.forName(driverName);
			Connection con = DriverManager.getConnection(dbURL,"root","gmrxhrl18");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			
			while(rs.next()){
	            String TheaterName = rs.getString("TheaterName");
	            SelectionField.addItem(TheaterName);
	         }
			con.close();
	    }
		catch(Exception e1){
			e1.printStackTrace();
		}	
  }
	
	// method needed to select theater information from data table DBCOURSE_Theaters
	
	public static void SHOW_THEATER(String Theater, JTextArea text){
		String TheaterName = null;
		String ScreenNum = null;
		String ParkingLot = null;
		String QuickTicketSys = null;
		String PhotoTickets = null;
		String Discount = null;
		String Snackbar = null;
		String Gamecenter = null;
		
		try{
			String SQL = " SELECT * FROM DBCOURSE_Theaters "+
			"WHERE TheaterName = '"+ Theater + "'";

			Class.forName(driverName);
			Connection con  = DriverManager.getConnection(dbURL,dbID,dbPW);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			
			while(rs.next()){
	            TheaterName = rs.getString("TheaterName");
	            ScreenNum = rs.getString("Screen");
	            ParkingLot = rs.getString("ParkingLot");
	            QuickTicketSys = rs.getString("QuickTicketSystem");
	            PhotoTickets = rs.getString("PhotoTickets");
	            Discount = rs.getString("DiscountInfo");
	            Snackbar = rs.getString("SnackBar");
	            Gamecenter = rs.getString("GameCenter");
	            
	            text.append("\n <<<<<<<<<<<<<<< "+TheaterName+" >>>>>>>>>>>>>>> ");
	            text.append("\n\n\n \bNumbers of screens\b : "+ScreenNum);
	            text.append("\n\n Number of parking lot : "+ScreenNum);
	            text.append("\n\n Number of quick ticket machine : "+QuickTicketSys);
	            text.append("\n Photo ticket availability : "+ PhotoTickets);
	            text.append("\n\n Discount info : "+Discount);
	            text.append("\n\n Snack bar existence : "+Snackbar);
	            text.append("\n Game center existence: "+Gamecenter);
	            
	         }
			
			con.close();
	    }
		catch(Exception e1){
			e1.printStackTrace();
		}
	}
	
	// method needed to show movies in the given theater

	public static void SHOW_MOVIE_IN_THEATER(String Theater, JTextArea text){
		try{
			String SQL = " SELECT * FROM DBCOURSE_Screens "+
					"WHERE TheaterName = '"+ Theater + "'";

			Class.forName(driverName);
			Connection con = DriverManager.getConnection(dbURL,"root","gmrxhrl18");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			
			while(rs.next()){
	            String MovieName = rs.getString("MovieName");
	            text.append(MovieName+"\n\n");
	         }
			con.close();
	    }
		catch(Exception e1){
			e1.printStackTrace();
		}	
	}
	
	// method needed to import information in to the given table model
	public static void ADD_SNACKS(DefaultTableModel tm){
		try{
			String SQL = " SELECT * " +
			" FROM DBCOURSE_Snacks";

			Class.forName(driverName);
			Connection con = DriverManager.getConnection(dbURL,dbID,dbPW);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
        	
        	String row[] = new String[3];
        	
			while(rs.next()){
				 row[0] = rs.getString("SnackName");
		         row[1] = Integer.toString(rs.getInt("Price"));
		         row[2] = rs.getString("SnackType");
		         tm.addRow(row);
		         }

			con.close();
	    }
		catch(Exception e1){
			e1.printStackTrace();
		}
	}
	
	// method needed to import information in to the given table model
	
	public static void ADD_GAMES(DefaultTableModel tm){
		try{
			String SQL = " SELECT * " +
			" FROM DBCOURSE_Games";

			Class.forName(driverName);
			Connection con = DriverManager.getConnection(dbURL,dbID,dbPW);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
        	
        	String row[] = new String[4];
        	
			while(rs.next()){
				 row[0] = rs.getString("GameName");
		         row[1] = Integer.toString(rs.getInt("Price"));
		         row[2] = rs.getString("Age");
		         row[3] = rs.getString("InspectionDate");
		         tm.addRow(row);
		         }

			con.close();
	    }
		catch(Exception e1){
			e1.printStackTrace();
		}
	}
	
	
   
	
	
	
////////////////////////////////////////// Main frame & button ///////////////////////////////////////////   
// Written by. "1315034 SeoYoung Sohn"										
// 													
///////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//Describing the name of the Button
   JButton jButton1 = new JButton("For User");
   JButton jButton2 = new JButton("For Admin");
   

   //when clicking the button, a new frame pops up
    test_Frame2_team14 tf2;
    public test_Frame_team14(){
    	  //Insert Image into Buttons
          jButton1 = new JButton(new ImageIcon("user.png"));
          jButton2 = new JButton(new ImageIcon("admin.png"));
          
            this.setLayout(new GridLayout(2,2)); 
            //GridLayout manage the basic arrangement of the screen
            getContentPane().add(jButton1);
            getContentPane().add(jButton2);
            //Add Buttons to the Screen
            
            this.setSize(100,30);
            this.setVisible(true);
            //Setting Size and Providing Visibility
            
            jButton1.addActionListener(this);
            jButton2.addActionListener(this);   
            
    }
    
    public static JFrame frame = new JFrame("Management");
    public static JFrame frame2 = new JFrame("Find Your Movie");
    public static String buttonNum = null;
   
    
    @Override
    public void actionPerformed(ActionEvent arg0) {
            if(arg0.getSource() == jButton1){
            	
////////////////////////////////////////// Tabs ///////////////////////////////////////////////////////   
// Written by Written by. "1315068 HeeJeong Cho"												
//																 					    									
////////////////////////////////////////////////////////////////////////////////////////////////
            	
            	JTabbedPane  jtab = new JTabbedPane();
        		
            	// adding new tabs in frame 1
                JPanel jp_1 = new JPanel();
                JPanel jp_2 = new JPanel();
                JPanel jp_3 = new JPanel();
                JPanel jp_4 = new JPanel();
                
        		jtab.add("MOVIES", jp_1);
        		jtab.add("THEATERS", jp_2);
        		jtab.add("SNACKS", jp_3);
        		jtab.add("GAMES", jp_4);
        		
        		frame2.add("North", jtab);
        		
///////////////////////////////////////////// tab 1/////////////////////////////////////////////////////   
// first Written by. "1315003 ChaeWon Kang"
// modified by."1315068 HeeJeong Cho"
//																							
// * "1315034 SeoYoung Sohn" designed GOODS Screen and every functions on the GOODS Screen					    
//                                           														
/////////////////////////////////////////////////////////////////////////////////////////////////////
        		
        		JPanel p01 = new JPanel();
        		p01.setLayout(new BorderLayout());
        		
        		JPanel p011 = new JPanel();
        		p011.setLayout(new GridLayout(8,2));
        		
        		// setting buttons
                JButton button011 = new JButton("Beauty and the Beast");
                JButton button012 = new JButton("Doctor Strange"); 
                JButton button013 = new JButton("Fantastic Beasts and Where to Find Them"); 
                JButton button014 = new JButton("Fences"); 
                JButton button015 = new JButton("Fifty Shades Darker"); 
                JButton button016 = new JButton("Get Out"); 
                JButton button017 = new JButton("Guardians of the Galaxy Vol.2"); 
                JButton button018 = new JButton("Hidden Figures"); 
                JButton button019 = new JButton("La La Land"); 
                JButton button0110 = new JButton("Logan"); 
                JButton button0111 = new JButton("Miss Peregrine's Home For Peculiar Children"); 
                JButton button0112 = new JButton("Moana"); 
                JButton button0113 = new JButton("Monster Trucks"); 
                JButton button0114 = new JButton("The Boss Baby"); 
                JButton button0115 = new JButton("The Promise"); 
                JButton button0116 = new JButton("The Zookeeper's Wife");  
                
                p011.add(button011);
                p011.add(button012);
                p011.add(button013);
                p011.add(button014);
                p011.add(button015);
                p011.add(button016);
                p011.add(button017);
                p011.add(button018);
                p011.add(button019);
                p011.add(button0110);
                p011.add(button0111);
                p011.add(button0112);
                p011.add(button0113);
                p011.add(button0114);
                p011.add(button0115);
                p011.add(button0116);
                
                p01.add(p011, BorderLayout.NORTH);
                
                JPanel p012 = new JPanel();
                JTextArea mArea = new JTextArea(12,50);
                p012.add(mArea);
                p01.add(p012, BorderLayout.CENTER);
                
                jp_1.add(p01);
                
                //when clicking on the buttons, the static void string buttonNum changes
                button011.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		mArea.setText(null);
                		buttonNum = "Beauty and the Beast" ;
                		SHOW_MOVIE(buttonNum, mArea);
                		}
                	}); 
                button012.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		mArea.setText(null);
                		buttonNum = "Doctor Strange" ;
                		SHOW_MOVIE(buttonNum, mArea);
                		}
                	});
                button013.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		mArea.setText(null);
                		buttonNum = "Fantastic Beasts and Where to Find Them" ;
                		SHOW_MOVIE(buttonNum, mArea);
                		}
                	});
                button014.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		mArea.setText(null);
                		buttonNum = "Fences" ;
                		SHOW_MOVIE(buttonNum, mArea);
                		}
                	});
                button015.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		mArea.setText(null);
                		buttonNum = "Fifty Shades Darker" ;
                		SHOW_MOVIE(buttonNum, mArea);
                		}
                	});
                button016.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		mArea.setText(null);
                		buttonNum = "Get Out" ;
                		SHOW_MOVIE(buttonNum, mArea);
                		}
                	});
                button017.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		mArea.setText(null);
                		buttonNum = "Guardians of the Galaxy Vol.2" ;
                		SHOW_MOVIE(buttonNum, mArea);
                		}
                	});
                button018.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		mArea.setText(null);
                		buttonNum = "Hidden Figures" ;
                		SHOW_MOVIE(buttonNum, mArea);
                		}
                	});
                button019.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		mArea.setText(null);
                		buttonNum = "La La Land" ;
                		SHOW_MOVIE(buttonNum, mArea);
                		}
                	});
                button0110.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		mArea.setText(null);
                		buttonNum = "Logan" ;
                		SHOW_MOVIE(buttonNum, mArea);
                		}
                	});
                button0111.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		mArea.setText(null);
                		buttonNum = "Miss Peregrine\\'s Home For Peculiar Children";
                		SHOW_MOVIE(buttonNum, mArea);
                		}
                	});
                button0112.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		mArea.setText(null);
                		buttonNum = "Moana" ;
                		SHOW_MOVIE(buttonNum, mArea);
                		}
                	});
                button0113.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		mArea.setText(null);
                		buttonNum = "Monster Trucks";
                		SHOW_MOVIE(buttonNum, mArea);
                		}
                	});
                button0114.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		mArea.setText(null);
                		buttonNum = "The Boss Baby" ;
                		SHOW_MOVIE(buttonNum, mArea);
                		}
                	});
                button0115.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		mArea.setText(null);
                		buttonNum = "The Promise" ;
                		SHOW_MOVIE(buttonNum, mArea);
                		}
                	});
                button0116.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		mArea.setText(null);
                		buttonNum = "The Zookeeper\\'s Wife"  ;
                		SHOW_MOVIE(buttonNum, mArea);                		
                		}
                	});
                
  
                
///////////////////////////////////////////tab 2,3,4/////////////////////////////////////////////////// 
//	First Written by. "1315034 SeoYoung Sohn"														        
//	Modified by. "1315068 HeeJeong Cho"																
//																		 			                
//	Created the 'screen' of the THEATERS,SNAKS,GAME tap and objective container. 	 	            
//	Designed screen in order to allocate inner structure and used individual functional component   
//                                                                                                  
///////////////////////////////////////////////////////////////////////////////////////////////////////
            	
                //////////////////// tab 2////////////////////
                JPanel p021 = new JPanel();
        		p021.setLayout(new GridLayout(2,1));   
        		
        		JPanel p21 = new JPanel();
        		p21.setLayout(new GridLayout(1,2));   
        		JLabel TheaterChoose = new JLabel("Theater Info");
        	    JComboBox TheaterSelection = new JComboBox();
        	    THEATER_INCLUDE(TheaterSelection);
        	    p21.add(TheaterChoose);
        	    p21.add(TheaterSelection);
        	    p021.add(p21);
        	    
        	    JPanel p22 = new JPanel();
        	    JButton ProcessRequest = new JButton("Select");
        	    
        	    JPanel p23 = new JPanel();
        	    JTextArea TheaterInfo = new JTextArea(18,38);
        	    p23.add(TheaterInfo); 
        		//Providing ability to insert String for many lines
        	    JPanel p24 = new JPanel();
        	    JButton moviesIn = new JButton("Check out the movies in this theater !");
        	    p24.add(moviesIn);
        	    
        	    ProcessRequest.addActionListener(new ActionListener() {
        			public void actionPerformed(ActionEvent e) {
        				TheaterInfo.setText(null);
        				String selected = (String)TheaterSelection.getSelectedItem();
        				SHOW_THEATER(selected,TheaterInfo);
        			}
        		}); 
        	    p22.add(ProcessRequest);
        	    p021.add(p22);
        	    
        	    //////////////////// POP UP ////////////////////
        	    moviesIn.addActionListener(new ActionListener() {
        			public void actionPerformed(ActionEvent e) {
        				String selected = (String)TheaterSelection.getSelectedItem();
        				JFrame MoviesInTheater = new JFrame("Movies in " + selected +" theater");
        				JPanel moviePOP = new JPanel();
        				JTextArea movieTh = new JTextArea(17,30);
        				SHOW_MOVIE_IN_THEATER(selected, movieTh);
        				moviePOP.add(movieTh);
        				MoviesInTheater.add(moviePOP);
        				
        				
        				Dimension dim = new Dimension(300, 300);
        				
        				MoviesInTheater.pack(); 
        				MoviesInTheater.setPreferredSize(dim);
        				MoviesInTheater.setVisible(true);
        				MoviesInTheater.setLocationRelativeTo(null); // Center the frame 
        			}
        		}); 
        	    
        	    
        	    jp_2.setLayout(new BorderLayout()); // Organizing Layout 
        	    jp_2.add(p021, BorderLayout.NORTH); 
        		jp_2.add(p23, BorderLayout.CENTER);
        		jp_2.add(p24, BorderLayout.SOUTH);
        		
        		
        //////////////////// tab3 ////////////////////
        		JPanel p3 = new JPanel(); // Snack Tab
        		
        		frame2.getContentPane().add(p3, BorderLayout.CENTER);
        		
        		//making a new Jtable panel
        		String[] SnackCol = {"SnackName","Price","Type"};
            	JTable SnackTable = new JTable();
            	JScrollPane SnackTableContainer = new JScrollPane(SnackTable);
            	DefaultTableModel Snacktm = new DefaultTableModel(SnackCol,0);
            	SnackTable.setModel(Snacktm);
            	SnackTable.getColumnModel().getColumn(0).setPreferredWidth(210);
            	SnackTable.getColumnModel().getColumn(1).setPreferredWidth(2);
            	SnackTable.getColumnModel().getColumn(2).setPreferredWidth(5);
            	ADD_SNACKS(Snacktm);
            	p3.add(SnackTableContainer);
            	
            	
            	jp_3.setLayout(new BorderLayout()); // Organizing Layout 
        		jp_3.add(p3, BorderLayout.NORTH); 

        		
        //////////////////// tab 4 ////////////////////
        		JPanel p4 = new JPanel(); // Games Tab
        		
        		frame2.getContentPane().add(p4, BorderLayout.CENTER);
        		
        		//making a new Jtable panel
        		String[] col = {"GameName","Price","Age","InspectionDate"};
            	JTable GameTable = new JTable();
            	JScrollPane GametableContainer = new JScrollPane(GameTable);
            	DefaultTableModel Gtm = new DefaultTableModel(col,0);
            	GameTable.setModel(Gtm);
            	GameTable.getColumnModel().getColumn(0).setPreferredWidth(180);
            	GameTable.getColumnModel().getColumn(1).setPreferredWidth(2);
            	GameTable.getColumnModel().getColumn(2).setPreferredWidth(5);
            	GameTable.getColumnModel().getColumn(3).setPreferredWidth(40);
            	ADD_GAMES(Gtm);
            	p4.add(GametableContainer);
            	
            	
            	jp_4.setLayout(new BorderLayout()); // Organizing Layout 
        		jp_4.add(p4, BorderLayout.CENTER); 
        		
        		

        ////////////////////Frame Organization ////////////////////
        		Dimension dim = new Dimension(600, 700);
        		
        		frame2.pack(); 
        		frame2.setPreferredSize(dim);
        		frame2.setLocationRelativeTo(null); // Center the frame 
        		frame2.setVisible(true); 
            
            }


            
///////////////////////////////////////// frame 2/////////////////////////////////////////////////// 
//First Written by. "1315034 SeoYoung Sohn"														        
//Modified by. "1315068 HeeJeong Cho"																
//											 		
// management tab - needs sql knowledge                                                                    
////////////////////////////////////////////////////////////////////////////////////////////////////
            
            
            
               
            if(arg0.getSource() == jButton2 ){
            	
            	//adding tabs
            	JTabbedPane  jtab = new JTabbedPane();
        		
                JPanel jp1 = new JPanel(); 
                JPanel jp2 = new JPanel();
                JPanel jp3 = new JPanel();
                JPanel jp4 = new JPanel();
                JPanel jp5 = new JPanel();
                JPanel jp6 = new JPanel();
                
                jtab.add("Movie Search", jp1);
                jtab.add("Movie Add", jp2);
                jtab.add("Movie Update", jp5);
                jtab.add("Movie Delete", jp3);
                jtab.add("Person Search", jp6);
        		jtab.add("Reservation Search", jp4);
        		
        		
        		frame.add("North", jtab);
        		
        		
        		
//////////////////////////// First tab //////////////////////////
//              
//Written by "1315034 SeoYoung Sohn"            
//Modified by  "1315068 HeeJeong Cho"     
//Searching movies that match the selected theater, genre, country
////////////////////////////////////////////////////////////////////////////////
        		
        		JPanel p11 = new JPanel(); 
        		p11.setLayout(new GridLayout(3,2));
                JLabel label11 = new JLabel("Theater Name");
                JLabel label12 = new JLabel("Genre");
                JLabel label13 = new JLabel("Country");
                JComboBox TheaterField = new JComboBox();
                JComboBox GenreField = new JComboBox();
                JComboBox CountryField = new JComboBox();
                
                p11.add(label11);
                p11.add(TheaterField);
                p11.add(label12);
                p11.add(GenreField);
                p11.add(label13);
                p11.add(CountryField);
                ITEM1_INCLUDE(TheaterField, GenreField, CountryField);
                
                JPanel p13 = new JPanel();
                JTextArea MovieSearch = new JTextArea(30, 50); 
                p13.add(MovieSearch);
                
                JPanel p12 = new JPanel();
                JButton button1 = new JButton("Search"); 
                button1.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		MovieSearch.setText(null);
                		// getting the selected item and putting in to the method already made;
                		String select_th = (String)TheaterField.getSelectedItem();
                		String select_g = (String)GenreField.getSelectedItem();
                		String select_c = (String)CountryField.getSelectedItem();
                		MOVIE_SEARCH(select_th, select_g, select_c, MovieSearch);
                		}
                	}); 
                p12.add(button1);    
                
                jp1.setLayout(new BorderLayout()); // Organizing Layout 
                jp1.add(p11, BorderLayout.NORTH); 
                jp1.add(p12, BorderLayout.CENTER);
                jp1.add(p13, BorderLayout.SOUTH);

       
                
////////////////////////////////// Second tab ///////////////////////////
//              
//Written by "1315034 SeoYoung Sohn"         
//Modified by "1315068 HeeJeong Cho"      
//Adding movie information UI -> inserting new data into databases
////////////////////////////////////////////////////////////////////////////////
                
                JPanel p21 = new JPanel();
                p21.setLayout(new GridLayout(1,2));
                
                JPanel panel2 = new JPanel();
                panel2.setLayout(new GridLayout(15,2));
                
                JLabel sub1 = new JLabel(" Title  ");
                JLabel sub2 = new JLabel(" Genre ");
                JLabel sub3 = new JLabel(" Date  ");
                JLabel sub4 = new JLabel(" Country");
                JLabel sub5 = new JLabel(" Evaluation");
                JLabel sub6 = new JLabel(" Age");
                JLabel sub7 = new JLabel(" Goods");
                JLabel sub8 = new JLabel(" Run time");
                JLabel sub9 = new JLabel(" Main Actor's name");
                JLabel sub10 = new JLabel(" Director's name");
                JLabel sub11 = new JLabel(" Main Actor's gender");
                JLabel sub12 = new JLabel(" Main Actor's birthday");
                JLabel sub13 = new JLabel(" Main Actor's country");
                JLabel sub14 = new JLabel(" Director's gender");
                JLabel sub15 = new JLabel(" Director's country");
                

                JTextField tf1 = new JTextField("",10);
                JTextField tf2 = new JTextField("",10);
                JTextField tf3 = new JTextField("",10);
                JTextField tf4 = new JTextField("",10);
                JTextField tf5 = new JTextField("",10);
                JTextField tf6 = new JTextField("",10);
                JTextField tf7 = new JTextField("",10);
                JTextField tf8 = new JTextField("",10);
                JTextField tf9 = new JTextField("",10);
                JTextField tf10 = new JTextField("",10);
                JTextField tf11 = new JTextField("",10);
                JTextField tf12 = new JTextField("",10);
                JTextField tf13 = new JTextField("",10);
                JTextField tf14 = new JTextField("",10);
                JTextField tf15 = new JTextField("",10);
                
                
                panel2.add(sub1); panel2.add(tf1);
                panel2.add(sub2); panel2.add(tf2);
                panel2.add(sub3); panel2.add(tf3);
                panel2.add(sub4); panel2.add(tf4);
                panel2.add(sub5); panel2.add(tf5);
                panel2.add(sub6); panel2.add(tf6);
                panel2.add(sub7); panel2.add(tf7);
                panel2.add(sub8); panel2.add(tf8);
                panel2.add(sub9); panel2.add(tf9);
                panel2.add(sub11); panel2.add(tf11);
                panel2.add(sub12); panel2.add(tf12);
                panel2.add(sub13); panel2.add(tf13);
                panel2.add(sub10); panel2.add(tf10);
                panel2.add(sub14); panel2.add(tf14);
                panel2.add(sub15); panel2.add(tf15);
                
                p21.add(panel2);
                
                JTextArea ta2 = new JTextArea(5,5);
                p21.add(ta2);
                
                JPanel panel22 = new JPanel();
                JButton b2 = new JButton("Insert");
                b2.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		String InsName = tf1.getText();
                		String InsGenre = tf2.getText();
                		String InsDate = tf3.getText();
                		String InsCountry = tf4.getText();
                		String InsEval = tf5.getText();
                		String InsAge = tf6.getText();
                		String InsGoods = tf7.getText();
                		String InsTime = tf8.getText();
                		String InsActor = tf9.getText();
                		String InsDirector = tf10.getText();
                		String InsAGender = tf11.getText();
                		String InsABirth = tf12.getText();
                		String InsACountry = tf13.getText();
                		String InsDGender = tf14.getText();
                		String InsDCountry = tf15.getText();
                		
                		INSERT_PERSON(InsActor, InsAGender, InsABirth, InsACountry, InsDirector, InsDGender, InsDCountry);
                		INSERT_MOVIE(InsName, InsGenre, InsDate, InsCountry, InsEval, InsAge, InsGoods, InsTime, InsActor, InsDirector);
                		
                		ta2.setText(null);
                		ta2.append("\n ... \n...");
                		ta2.append("\n Inserting actor information ... ");
                		ta2.append("\n ... \n...");
                		ta2.append("\n Inserting director information ... ");
                		ta2.append("\n ... \n...");
                		ta2.append("\n Inserting movie information ... ");
                		ta2.append("\n ... \n...");
                		ta2.append("\n\n\n Insertion Complete :D !");
                		}
                	}); 
                panel22.add(b2);

                
                jp2.setLayout(new BorderLayout());
                jp2.add(p21, BorderLayout.NORTH);
                jp2.add(panel22, BorderLayout.CENTER);
                
                
///////////////////////////////// Third tab ////////////////////////////////////
//                
//  Written by"1315068 HeeJeong Cho"      
//  Deletes movie information 
// reasks just in case - use transaction
////////////////////////////////////////////////////////////////////////////////
                
                
                JPanel p03 = new JPanel();
                p03.setLayout(new GridLayout(2,1));
                JPanel p31 = new JPanel(); //Reservation Number
                p31.setLayout(new GridLayout(1,2));		
                
                JLabel label33 = new JLabel("Movie Title");
                JTextField textF33 = new JTextField("Enter Movie Title"); 
                p31.add(label33);
                p31.add(textF33);
                p03.add(p31);
                
                JPanel p32 = new JPanel(); //console Area 
                JTextArea textA32 = new JTextArea(20,50); 
                p32.add(textA32); 

                
                JPanel p33 = new JPanel();  //console & cancel button 
                JButton button3 = new JButton("Delete"); 
                button3.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		textA32.setText(null);
                		textA32.append("\n ... \n...");
                		textA32.append("\n Erasing Goods... ");
                		textA32.append("\n ... \n...");
                		textA32.append("\n Canceling Tickets & Reservations... ");
                		textA32.append("\n ... \n...");
                		textA32.append("\n Deleting Movie... ");
                		textA32.append("\n ... \n...");
                		textA32.append("\n\n\n Are you sure about this?"); // transaction
                		}
                	}); 
                p33.add(button3);	 
                p03.add(p33);
                
                JPanel p34 = new JPanel();
                p34.setLayout(new GridLayout(1,2));
                JButton button331 = new JButton("Yes");
                button331.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		try{
                    		String MovieName = textF33.getText(); 
                			String SQL5 = "COMMIT;"; // yes button -> commit;
                			MOVIE_DELETE(MovieName, SQL5);
                			textA32.append("\n\n\n Succesfully Deleted :D ");
                	    }
                		catch(Exception e1){
                			e1.printStackTrace();
                		}
                		}
                	}); 
                JButton button332 = new JButton("No");
                button332.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		try{
                			String MovieName = textF33.getText();
                			String SQL5 = "ROLLBACK;"; // no button -> rollback;
                			MOVIE_DELETE(MovieName, SQL5);
                			textA32.append("\n\n\n Deleting Canceled :( ");
                	    }
                		catch(Exception e1){
                			e1.printStackTrace();
                		}
                		}
                	}); 
                p34.add(button331);
                p34.add(button332);
                
                jp3.setLayout(new BorderLayout()); // Organizing Layout 
                jp3.add(p03, BorderLayout.NORTH); 
                jp3.add(p32, BorderLayout.CENTER);
                jp3.add(p34, BorderLayout.SOUTH);


        		
//////////////// Fourth tab (in the UI, it is a little different)  /////////////////////
//              
//Written by"1315068 HeeJeong Cho"      
//Searching for the informations of reservation from data table DBCOURSE_Reservation
////////////////////////////////////////////////////////////////////////////////
        		JPanel p41 = new JPanel(); //phoneNum & password
        		p41.setLayout(new GridLayout(5,1));		
        		
        		JLabel label1 = new JLabel("Phone Number");
        		JTextField textF1 = new JTextField("01000000000");
        		p41.add(label1);
        		p41.add(textF1);
        		JLabel label2 = new JLabel("Password");
        		JTextField textF2 = new JTextField("****"); 
        		p41.add(label2);
        		p41.add(textF2);

        		JPanel p42 = new JPanel(); //console Area
        		JTextArea textA1 = new JTextArea(20, 50); 
        		p42.add(textA1); 
        		
        		JPanel p43 = new JPanel(); //console & confirm button 
        		JButton button4 = new JButton("Confirm"); 
        		button4.addActionListener(new ActionListener() {
        			public void actionPerformed(ActionEvent e) {
        				textA1.setText(null);
        				String CP = textF1.getText();
        				String tempPW = textF2.getText();
        				int PW = Integer.parseInt(tempPW);

        				TICKET_SEARCH(CP, PW, textA1);
        			}
        		}); 
        		p41.add(button4);
        		
        		
        		jp4.setLayout(new BorderLayout()); // Organizing Layout 
        		jp4.add(p41, BorderLayout.NORTH); 
        		jp4.add(p42, BorderLayout.CENTER);

///////////////////////////////// Fifth tab (in the UI, it is a little different)  /////////////////////
//              
//Written by"1315068 HeeJeong Cho"      
//Updating the movie information
// reasks just in case - use transaction
////////////////////////////////////////////////////////////////////////////////
        		
        		JPanel p51 = new JPanel(); //Reservation Number
        		p51.setLayout(new GridLayout(6,2));		

        		JLabel label5 = new JLabel("Movie title");
        		JComboBox Movies = new JComboBox();
        		JLabel label51 = new JLabel("Change Genre to");
        		JComboBox Genres = new JComboBox();
        		JLabel label52 = new JLabel("Change Country to");
        		JTextField Countries = new JTextField("");
        		JLabel label53 = new JLabel("Change Age to");
        		JComboBox Ages = new JComboBox();
        		JLabel label54 = new JLabel("Change Goods to");
        		JComboBox Goodss = new JComboBox();
        		JLabel label55 = new JLabel("Change Run time to");
        		JTextField RunTimes = new JTextField("");
        		
        		p51.add(label5); p51.add(Movies);
        		p51.add(label51); p51.add(Genres);
        		p51.add(label52); p51.add(Countries);
        		p51.add(label53); p51.add(Ages);
        		p51.add(label54); p51.add(Goodss);
        		p51.add(label55); p51.add(RunTimes);
        		
        		ITEM5_INCLUDE(Movies,Genres,Ages,Goodss);
        		
                JPanel p52 = new JPanel(); //console Area 
                JTextArea textA52 = new JTextArea(23,50); 
                p52.add(textA52); 
                
                JPanel p53 = new JPanel();  //console & cancel button 
                JButton button5 = new JButton("Update"); 

                button5.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		textA52.setText(null);
                		String mTitle = (String)Movies.getSelectedItem();
                		String update_G = (String)Genres.getSelectedItem();
                		String update_C = Countries.getText();
                		String update_A = (String)Ages.getSelectedItem();
                		String update_Gd = (String)Goodss.getSelectedItem();
                		String update_R = RunTimes.getText();
                		UPDATE_TEXT(mTitle, update_G, update_C, Integer.valueOf(update_A), update_Gd, Integer.valueOf(update_R), textA52);
                		textA52.append("\n\n\n Are you sure about this?"); // start transaction
                		}
                	}); 
                p53.add(button5);	 

                
                JPanel p54 = new JPanel();
                p54.setLayout(new GridLayout(1,2));
                JButton button551 = new JButton("Yes");
                button551.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		try{
                			String mTitle = (String)Movies.getSelectedItem();
                    		String update_G = (String)Genres.getSelectedItem();
                    		String update_C = Countries.getText();
                    		String update_A = (String)Ages.getSelectedItem();
                    		String update_Gd = (String)Goodss.getSelectedItem();
                    		String update_R = RunTimes.getText();
                			String SQL5 = "COMMIT;"; // yes button -> COMMIT;
                			UPDATE_RESERVATION(mTitle, update_G, update_C, Integer.valueOf(update_A), update_Gd, Integer.valueOf(update_R),SQL5);
                			textA52.append("\n\n\n Succesfully Updated :D ");
                	    }
                		catch(Exception e1){
                			e1.printStackTrace();
                		}
                		}
                	}); 
                JButton button552 = new JButton("No");
                button552.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		try{
                			String mTitle = (String)Movies.getSelectedItem();
                    		String update_G = (String)Genres.getSelectedItem();
                    		String update_C = Countries.getText();
                    		String update_A = (String)Ages.getSelectedItem();
                    		String update_Gd = (String)Goodss.getSelectedItem();
                    		String update_R = RunTimes.getText();
                    		String SQL5 = "ROLLBACK;"; // now button -> ROLLBACK;
                			textA52.append("\n\n\n Updating Canceled :( ");
                	    }
                		catch(Exception e1){
                			e1.printStackTrace();
                		}
                		}
                	}); 
                p54.add(button551);
                p54.add(button552);

                JPanel p05 = new JPanel();
        		p05.setLayout(new BorderLayout());
        		p05.add(p51);
        		p05.add(p53, BorderLayout.AFTER_LAST_LINE);
        		
                jp5.setLayout(new BorderLayout()); // Organizing Layout 
                jp5.add(p05, BorderLayout.NORTH); 
                jp5.add(p52, BorderLayout.CENTER);
                jp5.add(p54, BorderLayout.SOUTH);
        		


                

///////////////////////Sixth tab (in the UI, it is a little different)  ////////////////
//              
//Written by"1315068 HeeJeong Cho"      
//Searching for the informations of actors and directors individually
////////////////////////////////////////////////////////////////////////////////
                JPanel p61 = new JPanel(); 
                p61.setLayout(new GridLayout(4,2));
                JLabel label61 = new JLabel("Actor's gender");
                JLabel label62 = new JLabel("Actor's country");
                JLabel label63 = new JLabel("Director's gender");
                JLabel label64 = new JLabel("Director's country");
                
                JComboBox AGender = new JComboBox();
                JComboBox ACountry = new JComboBox();
                JComboBox DGender = new JComboBox();
                JComboBox DCountry = new JComboBox();
                
                p61.add(label61); p61.add(AGender);
                p61.add(label62); p61.add(ACountry);
                p61.add(label63); p61.add(DGender);
                p61.add(label64); p61.add(DCountry);
                ITEM6_INCLUDE(AGender, ACountry, DGender, DCountry);
                
                JPanel p63 = new JPanel();
                JTextArea PersonSearch = new JTextArea(30, 50); 
                p63.add(PersonSearch);
                
                JPanel p62 = new JPanel();
                JButton button61 = new JButton("Search Actor");
                JButton button62 = new JButton("Search Director");
                button61.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		PersonSearch.setText(null);
                		String select_AG = (String)AGender.getSelectedItem();
                		String select_AC = (String)ACountry.getSelectedItem();
                		ACTOR_SEARCH(select_AG, select_AC, PersonSearch);
                		} // search for only actors when left button
                	}); 
                button62.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		PersonSearch.setText(null);
                		String select_DG = (String)DGender.getSelectedItem();
                		String select_DC = (String)DCountry.getSelectedItem();
                		DIRECTOR_SEARCH(select_DG, select_DC, PersonSearch);
                		} // search for only directors when right button
                	}); 
                p62.add(button61);
                p62.add(button62);
                jp6.setLayout(new BorderLayout()); // Organizing Layout 
                jp6.add(p61, BorderLayout.NORTH); 
                jp6.add(p62, BorderLayout.CENTER);
                jp6.add(p63, BorderLayout.SOUTH);

                
                
                
                
                
                
        ////////////////////Frame Organization ////////////////////
        		Dimension dim = new Dimension(620, 500);
        		
        		frame.pack(); 
        		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        		frame.setPreferredSize(dim);
        		frame.setLocationRelativeTo(null); // Center the frame 
        		frame.setVisible(true); 

           }

        }
    }
//////////////////////////////////////////////////////////////////////////////////
// Written by "1315034 SeoYoung Sohn"													
// Designed whole process functions of GUI so that USER and ADMIN Screen can be 
// pop-up when user push buttons												
//////////////////////////////////////////////////////////////////////////////////
class test_Frame2_team14 extends JDialog{
	
    JLabel jlb1 = new JLabel("user.png");
    JLabel jlb2 = new JLabel("admin.png");
    
    public	 test_Frame2_team14(String str){
            getContentPane().add(jlb1);
            getContentPane().add(jlb2);
            
            jlb1.setText(str.toString());
            jlb2.setText(str.toString());
           
            this.setSize(200,200);
            this.setModal(true);
            this.setVisible(true);
           
       }
   }
