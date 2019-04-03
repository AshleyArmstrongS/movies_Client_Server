/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTOs;

/**
 *
 * @author racheldhc
 */
public class MoviesWatched {

    private String username;
    private int id;
    private int timesWatched;

    public MoviesWatched(String username, int id, int timesWatched) {
        this.username = username;
        this.id = id;
        this.timesWatched = timesWatched;
    }

    public MoviesWatched(String username, int id) {
        this.username = username;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTimesWatched() {
        return timesWatched;
    }

    public void setTimesWatched(int timesWatched) {
        this.timesWatched = timesWatched;
    }

    @Override
    public String toString() {
        return "MoviesWatched{" + "username=" + username + ", id=" + id + ", timesWatched=" + timesWatched + '}';
    }

}
