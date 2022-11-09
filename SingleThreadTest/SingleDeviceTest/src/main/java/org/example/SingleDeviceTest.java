package org.example;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SingleDeviceTest {
    static int numThread = 4;


    public static void main(String[] args) {

//        String nameTestFile = "../../TestFiles/TestFile.txt";
        String nameTestFile = args[1];
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
            Thread thisThread = new Thread(new DistributableRunnable(threadController,index, listOperands.get(i)));
            thisThread.start();

        }
        long afterTime = System.currentTimeMillis();
        long secDiffTime = (afterTime - beforeTime)/1000;
        System.out.println("Execution Time : " +secDiffTime);

    }
}

