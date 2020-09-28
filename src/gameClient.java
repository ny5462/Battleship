///**
// * ClientFile which first sends name and then
// * co-ordinates in the battleship game
// *
// * @author -Nikhil Yadav
// */
//
//import java.io.*;
//import java.net.Socket;
//
///***
// * This class creates Sockets And via TCP handshake
// * transmits and receives messages and manipulates the batlleship game
// */
//public class gameClient {
//    public static void main(String[] args) throws IOException {
//        Board opponent = new Board();
//        opponent.PlaceShip(opponent.playerShip1);
//        opponent.PlaceShip(opponent.playerShip2);
//        opponent.printBoards();
//
//        Socket sock = new Socket(args[0], Integer.parseInt(args[1]));
//        // reading from keyboard (keyRead object)
//        BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
//        // sending to client (pwrite object)
//        OutputStream ostream = sock.getOutputStream();
//        PrintWriter pwrite = new PrintWriter(ostream, true);
//
//        // receiving from server ( receiveRead  object)
//        InputStream istream = sock.getInputStream();
//        BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));
//        String receiveMessage, sendMessage;
//        System.out.println("Send Him your Name:");
//        sendMessage = keyRead.readLine();
//        pwrite.println(sendMessage);
//        pwrite.flush();
//
//
//        receiveMessage = receiveRead.readLine();
//        System.out.println("Opponent's name is "+receiveMessage);
//
//
//        System.out.println("client Move");
//        opponent.MyMove();
//        String s = "" + opponent.myLastx + "," + opponent.myLasty;
//        pwrite.println(s);
//        pwrite.flush();
//        opponent.printBoards();
//        while (true) {
//            receiveMessage = receiveRead.readLine();
//            System.out.println("Reading ......"+receiveMessage);//receive from server
//            {
//               if(receiveMessage.equals("0")){
//                   System.out.println("I Missed the Enemy's ship");
//                   opponent.enemyBoard[opponent.myLastx][opponent.myLasty]='M';
//                   opponent.printBoards();
//               }
//               else if(receiveMessage.equals("1")){
//                   System.out.println("I Scored a hit on enemy's ship");
//                   opponent.enemyBoard[opponent.myLastx][opponent.myLasty]='H';
//                   opponent.printBoards();
//               }
//               else if(receiveMessage.length()>2){
//                   opponent.enemyMove(receiveMessage);
//                   if(opponent.strike){
//                       pwrite.println(1);
//                       pwrite.flush();
//                   }else{
//                       pwrite.println(0);
//                       pwrite.flush();
//                   }
//                   opponent.printBoards();
//                   if(opponent.lost)
//                   {
//                       pwrite.println("-1,-1");
//                       pwrite.flush();
//                       opponent.refreshGame();
//
//                   }
//                   if(opponent.won){
//                       opponent.refreshGame();
//                   }
//                   opponent.MyMove();
//                   pwrite.println("" + opponent.myLastx + ","+opponent.myLasty);
//                   pwrite.flush();
//                   opponent.printBoards();
//               }
//            }
//        }
//    }
//}