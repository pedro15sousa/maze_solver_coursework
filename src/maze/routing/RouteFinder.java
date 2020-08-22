/** Maze class to hold the maze to be solved
 * @author Pedro Sousa
 * @version 22 April 2020
 */


package maze.routing;
import maze.Maze;
import maze.Tile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.StreamCorruptedException;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.LinkedList;
import java.io.EOFException;

public class RouteFinder implements Serializable{ 
	private Maze maze;
	private Stack<Tile> route;
	private boolean finished;
	private List<Maze.Direction> direction_list;
	private List<List<String>> new_mazeString;
	private List<Tile> wrong_path; 


	/**
   		* Constructor for objects of class RouteFinder
  		* Initializes the maze to be solved, the route to solve the maze, add entrance to route.
   	*/
	public RouteFinder(Maze mazeIn){
		maze = mazeIn;
		finished = false;
		route = new Stack<>();
		wrong_path = new ArrayList<Tile>();
	    new_mazeString = new ArrayList<List<String>>();
		Tile entrance = maze.getEntrance();
		route.push(entrance);
		direction_list = new ArrayList<Maze.Direction>();
		direction_list.add(Maze.Direction.NORTH);
		direction_list.add(Maze.Direction.WEST);
		direction_list.add(Maze.Direction.SOUTH);
		direction_list.add(Maze.Direction.EAST);
	}


	/** Reads the maze to be solved
		* @return Returns the maze
	*/
	public Maze getMaze(){
		return maze;
	}


	/** Reads the list of tiles that are part of the wrong path
		* @return Returns wrong_path
	*/
	public List<Tile> getWrongPath(){
		return wrong_path;
	}


	/** Reads the list of tiles that make the correct route to solve the maze
		* @return Returns the objects of the Stack route as a list of tiles
	*/
	public List<Tile> getRoute(){
	
		List<Tile> route_list = new ArrayList<Tile>();
		for(Tile t : route){
			route_list.add(t);
		}
		return route_list;
	}


	/** Method responsible for evaluating if the maze is solved
		* @return Returns true is maze is solved, false otherwise
	*/
	public boolean isFinished(){
		if(this.finished){
			return true;
		}else{
			return false;
		}
	}


	/** Method to load files with RouteFinder object
		* @param file_name: the name of the file to be loaded
		* @return Returns the RouteFinder object
		* @throws FileNotFoundException if file does not exist
		* @throws ClassNotFoundException if file does not contain a RouteFinder object
		* @throws StreamCorruptedException if file is corrupted
		* @throws IOException 
		* @throws EOFException if file is an empty file
	*/
	public static RouteFinder load(String file_name) throws FileNotFoundException, ClassNotFoundException,
	StreamCorruptedException, IOException, EOFException{ 
		RouteFinder route_finder = null;
		try(
			FileInputStream input_maze = new FileInputStream(file_name);
			ObjectInputStream inputStream = new ObjectInputStream(input_maze);
			)
		{
		    route_finder = (RouteFinder) inputStream.readObject();

			return route_finder;
		}
		catch (EOFException e){
			throw new EOFException("Error");
		}
		catch(FileNotFoundException e)
		{
		throw new FileNotFoundException("Error");
		}
		catch(ClassNotFoundException e)
		{
		throw new ClassNotFoundException("Error");
		}
		catch(StreamCorruptedException e) 
		{
		throw new StreamCorruptedException("Error");
		}
		catch(IOException e){
			throw new IOException("Error");
		}

	}


	/** Method to save RouteFinder object to a file
		* @param file_name: the name of the file to be saved
		* @throws IOException for errors with saving the file
	*/
	public void save(String file_name) throws IOException{
		
		try( 
			FileOutputStream solving_maze = new FileOutputStream(file_name);
			ObjectOutputStream solving_maze_stream = new ObjectOutputStream(solving_maze); 
			)
		{
			solving_maze_stream.writeObject(this);
		}
		catch(IOException e){
			throw new IOException(e);
		}

	}


	/** Method responsible for the logic of solving the maze by adding or removing elements 
		from route stack
		* @return Returns true if exit is found, and false otherwise
	*/
	public boolean step(){ 

		if(isFinished()){
			return true;
		}

		/*if (route.isEmpty()){
			throw new NoRouteFoundException("Unsolveable maze.");
		}*/
		
		
		for (Maze.Direction direction:direction_list){			
			try{
				
				if(!maze.getAdjacentTile(route.peek(), direction).isNavigable()){
						if(direction.equals(Maze.Direction.EAST)){
							wrong_path.add(route.peek());
							route.pop();							
							return false;
						}else{continue;}


				} else if (maze.getAdjacentTile(route.peek(), direction).getType().equals(Tile.Type.EXIT)){
					route.push(maze.getAdjacentTile(route.peek(), direction));
					this.finished = true;
					return true;


				}else if(maze.getAdjacentTile(route.peek(), direction).isNavigable()){
					
					if (route.contains(maze.getAdjacentTile(route.peek(),direction))){
						if(direction.equals(Maze.Direction.EAST)){
							wrong_path.add(route.peek());
							route.pop();
							return false;
						}else{continue;}

					}else if (!route.contains(maze.getAdjacentTile(route.peek(),direction)) &&
						wrong_path.contains(maze.getAdjacentTile(route.peek(),direction))){
						if(direction.equals(Maze.Direction.EAST)){
							wrong_path.add(route.peek());
							route.pop();
							return false;
						}else{continue;}

					}else if (!route.contains(maze.getAdjacentTile(route.peek(),direction)) &&
						!wrong_path.contains(maze.getAdjacentTile(route.peek(),direction))){
							route.push(maze.getAdjacentTile(route.peek(), direction));
							return false;
					}
				}

			}catch(IndexOutOfBoundsException error){
				if(direction.equals(Maze.Direction.EAST)){
					wrong_path.add(route.peek());
					route.pop();
					return false;
				}
				continue;
	
			}catch (EmptyStackException e){
				throw new NoRouteFoundException("Unsolveable maze.");
			}
		}

		if (route.isEmpty() || route.size()==1){
			throw new NoRouteFoundException("Unsolveable maze.");
		}

	    return false;
	}
	

	/** Reads the curent state of the maze as a String
		* @return Returns a string representing the maze and the current state of the solution
	*/
	@Override
	public String toString(){
		List<List<Tile>> tiles = maze.getTiles();
		new_mazeString.clear();

		for (List<Tile> row:tiles){
			List<String> rowString = new ArrayList<String>();
			for(Tile t: row){
				if(route.contains(t)){
					rowString.add("*");
				}else if (wrong_path.contains(t)){
					rowString.add("-");
				}else{rowString.add(t.toString());}
			}

			new_mazeString.add(rowString);
		
		}

	    String s = "";
	    StringBuilder listString = new StringBuilder();
	    for(int i=0; i<new_mazeString.size(); i++){
	        for(String x : new_mazeString.get(i)){
	            listString.append(x + " ");
	        }
	        listString.append(System.lineSeparator());
	    }
	    s = listString.toString();
	    return s;
	}


}