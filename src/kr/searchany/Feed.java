package kr.searchany;

import java.util.Date;
import java.text.SimpleDateFormat;
import android.location.Location;

public class Feed {
    private Date date;
    private String description;
    private Location location;
    private double magnitude;
    private String link;
	private String title;


    public Date getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}

	public Location getLocation() {
		return location;
	}

	public double getMagnitude() {
		return magnitude;
	}

	public String getLink() {
		return link;
	}

	public String getTitle() {
		return title;
	}

	public Feed(String _title, Date _d, String _det, Location _loc, double _mag, String _link) {
    	title = _title;
        date = _d;
        description = _det;
        location = _loc;
        magnitude = _mag;
        link = _link;
    }

    @Override
    public String toString() {
        return title;
    }
}