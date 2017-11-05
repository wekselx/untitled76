package ua.New.chat.server;

import ua.New.chat.network.TCPConectionListener;
import ua.New.chat.network.TCPConections;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class CharServer implements TCPConectionListener {
    public static void main(String[] args) {
    new CharServer();
    }
    private  final ArrayList<TCPConections> conections=new ArrayList<>();
    private CharServer(){
        System.out.println("server running...");
       try( ServerSocket serverSocket =new ServerSocket(813)){
          while (true){
              try  {
           new TCPConections(this, serverSocket.accept());

          }catch (IOException e) {
                  System.out.println("TCPConectein exeption:" + e);
              }
       }

    } catch (IOException e) {
           throw new  RuntimeException(e);
       }
    }

    @Override
    public synchronized void onConectionReady(TCPConections tcpConections) {
conections.add(tcpConections);
sendALLConections("client connected:"+tcpConections);
    }

    @Override
    public synchronized void onReceiveString(TCPConections tcpConections, String value) {
     sendALLConections(value);
    }

    @Override
    public synchronized void onDisconect(TCPConections tcpConections) {
conections.remove(tcpConections);
        sendALLConections("client disconnected:"+tcpConections);
    }

    @Override
    public synchronized void onExeption(TCPConections tcpConections, Exception e) {
        System.out.println("TCPConection exeption:"+ e);
    }
    private void sendALLConections(String value){
        System.out.println(value);
         final int cnt=conections.size();
        for (int i = 0; i <cnt ; i++) conections.get(i).sendString(value);

            
        }
    }

