package org.letsdig.app.models;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Created by adrian on 6/2/15.
 */

// abstract class providing a unique id for each entity that will
// persist in the db
@MappedSuperclass
public abstract class AbstractLetsDigEntity {

    // unique identifier
    private int uid;

    @Id // uid is the table's primary key
    @GeneratedValue // uid is autoincremented
    @Column(name = "uid", unique = true, nullable = false)
    public int getUid() {
        return uid;
    }

    protected void setUid(int uid) {
        this.uid = uid;
    }
}
