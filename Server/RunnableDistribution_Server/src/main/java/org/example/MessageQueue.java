package org.example;

import java.util.ArrayList;
import java.util.List;

public class MessageQueue {
    List<Message> msgQueue;
    public MessageQueue()
    {
        msgQueue = new ArrayList<>();
    }
    public Message getMessage()
    {
        if(!msgQueue.isEmpty()) //msqQueue not empty
        {
            Message msg = msgQueue.get(0);
            msgQueue.remove(0);
            return msg;
        }
        else
        {
            return null;
        }
    }
    public void pushMessage(Message msg)
    {
        msgQueue.add(msg);
    }
}