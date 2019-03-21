package com.pgault04.pojos;

import com.pgault04.entities.TutorRequests;
import com.pgault04.entities.User;

/**
 * @author Paul Gault 40126005
 * @since Jan 2019
 * Pojo to allow collection of result chart info to be sent to/from front end
 */
public class TutorRequestPojo {

    private User tutor;
    private TutorRequests request;

    /**
     * Default constructor
     */
    public TutorRequestPojo() {}

    /**
     * Constructor with arguments
     *
     * @param tutor   the tutor's user object
     * @param request the request object
     */
    public TutorRequestPojo(User tutor, TutorRequests request) {
        this.setTutor(tutor);
        this.setRequest(request);
    }

    /**
     * @return gets the user object for the tutor
     */
    public User getTutor() { return tutor; }

    /**
     * @param tutor sets the user object for the tutor
     */
    public void setTutor(User tutor) { this.tutor = tutor; }

    /**
     * @return gets the request object
     */
    public TutorRequests getRequest() { return request; }

    /**
     * @param request sets the request object
     */
    public void setRequest(TutorRequests request) { this.request = request; }

    /*
     * the object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TutorRequestPojo{");
        sb.append("tutor=").append(tutor);
        sb.append(", request=").append(request);
        sb.append('}');
        return sb.toString();
    }
}