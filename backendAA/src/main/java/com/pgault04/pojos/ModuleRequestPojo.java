package com.pgault04.pojos;

import com.pgault04.entities.Module;
import com.pgault04.entities.User;

public class ModuleRequestPojo {

    private User tutor;
    private Module module;

    public ModuleRequestPojo() {
    }

    public ModuleRequestPojo(User tutor, Module module) {
        this.tutor = tutor;
        this.module = module;
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
}
