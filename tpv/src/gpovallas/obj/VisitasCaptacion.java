package gpovallas.obj;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class VisitasCaptacion extends eEntity {

	public Integer Id;
	public String UniIdTpv;
	public Integer IdCaptacion;
	public String UniIdTpvCaptacion;
	public String Fecha;
	public String Hora;
	public String HoraEjecucion;//
	public Integer Fase; //
	public String DescFase; //
	public Integer Resultado; //
	public String DescResultado;
	public String Posibilidad;
	public String Observaciones;
	public Boolean Visitada;
	public Estado.status Estado;
	public Integer IdUsuario;

	public static Boolean checkVisitaOnDate(String Date, String UniIdTpvCaptacion, SQLiteDatabase db) {
		String sql = "SELECT * FROM VISITASCAPTACION WHERE DATE(FECHA) = DATE('@FECHA') AND UniIdTpvCaptacion = '@CODCLI'";

		sql = sql.replace("@FECHA", Date);
		sql = sql.replace("@CODCLI", UniIdTpvCaptacion);

		Cursor cursor = db.rawQuery(sql, null);

		if (cursor.moveToFirst()) {
			cursor.close();
			return true;
		} else {
			cursor.close();
			return false;
		}
	}

}