package com.pgault04.pojos;

import com.pgault04.entities.Module;
import com.pgault04.entities.User;

/**
 * @author Paul Gault - 40126005
 * @since Jan 2019
 * Pojo to allow collection of module request info to be sent to/from front end
 */
public class ModuleRequestPojo {

    private User tutor;
    private Module module;

    /**
     * Default constructor
     */
    public ModuleRequestPojo() { }

    /**
     * Constructor with arguments
     *
     * @param tutor  - the tutor
     * @param module - the module
     */
    public ModuleRequestPojo(User tutor, Module module) {
        this.setTutor(tutor);
        this.setModule(module);
    }

    /**
     * @return gets the tutor
     */
    public User getTutor() { return tutor; }

    /**
     * @param tutor sets the tutor
     */
    public void setTutor(User tutor) { this.tutor = tutor; }

    /**
     * @return gets the module
     */
    public Module getModule() { return module; }

    /**
     * @param module sets the module
     */
    public void setModule(Module module) { this.module = module; }

    /*
     * the object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ModuleRequestPojo{");
        sb.append("tutor=").append(tutor);
        sb.append(", module=").append(module);
        sb.append('}');
        return sb.toString();
    }
}