/** NoEntranceException class to be thrown if maze has no entrance 
 * @author Pedro Sousa
 * @version 6th April 2020
 */
package maze;
public class NoEntranceException extends InvalidMazeException{
    public NoEntranceException(String message){
        super(message);
    }
}