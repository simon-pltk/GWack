import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ClientNetworking {

    private String ip;
    private int port;
    private String username;

    private PrintWriter pw;
    private BufferedReader br;
    private Socket sock;
    private GWackClientGUI gwack;

    public boolean isConnected;

    public ClientNetworking(String name, String address, int portnum, GWackClientGUI g) {
        username = name;
        ip = address;
        port = portnum;
        gwack = g;

        try {
            sock = new Socket(ip, port);

            pw = new PrintWriter(sock.getOutputStream());
            String handshake = "SECRET\n3c3c4ac618656ae32b7f3431e75f7b26b1a14a87\nNAME\n" + username;
            pw.println(handshake);
            pw.flush();

            br = new BufferedReader(new InputStreamReader(sock.getInputStream()));

            ReadingThread thread = new ReadingThread(br, gwack);

            thread.start();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(gwack, "You Cannot Connect", "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }


    }

    public Socket getSocket() {
        return sock;
    }

    public void writeMsg(String message) {
        pw.println(message);
        pw.flush();
    }

    public void disconnect(){
        try {
            pw.println("LOGOUT");
            pw.flush();

            pw.close();
            br.close();
            sock.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(gwack, "Something went wrong, closing application", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.close();
        }

        isConnected = false;
    }

    public PrintWriter getOut(){
        return pw;
    }

    public String getName(){
        return username;
    }

}
/*
 * try {
            sock = new Socket(IP, port);

            pw = new PrintWriter(sock.getOutputStream());
            pw.println("SECRET");
            pw.println("3c3c4ac618656ae32b7f3431e75f7b26b1a14a87");
            pw.println("NAME");
            pw.println(username);
            pw.flush();

        } catch (Exception e) {
            // Throw a you cannot connect message on the GUI and kill the aplication
        }
 */
