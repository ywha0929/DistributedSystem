package org.example;

import java.io.*;

public class DistributableRunnable implements Serializable, Runnable {
    Operands operands;
    ThreadController threadController;
    int threadIndex;
    int result;
    OutputStream outputStream;
    public DistributableRunnable(ThreadController threadController, int index, Operands operands, OutputStream outputStream)
    {
        this.operands = operands;
        this.threadIndex = index;
        this.threadController = threadController;
        this.result = 1;
        this.outputStream = outputStream;
    }
    public int getResult()
    {
        return result;
    }
    @Override
    public void run() {
//        System.out.println(operands.get(0));
//        System.out.println(result);
        System.out.println("Distributable runnable : "+operands.getSize());

        for(int i = 0; i< operands.getSize(); i++)
        {
            for(int j = 0; j< operands.getSize(); j++)
            {
                for(int k = 0; k< operands.getSize(); k++)
                {
                    for(int l = 0; l< operands.getSize(); l++) {
                        result = result * operands.get(i);
                        result = result % 10;
                        if(result == 0)
                            result = 1;
                    }


                }
            }
//                System.out.println(result);


//                System.out.println(result);

        }


//        try {
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
//            objectOutputStream.writeInt(result);
//            objectOutputStream.flush();
//            outputStream.write(byteArrayOutputStream.toByteArray());
//        } catch (Exception e){
//            e.printStackTrace();
//        }
        System.out.println("answer : "+result);
        System.err.println("answer : "+result);
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        try {
            dataOutputStream.writeInt(2);
            dataOutputStream.writeInt(result);
            dataOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        threadController.isOccupied[threadIndex] = false;

    }
}
