package com.njuse.jvmfinal.instructions.references;


import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.Method;
import com.njuse.jvmfinal.runtime.JObject;
import com.njuse.jvmfinal.runtime.NonArrayObject;
import com.njuse.jvmfinal.instructions.base.Index16Instruction;
import com.njuse.jvmfinal.memory.jclass.Field;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.FieldRef;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

public class PUTFIELD extends Index16Instruction {
    public void execute(StackFrame frame) {
        Method currentMethod = frame.getMethod();
        JClass currentClazz = currentMethod.getClazz();
        RuntimeConstantPool runtimeConstantPool = frame.getMethod().getClazz().getRuntimeConstantPool();
        FieldRef fieldRef = (FieldRef) runtimeConstantPool.getConstant(index);
        Field field;
        try{
            //获取解析后的字段
            field = fieldRef.getResolvedFieldRef();

            //检查知否为静态字段
            if(field.isStatic()) throw new IncompatibleClassChangeError();

            if (field.isFinal() && (field.getClazz() != currentClazz || !currentMethod.getName().equals("<init>"))) {
                throw new IllegalAccessError();
            }

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
                case 'L':
                case '[':
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
        assert objectRef instanceof NonArrayObject;

        if(objectRef.isNull())
            throw new NullPointerException();
    }

}
