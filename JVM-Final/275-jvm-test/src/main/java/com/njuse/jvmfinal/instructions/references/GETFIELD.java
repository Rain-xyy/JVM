package com.njuse.jvmfinal.instructions.references;


import com.njuse.jvmfinal.runtime.*;
import com.njuse.jvmfinal.instructions.base.Index16Instruction;
import com.njuse.jvmfinal.memory.jclass.Field;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.FieldRef;

public class GETFIELD extends Index16Instruction {
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
                case 'L':
                case '[':
                    stack.pushObjectRef(fieldsVars.getObjectRef(slotID));
                    break;
            }

        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }

    }
}
