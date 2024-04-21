import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    public Socket newSocket;
    private GWackChannel HostServer;

    public boolean valid = true;
    public String username = "INVALID";

    private PrintWriter out;
    private BufferedReader in;

    public ClientThread(Socket client, GWackChannel server) {
        newSocket = client;
        HostServer = server;
        try {
            out = new PrintWriter(newSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(newSocket.getInputStream()));
        } catch (Exception e) {
            System.out.println("UH oh");
        }
    }

    public void sendMsg(String msg){
        out.println(msg);
        out.flush();
    }

    public void run() {

        try {
            String handshake = in.readLine();

            if(!(handshake.equals("SECRET"))){
                this.valid = false;
                HostServer.removeClients();
                newSocket.close();
                System.out.print("No secret");
            }

            handshake = in.readLine();
            if(!(handshake.equals("3c3c4ac618656ae32b7f3431e75f7b26b1a14a87"))){
                this.valid = false;
                HostServer.removeClients();
                newSocket.close();
                System.out.print("No code");
            }

            handshake = in.readLine();
            if(!(handshake.equals("NAME"))) {
                this.valid = false;
                HostServer.removeClients();
                newSocket.close();
                System.out.print("No Name");
            } else {
                username = in.readLine();
            }
            // TODO: ENQUE THE CLIENT MESSAGE SO THAT EVERYONE CAN SEE IT

            HostServer.enqueue(HostServer.getClientList());
            HostServer.broadCast();

            while(true) {
                String msg = in.readLine();

                if(msg == null) {
                    this.valid = false;
                    HostServer.removeClients();
                    newSocket.close();
                    break;
                }

                if(msg.equals("LOGOUT")) { 
                    this.valid = false;
                    HostServer.removeClients();
                    HostServer.enqueue(HostServer.getClientList());
                    HostServer.broadCast();

                    newSocket.close();
                    break;
                } else {
                    String output = "[" + username + "]" + " "+ msg;
                    System.out.println(output);
                    HostServer.enqueue(output);
                    HostServer.broadCast();
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
