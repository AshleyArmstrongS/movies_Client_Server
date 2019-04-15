package BuissnessObject;

import DTOs.Movie;
import DTOs.MoviesWatched;
import DAOs.MySqlMovieDao;
import DAOs.MovieDaoInterface;
import DAOs.MoviesWatchedDaoInterface;
import DAOs.MySqlMoviesWatchedDao;
import Exceptions.DaoException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 *
 * @author Administrator
 */
public class Server {

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start() {
        try
        {
            Cache<String, String> cacheMap = new Cache();

            ServerSocket ss = new ServerSocket(8080);//set port

            System.out.println("Server: Server started. Listening for connections on port 8080...");

            int clientNumber = 0;  //client no

            while (true)  //new client loop
            {
                Socket socket = ss.accept();   //wait for connection
                //client numbering
                clientNumber++;

                System.out.println("Server: Client " + clientNumber + " has connected.");

                System.out.println("Server: Port# of remote client: " + socket.getPort());
                System.out.println("Server: Port# of this server: " + socket.getLocalPort());

                Thread t = new Thread(new ClientHandler(socket, clientNumber, cacheMap)); //new client handler for each client
                t.start();                                                  // and run it in its own thread

                System.out.println("Server: ClientHandler started in thread for client " + clientNumber + ". ");
                System.out.println("Server: Listening for further connections...");
            }
        } catch (IOException e)
        {
            System.out.println("Server: IOException: " + e);
        }
        System.out.println("Server: Server exiting, Goodbye!");
    }

    public class ClientHandler implements Runnable // each ClientHandler communicates with one Client
    {

        BufferedReader socketReader;
        PrintWriter socketWriter;
        Socket socket;
        int clientNumber;
        Cache cacheMap;

        public ClientHandler(Socket clientSocket, int clientNumber, Cache cacheMap) {

            try
            {
                InputStreamReader isReader = new InputStreamReader(clientSocket.getInputStream());
                this.socketReader = new BufferedReader(isReader);

                OutputStream os = clientSocket.getOutputStream();
                this.socketWriter = new PrintWriter(os, true); // true => auto flush socket buffer

                this.clientNumber = clientNumber;  // ID number that we are assigning to this client

                this.socket = clientSocket;  // store socket ref for closing
                this.cacheMap = cacheMap;
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            String message;
            Boolean socketClose = false;
            try
            {
                while ((message = socketReader.readLine()) != null)
                {
                    System.out.println(message);
                    /*
                    String is taken from the client in json format, all input contains a key called "serverCommand",
                    this selects the function on the server end.
                     */
                    String returnToClient;
                    MovieDaoInterface IMovieDao = new MySqlMovieDao();
                    MoviesWatchedDaoInterface IMoviesWatchedDao = new MySqlMoviesWatchedDao();

                    JsonObject fromClient = jsonFromString(message);
                    String serverCommand = fromClient.getString("serverCommand");//extracts the command given by the client
                    System.out.println(serverCommand);                          // prints the command from the client side to be used on the server

                    if (userCommands().contains(serverCommand))                 //checks if the given command is supported
                    {

                        switch (serverCommand)
                        {
                            // closes socket to end the server
                            case "CLOSESOCKET":
                                socketClose = true;
                                returnToClient = "{\"type\" : \"message\", \"message\" : \"Server Socket closed\"}";
                                break;
                            // deletes a movie from the database
                            case "DELETEBYID":
                                returnToClient = deletebyId(cacheMap, fromClient, IMovieDao);
                                cacheMap.clearCache();
                                break;
                            // adds a movie to the database
                            case "ADDMOVIE":
                                returnToClient = addNewMovie(cacheMap, fromClient, IMovieDao);
                                cacheMap.clearCache();
                                break;
                            // updates an exsisting movie in the database, is the second call made in updating a movie
                            case "UPDATEMOVIETWO":
                                returnToClient = updateMovie(cacheMap, fromClient, IMovieDao);
                                cacheMap.clearCache();
                                break;
                            // adds a movie/user to the watchedmovies table
                            case "WATCH":
                                returnToClient = watch(fromClient, IMoviesWatchedDao, IMovieDao);
                                cacheMap.clearCache();
                                break;
                            case "FINDBYGENRES":
                                returnToClient = runMultiFindBys(cacheMap, fromClient, IMovieDao, IMoviesWatchedDao);
                                break;
                            case "FINDBYGENRETHENDIRECTOR":
                                returnToClient = runMultiFindBys(cacheMap, fromClient, IMovieDao, IMoviesWatchedDao);
                                break;
                            default:
                                returnToClient = runFindBys(cacheMap, fromClient, IMovieDao, IMoviesWatchedDao);
                                break;
                        }
                        System.out.println(returnToClient);
                        socketWriter.println(returnToClient);
                        System.out.println("Response sent to client");

                    }
                    else
                    {
                        returnToClient = "{\"type\": \"message\", \"message\": \"Command  was not accpeted\"}";
                        socketWriter.println(returnToClient);
                    }
                }
                if (socketClose)
                {
                    socket.close();
                }
            } catch (IOException | DaoException ex)
            {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Server: (ClientHandler): Handler for Client " + clientNumber + " is terminating .....");
        }
    }

    public static String runFindBys(Cache c, JsonObject fromClient, MovieDaoInterface IMovieDao, MoviesWatchedDaoInterface IMoviesWatchedDao) throws DaoException {
        String serverCommand = fromClient.getString("serverCommand");                  //get find by type
        String findByVariable = fromClient.getString("findByVariable").trim();         //get variable to find by
        String movie = "{\"type\": \"message\", \"message\": \"Command  was not accpeted\"}";

        if (c.checkCache(fromClient.toString()))
        {
            System.out.println("Got from cache");
            return (String) c.returnFromCache(fromClient.toString());
        }
        else
        {
            switch (serverCommand)
            {
                case "FINDBYDIRECTOR":
                    movie = findByDirector(findByVariable, IMovieDao);
                    break;
                case "FINDBYTITLE":
                    movie = findByTitle(findByVariable, IMovieDao);
                    break;
                case "FINDBYID":
                    movie = findById(findByVariable, IMovieDao);
                    break;
                case "FINDBYGENRE":
                    movie = findByGenre(findByVariable, IMovieDao);
                    break;
                case "RECOMMEND":
                    movie = recommend(findByVariable, IMovieDao, IMoviesWatchedDao);
                    break;
                case "GETWATCHED":
                    movie = getWatchedMovies(findByVariable, IMovieDao, IMoviesWatchedDao);
                    break;
                default:
                    break;
            }
            System.out.println("Got from database");
        }

        c.addToCache(fromClient.toString(), movie);
        return movie;
    }

    public static String findAllMovies(MovieDaoInterface IMovieDao) throws DaoException {
        List<Movie> movies = IMovieDao.findAllMovies();                         //gets all movies from the db

        if (!movies.isEmpty())                                                  //checks if the list is empty
        {
            return jsonMovieArrayFormatter(movies);                             //returns as an array as multiple are likely to be returned
        }
        else
        {
            return "{\"type\": \"message\", \"message\": \"Error no movies in DB\"}";
        }
    }

    public static String findByDirector(String findByVariable, MovieDaoInterface IMovieDao) throws DaoException {
        List<Movie> dMovies = IMovieDao.findMovieByDirector(findByVariable); //gets the movies

        if (!dMovies.isEmpty())                                                    //checks if the list is empty
        {
            return jsonMovieArrayFormatter(dMovies);                            //returns as an array as multiple are likely to be returned
        }
        else
        {
            return "{\"type\": \"message\", \"message\": \"Couldnt find any movies with the director " + findByVariable + "\"}}";
        }
    }

    public static String findById(String findByVariable, MovieDaoInterface IMovieDao) throws DaoException {
        Movie movie = IMovieDao.findMovieById(Integer.parseInt(findByVariable)); //gets the movie with the id

        if (movie != null)                                                      //checks if a movie was returned
        {
            return jsonFormatter(movie, false);                                 //returns the movie and tells the formatter that it is not in an array so that it adds the type field
        }
        else
        {
            return "{\"type\": \"message\", \"message\": \"Couldnt find a movie with the Id " + findByVariable + "\"}";
        }
    }

    public static String findByGenre(String findByVariable, MovieDaoInterface IMovieDao) throws DaoException {
        List<Movie> movies = IMovieDao.findMoviesByGenre(findByVariable); //gets the movies

        if (!movies.isEmpty())                                                  //checks if the list is empty
        {
            return jsonMovieArrayFormatter(movies);                             //returns as an array as multiple are likely to be returned
        }
        else
        {
            return "{\"type\": \"message\", \"message\": \"Couldnt find any movies with the genre " + findByVariable + "\"}";
        }
    }

    public static String findByTitle(String findByVariable, MovieDaoInterface IMovieDao) throws DaoException {
        Movie movie = IMovieDao.findMovieByTitle(findByVariable);    //gets the movie
        if (movie != null)                                                      //checks if the movie was found
        {
            return jsonFormatter(movie, false);                                 //returns the movie and tells the formatter that it is not in an array so that it adds the type field

        }
        else
        {
            return "{\"type\": \"message\", \"message\": \"Couldnt find any movies with the title " + findByVariable + "\"}";
        }
    }

    public static String runMultiFindBys(Cache c, JsonObject fromClient, MovieDaoInterface IMovieDao, MoviesWatchedDaoInterface IMoviesWatchedDao) throws DaoException {
        String serverCommand = fromClient.getString("serverCommand");                  //get find by type
        String findByVariable1 = fromClient.getString("findByVariable1").trim();
        String findByVariable2 = fromClient.getString("findByVariable2").trim();         //get variable to find by
        String movie = "{\"type\": \"message\", \"message\": \"Command  was not accpeted\"}";

        if (c.checkCache(fromClient.toString()))
        {
            System.out.println("Got from cache");
            return (String) c.returnFromCache(fromClient.toString());
        }
        else
        {
            switch (serverCommand)
            {
                case "FINDBYGENRES":
                    movie = findByGenres(findByVariable1, findByVariable2, IMovieDao);
                    break;
                case "FINDBYTITLE":
                    movie = findByGenreThenDirector(findByVariable1, findByVariable2, IMovieDao);
                    break;
            }
        }
        c.addToCache(fromClient.toString(), movie);
        return movie;
    }

    public static String findByGenres(String findByVariable1, String findByVariable2, MovieDaoInterface IMovieDao) throws DaoException {

        List<Movie> movies = IMovieDao.findMoviesByGenres(findByVariable1, findByVariable2); //gets the movies
        if (!movies.isEmpty())                                                  //checks if the list is empty
        {
            return jsonMovieArrayFormatter(movies);                             //returns as an array as multiple are likely to be returned
        }
        else
        {
            return "{\"type\": \"message\", \"message\": \"Couldnt find any movies with the genres " + findByVariable1 + "\" and \"" + findByVariable2 + "\"}";
        }
    }

    public static String findByGenreThenDirector(String findByVariable1, String findByVariable2, MovieDaoInterface IMovieDao) throws DaoException {

        List<Movie> movies = IMovieDao.findMoviesByGenreThenDirector(findByVariable1, findByVariable2); //gets the movies
        if (!movies.isEmpty())                                                  //checks if the list is empty
        {
            return jsonMovieArrayFormatter(movies);                             //returns as an array as multiple are likely to be returned
        }
        else
        {
            return "{\"type\": \"message\", \"message\": \"Couldnt find any movies with the genre " + findByVariable1 + " and director " + findByVariable2 + "\"}";
        }
    }

    public static String updateMovie(Cache c, JsonObject fromClient, MovieDaoInterface IMovieDao) throws DaoException {
        Movie m = jsonToMovie(fromClient, true);
        IMovieDao.updateMovie(m);                                               //runs the update on the movie
        Movie x = IMovieDao.findMovieById(m.getId());                           //gets the movie from the db
        if (m.equals(x))                                                        //compare the movie from the db with the db from the client
        {
            c.clearCache();
            return "{\"type\": \"message\", \"message\": \"Movie " + m.getTitle() + " Updated\"}";
        }
        else
        {
            return "{\"type\": \"message\", \"message\": \"Error movie " + m.getTitle() + " not updated\"}";
        }
    }

    public static String addNewMovie(Cache c, JsonObject fromClient, MovieDaoInterface IMovieDao) throws DaoException {
        Movie m = jsonToMovie(fromClient, false);
        IMovieDao.addMovie(m);
        if (findByTitle(m.getTitle(), IMovieDao) != null)
        {
            c.clearCache();
            return "{\"type\": \"message\", \"message\":\"Movie " + m.getTitle() + " added\"}";
        }
        else
        {
            return "{\"type\": \"message\", \"message\":\"Error movie " + m.getTitle() + " not added\"}";
        }
    }

    public static String deletebyId(Cache c, JsonObject fromClient, MovieDaoInterface IMovieDao) throws DaoException {

        int movieToDelete = Integer.parseInt(fromClient.getString("deleteByVariable"));//extracts the id of the movie to delete
        IMovieDao.deleteMovieById(movieToDelete);                               //deletes the movie from the db

        if (IMovieDao.findMovieById(movieToDelete) == null)                     //checks the db for the movie
        {
            c.clearCache();
            return "{\"type\": \"message\", \"message\": \" Movie " + fromClient.getString("deleteByVariable") + " deleted\"}";
        }
        else
        {
            return "{\"type\": \"message\", \"message\": \"Error movie " + fromClient.getString("deleteByVariable") + " not deleted, ensure id is correct\"}";
        }
    }

    public static String deletebyTitle(JsonObject fromClient, MovieDaoInterface IMovieDao) throws DaoException {

        String movieToDelete = fromClient.getString("deleteByVariable");//extracts the title of the movie to delete
        IMovieDao.deleteMovieByTitle(movieToDelete);                               //deletes the movie from the db

        if (IMovieDao.findMovieByTitle(movieToDelete) == null)                     //checks the db for the movie
        {
            System.out.println("check");
            return "{\"type\": \"message\", \"message\": \" Movie " + fromClient.getString("deleteByVariable") + " deleted\"}";
        }
        else
        {
            System.out.println("not check");
            return "{\"type\": \"message\", \"message\": \"Error movie " + fromClient.getString("deleteByVariable") + " not deleted, ensure title is correct\"}";
        }
    }

    public static String watch(JsonObject fromClient, MoviesWatchedDaoInterface IMoviesWatchedDao, MovieDaoInterface IMovieDao) throws DaoException {

        String username = fromClient.getString("username");                     //takes in username
        String movieToWatch;
        movieToWatch = fromClient.getString("movieTitle");                      //takes in movie to add

        Movie movie = IMovieDao.findMovieByTitle(movieToWatch.trim());          //finds the movie

        System.out.println(movie);
        if (movie != null)                                                      //checks if a movie object was returned, if the movie title given was present
        {
            int id = movie.getId();                                             //extracts the id of the movie
            IMoviesWatchedDao.addMovieWatched(username, id);                    // adds movie and username to the table
            return "{\"type\": \"message\", \"message\": \"Movie added to watched\"}";
        }
        else
        {
            return "{\"type\": \"message\", \"message\": \"No movie with this title in the database\"}";
        }
    }

    public static String getWatchedMovies(String findByVariable, MovieDaoInterface IMovieDao, MoviesWatchedDaoInterface IMoviesWatchedDao) throws DaoException {
        String username = findByVariable;

        List<MoviesWatched> watched = IMoviesWatchedDao.getMoviesWatched(username);
        List<Movie> moviesWatched = new ArrayList<>();
        if (watched.isEmpty())
        {
            return "{\"type\": \"message\", \"message\": \"" + username + " hasnt watched any movies\"}";
        }
        else
        {
            for (MoviesWatched movie : watched)
            {

                moviesWatched.add(IMovieDao.findMovieById(movie.getId()));
            }
            return jsonMovieArrayFormatter(moviesWatched);
        }
    }

    public static String recommend(String findByVariable, MovieDaoInterface IMovieDao, MoviesWatchedDaoInterface IMoviesWatchedDao) throws DaoException {
        String username = findByVariable;

        //Gets movies watched in MoviesWatched format
        List<MoviesWatched> watched = IMoviesWatchedDao.getMoviesWatched(username);

        if (watched.isEmpty())
        {
            System.out.println("");
            return "{\"type\": \"message\", \"message\" : \"Cant reccommend movies for " + username + " as none have been watched\"}";
        }
        else
        {
            List<Movie> movieList = new ArrayList<>();

            //Gets movie objects using movie id from movies watched
            for (MoviesWatched m : watched)
            {
                movieList.add(IMovieDao.findMovieById(m.getId()));
            }

            List<String> genres = new ArrayList<>();

            //gets genres of movies watched
            for (Movie movie : movieList)
            {
                for (String g : movie.getGenre())
                {
                    genres.add(g);
                }
            }

            //gets most common genre
            String mostCommon = IMoviesWatchedDao.mostCommonGenre(genres);

            if (movieList.size() < 4)
            {
                List<Movie> reccommended = IMovieDao.findMoviesByGenre(mostCommon);

                //recomends 10 random movied in reccommended list
                List<Movie> rand10 = random10(movieList, reccommended);

                return jsonMovieArrayFormatter(rand10);
            }
            else
            {
                String sndMostCommon = IMoviesWatchedDao.sndMostCommonGenre(genres);
                List<Movie> reccommended = IMovieDao.findMoviesByGenres(mostCommon, sndMostCommon);

                List<Movie> rand10 = random10(movieList, reccommended);

                return jsonMovieArrayFormatter(rand10);
            }

        }
    }

    public static List<Movie> random10(List<Movie> movieList, List<Movie> reccommended) {
        List<Movie> rand10 = new ArrayList<>();
        Random rand = new Random();
        int i = 0;
        while (i < 10)
        {
            int randNum = rand.nextInt(reccommended.size());
            if (!movieList.contains(reccommended.get(randNum)))
            {
                rand10.add(reccommended.get(randNum));
                i++;
            }
        }
        return rand10;
    }

    public static String jsonFormatter(Movie m, Boolean isArray) {

        String JSON = "{";
        if (!isArray)
        {
            JSON = JSON + "\"type\": \"movie\", ";
        }
        return JSON + "\"Id\": \"" + m.getId()
                + "\", \"title\": \"" + m.getTitle()
                + "\", \"genre\": " + jsonArrayFormatter(m.getGenre()) //calls the method to correctly format the genre array into a json array
                + ", \"starring\": " + jsonArrayFormatter(m.getStarring()) //calls the method to correctly format the starring array into a json array
                + ", \"runtime\": \"" + m.getRuntime()
                + "\", \"rating\": \"" + m.getRating()
                + "\", \"director\": \"" + m.getDirector()
                + "\", \"user_rating\": \"" + m.getUser_rating()
                + "\", \"copies\": \"" + m.getCopies()
                + "\" }";

    }

    public static String jsonArrayFormatter(ArrayList<String> arrayIn) {

        String jsonArray = "[ ";
        for (int i = 0; i < arrayIn.size(); i++)
        {
            jsonArray = jsonArray + "\"" + arrayIn.get(i) + "\"";
            if (i != arrayIn.size() - 1)
            {
                jsonArray = jsonArray + ", ";
            }
        }
        jsonArray = jsonArray + "]";
        return jsonArray;
    }

    public static String jsonMovieArrayFormatter(List<Movie> movies) {
        String array = "{\"type\": \"movieArray\", \"movies\": [";

        for (int i = 0; i < movies.size(); i++)                                 // this type of for loop is required so the right formatting can be used
        {
            array = array + jsonFormatter(movies.get(i), true);

            if (i != movies.size() - 1)
            {
                array = array + ", ";
            }
        }
        array = array + "]}";

        return array;
    }

    public static JsonObject jsonFromString(String jsonObjectStr) {
        JsonObject object;
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonObjectStr)))
        {
            object = jsonReader.readObject();
        }

        return object;
    }

    public static Movie jsonToMovie(JsonObject clientMovie, Boolean update) {
        Movie m = new Movie();
        if (update)                                                             //if update is true then the id is used as it identifies the movie to be updated. add cant have an id as it is auto incremented by the db
        {
            m.setId(Integer.parseInt(clientMovie.getString("Id")));
        }
        m.setTitle(clientMovie.getString("title"));

        m.setGenre(jsonArrayToArrayList(clientMovie, "genre"));                 //calls the method to extract the json array of genre and make it into an arraylist

        m.setDirector(clientMovie.getString("director"));
        m.setRuntime(clientMovie.getString("runtime"));
        m.setRating(clientMovie.getString("rating"));

        m.setStarring(jsonArrayToArrayList(clientMovie, "starring"));           //calls the method to extract the json array of starring and make it into an arraylist

        m.setCopies(Integer.parseInt(clientMovie.getString("copies")));
        m.setUser_rating(clientMovie.getString("user_rating"));

        return m;
    }

    public static ArrayList<String> jsonArrayToArrayList(JsonObject jsonMovie, String arrayName) {
        ArrayList<String> newArray = new ArrayList<>();         //initialise arraylist
        JsonArray jsonArray = jsonMovie.getJsonArray(arrayName);  //extract array from json
        for (int i = 0; i < jsonArray.size(); i++)
        {
            newArray.add(jsonArray.getString(i));               // adds to the arraylist from the json array
        }
        return newArray;
    }

    public static ArrayList<String> userCommands() {

        ArrayList<String> userCommands = new ArrayList<>();
        userCommands.add("FINDBYDIRECTOR");//find bys
        userCommands.add("FINDALLMOVIES");
        userCommands.add("FINDBYTITLE");
        userCommands.add("FINDBYID");
        userCommands.add("RECOMMEND");
        userCommands.add("GETWATCHED");
        userCommands.add("FINDBYGENRE");
        userCommands.add("FINDBYGENRES");
        userCommands.add("FINDBYGENRETHENDIRECTOR");

        userCommands.add("DELETEBYID");//deletes
        userCommands.add("DELETEBYTITLE");

        userCommands.add("WATCH");      //update/adds
        userCommands.add("UPDATEMOVIE");
        userCommands.add("ADDMOVIE");

        userCommands.add("EXIT");//exit

        return userCommands;

    }

}
