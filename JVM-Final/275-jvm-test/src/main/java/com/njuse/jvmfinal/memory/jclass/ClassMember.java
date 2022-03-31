package com.njuse.jvmfinal.memory.jclass;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ClassMember {
    public short accessFlags;
    public String name;
    public String descriptor;
    public JClass clazz;

    public ClassMember() {
    }

    public boolean isPublic() {
        return 0 != (this.accessFlags & 1);
    }

    public boolean isPrivate() {
        return 0 != (this.accessFlags & 2);
    }

    public boolean isLongOrDouble() {
        return this.descriptor.equals("J") || this.descriptor.equals("D");
    }

    public boolean isStatic() {
        return 0 != (this.accessFlags & 8);
    }

    public boolean isNative() {
        return 0 != (this.accessFlags & 256);
    }

    public boolean isFinal() {
        return 0 != (this.accessFlags & 16);
    }

    public boolean isProtected() {
        return 0 != (this.accessFlags & 4);
    }

    public boolean isAccessibleTo(JClass D) {
        if (this.isPublic()) {
            return true;
        } else if (!this.isProtected() || this.clazz != D && D.getSuperClass() != this.clazz && !this.clazz.getPackageName().equals(D.getPackageName())) {
            if ((this.isPublic() || this.isPrivate()) && this.clazz.getPackageName().equals(D.getPackageName())) {
                return true;
            } else {
                return this.isPrivate() && this.clazz == D;
            }
        } else {
            return true;
        }
    }

    public short getAccessFlags() {
        return this.accessFlags;
    }

    public String getName() {
        return this.name;
    }

    public String getDescriptor() {
        return this.descriptor;
    }

    public JClass getClazz() {
        return this.clazz;
    }

    public void setAccessFlags(short accessFlags) {
        this.accessFlags = accessFlags;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public void setClazz(JClass clazz) {
        this.clazz = clazz;
    }
}
