import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import java.awt.ScrollPane;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import java.sql.*;
import java.util.*;

//: actions
import javafx.event.EventHandler;
import javafx.event.ActionEvent;


public class Main extends Application
{
	private Stage mainStage;
	
	public static ResultSet resultSet;
	public static PreparedStatement preparedStatement;
	public static Connection connection;
	public static Statement statement;
	
	
	
public Main()
{
	try{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:Data.db");
	}catch(Exception e){
		System.out.println("Error encounted while establishing connection.");
	}
}

	
	public static void main(String[] den)
	{
		launch(den);
	}//end-main
	
	@Override
	public void start(Stage pstage)
	{
		createConnection();
		startapp();
	}//end-start
	
	public void startapp()
	{
		monoObjects();
		createConnection();
		
		//: Header
		Text header = new Text("MovieMAD");
		header.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
		
		//: center
		TextField uname = new TextField();
		uname.setPromptText("Enter username");
		
		PasswordField pas = new PasswordField();
		pas.setPromptText("Enter password");
		
		
		Button login = new Button("Login");
		login.setOnAction((e)->{
		
			String uname_ = uname.getText();
			String pass_  = pas.getText();
			
			String query = "SELECT name, password FROM user where name = ? and password = ?";
			
			try
			{
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, uname_);
				preparedStatement.setString(2, pass_);
				
				resultSet = preparedStatement.executeQuery();
				
				if(resultSet.next())
				{
					mainStage.setScene(dashboard());
				}
			}//end-try
			catch(Exception ee){
				System.out.println("Un-authorized access!");
			}
		});
		
		Button not_reg = new Button("Create new");
		not_reg.setOnAction((e)->{
		
			mainStage.setScene(register());
			mainStage.setTitle("Register new user");
		});
		
		Button guest = new Button("Guest");
		guest.setOnAction((e)->{mainStage.setScene(guestMode());});
		
		Button admin = new Button("Admin");
		admin.setOnAction((e)->{
			mainStage.setScene(adminLogin());
		});
		
		HBox btnBox = new HBox();
		btnBox.setSpacing(20);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().addAll(login, not_reg, guest, admin);
		
		VBox box = new VBox();
		box.setSpacing(20);
		box.setPadding(new Insets(10, 30, 10, 30));
		box.setAlignment(Pos.CENTER);
		box.getChildren().addAll(header, uname, pas, btnBox);
		
		//: borderpane
		BorderPane bp = new BorderPane();
		bp.setCenter(box);
		
		//: scene
		Scene scene = new Scene(bp, 500, 500);
		mainStage.setScene(scene);
		mainStage.setTitle("Login");
		mainStage.show();
		mainStage.setResizable(false);
	}//end-startapp
	
	//: register new
	public Scene register()
	{
		createConnection();
		
		//: header
		Text header = new Text("Register new user");
		header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");
		
		Text line = new Text("\n__________________________________\n");
		
		//: name
		Text uname = new Text("Name:       ");
		
		TextField nameF = new TextField();
		nameF.setPromptText("Enter new name");
		
		HBox nameBox = new HBox();
		nameBox.setSpacing(20);
		nameBox.setAlignment(Pos.CENTER);
		nameBox.getChildren().addAll(uname, nameF);
		
		//: age
		Text age = new Text("Age:          ");
		
		TextField ageF = new TextField();
		ageF.setPromptText("Enter your age");
		
		HBox ageBox = new HBox();
		ageBox.setSpacing(20);
		ageBox.setAlignment(Pos.CENTER);
		ageBox.getChildren().addAll(age, ageF);
		
		//: interest
		Text interest = new Text("Interest:    ");
		
		TextField interestF = new TextField();
		interestF.setPromptText("Enter your interest");
		
		HBox interestBox = new HBox();
		interestBox.setSpacing(20);
		interestBox.setAlignment(Pos.CENTER);
		interestBox.getChildren().addAll(interest, interestF);
		
		//: password
		Text password = new Text("Password: ");
		
		PasswordField passwordF = new PasswordField();
		passwordF.setPromptText("Enter your password");
		
		HBox passwordBox = new HBox();
		passwordBox.setSpacing(20);
		passwordBox.setAlignment(Pos.CENTER);
		passwordBox.getChildren().addAll(password, passwordF);
		
		//: btns
		Button reg = new Button("Register");
		reg.setOnAction((e)->{
		
		String query = "INSERT INTO user VALUES(?,?,?,?)";
		
			try
			{
								
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, nameF.getText());
				preparedStatement.setString(2, ageF.getText());
				preparedStatement.setString(3, interestF.getText());
				preparedStatement.setString(4, passwordF.getText());
				
				preparedStatement.execute();
				
			}//end-try
			catch(Exception aa){}
		
			success();
		});
		
		Button back = new Button("Back");
		back.setOnAction((e)->{
		
			startapp();
		});
		
		HBox btn = new HBox();
		btn.setSpacing(20);
		btn.setAlignment(Pos.CENTER);
		btn.getChildren().addAll(reg, back);
		
		//: 
		VBox box = new VBox();
		box.setSpacing(20);
		box.setAlignment(Pos.CENTER);
		box.getChildren().addAll(header, line, nameBox, ageBox, interestBox, passwordBox, btn);
		
		//: bp
		BorderPane bp = new BorderPane();
		bp.setCenter(box);
		
		Scene scene = new Scene(bp, 500, 500);
		return scene;		
		
	}//: end
	
	//: dashboard
	public Scene dashboard()
	{
		createConnection();
		
		//: Text
		Text header = new Text("User-Dashboard");
		header.setStyle("-fx-font-weight: bold;");
		
		HBox headerBox = new HBox();
		headerBox.setSpacing(20);
		headerBox.setPadding(new Insets(10, 20, 10, 20));
		headerBox.getChildren().add(header);
		
		Button add = new Button("Add info");
		add.setMinWidth(50);
		add.setMinHeight(50);
		
		add.setOnAction((e)->{
			mainStage.setScene(userAddInfo());
		});
		
		Button edit = new Button("Edit info");
		edit.setMinWidth(50);
		edit.setMinHeight(50);
		edit.setOnAction((e)->{mainStage.setScene(searchMovie());});
				
		Button view = new Button("View Movies");
		view.setMinWidth(50);
		view.setMinHeight(50);
		view.setOnAction((e)->{mainStage.setScene(userviewInfo());});
		
		HBox btnBox = new HBox();
		btnBox.setSpacing(20);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().addAll(add, edit, view);
		
		
		Button exit = new Button("exit");
		exit.setStyle("-fx-text-fill: red;");
		exit.setOnAction((e)->{
			Platform.exit();
		});
		
		HBox exitBox = new HBox();
		exitBox.setPadding(new Insets(0,0, 20,0));
		exitBox.setAlignment(Pos.CENTER);
		exitBox.getChildren().add(exit);
		
		
		BorderPane bp = new BorderPane();
		bp.setTop(headerBox);
		bp.setCenter(btnBox);
		bp.setBottom(exitBox);
		
		Scene scene = new Scene(bp, 500, 500);
		return scene;		
		
	}//end-dashboard
	
	//: add user-moive info
	public Scene userAddInfo()
	{
		createConnection();
		
		//: header
		Text header = new Text("Add Movie/Show info");
		header.setStyle("-fx-font-weight: bold;");
		
		Text line = new Text("\n___________________________________________\n");
		
		//: movie name
		Text movieName = new Text("Movie/Show name: ");
		
		TextField movieNameF = new TextField();
		movieNameF.setPromptText("Enter name of movie/show");
		
		HBox movieNameBox = new HBox();
		movieNameBox.setSpacing(20);
		movieNameBox.setAlignment(Pos.CENTER);
		movieNameBox.getChildren().addAll(movieName, movieNameF);
		
		//: genere
		Text genere = new Text("Genere:                 ");
		 
		TextField genereF = new TextField();
		genereF.setPromptText("Enter genere of movie/show");
		
		HBox genereBox = new HBox();
		genereBox.setSpacing(20);
		genereBox.setAlignment(Pos.CENTER);
		genereBox.getChildren().addAll(genere, genereF);
		
		//: rate
		Text rated = new Text("Rated:                   ");
		 
		TextField ratedF = new TextField();
		ratedF.setPromptText("Rate R/Adult");
		
		HBox ratedBox = new HBox();
		ratedBox.setSpacing(20);
		ratedBox.setAlignment(Pos.CENTER);
		ratedBox.getChildren().addAll(rated, ratedF);
		
		//: username
		Text username = new Text("Username:                   ");
		 
		TextField usernameF = new TextField();
		usernameF.setPromptText("Rate R/Adult");
		
		HBox usernameBox = new HBox();
		usernameBox.setSpacing(20);
		usernameBox.setAlignment(Pos.CENTER);
		usernameBox.getChildren().addAll(username, usernameF);
		
		Button add = new Button("Add");
		add.setOnAction((e)->{
		
		
		String query1 = "INSERT INTO movies VALUES(? , ?, ?)";
		
			try
			{
			
				preparedStatement = connection.prepareStatement(query1);
				preparedStatement.setString(1, movieNameF.getText());
				preparedStatement.setString(2, genereF.getText());
				preparedStatement.setString(3, ratedF.getText());
				preparedStatement.execute();
				
			}//end-try
			catch(Exception l)
			{
				
			}
			success();
			
		});
		
		Button home = new Button("Back");
		home.setOnAction((e)->{mainStage.setScene(dashboard());});
		
		HBox btnBox = new HBox();
		btnBox.setSpacing(20);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().addAll(add, home);
		
		//: 
		VBox box = new VBox();
		box.setAlignment(Pos.CENTER);
		box.setSpacing(20);
		box.getChildren().addAll(header, line, movieNameBox, genereBox, ratedBox, btnBox);
		
				
		//: scene
		Scene scene = new Scene(box, 500, 500);
		return scene;
		
	}//enduserAddInfo
	
	//: search and edit movie
	public Scene searchMovie()
	{
		createConnection();
		
		//: header
		Text header = new Text("Search Movie");
		header.setStyle("-fx-font-weight: bold;");
		
		Text line = new Text("\n_____________________________________\n");
		
		Text search = new Text("Look for Movie: ");
		TextField sf = new TextField();
		sf.setPrefWidth(300);
		
		Button sear = new Button("Look");
		sear.setOnAction((e)->{
		
			try
			{
				String query = "SELECT * from movies_ where name = '"+sf+"'";
				
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.execute();
				
			}//end-try
			catch(Exception aa){}
			
			mainStage.setScene(userEditInfo(sf.getText()));
			
		});
		
		HBox box = new HBox();
		box.setSpacing(20);
		box.setAlignment(Pos.CENTER);
		box.getChildren().addAll(search, sf);
		
		Button home = new Button("Home");
		home.setOnAction((e)->{mainStage.setScene(adminPanel());});
		
		
		HBox btnBox = new HBox();
		btnBox.setSpacing(20);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().addAll(sear, home);
		
		VBox v = new VBox();
		v.setSpacing(20);
		v.setAlignment(Pos.CENTER);
		v.getChildren().addAll(header, line, box, btnBox);
		
		Scene scene = new Scene(v, 500, 500);
		return scene;
		
	}//end-search and edit
	
	//: edit user-moive info
	public Scene userEditInfo(String name_)
	{
		createConnection();
		
		//: header
		Text header = new Text("Edit Movie/Show info");
		header.setStyle("-fx-font-weight: bold;");
		
		Text line = new Text("\n___________________________________________\n");
		
		//: movie name
		Text movieName = new Text("Movie/Show name: ");
		
		TextField movieNameF = new TextField();
		movieNameF.setPromptText("Enter name of movie/show");
		
		HBox movieNameBox = new HBox();
		movieNameBox.setSpacing(20);
		movieNameBox.setAlignment(Pos.CENTER);
		movieNameBox.getChildren().addAll(movieName, movieNameF);
		
		//: genere
		Text genere = new Text("Genere:                 ");
		 
		TextField genereF = new TextField();
		genereF.setPromptText("Enter genere of movie/show");
		
		HBox genereBox = new HBox();
		genereBox.setSpacing(20);
		genereBox.setAlignment(Pos.CENTER);
		genereBox.getChildren().addAll(genere, genereF);
		
		//: rate
		Text rated = new Text("Rated:                   ");
		 
		TextField ratedF = new TextField();
		ratedF.setPromptText("Rate R/Adult");
		
		HBox ratedBox = new HBox();
		ratedBox.setSpacing(20);
		ratedBox.setAlignment(Pos.CENTER);
		ratedBox.getChildren().addAll(rated, ratedF);
		
		Button add = new Button("Edit");
		add.setOnAction((e)->{
			try{
				String query = "UPDATE movies_ set genere = '"+genereF.getText()+"', rate='"+ratedF.getText()+"' where name = '"+name_+"'";
				
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.execute();
			}//end-try
			catch(Exception aa){}
			success();}
		
		);
		
		Button home = new Button("Back");
		home.setOnAction((e)->{mainStage.setScene(dashboard());});
		
		HBox btnBox = new HBox();
		btnBox.setSpacing(20);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().addAll(add, home);
		
		//: 
		VBox box = new VBox();
		box.setAlignment(Pos.CENTER);
		box.setSpacing(20);
		box.getChildren().addAll(header, line, movieNameBox, genereBox, ratedBox, btnBox);
		
		//: scene
		Scene scene = new Scene(box, 500, 500);
		return scene;
		
	}//enduserEditInfo
	
	//: view movies list
	public Scene userviewInfo()
	{
		createConnection();
		
		//: header
		Text header = new Text("Top Movies");
		header.setStyle("-fx-font-weight: bold;");
		
		Text line = new Text("\n_____________________________________\n");
		
		//: movie: 1
		Text mn1 = new Text("Movie: 1");
		Text mname1 = new Text("Movie_1");
		Text r1 = new Text("Rated:  ");
		Text rated1 = new Text("R"); 
		
		HBox box1= new HBox();
		box1.setSpacing(20);
		box1.setAlignment(Pos.CENTER);
		box1.getChildren().addAll(mn1, mname1);
		
		HBox rated1Box= new HBox();
		rated1Box.setSpacing(20);
		rated1Box.setAlignment(Pos.CENTER);
		rated1Box.getChildren().addAll(r1, rated1);
		
		VBox v1 = new VBox();
		v1.setAlignment(Pos.CENTER);
		v1.setSpacing(20);
		v1.setPadding(new Insets(10, 10, 10, 10));
		v1.setStyle("-fx-border-width: 1px; -fx-border-color: #000;");
		v1.getChildren().addAll(box1, rated1Box);
		
		//: movie: 2
		Text mn2 = new Text("Movie: 2");
		Text mname2 = new Text("Movie_2");
		Text r2 = new Text("Rated:  ");
		Text rated2 = new Text("R"); 
		
		HBox box2= new HBox();
		box2.setSpacing(20);
		box2.setAlignment(Pos.CENTER);
		box2.getChildren().addAll(mn2, mname2);
		
		HBox rated2Box= new HBox();
		rated2Box.setSpacing(20);
		rated2Box.setAlignment(Pos.CENTER);
		rated2Box.getChildren().addAll(r2, rated2);
		
		VBox v2 = new VBox();
		v2.setAlignment(Pos.CENTER);
		v2.setSpacing(20);
		v2.setPadding(new Insets(20, 20, 20, 20));
		v2.setStyle("-fx-border-width: 2px; -fx-border-color: #000;");
		v2.getChildren().addAll(box2, rated2Box);
		
		//: movie: 3
		Text mn3 = new Text("Movie: 3");
		Text mname3 = new Text("Movie_3");
		Text r3 = new Text("Rated:  ");
		Text rated3 = new Text("R"); 
		
		HBox box3= new HBox();
		box3.setSpacing(30);
		box3.setAlignment(Pos.CENTER);
		box3.getChildren().addAll(mn3, mname3);
		
		HBox rated3Box= new HBox();
		rated3Box.setSpacing(30);
		rated3Box.setAlignment(Pos.CENTER);
		rated3Box.getChildren().addAll(r3, rated3);
		
		VBox v3 = new VBox();
		v3.setAlignment(Pos.CENTER);
		v3.setSpacing(30);
		v3.setPadding(new Insets(30, 30, 30, 30));
		v3.setStyle("-fx-border-width: 3px; -fx-border-color: #000;");
		v3.getChildren().addAll(box3, rated3Box);
		
		//: movie: 4
		Text mn4 = new Text("Movie: 4");
		Text mname4 = new Text("Movie_4");
		Text r4 = new Text("Rated:  ");
		Text rated4 = new Text("R"); 
		
		HBox box4= new HBox();
		box4.setSpacing(40);
		box4.setAlignment(Pos.CENTER);
		box4.getChildren().addAll(mn4, mname4);
		
		HBox rated4Box= new HBox();
		rated4Box.setSpacing(40);
		rated4Box.setAlignment(Pos.CENTER);
		rated4Box.getChildren().addAll(r4, rated4);
		
		VBox v4 = new VBox();
		v4.setAlignment(Pos.CENTER);
		v4.setSpacing(40);
		v4.setPadding(new Insets(40, 40, 40, 40));
		v4.setStyle("-fx-border-width: 4px; -fx-border-color: #000;");
		v4.getChildren().addAll(box4, rated4Box);
		
		//: btn
		Button home = new Button("Back");
		home.setOnAction((e)->{mainStage.setScene(dashboard());});
		
		HBox btnBox = new HBox();
		btnBox.setSpacing(20);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(home);
		
		TextArea area = new TextArea();
		area.setEditable(false);
		
		try
		{
			String query = "SELECT * FROM movies_";
			
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			
			StringBuilder sb = new StringBuilder();
			
			int trace = 1;
			while(resultSet.next())
			{
				sb.append("\n|==========================|\n");
				sb.append("|=====| Movie: "+trace);
				sb.append("\n|==========================|\n");
				String name = resultSet.getString("name");
				sb.append("Name: ");
				sb.append(name);
				sb.append("\n");
				
				String genere = resultSet.getString("genere");
				sb.append("Genere: ");
				sb.append(genere);
				sb.append("\n");
				
				String rate = resultSet.getString("rate");
				sb.append("Rated: ");
				sb.append(rate);
				sb.append("\n");
				
				trace++;
			}//end-while
			
			area.setText(sb.toString());
			
		}//end-try
		catch(Exception aaq){}
		
		VBox box = new VBox();
		box.setSpacing(20);
		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(10, 10, 10, 10));
		box.getChildren().addAll(header, line, area, btnBox);
		
		Scene scene = new Scene(box, 500, 500);
		return scene;
		
	}//end-userviewInfo
	
	//: guestMode movies list
	public Scene guestMode()
	{
		createConnection();
		
		//: header
		Text header = new Text("Top Movies (Guest)");
		header.setStyle("-fx-font-weight: bold;");
		
		Text line = new Text("\n_____________________________________\n");
		
				
		//: btn
		Button home = new Button("Back");
		home.setOnAction((e)->{startapp();});
		
		HBox btnBox = new HBox();
		btnBox.setSpacing(20);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(home);
		
		
		TextArea area = new TextArea();
		area.setEditable(false);
		
		try
		{
			String query = "SELECT * FROM movies_";
			
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			
			StringBuilder sb = new StringBuilder();
			
			int trace = 1;
			while(resultSet.next())
			{
				sb.append("\n|==========================|\n");
				sb.append("|=====| Movie: "+trace);
				sb.append("\n|==========================|\n");
				String name = resultSet.getString("name");
				sb.append("Name: ");
				sb.append(name);
				sb.append("\n");
				
				String genere = resultSet.getString("genere");
				sb.append("Genere: ");
				sb.append(genere);
				sb.append("\n");
				
				String rate = resultSet.getString("rate");
				sb.append("Rated: ");
				sb.append(rate);
				sb.append("\n");
				
				trace++;
			}//end-while
			
			area.setText(sb.toString());
			
		}//end-try
		catch(Exception aaq){}
		
		VBox box = new VBox();
		box.setSpacing(20);
		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(10, 10, 10, 10));
		box.getChildren().addAll(header, line, area, btnBox);
		
		Scene scene = new Scene(box, 500, 500);
		return scene;
		
	}//end-guestmode
	
	//: admin-login
	public Scene adminLogin()
	{
		createConnection();
		
		//: Header
		Text header = new Text("Admin-Login");
		header.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
		
		//: center
		TextField uname = new TextField();
		uname.setPromptText("Enter username");
		
		PasswordField pas = new PasswordField();
		pas.setPromptText("Enter password");
		
		
		Button login = new Button("Login");
		login.setOnAction((e)->{
		
			String uname_ = uname.getText();
			String pass_  = pas.getText();
			
			String query = "SELECT name, password FROM admin where name = ? and password = ?";
			
			try
			{
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, uname_);
				preparedStatement.setString(2, pass_);
				
				resultSet = preparedStatement.executeQuery();
				
				if(resultSet.next())
				{
					mainStage.setScene(adminPanel());	
				}
			}//end-try
			catch(Exception ee){
				System.out.println("Un-authorized access!");
			}
		});
			
		
		Button home = new Button("Home");
		home.setOnAction((e)->{startapp();});
		
		
		HBox btnBox = new HBox();
		btnBox.setSpacing(20);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().addAll(login, home);
		
		VBox box = new VBox();
		box.setSpacing(20);
		box.setPadding(new Insets(10, 30, 10, 30));
		box.setAlignment(Pos.CENTER);
		box.getChildren().addAll(header, uname, pas, btnBox);
		
		//: borderpane
		BorderPane bp = new BorderPane();
		bp.setCenter(box);
		
		//: scene
		Scene scene = new Scene(bp, 500, 500);
		
		return scene;
	}//end-adminLogin
	
	//: admin-panel
	public Scene adminPanel()
	{
		createConnection();
		
		//: Text
		Text header = new Text("Admin-Panel");
		header.setStyle("-fx-font-weight: bold;");
		
		HBox headerBox = new HBox();
		headerBox.setSpacing(20);
		headerBox.setPadding(new Insets(10, 20, 10, 20));
		headerBox.getChildren().add(header);
		
		Button add = new Button("Approve/Deny");
		add.setMinWidth(50);
		add.setMinHeight(50);
		add.setOnAction((e)->{
			mainStage.setScene(approve());
		});
		
		
		Button delete = new Button("Delete requests");
		delete.setMinWidth(50);
		delete.setMinHeight(50);
		
		delete.setOnAction((e)->{
			mainStage.setScene(delete_());
		});
		
				
		Button view = new Button("View Accounts");
		view.setMinWidth(50);
		view.setMinHeight(50);
		
		view.setOnAction((e)->{
			mainStage.setScene(viewAccounts());
		});
		
		
		HBox btnBox = new HBox();
		btnBox.setSpacing(20);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().addAll(add, delete, view);
		
		
		Button exit = new Button("exit");
		exit.setStyle("-fx-text-fill: red;");
		exit.setOnAction((e)->{
			Platform.exit();
		});
		
		HBox exitBox = new HBox();
		exitBox.setPadding(new Insets(0,0, 20,0));
		exitBox.setAlignment(Pos.CENTER);
		exitBox.getChildren().add(exit);
		
		
		BorderPane bp = new BorderPane();
		bp.setTop(headerBox);
		bp.setCenter(btnBox);
		bp.setBottom(exitBox);
		
		Scene scene = new Scene(bp, 500, 500);
		return scene;		
	}//end-adminPanel
	
	//: approve request
	public Scene approve()
	{
		createConnection();
		
		//: header
		Text header = new Text("Approve/Deny request");
		header.setStyle("-fx-font-weight: bold;");
		
		Text line = new Text("\n_____________________________________\n");
		
		Text search = new Text("Look for new request: ");
		TextField sf = new TextField();
		sf.setPrefWidth(300);
		
		Button sear = new Button("Look");
		sear.setOnAction((e)->{
		
			
			approveRequest(sf.getText());
			
		});
		
		HBox box = new HBox();
		box.setSpacing(20);
		box.setAlignment(Pos.CENTER);
		box.getChildren().addAll(search, sf);
		
		Button home = new Button("Home");
		home.setOnAction((e)->{mainStage.setScene(adminPanel());});
		
		
		HBox btnBox = new HBox();
		btnBox.setSpacing(20);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().addAll(sear, home);
		
		VBox v = new VBox();
		v.setSpacing(20);
		v.setAlignment(Pos.CENTER);
		v.getChildren().addAll(header, line, box, btnBox);
		
		Scene scene = new Scene(v, 500, 500);
		return scene;
		
	}//end-approve
	
	//: approveRequest msg
	public void approveRequest(String name__)
	{
		createConnection();
		
		Stage stage = new Stage();
		stage.setTitle("Approve or Deny request");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setResizable(false);
		
		Text msg = new Text("Approve or Deny request?");
		msg.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;");
		
		Button ap = new Button("Approve");
		ap.setOnAction((e)->{
		
			try
			{
				String query = "SELECT name, genere, rate from movies where name = '"+name__+"'";
				
				preparedStatement = connection.prepareStatement(query);
				resultSet = preparedStatement.executeQuery();
				
				while(resultSet.next())
				{
					String name_ = resultSet.getString("name");
					String genere_ = resultSet.getString("genere");
					String rate_  = resultSet.getString("rate");
					
					//: add to db
					String q = "INSERT INTO movies_ values(?,?,?)";
					
					preparedStatement = connection.prepareStatement(q);
					preparedStatement.setString(1, name_);
					preparedStatement.setString(2, genere_);
					preparedStatement.setString(3, rate_);
					preparedStatement.execute();
					
					//: del from temp movies
					String d = "DELETE FROM movies where name = '"+name__+"'"; 
					preparedStatement = connection.prepareStatement(d);
					preparedStatement.execute();
					
				}//end-while
				
			}//:end-try
			catch(Exception nn){}
			success();
			
		});
		
		Button deny = new Button("Deny");
		deny.setOnAction((e)->{
			denied();
		});
		
		HBox box1 = new HBox();
		box1.setSpacing(20);
		box1.setAlignment(Pos.CENTER);
		box1.getChildren().addAll(ap, deny);
		
		VBox box = new VBox();
		box.setAlignment(Pos.CENTER);
		box.setSpacing(20);
		box.getChildren().addAll(msg, box1);
		
		Scene scene = new Scene(box, 300, 120);
		stage.setScene(scene);
		stage.show();
	}//end-
	
	
	
	
	//: delete_
	public Scene delete_()
	{
		createConnection();
		
		//: header
		Text header = new Text("Delete request");
		header.setStyle("-fx-font-weight: bold;");
		
		Text line = new Text("\n_____________________________________\n");
		
		Text search = new Text("Look for request: ");
		TextField sf = new TextField();
		sf.setPrefWidth(300);
		
		Button sear = new Button("delete");
		sear.setStyle("-fx-text-fill: red;");
		sear.setOnAction((e)->{
		
			deleteRequest(sf.getText());
			
		});
		
		HBox box = new HBox();
		box.setSpacing(20);
		box.setAlignment(Pos.CENTER);
		box.getChildren().addAll(search, sf);
		
		Button home = new Button("Home");
		home.setOnAction((e)->{mainStage.setScene(adminPanel());});
		
		
		HBox btnBox = new HBox();
		btnBox.setSpacing(20);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().addAll(sear, home);
		
		VBox v = new VBox();
		v.setSpacing(20);
		v.setAlignment(Pos.CENTER);
		v.getChildren().addAll(header, line, box, btnBox);
		
		Scene scene = new Scene(v, 500, 500);
		return scene;
		
	}//end-delete_
	
	//: view account
	public Scene viewAccounts()
	{
		createConnection();
		
		//: header
		Text header = new Text("View Accounts");
		header.setStyle("-fx-font-weight: bold;");
		
		Text line = new Text("\n_____________________________________\n");
		
		Text search = new Text("Look for accounts: ");
		TextField sf = new TextField();
		sf.setPrefWidth(300);
		
		Button sear = new Button("Accounts");
		sear.setStyle("-fx-text-fill: red;");
		sear.setOnAction((e)->{
		
			mainStage.setScene(account(""));
			
		});
		
		
		HBox box = new HBox();
		box.setSpacing(20);
		box.setAlignment(Pos.CENTER);
		box.getChildren().addAll(search, sf);
		
		Button home = new Button("Home");
		home.setOnAction((e)->{mainStage.setScene(adminPanel());});
		
		
		HBox btnBox = new HBox();
		btnBox.setSpacing(20);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().addAll(sear, home);
		
		VBox v = new VBox();
		v.setSpacing(20);
		v.setAlignment(Pos.CENTER);
		v.getChildren().addAll(header, line, btnBox);
		
		Scene scene = new Scene(v, 500, 500);
		return scene;
		
	}//end-viewaccounts
	
	//: accounts
	public Scene account(String name__)
	{
		//: header
		Text header = new Text("User-Accounts");
		header.setStyle("-fx-font-weight: bold;");
		
		Text line = new Text("\n_____________________________________\n");
		
				
		//: btn
		Button home = new Button("Back");
		home.setOnAction((e)->{mainStage.setScene(adminPanel());});
		
		HBox btnBox = new HBox();
		btnBox.setSpacing(20);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(home);
		
		
		TextArea area = new TextArea();
		area.setEditable(false);
		
		try
		{
			String query = "SELECT * FROM user";
			
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			
			StringBuilder sb = new StringBuilder();
			
			int trace = 1;
			while(resultSet.next())
			{
				sb.append("\n|==========================|\n");
				sb.append("|=====| User: "+trace);
				sb.append("\n|==========================|\n");
				String name = resultSet.getString("name");
				sb.append("Name: ");
				sb.append(name);
				sb.append("\n");
				
				String genere = resultSet.getString("age");
				sb.append("Age: ");
				sb.append(genere);
				sb.append("\n");
				
				String rate = resultSet.getString("interest");
				sb.append("Interest: ");
				sb.append(rate);
				sb.append("\n");
				
				String pas = resultSet.getString("password");
				sb.append("Password: ");
				sb.append(pas);
				sb.append("\n");
				
				trace++;
			}//end-while
			
			area.setText(sb.toString());
			
		}//end-try
		catch(Exception aaq){}
		
		VBox box = new VBox();
		box.setSpacing(20);
		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(10, 10, 10, 10));
		box.getChildren().addAll(header, line, area, btnBox);
		
		Scene scene = new Scene(box, 500, 500);
		return scene;

	}
	
	//: deleteRequest msg
	public void deleteRequest(String name__)
	{
		createConnection();
		
		Stage stage = new Stage();
		stage.setTitle("Done");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setResizable(false);
		
		Text msg = new Text("Successfully done!");
		msg.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;");
		
			try
			{
				String query = "SELECT name, genere, rate from movies where name = '"+name__+"'";
				
				preparedStatement = connection.prepareStatement(query);
				resultSet = preparedStatement.executeQuery();
				
				while(resultSet.next())
				{
					String name_ = resultSet.getString("name");
					String genere_ = resultSet.getString("genere");
					String rate_  = resultSet.getString("rate");
										
					//: del from temp movies
					String d = "DELETE FROM movies where name = '"+name__+"'"; 
					preparedStatement = connection.prepareStatement(d);
					preparedStatement.execute();
					
				}//end-while
				
			}//:end-try
			catch(Exception nn){}
			
			
		
		
		Button close = new Button("Close");
		close.setOnAction((e)->{
			stage.close();
		});
		
		VBox box = new VBox();
		box.setAlignment(Pos.CENTER);
		box.setSpacing(20);
		box.getChildren().addAll(msg, close);
		
		Scene scene = new Scene(box, 250, 120);
		stage.setScene(scene);
		stage.show();
	}//end-success
	
	//: success msg
	public void success()
	{
		Stage stage = new Stage();
		stage.setTitle("Done");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setResizable(false);
		
		Text msg = new Text("Successfully done!");
		msg.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;");
		
		Button close = new Button("Close");
		close.setOnAction((e)->{
			stage.close();
		});
		
		VBox box = new VBox();
		box.setAlignment(Pos.CENTER);
		box.setSpacing(20);
		box.getChildren().addAll(msg, close);
		
		Scene scene = new Scene(box, 250, 120);
		stage.setScene(scene);
		stage.show();
	}//end-success
	
	//: denied msg
	public void denied()
	{
		createConnection();
		
		Stage stage = new Stage();
		stage.setTitle("Denied");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setResizable(false);
		
		Text msg = new Text("Denied!");
		msg.setFill(Color.RED);
		msg.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;");
		
		Button close = new Button("Close");
		close.setOnAction((e)->{
			stage.close();
		});
		
		VBox box = new VBox();
		box.setAlignment(Pos.CENTER);
		box.setSpacing(20);
		box.getChildren().addAll(msg, close);
		
		Scene scene = new Scene(box, 250, 120);
		stage.setScene(scene);
		stage.show();
	}//end-success
	
	public void monoObjects()
	{
		if(mainStage == null)
			mainStage = new Stage();
	}//end-monoObjects
	
	public void createConnection()
	{
		System.out.print("");
	}
}//end_class















