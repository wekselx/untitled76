package ua.New.chat.network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class TCPConections {

    private final Socket socket;
    private final Thread rxThread;
    private final BufferedReader in;
    private final BufferedWriter out;
   private final TCPConectionListener evenListener;
   public TCPConections(TCPConectionListener evenListener, String ipAdr, int port) throws IOException{
this(evenListener,new Socket(ipAdr,port));
   }
    public TCPConections(TCPConectionListener evenListener ,Socket socket) throws IOException{
            this.evenListener= evenListener;
            this.socket=socket;
        in=new BufferedReader(new InputStreamReader(socket.getInputStream(),Charset.forName("UTF-8")));
        out=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),Charset.forName("UTF-8")));
        rxThread=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    evenListener.onConectionReady(TCPConections.this);
               while (!rxThread.isInterrupted()){
                   evenListener.onReceiveString(TCPConections.this,in.readLine());
               }
            }catch (IOException e){
                    evenListener.onExeption(TCPConections.this,e);


                }finally {
                evenListener.onDisconect(TCPConections.this);

                }
                }
        });
        rxThread.start();
    }
    public synchronized void sendString(String  value){
        try {
            out.write(value +"\r\n");
            out.flush();
        } catch (IOException e) {
            evenListener.onExeption(TCPConections.this,e);
             disconect();
        }

    }
    public synchronized void disconect(){
        rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            evenListener.onExeption(TCPConections.this,e);
 disconect();
        }

    }

    @Override
    public String toString() {
        return "TCPConection:"+socket.getInetAddress()+": "+socket.getPort();
    }
}
