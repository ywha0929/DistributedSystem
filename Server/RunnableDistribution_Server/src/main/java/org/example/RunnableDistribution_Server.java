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
    static int numThread = 8;
    static int i = 1;
    public static void main(String[] args) throws IOException {
        numThread = Integer.parseInt(args[1]);
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

//        while (true)
//        {
        socket = serverSocket.accept();
            System.err.println("got request");
//            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
//            dataOutputStream.writeInt(port + i);
//            dataOutputStream.flush();

            Thread thread = new ServerThread(socket);
            thread.setPriority(7);
            thread.start();
            i++;
//        }



    }




}

class ServerThread extends Thread {

    Socket socket;

    ThreadController threadController;

    InputStream inputStream;
    OutputStream  outputStream;
    MessageQueue msgQ;
    int port;
//    byte[] buffer;
    public ServerThread(Socket socket) throws IOException {
        this.port = socket.getLocalPort();
//        ServerSocket serverSocket = new ServerSocket(port);
//        this.socket = serverSocket.accept();
        this.socket = socket;
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
            System.err.println(port + " : Server Loop");
            try {
                while(inputStream.available() < 0);


//                buffer = new byte[10];
//                System.out.println("read bytes : "+inputStream.read(buffer));
//                    for(int j = 0; j< buffer.length; j++)
//                    {
//                        System.out.print(buffer[j]);
//                    }
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                int mode = dataInputStream.readInt();
                if(mode == 1)
                {
                    System.err.println(port + " : got message 1 from client");
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
//                    Message msg = new Message(threadController.getIdleThreadIndex());
//                    msgQ.pushMessage(msg);
                    dataOutputStream.writeInt(1);
                    dataOutputStream.writeInt(threadController.getIdleThreadIndex());
                    dataOutputStream.flush();
                    //outputStream.write(byteArrayOutputStream.toByteArray());
                    System.err.println(port + " : send message to Client");
                }
                else if(mode == 2)
                {
                    System.err.println(port + " : got message 2 from client");
//                        while(inputStream.available() == -1);
//                        inputStream.read(buffer);
//                        System.out.println("got parameters");
//                        ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(buffer);
//                        objectInputStream = new ObjectInputStream(byteArrayInputStream2);
                    int  taskNum = dataInputStream.readInt();
                    System.err.println(port + " : got taskNum : "+taskNum);
                    byte[] operandByte = new byte[180];
                    dataInputStream.read(operandByte);
                    Operands operands = new Operands();
                    operands.fromByteArray(operandByte);

//                    for(int i= 0; i< operands.getSize(); i++)
//                    {
//                        System.out.print(operands.get(i) + " ");
//                    }
//                    System.out.println("");


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