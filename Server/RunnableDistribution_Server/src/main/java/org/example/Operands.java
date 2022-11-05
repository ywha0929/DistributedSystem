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
}
