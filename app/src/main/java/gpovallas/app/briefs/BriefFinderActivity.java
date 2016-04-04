package gpovallas.app.briefs;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import gpovallas.adapter.BriefFinderAdapter;
import gpovallas.app.ApplicationStatus;
import gpovallas.app.GPOVallasListActivity;
import gpovallas.app.R;
import gpovallas.app.constants.GPOVallasConstants;

public class BriefFinderActivity extends GPOVallasListActivity {

    private static final String TAG = BriefFinderActivity.class.getSimpleName();

    private EditText mTxtSearchFilter;
    private ArrayList<HashMap<String, String>> mBriefList;
    private SQLiteDatabase db;
    private String filter_nombreBri;
    private BriefFinderAdapter mAdapter;
    private Button mButtonNewBrief;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brief_finder);
        setBreadCrumb("Briefs", "Buscador de briefs");

        Log.v(TAG, "BriefFinder on create");
        db = ApplicationStatus.getInstance().getDb(getApplicationContext());

        init();


    }

    @Override
    protected void onResume() {
        super.onResume();
        populate();
    }

    public void finalizar(View v) {
        finish();
    }

    private void init() {

        filter_nombreBri = "";

        mTxtSearchFilter = (EditText) findViewById(R.id.et_search_filter);
        mTxtSearchFilter.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                filter_nombreBri = s.toString();
                populate();
            }
        });

        mButtonNewBrief = (Button) findViewById(R.id.btn_add_brief);
        mButtonNewBrief.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BriefFinderActivity.this, BriefDetailActivity.class);
                startActivity(intent);
            }
        });

    }

    public void populate() {

        mBriefList = new ArrayList<HashMap<String, String>>();

        String sql = "SELECT b.token, b.fecha_solicitud as fecha, c.razon_social as cliente  " +
                "FROM BRIEF b LEFT JOIN CLIENTE c ON b.fk_cliente = c.pk_cliente WHERE b.estado = 1";

        filter_nombreBri = filter_nombreBri.replace("'", "''");

        if (!TextUtils.isEmpty(filter_nombreBri)) {
            sql += " AND c.razon_social LIKE '%" + filter_nombreBri + "%' ";
        }

        sql += " ORDER BY c.razon_social ASC";

        Cursor c = db.rawQuery(sql, null);
        Log.i(TAG, "" + c.getCount());
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("token", c.getString(c.getColumnIndex("token")));
                map.put("fecha", c.getString(c.getColumnIndex("fecha")));
                map.put("cliente", c.getString(c.getColumnIndex("cliente")));
                mBriefList.add(map);
            } while (c.moveToNext());
        }
        c.close();

        mAdapter = new BriefFinderAdapter(this, mBriefList);
        setListAdapter(mAdapter);

    }

    public void deleteSearchFilter(View v) {
        filter_nombreBri = "";
        mTxtSearchFilter.setText("");
        populate();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Log.i(TAG, "Posicion cliqueada " + position + " token " + mBriefList.get(position).get("token"));
        String token = mBriefList.get(position).get("token");
        Intent intent = new Intent(BriefFinderActivity.this, BriefDetailActivity.class);
        intent.putExtra(GPOVallasConstants.BRIEF_TOKEN, token);
        startActivity(intent);

    }
}
