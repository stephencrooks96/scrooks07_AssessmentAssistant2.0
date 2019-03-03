package com.pgault04.pojos;

import com.pgault04.entities.Module;

import java.util.List;

public class ModulePojo {

    private Module module;
    private List<Associate> associations;

    public ModulePojo() {}

    public ModulePojo(Module module, List<Associate> associations) {
        this.module = module;
        this.associations = associations;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public List<Associate> getAssociations() {
        return associations;
    }

    public void setAssociations(List<Associate> associations) {
        this.associations = associations;
    }
}
