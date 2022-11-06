package org.example;

public class ThreadController {
    int numThread;
    boolean[] isOccupied;
    static ThreadController instance = null;
    Thread[] threads;
    Runnable[] runnables;

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
        threads = new Thread[numThread];
        runnables = new Runnable[numThread];
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
    public void setThread(Thread thread, int index)
    {
        threads[index] = thread;
    }
    public Thread getThread(int index)
    {
        return threads[index];
    }
    public void setRunnable(Runnable runnable, int index)
    {
        runnables[index] = runnable;
    }
    public Runnable getRunnable(int index)
    {
        return runnables[index];
    }
}
