package lk.ijse.gdse.utill;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {

    BufferedReader in;
    PrintWriter out;

    //02.swing bass application ekak nisa frame ekak use krnawa.
    //fream eken balaganna puluwan naththan visible wen na

    JFrame frame = new JFrame("Chatter App");
    JTextField textField = new JTextField(40);
    JTextArea messageArea = new JTextArea(8, 40);

    //03.create chat client's constructor
    public ChatClient(){
        //04.Graffical interface eka layout karaganna one
        // false --- server eken client ge nama approve karanakn meka edit k bari wenna ona hinda
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField,"North");
        frame.getContentPane().add(new JScrollPane(messageArea));
        frame.pack();

        //05.ActionListner --- Enter eka dunama msg eka send wela bar eka clear wenna one
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });
    }
    //06.get server address
    //OptionPane ekakin thama mona server address ekakatada connect wenna one kiyala ahanne
    private String getServerAddress(){
        return JOptionPane.showInputDialog(
                frame,
                "Enter the IP Address of the Server :",
                "Welcome the Chatter App !",
                JOptionPane.QUESTION_MESSAGE
        );
    }

    private String getName(){
        return JOptionPane.showInputDialog(
                frame,
                "Choose Screen Name :",
                "Screen Name Selection",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    public void run() throws IOException {
        String serverAddress = getServerAddress();
        Socket socket = new Socket(serverAddress, 9001);

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(),true);

        while (true){
            String line = in.readLine();
            if (line.startsWith("SUBMITNAME")){
                out.println(getName());
            } else if (line.startsWith("NAMEACCEPTED")) {
                textField.setEditable(true);
            } else if (line.startsWith("MESSAGE")) {
                messageArea.append(line.substring(8) + "\n");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
}
