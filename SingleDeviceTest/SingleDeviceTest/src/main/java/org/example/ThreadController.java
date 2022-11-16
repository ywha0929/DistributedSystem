package org.example;

public class ThreadController {
    int numThread;
    boolean[] isOccupied;
    static ThreadController instance = null;
//    Thread[] threads;

    public static ThreadController getInstance()
    {
        if(instance == null)
        {
            instance = new ThreadController();
            return instance;
        }
        else {
            return instance;
        }
    }

    public void setNumThread(int numThread)
    {
        instance.numThread = numThread;
        instance.isOccupied = new boolean[numThread];
    }

    public int getIdleThreadIndex()
    {
        for(int i = 0; i< instance.numThread; i++)
        {
            if(instance.isOccupied[i] == false)
            {
                return i;
            }
        }
        return -1;
    }
    public void useThread(int index)
    {
        if(instance.isOccupied[index] == false)
        {
            instance.isOccupied[index] = true;
//            return threads[index];
        }
        else
        {
//            return null;
        }
    }


}
