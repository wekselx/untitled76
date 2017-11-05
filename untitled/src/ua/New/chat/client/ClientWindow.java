package ua.New.chat.client;

import ua.New.chat.network.TCPConectionListener;
import ua.New.chat.network.TCPConections;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWindow extends JFrame implements ActionListener,TCPConectionListener{
    private static final String IP_ADDR="10.0.0.34";
    private static final int PORT=813;
    private static final int WIDTH=600;
    private static final int HEIGHT=400;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();
            }
        });

    }
    private final JTextArea log=new JTextArea();
private final JTextField fieldNickname=new JTextField("Weksel");
private final JTextField fieldInput=new JTextField();
    private TCPConections connections;
    private ClientWindow(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH,HEIGHT);

        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        log.setEditable(false);
        log.setLineWrap(true);

        add(log,BorderLayout.CENTER);

        fieldInput.addActionListener(this);

        add(fieldInput,BorderLayout.SOUTH );
        add(fieldNickname,BorderLayout.NORTH);




        setVisible(true);
        try {
            connections=new TCPConections(this,IP_ADDR,PORT);
        } catch (IOException e) {
            printMesage("Connection exeption:"+e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg=fieldInput.getText();
        if (msg.equals(""))return;
        fieldInput.setText(null);
        connections.sendString(fieldNickname.getText()+": "+ msg);

    }

    @Override
    public void onConectionReady(TCPConections tcpConections) {
  printMesage("Conection ready....");
    }

    @Override
    public void onReceiveString(TCPConections tcpConections, String value) {
printMesage(value);
    }

    @Override
    public void onDisconect(TCPConections tcpConections) {
        printMesage("Connection close:");
    }

    @Override
    public void onExeption(TCPConections tcpConections, Exception e) {
        printMesage("Connection exeption:"+e);

    }
    private synchronized void printMesage(String msg){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg+"\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
}
