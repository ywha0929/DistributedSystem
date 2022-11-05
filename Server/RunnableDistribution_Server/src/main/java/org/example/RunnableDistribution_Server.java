package org.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class RunnableDistribution_Server {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
    int port = 21234;

}

class ServerThread extends Thread {
    static int numThread = 8;
    Socket socket;
    ObjectInputStream objectInputStream;
    ThreadController threadController;
    ObjectOutputStream objectOutputStream;
    public ServerThread(int port) throws IOException {
        socket = new ServerSocket(port).accept();
        threadController = ThreadController.getInstance();
        threadController.setNumThread(numThread);
    }
    @Override
    public void run() {
        try {
            objectInputStream= (ObjectInputStream) socket.getInputStream();
            objectOutputStream = (ObjectOutputStream) socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true)
        {
            try {
                if(objectInputStream.available() == -1)
                    continue;
                else
                {
                    String msg = objectInputStream.readUTF();
                    if(msg.equals("getServerIdleIndex"))
                    {
                        objectOutputStream.writeInt(threadController.getIdleThreadIndex());
                    }
                    else if(msg.equals("DistributeCalculation"))
                    {
                        while(objectInputStream.available() == -1);
                        Operands operands = (Operands) objectInputStream.readObject();
                        Thread thisThread = new Thread(new DistributableRunnable(threadController,threadController.getIdleThreadIndex(),operands, objectOutputStream));
                        thisThread.start();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}