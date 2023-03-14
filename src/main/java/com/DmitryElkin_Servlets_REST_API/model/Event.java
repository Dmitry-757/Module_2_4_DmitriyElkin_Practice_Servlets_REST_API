package com.DmitryElkin_Servlets_REST_API.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "event_tbl")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id", unique = true, nullable = true)
    @OneToOne
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "file_id", unique = true, nullable = true)
    private File file;


    public Event() {
    }

    public Event(User user, File file) {
        this.user = user;
        this.file = file;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id && user.equals(event.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user);
    }
}
