package com.pgault04.pojos;

import com.pgault04.entities.TutorRequests;
import com.pgault04.entities.User;

public class TutorRequestPojo {

    private User tutor;
    private TutorRequests request;

    public TutorRequestPojo() {
    }

    public TutorRequestPojo(User tutor, TutorRequests request) {
        this.setTutor(tutor);
        this.setRequest(request);
    }

    public User getTutor() {
        return tutor;
    }

    public void setTutor(User tutor) {
        this.tutor = tutor;
    }

    public TutorRequests getRequest() {
        return request;
    }

    public void setRequest(TutorRequests request) {
        this.request = request;
    }
}
