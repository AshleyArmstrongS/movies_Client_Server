/**
 * MovieDaoInterfce
 * */
package Daos;

import DTOs.Movie;
import Exceptions.DaoException;
import java.util.List;

public interface MovieDaoInterface {

    public List<Movie> findAllMovies() throws DaoException;

    public List<Movie> findMovieByDirector(String direct) throws DaoException;

    public Movie findMovieByTitle(String direct) throws DaoException;

    public Movie findMovieById(Integer id) throws DaoException;

    public List<Movie> findMoviesByGenre(String genre) throws DaoException;

    public List<Movie> findMoviesByGenres(String genre, String genre2) throws DaoException;

    public void addMovie(Movie m) throws DaoException;

    public void updateMovie(Movie m) throws DaoException;

    public void deleteMovieById(Integer id) throws DaoException;
    
     public void deleteMovieByTitle(String title) throws DaoException;

}
