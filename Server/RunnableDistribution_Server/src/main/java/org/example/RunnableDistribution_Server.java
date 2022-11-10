package org.example;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class RunnableDistribution_Server {
    public RunnableDistribution_Server() throws IOException {
    }
    static ServerSocket serverSocket = null;
    static Socket socket = null;
    static int numThread = 4;
    static int i = 1;
    public static void main(String[] args) throws IOException {

        ThreadController.getInstance();
        ThreadController.getInstance().setNumThread(numThread);
        System.err.println("Hello world!");
        int port = 21234;
        try{
            serverSocket = new ServerSocket(port);
        } catch (Exception e )
        {
            e.printStackTrace();
        }
    while(true)
    {
        try{
            socket = serverSocket.accept();
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeInt(port + i);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        Thread thread = new ServerThread(port + i);
        thread.setPriority(7);
        thread.start();
        i++;
    }

    }




}

class ServerThread extends Thread {

    Socket socket;

    ThreadController threadController;

    InputStream inputStream;
    OutputStream  outputStream;
    MessageQueue msgQ;
    byte[] buffer;
    public ServerThread(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        this.socket = serverSocket.accept();
        threadController = ThreadController.getInstance();
        SendThread sendThread = new SendThread(socket);
        this.msgQ = sendThread.msgQ;
        sendThread.start();

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
            System.err.println("Server Loop");
            try {
                while(inputStream.available() < 0);


                buffer = new byte[10];
//                System.out.println("read bytes : "+inputStream.read(buffer));
//                    for(int j = 0; j< buffer.length; j++)
//                    {
//                        System.out.print(buffer[j]);
//                    }
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                int mode = dataInputStream.readInt();
                if(mode == 1)
                {
                    System.err.println("got message 1 from client");
//                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    Message msg = new Message(threadController.getIdleThreadIndex());
                    msgQ.pushMessage(msg);
//                    dataOutputStream.writeInt(1);
//                    dataOutputStream.writeInt(threadController.getIdleThreadIndex());
//                    dataOutputStream.flush();
                    //outputStream.write(byteArrayOutputStream.toByteArray());
                    System.err.println("send message to Client");
                }
                else if(mode == 2)
                {
                    System.err.println("got message 2 from client");
//                        while(inputStream.available() == -1);
//                        inputStream.read(buffer);
//                        System.out.println("got parameters");
//                        ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(buffer);
//                        objectInputStream = new ObjectInputStream(byteArrayInputStream2);
                    int  taskNum = dataInputStream.readInt();
                    System.err.println("got taskNum : "+taskNum);
                    byte[] operandByte = new byte[180];
                    dataInputStream.read(operandByte);
                    Operands operands = new Operands();
                    operands.fromByteArray(operandByte);
                    Runnable thisRunnable = new DistributableRunnable(threadController,threadController.getIdleThreadIndex(),operands, outputStream, taskNum, msgQ);
                    Thread thisThread = new Thread(thisRunnable);
                    threadController.setThread(thisThread,threadController.getIdleThreadIndex());
                    threadController.setRunnable(thisRunnable, threadController.getIdleThreadIndex());
                    threadController.useThread(threadController.getIdleThreadIndex());
                    thisThread.start();
                }


            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
//            for(int i = 0; i<numThread; i++)
//            {
//                Thread thisThread = threadController.getThread(i);
//                if(thisThread != null && thisThread.isAlive() == false)
//                {
//                    DistributableRunnable thisRunnable = (DistributableRunnable) threadController.getRunnable(i);
//                    int result = thisRunnable.getResult();
//
//
//                    try {
//                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
//                        dataOutputStream.writeInt(2);
//                        dataOutputStream.writeInt(result);
//                        dataOutputStream.flush();
////                        outputStream.write(byteArrayOutputStream.toByteArray());
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//
//                }
//            }
        }
    }
}