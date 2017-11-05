package ua.New.chat.network;

public interface TCPConectionListener {

    void onConectionReady(TCPConections tcpConections);
    void onReceiveString(TCPConections tcpConections, String value);
    void onDisconect(TCPConections tcpConections);
    void onExeption(TCPConections tcpConections,Exception e);



}

