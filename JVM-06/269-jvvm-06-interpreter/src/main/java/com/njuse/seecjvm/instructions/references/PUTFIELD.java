package com.njuse.seecjvm.instructions.references;

import com.njuse.seecjvm.instructions.base.Index16Instruction;
import com.njuse.seecjvm.memory.jclass.Field;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.ref.FieldRef;
import com.njuse.seecjvm.runtime.OperandStack;
import com.njuse.seecjvm.runtime.StackFrame;
import com.njuse.seecjvm.runtime.Vars;
import com.njuse.seecjvm.runtime.struct.JObject;
import com.njuse.seecjvm.runtime.struct.NonArrayObject;
import com.njuse.seecjvm.runtime.struct.NullObject;
import com.njuse.seecjvm.runtime.struct.Slot;


public class PUTFIELD extends Index16Instruction {
    /**
     * TODO 实现这条指令
     * 其中 对应的index已经读取好了
     */
    @Override
    public void execute(StackFrame frame) {
        RuntimeConstantPool runtimeConstantPool = frame.getMethod().getClazz().getRuntimeConstantPool();
        FieldRef fieldRef = (FieldRef) runtimeConstantPool.getConstant(index);
        Field field;
        try{
            //获取解析后的字段
            field = fieldRef.getResolvedFieldRef();

            //检查知否为静态字段
            if(field.isStatic()) throw new IncompatibleClassChangeError();

            if(field.isFinal() && !frame.getMethod().getName().equals("<init>")) throw new IncompatibleClassChangeError();

            int slotID = field.getSlotID();
            OperandStack operandStack = frame.getOperandStack();
            JObject objectRef;
            switch (field.descriptor.charAt(0)) {
                case 'Z':
                case 'B':
                case 'C':
                case 'S':
                case 'I':
                    int intVal = operandStack.popInt();
                    objectRef = operandStack.popObjectRef();
                    judgeRef(objectRef);
                    ((NonArrayObject) objectRef).getFields().setInt(slotID, intVal);
                    break;
                case 'F':
                    float floatVal = operandStack.popFloat();
                    objectRef = operandStack.popObjectRef();
                    judgeRef(objectRef);
                    ((NonArrayObject) objectRef).getFields().setFloat(slotID, floatVal);
                    break;
                case 'J':
                    long longVal = operandStack.popLong();
                    objectRef = operandStack.popObjectRef();
                    judgeRef(objectRef);
                    ((NonArrayObject) objectRef).getFields().setLong(slotID, longVal);
                    break;
                case 'D':
                    double doubleVal = operandStack.popDouble();
                    objectRef = operandStack.popObjectRef();
                    judgeRef(objectRef);
                    ((NonArrayObject) objectRef).getFields().setDouble(slotID, doubleVal);
                    break;
                default:
                    JObject refVal = frame.getOperandStack().popObjectRef();
                    objectRef = frame.getOperandStack().popObjectRef();
                    judgeRef(objectRef);
                    ((NonArrayObject) objectRef).getFields().setObjectRef(slotID, refVal);
                    break;
            }
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    private void judgeRef(JObject objectRef){
        if(objectRef.isNull())
            throw new NullPointerException();
    }

}
