/** RaggedMazeException class to be thrown if rows in the maze
 * are not all of the same size
 * @author Pedro Sousa
 * @version 6th April 2020
 */
package maze;
public class RaggedMazeException extends InvalidMazeException{
    public RaggedMazeException(String message){
        super(message);
    }
}

