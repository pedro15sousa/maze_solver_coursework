/** Maze class that defines the tiles that belong to the maze
 * @author Pedro Sousa
 * @version 6th April 2020
 */
package maze;
import java.io.Serializable;

public class Tile implements Serializable{
    private Type type;
    
    /** Constructor initialises th type of the tile
     * @param type: the type of the tile
     */
    private Tile(Type type){
        this.type = type;
    }
    
    /** Creates a Tile object from a given character
     * @param character: the character to make Tile object
     * @return Returns a Tile object
     */
    protected static Tile fromChar(char character){;
        if (character == 'e'){
            Tile tile = new Tile(Type.ENTRANCE);
            //System.out.println(type);
            return tile;
        }else if (character == 'x'){
            Tile tile = new Tile(Type.EXIT);
            return tile;
        }else if (character == '.'){
            Tile tile = new Tile(Type.CORRIDOR);
            return tile;
        }else{
          Tile tile = new Tile(Type.WALL);
          return tile;
        }
    }
    
    /** Reads the type of the tile
     * @return Returns the type
     */
    public Type getType(){
        return type;
    }
    
    /** Evaluates if the tile is navigable in the maze
     * @return Returns false if the Tile object is of type WALL
     */
    public boolean isNavigable(){
        if (type.equals(Type.WALL)){
            return false;
        }
        return true;
    }
    
    /** Reads the tile back to a string that changes accordingly to type
     * @return Returns the String representation of the tile
     */
    //@Override
    public String toString(){
        String r = new String();
        if(type.equals(Type.ENTRANCE)){
            r = "e";
        }else if(type.equals(Type.EXIT)){
            r = "x";
        }else if(type.equals(Type.WALL)){
            r = "#";
        }else{r=".";}
        return r;
    } 

    
    /** Inner enum class */
    public enum Type{
       CORRIDOR, ENTRANCE, EXIT, WALL;
    }
   
}

