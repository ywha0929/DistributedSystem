package org.example;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicBoolean;

public class RunnableDistribution_Client {
    static String[] ips;
    static int[] ports;
    static Socket[] sockets;
    static int numServers = 1;
    static int numThread = 4;
    static SendThread[] sendThreads;
    static ReadThread[] readThreads;
    //tatic Thread SendThread;
    public static void main(String[] args) {

        ips = new String[numServers];
        ports = new int[numServers];
        sockets = new Socket[numServers];
        ips[0] = "127.0.0.1";
//        Scanner sc = new Scanner(System.in);
        ips[0] = args[1];
        ports[0] = 21234;
        sendThreads = new SendThread[numServers];
        readThreads = new ReadThread[numServers];
//        SocketOutputThread[] SocketOutputThreads = new SocketOutputThread[numServers];

//        String nameTestFile = "../../TestFiles/TestFile.txt";
        String nameTestFile = args[2];
        File file = new File(nameTestFile);
        List<String> allLines;
        try {
            allLines = Files.readAllLines(Paths.get(nameTestFile));
        } catch(Exception e)
        {
            e.printStackTrace();
            return;
        }
        List<Operands> listOperands = new ArrayList<>();
        for (String line : allLines) {
            int numPerLine = line.length() / 4;
            Operands temp = new Operands();
            temp.setSize(numPerLine);
            StringTokenizer stringTokenizer = new StringTokenizer(line," ");

            for(int i = 0; i< numPerLine; i++)
            {
                String stringNumber = stringTokenizer.nextToken();
                //System.out.println(stringNumber);
                //Integer.parseInt(stringNumber);
                temp.pushOperand(Integer.parseInt(stringNumber));
            }
            listOperands.add(temp);
        }

        ThreadController threadController = ThreadController.getInstance();
        threadController.setNumThread(numThread);

        //initialize socket
        for(int i= 0; i<numServers; i++)
        {
            try{
                sockets[i] = new Socket(ips[i],ports[i]);
                sendThreads[i] = new SendThread(sockets[i]);
                sendThreads[i].start();
                readThreads[i] = new ReadThread(sockets[i]);
                readThreads[i].start();
//                SocketOutputThreads[i] = new SocketOutputThread(socket);

            } catch(Exception e)
            {
                e.printStackTrace();
            }
        }


        long beforeTime = System.currentTimeMillis();
        for(int i = 0; i<listOperands.size(); i++)
        {
            System.err.println("i : "+i);
            int index = threadController.getIdleThreadIndex();
            System.err.println("before getServerIdleThreadIndex");
            int serverIndex = getServerIdleThreadIndex();
            System.err.println("after getServerIdleThreadIndex : "+ serverIndex);
//            System.out.println("index : " + index);
            while(index == -1 && serverIndex==-1)
            {
                index = threadController.getIdleThreadIndex();
//                System.out.println("before getServerIdleThreadIndex");
                serverIndex = getServerIdleThreadIndex();
                System.err.println("after getServerIdleThreadIndex : "+ serverIndex);
            } //wait until idel thread is found
            if(index == -1)
            {
                int numServer = serverIndex / 1000;
                int finalI = i;
                System.err.println("send Calculation to Server : "+serverIndex);
                byte[] thisOperand = listOperands.get(i).toByteArray();
                Message msg = new Message(thisOperand);
                sendThreads[0].putMsg(msg);
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        try {
////                            Message msg = new Message(2);
////                            sendThreads[0].putMsg(msg);
////                            OutputStream outputStream =  sockets[numServer].getOutputStream();
////                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
////                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
////                            objectOutputStream.writeInt(2);
////                            objectOutputStream.flush();
////                            outputStream.write(byteArrayOutputStream.toByteArray());
//
////                            byteArrayOutputStream.reset();
////                            Message msg = new Message(listOperands.get(finalI).toByteArray());
//                            byte[] thisOperands= listOperands.get(finalI).toByteArray();
//                            Message msg = new Message(thisOperands);
//                            System.out.println("operands length : "+thisOperands.length);
//                            sendThreads[0].putMsg(msg);
////
////                            objectOutputStream.write(listOperands.get(finalI).toByteArray());
//////                            objectOutputStream.writeObject(listOperands.get(finalI));
////
////                            outputStream.write(byteArrayOutputStream.toByteArray());
////                            System.out.println(listOperands.get(finalI).toByteArray().length);
//                            InputStream inputStream = sockets[0].getInputStream();
//                            while(inputStream.available() == -1);
//                            DataInputStream dataInputStream = new DataInputStream(inputStream);
//                            int result = dataInputStream.readInt();
//                            System.out.println("answer from Server : "+result+", i : " + finalI);
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//
//                    }
//                });
//                thread.start();
            }
            else
            {
                System.err.println("start Calculation");
                threadController.useThread(index);
                Thread thisThread = new Thread(new DistributableRunnable(threadController,index, listOperands.get(i)));
                thisThread.start();
            }


        }
        long afterTime = System.currentTimeMillis();
        long secDiffTime = (afterTime - beforeTime)/1000;
        System.out.println("Execution Time : " +secDiffTime);

    }
//
//    static boolean isReceived = false;
//    static int received_data = 0;
    static int getServerIdleThreadIndex()
    {

        for(int i = 0; i<numServers; i++)
        {

            try{
                Message msg = new Message(1);
                sendThreads[i].putMsg(msg);
//                OutputStream outputStream =  sockets[i].getOutputStream();
//                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
//                objectOutputStream.writeInt(1); //getServerIdleIndex
//                objectOutputStream.flush();
//                outputStream.write(byteArrayOutputStream.toByteArray());
//                for(int j = 0; j< byteArrayOutputStream.toByteArray().length; j++)
//                {
//                    System.out.print(byteArrayOutputStream.toByteArray()[j]);
//                }
                //System.out.println(readThreads[i].index);
                while(!readThreads[i].isIndex.get())
                    for(int j = 0; j<10000; j++);


                System.err.println("returning");
                readThreads[i].isIndex.set(false);
//                readThreads[i].isCheck = true;
                return readThreads[i].index;

            }catch(Exception e){
                e.printStackTrace();
                return 100;
            }

        }
        return -1;
    }


}

class ReadThread extends Thread
{
    InputStream inputStream;
    AtomicBoolean isIndex = new AtomicBoolean();
    int index;
    byte[] buffer;
    boolean isCheck;
    public ReadThread(Socket socket) throws IOException {
        inputStream = socket.getInputStream();
        buffer = new byte[16];
        isIndex.set(false);
//        isCheck = true;
        index = 0;
    }

    @Override
    public void run() {
        while(true)
        {
            try{
                while(inputStream.available()<=0);
                System.err.println("msg from Server");
                DataInputStream dataInputStream = new DataInputStream(inputStream);
                int mode = dataInputStream.readInt();
                int other = dataInputStream.readInt();
                System.err.println("mode : "+mode+", other : "+other);
                if(mode == 1)
                {
//                    while(!isCheck)
//                        for(int i = 0; i< 10000; i++);
                    index = other;
                    isIndex.set(true);
//                    isCheck = false;
                    System.err.println("change:");
                }
                else
                {
                    System.err.println("answer from Server : "+other);
                    System.out.println("answer from Server : "+other);
                }
            } catch(Exception e)
            {
                e.printStackTrace();
            }

        }
    }
}
