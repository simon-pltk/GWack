import java.io.BufferedReader;
import java.util.ArrayList;

public class ReadingThread extends Thread {
    private BufferedReader buffRead;
    private GWackClientGUI gwack;
    private boolean adding = false;
    private ArrayList<String> members = new ArrayList<String>();
    private String messages = "";

    public ReadingThread(BufferedReader br, GWackClientGUI gw){
        buffRead = br;
        gwack = gw;
    }

    public void run(){
        while(true){
            try {
                String line = buffRead.readLine();

                if(line.equals("END_CLIENT_LIST")){
                    adding = false;

                    String clients = "";
                    for(String mem : members){
                        clients = clients + mem +"\n";
                    }

                    gwack.memberTextArea.setText(clients);
                    members.clear();
                }

                if(adding){
                    members.add(line);
                } else if(!(line.equals("END_CLIENT_LIST") || line.equals("START_CLIENT_LIST"))) {
                    messages += line + "\n";
                    gwack.getDisplayTextArea().setText(messages);
                }

                if(line.equals("START_CLIENT_LIST")){
                    adding = true;
                }
                

                
                
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("Stopped reading");
                break;
            }
        }   
    }


}
