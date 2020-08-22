/** Maze class to hold the maze to be solved
 * @author Pedro Sousa
 * @version 6th April 2020
 */
package maze;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Serializable;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class Maze implements Serializable{
	private Tile entrance;
	private Tile exit;
	private List<List<Tile>> tiles;
	private List<List<String>> mazeString;
	private boolean entranceUsed = false;
	private boolean exitUsed = false;
	
	/**
        * Constructor for objects of class Maze
        * Initializes the tiles and mazeString by setting them to empty lists
        */ 
	private Maze(){
	    tiles =  new ArrayList<List<Tile>>();
	    mazeString = new ArrayList<List<String>>();
	}
	
	/** Creates a new maze object from a txt file
	 * @param fileString: The txt file to be read
	 * @return Returns a maze object
	 * @throws RaggedMazeException if all rows are not of the same size
	 * @throws NoEntranceException if there is no entrance in the maze
	 * @throws NoExitException if there is no exit in the maze
	 * @throws MultipleEntranceException if there is more than one entrance
	 * @throws MultipleExitException if there is more than one exit
	 * @throws IllegalAccessException if there is an attempt of illegal access to method
	 * @throws InvalidMazeException parent class for all Mazes that are illegal
	 */
	public static Maze fromTxt(String fileString) throws RaggedMazeException, 
	MultipleEntranceException, MultipleExitException, NoEntranceException, NoExitException, IllegalAccessException, InvalidMazeException{
	    Maze maze = new Maze();
	    try( FileReader fileNew = new FileReader(fileString);
	         BufferedReader fileStream = new BufferedReader(fileNew);
	       )
	    {
	        String lineText = null;
	        List<String> listLines = new ArrayList<String>();
	        while((lineText = fileStream.readLine()) != null){
	                listLines.add(lineText);
	                List<Tile> row = new ArrayList<Tile>();
	                List<String> row_String = new ArrayList<String>();
        	        for(int i=0; i<lineText.length(); i++){
        	            char c = lineText.charAt(i);
        	            row_String.add(String.valueOf(c));
        	            if(c!='e' && c!='x' && c!='.' && c!='#'){
        	            	throw new InvalidMazeException("The file contains characters that are not part of maze.");
        	            }else{
        	            	Tile t = Tile.fromChar(c);
        	            	row.add(t);
        	            }
        	        }
        	        maze.tiles.add(row);
        	        maze.mazeString.add(row_String);
	       }

	       for(List<Tile> l : maze.tiles){
        	    for(Tile t: l){
        	       	if(t.getType().equals(Tile.Type.ENTRANCE)){
        	        	maze.setEntrance(t);
        	        }else if(t.getType().equals(Tile.Type.EXIT)){
        	        	maze.setExit(t);
        	        }
        	    }
        	}

	       for(List<String> s : maze.mazeString){
	           if(s.size() != maze.mazeString.get(0).size()){
        	        throw new 
        	        RaggedMazeException("The rows in the maze have to be of the same size");
        	           
	           }else{continue;}
	       }

	    } catch(IOException ioe){
	        System.out.println("There was a problem with the file");

	    }

	    if(!maze.entranceUsed){
	       		throw new NoEntranceException("The maze needs to have an entrance.");
	    } 

	    if(!maze.exitUsed){
	        	throw new NoExitException("The maze needs to have an exit.");
	   	}

	    return maze;
	}

	
        /** Reads the adjacent tile to a given tile in the maze 
         * @param tileIn: the given tile from the maze
         * @param direction: the direction from which to find the adjacent tile
         * @return Returns the adjacent tile
         */
	public Tile getAdjacentTile(Tile tileIn, Direction direction){
	    Coordinate c = getTileLocation(tileIn);
	    int x = c.getX();
	    int y = c.getY();
	    try {
		    if (direction.equals(Direction.NORTH)){
		    	y = y + 1;
		    	Coordinate new_c = new Coordinate(x, y);
		    	Tile t = getTileAtLocation(new_c);
		    	return t;
		    } else if (direction.equals(Direction.SOUTH)){
		    	y = y - 1;
		    	Coordinate new_c = new Coordinate(x, y);
		    	Tile t = getTileAtLocation(new_c);
		    	return t;
		    } else if (direction.equals(Direction.WEST)){
		    	x = x - 1;
		    	Coordinate new_c = new Coordinate(x, y);
		    	Tile t = getTileAtLocation(new_c);
		    	return t;
		    } else { 
		    	x = x + 1;
		    	Coordinate new_c = new Coordinate(x, y);
		    	Tile t = getTileAtLocation(new_c);
		    	return t;
		    }
		} catch(NumberFormatException error){
		 	System.out.println("There is no adjacent tile in that direction;");
        }
        return tileIn;
	}
	
	
	/** Reads the entrance of the maze
	 * @return Returns the entrance tile
	 */  
	public Tile getEntrance(){
	    return entrance;
	}
	
	/** Reads the exit from the maze
	 * @return Returns the exit tile
	 */
	public Tile getExit(){
	    return exit;
	}
	
	/** Reads the tile at given coordinates (x, y)
	 * @param coordinate: the coordinates where the file is located
	 * @return Returns the tile in that position
	 */
	public Tile getTileAtLocation(Coordinate coordinate){
	   int x = coordinate.getX();
	   int y = (tiles.size()-1) - coordinate.getY();
	   Tile t = tiles.get(y).get(x);
	   return t;
	}
	
	/** Reads the location of a given tile
	 * @param tileIn: the tile we want to find
	 * @return Returns the coordinates (x, y) of the tile 
	 */
	public Coordinate getTileLocation(Tile tileIn){

		for(List<Tile> row: tiles){
			if(row.contains(tileIn)){break;}
			else if((tiles.indexOf(row) == tiles.size()-1) && !row.contains(tileIn)){
				return null;
			}
		}
		
	    int x = 0;
	    int y = 0;
	    int index_x=0;
	    int index_y=0;
	    for (List<Tile> row : tiles){
	    	if (row.contains(tileIn)){
	    		index_y = tiles.indexOf(row);
	    		index_x = row.indexOf(tileIn);
	    	}
		}
		x = index_x;
		y = (tiles.size()-1) - index_y;
	    Coordinate coordinate = new Coordinate(x, y);
	    return coordinate; 
		
	}

	
	/** Reads the two dimension list with all the tiles of the maze
	 * @return Returns the list of tiles
	 */
	public List<List<Tile>> getTiles(){
	    return tiles; 
	}
	
	/** Sets the entrance of the maze to a given tile
	 * @param entranceIn: the tile that we want to record as the entrance
	 * @throws MultipleEntranceException if there is more than one entrance
	 */
	private void setEntrance(Tile entranceIn) throws MultipleEntranceException, IllegalAccessException {
		if(getTileLocation(entranceIn) == null){
			throw new IllegalArgumentException("Illegal Access to entrance.");
		}else if (entranceIn == null){
			throw new IllegalArgumentException("Illegal Access to entrance.");
		}else if (this.getEntrance() == null){
			this.entrance = entranceIn;
			this.entranceUsed = true;
		}else{
			throw new MultipleEntranceException("The maze can oly have one entrance.");
		}
	}
	

	/** Sets the exit of the maze to a given tile
	 * @param exitIn: the tile that we want to record as the exit
	 * @throws MultipleExitException if there is more than one exit
	 */
	private void setExit(Tile exitIn) throws MultipleExitException, IllegalAccessException{
		if(getTileLocation(exitIn) == null){
			throw new IllegalArgumentException("Illegal Access to exit.");
		}else if(exitIn == null){
			throw new IllegalArgumentException("Illegal Access to exit.");
		}else if (this.getExit() == null){
			this.exit = exitIn;
			this.exitUsed = true;
		}else{
			throw new MultipleExitException("The maze can oly have one exit.");
		}
	}
	

	/** Allows for the test class to get a Coordinate object in order
	 * to test other methods
	 * @param xIn: the x coordinate
	 * @param yIn: the y coordinate
	 * @return Returns the Coordinate object we need to test methods
	 */
	public Coordinate setCoordinatesTest(int xIn, int yIn){
	    Coordinate c = new Coordinate(xIn, yIn);
	    return c;
	}
	
	/** Allows for the test class to set directions to test the method 
	 * where we find the adjacent tile
	 * @param s: string value that gets assigned to a direction
	 * @return Returns Direction
	 */
	public Direction setDirectionTest(String s){
	    if(s.equals("north")){
	        return Direction.NORTH;
	    }else if(s.equals("south")){
	        return Direction.SOUTH;
	    }else if(s.equals("west")){
	        return Direction.WEST;
	    }else{return Direction.EAST;}
	}

	public List<List<String>> get_mazeString(){
		return mazeString;
	}
	
	/** Reads the maze back to a single string value
	 * @return Returns a string with a visual representation of the maze
	 */
	@Override
	public String toString(){
	    String s = "";
	    StringBuilder listString = new StringBuilder();
	    for(int i=0; i<mazeString.size(); i++){
	        for(String x : mazeString.get(i)){
	            listString.append(x + " ");
	        }
	        listString.append(System.lineSeparator());
	    }
	    s = listString.toString();
	    return s;
	}


	
	/** Inner class Coordinate */
	public class Coordinate{
	    private int x;
	    private int y;
	    
	    /** Contructor initialises the x and y values
	     * @param x: the x coordinate
	     * @param y: the y coordinate  
	     */
	    public Coordinate(int x, int y){
	        this.x = x;
	        this.y = y;
	    }
	    
	    /** Reads the x coordinate
	     * @return Returns x
	     */
	    public int getX(){
	        return x;
	    }
	    
	    /** Reads the y coordinate
	     * @return Returns y
	     */
	    public int getY(){
	        return y;
	    }

	    public String toString(){
	    	String x = String.valueOf(getX());
	    	String y = String.valueOf(getY());
	    	String s = "(" + x + ", " + y + ")";
	    	return s;
	    }
	}
	
	/** Inner enum class */
	public enum Direction{
        NORTH, SOUTH, EAST, WEST;
        }

}