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

public class GETFIELD extends Index16Instruction {


    /**
     * TODO 实现这条指令
     * 其中 对应的index已经读取好了
     */
    @Override
    public void execute(StackFrame frame) {
        JObject objectRef = frame.getOperandStack().popObjectRef();
        if(objectRef.isNull()) {
            throw new NullPointerException();
        }

        RuntimeConstantPool runtimeConstantPool = frame.getMethod().getClazz().getRuntimeConstantPool();
        FieldRef fieldRef = (FieldRef) runtimeConstantPool.getConstant(index);
        Field field;
        try {
            //获取解析后的字段
            field = fieldRef.getResolvedFieldRef();

            //检查知否为静态字段
            if(field.isStatic())
                throw new IncompatibleClassChangeError();

            //获取objectRef所引用对象中该字段的值
            String descriptor = field.getDescriptor();
            int slotID = field.getSlotID();
            OperandStack stack = frame.getOperandStack();
            Vars fieldsVars = ((NonArrayObject) objectRef).getFields();
            switch (descriptor.charAt(0)){
                case 'Z':
                case 'B':
                case 'C':
                case 'S':
                case 'I':
                    stack.pushInt(fieldsVars.getInt(slotID));
                    break;
                case 'F':
                    stack.pushFloat(fieldsVars.getFloat(slotID));
                    break;
                case 'J':
                    stack.pushLong(fieldsVars.getLong(slotID));
                    break;
                case 'D':
                    stack.pushDouble(fieldsVars.getDouble(slotID));
                    break;
                default:
                    stack.pushObjectRef(fieldsVars.getObjectRef(slotID));
                    break;
            }

        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }

    }
}
