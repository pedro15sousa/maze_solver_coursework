/** MultipleEntranceException class to be thrown if maze has more than one entrance 
 * @author Pedro Sousa
 * @version 6th April 2020
 */
package maze;
public class MultipleEntranceException extends InvalidMazeException{
    public MultipleEntranceException(String message){
        super(message);
    }
}