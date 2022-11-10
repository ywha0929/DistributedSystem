package org.example;

import java.io.Serializable;

public class Operands implements Serializable {
    int[] operands;
    int cursor = 0;
    int size = 0;

    public void setSize(int size) {
        this.size = size;
        operands = new int[size];
    }
    public int getSize()
    {
        return size;
    }
    public void pushOperand(int operand)
    {
        operands[cursor] = operand;
        cursor++;
    }

    public int[] getOperands() {
        return operands;
    }
    public int get(int i)
    {
        return operands[i];
    }

    public byte[] toByteArray()
    {
        byte[] result = new byte[operands.length];

        for(int i = 0; i< operands.length; i++)
        {
            result[i] = (byte) (operands[i] & 0xFF);
        }
        return result;
    }

    public void fromByteArray(byte[] source)
    {
        this.size = source.length;
        this.operands = new int[this.size];
        for(int i=0; i<this.size; i++)
        {
            operands[i] = source[i] & 0xFF;
        }
    }
}

