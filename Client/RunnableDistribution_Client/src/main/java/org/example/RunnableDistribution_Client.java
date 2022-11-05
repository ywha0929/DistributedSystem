package org.example;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class RunnableDistribution_Client {
    static String[] ips;
    static int[] ports;
    static Socket[] sockets;
    static int numServers = 1;
    static int numThread = 4;
    public static void main(String[] args) {

        ips = new String[numServers];
        ports = new int[numServers];
        sockets = new Socket[numServers];
        ips[0] = "210.107.198.220";
        ports[0] = 21234;

//        SocketOutputThread[] SocketOutputThreads = new SocketOutputThread[numServers];

        String nameTestFile = "../../TestFiles/TestFile.txt";
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
//                SocketOutputThreads[i] = new SocketOutputThread(socket);

            } catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        for(int i = 0; i<listOperands.size(); i++)
        {
            int index = threadController.getIdleThreadIndex();
            int serverIndex = getServerIdleThreadIndex();
//            System.out.println("index : " + index);
            while(index == -1 && serverIndex==-1)
            {
                index = threadController.getIdleThreadIndex();
                serverIndex = getServerIdleThreadIndex();
            } //wait until idel thread is found
            if(index == -1)
            {
                int numServer = serverIndex / 1000;
                int finalI = i;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            ObjectOutputStream objectOutputStream = (ObjectOutputStream) sockets[numServer].getOutputStream();
                            objectOutputStream.writeUTF("DistributeCalculation");
                            objectOutputStream.writeObject(listOperands.get(finalI));
                            ObjectInputStream objectInputStream = (ObjectInputStream) sockets[numServer].getInputStream();
                            int result = objectInputStream.readInt();
                            System.out.println("answer : "+result);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });
                thread.start();
            }
            else
            {
                threadController.useThread(index);
                Thread thisThread = new Thread(new DistributableRunnable(threadController,index, listOperands.get(i)));
                thisThread.start();
            }


        }


    }


    static int getServerIdleThreadIndex()
    {
        for(int i = 0; i<numServers; i++)
        {

            try{
                ObjectOutputStream objectOutputStream = (ObjectOutputStream) sockets[i].getOutputStream();
                objectOutputStream.writeUTF("getServerIdleIndex");
                ObjectInputStream objectInputStream = (ObjectInputStream) sockets[i].getInputStream();
                int index = objectInputStream.readInt();
                if(index != -1)
                {
                    return i*1000 + index;
                }

            }catch(Exception e){
                e.printStackTrace();
            }

        }
        return -1;
    }
}
