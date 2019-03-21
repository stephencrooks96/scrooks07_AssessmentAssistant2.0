package com.pgault04.pojos;

import com.pgault04.entities.Module;
import com.pgault04.entities.User;

/**
 * Class to accumulate module data with tutor data for output on the front end
 *
 * @author Paul Gault 40126005
 * @since November 2018
 */
public class ModuleWithTutor {

    private User tutor;
    private Module module;

    /**
     * Default constructor
     */
    public ModuleWithTutor() {}

    /**
     * Constructor with args
     */
    public ModuleWithTutor(User tutor, Module module) {
        this.setModule(module);
        this.setTutor(tutor);
    }

    /**
     * @return the tutor
     */
    public User getTutor() {
        return tutor;
    }

    /**
     * @param tutor the tutor to set
     */
    public void setTutor(User tutor) {
        this.tutor = tutor;
    }

    /**
     * @return the module
     */
    public Module getModule() {
        return module;
    }

    /**
     * @param module the module to set
     */
    public void setModule(Module module) {
        this.module = module;
    }

    /**
     * @return the object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ModuleWithTutor{");
        sb.append("tutor=").append(tutor.toString());
        sb.append(", module=").append(module.toString());
        sb.append('}');
        return sb.toString();
    }
}