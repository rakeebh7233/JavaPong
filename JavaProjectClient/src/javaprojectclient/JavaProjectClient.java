/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javaprojectclient;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;
import java.util.*;

/**
 *
 * @author Nihal Rahman and Rakeeb Hossain
 */

public class JavaProjectClient{
    static Socket sock;
    static Scanner sin;
    static PrintWriter sout;
    static Thread t = new Thread();
    static WaitingRoom wr;
    static int playerID;
    static String userName;
    static ArrayList<String[]> leaderBoardData = new ArrayList<>();
    
    public static void main(String[] args) {
        new LoginInterface();
    }
}

class LoginInterface{
    static JFrame jf = new JFrame();
    static TextField user = new TextField();
    static TextField server = new TextField();
    
    LoginInterface(){
        jf = new JFrame("Login Screen");
        JPanel jp = new JPanel();
        jp.setLayout(new GridLayout(4, 2, 4,4));
        
        JLabel userLabel = new JLabel("Username:");
        JLabel serverLabel = new JLabel("Server:");
        Button submit = new Button("Submit");
        submit.addActionListener(new ButtonPressed());
        
        jp.add(userLabel);
        jp.add(user);
        jp.add(serverLabel);
        jp.add(server);
        
        jp.add(submit);
        
        jf.add(jp);
        jf.setSize(500,500);
        jf.setVisible(true);
        
    }
}
// Submit Button for Login
// Reads in Users Win/Loss data from DB & Leaderboard data
// Starts waiting room
class ButtonPressed implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent e){  
        String username = LoginInterface.user.getText();
        String server = LoginInterface.server.getText();
        // Checks for Server
        if(server != null){
            try{
                JavaProjectClient.sock = new Socket(server,5190);
                JavaProjectClient.sin = new Scanner(JavaProjectClient.sock.getInputStream());
                JavaProjectClient.sout = new PrintWriter(JavaProjectClient.sock.getOutputStream(), true);
            } catch (IOException ex){
            System.out.println("Sorry, Connection failed.");
            }
        }

        JavaProjectClient.sout.println(username);
        LoginInterface.jf.setVisible(false);
        String user = "";
        String wins = "";
        String loss = "";
        int counter = 0;
        try{
            int max = JavaProjectClient.sin.nextInt();
            JavaProjectClient.sin.nextLine();
            while(JavaProjectClient.sin.hasNext()){
                if(counter == 0){
                   user = JavaProjectClient.sin.nextLine();
                }
                else if(counter == 1){
                   wins = JavaProjectClient.sin.nextLine();
                }
                else if(counter == 2){
                   loss = JavaProjectClient.sin.nextLine();
                }
                else if(counter == 3){
                    JavaProjectClient.playerID = Integer.parseInt(JavaProjectClient.sin.nextLine());
                }
                else if (counter>3 && counter<4+max){
                    String user1 = JavaProjectClient.sin.nextLine();
                    String wins1 = JavaProjectClient.sin.nextLine();
                    String loss1 = JavaProjectClient.sin.nextLine();
                    String[] data = {user1,wins1,loss1};
                    JavaProjectClient.leaderBoardData.add(data);
                    if (counter==max+3) {
                        break;
                    } 
                }
                counter += 1;
            }
        } catch(Exception e1){
            System.out.print(e1);
        }
        
        new WaitingRoom(user, wins, loss);

        checkReady isReady = new checkReady();
        JavaProjectClient.t = new Thread(isReady);
        JavaProjectClient.t.start();

    }
}

// Waiting room GUI for Client 1, while no client 2
class WaitingRoom{
    static JFrame jf = new JFrame("Waiting Room");
    
    WaitingRoom(String username, String numWins, String numLoss){
        JavaProjectClient.userName = username;
        JPanel jp = new JPanel();
        jp.setLayout(new GridLayout(4, 1, 4,4));
        JLabel message = new JLabel("Waiting for another user to connect...");
        JLabel user = new JLabel("Username: " + username);
        JLabel wins = new JLabel("Number of Wins: " + numWins);
        JLabel loss = new JLabel("Number of Losses: " + numLoss);
        
        jp.add(message);
        jp.add(user);
        jp.add(wins);
        jp.add(loss);
        
        jf.add(jp);
        jf.setSize(500,500);
        jf.setVisible(true);
    }       
}
// Checks for "ready" broadcast and closes waitingroom
class checkReady implements Runnable{
    @Override
    public void run(){
        String messages;
        try{
            while(JavaProjectClient.sin.hasNext()){
                if(JavaProjectClient.sin.nextLine().equals("Ready")){
                    JavaProjectClient.t.interrupt();
                    JavaProjectClient.wr.jf.setVisible(false);
                    
                    new GameGUI();
                }
            }
        } catch(Exception e){}
    }
}


