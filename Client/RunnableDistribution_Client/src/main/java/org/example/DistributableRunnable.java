package org.example;

import java.io.Serializable;
import java.util.HashMap;

public class DistributableRunnable implements Serializable, Runnable {
    org.example.Operands operands;
    ThreadController threadController;
    int threadIndex;
    int result;
    int taskNum;
    public DistributableRunnable(ThreadController threadController, int index, Operands operands, int taskNum)
    {
        this.operands = operands;
        this.threadIndex = index;
        this.threadController = threadController;
        this.result = 1;
        this.taskNum = taskNum;
    }
    @Override
    public void run() {
//        System.out.println("startCalculation");

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



//        System.out.println(taskNum + "th answer : "+result);
        while(RunnableDistribution_Client.listLock.get())
            for(int j = 0; j< 10000; j++);// if not lock
        RunnableDistribution_Client.listLock.set(true);
        RunnableDistribution_Client.listAnswer.put(taskNum,result);
        System.err.println("listAnswer size : "+RunnableDistribution_Client.listAnswer.size());
        RunnableDistribution_Client.listLock.set(false);

        System.err.println(taskNum + "th answer : "+result);
        threadController.isOccupied[threadIndex] = false;
    }
}
