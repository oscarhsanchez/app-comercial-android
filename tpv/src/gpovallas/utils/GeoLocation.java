package gpovallas.utils;


import gpovallas.app.GPOVallasApplication;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GeoLocation{
	private LocationManager locationManager;
	private String provider;
	private final Context mContext;
	private MyLocationListener mylistener;
	
	public GeoLocation(Context mContext){
		this.mContext = mContext;
		getLocation();
		if(GPOVallasApplication.location==null){
			 GPOVallasApplication.location = new Location("");//provider name is unecessary
			 GPOVallasApplication.location.setLatitude(0.00d);//your coords of course
			 GPOVallasApplication.location.setLongitude(0.00d);

		}
	}
	
	public void getLocation(){
		  // Get the location manager
		  locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		  // Define the criteria how to select the location provider
		  Criteria criteria = new Criteria();
		  criteria.setAccuracy(Criteria.ACCURACY_FINE);	//default
		  criteria.setCostAllowed(false); 
		  // get the best provider depending on the criteria
		  provider = locationManager.getBestProvider(criteria, false);
	    

		  mylistener = new MyLocationListener();
	
		  // location updates: at least 1 meter and 20000000 millsecs change
		  locationManager.requestLocationUpdates(provider, 20, 1, mylistener);
	}
	private class MyLocationListener implements LocationListener {
		
		  @Override
		  public void onLocationChanged(Location location) {
			// Initialize the location fields
			  GPOVallasApplication.location=location;
			  locationManager.removeUpdates(this);
		  }
	
		  @Override
		  public void onStatusChanged(String provider, int status, Bundle extras) {
		  }
	
		  @Override
		  public void onProviderEnabled(String provider) {
	
		  }
	
		  @Override
		  public void onProviderDisabled(String provider) {
			  /* this is called if/when the GPS is disabled in settings */
				Log.v("", "Disabled");
		  }
	  }
}
