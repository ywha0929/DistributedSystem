package org.example;

import java.io.ObjectOutputStream;
import java.io.Serializable;

public class DistributableRunnable implements Serializable, Runnable {
    Operands operands;
    ThreadController threadController;
    int threadIndex;
    int result;
    ObjectOutputStream objectOutputStream;
    public DistributableRunnable(ThreadController threadController, int index, Operands operands, ObjectOutputStream objectOutputStream)
    {
        this.operands = operands;
        this.threadIndex = index;
        this.threadController = threadController;
        this.result = 1;
        this.objectOutputStream = objectOutputStream;
    }
    @Override
    public void run() {
//        System.out.println(operands.get(0));
//        System.out.println(result);
//        System.out.println(operands.getSize());

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


        try {
            objectOutputStream.writeInt(result);
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("answer : "+result);
        threadController.isOccupied[threadIndex] = false;
    }
}
