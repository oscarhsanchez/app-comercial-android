package gpovallas.com;

import gpovallas.app.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Header extends LinearLayout {
	
	public Header(Context context) {
		super(context);
	}

	public Header(Context context, AttributeSet attr){
		super(context,attr);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout view = (LinearLayout) inflater.inflate(R.layout.header, this);
		
	}
	
	public void setTextN1(String text) {
		((TextView)findViewById(R.id.header_breadcrumb_n1)).setText(text);
	}
	
	public void setTextN2(String text) {
		((TextView)findViewById(R.id.header_breadcrumb_n2)).setText(text);
	}
}
