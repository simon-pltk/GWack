import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.*;
public class GWackClientGUI extends JFrame {

    private static String username;
    private static String Ipaddress;
    private static String port;

    public GWackClientGUI() {
        super();

        this.setTitle("GWack -- GW Slack Simulator");

        JPanel wholePanel = new JPanel(new BorderLayout(10, 3));

        JPanel serverSpecs = new JPanel(new FlowLayout()); // This is gonna be put on the top of the borderlayout
        JPanel membersPanel = new JPanel(new BorderLayout()); // This is going to handel the member label and textArea
        JPanel messagesPanel = new JPanel(new BorderLayout()); // This is going to handel the messages and compose labels, textAreas, and the Send button

        // Adding spec components

        JLabel name = new JLabel("Name");
        JTextField nameField = new JTextField(13);
        JLabel IPAddress = new JLabel("IP Address");
        JTextField addressField = new JTextField(13);
        JLabel portLabel = new JLabel("Port");
        JTextField portField = new JTextField(8);
        JButton connectionButton = new JButton("Connect");
        

        serverSpecs.add(name);
        serverSpecs.add(nameField);
        serverSpecs.add(IPAddress);
        serverSpecs.add(addressField);
        serverSpecs.add(portLabel);
        serverSpecs.add(portField);
        serverSpecs.add(connectionButton);

        // Adding member components
        JLabel memberJLabel = new JLabel("Members Online");
        JTextArea memberTextArea = new JTextArea(20,13);
        memberTextArea.setEditable(false);

        membersPanel.add(memberJLabel, BorderLayout.NORTH);
        membersPanel.add(memberTextArea, BorderLayout.CENTER);

        

        // Adding message components
        JLabel messageLabel = new JLabel("Messages");
        JTextArea messageTextArea = new JTextArea(10, 20);
        messageTextArea.setEditable(false);

        JPanel sendPanel = new JPanel(new BorderLayout());

        JLabel composLabel = new JLabel("Compose");
        JTextArea inputTextArea = new JTextArea(5, 20);
        JButton sendButton = new JButton("Send");


        inputTextArea.setLineWrap(true);
        

        sendPanel.add(composLabel, BorderLayout.NORTH);
        sendPanel.add(inputTextArea, BorderLayout.CENTER);
        sendPanel.add(sendButton, BorderLayout.SOUTH);

        messagesPanel.add(messageLabel, BorderLayout.NORTH);
        messagesPanel.add(messageTextArea, BorderLayout.CENTER);
        messagesPanel.add(sendPanel, BorderLayout.SOUTH);



        // Adding functionality 

        connectionButton.addActionListener((e) -> {
            if(connectionButton.getText().equals("Connect")){
                connectionButton.setText("Disconnect");
                nameField.setEditable(false);
                addressField.setEditable(false);
                portField.setEditable(false);

                username = nameField.getText();
                Ipaddress = addressField.getText();
                port = portField.getText();
            } else {
                connectionButton.setText("Connect");
                nameField.setEditable(true);
                addressField.setEditable(true);
                portField.setEditable(true);
            }
        });



        


        wholePanel.add(serverSpecs, BorderLayout.NORTH);
        wholePanel.add(membersPanel, BorderLayout.WEST);
        wholePanel.add(messagesPanel, BorderLayout.CENTER);
        this.add(wholePanel);
        this.setSize(800,400);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void main(String[] args) {
        GWackClientGUI GWCG = new GWackClientGUI();
        GWCG.setVisible(true);
    }
}


        /*
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JLabel messageLabel = new JLabel("Messages");
        JTextArea messageTextArea = new JTextArea(5, 5);
        messageTextArea.setEditable(false);
        topPanel.add(messageLabel, BorderLayout.NORTH);
        topPanel.add(messageTextArea, BorderLayout.CENTER);


        messagesPanel.add(topPanel, BorderLayout.CENTER);
        */
