
/**
 * Write a description of YearAfterFilter here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class YearAfterFilter implements Filter{
    private int myYear;
	
    public YearAfterFilter(int year) {
	myYear = year;
    }
	
    @Override
	public boolean satisfies(String id) {
	return MovieDatabase.getYear(id) >= myYear;
    }
}
