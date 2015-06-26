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
    private int id;

    @Id // id is the table's primary key
    @GeneratedValue // id is autoincremented
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }
}
