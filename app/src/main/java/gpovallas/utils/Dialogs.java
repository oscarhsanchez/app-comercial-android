package gpovallas.utils;

import gpovallas.app.R;
import gpovallas.com.numberPicker.NumberPicker;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public class Dialogs {	

	public static Dialog newAlertDialog(Context c, String title, String message, String textButton)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(c).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton(textButton, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		//alertDialog.setIcon(R.drawable.logo_bg_white);

		return alertDialog;
	}

	public static void showToast(Context c, String msg){
		Toast toast;
		toast = Toast.makeText(c, msg, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();

	}

	public static Dialog newConfirmDialog(Context c, String title, String message, 
			OnClickListener positiveBtnListener, 
			OnClickListener negativeBtnListener){
		AlertDialog confirmDialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		builder.setTitle(title);
		//builder.setIcon(R.drawable.logo_bg_white);
		builder.setMessage(message)
		.setCancelable(false)
		.setPositiveButton("Si", positiveBtnListener)
		.setNegativeButton("No", negativeBtnListener);

		confirmDialog = builder.create();
		return confirmDialog;

	}


	public static Dialog newConfirmDialog(Context c, String title, String message, 
			OnClickListener positiveBtnListener, 
			OnClickListener negativeBtnListener, String textPositive, String textNegative){
		AlertDialog confirmDialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		builder.setTitle(title);
		//builder.setIcon(R.drawable.logo_bg_white);
		builder.setMessage(message)
		.setCancelable(false)
		.setPositiveButton(textPositive, positiveBtnListener)
		.setNegativeButton(textNegative, negativeBtnListener);

		confirmDialog = builder.create();
		return confirmDialog;

	}

	public static Dialog newDialogNumberPicker(Context c, Integer cantidad, 
			OnClickListener positiveBtnListener){

		AlertDialog alertDialog = (AlertDialog) Dialogs.newDialogNumberPicker(c, cantidad, positiveBtnListener, null); 
		return alertDialog;
	}

	public static Dialog newDialogNumberPicker(Context c, Integer cantidad, 
			OnClickListener positiveBtnListener,
			OnClickListener negativeBtnListener){

		LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View npView = inflater.inflate(R.layout.number_picker_pref, null);

		final NumberPicker np = NumberPicker.getNumberPicker(c);
		np.setCurrent(cantidad);

		AlertDialog alertDialog = new AlertDialog.Builder(c)
		.setTitle("Indique una cantidad")
		.setView(npView)
		//.setIcon(R.drawable.logo_bg_white)
		.setPositiveButton("OK", positiveBtnListener)
		.setNegativeButton("Cancelar", negativeBtnListener)
		.create();
		return alertDialog;
	}

}
