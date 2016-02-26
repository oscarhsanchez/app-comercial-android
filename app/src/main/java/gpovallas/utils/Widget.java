package gpovallas.utils;

import gpovallas.app.ApplicationStatus;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Widget {

	public static String[] populateSpinner(Activity activity, Spinner spinner, String SQL){
				
		//Cargo los grupos
		SQLiteDatabase db = ApplicationStatus.getInstance().getDbRead(activity.getApplicationContext());
		Cursor cursor = db.rawQuery(SQL, null);
		String[] arrLM = new String[]{ "Seleccione un valor" };
		String[] arrLMId = new String[]{ "" };

		int iLM = 1;

		if (cursor.moveToFirst()){
			arrLM = new String[cursor.getCount()+1];
			arrLMId = new String[cursor.getCount()+1];
			arrLM[0] = "Seleccione un valor";
			arrLMId[0] = "";
			do {
				arrLM[iLM] = cursor.getString(1);
				arrLMId[iLM] = cursor.getString(0);
				iLM++;
			}while(cursor.moveToNext());
		}
		cursor.close();

		ArrayAdapter adapterLM = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, arrLM);
		adapterLM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapterLM);
		return arrLMId;
		
	}
	
}
