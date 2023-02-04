package com.pos.laborator.L05X.model.entity;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "artists")
public class Artist extends RepresentationModel<Artist> {

    @Id
    @GeneratedValue(generator = "uuid-generator")
    @GenericGenerator(name = "uuid-generator", strategy = "com.pos.laborator.L05X.generator.UUIDGenerator")
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "active", nullable = false)
    private Boolean active;

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Boolean isActive(){
        return this.active;
    }

    public void setActive(Boolean active){
        this.active = active;
    }
}
