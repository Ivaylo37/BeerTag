package com.company.web.springdemo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "beers")
public class Beer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "beer_id")
    private int id;
    @Column(name = "beer_name")
    private String name;
    @Column(name = "beer_abv")
    private double abv;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "beer_style")
    private Style style;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "beer_creator")
    private User createdBy;

    public Beer() {
    }

    public Beer(int id, String name, double abv, Style style, User user) {
        this.id = id;
        this.name = name;
        this.abv = abv;
        this.style = style;
        this.createdBy = user;
    }

    public Beer(String name, double abv, Style style, User user) {
        this.name = name;
        this.abv = abv;
        this.style = style;
        this.createdBy = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAbv() {
        return abv;
    }

    public void setAbv(double abv) {
        this.abv = abv;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Beer beer = (Beer) o;
        return id == beer.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
