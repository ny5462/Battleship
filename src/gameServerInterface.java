import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This is the interface for the game server
 */
public interface gameServerInterface extends Remote {
    String sendMsg(String msg) throws IOException;
    void player1Move() throws IOException;
    void player2Move() throws IOException;
    boolean PlaceShipPlayer1(BattleShip ship) throws IOException;
    boolean PlaceShipPlayer2(BattleShip ship) throws IOException;
    void shipCoordsPlayer1(String a,String b) throws IOException;
    public  void broadcastMessage(String message) throws RemoteException;
    void shipCoordsPlayer2(String a ,String b)throws IOException;
    void getPlayer(playerInterface player)throws IOException;

}
