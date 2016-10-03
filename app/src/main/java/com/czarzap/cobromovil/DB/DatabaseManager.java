package com.czarzap.cobromovil.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.czarzap.cobromovil.beans.InAgentesMoviles;
import com.czarzap.cobromovil.beans.InEmpresas;
import com.czarzap.cobromovil.beans.InWebServices;

public class DatabaseManager {

    public static final String TABLE_NAME_INWEBSERVICES = "inWebServices";          // NOMBRE DE LA TABLA

    public static final String INWEBSERVICES_COLUMN_EMPRESA = "ws_empresa";         // NOMBRE DE LAS COLUMNAS
    public static final String INWEBSERVICES_COLUMN_ID = "ws_id";
    public static final String INWEBSERVICES_COLUMN_URL = "ws_url";


    public static final String CREATE_TABLE_INWEBSERVICES =     " CREATE TABLE inWebServices"  +               // SENTENCIA PARA CREAR TABLA INWEBSERVICE
                                                                "("+
                                                                " ws_empresa    NUMBER(4) NOT NULL , "+
                                                                " ws_id         NUMBER(4) NOT NULL , "+
                                                                " ws_url        VARCHAR(500) NULL "+
                                                                " );"+
                                                                " CREATE UNIQUE INDEX XPKinWebServices ON inWebServices " +
                                                                " (ws_empresa   ASC,ws_id   ASC); "+
                                                                " ALTER TABLE inWebServices "+
                                                                " ADD CONSTRAINT  XPKinWebServices PRIMARY KEY (ws_empresa,ws_id);";

    public static final String TABLE_NAME_INAGENTESMOVILES = "inAgentesMoviles";              // NOMBRE TABLA

    public static final String INAGENTESMOVILES_COLUMN_EMPRESA = "am_empresa";                      // NOMBRE DE LAS COLUMNAS
    public static final String INAGENTESMOVILES_COLUMN_NUMERO = "am_numero";
    public static final String INAGENTESMOVILES_COLUMN_CEL = "am_cel";
    public static final String INAGENTESMOVILES_COLUMN_PASS = "am_password";
    public static final String INAGENTESMOVILES_COLUMN_NOMBRE = "am_nombre";
    public static final String INAGENTESMOVILES_COLUMN_ROL = "am_rol";
    public static final String INAGENTESMOVILES_COLUMN_MSG = "am_msg";
    public static final String INAGENTESMOVILES_COLUMN_ULT_LOGIN = "am_ult_login";
    public static final String INAGENTESMOVILES_COLUMN_STATUS = "am_status";
    public static final String INAGENTESMOVILES_COLUMN_ALTA = "am_alta";
    public static final String INAGENTESMOVILES_COLUMN_BAJA = "am_baja";
    public static final String INAGENTESMOVILES_COLUMN_ALTA_USR = "am_alta_usr";
    public static final String INAGENTESMOVILES_COLUMN_BAJA_USR = "am_baja_usr";
    public static final String INAGENTESMOVILES_COLUMN_HORARIO_INI = "am_horario_ini_militar";
    public static final String INAGENTESMOVILES_COLUMN_HORARIO_FIN = "am_horario_fin_militar";
    public static final String INAGENTESMOVILES_COLUMN_OBSERVACIONES = "am_observaciones";
    public static final String INAGENTESMOVILES_COLUMN_NUM_COBRO = "am_num_cobro";
    public static final String INAGENTESMOVILES_COLUMN_REMEMBER = "am_remember";

    public static final String CREATE_TABLE_INAGENTESMOVILES = "CREATE TABLE inAgentesMoviles" +            // SENTENCIA PARA CREAR LA TABLA
                                                                "(" +
                                                                " am_empresa           NUMBER(4) NOT NULL ," +
                                                                " am_numero            NUMBER(10) NOT NULL ," +
                                                                " am_cel               VARCHAR2(10) NULL ," +
                                                                " am_password          VARCHAR2(120) NULL ," +
                                                                " am_nombre            VARCHAR2(120) NULL ," +
                                                                " am_rol               VARCHAR2(4) NULL ," +
                                                                " am_msg               VARCHAR2(500) NULL ," +
                                                                " am_ult_login         DATE NULL ," +
                                                                " am_status            VARCHAR2(1) NULL ," +
                                                                " am_alta              DATE NULL ," +
                                                                " am_baja              DATE NULL ," +
                                                                " am_alta_usr          VARCHAR2(18) NULL ," +
                                                                " am_baja_usr          VARCHAR2(18) NULL ," +
                                                                " am_horario_ini_militar VARCHAR2(10) NULL ," +
                                                                " am_horario_fin_militar VARCHAR2(10) NULL ," +
                                                                " am_observaciones     VARCHAR2(500) NULL, " +
                                                                " am_num_cobro            NUMBER(12) NULL, " +
                                                                " am_remember            NUMBER(12) NULL " +
                                                                ");" +

                                                                "CREATE UNIQUE INDEX XPKinAgentesMoviles ON inAgentesMoviles" +
                                                                " (am_empresa   ASC,am_numero   ASC);" +

                                                                "ALTER TABLE inAgentesMoviles" +
                                                                " ADD CONSTRAINT  XPKinAgentesMoviles PRIMARY KEY (am_empresa,am_numero);";



    public static final String TABLE_NAME_INEMPRESAS= "inEmpresas";              // NOMBRE TABLA

    public static final String INEMPRESAS_COLUMN_EMPRESA = "em_id";                      // NOMBRE DE LAS COLUMNAS
    public static final String INEMPRESAS_COLUMN_NOMBRE = "em_nombre";
    public static final String INEMPRESAS_COLUMN_CALLE = "em_calle";
    public static final String INEMPRESAS_COLUMN_EXTERIOR = "em_exterior";
    public static final String INEMPRESAS_COLUMN_INTERIOR = "em_interior";
    public static final String INEMPRESAS_COLUMN_COLONIA = "em_colonia";
    public static final String INEMPRESAS_COLUMN_MUNICIPIO = "em_municipio";
    public static final String INEMPRESAS_COLUMN_ESTADO = "em_estado";
    public static final String INEMPRESAS_COLUMN_PAIS = "em_pais";
    public static final String INEMPRESAS_COLUMN_RFC = "em_rfc";
    
    
    public static final String CREATE_TABLE_INEMPRESAS = "CREATE TABLE inEmpresas" +            // SENTENCIA PARA CREAR LA TABLA
            "(" +
            " em_id               NUMBER(4) NOT NULL ," +
            " em_nombre           VARCHAR2(200) NULL ," +
            " em_calle            VARCHAR2(200) NULL ," +
            " em_exterior         VARCHAR2(10) NULL ," +
            " em_interior         VARCHAR2(10) NULL ," +
            " em_colonia          VARCHAR2(200) NULL ," +
            " em_municipio        VARCHAR2(200) NULL ," +
            " em_estado           VARCHAR2(200) NULL ," +
            " em_pais             VARCHAR2(200) NULL ," +
            " em_rfc              VARCHAR2(30) NULL " +
            ");" +

            "CREATE UNIQUE INDEX XPKinEmpresas ON inEmpresas" +
            " (em_id   ASC);" +

            "ALTER TABLE inAgentesMoviles" +
            " ADD CONSTRAINT  XPKinEmpresas PRIMARY KEY (em_id);";
    


    private DBHelper helper;
    private SQLiteDatabase db;

    public DatabaseManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public ContentValues contenedorWebService(InWebServices webService){
        ContentValues contentValues = new ContentValues();
        contentValues.put(INWEBSERVICES_COLUMN_EMPRESA,webService.getWs_empresa());
        contentValues.put(INWEBSERVICES_COLUMN_ID, webService.getWs_id());
        contentValues.put(INWEBSERVICES_COLUMN_URL ,webService.getWs_url());

        return contentValues;
    }

    public ContentValues contenedorAgentesMoviles(InAgentesMoviles agenteMovil){
        ContentValues contentValues = new ContentValues();
        contentValues.put(INAGENTESMOVILES_COLUMN_EMPRESA,agenteMovil.getAm_empresa());
        contentValues.put(INAGENTESMOVILES_COLUMN_NUMERO,agenteMovil.getAm_numero());
        contentValues.put(INAGENTESMOVILES_COLUMN_CEL,agenteMovil.getAm_cel());
        contentValues.put(INAGENTESMOVILES_COLUMN_PASS,agenteMovil.getAm_password());
        contentValues.put(INAGENTESMOVILES_COLUMN_NOMBRE,agenteMovil.getAm_nombre());
        contentValues.put(INAGENTESMOVILES_COLUMN_STATUS,agenteMovil.getAm_status());
        contentValues.put(INAGENTESMOVILES_COLUMN_NUM_COBRO,agenteMovil.getAm_num_cobro());

        return contentValues;
    }

    public ContentValues contenedorEmpresa(InEmpresas empresa){
        ContentValues contentValues = new ContentValues();
        contentValues.put(INEMPRESAS_COLUMN_EMPRESA,empresa.getEm_id());
        contentValues.put(INEMPRESAS_COLUMN_NOMBRE, empresa.getEm_nombre());
        contentValues.put(INEMPRESAS_COLUMN_CALLE ,empresa.getEm_calle());
        contentValues.put(INEMPRESAS_COLUMN_EXTERIOR,empresa.getEm_exterior());
        contentValues.put(INEMPRESAS_COLUMN_INTERIOR, empresa.getEm_interior());
        contentValues.put(INEMPRESAS_COLUMN_COLONIA ,empresa.getEm_colonia());
        contentValues.put(INEMPRESAS_COLUMN_MUNICIPIO,empresa.getEm_municipio());
        contentValues.put(INEMPRESAS_COLUMN_ESTADO, empresa.getEm_estado());
        contentValues.put(INEMPRESAS_COLUMN_PAIS ,empresa.getEm_pais());
        contentValues.put(INEMPRESAS_COLUMN_RFC ,empresa.getEm_rfc());

        return contentValues;
    }

    public void insertarWebService(InWebServices webService){
        db.insert(TABLE_NAME_INWEBSERVICES, null,contenedorWebService(webService));
    }

    public void insertarAgenteMovil(InAgentesMoviles inAgentesMoviles){
        db.insert(TABLE_NAME_INAGENTESMOVILES, null,contenedorAgentesMoviles(inAgentesMoviles));
    }

    public void insertarEmpresa(InEmpresas empresa){
        db.insert(TABLE_NAME_INEMPRESAS, null,contenedorEmpresa(empresa));
    }

    public Integer getEmpresa(){                          // existe agente
        String sql = "SELECT "+INEMPRESAS_COLUMN_EMPRESA +" FROM " + TABLE_NAME_INEMPRESAS ;
        Cursor c = db.rawQuery(sql,null);
        Integer count = null;
        if( c != null && c.moveToFirst() ) {
            count = c.getInt(0);
            c.close();
        }
        return count;
    }


    public Integer existeAgente(){                          // existe agente
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME_INAGENTESMOVILES;
        Cursor c = db.rawQuery(sql,null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
            if(count != 1){return 0;}
        return count;
    }
    public Integer getAgente(){                          // existe agente
        String sql = "SELECT "+INAGENTESMOVILES_COLUMN_NUMERO +" FROM " + TABLE_NAME_INAGENTESMOVILES;
        Cursor c = db.rawQuery(sql,null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        return count;
    }

    public String getNombreAgente(){                          // existe agente
        String sql = "SELECT "+INAGENTESMOVILES_COLUMN_NOMBRE +" FROM " + TABLE_NAME_INAGENTESMOVILES;
        Cursor c = db.rawQuery(sql,null);
        String count = null;
        if( c != null && c.moveToFirst() ) {
            count = c.getString(0);
            c.close();
        }
        return count;
    }

    public void updateAgente(String status, Integer numero){
        String sql = "UPDATE " + TABLE_NAME_INAGENTESMOVILES
                   + " SET " + INAGENTESMOVILES_COLUMN_STATUS +" = '" +status +"'"
                   + " WHERE " + INAGENTESMOVILES_COLUMN_NUMERO + " = " + numero;
        db.execSQL(sql);
    }

    public String getStatus(){                          // existe agente
        String sql = "SELECT "+INAGENTESMOVILES_COLUMN_STATUS +" FROM " + TABLE_NAME_INAGENTESMOVILES;
        Cursor c = db.rawQuery(sql,null);
        String count = null;
        if( c != null && c.moveToFirst() ) {
            count = c.getString(0);
            c.close();
        }
        return count;
    }

    public Integer getNextFolio(Integer numero) {                          // existe agente
        String sql = "SELECT " + INAGENTESMOVILES_COLUMN_NUM_COBRO + " FROM " + TABLE_NAME_INAGENTESMOVILES;
        Cursor c = db.rawQuery(sql, null);
        Integer count = null;
        if (c != null && c.moveToFirst()) {
            count = c.getInt(0);
            c.close();
        }
        ++count;
        String update = "UPDATE " + TABLE_NAME_INAGENTESMOVILES +
                " SET " + INAGENTESMOVILES_COLUMN_NUM_COBRO + "=" + count +
                " WHERE " + INAGENTESMOVILES_COLUMN_NUMERO + "=" + numero;
        db.execSQL(update);

        return count;
    }

    public Integer getFolio() {                          // existe agente
        String sql = "SELECT " + INAGENTESMOVILES_COLUMN_NUM_COBRO + " FROM " + TABLE_NAME_INAGENTESMOVILES;
        Cursor c = db.rawQuery(sql, null);
        Integer count = null;
        if (c != null && c.moveToFirst()) {
            count = c.getInt(0);
            c.close();
        }
        return count;
    }

    public String get(){                          // existe agente
        String sql = "SELECT "+INAGENTESMOVILES_COLUMN_PASS +" FROM " + TABLE_NAME_INAGENTESMOVILES;
        Cursor c = db.rawQuery(sql,null);
        String count = null;
        if( c != null && c.moveToFirst() ) {
            count = c.getString(0);
            c.close();
        }
        return count;
    }

    public String getWebService(Integer id) {                   // obtener una url del WebService
        String sql = "SELECT ws_url FROM " + TABLE_NAME_INWEBSERVICES +
                     " WHERE ws_id = " + id;
        Cursor c = db.rawQuery(sql,null);
        if( c != null && c.moveToFirst() ){
        String url=c.getString(0);
            c.close();
            return url;
        }
        return null;
    }

    public String getNombreEmpresa() {
        String sql = "SELECT "+INEMPRESAS_COLUMN_NOMBRE +" FROM " + TABLE_NAME_INEMPRESAS ;
        Cursor c = db.rawQuery(sql,null);
        String count = null;
        if( c != null && c.moveToFirst() ) {
            count = c.getString(0);
            c.close();
        }
        return count;

    }

    public String getDomicilioEmpresa() {
        String sql = "SELECT "+INEMPRESAS_COLUMN_CALLE +" FROM " + TABLE_NAME_INEMPRESAS ;
        Cursor c = db.rawQuery(sql,null);
        String count = null;
        if( c != null && c.moveToFirst() ) {
            count = c.getString(0);
            c.close();
        }
        return count;
    }

    public String getRfceEmpresa() {
        String sql = "SELECT "+INEMPRESAS_COLUMN_RFC +" FROM " + TABLE_NAME_INEMPRESAS ;
        Cursor c = db.rawQuery(sql,null);
        String count = null;
        if( c != null && c.moveToFirst() ) {
            count = c.getString(0);
            c.close();
        }
        return count;
    }

    public void setPassword(String password,Integer numero) {
        String sql = "UPDATE " + TABLE_NAME_INAGENTESMOVILES +
        " SET " + INAGENTESMOVILES_COLUMN_PASS + "=" + password+
        " WHERE " + INAGENTESMOVILES_COLUMN_NUMERO +"="+ numero;
        db.execSQL(sql);
    }

    public void login() {
        String sql = "UPDATE " + TABLE_NAME_INAGENTESMOVILES +
                " SET " + INAGENTESMOVILES_COLUMN_REMEMBER + "=" +"'true'" ;
        db.execSQL(sql);
    }

    public void logout() {
        String sql = "UPDATE " + TABLE_NAME_INAGENTESMOVILES +
                " SET " + INAGENTESMOVILES_COLUMN_REMEMBER + "="  +"'false'" ;
        db.execSQL(sql);
    }

    public void setLogin(Integer num_cobro) {
        String sql = "UPDATE " + TABLE_NAME_INAGENTESMOVILES +
                " SET " + INAGENTESMOVILES_COLUMN_NUM_COBRO + "="  + num_cobro ;
        db.execSQL(sql);
    }

    public String getLogin(){
        String sql = "SELECT "+INAGENTESMOVILES_COLUMN_REMEMBER +" FROM " + TABLE_NAME_INAGENTESMOVILES ;
        Cursor c = db.rawQuery(sql,null);
        String count = null;
        if( c != null && c.moveToFirst() ) {
            count = c.getString(0);
            c.close();
        }
        return count;
    }

}
