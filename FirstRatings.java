
/**
 * Write a description of FirstRatings here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.*;
import java.io.*;
import org.apache.commons.csv.*;
import edu.duke.*;

public class FirstRatings {
    public ArrayList<Movie> loadMovies(String filename) {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        FileResource fr = new FileResource("data/" + filename);
        CSVParser parser = fr.getCSVParser();
        for(CSVRecord mov : parser) {
            String id = mov.get("id");
            String title = mov.get("title");
            String year = mov.get("year");
            String genres = mov.get("genre");
            String director = mov.get("director");
            String country = mov.get("country");
            String poster = mov.get("poster");
            int minutes = Integer.parseInt(mov.get("minutes"));
            movies.add(new Movie(id,title,year,genres,director,country,poster,minutes));
        }
        return movies;
    }
    
    public ArrayList<Rater> loadRaters(String filename) {
        ArrayList<Rater> raters = new ArrayList<Rater>();
        FileResource fr = new FileResource("data/" + filename);
        CSVParser parser = fr.getCSVParser();
        int num;
        for(CSVRecord rec : parser) {
            String raterId = rec.get("rater_id");
            String movieId = rec.get("movie_id");
            double rating = Double.parseDouble(rec.get("rating"));
            boolean exist = false;
            for (Rater r: raters){
                if (r.getID().equals(raterId)){
                    exist = true;
                }
            }
            if (exist == false){
                Rater currRater = new EfficientRater(raterId);
                currRater.addRating(movieId, rating);
                raters.add(currRater);
            } else {
                for (Rater r: raters){
                    if (r.getID().equals(raterId)){
                        r.addRating(movieId, rating);
                    }
                }
            }
        }
        return raters;
    }
    
    public void findNumOfRater(String filename, String RaterID) {
        ArrayList<Rater> users = loadRaters(filename);
        int x = 0;
        for(Rater r : users) {
            if(r.getID().equals(RaterID)) {
                x = r.numRatings();
                break;
            }
        }        
        System.out.println("User with ID: " + RaterID + " has " + x + " ratings");
    }
    
    public void findMaxNumOfRatingsByRater(String filename) {
        ArrayList<Rater> users = loadRaters(filename);
        int max = 0;
        String s = "";
        for(Rater r : users) {
            if(r.numRatings() > max) {
                max = r.numRatings();
            }
        }
        for(Rater r : users) {
            if(r.numRatings() == max) {
                s += r.getID() + ", ";
            }
        }
        System.out.println(s.substring(0,s.length()-2));
    }
    
    public void findRatingsOfMovie(String filename, String movieID) {
        ArrayList<Rater> users = loadRaters(filename);
        int num = 0;
        for(Rater r : users) {
            ArrayList<String> movies = r.getItemsRated();
            if(movies.contains(movieID)) {
                num++;
            }
        }
        System.out.println(num + " users have rated movie with ID: " + movieID);
    }
    
    public void countRatedMovies(String filename) {
        ArrayList<Rater> users = loadRaters(filename);
        ArrayList<String> movies = new ArrayList<String>();
        for(Rater r : users) {
            ArrayList<String> list = r.getItemsRated();
            for(String s : list) {
                if(!movies.contains(s)) {
                    movies.add(s);
                }
            }
        }
        System.out.println("There are " + movies.size() + " rated");
    }
    
    public void test(){
        DirectoryResource dr = new DirectoryResource();
        for (File f: dr.selectedFiles()){
            String filename = f.getName();
            System.out.println("Processing file: " + filename);
            System.out.println(" ");
            //testLoadMovies(filename);
            //testLoadRaters(filename);
            //findNumOfRater(filename, "193");
            //findMaxNumOfRatingsByRater(filename);
            //findRatingsOfMovie(filename, "1798709");
            //countRatedMovies(filename);
        }
    }
    
    public void testLoadMovies(String filename){
        //DirectoryResource dr = new DirectoryResource();
        //for (File f: dr.selectedFiles()){
            //String filename = f.getName();
            //System.out.println("Processing file: " + filename);
            ArrayList<Movie> movies = loadMovies(filename);
            System.out.println("There are " + movies.size() + " records.");
            //System.out.println(movie);
            
            int numComedy = 0;
            for (Movie currMovie: movies){
                if (currMovie.getGenres().indexOf("Comedy") != -1){
                    numComedy += 1;
                }
            }
            System.out.println("There are " + numComedy + " comedy movies in the file.");
            
            int numLength150 = 0;
            for (Movie currMovie: movies){
                if (currMovie.getMinutes() > 150){
                    numLength150 += 1;
                }
            }
            System.out.println("There are " + numLength150 + " movies which their lengths are more than 150 min.\n");
            
            // Remember that some movies may have more than one director.
            HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
            for (Movie currMovie: movies){
                String director = currMovie.getDirector().trim();
                if (director.indexOf(",") == -1){
                    if (!map.containsKey(director)){
                        map.put(director, new ArrayList<String>());
                    }
                    String title = currMovie.getTitle();
                    map.get(director).add(title);
                  
                } else {
                    while (director.indexOf(",") != -1){
                        int idxComma = director.indexOf(",");
                        String currDirector = director.substring(0, idxComma);
                        
                        if (!map.containsKey(currDirector)){
                            map.put(currDirector, new ArrayList<String>());
                        }
                        String title = currMovie.getTitle();
                        map.get(currDirector).add(title);
                        
                        director = director.substring(idxComma+1).trim();
                    }
                }
            }
            
            int maxNumOfMoviesByDirector = 0;
            for (String s: map.keySet()){
                if (map.get(s).size() > maxNumOfMoviesByDirector){
                    maxNumOfMoviesByDirector = map.get(s).size();
                }
            }
            System.out.println("The maximum number of films directed by one director is " + maxNumOfMoviesByDirector);
            
            String directorWithMaxMovies = "";
            for (String s: map.keySet()){
                if (map.get(s).size() == maxNumOfMoviesByDirector){
                    directorWithMaxMovies += s + ", ";
                }
            }
            System.out.println("Names of the directors who directed the maximum number of movies " +
                                directorWithMaxMovies.substring(0, directorWithMaxMovies.length()-2));
    }
    
    public void testLoadRaters(String filename){
        ArrayList<Rater> raters = loadRaters(filename);
        System.out.println("There are " + raters.size() + " raters.");
        System.out.println(" ");
        /*for (Rater currRater: raters){
            System.out.println("Rater ID " + currRater.getID() + ": " + currRater.numRatings() + " ratings.");
            ArrayList<String> items = currRater.getItemsRated();
            for (String item: items){
                double rating = currRater.getRating(item);
                System.out.print(item + " " + rating + "; ");
            }
            System.out.println("\n");
        }*/
        
    }
}
