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



public class MazeDriver{
	
	 public static void main(String[] args) {
	     try{
    		Maze maze = Maze.fromTxt("maze2.txt");
            RouteFinder test = new RouteFinder(maze);
            maze.toString();
    		System.out.println();
    		Tile t = maze.getTileAtLocation(maze.setCoordinatesTest(0, 0));
            System.out.println("Tile and its coordinates");
    		System.out.println(t.toString());
    		System.out.println(maze.getTileLocation(t).getX());
            System.out.println(maze.getTileLocation(t).getY());
            System.out.println();
            System.out.println("Adjacent tile");
    		//System.out.println(maze.getAdjacentTile(t, Maze.Direction.NORTH));
            Tile e = maze.getEntrance();
            System.out.println(e);
            System.out.println(maze.getAdjacentTile(t, Maze.Direction.NORTH));
            if (maze.getAdjacentTile(t, Maze.Direction.NORTH).equals(e)){
                System.out.println("eureka");
            }
            System.out.println();
            Maze.Coordinate c  =  maze.new Coordinate(0, 2);
            System.out.println(maze.getTileAtLocation(c));
            System.out.println("Route finder method");
            //RouteFinder z = test.load("aaaaaa.ser");
            //z.getRoute();
            while(!test.isFinished()){
                test.step();
                test.getRoute();
            }
            test.step();
            //z.toString();
            //test.save("aaaaaa.ser");
            }catch (RaggedMazeException e){
                e.printStackTrace();
            } catch (MultipleEntranceException j){
                j.printStackTrace();
            } catch (MultipleExitException k){
                k.printStackTrace();
            } catch (NoEntranceException d){
                d.printStackTrace();
            }catch (NoExitException f){
                f.printStackTrace();
            }catch(NoRouteFoundException s){
                s.printStackTrace();
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }catch (InvalidMazeException e ){
                e.printStackTrace();
            }
	}
}