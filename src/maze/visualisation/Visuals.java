/** Maze class to hold the maze to be solved
 * @author Pedro Sousa
 * @version 22 April 2020
 */


package maze.visualisation;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.effect.Reflection;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import java.io.File;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.shape.Rectangle;



public class Visuals{
	private Button stepButton;
	private Button saveButton;
	private Button goBack;
	private Button newMaze;
	private Button loadMaze;
	private Button mazeGen;
	private Button solution;
	private Button quit;
	private HBox box;
	private	int rows = 0;
	private	int columns = 0;


	public  Visuals(){}

	/** Method that sets the visual aspect of all maze solver pages of the application 
		* @return Returns the AnchorPane object containing the visual components
	*/
	public AnchorPane fancy(){

            Button stepButton = new Button("STEP");
            this.stepButton = stepButton;
            Button saveButton = new Button("SAVE");
            this.saveButton = saveButton;
            Button goBack = new Button("BACK");
            this.goBack = goBack;
            Button solution = new Button("SOLUTION");
            this.solution = solution;

            HBox box = new HBox(50);
            this.box = box;
			box.getChildren().addAll(saveButton,stepButton, goBack, solution);
			box.setAlignment(Pos.CENTER);
			stepButton.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 153),
			new CornerRadii(60), Insets.EMPTY)));
			saveButton.setBackground(new Background(new BackgroundFill(Color.rgb(255, 128, 128),
			new CornerRadii(60), Insets.EMPTY)));
			goBack.setBackground(new Background(new BackgroundFill(Color.rgb(152, 230, 152),
			new CornerRadii(60), Insets.EMPTY)));
			solution.setBackground(new Background(new BackgroundFill(Color.rgb(153, 255, 255),
			new CornerRadii(60), Insets.EMPTY)));
			AnchorPane.setLeftAnchor(box, 150d);
			AnchorPane.setRightAnchor(box, 150d);

      		Region red = new Region();
		    Region lime = new Region();
		    Region blue = new Region();
		    Region yellow = new Region();
		    Region center = new Region();

		    red.setPrefSize(50, 100);
		    lime.setPrefSize(100, 50);
		    blue.setPrefSize(100, 50);
		    yellow.setPrefSize(50, 100);

		    setBackgroundColor(red, Color.RED);
		    setBackgroundColor(blue, Color.BLUE);
		    setBackgroundColor(lime, Color.LIME);
		    setBackgroundColor(yellow, Color.YELLOW);
		    setBackgroundColor(center, Color.GAINSBORO);

		    AnchorPane.setBottomAnchor(red, 0d);
		    AnchorPane.setLeftAnchor(red, 50d);
		    AnchorPane.setTopAnchor(lime, 50d);
		    AnchorPane.setRightAnchor(blue, 0d);
		    AnchorPane.setBottomAnchor(blue, 50d);
		    AnchorPane.setRightAnchor(yellow, 50d);
		    
		    AnchorPane.setBottomAnchor(center, 50d);
		    AnchorPane.setTopAnchor(center, 50d);
		    AnchorPane.setLeftAnchor(center, 50d);
		    AnchorPane.setRightAnchor(center, 50d);

		    AnchorPane root = new AnchorPane(red, lime, blue, yellow, center, box);

		    return root;

	}


	/** Method responsible for creating the visual components of the homepage
		* @return Returns a Scene object to work as homepage for the application
	*/
	public Scene homePage(){

		double width;
    	double height;
		newMaze = new Button("NEW");
		this.newMaze = newMaze;
		loadMaze = new Button("LOAD");
		this.loadMaze = loadMaze;
		mazeGen = new Button("CREATE");
		this.mazeGen = mazeGen;
		quit = new Button("QUIT");
		this.quit = quit;


		Reflection r = new Reflection();
		r.setFraction(0.7f);

		Text headingLabel = new Text("MAZE SOLVER");
		headingLabel.setCache(true);
		headingLabel.setEffect(r);


		HBox buttons_box = new HBox(50);
		buttons_box.setAlignment(Pos.CENTER);
		buttons_box.getChildren().addAll(newMaze, loadMaze, mazeGen, quit);
		
		
		VBox vb = new VBox(70);
		vb.setAlignment(Pos.CENTER);
		AnchorPane.setTopAnchor(vb, 200d);
		AnchorPane.setLeftAnchor(vb, 200d);
		AnchorPane.setRightAnchor(vb, 200d);
		AnchorPane.setBottomAnchor(vb, 200d);
		vb.getChildren().addAll(headingLabel, buttons_box);

		AnchorPane root = fancy();
		root.getChildren().remove(box);
		root.getChildren().addAll(vb);

		Scene scene = new Scene(root, Color.LIGHTBLUE);
		scene.getStylesheets().add("maze/visualisation/style.css");

		headingLabel.setId("headingLabel");


		BorderStroke style = new BorderStroke(Color.rgb(153, 255, 255), BorderStrokeStyle.SOLID,
		new CornerRadii(40), new BorderWidths(2) );
		root.setBorder(new Border (style));
		root.setBackground(Background.EMPTY);

		loadMaze.setBackground(new Background(new BackgroundFill(Color.rgb(255, 128, 128),
			new CornerRadii(60), Insets.EMPTY)));;

		newMaze.setBackground(new Background(new BackgroundFill(Color.rgb(152, 230, 152),
			new CornerRadii(60), Insets.EMPTY)));;

		mazeGen.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 153),
			new CornerRadii(60), Insets.EMPTY)));;

		quit.setBackground(new Background(new BackgroundFill(Color.rgb(128, 128, 255),
			new CornerRadii(60), Insets.EMPTY)));;

		root.setMinSize(200, 200);
		root.setMaxSize(200, 200);


		return scene;
	}


	/** Method responsible for setting up the original maze read by the program, without worrying about
		the progress state of RouteFinder
		* @param mazeIn: the Maze object to be represented in the application
		* @param root: the AnchorPane object where the visual components will be implmented
		* @param rectangles: a list of rectangles where the rectangles that make the maze will be added
		* @param tilesRec: a list of tiles
	*/
	public void setUpOriginalMaze(Maze mazeIn, AnchorPane root, List<Rectangle> rectangles, List<Tile> tilesRec){

		List<List<Tile>> tiles = mazeIn.getTiles();
        rows = tiles.size();
        columns = tiles.get(0).size();

		Rectangle rectangle = null;

		for (int i = 0; i<columns; i++){
            	if (i==0){
            		rectangle = new Rectangle(150.0f, 80.0f,  30.0f, 30.0f);
            		rectangle.setArcHeight(10.0f);
            		rectangle.setArcWidth(10.0f);
            		Tile t = tiles.get(0).get(i);
  					Color c = getTypeColor(t);
  					rectangle.setFill(c);
			    	root.getChildren().add(rectangle);
			    	rectangles.add(rectangle);
			    	tilesRec.add(t);

			    	for(int j=1; j<rows; j++){
			    		Tile k = tiles.get(j).get(i);
			    		Color co = getTypeColor(k);
		        		rectangle = new Rectangle(150.0f, 80.0f + 30*j,  30.0f, 30.0f);
		            	rectangle.setFill(co);
		            	rectangle.setArcHeight(10.0f);
		            	rectangle.setArcWidth(10.0f);
		            	root.getChildren().add(rectangle);
		            	rectangles.add(rectangle);
			    		tilesRec.add(k);
		            }


            	}else if (i!=0){
            		rectangle = new Rectangle(150.0f + 30*i, 80.0f,  30.0f, 30.0f);
            		rectangle.setArcHeight(10.0f);
            		rectangle.setArcWidth(10.0f);
  					Tile t = tiles.get(0).get(i);
  					Color c = getTypeColor(t);
  					rectangle.setFill(c);
   			    	root.getChildren().add(rectangle);
   			    	rectangles.add(rectangle);
			    	tilesRec.add(t);

   			    	for(int j=1; j<rows; j++){
   			    		Tile k = tiles.get(j).get(i);
			    		Color co = getTypeColor(k);
		        		rectangle = new Rectangle(150.0f + 30*i, 80.0f + 30*j,  30.0f, 30.0f);
		            	rectangle.setFill(co);
		            	rectangle.setArcHeight(10.0f);
		            	rectangle.setArcWidth(10.0f);
		            	root.getChildren().add(rectangle);
		            	rectangles.add(rectangle);
			    		tilesRec.add(k);
            		}
            	}
        	}

	}


	/** Method responsible for matching different colors to the different types of Tile
		* @param t: the Tile object to be assigned a color
		* @return Returns the Color object
	*/
	public Color getTypeColor(Tile t){
		Color c = null;
		if(t.getType().equals(Tile.Type.ENTRANCE)){
			c = Color.rgb(255, 102, 102);
		}else if (t.getType().equals(Tile.Type.CORRIDOR)){
			c = Color.rgb(255, 255, 153);
		}else if (t.getType().equals(Tile.Type.WALL)){
			c = Color.rgb(128, 128, 255);
		}else{c=Color.rgb(152, 230, 152);}

		return c;
	} 


	/** Reads the new button
		* @return Returns the newMaze Button object
	*/
	public Button get_newMaze(){
		return newMaze;
	}

	/** Reads the load button
		* @return Returns the loadMaze Button object
	*/
	public Button get_loadMaze(){
		return loadMaze;
	}

	/** Reads the step button
		* @return Returns the stepButton Button object
	*/
	public Button get_stepButton(){
		return stepButton;
	}

	/** Reads the save button
		* @return Returns the saveButton Button object
	*/
	public Button get_saveButton(){
		return saveButton;
	}

	/** Reads the goBack button
		* @return Returns the goBack Button object
	*/
	public Button get_goBack(){
		return goBack;
	}

	/** Reads the solution button
		* @return Returns the goBack Button object
	*/
	public Button getSolution(){
		return solution;
	}

	/** Reads the quit button
		* @return Returns the quit Button object
	*/
	public Button getQuit(){
		return quit;
	}

	/** Reads the mazeGen button
		* @return Returns the mazeGen Button object
	*/
	public Button get_mazeGen(){
		return mazeGen;
	}

	/** Reads one of the visual componets of type HBox
		* @return Returns the box HBox object
	*/
	public HBox get_Box(){
		return box;
	}

	/** Sets the effects for the color of the regions
		* @param region: the Region object to whish we want to assign a color
		* @param color: the Color object to whish we want to assign the visual effects
	*/
	public static void setBackgroundColor(Region region, Color color) {
    // change to 50% opacity
	    color = color.deriveColor(0, 1, 1, 0.5);
	    region.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
	}


}