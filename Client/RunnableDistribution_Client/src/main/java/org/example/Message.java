package org.example;

public class Message {
    int parameter;
    Object obj;
    int type = 1;
    public int getParameter() {
        return parameter;
    }
    public Object getObj() {
        return obj;
    }
    public int getType()
    {
        return type;
    }
    public void setParameter(int parameter) {
        this.parameter = parameter;
    }
    public Message(int parameter)
    {
        this.parameter = parameter;
        this.type = 1;
    }
    public Message(Object obj)
    {
        this.type = 2;
        this.obj = obj;
    }
}
