/** InvalidMazeException class to be thrown if maze 
 * does not have all rows of same size
 * @author Pedro Sousa
 * @version 6th April 2020
 */
package maze;
public  class InvalidMazeException extends Exception{
    public InvalidMazeException(String message){
        super(message);
    }
}
