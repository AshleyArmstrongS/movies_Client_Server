/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import DTOs.Movie;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
public class MySqlMovieDaoTest {
    
    public MySqlMovieDaoTest() {
    }


    /**
     * Test of findMovieByDirector method, of class MySqlMovieDao.
     */
    @Test
    public void testFindMovieByDirector() throws Exception {
        System.out.println("findMovieByDirector");
        String direct = "Gus Van Sant";
        MySqlMovieDao instance = new MySqlMovieDao();
        
        ArrayList<String> genre = new ArrayList<>();
        Collections.addAll(genre, "Horror", "Mystery", "Thriller");
        ArrayList<String> starring = new ArrayList<>();
        Collections.addAll(starring, "Vince Vaughn", "Anne Heche", "Julianne Moore", "Viggo Mortensen");
        List<Movie> expResult = new ArrayList<>();
        expResult.add(new Movie(358, "Psycho", genre, "Gus Van Sant", "105 min", "R", starring, 1, "4.6"));
        List<Movie> result = instance.findMovieByDirector(direct);
        assertEquals(expResult, result);
    }

    /**
     * Test of findMovieByTitle method, of class MySqlMovieDao.
     */
    @Test
    public void testFindMovieByTitle() throws Exception {
        System.out.println("findMovieByTitle");
        String direct = "iron man 2";
        MySqlMovieDao instance = new MySqlMovieDao();
        ArrayList<String> genre = new ArrayList<>();
        Collections.addAll(genre, "Disney", "marvel", "Action", "Adventure", "Sci-Fi");
        ArrayList<String> starring = new ArrayList<>();
        Collections.addAll(starring, "Robert Downey Jr.", "Gwyneth Paltrow", "Don Cheadle", "Scarlett Johansson");
        Movie expResult = new Movie(15, "iron man 2", genre, "Jon Favreau", "124 min", "PG-13", starring, 3,"null");
        Movie result = instance.findMovieByTitle(direct);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of findMovieById method, of class MySqlMovieDao.
     */
    @Test
    public void testFindMovieById() throws Exception {
        System.out.println("findMovieById");
        Integer direct = 15;
        MySqlMovieDao instance = new MySqlMovieDao();
        
        ArrayList<String> genre = new ArrayList<>();
        Collections.addAll(genre, "Disney", "marvel", "Action", "Adventure", "Sci-Fi");
        ArrayList<String> starring = new ArrayList<>();
        Collections.addAll(starring, "Robert Downey Jr.", "Gwyneth Paltrow", "Don Cheadle", "Scarlett Johansson");
        Movie expResult = new Movie(15, "iron man 2", genre, "Jon Favreau", "124 min", "PG-13", starring, 3,"null");
        Movie result = instance.findMovieById(direct);
        assertEquals(expResult, result);
    }
//
    /**
     * Test of findMoviesByGenre method, of class MySqlMovieDao.
     */
    @Test
    public void testFindMoviesByGenre() throws Exception {
        System.out.println("findMoviesByGenre");
        String input = "Reality-TV";
        MySqlMovieDao instance = new MySqlMovieDao();
        
        ArrayList<String> genre = new ArrayList<>();
        Collections.addAll(genre, "Reality-TV");
        ArrayList<String> starring = new ArrayList<>();
        Collections.addAll(starring, "K.C. Licklider");
        List<Movie> expResult = new ArrayList<>();
        expResult.add(new Movie(753, "alpha dogs", genre, "N/A", "N/A", "N/A", starring, 1, "7.7"));
        List<Movie> result = instance.findMoviesByGenre(input);
        assertEquals(expResult, result);
    }

    /**
     * Test of findMoviesByGenres method, of class MySqlMovieDao.
     */
    @Test
    public void testFindMoviesByGenres() throws Exception {
        System.out.println("findMoviesByGenres");
        String input = "Musical";
        String input2 = "War";
        MySqlMovieDao instance = new MySqlMovieDao();
        
        ArrayList<String> genre = new ArrayList<>();
        Collections.addAll(genre, "Comedy", "Musical", "War");
        ArrayList<String> starring = new ArrayList<>();
        Collections.addAll(starring, "The Marx Brothers", "Groucho Marx", "Harpo Marx", "Chico Marx");
        List<Movie> expResult = new ArrayList<>();
        expResult.add(new Movie(452, "Duck Soup", genre, "Leo McCarey", "68 min", "PASSED", starring, 1, "8.0"));
        List<Movie> result = instance.findMoviesByGenres(input, input2);
        assertEquals(expResult, result);
    }

    /**
     * Test of findMoviesByGenreThenDirector method, of class MySqlMovieDao.
     */
    @Test
    public void testFindMoviesByGenreThenDirector() throws Exception {
        System.out.println("findMoviesByGenreThenDirector");
        String input = "Horror";
        String input2 = "Gus Van Sant";
        MySqlMovieDao instance = new MySqlMovieDao();
        
        ArrayList<String> genre = new ArrayList<>();
        Collections.addAll(genre, "Horror", "Mystery", "Thriller");
        ArrayList<String> starring = new ArrayList<>();
        Collections.addAll(starring, "Vince Vaughn", "Anne Heche", "Julianne Moore", "Viggo Mortensen");
        List<Movie> expResult = new ArrayList<>();
        expResult.add(new Movie(358, "Psycho", genre, "Gus Van Sant", "105 min", "R", starring, 1, "4.6"));
        List<Movie> result = instance.findMoviesByGenreThenDirector(input, input2);
        assertEquals(expResult, result);
    }

  /**
     * Test of deleteMovieById method, of class MySqlMovieDao.
     */
    
    @Test
    public void testDeleteMovieById() throws Exception {
        System.out.println("deleteMovieById");
        Integer direct = 482;
        MySqlMovieDao instance = new MySqlMovieDao();
        instance.deleteMovieById(direct);
    }

    /**
     * Test of deleteMovieByTitle method, of class MySqlMovieDao.
     */
    @Test
    public void testDeleteMovieByTitle() throws Exception {
        System.out.println("deleteMovieByTitle");
        String direct = "High Society";
        MySqlMovieDao instance = new MySqlMovieDao();
        instance.deleteMovieByTitle(direct);
    }

    /**
     * Test of addMovie method, of class MySqlMovieDao.
     */
    @Test
    public void testAddMovie() throws Exception {
        System.out.println("addMovie");
        MySqlMovieDao instance = new MySqlMovieDao();
          ArrayList<String> genre = new ArrayList<>();
        Collections.addAll(genre, "Disney", "marvel", "Action", "Adventure", "Sci-Fi");
        ArrayList<String> starring = new ArrayList<>();
        Collections.addAll(starring, "Robert Downey Jr.", "Gwyneth Paltrow", "Don Cheadle", "Scarlett Johansson");
         instance.addMovie(new Movie(0, "iron man", genre, "Jon Favreau", "140 min", "PG-13", starring, 4, "10.0"));
    }

    /**
     * Test of updateMovie method, of class MySqlMovieDao.
     */
    @Test
    public void testUpdateMovie() throws Exception {
        System.out.println("updateMovie");
        Movie m = null;
        MySqlMovieDao instance = new MySqlMovieDao();
                 ArrayList<String> genre = new ArrayList<>();
        Collections.addAll(genre, "Disney", "marvel", "Action", "Adventure", "Sci-Fi");
        ArrayList<String> starring = new ArrayList<>();
        Collections.addAll(starring, "Robert Downey Jr.", "Gwyneth Paltrow", "Don Cheadle", "Scarlett Johansson");
         instance.updateMovie(new Movie(1058, "iron man", genre, "Jon Favreau", "140 min", "PG-13", starring, 3, "9.0"));
   
    }
    
}
