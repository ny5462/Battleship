/**
Player 2 action file 
@author - Nikhil yadav
**/
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * This class acts as a setup for player 2
 */
public class player2 extends UnicastRemoteObject implements Serializable,playerInterface {
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
    static String ship1;
    static String ship2;


    boolean lost = false;
    int myLastx;
    int myLasty;

    boolean strike = false;
    boolean won=false;

    public player2(String name)throws RemoteException {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                playerBoard[i][j] = board;
                enemyBoard[i][j] = board;
            }

        }
        this.name=name;

    }


    @Override
    public String getName() {
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
    public boolean validPlacementPoint(double x, double y) throws IOException{
        return (x >= 0 && x <= 10) && (y >= 0 && y < 10);

    }

    @Override
    public boolean PlaceShip(BattleShip playerShip) throws IOException {
        System.out.println("Insert co=ordinates for ship: (x,y)");
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
            won = true;
            //refreshGame();
        } else {
            x = Integer.parseInt(String.valueOf(coords.charAt(0)));
            y = Integer.parseInt(String.valueOf(coords.charAt(2)));
            this.myLastx = x;
            this.myLasty = y;

        }

    }

    @Override
    public void init(playerInterface player1) throws IOException{
        player1.PlaceShip(playerShip1);
        player1.PlaceShip(playerShip2);
    }

    @Override
    public void sendMessageToClient(String message) throws RemoteException {
        System.out.println(message);
    }

    public static void main(String[] args)throws IOException {
        System.out.println("enter player name:");
        Scanner sc =new Scanner(System.in);
        String name=sc.nextLine();
        playerInterface player2=new player2(name);

        //playerInterface player2=new player2("Nikhil");
        player2.init(player2);
        System.out.println(player2.getName() +"is ready to play");
        try{
            Registry registry= LocateRegistry.getRegistry(3000);
            gameServerInterface client=(gameServerInterface) registry.lookup("Hello");
            client.getPlayer(player2);
            //System.out.println(client.sendMsg("3,1"));
            client.shipCoordsPlayer2(ship1,ship2);
            player2.printBoards();

            client.broadcastMessage(player2.getName()+" is ready to play");
            player2.PlayerMove();
            client.broadcastMessage(""+((player2) player2).myLastx+","+((player2) player2).myLasty);
        } catch (NotBoundException e) {
            e.printStackTrace();
        }


    }
}
