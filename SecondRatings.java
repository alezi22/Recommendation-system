
/**
 * Write a description of SecondRatings here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.*;

public class SecondRatings {
    private ArrayList<Movie> myMovies;
    private ArrayList<Rater> myRaters;
    
    public SecondRatings() {
        this("ratedmoviesfull.csv", "ratings.csv");
    }
    
    public SecondRatings(String moviefile, String ratingsfile) {
        FirstRatings firstrating = new FirstRatings();
        myMovies = firstrating.loadMovies(moviefile);
        myRaters = firstrating.loadRaters(ratingsfile);
    }
    
    public int getMovieSize() {
        return myMovies.size();
    }
    
    public int getRaterSize() {
        return myRaters.size();
    }
    
    private double getAverageByID(String movieID, int minimalRaters) {
        int num = 0;
        double avgrating = 0.0;
        for(Rater r : myRaters) {
            ArrayList<String> mov = r.getItemsRated();
            for(String s : mov) {
                if(s.equals(movieID)) {
                    num++;
                    avgrating += r.getRating(movieID);
                }
            }
        }
        if(num >= minimalRaters) {
            return avgrating/num;
        } else {
            return 0.0;
        }
    }
    
    public ArrayList<Rating> getAverageRatings(int minimalRaters) {
        ArrayList<Rating> allavgratings = new ArrayList<Rating>();
        for(Movie m : myMovies) {
            String mID = m.getID();
            double avgrate = getAverageByID(mID,minimalRaters);            
            allavgratings.add(new Rating(mID,avgrate));
        }
        return allavgratings;
    }
    
    public String getTitle(String movieID) {
        for(Movie m : myMovies) {
            if(m.getID().equals(movieID)) {
                return m.getTitle();
            }
        }
        return "Movie with this ID not foun.";
    }
    
    public String getID(String movieTitle) {
        for(Movie m : myMovies) {
            if(m.getTitle().equals(movieTitle)) {
                return m.getID();
            }
        }
        return "There is not a movie with this title.";
    }
}
