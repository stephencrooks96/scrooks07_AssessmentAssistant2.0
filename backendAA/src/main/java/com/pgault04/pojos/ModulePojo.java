package com.pgault04.pojos;

import com.pgault04.entities.Module;

import java.util.List;

/**
 * @author Paul Gault - 40126005
 * @since Jan 2019
 * Pojo to allow collection of module info to be sent to/from front end
 */
public class ModulePojo {

    private Module module;
    private List<Associate> associations;

    /**
     * Default constructor
     */
    public ModulePojo() {}

    /**
     * Constructor with arguments
     *
     * @param module       - the module
     * @param associations - the associations
     */
    public ModulePojo(Module module, List<Associate> associations) {
        this.setModule(module);
        this.setAssociations(associations);
    }

    /**
     * @return gets the module
     */
    public Module getModule() { return module; }

    /**
     * @param module sets the module
     */
    public void setModule(Module module) { this.module = module; }

    /**
     * @return gets the associates of the module
     */
    public List<Associate> getAssociations() { return associations; }

    /**
     * @param associations sets the associations of the module
     */
    public void setAssociations(List<Associate> associations) { this.associations = associations; }

    /*
     * the object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ModulePojo{");
        sb.append("module=").append(module);
        sb.append(", associations=").append(associations);
        sb.append('}');
        return sb.toString();
    }
}