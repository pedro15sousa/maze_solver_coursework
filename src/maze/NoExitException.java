/** NoExitException class to be thrown if maze has no exit 
 * @author Pedro Sousa
 * @version 6th April 2020
 */
package maze;
public class NoExitException extends InvalidMazeException{
    public NoExitException(String message){
        super(message);
    }
}
