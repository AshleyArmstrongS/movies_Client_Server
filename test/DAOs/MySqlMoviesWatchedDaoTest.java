/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import DTOs.MoviesWatched;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
public class MySqlMoviesWatchedDaoTest {
    
    public MySqlMoviesWatchedDaoTest() {
    }

    /**
     * Test of addMovieWatched method, of class MySqlMoviesWatchedDao.
     */
    @Test
    public void testAddMovieWatched() throws Exception {
        System.out.println("addMovieWatched");
        String username = "iWatchMoviesAllDay";
        int id = 1058;
        MySqlMoviesWatchedDao instance = new MySqlMoviesWatchedDao();
        instance.addMovieWatched(username, id);
    }

//    /**
//     * Test of getMoviesWatched method, of class MySqlMoviesWatchedDao.
//     */
//    @Test
//    public void testGetMoviesWatched() throws Exception {
//        System.out.println("getMoviesWatched");
//        String username = "me";
//        MySqlMoviesWatchedDao instance = new MySqlMoviesWatchedDao();
//        List<MoviesWatched> expResult = new ArrayList<>();
//        MoviesWatched m = new MoviesWatched(username, 15, 1);
//        expResult.add(m);
//        List<MoviesWatched> result = instance.getMoviesWatched(username);
//        
//        assertEquals(expResult, result);
//    }

    /**
     * Test of mostCommon method, of class MySqlMoviesWatchedDao.
     */
    @Test
    public void testMostCommon() {
        System.out.println("mostCommon");
        List<String> variables = new ArrayList <>();
        variables.add("action");
        variables.add("action");
        variables.add("sci-fi");
        MySqlMoviesWatchedDao instance = new MySqlMoviesWatchedDao();
        String expResult = "action";
        String result = instance.mostCommon(variables);
        assertEquals(expResult, result);
    }

    /**
     * Test of sndMostCommon method, of class MySqlMoviesWatchedDao.
     */
    @Test
    public void testSndMostCommon() {
        System.out.println("sndMostCommon");
        List<String> variables = new ArrayList<>();
        variables.add("action");
        variables.add("action");
        variables.add("sci-fi");
        variables.add("action");
        variables.add("action");
        variables.add("sci-fi");
        variables.add("marvel");
        MySqlMoviesWatchedDao instance = new MySqlMoviesWatchedDao();
        String expResult = "sci-fi";
        String result = instance.sndMostCommon(variables);
        assertEquals(expResult, result);
    }
    
}
