package com.example.maptiletest.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;


public class OnlineTileProvider implements TileProvider {
    private static final String TAG = "OnlineTileProvider";

    public static final String GOOGLE_UPPER_ZOOM_TILE_URL = "http://mt0.google.com/vt/lyrs=p&hl=zh-tw&x=%d&y=%d&z=%d&scale=1&s=Galileo";
    //base
//    public static final String OSM_UPPER_ZOOM_TILE_URL = "https://tile.openstreetmap.org/%d/%d/%d.png";
    //service a
//    public static final String OSM_UPPER_ZOOM_TILE_URL = "https://a.tile.openstreetmap.org/%d/%d/%d.png";
    //service b
//    public static final String OSM_UPPER_ZOOM_TILE_URL = "https://b.tile.openstreetmap.org/%d/%d/%d.png";
    //service c
//    public static final String OSM_UPPER_ZOOM_TILE_URL = "https://c.tile.openstreetmap.org/%d/%d/%d.png";
    //water
//    public static final String OSM_UPPER_ZOOM_TILE_URL = "http://c.tile.stamen.com/watercolor/%d/%d/%d.jpg";
    //wiki
    public static final String OSM_UPPER_ZOOM_TILE_URL = "https://maps.wikimedia.org/osm-intl/%d/%d/%d.png";
    public static final String RUDY_UPPER_ZOOM_TILE_URL = "http://rudy.tile.basecamp.tw/%d/%d/%d.png";
    public static final String WMTS_UPPER_ZOOM_TILE_URL = "http://gis.sinica.edu.tw/tileserver/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=TM25K_2001&TILEMATRIXSET=GoogleMapsCompatible&TILEMATRIX=%d&TILEROW=%d&TILECOL=%d&FORMAT=image/jpeg";
    public static final String JM1924_UPPER_ZOOM_TILE_URL = "http://gis.sinica.edu.tw/tileserver/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=JM50K_1924&TILEMATRIXSET=GoogleMapsCompatible&TILEMATRIX=%d&TILEROW=%d&TILECOL=%d&FORMAT=image/jpeg";

    public static final int GOOGLE_MAP = 0;
    public static final int OPEN_STREET_MAP = 1;
    public static final int MAPSFORGE = 2;
    public static final int RUDY_MAP = 3;
    public static final int WMTS_MAP = 4;
    public static final int JM1924_MAP = 5;
    public static final int OFFLINE = 6;

    private static final int TILE_WIDTH = 256;
    private static final int TILE_HEIGHT = 256;
    private static final int BUFFER_SIZE = 16 * 1024;
    private int mapType;

    public OnlineTileProvider(int mapType){
        this.mapType = mapType;
    }

    @Override
    public Tile getTile(int x, int y, int zoom) {
        Tile tile;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        getBitmapFromURL(x, y, zoom).compress(Bitmap.CompressFormat.JPEG, 100, stream);
        tile = new Tile(256, 256, stream.toByteArray());
        return tile;
    }

    private Bitmap getBitmapFromURL(int x, int y, int zoom)
    {
        URL url;
        Log.d(TAG, String.format(GOOGLE_UPPER_ZOOM_TILE_URL, x, y, zoom));
        try
        {
            if (mapType == GOOGLE_MAP) {
                url = new URL(String.format(Locale.TAIWAN,GOOGLE_UPPER_ZOOM_TILE_URL, x, y, zoom));
                Log.d(TAG, String.format(GOOGLE_UPPER_ZOOM_TILE_URL, x, y, zoom));
            } else if (mapType == OPEN_STREET_MAP) {
                url = new URL(String.format(Locale.TAIWAN, OSM_UPPER_ZOOM_TILE_URL, zoom, x, y));
                Log.d(TAG, String.format(Locale.TAIWAN, OSM_UPPER_ZOOM_TILE_URL, zoom, x, y));
            } else if (mapType == RUDY_MAP) {
                url = new URL(String.format(Locale.TAIWAN, RUDY_UPPER_ZOOM_TILE_URL, zoom, x, y));
                Log.d(TAG, String.format(RUDY_UPPER_ZOOM_TILE_URL, zoom, x, y));
            } else if (mapType == WMTS_MAP) {
                url = new URL(String.format(Locale.TAIWAN, WMTS_UPPER_ZOOM_TILE_URL, zoom, y, x));
                Log.d(TAG, String.format(WMTS_UPPER_ZOOM_TILE_URL, zoom, y, x));
            } else if (mapType == JM1924_MAP) {
                url = new URL(String.format(Locale.TAIWAN, JM1924_UPPER_ZOOM_TILE_URL, zoom, y, x));
                Log.d(TAG, String.format(JM1924_UPPER_ZOOM_TILE_URL, zoom, y, x));
            } else {
                url = new URL(String.format(Locale.TAIWAN, GOOGLE_UPPER_ZOOM_TILE_URL, x, y, zoom));
            }
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            return BitmapFactory.decodeStream(connection.getInputStream());
        }
        catch (IOException e)
        {
            Log.d(TAG, "exception when retrieving bitmap from internet: " + e.toString());
            return null;
        }
    }
}