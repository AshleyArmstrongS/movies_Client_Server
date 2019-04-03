package DTOs;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This POJO (Plain Old Java Object) is called the Data Transfer Object (DTO).
 */
public class Movie {

    private int id;
    private String title;
    private ArrayList<String> genre;
    private String director;
    private String runtime;
    private String rating;
    private ArrayList<String> starring;
    private int copies;
    private String user_rating;

    public Movie(int id, String title, ArrayList<String> genre, String director, String runtime, String rating, ArrayList<String> starring, int copies, String user_rating) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.runtime = runtime;
        this.rating = rating;
        this.starring = starring;
        this.copies = copies;
        this.user_rating = user_rating;
    }

    public Movie() {
        this.id = 0;
        this.title = " ";
        this.genre = null;
        this.director = " ";
        this.runtime = " ";
        this.rating = " ";
        this.starring = null;
        this.copies = 0;
        this.user_rating = " ";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(ArrayList<String> genre) {
        this.genre = genre;
    }

    public void setStarring(ArrayList<String> starring) {
        this.starring = starring;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public ArrayList<String> getGenre() {
        return genre;
    }

    public ArrayList<String> getStarring() {
        return starring;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public String getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(String user_rating) {
        this.user_rating = user_rating;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + this.id;
        hash = 71 * hash + Objects.hashCode(this.title);
        hash = 71 * hash + Objects.hashCode(this.genre);
        hash = 71 * hash + Objects.hashCode(this.director);
        hash = 71 * hash + Objects.hashCode(this.runtime);
        hash = 71 * hash + Objects.hashCode(this.rating);
        hash = 71 * hash + Objects.hashCode(this.starring);
        hash = 71 * hash + this.copies;
        hash = 71 * hash + Objects.hashCode(this.user_rating);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Movie other = (Movie) obj;
        if (this.id != other.id)
        {
            return false;
        }
        if (this.copies != other.copies)
        {
            return false;
        }
        if (!Objects.equals(this.title, other.title))
        {
            return false;
        }
        if (!Objects.equals(this.director, other.director))
        {
            return false;
        }
        if (!Objects.equals(this.runtime, other.runtime))
        {
            return false;
        }
        if (!Objects.equals(this.rating, other.rating))
        {
            return false;
        }
        if (!Objects.equals(this.user_rating, other.user_rating))
        {
            return false;
        }
        if (!Objects.equals(this.genre, other.genre))
        {
            return false;
        }
        if (!Objects.equals(this.starring, other.starring))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Movie{" + "id=" + id + ", title=" + title + ", genre=" + genre + ", director=" + director + ", runtime=" + runtime + ", rating=" + rating + ", starring=" + starring + ", copies=" + copies + ", user_rating=" + user_rating + '}';
    }

}
