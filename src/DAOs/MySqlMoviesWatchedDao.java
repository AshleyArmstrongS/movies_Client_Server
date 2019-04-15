package DAOs;

import DTOs.MoviesWatched;
import Exceptions.DaoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author racheldhc
 */
public class MySqlMoviesWatchedDao extends MySqlDao implements MoviesWatchedDaoInterface {

    @Override
    public void addMovieWatched(String username, int id) throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();
            String query = "INSERT INTO movieswatched(username, movieId, timesWatched) VALUES (?, ?, 1);";

            //Using a PreparedStatement to execute SQL...
            ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setInt(2, id);

            ps.execute();
        } catch (SQLException e)
        {

            throw new DaoException("add MovieWatched() " + e.getMessage());
        }
    }

    @Override
    public List<MoviesWatched>getMoviesWatched(String username) throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<MoviesWatched> movieswatched = new ArrayList<>();

        try
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();

            String query = "SELECT * FROM movieswatched where username = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, username);

            //Using a PreparedStatement to execute SQL...
            rs = ps.executeQuery();
            while (rs.next())
            {
                String user = rs.getString("username");
                int id = rs.getInt("movieId");
                int timesWatched = rs.getInt("timesWatched");

                MoviesWatched m = new MoviesWatched(user, id, timesWatched);
                movieswatched.add(m);

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
        return movieswatched;
    }

    @Override
    public String mostCommon(List<String> variables) {
        HashMap<String, Integer> map = new HashMap<>();

        for (String variable : variables)
        {
            Integer val = map.get(variable);
            map.put(variable, val == null ? 1 : val + 1);
        }

        Entry<String, Integer> max = null;

        for (Entry<String, Integer> e : map.entrySet())
        {
            if (max == null || e.getValue() > max.getValue())
            {
                max = e;
            }
        }

        return max.getKey();
    }

    @Override
    public String sndMostCommon(List<String> genres) {
        HashMap<String, Integer> map = new HashMap<>();

        for (String genre : genres)
        {
            Integer val = map.get(genre);
            map.put(genre, val == null ? 1 : val + 1);
        }

        Entry<String, Integer> max = null;
        Entry<String, Integer> snd = null;

        for (Entry<String, Integer> e : map.entrySet())
        {
            if (max == null || e.getValue() > max.getValue())
            {
                max = e;
            }
            else if (snd == null || e.getValue() < max.getValue() && e.getValue() > snd.getValue())
            {
                snd = e;
            }
        }

        return snd.getKey();
    }

}
