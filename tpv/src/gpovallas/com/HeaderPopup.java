package gpovallas.com;

import gpovallas.app.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class HeaderPopup extends LinearLayout {
	
	public HeaderPopup(Context context) {
		super(context);
	}

	public HeaderPopup(Context context, AttributeSet attr){
		
		super(context,attr);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout view = (LinearLayout) inflater.inflate(R.layout.header_popup, this);
		
	}
}
