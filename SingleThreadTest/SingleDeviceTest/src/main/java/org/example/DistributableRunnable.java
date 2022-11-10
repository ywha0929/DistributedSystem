package org.example;

import java.io.Serializable;

public class DistributableRunnable implements Serializable, Runnable {
    Operands operands;
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


        while(SingleDeviceTest.listLock.get())
            for(int j = 0; j< 10000; j++);// if not lock
        SingleDeviceTest.listLock.set(true);
        SingleDeviceTest.listAnswer.put(taskNum,result);
        System.err.println("listAnswer size : "+SingleDeviceTest.listAnswer.size());
        SingleDeviceTest.listLock.set(false);
        System.err.println("answer : "+result);
        threadController.isOccupied[threadIndex] = false;
    }
}
