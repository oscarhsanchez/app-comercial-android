package gpovallas.app;

import gpovallas.utils.Text;

import java.util.ArrayList;
import java.util.HashMap;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class GenericSelectActivity extends GPOVallasListActivity {

	protected String[] listColumnTags = new String[] {"codigo", "descripcion"};
	protected int[] listColumnIds = new int[] { R.id.txtCodigo, R.id.txtDescripcion };
	public String strFilterCodigo;
	public String strFilterDescripcion;
	protected EditText txtFilterCodigo;
	protected EditText txtFilterDescripcion;
	protected ArrayList<HashMap<String, String>> arrRows;
	public SQLiteDatabase db;
	protected String sqlExtraWhere;
	protected String sqlSearch;
	protected Cursor cursorResults;
	protected int offsetResults;
	private Boolean boolFirstSearchDone = false;
	private Boolean boolLoadMore = false;
	private Boolean boolSearching = false;
	private ListView listView;
	private int layoutRow; 
	
	protected Boolean ArticleSelectedWindowTriggered = false; //Para controlar que no se abra la ventana de seleccion de articulo mas de una vez consecuencia de la busqueda
	
	public void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		setContentView(getContentView());
		layoutRow = getRowLayout();
		db = ApplicationStatus.getInstance().getDbRead(getApplicationContext());
		offsetResults = 0;
		init();
		initSearchFields();
		initListView();
		resetFields();
	}
	
	protected int getContentView(){
		return R.layout.generic_select_activity;
	}
	
	protected int getRowLayout(){
		return R.layout.generic_select_activity_row;
	}
	
	protected String getSQL(){
		return "";
	}
	
	public void setSQLExtraWhere(String sqlExtraWhere){
		this.sqlExtraWhere = sqlExtraWhere;
	}
	
	protected void init(){
		this.setBreadCrumb("Seleccione un registro", "");
	}
	
	public void setHeader(Boolean withHeader){
		LinearLayout layoutHeader = (LinearLayout) findViewById(R.id.header);
		layoutHeader.setVisibility(withHeader ? View.VISIBLE : View.GONE);
		
	}
	
	protected void initSearchFields(){

		strFilterCodigo = "";
		strFilterDescripcion = "";
		
		txtFilterCodigo = (EditText)findViewById(R.id.et_search_filter_codigo);
		if (txtFilterCodigo != null){
			txtFilterCodigo.addTextChangedListener(new TextWatcher(){
	
				public void onTextChanged(CharSequence s, int start, int before, int count) {}
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
	
				public void afterTextChanged(Editable s){
					strFilterCodigo = s.toString();
					init_populate();
					populate();
					showData();
					
				}
			});
		}

		txtFilterDescripcion = (EditText)findViewById(R.id.et_search_filter_descripcion);
		if (txtFilterDescripcion != null){
			txtFilterDescripcion.addTextChangedListener(new TextWatcher(){
	
				public void onTextChanged(CharSequence s, int start, int before, int count) {}
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
	
				public void afterTextChanged(Editable s){
					strFilterDescripcion = s.toString();
					init_populate();
					populate();
					showData();
				}
			});
		}

	}
	
	protected void init_populate(){
		if (boolSearching) return;
    	boolSearching = true;
		arrRows = new ArrayList<HashMap<String, String>>();
		offsetResults = 0;
		boolLoadMore = true;
		strFilterCodigo = strFilterCodigo.replace("'", "''");
		strFilterDescripcion = strFilterDescripcion.replace("'", "''");
	}
	
	protected void load_more(){
		if (boolSearching) return;
    	boolSearching = true;
    	cursorResults = db.rawQuery(getSQLPagination(),null );
    	populate();
    	offsetResults += cursorResults.getCount();
    	boolSearching = false;
		boolLoadMore = cursorResults.getCount() > 0;
		
		if (SimpleAdapter.class.isAssignableFrom(listView.getAdapter().getClass())){
			SimpleAdapter simpleAdapter = (SimpleAdapter) listView.getAdapter();
			simpleAdapter.notifyDataSetChanged();
		} else if (BaseAdapter.class.isAssignableFrom(listView.getAdapter().getClass())){
			BaseAdapter baseAdapter = (BaseAdapter) listView.getAdapter();
			baseAdapter.notifyDataSetChanged();
		}
		
    	//showData();
	}
	
	protected Cursor getCursorResults(){
		cursorResults = db.rawQuery(getSQLPagination(),null );
		return cursorResults;
	}
	
	protected void populate(){
		
	}
	
	protected void updateList(){
		arrRows = new ArrayList<HashMap<String, String>>();
		offsetResults = 0;
		cursorResults = db.rawQuery(getSQLPagination(),null );
    	populate();
    	showData();
	}
	
	protected void showData(){
		offsetResults += cursorResults.getCount();
		boolFirstSearchDone = true;
		listView.setAdapter(getDataAdapter());
		boolSearching = false;
		boolLoadMore = arrRows.size() > 0;
	}
	
	protected ListAdapter getDataAdapter(){
		SimpleAdapter adapter = new SimpleAdapter(this, arrRows, layoutRow, listColumnTags , listColumnIds);
		return adapter;
	}
	
	protected void selectRow(HashMap<String,String> hmSelected, Integer position, View view){
		
	}
	
	protected void resetFields(){
		//La descripcion solo la borramos cuando se pulse el boton TODOS para dejar la busqueda o si solo hay un resultado
		if (txtFilterCodigo != null && !Text.isEmpty(txtFilterCodigo.getText().toString())) txtFilterCodigo.setText("");		
		strFilterCodigo = "";		
		if (txtFilterDescripcion != null  && !Text.isEmpty(txtFilterDescripcion.getText().toString()) && arrRows.size() == 1) {
			txtFilterDescripcion.setText("");
			strFilterDescripcion = "";
		}
		
	}

	public void deleteSearchFilter(View v){
		//Esta funcion se llama cuando presionamos el boton TODOS. Forzamos el borrado del filtro de busqueda por descripcion
		if (txtFilterDescripcion != null  && !Text.isEmpty(txtFilterDescripcion.getText().toString()) ) txtFilterDescripcion.setText("");
		strFilterDescripcion = "";
		
		
		resetFields();
		init_populate();
		populate();
		showData();
	}

	@Override
	public void onResume(){
		super.onResume();
		//resetFields();
		ArticleSelectedWindowTriggered = false;
		init_populate();
		populate();
		showData();
		
	}
		
	private String getSQLPagination(){
		String sql = getSQL();
		sql += " LIMIT 50 OFFSET " + offsetResults;
		return sql;
	}
	
	private void initListView(){
		
		listView = getListView();
		
		listView.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		    	HashMap<String,String> map = arrRows.get(position);
				selectRow(map, position, view);
				
		    }
		});
		
		listView.setOnScrollListener(new OnScrollListener() {
			
			Boolean mIsScrolling = false;
			Boolean mIsScrollingUp = false;
			int mLastFirstVisibleItem = 0;
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				
				if (boolFirstSearchDone){
		    		//Check if the last view is visible
					if (visibleItemCount != 0 && ((firstVisibleItem + visibleItemCount) >= (totalItemCount)) && (mIsScrolling && !mIsScrollingUp)){
						if (boolLoadMore) load_more();
					}else{
						boolLoadMore = true;
					}
		    	}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				final int currentFirstVisibleItem = view.getFirstVisiblePosition();

				mIsScrolling = false;
				
		        if (currentFirstVisibleItem > mLastFirstVisibleItem) {
		        	mIsScrolling = true;
		            mIsScrollingUp = false;
		        } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
		        	mIsScrolling = true;
		            mIsScrollingUp = true;
		        }

		        mLastFirstVisibleItem = currentFirstVisibleItem;
			}
		});
	    
	}
	
	protected String getSQLFiltersConditionByCodigoDescripcion(String cod, String desc){
		
		String cond = "";
		
		if (!Text.isEmpty(strFilterCodigo)){
			cond += cod + " LIKE '%" + strFilterCodigo + "%'";
		}
		
		if (!Text.isEmpty(strFilterDescripcion)){
			
			if (!Text.isEmpty(cond)) cond += " AND ";
			
			cond += desc + " LIKE '%" + strFilterDescripcion + "%'";
		}
		
		if (!Text.isEmpty(cond)) cond = "("+cond+")";
		
		return cond;
		
	}

}
