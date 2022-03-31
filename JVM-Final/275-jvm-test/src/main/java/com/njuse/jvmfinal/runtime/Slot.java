package com.njuse.jvmfinal.runtime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Slot {
    private Integer value;
    private JObject object;

    public Slot clone() {
        Slot toClone = new Slot();
        toClone.object = this.object;
        toClone.value = this.value;
        return toClone;
    }
}
