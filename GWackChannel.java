import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class GWackChannel {
    private int portnum;
    public volatile ServerSocket serverSock;
    public volatile ArrayList<ClientThread> connectedClients = new ArrayList<ClientThread>(); // Potentially dont make volatile
    public volatile Queue<String> outputQueue = new LinkedList<String>(); // Potentially dont make volatile

    public GWackChannel(int port){
        portnum = port;

        try {
            serverSock = new ServerSocket(portnum);
        } catch (Exception e) {
            System.out.println("Could not make a server socket");
            System.exit(1);
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

    public synchronized void removeClients() { // Might cause problems
        ArrayList<ClientThread> remove = new ArrayList<ClientThread>();
        for(ClientThread thread : connectedClients){
            if(!thread.valid) {
                remove.add(thread);
            }
        }

        connectedClients.removeAll(remove);
    }

    public synchronized String getClientList(){ //Maybe make it synchronized
        String output = "START_CLIENT_LIST\n";

        for(ClientThread client : connectedClients) {
            output += client.username + "\n";
        }

        output += "END_CLIENT_LIST";

        return output;
    }

    
    public synchronized void enqueue(String input){
        outputQueue.add(input);
    }

    public synchronized void broadCast(){
        ArrayList<String> sending = new ArrayList<String>();

        while (outputQueue.size() > 0) {
            sending.add(outputQueue.remove());
        }

        for(String s : sending){
            for(ClientThread ct : connectedClients){
                ct.sendMsg(s);
            }
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
                    System.out.println("Connection failed FOR");
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
                    System.out.println("Connection failed INFINITE");
                    System.exit(1);
                }
            }
        }
    }

    public static void main(String[] args) {
        int portnum;
        if(args.length > 0) {
            portnum = Integer.parseInt(args[0]);
        } else {
            portnum = 2;
        }

        GWackChannel server = new GWackChannel(portnum);
        server.serve(-1);
    }
}

/*
    public synchronized void enqueue(String input){
        outputQueue.add(input);
    }

    public synchronized void broadCast(){
        while(outputQueue.size() > 0) {
            for(ClientThread ct : connectedClients){
                ct.sendMsg(outputQueue.remove()); // Maybe change sendMsg
            } 
        }
    }
    */
