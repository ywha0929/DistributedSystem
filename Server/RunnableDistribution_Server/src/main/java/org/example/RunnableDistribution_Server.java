package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class RunnableDistribution_Server {
    public RunnableDistribution_Server() throws IOException {
    }

    public static void main(String[] args) throws IOException {

        System.out.println("Hello world!");
        int port = 21234;


        Thread serverThread = new ServerThread(port);
        serverThread.start();
    }




}

class ServerThread extends Thread {
    static int numThread = 8;
    Socket socket;

    ThreadController threadController;

    InputStream inputStream;
    OutputStream  outputStream;
    byte[] buffer;
    public ServerThread(int port) throws IOException {
        socket = new ServerSocket(port).accept();
        threadController = ThreadController.getInstance();
        threadController.setNumThread(numThread);
        buffer = new byte[2000];
    }
    @Override
    public void run() {
        try {
            inputStream = socket.getInputStream();
            outputStream =  socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true)
        {
            try {
                if(inputStream.available() == -1)
                    continue;
                else
                {
                    inputStream.read(buffer);
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    String msg = objectInputStream.readUTF();
                    if(msg.equals("getServerIdleIndex"))
                    {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                        objectOutputStream.writeInt(threadController.getIdleThreadIndex());
                        outputStream.write(byteArrayOutputStream.toByteArray());
                    }
                    else if(msg.equals("DistributeCalculation"))
                    {
                        while(inputStream.available() == -1);
                        inputStream.read(buffer);
                        ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(buffer);
                        objectInputStream = new ObjectInputStream(byteArrayInputStream2);
                        Operands operands = (Operands) objectInputStream.readObject();
                        Thread thisThread = new Thread(new DistributableRunnable(threadController,threadController.getIdleThreadIndex(),operands, outputStream));
                        thisThread.start();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}