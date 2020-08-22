/** MultipleExitException class to be thrown if maze has more than one exit 
 * @author Pedro Sousa
 * @version 6th April 2020
 */
package maze;
public class MultipleExitException extends InvalidMazeException{
    public MultipleExitException(String message){
        super(message);
    }
}
