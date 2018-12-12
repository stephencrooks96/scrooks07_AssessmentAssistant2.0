package com.pgault04.pojos;

import com.pgault04.entities.Module;
import com.pgault04.entities.User;

public class ModuleWithTutor {

    private User tutor;
    private Module module;

    public ModuleWithTutor(User tutor, Module module) {
        this.setModule(module);
        this.setTutor(tutor);
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
