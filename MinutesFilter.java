
/**
 * Write a description of MinutesFilter here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MinutesFilter implements Filter{
    private int myMin;
    private int myMax;
    
    public MinutesFilter(int minMinutes, int maxMinutes) {
        myMin = minMinutes;
        myMax = maxMinutes;
    }
    
    @Override
    public boolean satisfies(String id) {
        int duration = MovieDatabase.getMinutes(id);
        return (duration >= myMin) && (duration <= myMax);
    }
}
