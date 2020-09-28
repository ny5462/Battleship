/** Player 1 action file
@author - Nikhil Yadav
**/
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * This class acts as a setup for player1
 */
public class player1 extends UnicastRemoteObject implements Serializable,playerInterface {
    String name;
    private char board = '-';
    private boolean enemy = false;
    char[][] playerBoard = new char[10][10];
    char[][] enemyBoard = new char[10][10];
    private char hit = 'H';
    private char miss = 'M';
    private char ship = 'S';
    static BattleShip playerShip1 = new BattleShip(2, true);
    static BattleShip playerShip2 = new BattleShip(4, false);

    boolean lost = false;
    int myLastx;
    int myLasty;

    boolean strike = false;
    boolean won = false;
    static String ship1;
    static String ship2;

    public player1(String name) throws RemoteException{
        this.name=name;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                playerBoard[i][j] = board;
                enemyBoard[i][j] = board;
            }

        }

    }

    @Override
    public String getName() throws IOException{
        return this.name;
    }

    /**
     * displays the board
     */
    public void printBoards() {
        System.out.println("################Player Board####################");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(playerBoard[i][j] + " ");
            }
            System.out.println();

        }
        System.out.println("\n");
        System.out.println("############Opponent Board#####################");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(enemyBoard[i][j] + " ");
            }
            System.out.println();

        }
    }



    /**
     * checks if valid coordinates are present
     *
     * @param x
     * @param y
     * @return
     */
    @Override
    public boolean validPlacementPoint(double x, double y) throws IOException {
        return (x >= 0 && x <= 10) && (y >= 0 && y < 10);

    }

    @Override
    public boolean PlaceShip(BattleShip playerShip) throws IOException {
        System.out.println("Insert co-ordinates for ship :(x,y)");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String cords=in.readLine();

        if(playerShip==playerShip1){
            ship1=cords;
        }
        else{
            ship2=cords;
        }

        int x=Integer.parseInt(String.valueOf(cords.charAt(0)));
        int y = Integer.parseInt(String.valueOf(cords.charAt(2)));

        if (validPlacementPoint(x, y)) {
            int length = playerShip.shipLength;
            if (!playerShip.horizontal) {
                for (int i = y; i < y + length; i++) {
                    playerBoard[x][i] = ship;
                    if (playerShip == playerShip1) {

                        playerShip1.pairList.add(new Pair(x, i));
                    } else {
                        playerShip2.pairList.add(new Pair(x, i));
                    }

                }
            } else {
                for (int i = x; i < x + length; i++) {
                    playerBoard[i][y] = ship;
                    if (playerShip == playerShip1) {
                        //      BattleShip.MyClass pair=new BattleShip.MyClass(x,1);
                        playerShip1.pairList.add(new Pair(i, y));
                    } else {
                        //       BattleShip.MyClass pair=new BattleShip.MyClass(x,1);
                        playerShip2.pairList.add(new Pair(i, y));
                    }

                }

            }
            return true;
        }
        return false;
    }


    /**
     * players Move performed with this method
     *
     * @return
     * @throws IOException
     */
    @Override
    public void PlayerMove() throws IOException {
        System.out.println("Insert Coordinates to hit at enemy\n");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int x, y;
        String coords = in.readLine();
        if (coords.equals("-1,-1")) {
            x = -1;
            y = -1;
            this.myLastx = -1;
            this.myLasty = -1;
            System.out.println("I WON");
            won=true;
            //refreshGame();
        } else {
            x = Integer.parseInt(String.valueOf(coords.charAt(0)));
            y = Integer.parseInt(String.valueOf(coords.charAt(2)));
            this.myLastx = x;
            this.myLasty = y;

        }
    }

    @Override
    public void init(playerInterface player1) throws IOException {
        player1.PlaceShip(playerShip1);
        player1.PlaceShip(playerShip2);
    }

    @Override
    public void sendMessageToClient(String message) throws RemoteException {
        System.out.println(message);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("enter player name:");
        Scanner sc =new Scanner(System.in);
        String nam=sc.nextLine();
        playerInterface player1=new player1(nam);

        player1.init(player1);
        System.out.println(player1.getName() + " is ready to play");
        try{


           // Registry registry = LocateRegistry.getRegistry(serverIp, serverRegistryPort);

            Registry registry= LocateRegistry.getRegistry(3000);
            gameServerInterface client=(gameServerInterface) registry.lookup("Hello");
            client.getPlayer(player1);
            client.shipCoordsPlayer1(ship1,ship2);
            //System.out.println(client.sendMsg("2,2"));
            player1.printBoards();
            client.broadcastMessage(player1.getName()+" is ready to play");
            player1.PlayerMove();
            client.broadcastMessage(""+((player1) player1).myLastx+","+((player1) player1).myLasty);

        } catch (NotBoundException e) {
            e.printStackTrace();
        }

    }
}
