package gpovallas.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import gpovallas.app.R;

public class Utils {

    /**
     * Establece el TÃ­tulo de la cabecera de la Activity
     *
     * @param breadcrumb1 TÃ­tulo (Typeface Bold)
     * @param breadcrumb2 Detalle (Typeface Normal)
     * @param ctx         Contexto de la AplicaciÃ³n
     * @author Javier Hdez
     */
    public static void setBreadcrumbTitle(Activity ctx, String breadcrumb1,
                                          String breadcrumb2) {
        TextView breadcrumb_n1 = (TextView) ctx
                .findViewById(R.id.header_breadcrumb_n1);
        TextView breadcrumb_n2 = (TextView) ctx
                .findViewById(R.id.header_breadcrumb_n2);
        breadcrumb_n1.setText(breadcrumb1);
        breadcrumb_n2.setText(breadcrumb2);
    }

    public static String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    @SuppressLint("SimpleDateFormat")
    public static String generateToken() throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        Calendar c = Calendar.getInstance();
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        SimpleDateFormat formatDate = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS");
        return Base64
                .encode(Utils.byteArrayToHexString(md.digest(((int) (Math
                        .random() * 100000)
                        + "asc/!*20aa"
                        + formatDate.format(c.getTime()) + (int) (Math.random() * 100000))
                        .getBytes("UTF-8"))));
    }

    public static String getTokenFromString(String pKey)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return Base64.encode(Utils.byteArrayToHexString(md
                .digest(("psub/!*20aa" + pKey).getBytes("UTF-8"))));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List sortByValueAsc(final Map m) {
        List keys = new ArrayList();
        keys.addAll(m.keySet());
        Collections.sort(keys, new Comparator() {
            public int compare(Object o1, Object o2) {
                Object v1 = m.get(o1);
                Object v2 = m.get(o2);
                if (v1 == null) {
                    return (v2 == null) ? 0 : 1;
                } else if (v1 instanceof Comparable) {
                    return ((Comparable) v1).compareTo(v2);
                } else {
                    return 0;
                }
            }
        });
        return keys;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List sortByValueDesc(final Map m) {
        List keys = new ArrayList();
        keys.addAll(m.keySet());
        Collections.sort(keys, new Comparator() {
            public int compare(Object o1, Object o2) {
                Object v1 = m.get(o1);
                Object v2 = m.get(o2);
                if (v1 == null) {
                    return (v2 == null) ? 0 : 1;
                } else if (v1 instanceof Comparable) {
                    return ((Comparable) v2).compareTo(v1);
                } else {
                    return 0;
                }
            }
        });
        return keys;
    }

    public static Bitmap DownloadFullFromUrl(String imageFullURL) {
        Bitmap bm = null;
        try {
            URL url = new URL(imageFullURL);
            URLConnection ucon = url.openConnection();
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            bm = BitmapFactory.decodeByteArray(baf.toByteArray(), 0,
                    baf.toByteArray().length);
        } catch (IOException e) {
            Log.d("ImageManager", "Error: " + e);
        }
        return bm;
    }

    public static String calculaFecha(String fecha) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.US);
        try {
            return Utils.diferenciaEntreFechas(
                    formatter.parse(fecha).getTime(),
                    System.currentTimeMillis());
        } catch (Exception e1) {
            return "Ahora mismo";
        }
    }

    public static String removeNull(String text) {
        if (text == null) {
            return "";
        }
        return text;
    }

    public static String encodeUrl(String url2) {
        try {
            URL url = new URL(url2);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(),
                    url.getHost(), url.getPort(), url.getPath(),
                    url.getQuery(), url.getRef());
            url = uri.toURL();
            return url.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDiaMesFecha(String fecha) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.US);
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = formatter.parse(fecha);
            calendar.setTime(date);
        } catch (Exception e) {
            calendar.setTimeInMillis(System.currentTimeMillis());
        }
        return String.format(Locale.US, "El %d/%d/%d",
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    public static String diferenciaEntreFechas(long milis1, long milis2) {
        // calcular la diferencia en milisengundos
        long diff = milis2 - milis1;
        // calcular la diferencia en segundos
        long diffSeconds = diff / 1000;
        // calcular la diferencia en minutos
        long diffMinutes = diff / (60 * 1000);
        // calcular la diferencia en horas
        long diffHours = diff / (60 * 60 * 1000);
        // calcular la diferencia en dias
        long diffDays = diff / (24 * 60 * 60 * 1000);
        // calcular la diferencia en semanas
        long diffWeeks = diff / (7 * 24 * 60 * 60 * 1000);

        if (diffWeeks >= 1)
            return "Hace " + diffWeeks
                    + (diffWeeks == 1 ? " semana" : " semanas");
        if (diffDays >= 1)
            return "Hace " + diffDays + (diffDays == 1 ? " día" : " días");
        if (diffHours >= 1)
            return "Hace " + diffHours + (diffHours == 1 ? " hora" : " horas");
        if (diffMinutes >= 1)
            return "Hace " + diffMinutes
                    + (diffMinutes == 1 ? " minuto" : " minutos");
        if (diffSeconds >= 1)
            return "Hace " + diffSeconds
                    + (diffSeconds == 1 ? " segundo" : " segundos");

        return null;
    }

    public static Bitmap decodeSampledBitmapFromStream(InputStream is,
                                                       int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        InputStream aux = inputStreamCopy(is);
        BitmapFactory.decodeStream(aux, null, options); // SkImageDecoder::Factory
        // returned null
        try {
            aux.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(aux, null, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private static InputStream inputStreamCopy(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len;
        byte[] buffer = new byte[1024];

        try {
            while ((len = is.read(buffer)) > -1)
                baos.write(buffer, 0, len);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(baos.toByteArray());
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = android.util.Base64.encodeToString(b,
                android.util.Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = android.util.Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public static InputStream retrieveStream(String url) {

        DefaultHttpClient client = new DefaultHttpClient();

        HttpGet httpRequest = new HttpGet(url);

        try {

            HttpResponse httpResponse = client.execute(httpRequest);
            final int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }

            HttpEntity httpEntity = httpResponse.getEntity();
            return httpEntity.getContent();

        } catch (IOException e) {
            httpRequest.abort();
        }

        return null;

    }

    public static Bitmap RotateBitmap(Bitmap bitmap, String filePath) {
        try {
            // Determine Orientation
            ExifInterface exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 1);

            // Determine Rotation
            int rotation = 0;
            if (orientation == 6)
                rotation = 90;
            else if (orientation == 3)
                rotation = 180;
            else if (orientation == 8)
                rotation = 270;

            // Rotate Image if Necessary
            if (rotation != 0) {
                // Create Matrix
                Matrix matrix = new Matrix();
                matrix.postRotate(rotation);

                // Rotate Bitmap
                Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                // Pretend none of this ever happened!
                bitmap.recycle();
                bitmap = rotated;
                rotated = null;
            }
        } catch (Exception e) {
            // TODO: Log Error Messages Here
        }
        return bitmap;
    }

    public static int getOrientation(Bitmap bitmap, String filePath) {
        try {
            // Determine Orientation
            ExifInterface exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 1);

            // Determine Rotation
            if (orientation == 6)
                return 90;
            else if (orientation == 3)
                return 180;
            else if (orientation == 8)
                return 270;
        } catch (Exception e) {
            // TODO: Log Error Messages Here
        }
        return -1;
    }

    public static Bitmap scaleImage(Context context, Uri photoUri)
            throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > 100 || rotatedHeight > 100) {
            float widthRatio = ((float) rotatedWidth) / ((float) 100); // 100 =
            // MAX_IMAGE_DIMENSION
            float heightRatio = ((float) rotatedHeight) / ((float) 100);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

		/*
         * if the orientation is not 0 (or -1, which means we don't know), we
		 * have to do a rotation.
		 */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
                    srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
        }

        String type = context.getContentResolver().getType(photoUri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (type.equals("image/png")) {
            srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        } else if (type.equals("image/jpg") || type.equals("image/jpeg")) {
            srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        byte[] bMapArray = baos.toByteArray();
        baos.close();
        return BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length);
    }

    public static int getOrientation(Context context, Uri photoUri) {
		/* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION},
                null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, float angle) {

        Matrix matrix = new Matrix();
        matrix.setRotate(angle);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        return rotatedBitmap;
    }

    public static String md5(String s) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes(), 0, s.length());
            BigInteger i = new BigInteger(1, m.digest());
            return String.format("%1$032x", i);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkInternetConnection(Context ctx) {
        ConnectivityManager conMgr = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean ret = true;

        if (conMgr != null) {
            NetworkInfo i = conMgr.getActiveNetworkInfo();
            if (i != null) {
                if (!i.isConnected() || !i.isAvailable()) {
                    ret = false;
                }
            } else {
                ret = false;
            }
        } else {
            ret = false;
        }

        return ret;
    }

    /**
     * Validate if a editText is empty from a edit text list
     *
     * @param emptyText
     * @param items
     * @return
     */
    public static boolean existsEmptyFields(String emptyText, EditText... items) {
        for (EditText edit : items) {
            if (TextUtils.isEmpty(edit.getText())) {
                edit.setError(emptyText);
                edit.requestFocus();
                return true;
            }
        }
        return false;
    }

    public static void deleteFiles(String path) {

        File file = new File(path);

        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {
            }
        }
    }

}
