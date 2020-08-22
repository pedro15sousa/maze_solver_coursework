/** NoRouteFoundException class to be thrown if maze 
 * has no solution
 * @author Pedro Sousa
 * @version 6th April 2020
 */

package maze.routing;

public class NoRouteFoundException extends RuntimeException{
	public NoRouteFoundException(String message){
		super(message);
	}
}
