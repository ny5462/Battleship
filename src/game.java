/**
 * This program acts as server for the battleship game
 * and creates the board and performs TCP handshake protocol
 *
 * @author- Nikhil Yadav
 */

import javafx.util.Pair;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * creates board and adds plays the game
 */
public class game extends UnicastRemoteObject implements gameServerInterface {
    private int n_ships = 2;
    private char board = '-';
    private boolean enemy = false;
    char[][] playerBoard = new char[10][10];
    char[][] enemyBoard = new char[10][10];
    private char hit = 'H';
    private char miss = 'M';
    private char ship = 'S';
    BattleShip playerShip1 = new BattleShip(2, true);
    BattleShip playerShip2 = new BattleShip(4, false);
    BattleShip playerShip3 = new BattleShip(2, true);
    BattleShip playerShip4 = new BattleShip(4, false);
    static List<playerInterface> players=new ArrayList<>();

    boolean lost = false;
    int p1myLastx;
    int p1myLasty;
    int p2myLastx;
    int p2myLasty;
    boolean strike = false;
    boolean won=false;

    String ship1;
    String ship2;
    String ship3;
    String ship4;

    /**
     * constructs player and opponent board
     */
    protected game() throws RemoteException {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                playerBoard[i][j] = board;
                enemyBoard[i][j] = board;
            }

        }
    }

    /**
     * checks if valid coordinates are present
     *
     * @param x
     * @param y
     * @return
     */
    public boolean validPlacementPoint(double x, double y) {
        return (x >= 0 && x <= 10) && (y >= 0 && y < 10);

    }

    /**
     * players Move performed with this method
     *
     * @return
     * @throws IOException
     */
    @Override
    public void player1Move() throws IOException {
        System.out.println("Insert Coordinates to hit at enemy\n");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int x, y;
        String coords = in.readLine();
        if (coords.equals("-1,-1")) {
            x = -1;
            y = -1;
            this.p1myLastx = -1;
            this.p1myLasty = -1;
            System.out.println("I WON");
            won=true;
          //  refreshGame();
        } else {
            x = Integer.parseInt(String.valueOf(coords.charAt(0)));
            y = Integer.parseInt(String.valueOf(coords.charAt(2)));
            this.p1myLastx = x;
            this.p1myLasty = y;

        }
    }




    /**
     * players Move performed with this method
     *
     * @return
     * @throws IOException
     */
    @Override
    public void player2Move() throws IOException {
        System.out.println("Insert Coordinates to hit at enemy\n");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int x, y;
        String coords = in.readLine();
        if (coords.equals("-1,-1")) {
            x = -1;
            y = -1;
            this.p2myLastx = -1;
            this.p2myLasty = -1;
            System.out.println("I WON");
            won=true;
         //   refreshGame();
        } else {
            x = Integer.parseInt(String.valueOf(coords.charAt(0)));
            y = Integer.parseInt(String.valueOf(coords.charAt(2)));
            this.p2myLastx = x;
            this.p2myLasty = y;

        }
    }





    /**
     * enemy move performed by this method
     *
     * @param s
     * @throws IOException
     */
    public void enemyMove(String s) throws IOException {
        System.out.println("Received From Enemy:"+s);
        int x, y;
        if (s.equals("-1,-1")) {
            won=true;
            System.out.println("I WIN");

        } else {
            x = Integer.parseInt(String.valueOf(s.charAt(0)));
            y = Integer.parseInt(String.valueOf(s.charAt(2)));
            if (winCondition(x, y) && lost) {
                System.out.println("I Lost");
            }
        }

    }


    /**
     * checks for winning condition of the game
     *
     * @param x
     * @param y
     * @return
     */
    public boolean winCondition(int x, int y) {
        if (playerBoard[x][y] == 'S') {
            playerBoard[x][y] = 'H';
            System.out.println("It was a hit from the enemy, sending 1");
            this.strike = true;
            if (playerShip1.pairList.contains(new Pair(x, y)))
                playerShip1.blow();
            if (playerShip2.pairList.contains(new Pair(x, y)))
                playerShip2.blow();
            if (!playerShip1.alive() && !playerShip2.alive()) {
                lost = true;
                return true;
            } else {
                return false;
            }

        } else {
            playerBoard[x][y] = 'M';
            System.out.println("It was a miss from the enemy, sending 0");
            this.strike = false;
            return false;
        }

    }

//
//    /**
//     * Method to refresh the game
//     * @throws IOException
//     */
//    public void refreshGame() throws IOException {
//        gameServer board=new gameServer();
//        lost=false;
//        strike=false;
//        won=false;
//        this.playerShip1 = new BattleShip(2, true);
//        this.playerShip2 = new BattleShip(4, false);
//        board.PlaceShip(playerShip1);
//        board.PlaceShip(playerShip2);
//
//    }


    /**
     * places Ship on the board
     *
     * @param playerShip
     * @param
     * @param
     * @return
     */
    @Override
    public boolean PlaceShipPlayer1(BattleShip playerShip) throws IOException {
      //  BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String cords=null;
        if(playerShip==playerShip1){
            cords=ship1;
        }
        else if(playerShip==playerShip2){
            cords=ship2;
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

    @Override
    public boolean PlaceShipPlayer2(BattleShip playerShip) throws IOException {
        String cords=null;
        if(playerShip==playerShip3){
            cords=ship3;
        }
        else if(playerShip==playerShip4){
            cords=ship4;
        }

        int x=Integer.parseInt(String.valueOf(cords.charAt(0)));
        int y = Integer.parseInt(String.valueOf(cords.charAt(2)));

        if (validPlacementPoint(x, y)) {
            int length = playerShip.shipLength;
            if (!playerShip.horizontal) {
                for (int i = y; i < y + length; i++) {
                    enemyBoard[x][i] = ship;
                    if (playerShip == playerShip3) {

                        playerShip3.pairList.add(new Pair(x, i));
                    } else {
                        playerShip4.pairList.add(new Pair(x, i));
                    }

                }
            } else {
                for (int i = x; i < x + length; i++) {
                    enemyBoard[i][y] = ship;
                    if (playerShip == playerShip3) {
                        //      BattleShip.MyClass pair=new BattleShip.MyClass(x,1);
                        playerShip3.pairList.add(new Pair(i, y));
                    } else {
                        //       BattleShip.MyClass pair=new BattleShip.MyClass(x,1);
                        playerShip4.pairList.add(new Pair(i, y));
                    }

                }

            }
            return true;
        }
        return false;
    }

    @Override
    public void shipCoordsPlayer1(String a, String b) {
        ship1=a;
        ship2=b;
        System.out.println(a+"|"+b);
    }

    @Override
    public void shipCoordsPlayer2(String a, String b) {
        ship3=a;
        ship4=b;
        System.out.println(a+"|"+b);
    }

    @Override
    public void getPlayer(playerInterface player)throws IOException {
        players.add(player);
        System.out.println(player.getName() +" has connected");
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

    @Override
    public void broadcastMessage( String message) throws RemoteException {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).sendMessageToClient(message);
        }
    }

    @Override
    public String sendMsg(String msg) {
        return msg;
    }



//
//    public void init(){
//        board.PlaceShip(board.playerShip1);
//        board.PlaceShip(board.playerShip2);
//    }

    /**
     * Server initiated and starts the game
     *
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        gameServerInterface server=new game();
        //gameServerInterface stub=(gameServerInterface) UnicastRemoteObject.exportObject(server,0);
        Registry registry= LocateRegistry.getRegistry(3000);
        registry.rebind("Hello",server);
        System.out.println("server bounded in registry");

        ((game) server).printBoards();
        while(players.size()<2){
            sleep(10000);
        }
        server.PlaceShipPlayer1(((game) server).playerShip1);
        server.PlaceShipPlayer1(((game) server).playerShip2);
        server.PlaceShipPlayer2(((game) server).playerShip3);
        server.PlaceShipPlayer2(((game) server).playerShip4);
        ((game) server).printBoards();











//        gameServerInterface board=new gameServer();
//
//        ServerSocket sersock = new ServerSocket(Integer.parseInt(args[0]));
//        System.out.println("Server  ready for receiving");
//        Socket sock = sersock.accept();
//        // reading from keyboard (keyRead object)
//        BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
//        // sending to client (pwrite object)
//        OutputStream ostream = sock.getOutputStream();
//        PrintWriter pwrite = new PrintWriter(ostream, true);
//
//        // receiving from server ( receiveRead  object)
//        InputStream istream = sock.getInputStream();
//        BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));
//
//        String receiveMessage, sendMessage;
//        receiveMessage = receiveRead.readLine();
//        System.out.println("Opponent's name is:"+receiveMessage);
//        System.out.println("Send Him Your Name:");
//        sendMessage = keyRead.readLine();
//        pwrite.println(sendMessage);
//        pwrite.flush();
//
//
//        board.printBoards();
//        while (true) {
//            receiveMessage = receiveRead.readLine();
//            System.out.println("reading......"+receiveMessage);
//            {
//                if (receiveMessage.equals("1")) {
//                    System.out.println("I scored a hit on the enemy\n");
//                    board.enemyBoard[board.myLastx][board.myLasty] = 'H';
//                    board.printBoards();
//                } else if (receiveMessage.equals("0")) {
//                    System.out.println("I missed the enemy ship");
//                    board.enemyBoard[board.myLastx][board.myLasty] = 'M';
//                    board.printBoards();
//                } else if (receiveMessage.length() > 2) {
//                    System.out.println("Enemy attacks at" + receiveMessage);
//                    // System.out.println(receiveMessage);
//                    sleep(100);
//                    board.enemyMove(receiveMessage);
//                    if (board.strike) {
//                        pwrite.println(1);
//                        pwrite.flush();
//                    } else {
//                        pwrite.println(0);
//                        pwrite.flush();
//                    }
//                    board.printBoards();
//                    if(board.lost)
//                    {
//                        pwrite.println("-1,-1");
//                        pwrite.flush();
//                        board.refreshGame();
//
//                    }
//                    if(board.won){
//                        board.refreshGame();
//                    }
//                    board.MyMove();
//                    pwrite.println("" + board.myLastx + "," + board.myLasty);
//                    pwrite.flush();
//                    board.printBoards();
//                }
//            }
//        }

    }


}
