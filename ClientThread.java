import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    public Socket newSocket;
    public GWackChannel HostServer;

    public volatile boolean valid = true;
    public volatile boolean handshakeCheck = false;

    public String username = "TEST";
    private String line;

    public PrintWriter out;
    public BufferedReader in;
    
    
    public ClientThread(Socket client, GWackChannel server) {
        newSocket = client;
        HostServer = server;
    }

    public void sendMsg(String msg){
        out.println(msg);
        out.flush();
    }

    public void run() {

        try {
            out = new PrintWriter(newSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(newSocket.getInputStream()));

            line = in.readLine();
            if(!(line.equals("SECRET"))){
                this.valid = false;
                HostServer.removeClients();
                out.close();
                in.close();
                newSocket.close();
                System.out.print("No secret");
            }

            line = in.readLine();
            if(!(line.equals("3c3c4ac618656ae32b7f3431e75f7b26b1a14a87"))){
                this.valid = false;
                HostServer.removeClients();
                out.close();
                in.close();
                newSocket.close();
                System.out.print("No code");
            }

            line = in.readLine();
            if(!(line.equals("NAME"))) {
                this.valid = false;
                HostServer.removeClients();
                out.close();
                in.close();
                newSocket.close();
                System.out.print("No Name");
            } else {
                username = in.readLine();
                // HostServer.enqueue(HostServer.getClientList());
                // HostServer.broadCast();
            }

            while(true){
                line = in.readLine();

                System.out.println(line + " WHILE LOOOP");

                if(line == null) {
                    out.close();
                    in.close();
                    newSocket.close();
                    break;
                }

                if(line.equals("LOGOUT")) {
                    this.valid = false;
                    HostServer.removeClients();
                    out.println(HostServer.getClientList());
                    out.flush();

                    out.close();
                    in.close();
                    newSocket.close();
                    HostServer.broadCast();
                    break;
                } else if(!(line.equals("3c3c4ac618656ae32b7f3431e75f7b26b1a14a87")) && !(line.equals("NAME")) && !(line.equals("SECRET")) && !(line.equals(username))) {
                    String output = "[" + username + "]" + " "+ line;
                    HostServer.enqueue(output);
                    HostServer.broadCast();
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}
/*
    if(handshakeCheck) {
        this.valid = true;
    } else {
        this.valid = false;
    }

 * if(valid){
        // Maybe add the client here? Thing is then there would be no need for the getClientList
        // How do I detect changes to print out when a new member is added
    } else {
        HostServer.removeClients();
        out.println(HostServer.getClientList());
        out.flush();

        out.close();
        in.close();
        newSocket.close();
    }

 */

 /*
            String[] arguments = new String[4];

            for(int i = 0; i < arguments.length; i++){ // Check if this approach is OK
                arguments[i] = in.readLine();
            }

            
            if(arguments[0].equals("SECRET")) {
                if(arguments[1].equals("3c3c4ac618656ae32b7f3431e75f7b26b1a14a87")) {
                    if(arguments[2].equals("NAME")){
                        username = arguments[3];
                        handshakeCheck = true;
                        out.println(HostServer.getClientList());
                    }
                }
            }
            out.flush();

            if(!handshakeCheck) {
                this.valid = false;
                HostServer.removeClients();
                out.close();
                in.close();
                newSocket.close();
                return;
            }

            else if(!(line.equals("3c3c4ac618656ae32b7f3431e75f7b26b1a14a87")) && !(line.equals("NAME")) && !(line.equals("SECRET")) && !(line.equals(username))) {
                    String output = "[" + username + "]" + " "+ line;
                    HostServer.enqueue(output);
                    HostServer.broadCast();
                }
            */