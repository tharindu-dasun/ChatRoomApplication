package lk.ijse.gdse.utill;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class ChatServer {
    public static final int PORT = 9001;
    private static HashSet<String> names = new HashSet<String>();
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

    public static void main(String[] args) throws Exception {   //service eka request karanakn main method eka lisent karanawa
        System.out.println("The chat server is running");
        ServerSocket listner = new ServerSocket(PORT);

        try {    //client gen request ekak enakn wenna ona tika try catch eka athule danawa
            while (true) {
                Socket socket = listner.accept();
                Thread handlerThread = new Thread(new Handler(socket));
                handlerThread.start();
            }
        }finally {
            listner.close();
        }
    }

    //Handler kiyana thred class eka Single client kenek ekka eyage brodcast msg handle karanawa
    private static class Handler implements Runnable{
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        //Handler ge constructor eka
        public Handler(Socket socket){
            this.socket=socket;
        }

        //implement the run method
        public void run(){
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());

                while (true){
                    System.out.println("Submit Name");
                    name = in.readLine();

                    if (name == null){
                        return;     // kalin dapu name ekaknam return wenawa
                    }
                    if (!names.contains(names)) {
                        names.add(name);  // hashset eke name eka naththam add karanna
                            break;
                    }
                }
                System.out.println("Name Added !");
                writers.add(out);

                while (true){
                    String input  = in.readLine();
                    if (input == null){
                        return;
                    }
                    for (PrintWriter writer : writers){
                        writer.println("Message" + name +":" + input);
                    }
                }
            }catch (IOException e){
                System.out.println(e);
                //Client chat eken ain weddi names , writers , socket okkoma close wenna one

            }finally {
                if (names != null){
                    names.remove(name);
                }
                if (out != null){
                    writers.remove(out);
                }
                try {
                    socket.close();
                }catch (IOException e){

                }
            }
        }
    }
}
