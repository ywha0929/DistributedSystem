package org.example;

import java.io.*;
import java.net.Socket;

public class SendThread extends Thread
{
    OutputStream outputStream;
    DataOutputStream dataOutputStream;
    MessageQueue msgQ;
    public SendThread(Socket socket) throws IOException {
        msgQ = new MessageQueue();
        outputStream =  socket.getOutputStream();
        dataOutputStream = new DataOutputStream(outputStream);
    }
    public void putMsg(Message msg)
    {
        msgQ.pushMessage(msg);
    }

    @Override
    public void run() {
        while (true)
        {
            while (msgQ.msgQueue.isEmpty())
                for(int j = 0; j<5000; j++);
            System.out.println("Send thread : Got Message");
            Message msg = msgQ.getMessage();
            try {
                if(msg.getType() == 1)
                {
                    dataOutputStream.writeInt(msg.getParameter());
                    dataOutputStream.flush();
                    System.out.println("Send thread : send num to server");
                    //outputStream.write(byteArrayOutputStream.toByteArray());
                }
                else if(msg.getType() == 2)
                {
                    dataOutputStream.writeInt(2);
                    dataOutputStream.write((byte[])msg.getObj());
                    dataOutputStream.flush();
                    System.out.println("Send thread : sent operands to server");
                    //outputStream.write(byteArrayOutputStream.toByteArray());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}