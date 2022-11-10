package org.example;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class SendThread extends Thread
{
    OutputStream outputStream;
    DataOutputStream dataOutputStream;
    MessageQueue msgQ;
    AtomicBoolean isQBusy;
    public SendThread(Socket socket) throws IOException {
        msgQ = new MessageQueue();
        outputStream =  socket.getOutputStream();
        dataOutputStream = new DataOutputStream(outputStream);
        isQBusy = new AtomicBoolean(false);
    }
    public void putMsg(Message msg)
    {
//        while(isQBusy.get())
//            for(int j= 0; j<5000; j++);
//        isQBusy.set(true);
        msgQ.pushMessage(msg);
//        isQBusy.set(false);
    }

    @Override
    public void run() {
        while (true)
        {
            while (msgQ.msgQueue.isEmpty())
                for(int j = 0; j<5000; j++);
//            isQBusy.set(true);
            System.err.println("Send thread : Got Message");
            Message msg = msgQ.getMessage();
//            isQBusy.set(false);
            if(msg == null)
                continue;
            try {
                if(msg.getType() == 1)
                {
                    dataOutputStream.writeInt(msg.getParameter());
                    dataOutputStream.flush();
                    System.err.println("Send thread : send num to server");
                    //outputStream.write(byteArrayOutputStream.toByteArray());
                }
                else if(msg.getType() == 2)
                {
                    dataOutputStream.writeInt(2);
                    dataOutputStream.writeInt(msg.getParameter());
                    dataOutputStream.write((byte[])msg.getObj());
//                    byte[] bytes = (byte[]) msg.getObj();
//                    Operands operands = new Operands();
//                    operands.fromByteArray(bytes);
//                    System.out.println(operands.getOperands()[0]);
                    dataOutputStream.flush();
                    System.err.println("Send thread : sent operands to server + "+msg.getParameter());
//                    for(int i = 0; i< 100; i++)
//                    {
//                        System.out.print(((byte[])msg.getObj())[i]);
//                    }
//                    System.out.println();
                    //outputStream.write(byteArrayOutputStream.toByteArray());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}