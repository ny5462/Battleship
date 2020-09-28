import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface is the interface for players
 * @author- Nikhil Yadav
 */
public interface playerInterface extends Remote {
    //String name=" ";
    public String getName() throws IOException;
    public void printBoards() throws IOException;
    boolean validPlacementPoint(double a,double b) throws IOException;
    public boolean PlaceShip(BattleShip playerShip) throws IOException;
    public void PlayerMove() throws IOException;
    public void init(playerInterface player)throws IOException;
    public void sendMessageToClient(String message) throws RemoteException;

}
