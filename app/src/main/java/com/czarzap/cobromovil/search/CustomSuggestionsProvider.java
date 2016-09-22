package com.czarzap.cobromovil.search;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import com.czarzap.cobromovil.DB.DatabaseManager;
import com.czarzap.cobromovil.rtprinter.R;
import com.czarzap.cobromovil.service.DatosComercioService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CustomSuggestionsProvider extends ContentProvider {

    private static final String TAG = "CustomSuggestionsPr";
    private static final int GET_SAMPLE = 0;
    private static final int GET_CUSTOM_SUGGESTIONS = 1;

    private static final String PROVIDER_NAME = "br.com.edsilfer.content_provider.CustomSuggestionsProvider";
    private static final UriMatcher uriMatcher;

    List<Contribuyente> comerciosList;
    private  String url;
    private Integer empresa;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "GET_SAMPLE", GET_SAMPLE);
        uriMatcher.addURI(PROVIDER_NAME, "suggestions/*", GET_CUSTOM_SUGGESTIONS);
    }

    @Override
    public boolean onCreate() {
        DatabaseManager manager = new DatabaseManager(getContext());
        url = manager.getWebService(1);
        empresa = manager.getEmpresa();

        return true;
    }

    // Content Provider SQL callbacks ______________________________________________________________
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        switch (uriMatcher.match(uri)) {
            case GET_SAMPLE:
                return getFakeData(uri);
            case GET_CUSTOM_SUGGESTIONS:
                Log.i(TAG, "received parameter in query: " + uri.getLastPathSegment());
                return getFakeData(uri);
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


    // Util ________________________________________________________________________________________
    private Cursor getFakeData (Uri uri) {
        // Columns explanation: http://developer.android.com/guide/topics/search/adding-custom-suggestions.html#HandlingSuggestionQuery

        if(url==null){
            DatabaseManager manager = new DatabaseManager(getContext());
            url = manager.getWebService(1);
        }
        if(empresa ==null){
            DatabaseManager manager = new DatabaseManager(getContext());
            empresa = manager.getEmpresa();
        }

        if (comerciosList == null || comerciosList.isEmpty()){
            Retrofit retrofit = new Retrofit.Builder()                          // Crear REST
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            DatosComercioService service = retrofit.create(DatosComercioService.class);
            Call<List<Contribuyente>> call = service.getContribuyentes(empresa);
            call.enqueue(new Callback<List<Contribuyente>>() {
                @Override
                public void onResponse(Call<List<Contribuyente>> call, Response<List<Contribuyente>> response) {
                    comerciosList = response.body();
                    Log.d("Contribuyentes",comerciosList.toString());
                }

                @Override
                public void onFailure(Call<List<Contribuyente>> call, Throwable t) {
                    Log.d("Error Co",t.getMessage());
                }
            });
        }

        MatrixCursor cursor = new MatrixCursor(new String[] {
                "_ID",
                SearchManager.SUGGEST_COLUMN_TEXT_1,
                SearchManager.SUGGEST_COLUMN_TEXT_2,
                SearchManager.SUGGEST_COLUMN_ICON_1,
                SearchManager.SUGGEST_COLUMN_ICON_2,
                SearchManager.SUGGEST_COLUMN_INTENT_ACTION,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID,
                SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA,
                SearchManager.SUGGEST_COLUMN_QUERY,
        });

        if (comerciosList != null) {
            String query = uri.getLastPathSegment().toUpperCase();
            int lenght = comerciosList.size();
            for (int i = 0; i < lenght; i++) {
                Contribuyente contribuyente = comerciosList.get(i);
                if (contribuyente.getNombre().toUpperCase().contains(query)) {
                    Object[] fakeRow = {"ID" + i, contribuyente.getNombre().toUpperCase(), contribuyente.getId(), null, R.drawable.arrow_left_icon, null, null, null, null, null};
                    cursor.addRow(fakeRow);
                }
            }
        }
        return cursor;

    }
}
