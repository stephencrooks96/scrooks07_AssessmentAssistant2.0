package com.pgault04.pojos;

import com.pgault04.entities.Module;
import com.pgault04.entities.User;

public class ModuleRequestPojo {

    private User tutor;
    private Module module;

    public ModuleRequestPojo() {
    }

    public ModuleRequestPojo(User tutor, Module module) {
        this.setTutor(tutor);
        this.setModule(module);
    }

    public User getTutor() {
        return tutor;
    }

    public void setTutor(User tutor) {
        this.tutor = tutor;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ModuleRequestPojo{");
        sb.append("tutor=").append(tutor);
        sb.append(", module=").append(module);
        sb.append('}');
        return sb.toString();
    }
}
