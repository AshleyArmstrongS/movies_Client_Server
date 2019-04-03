/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Daos;

import DTOs.MoviesWatched;
import Exceptions.DaoException;
import java.util.List;

/**
 *
 * @author racheldhc
 */
public interface MoviesWatchedDaoInterface {

    public void addMovieWatched(String username, int id) throws DaoException;

    public List<MoviesWatched> getMoviesWatched(String username) throws DaoException;

    public String mostCommonGenre(List<String> genres);

    public String sndMostCommonGenre(List<String> genres);
}
