import java.text.NumberFormat;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import javafx.stage.FileChooser;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import maze.Maze;
import maze.Tile;
import maze.InvalidMazeException;
import maze.RaggedMazeException;
import maze.NoEntranceException;
import maze.NoExitException;
import maze.MultipleEntranceException;
import maze.MultipleExitException;
import maze.routing.RouteFinder;
import maze.routing.NoRouteFoundException;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import maze.visualisation.Visuals;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.io.EOFException;
import java.util.Random;
import java.util.Optional;


public class MazeApplication extends Application{

	private Stage stage;
	private Label headingLabel = new Label("MAZE SOLVER");
	private	int rows = 0;
	private	int columns = 0;
    private float blockSize = 30.0f;
    private boolean on = false;
    private double width;
    private double height;
    private int rowsInt;
    private int columnsInt;
    private boolean firstVisit = true;
    private Scene homeScene;


	@Override
	public void start(Stage stage){

		this.stage = stage;

		Visuals v = new Visuals();
		Scene homeScene = v.homePage();
		this.homeScene = homeScene;

		v.get_newMaze().setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(stage);
            if (file != null) {
                String fileAsString = file.toString();
                Scene scene1 = maze(fileAsString);
                stage.setScene(scene1);
            }
           
        });

		v.get_loadMaze().setOnAction(event -> {
			FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(stage);
            if (file != null) {
                String fileAsString = file.toString();
                Scene scene2 = load(fileAsString);
                stage.setScene(scene2);
            }
		});

		v.get_mazeGen().setOnAction(event -> {
			stage.setScene(genMaze());
		});

		v.getQuit().setOnAction(event -> {
			Platform.exit();
		});

		Screen screen = Screen.getPrimary();
	    Rectangle2D bounds = screen.getVisualBounds();
	    width = bounds.getWidth() / 3;
	    height = bounds.getHeight() / 3;
	    stage.setX(400);
	    stage.setY(400);
	    stage.setWidth(bounds.getWidth() / 2);
	    stage.setHeight(bounds.getHeight() / 2);
	  
        stage.setScene(homeScene);
		
		stage.show();


	}


	//new maze method
	public Scene maze(String file_name){

		List<Rectangle> rectangles = new ArrayList<>();
   		List<Tile> tilesRec = new ArrayList<>();

		try{
			on = true;
            Maze new_maze = Maze.fromTxt(file_name);
            RouteFinder new_route = new RouteFinder(new_maze);
            List<List<Tile>> tiles = new_maze.getTiles();
            rows = tiles.size();
            columns = tiles.get(0).size();

		    Visuals v = new Visuals();
		    AnchorPane root = v.fancy();
      		
      		v.setUpOriginalMaze(new_maze, root, rectangles, tilesRec);
	        
        	v.get_stepButton().setOnAction(e -> {
        		step(rectangles, tilesRec, new_route, root);
			});
        	v.get_saveButton().setOnAction(e -> {
        		save(new_route);
        	});
        	v.get_goBack().setOnAction(e -> {
        		start(stage);
        	});
        	v.getSolution().setOnAction(e -> {
      			finalRoute(rectangles, tilesRec, new_route, root);
      		});

	        Scene scene1 = new Scene(root);
	        return scene1;

        }catch (RaggedMazeException e){
         	errorMessages();
         	return homeScene;
      	} catch (MultipleEntranceException j){
            errorMessages();
         	return homeScene;
  		} catch (MultipleExitException k){
            errorMessages();
         	return homeScene;
        } catch (NoEntranceException d){
            errorMessages();
         	return homeScene;
        }catch (NoExitException f){
            errorMessages();
         	return homeScene;
        }catch (IllegalAccessException e){
            errorMessages();
         	return homeScene;
        }catch (InvalidMazeException e ){
            errorMessages();
         	return homeScene;
        }	

	}


	//load maze method
	public Scene load(String file_name){

		List<Rectangle> rectangles = new ArrayList<>();
   		List<Tile> tilesRec = new ArrayList<>();

   		try{

			RouteFinder route = RouteFinder.load(file_name);
			Visuals v = new Visuals();
		    AnchorPane root = v.fancy();
      		Rectangle rectangle = null;

      		Maze m = route.getMaze();
      		List<List<Tile>> tiles = m.getTiles();
      		rows = tiles.size();
            columns = tiles.get(0).size();

            v.setUpOriginalMaze(m, root, rectangles, tilesRec);

        	for(Tile s : tilesRec){
        		if (route.getWrongPath().contains(s)){
        			int index = tilesRec.indexOf(s);
					Rectangle r = rectangles.get(index);
					double x = r.getX();
					double y = r.getY();
					Rectangle move = new Rectangle(x, y, 30.0f, 30.0f);
					move.setArcHeight(10.0f);
		            move.setArcWidth(10.0f);
					move.setFill(Color.DARKGREY);
					root.getChildren().add(move);
        		}else if (route.getRoute().contains(s)){
        			int index = tilesRec.indexOf(s);
					Rectangle r = rectangles.get(index);
					double x = r.getX();
					double y = r.getY();
					Rectangle move = new Rectangle(x, y, 30.0f, 30.0f);
					move.setArcHeight(10.0f);
		            move.setArcWidth(10.0f);
					move.setFill(Color.rgb(153, 255, 255));
					root.getChildren().add(move);
        		}
        	}


        	v.get_stepButton().setOnAction(e -> {
        		step(rectangles, tilesRec, route, root);
			});
        	v.get_saveButton().setOnAction(e -> {
        		save(route);
        	});
        	v.getSolution().setOnAction(e -> {
      			finalRoute(rectangles, tilesRec, route, root);
      		});
        	v.get_goBack().setOnAction(e -> {
        		start(stage);
        	});

          	Scene scene2 = new Scene(root);
	        return scene2;

	    }
	    catch(EOFException e)
	    {
	    	errorMessages();
	    	return homeScene;
	    }
		catch(FileNotFoundException e)
		{
		errorMessages();
		return homeScene;
		}
		catch(ClassNotFoundException e)
		{
		errorMessages();
		return homeScene;
		}
		catch(StreamCorruptedException e) 
		{
		Alert a = new Alert(AlertType.ERROR);
		a.setContentText("THAT IS A NEW MAZE!");
		a.show();
		return homeScene;
		}
		catch(IOException e)
		{
		errorMessages();
		return homeScene;
		}

	}


	// star quality method to generate random maze
	public Scene genMaze(){
		
		String file_name = getRandomMazeFile();

		List<Rectangle> rectangles = new ArrayList<>();
   		List<Tile> tilesRec = new ArrayList<>();

		try{
			on = true;
            Maze new_maze = Maze.fromTxt(file_name);
            RouteFinder new_route = new RouteFinder(new_maze);
            List<List<Tile>> tiles = new_maze.getTiles();
            rows = tiles.size();
            columns = tiles.get(0).size();

		    Visuals v = new Visuals();
		    AnchorPane root = v.fancy();
      		v.setUpOriginalMaze(new_maze, root, rectangles, tilesRec);
	        
	        Button again = new Button("AGAIN");
	        again.setBackground(new Background(new BackgroundFill(Color.rgb(128, 128, 255),
			new CornerRadii(60), Insets.EMPTY)));
      		HBox box = v.get_Box();
      		box.getChildren().add(again);

      		again.setOnAction(e -> {
      			stage.setScene(genMaze());
      		});
      		v.getSolution().setOnAction(e -> {
      			finalRoute(rectangles, tilesRec, new_route, root);
      		});
        	v.get_stepButton().setOnAction(e -> {
        		step(rectangles, tilesRec, new_route, root);
			});
        	v.get_saveButton().setOnAction(e -> {
        		save(new_route);
        	});
        	v.get_goBack().setOnAction(e -> {
        		this.firstVisit = true;
        		start(stage);
        	});
   
	        Scene scene2 = new Scene(root);
	        return scene2;

        }catch (Exception e){
            errorMessages();
            return homeScene;
      	}

	}



	public static void setBackgroundColor(Region region, Color color) {
    // change to 50% opacity
	    color = color.deriveColor(0, 1, 1, 0.5);
	    region.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
	}



	public void step(List<Rectangle> rectangles, List<Tile> tilesRec, RouteFinder routeIn, AnchorPane root){

				if(routeIn.isFinished()){
        				Alert a = new Alert(AlertType.INFORMATION);
						a.setContentText("THE MAZE IS ALREADY SOLVED!");
						a.show();
				}

				try{
					routeIn.step();
					int size = routeIn.getRoute().size();
					Tile t = routeIn.getRoute().get(size-1);
					for(Tile s : tilesRec){
						if(routeIn.getWrongPath().contains(s)){
							int index = tilesRec.indexOf(s);
							Rectangle r = rectangles.get(index);
							double x = r.getX();
							double y = r.getY();
							Rectangle move = new Rectangle(x, y, 30.0f, 30.0f);
							move.setArcHeight(10.0f);
			            	move.setArcWidth(10.0f);
							move.setFill(Color.DARKGREY);
							root.getChildren().add(move);

						}else if(s.equals(t)){
							if(s.getType().equals(Tile.Type.EXIT)){
								continue;
							}else if(s.getType().equals(Tile.Type.ENTRANCE)){
								continue;
							}else{
								int index = tilesRec.indexOf(s);
								Rectangle r = rectangles.get(index);
								double x = r.getX();
								double y = r.getY();
								Rectangle move = new Rectangle(x, y, 30.0f, 30.0f);
								move.setArcHeight(10.0f);
				            	move.setArcWidth(10.0f);
								move.setFill(Color.rgb(153, 255, 255));
								move.setStroke(Color.rgb(0, 255, 255));
								root.getChildren().add(move);
							}	
						}
					}
				}catch(NoRouteFoundException e){
					Alert a = new Alert(AlertType.ERROR);
					a.setContentText("THE MAZE IS IMPOSSIBLE TO SOLVE!");
					a.show();
					start(stage);
				}catch(IndexOutOfBoundsException e){
					Alert a = new Alert(AlertType.ERROR);
					a.setContentText("THE MAZE IS IMPOSSIBLE TO SOLVE!");
					a.show();
					start(stage);
				}
	}


	public void save(RouteFinder routeIn){

				if(routeIn.isFinished()){
        				Alert a = new Alert(AlertType.INFORMATION);
						a.setContentText("THE MAZE IS ALREADY SOLVED!");
						a.show();
				}else{
					TextInputDialog dialog = new TextInputDialog();
					dialog.setHeaderText("Please type the name of the file:");
					dialog.setTitle("Save File Information");
					String response = dialog.showAndWait().get();

					try{
						routeIn.save(response);
					}catch (IOException w){
						Alert a = new Alert(AlertType.INFORMATION);
						a.setContentText("Something is wrong with the saving process.");
						a.show();
					}
				}
	}



	public String getRandomMazeFile(){

		if(this.firstVisit){
			getDimension();
		}

		if(rowsInt > 20 || columnsInt>20){
			Alert a = new Alert(AlertType.INFORMATION);
			a.setContentText("TOO BIG!!");
			a.show();
			start(stage);
			return null;
		}else if (rowsInt==1 && columnsInt==1){
			Alert a = new Alert(AlertType.ERROR);
			a.setContentText("INVALID MAZE");
			a.show();
			start(stage);
			return null;
		}
			
		Character [] arr = {'.', '#'};
		List<Character> values = new ArrayList<>();
		String file_name = "genMaze.txt";

		try{
			PrintWriter writer = new PrintWriter("genMaze.txt", "UTF-8");
			for(int i=0; i<rowsInt; i++){
				for(int j=0; j<columnsInt; j++){
					if(i!=0 && j==0){
						writer.println("");
					}
					Random r = new Random();
		        	int randomNumber= r.nextInt(arr.length);

		        	if(i==0 && j==0){
		        		writer.print('e');
		        		values.add('e');
		        		continue;
		        	}

		        	if(i==rowsInt-1 && j==columnsInt-1){
		        		writer.print('x');
		        		values.add('x');
		        		continue;
		        	}

		        	writer.print(arr[randomNumber]);
		        	values.add(arr[randomNumber]);

	        	}
			}

			writer.close();

		}catch (FileNotFoundException e){
			System.out.println("Error");
		}catch (UnsupportedEncodingException e){
			System.out.println("Error");
		}

		this.firstVisit = false;

		try{
			Maze new_maze = Maze.fromTxt(file_name);
            RouteFinder new_route = new RouteFinder(new_maze);
            while(!new_route.isFinished()){
            	new_route.step();
            }
        }catch(Exception n){
        	getRandomMazeFile();
        }

		return file_name;

	}


	public void getDimension(){
		
		TextInputDialog dialog = new TextInputDialog();
		dialog.setHeaderText("Please enter the number of rows: ");
		dialog.setTitle("Get Rows Number");
		Optional<String> rowsM = dialog.showAndWait();
		int rowsInt = 0;

		if (rowsM.isPresent()){
			try{
				rowsInt = Integer.valueOf(rowsM.get());
				this.rowsInt = rowsInt;
				dialog.close();
			}catch(Exception e){
				System.out.println("error");
			} 			 
		}

		TextInputDialog dialog1 = new TextInputDialog();
		dialog1.setHeaderText("Please enter the number of columns: ");
		dialog1.setTitle("Get Columns Number");
		Optional<String> columnsM = dialog1.showAndWait();
		int columnsInt = 0;

		if (columnsM.isPresent()){
			try{
				columnsInt = Integer.valueOf(columnsM.get());
				this.columnsInt = columnsInt;
			}catch(Exception e){
				System.out.println("error");
			} 			 
		}else{start(stage);}


	}


	public void finalRoute(List<Rectangle> rectangles, List<Tile> tilesRec, RouteFinder routeIn, AnchorPane root){

				if(routeIn.isFinished()){
        				Alert a = new Alert(AlertType.INFORMATION);
						a.setContentText("THE MAZE IS ALREADY SOLVED!");
						a.show();
				}

				try{
					while(!routeIn.isFinished()){
						routeIn.step();
					}
				}catch(NoRouteFoundException e){
					Alert a = new Alert(AlertType.ERROR);
					a.setContentText("THE MAZE IS IMPOSSIBLE TO SOLVE!");
					a.show();
					start(stage);
				}catch(IndexOutOfBoundsException e){
					Alert a = new Alert(AlertType.ERROR);
					a.setContentText("THE MAZE IS IMPOSSIBLE TO SOLVE!");
					a.show();
					start(stage);
				}

				for(Tile s : tilesRec){
					if(routeIn.getRoute().contains(s)){
							if(s.getType().equals(Tile.Type.EXIT)){
								continue;
							}else if(s.getType().equals(Tile.Type.ENTRANCE)){
								continue;
							}else{
								int index = tilesRec.indexOf(s);
								Rectangle r = rectangles.get(index);
								double x = r.getX();
								double y = r.getY();
								Rectangle move = new Rectangle(x, y, 30.0f, 30.0f);
								move.setArcHeight(10.0f);
				            	move.setArcWidth(10.0f);
								move.setFill(Color.rgb(153, 255, 255));
								move.setStroke(Color.rgb(0, 255, 255));
								root.getChildren().add(move);
							}		
					}
				}
	}



	public void errorMessages(){
		Alert a = new Alert(AlertType.ERROR);
		a.setContentText("INVALID MAZE");
		a.show();
		
	}


	public static void main(String[] args){
		launch(args);
	}

}

