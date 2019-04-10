package Main;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import static java.lang.System.out;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

/**
 *
 * @author
 */
public class Client {

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    public void start() {
        try
        {
            Scanner in = new Scanner(System.in);
            Socket socket = new Socket("localhost", 8080);

            System.out.println("Client: This Client is running and has connected to the server");
            boolean ClientRun = true;
            boolean dontPrintReturn;
            while (ClientRun)
            {
                dontPrintReturn = false;
                ArrayList<String> userCommands = userCommands();
                System.out.print("User Commands   ");
                for (String c : userCommands)
                {
                    System.out.print(" : " + c + " : ");
                }
                System.out.println("Please enter a command: ");
                String userCommandInput = in.nextLine().toUpperCase();
                if (userCommands.contains(userCommandInput))        //compares the user value with an arraylist of values to ensure its valid
                {
                    switch (userCommandInput)
                    {
                        case "EXIT":                                            //exists the client closes the socket
                            socket.close();
                            out.close();
                            ClientRun = false;
                            break;
                        case "ADDMOVIE":
                            runAddMovie(socket);                                //runs the method to add a new movie
                            break;
                        case "UPDATEMOVIE":
                            runUpdateMovie(socket);                             //runs the method to update an existing movie present in the db
                            break;
                        case "WATCH":
                            runWatch(socket);                                   //runs the method to watch a movie adding it and the user to the db
                            break;
                        //
                        case "FINDBYGENRES":
                            runFindByMultiple(socket, userCommandInput);        //runs the method to find movies by more than one variable
                            break;
                        case "FINDBYGENRETHENDIRECTOR":
                            runFindByMultiple(socket, userCommandInput);                                  //runs the method to find movies by more than one variable
                            break;
                        case "DELETEBYID":
                            deleteById(socket);                                  //runs the method to delete a movie from the db
                            break;
                        case "DELETEBYTITLE":
                            deleteByTitle(socket);                                  //runs the method to delete a movie from the db
                            break;
                        default:
                            runSingleFinds(socket, userCommandInput);           //runs the method to get a findby from the server. takes in the user command to determine the findbytype
                            break;

                    }
                }
                else                                                            //print out if user command is invalid
                {
                    System.out.println("Invalid command");
                    dontPrintReturn = true;
                }
                if (!dontPrintReturn)
                {
                    printRun(socket);                                               //prints the final return for a command from the server
                }
            }
        } catch (IOException e)
        {
            System.out.println("Client message: IOException: " + e);
        }
    }

    public static void runAddMovie(Socket socket) throws IOException {
        OutputStream os = socket.getOutputStream();                     //To server
        PrintWriter socketWriter = new PrintWriter(os, true);           //Writes to the socket

        String sendToServer = addMovie();                               //runs addMovie and recieves the json formmated movie
        socketWriter.println(sendToServer);                             //sends the movie to the server 
    }

    public static String addMovie() {
        Scanner in = new Scanner(System.in);

        System.out.println("Title");
        String title = in.nextLine();
        System.out.println("genre comma seperated");                                // comma seperated for genres to be interpeted into an array
        String genreString = in.nextLine();
        System.out.println("director");
        String director = in.nextLine();
        System.out.println("runtime");
        String runtime = in.nextLine();
        System.out.println("rating");
        String rating = in.nextLine();
        System.out.println("starring comma seperated");                              // comma seperated for stars to be interpeted into an array
        String starringString = in.nextLine();
        System.out.println("copies");
        String copies = in.nextLine();
        System.out.println("user-rating");
        String userRating = in.next();

        String[] genreArray = genreString.split(",");                               // splits up the genres into an array allowing them to be added into an arraylist
        ArrayList<String> genre = new ArrayList<>(Arrays.asList(genreArray));       // puts the genres into an arraylist so it can be parsed by the jsonMovieFormatter

        String[] starringArray = starringString.split(",");                         // splits up the stars into an array allowing them to be added into an arraylist
        ArrayList<String> starring = new ArrayList<>(Arrays.asList(starringArray)); // puts the stars into an arraylist so it can be parsed by the jsonMovieFormatter

        return jsonMovieFormatter("ADDMOVIE", null, title, genre, director, runtime, rating, starring, copies, userRating); // returns the movie, the function name and null for Id as it is not used for add
    }

    public static void runUpdateMovie(Socket socket) throws IOException {
        Scanner in = new Scanner(System.in);
        OutputStream os = socket.getOutputStream();                             //To server
        PrintWriter socketWriter = new PrintWriter(os, true);
        boolean inValidReturn = true;
        while (inValidReturn)                                                   //ensures the reutrn value is a movie if not asks for another title
        {
            System.out.println("Enter title");
            String findByVariable = in.nextLine();                              //takes in the movie title to update

            String sendToServer = "{\"serverCommand\": \"FINDBYTITLE\",\"findByVariable\": \"" + findByVariable + "\"}"; //create json string, is short so no need for a formatter
            socketWriter.println(sendToServer);                                 //sends the updateOne to the server

            Scanner socketReader = new Scanner(socket.getInputStream());        //creates the socket reader to recieve the return from the server    
            String returnedString = socketReader.nextLine();                    //extracts the string from the socket reader
            JsonObject jsonReturn = jsonFromString(returnedString);             //turns the string into a json object
            if (jsonReturn.getString("type").equals("movie"))
            {
                socketWriter.flush();
                String sendToserver = updateMovieTwo(jsonReturn);
                socketWriter.println(sendToServer);
                inValidReturn = false;
            }
            else
            {
                System.out.println("movie does not exsist");
            }
        }
    }

    public static String updateMovieTwo(JsonObject movie) {
        Scanner sc = new Scanner(System.in);

        String Id = movie.getString("Id");              //takes the movie json object and initilises a list of varibles which can be overided or passed back to the server for updating
        String title = movie.getString("title");

        ArrayList<String> genreArrayJsonTemp = jsonArrayToArrayList(movie, "genre"); // creates an arraylist of genres

        String director = movie.getString("director");
        String runtime = movie.getString("runtime");
        String rating = movie.getString("rating");

        ArrayList<String> starringArrayJsonTemp = jsonArrayToArrayList(movie, "starring"); // creates an arraylist of stars

        String copies = movie.getString("copies");
        String user_rating = movie.getString("user_rating");

        String inputString;
        ArrayList<String> genreArrayList;
        ArrayList<String> starringArrayList;

        System.out.println("Leave blank if you do not wish to change"); //asks for new varibles but if left blank the old ones are used
        System.out.println("Please enter title");
        inputString = sc.nextLine();
        if (!inputString.equals(""))
        {
            title = inputString;
        }
        System.out.println("Please enter genre , seperated");
        inputString = sc.nextLine();
        if (!inputString.equals(""))
        {
            String[] genreArray = inputString.split(",");                       //creates an array of genres
            genreArrayList = new ArrayList<>(Arrays.asList(genreArray));        //sets the arraylist to use the new variables
        }
        else
        {
            genreArrayList = genreArrayJsonTemp;                                //sets the arraylist to use the old variables
        }
        System.out.println("Please enter director");
        inputString = sc.nextLine();
        if (!inputString.equals(""))
        {
            director = inputString;
        }
        System.out.println("Please enter runtime");
        inputString = sc.nextLine();
        if (!inputString.equals(""))
        {
            runtime = inputString;
        }
        System.out.println("Please enter rating");
        inputString = sc.nextLine();
        if (!inputString.equals(""))
        {
            rating = inputString;
        }
        System.out.println("Please enter starring");
        inputString = sc.nextLine();
        if (!inputString.equals(""))
        {
            String[] starringArray = inputString.split(",");                    //creates an array of genres
            starringArrayList = new ArrayList<>(Arrays.asList(starringArray));  //sets the arraylist to use the new variables
        }
        else
        {
            starringArrayList = starringArrayJsonTemp;                          //sets the arraylist to use the old variables
        }
        System.out.println("Please enter user_rating");
        inputString = sc.nextLine();
        if (!inputString.equals(""))
        {
            user_rating = inputString;
        }
        System.out.println("Please enter copies");
        inputString = sc.nextLine();
        if (!inputString.equals(""))
        {
            copies = inputString;
        }
        return jsonMovieFormatter("UPDATEMOVIETWO", Id, title, genreArrayList, director, runtime, rating, starringArrayList, copies, user_rating);
    }

    public static void runWatch(Socket socket) throws IOException {
        Scanner in = new Scanner(System.in);                        //keyboard
        OutputStream os = socket.getOutputStream();                 //To server
        PrintWriter socketWriter = new PrintWriter(os, true);       //Writes to the socket

        System.out.println("Please enter username.");               //username of the user watching the movie
        String username = in.nextLine();
        System.out.println("Please enter watched movie title");     //movie to be added to the users watch list
        String movieTitle = in.nextLine();

        String sendToServer = "{\"serverCommand\": \"WATCH\", \"username\": \"" + username + "\", \"movieTitle\": \"" + movieTitle + "\" }"; //create json string, is short so no need for a formatter
        socketWriter.println(sendToServer);                         //sends the json to the server
    }

    public static void runSingleFinds(Socket socket, String FindByType) throws IOException {
        Scanner in = new Scanner(System.in);                        //keyboard
        OutputStream os = socket.getOutputStream();                 //To server
        PrintWriter socketWriter = new PrintWriter(os, true);       //Writes to the socket

        System.out.println("Enter the variable to find by");
        String findByVariable = in.nextLine();                      //takes in variable used to identify the movie i.e(id,title)

        String sendToServer = "{\"serverCommand\": \"" + FindByType + "\",\"findByVariable\": \"" + findByVariable + "\"}"; //create json string, is short so no need for a formatter
        socketWriter.println(sendToServer);                         //sends the json to the server
    }

    public static void runFindByMultiple(Socket socket, String FindByType) throws IOException {
        Scanner in = new Scanner(System.in);                        //keyboard
        OutputStream os = socket.getOutputStream();                 //To server
        PrintWriter socketWriter = new PrintWriter(os, true);       //Writes to the socket

        System.out.println("Enter the variable to find by");
        String findByVariable1 = in.nextLine();                      //takes in variable used to identify the movie i.e(id,title)
        System.out.println("Enter the variable to find by");
        String findByVariable2 = in.nextLine();                      //takes in variable used to identify the movie i.e(id,title)

        String sendToServer = "{\"serverCommand\": \"" + FindByType + "\",\"findByVariable1\": \"" + findByVariable1 + "\",\"findByVariable2\": \"" + findByVariable2 + "\"}"; //create json string, is short so no need for a formatter
        socketWriter.println(sendToServer);                         //sends the json to the server
    }

    public static void deleteById(Socket socket) throws IOException {
        Scanner in = new Scanner(System.in);                        //keyboard
        OutputStream os = socket.getOutputStream();                 //To server
        PrintWriter socketWriter = new PrintWriter(os, true);       //Writes to the socket

        System.out.println("Enter the Id of the movie to be deleted");
        String deleteByVariable = in.nextLine();                      //takes in variable used to identify the movie

        String sendToServer = "{\"serverCommand\": \"DELETEBYID\",\"deleteByVariable\": \"" + deleteByVariable + "\"}"; //create json string, is short so no need for a formatter
        socketWriter.println(sendToServer);                         //sends the json to the server                             //extract the return and prints the message
    }

    public static void deleteByTitle(Socket socket) throws IOException {
        Scanner in = new Scanner(System.in);                        //keyboard
        OutputStream os = socket.getOutputStream();                 //To server
        PrintWriter socketWriter = new PrintWriter(os, true);       //Writes to the socket

        System.out.println("Enter the title of the movie to be deleted");
        String deleteByVariable = in.nextLine();                      //takes in variable used to identify the movie

        String sendToServer = "{\"serverCommand\": \"DELETEBYTITLE\",\"deleteByVariable\": \"" + deleteByVariable + "\"}"; //create json string, is short so no need for a formatter
        socketWriter.println(sendToServer);                         //sends the json to the server                             //extract the return and prints the message

    }

    public static JsonObject jsonFromString(String jsonObjectStr) {

        JsonObject object;                      //initialize
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonObjectStr)))
        {
            object = jsonReader.readObject(); //interpets the string
        }

        return object;
    }

    public static String jsonMovieFormatter(String functionName, String Id, String title, ArrayList<String> genre, String director, String runtime, String rating, ArrayList<String> starring, String copies, String userRating) {
        String IdString = "";
        if (functionName.equals("UPDATEMOVIETWO"))                          //checks if the functionName = update. If so the id is requied to be sent to the server so it is added to the return
        {
            IdString = "\",\"Id\": \"" + Id;
        }
        return "{\"serverCommand\": \"" + functionName //sends the function name allowing for it to be used in both updates and adds
                + IdString
                + "\",\"title\": \"" + title
                + "\", \"genre\": " + jsonArrayFormatter(genre) //calls jsonArray formatter to take the arraylist of Genres and format them into a json array
                + ", \"starring\": " + jsonArrayFormatter(starring) //calls jsonArray formatter to take the arraylist of Stars and format them into a json array
                + ", \"runtime\": \"" + runtime
                + "\", \"director\": \"" + director
                + "\", \"rating\": \"" + rating
                + "\", \"copies\": \"" + copies
                + "\", \"user_rating\": \"" + userRating
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

    public static ArrayList<String> jsonArrayToArrayList(JsonObject jsonMovie, String arrayName) {
        ArrayList<String> newArray = new ArrayList<>();         //initialise arraylist
        JsonArray jsonArray = jsonMovie.getJsonArray(arrayName);  //extract array from json 
        for (JsonValue jsonArr : jsonArray)
        {
            newArray.add(jsonArr.toString());               // adds to the arraylist from the json array
        }
        return newArray;
    }

    public static void printRun(Socket socket) throws IOException {
        Scanner socketReader = new Scanner(socket.getInputStream());            //creates the socket reader to recieve the return from the server    
        String returnedString = socketReader.nextLine();                        //extracts the string from the socket reader
        JsonObject jsonReturn = jsonFromString(returnedString);                 //turns the string into a json object
        switch (jsonReturn.getString("type"))
        {
            case "message":
                System.out.println(jsonReturn.getString("message"));           //extracts the return and prints it
                break;
            case "movie":
                System.out.println(printMovieReturn(jsonReturn));               //extracts the return and prints the movie
                break;
            case "movieArray":
                System.out.println(printMovieArrayReturn(jsonReturn));          //extracts the return and prints the movies from the array
                break;
            default:
                System.out.println("Server response unknown.");
                break;
        }
    }

    public static String printMovieArrayReturn(JsonObject jsonReturn) {

        JsonArray jsonArray = jsonReturn.getJsonArray("movies");
        String movies = "";
        for (JsonValue jsonMovie : jsonArray)
        {
            movies = movies + " : " + printMovieReturn(jsonMovie.asJsonObject()) + " : \n";
        }
        return movies;
    }

    public static String printMovieReturn(JsonObject jsonMovie) {
        String genre = "";
        String starring = "";
        for (int i = 0; i < jsonArrayToArrayList(jsonMovie, "genre").size(); i++)
        {
            genre = genre + jsonArrayToArrayList(jsonMovie, "genre").get(i)
                    + ", ";

        }
        for (int i = 0; i < jsonArrayToArrayList(jsonMovie, "starring").size(); i++)
        {
            starring = starring + jsonArrayToArrayList(jsonMovie, "starring").get(i)
                    + ", ";

        }
        return "Title: " + jsonMovie.getString("title") + ", Genres: " + genre + " Starring: " + starring + " Runtime: " + jsonMovie.getString("runtime")
                + ", Director: " + jsonMovie.getString("director") + ", Rating: " + jsonMovie.getString("rating") + ", Copies: " + jsonMovie.getString("copies") + ", User rating: " + jsonMovie.getString("user_rating");
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
