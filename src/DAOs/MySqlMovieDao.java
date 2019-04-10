package Daos;

import DTOs.Movie;
import Exceptions.DaoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MySqlMovieDao extends MySqlDao implements MovieDaoInterface {

    @Override
    public List<Movie> findAllMovies() throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Movie> movies = new ArrayList<>();

        try
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();

            String query = "SELECT * FROM MOVIES";
            ps = con.prepareStatement(query);

            //Using a PreparedStatement to execute SQL...
            rs = ps.executeQuery();
            while (rs.next())
            {
                int id = rs.getInt("ID");
                String title = rs.getString("title");
                String genreString = rs.getString("genre");
                String director = rs.getString("director");
                String runtime = rs.getString("runtime");
                String plot = rs.getString("plot");
                String rating = rs.getString("rating");
                String format = rs.getString("format");
                String year = rs.getString("year");
                String starringString = rs.getString("starring");
                int copies = rs.getInt("copies");
                String barcode = rs.getString("barcode");
                String user_rating = rs.getString("user_rating");

                String[] genreArray = genreString.split(",");
                String[] starringArray = starringString.split(",");
                ArrayList<String> genre = new ArrayList<>(Arrays.asList(genreArray));
                ArrayList<String> starring = new ArrayList<>(Arrays.asList(starringArray));

                Movie m = new Movie(id, title, genre, director, runtime, rating, starring, copies, user_rating);
                movies.add(m);
            }
        } catch (SQLException e)
        {
            throw new DaoException("findAllMovies() " + e.getMessage());
        } finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            } catch (SQLException e)
            {
                throw new DaoException("findAllMovies() " + e.getMessage());
            }
        }
        return movies;     // may be empty
    }

    @Override
    public List<Movie> findMovieByDirector(String direct) throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Movie> movies = new ArrayList<>();

        try
        {
            con = this.getConnection();

            String query = "SELECT * FROM movies WHERE director = ? ";
            ps = con.prepareStatement(query);
            ps.setString(1, direct);

            rs = ps.executeQuery();
            while (rs.next())
            {
                int movie = rs.getInt("id");
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String genreString = rs.getString("genre");
                String director = rs.getString("director");
                String runtime = rs.getString("runtime");
                String plot = rs.getString("plot");
                String rating = rs.getString("rating");
                String format = rs.getString("format");
                String year = rs.getString("year");
                String starringString = rs.getString("starring");
                int copies = rs.getInt("copies");
                String barcode = rs.getString("barcode");
                String user_rating = rs.getString("user_rating");

                String[] genreArray = genreString.split(",");
                String[] starringArray = starringString.split(",");
                ArrayList<String> genre = new ArrayList<>(Arrays.asList(genreArray));
                ArrayList<String> starring = new ArrayList<>(Arrays.asList(starringArray));

                Movie m = new Movie(id, title, genre, director, runtime, rating, starring, copies, user_rating);
                movies.add(m);
            }
        } catch (SQLException e)
        {
            throw new DaoException("findMovieByDirector() " + e.getMessage());
        } finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            } catch (SQLException e)
            {
                throw new DaoException("findMovieByDirector() " + e.getMessage());
            }
        }
        return movies;     // u may be null 
    }

    @Override
    public Movie findMovieByTitle(String direct) throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Movie m = null;
        try
        {
            con = this.getConnection();

            String query = "SELECT * FROM movies WHERE title = ? ";
            ps = con.prepareStatement(query);
            ps.setString(1, direct);

            rs = ps.executeQuery();
            if (rs.next())
            {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String genreString = rs.getString("genre");
                String director = rs.getString("director");
                String runtime = rs.getString("runtime");
                String plot = rs.getString("plot");
                String rating = rs.getString("rating");
                String format = rs.getString("format");
                String year = rs.getString("year");
                String starringString = rs.getString("starring");
                int copies = rs.getInt("copies");
                String barcode = rs.getString("barcode");
                String user_rating = rs.getString("user_rating");

                String[] genreArray = genreString.split(",");
                String[] starringArray = starringString.split(",");
                ArrayList<String> genre = new ArrayList<>(Arrays.asList(genreArray));
                ArrayList<String> starring = new ArrayList<>(Arrays.asList(starringArray));

                m = new Movie(id, title, genre, director, runtime, rating, starring, copies, user_rating);
            }
        } catch (SQLException e)
        {
            throw new DaoException("findMovieByTitle() " + e.getMessage());
        } finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            } catch (SQLException e)
            {
                throw new DaoException("findMovieByTitle() " + e.getMessage());
            }
        }
        return m;
    }

    @Override
    public Movie findMovieById(Integer direct) throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Movie m = null;
        try
        {
            con = this.getConnection();

            String query = "SELECT * FROM movies WHERE id = ? ";
            ps = con.prepareStatement(query);
            ps.setInt(1, direct);

            rs = ps.executeQuery();
            if (rs.next())
            {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String genreString = rs.getString("genre");
                String director = rs.getString("director");
                String runtime = rs.getString("runtime");
                String plot = rs.getString("plot");
                String rating = rs.getString("rating");
                String format = rs.getString("format");
                String year = rs.getString("year");
                String starringString = rs.getString("starring");
                int copies = rs.getInt("copies");
                String barcode = rs.getString("barcode");
                String user_rating = rs.getString("user_rating");

                String[] genreArray = genreString.split(",");
                String[] starringArray = starringString.split(",");
                ArrayList<String> genre = new ArrayList<>(Arrays.asList(genreArray));
                ArrayList<String> starring = new ArrayList<>(Arrays.asList(starringArray));

                m = new Movie(id, title, genre, director, runtime, rating, starring, copies, user_rating);
            }
        } catch (SQLException e)
        {
            throw new DaoException("findMovieById() " + e.getMessage());
        } finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            } catch (SQLException e)
            {
                throw new DaoException("findMovieById() " + e.getMessage());
            }
        }
        return m;
    }

    @Override
    public List<Movie> findMoviesByGenre(String input) throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Movie m;

        System.out.println("input = " + input);
        ArrayList<Movie> movies = new ArrayList<>();
        try
        {
            con = this.getConnection();

            String query = "SELECT * FROM movies WHERE genre LIKE ? ";
            ps = con.prepareStatement(query);
            ps.setString(1, "%" + input + "%");
            //System.out.println("input = "+input);
            rs = ps.executeQuery();
            while (rs.next())
            {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String genreString = rs.getString("genre");
                String director = rs.getString("director");
                String runtime = rs.getString("runtime");
                String plot = rs.getString("plot");
                String rating = rs.getString("rating");
                String format = rs.getString("format");
                String year = rs.getString("year");
                String starringString = rs.getString("starring");
                int copies = rs.getInt("copies");
                String barcode = rs.getString("barcode");
                String user_rating = rs.getString("user_rating");

                String[] genreArray = genreString.split(",");
                String[] starringArray = starringString.split(",");
                ArrayList<String> genre = new ArrayList<>(Arrays.asList(genreArray));
                ArrayList<String> starring = new ArrayList<>(Arrays.asList(starringArray));
                System.out.println(genre);

                m = new Movie(id, title, genre, director, runtime, rating, starring, copies, user_rating);
                movies.add(m);
            }
        } catch (SQLException e)
        {

            throw new DaoException("findMoviesByGenre() " + e.getMessage());
        } finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            } catch (SQLException e)
            {
                throw new DaoException("findMoviesByGenre() " + e.getMessage());
            }
        }
        return movies;
    }

    @Override
    public List<Movie> findMoviesByGenres(String input, String input2) throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Movie m;

        ArrayList<Movie> movies = new ArrayList<>();
        try
        {
            con = this.getConnection();

            String query = "SELECT * FROM movies WHERE genre LIKE ? AND genre LIKE ?";
            ps = con.prepareStatement(query);
            ps.setString(1, "%" + input + "%");
            ps.setString(2, "%" + input2 + "%");

            rs = ps.executeQuery();
            while (rs.next())
            {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String genreString = rs.getString("genre");
                String director = rs.getString("director");
                String runtime = rs.getString("runtime");
                String plot = rs.getString("plot");
                String rating = rs.getString("rating");
                String format = rs.getString("format");
                String year = rs.getString("year");
                String starringString = rs.getString("starring");
                int copies = rs.getInt("copies");
                String barcode = rs.getString("barcode");
                String user_rating = rs.getString("user_rating");

                String[] genreArray = genreString.split(",");
                String[] starringArray = starringString.split(",");
                ArrayList<String> genre = new ArrayList<>(Arrays.asList(genreArray));
                ArrayList<String> starring = new ArrayList<>(Arrays.asList(starringArray));

                m = new Movie(id, title, genre, director, runtime, rating, starring, copies, user_rating);
                movies.add(m);
            }
        } catch (SQLException e)
        {

            throw new DaoException("findMoviesByGenres() " + e.getMessage());
        } finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            } catch (SQLException e)
            {
                throw new DaoException("findMoviesByGenres() " + e.getMessage());
            }
        }
        return movies;
    }

    public List<Movie> findMoviesByGenreThenDirector(String input, String input2) throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Movie m;

        ArrayList<Movie> movies = new ArrayList<>();
        try
        {
            con = this.getConnection();

            String query = "SELECT * FROM movies WHERE genre LIKE ? AND director LIKE ?";
            ps = con.prepareStatement(query);
            ps.setString(1, "%" + input + "%");
            ps.setString(2, "%" + input2 + "%");

            rs = ps.executeQuery();
            while (rs.next())
            {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String genreString = rs.getString("genre");
                String director = rs.getString("director");
                String runtime = rs.getString("runtime");
                String plot = rs.getString("plot");
                String rating = rs.getString("rating");
                String format = rs.getString("format");
                String year = rs.getString("year");
                String starringString = rs.getString("starring");
                int copies = rs.getInt("copies");
                String barcode = rs.getString("barcode");
                String user_rating = rs.getString("user_rating");

                String[] genreArray = genreString.split(",");
                String[] starringArray = starringString.split(",");
                ArrayList<String> genre = new ArrayList<>(Arrays.asList(genreArray));
                ArrayList<String> starring = new ArrayList<>(Arrays.asList(starringArray));

                m = new Movie(id, title, genre, director, runtime, rating, starring, copies, user_rating);
                movies.add(m);
            }
        } catch (SQLException e)
        {

            throw new DaoException("findMoviesByGenres() " + e.getMessage());
        } finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            } catch (SQLException e)
            {
                throw new DaoException("findMoviesByGenres() " + e.getMessage());
            }
        }
        return movies;
    }

    @Override
    public void deleteMovieById(Integer direct) throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            con = this.getConnection();

            String query = "DELETE FROM movies WHERE id = ? ";
            ps = con.prepareStatement(query);
            ps.setInt(1, direct);

            ps.executeUpdate();

        } catch (SQLException e)
        {
            throw new DaoException("deleteMovieById() " + e.getMessage());
        } finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            } catch (SQLException e)
            {
                throw new DaoException("deleteMovieById() " + e.getMessage());
            }
        }
    }

    @Override
    public void deleteMovieByTitle(String direct) throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            con = this.getConnection();

            String query = "DELETE FROM movies WHERE title = ? ";
            ps = con.prepareStatement(query);
            ps.setString(1, direct);

            ps.executeUpdate();

        } catch (SQLException e)
        {
            throw new DaoException("deleteMovieById() " + e.getMessage());
        } finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            } catch (SQLException e)
            {
                throw new DaoException("deleteMovieById() " + e.getMessage());
            }
        }
    }

    @Override
    public void addMovie(Movie m) throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();
            String query = "INSERT INTO movies(title, genre, director, runtime, rating, starring, copies, user_rating) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?);";

            //Using a PreparedStatement to execute SQL...
            String genre = "";
            String starring = "";

            for (int i = 0; i < m.getGenre().size(); i++)
            {
                genre = genre + m.getGenre().get(i).trim();

                if (i != m.getGenre().size())
                {
                    genre = genre + ",";
                }
            }
            for (int i = 0; i < m.getStarring().size(); i++)
            {
                starring = starring + m.getStarring().get(i).trim();
                if (i != m.getStarring().size())
                {
                    starring = starring + ",";
                }
            }

            ps = con.prepareStatement(query);
            ps.setString(1, m.getTitle());
            ps.setString(2, genre);
            ps.setString(3, m.getDirector());
            ps.setString(4, m.getRuntime());
            ps.setString(5, m.getRating());
            ps.setString(6, starring);
            ps.setInt(7, m.getCopies());
            ps.setString(8, m.getUser_rating());

            ps.execute();
        } catch (SQLException e)
        {
            throw new DaoException("add Movie() " + e.getMessage());
        }
    }

    @Override
    public void updateMovie(Movie m) throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Movie> movies = new ArrayList<>();

        try
        {
            con = this.getConnection();
            String query = "UPDATE movies SET title = ?, genre = ?, director = ?, runtime = ?, rating = ?, starring = ?, copies = ?, user_rating = ? WHERE id = ?";

            String genre = "";
            String starring = "";

            for (int i = 0; i < m.getGenre().size(); i++)
            {
                genre = genre + m.getGenre().get(i).trim();

                if (i != m.getGenre().size())
                {
                    genre = genre + ",";
                }
            }
            for (int i = 0; i < m.getStarring().size(); i++)
            {
                starring = starring + m.getStarring().get(i).trim();
                if (i != m.getStarring().size())
                {
                    starring = starring + ",";
                }
            }

            ps = con.prepareStatement(query);
            ps.setString(1, m.getTitle());
            ps.setString(2, genre);
            ps.setString(3, m.getDirector());
            ps.setString(4, m.getRuntime());
            ps.setString(5, m.getRating());
            ps.setString(6, starring);
            ps.setInt(7, m.getCopies());
            ps.setString(8, m.getUser_rating());
            ps.setInt(9, m.getId());

            ps.execute();
        } catch (SQLException e)
        {
            throw new DaoException("add Movie() " + e.getMessage());
        }
    }
}
