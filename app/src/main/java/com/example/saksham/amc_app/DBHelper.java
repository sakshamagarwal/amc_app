package com.example.saksham.amc_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBHelper {

	public static final String TAG = "DBHelper";
	public static final String DATABASE_NAME = "namastetaxi.sqlite";
	public static final int DATABASE_VERSION = 3;
	public static final String CREATE_CUSTOMER_TABLE =
			"create table if not exists customer_info (user_id text, name text, phone text, email text, address text);";
    private SQLiteDatabase db;

    public static final String CREATE_LOGIN_TABLE = "create table if not exists login_info (user_id text);";

    public static final String CREATE_PRODUCT_TABLE =
            "create table if not exists product_info (user_id text, vendor text, category text, product_name text);";

    public static final String CREATE_PROBLEM_TABLE = "create table if not exists problem_info (category text, problem text);";

    public static final String CREATE_REQUEST_TABLE =
            "create table if not exists request_info (vendor text, user text, device text, problem text, description text, dates text, status text);";

    public static final String CREATE_ENQUIRY_TABLE =
            "create table if not exists enquiry_info (vendor text, user text, device text, cat text, status text);";

    public static final String CREATE_ENGINEER_TABLE =
            "create table if not exists engineer_info (vendor text, eng_id text, eng_name text);";
	/*public static final String CREATE_BOOKING_INFO_TABLE =
			"create table if not exists booking_info (driver_id text, pickup_time text, driver_curr_lat text, " +
			"driver_curr_long text, driver_name text, driver_phone text, cab_no text, cab_type text, driver_img text);";*/
	
	/*public static final String CREATE_PICKDROP_INFO_TABLE =
			"create table if not exists pickdrop_info (id integer primary key autoincrement, pickup_lat text, " +
			"pickup_lng text, drop_lat text, drop_lng text);";*/
	private final Context context;
	private DatabaseHelper DBHelper;
	public DBHelper(Context ctx)
	{
		this.context = ctx;
		this.DBHelper = new DatabaseHelper(this.context);
	}
	private class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			try {
				db.execSQL(CREATE_CUSTOMER_TABLE);
                db.execSQL(CREATE_LOGIN_TABLE);
				db.execSQL(CREATE_PRODUCT_TABLE);
				db.execSQL(CREATE_PROBLEM_TABLE);
                db.execSQL(CREATE_ENQUIRY_TABLE);
                db.execSQL(CREATE_REQUEST_TABLE);
                db.execSQL(CREATE_ENGINEER_TABLE);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
			//db.execSQL("DROP TABLE IF EXISTS enquiry_info");
			onCreate(db);
		}
	}
	//---opens the database---
	public DBHelper open() throws SQLException
	{
        db = this.DBHelper.getWritableDatabase();
		return this;
	}
	//---closes the database---
	public void close()
	{
		this.DBHelper.close();
	}

    public void insert (String... data) {
        String uid = data[0];
        String name = data[1];
        String email = data[2];
        String mobile = data[3];
        String address = data[4];
        ContentValues values = new ContentValues();
        values.put("user_id", uid);
        values.put("email", email);
        values.put("phone", mobile);
        values.put("name", name);
        values.put("address", address);
        if (!isPresent("customer_info", "user_id", uid)) {
           db.insert("customer_info", null, values);
        }
    }

    public void logout() {
        db.delete("login_info", null, null);
    }

    public Boolean isLoggedIn() {
        String query = "select * from login_info";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        return (!cursor.isAfterLast());
    }

    public String get_user() {
        String query = "select * from login_info";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public String get_name(String uid) {
        String query = "select * from customer_info";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String result = "";
        while (!cursor.isAfterLast()) {
            if (cursor.getString(0).equals(uid)) {
                result = cursor.getString(1);
            }
            cursor.moveToNext();
        }
        return result;
    }

    public void login(String uid) {
        ContentValues values = new ContentValues();
        values.put("user_id", uid);
        db.insert("login_info", null, values);
    }

    public Cursor getValues(){
        String selquery="select * from customer_info";
        Cursor cur = db.rawQuery(selquery, null);
        return cur;
    }

    public boolean isPresent(String table_name, String colname, String data) {
        String query = "select * from " + table_name + " where " + colname + "=\"" + data + "\"";
        Cursor cur = db.rawQuery(query,null);
        cur.moveToFirst();
        if (cur.isAfterLast()) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor get_products(String uid) {
        String selquery="select * from product_info where user_id = \"" + uid + "\"";
        Cursor cur = db.rawQuery(selquery, null);
        return cur;
    }

    public void add_product(String... data) {
        String uid = data[0];
        String vendor = data[1];
        String category = data[2];
        String name = data[3];
        ContentValues values = new ContentValues();
        values.put("user_id", uid);
        values.put("vendor", vendor);
        values.put("category", category);
        values.put("product_name", name);
        Cursor c = get_products(uid);

        c.moveToFirst();
        Boolean flag = false;
        while (!c.isAfterLast()) {
            if (c.getString(1).equals(vendor) && c.getString(2).equals(category) && c.getString(3).equals(name)) {
                flag = true;
            }
            c.moveToNext();
        }
        if (!flag) {
            db.insert("product_info", null, values);
        }
    }

    public void add_problems(String... data) {
        String category = data[0];
        String problem = data[1];
        ContentValues values = new ContentValues();
        values.put("category", category);
        values.put("problem", problem);
        Cursor cursor = get_problems(category);
        cursor.moveToFirst();
        Boolean flag = false;
        while (!cursor.isAfterLast()) {
            if (cursor.getString(1).equals(problem)) {
                flag = true;
            }
            cursor.moveToNext();
        }

        if (!flag) {
            db.insert("problem_info", null, values);
        }
    }

    public Cursor get_problems(String category) {
        String selquery="select * from problem_info where category = \"" + category + "\"";
        Cursor cur = db.rawQuery(selquery, null);
        return cur;
    }

    public String get_category(String uid, String product) {
        String selquery="select * from product_info where user_id = \"" + uid + "\" and product_name = \"" + product + "\"";
        Cursor cur = db.rawQuery(selquery, null);
        cur.moveToFirst();
        return cur.getString(2);
    }

    public Cursor get_requests(String col_name, String id) {
        String selquery = "select * from request_info where " + col_name + " = \"" + id + "\"";
        Cursor cur = db.rawQuery(selquery, null);
        return cur;
    }

    public void add_request(String... data) {
        String vendor = data[0];
        String user = data[1];
        String device = data[2];
        String problem = data[3];
        String description = data[4];
        String dates = data[5];
        String status = data[6];
        ContentValues values = new ContentValues();
        values.put("user", user);
        values.put("vendor", vendor);
        values.put("device", device);
        values.put("description", description);
        values.put("problem", problem);
        values.put("dates", dates);
        values.put("status", status);
        Cursor cursor = get_requests("user", user);
        cursor.moveToFirst();
        Boolean flag = false;
        while (!cursor.isAfterLast()) {
            if (cursor.getString(0).equals(vendor) && cursor.getString(2).equals(device) && cursor.getString(3).equals(problem) && cursor.getString(4).equals(description) && cursor.getString(5).equals(dates)) {
                String query = "delete from request_info where vendor = \"" + vendor + "\" and device = \"" + device + "\" and user = \"" + user + "\" and problem = \"" + problem + "\" and dates = \"" + dates + "\"";
                db.execSQL(query);
            }
            cursor.moveToNext();
        }

        db.insert("request_info", null, values);

    }

    public Cursor get_enquiries(String col_name, String id) {
        String selquery = "select * from enquiry_info where " + col_name + " = \"" + id + "\"";
        Cursor cur = db.rawQuery(selquery, null);
        return cur;
    }

    public void add_enquiry(String... data) {
        String vendor = data[0];
        String user = data[1];
        String device = data[2];
        String category = data[3];
        String status = data[4];
        ContentValues values = new ContentValues();
        values.put("user", user);
        values.put("vendor", vendor);
        values.put("device", device);
        values.put("cat", category);
        values.put("status", status);
        Cursor cursor = get_enquiries("user", user);
        cursor.moveToFirst();
        Boolean flag = false;
        while (!cursor.isAfterLast()) {
            if (cursor.getString(0).equals(vendor) && cursor.getString(1).equals(user) && cursor.getString(2).equals(device) && cursor.getString(3).equals(category)) {
                String query = "delete from enquiry_info where vendor = \"" + vendor + "\" and device = \"" + device + "\" and user = \"" + user + "\" and cat = \"" + category + "\"";
                db.execSQL(query);
            }
            cursor.moveToNext();
        }

        db.insert("enquiry_info", null, values);

    }

    public Cursor get_engineers(String vendor) {
        String selquery = "select * from engineer_info where vendor = \"" + vendor + "\"";
        Cursor cur = db.rawQuery(selquery, null);
        return cur;
    }

    public void add_engineer(String... data) {
        String vendor = data[0];
        String eng_id = data[1];
        String eng_name = data[2];
        ContentValues values = new ContentValues();
        values.put("vendor", vendor);
        values.put("eng_id", eng_id);
        values.put("eng_name", eng_name);
        Cursor cursor = get_engineers(vendor);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getString(1).equals(eng_id)) {
                String query = "delete from engineer_info where eng_id = \"" + eng_id + "\"";
                db.execSQL(query);
            }
            cursor.moveToNext();
        }
        db.insert("engineer_info", null, values);
    }
}
