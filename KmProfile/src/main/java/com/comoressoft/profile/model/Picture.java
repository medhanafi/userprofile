package com.comoressoft.profile.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * @author mha14633
 *
 *         6 déc. 2018
 *
 */
@Entity(name = "picture")
public class Picture {

    private String fileName;

    @OneToOne(mappedBy = "picture")
    private KmUser user;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public KmUser getUser() {
        return this.user;
    }

    public void setUser(KmUser user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
