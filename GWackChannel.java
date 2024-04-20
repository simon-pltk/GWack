import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class GWackChannel {
   private int portnum;
   public ServerSocket serverSock;
   public static volatile ArrayList<ClientThread> connectedClients = new ArrayList<ClientThread>();
   public static volatile Queue<String> outputQueue = new LinkedList<String>();

   public GWackChannel(int port){
        portnum = port;

        try {
            serverSock = new ServerSocket(portnum);
        } catch (Exception e) {
            System.out.println("Could not make a server socket");
            System.exit(1);
        }
    }

    public void serve(int num) {
        if(num != -1) {
            for(int i = 0; i < num; i++){
                try {
                    Socket clientSock = serverSock.accept();
                    System.out.println("New connection: "+clientSock.getRemoteSocketAddress());
                    
                    ClientThread ct = new ClientThread(clientSock, this);
                    addClient(ct);
                    ct.start();
                } catch (Exception e) {
                    System.out.println("Connection failed");
                    System.exit(1);
                }
            }
        } else {
            while(true){

                try {
                    Socket clientSock = serverSock.accept();
                    
                    ClientThread ct = new ClientThread(clientSock, this);
                    addClient(ct);
                    ct.start();
                } catch (Exception e) {
                    System.out.println("Connection failed");
                    System.exit(1);
                }
            }
        }
    }

    public synchronized void addClient(ClientThread ct) {
        connectedClients.add(ct);
    }

    public ServerSocket getServerSocket() {
        return serverSock;
    }

    public ArrayList getConnectedClients() {
        return connectedClients;
    }

    public Queue getOutputQueue(){
        return outputQueue;
    }

    public synchronized void removeClients() {
        ArrayList<ClientThread> remove = new ArrayList<ClientThread>();
        for(ClientThread thread : connectedClients){
            if(thread.valid != true) {
                remove.add(thread);
            }
        }

        connectedClients.removeAll(remove);
    }
    public synchronized String getClientList(){ //TODO: FINISH THIS
        String output = "START_CLIENT_LIST\n";

        for(ClientThread client : connectedClients) {
            output += client.username + "\n";
        }

        output += "END_CLIENT_LIST";

        return output;
    }

    public synchronized void enqueue(String bruh){
        outputQueue.add(bruh);
    }

    public synchronized void broadCast(){
        while(outputQueue.size() > 0) {
            for(ClientThread ct : connectedClients){
                ct.sendMsg(outputQueue.remove());
            } 
        }
    }
    

    public static void main(String[] args) {
        if(args.length != 1){
            System.out.println("no args");
        } else {
            int port = Integer.parseInt(args[0]);
            GWackChannel gwc = new GWackChannel(port);
            gwc.serve(-1);
        }
    }
    
}

/*
 * public static volatile ArrayList<Socket> connectedClients = new ArrayList<Socket>();
    private int portnum;
    public ServerSocket serversock;

    public GWackChannel(int port) {
        portnum = port;

        try {
            serversock = new ServerSocket(portnum);
        } catch (Exception e) {
            System.out.println("Could not make a server socket");
            System.exit(1);
        }
    }

    public void serve() {

        while(true){
            try {
                Socket clientSocket = serversock.accept();
                connectedClients.add(clientSocket);

                (new ClientThread(clientSocket)).start();
            } catch (Exception e) {
                System.out.println("Connection failed");
                System.exit(1);
            }
        }
    }

    public ServerSocket getServerSocket() {
        return serversock;
    }

    public ArrayList getConnectedClients() {
        return connectedClients;
    }
 */
