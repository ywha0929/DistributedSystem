package org.example;

import java.io.File;
import java.io.IOException;
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


    static int numThread = 4;
    public static void main(String[] args) {
        int numThread = 4;
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
        for(int i= 0; i<ips.length; i++)
        {
            try{
                Socket socket = new Socket(ips[i],ports[i]);
            } catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        for(int i = 0; i<listOperands.size(); i++)
        {
            int index = threadController.getIdleThreadIndex();
//            System.out.println("index : " + index);
            while(index == -1)
            {
                index = threadController.getIdleThreadIndex();
            } //wait until idel thread is found
            threadController.useThread(index);
            Thread thisThread = new Thread(new DistributableRunnable(threadController,index, listOperands.get(i)));
            thisThread.start();

        }


    }
}

class SocketOutputThread extends Thread {
    String ip;
    int port;
    int index;
    public SocketOutputThread(String ip, int port, int index)
    {
        this.ip = ip;
        this.port = port;
        this.index = index;
    }
    public void run() {
        try{
            Socket socket = new Socket(ip,port);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}