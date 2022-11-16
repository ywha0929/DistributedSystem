package org.example;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class SingleDeviceTest {
    static int numThread = 8;
    static AtomicBoolean listLock;

    static Map<Integer, Integer> listAnswer;

    public static void main(String[] args) {
        listLock = new AtomicBoolean(false);
        listAnswer = new HashMap<>();
//        String nameTestFile = "../../TestFiles/TestFile.txt";
        numThread = Integer.parseInt(args[1]);
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

        long beforeTime = System.currentTimeMillis();
        for(int i = 0; i<listOperands.size(); i++)
        {
            int index = threadController.getIdleThreadIndex();
//            System.out.println("index : " + index);
            while(index == -1)
            {
                index = threadController.getIdleThreadIndex();
            } //wait until idel thread is found
            threadController.useThread(index);
            Thread thisThread = new Thread(new DistributableRunnable(threadController,index, listOperands.get(i), i));
            thisThread.start();

        }

        int listAnswerSize = listAnswer.size();
        while(listAnswerSize < listOperands.size()) {
            while(SingleDeviceTest.listLock.get())
                for(int j = 0; j< 10000; j++);// if not lock
            SingleDeviceTest.listLock.set(true);
            listAnswerSize = listAnswer.size();
//            System.err.println("listAnswer size : "+RunnableDistribution_Client.listAnswer.size());
            SingleDeviceTest.listLock.set(false);
        }

        long afterTime = System.currentTimeMillis();
        long secDiffTime = (afterTime - beforeTime)/1000;
        for(int i = 0; i< listAnswer.size(); i++)
        {
            System.out.println(i + "th answer : "+listAnswer.get(i));

        }
        System.out.println("Execution Time : " +secDiffTime);

    }
}

