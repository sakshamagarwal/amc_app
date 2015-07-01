package com.example.saksham.amc_app;

import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


public class maintainence_newreq2 extends FragmentActivity
{
	private PagerAdapter mPagerAdapter;
	
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.maintainence_newreq2);
        ImageButton back = (ImageButton)findViewById(R.id.back_newreq2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),new_req1.class));
            }
        });
        KeyboardManager km = new KeyboardManager((ViewGroup)findViewById(R.id.new_req2_layout), getApplicationContext());
        Button submit = (Button)findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Thanks for posting your Request",Toast.LENGTH_SHORT).show();
            }
        });
	    this.initialisePaging();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_req_actions,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.enquiry_button:
                Toast.makeText(getApplicationContext(), "Switching to Enquiry", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.history_button:
                startActivity(new Intent(getApplicationContext(), maintainance.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initialisePaging() {
		// TODO Auto-generated method stub
		List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, datechoice1.class.getName()));
        fragments.add(Fragment.instantiate(this, datechoice2.class.getName()));
        this.mPagerAdapter  = new mainreq2_adapter(super.getSupportFragmentManager(), fragments);
        //
        ViewPager pager = (ViewPager)findViewById(R.id.viewpager);
        pager.setAdapter(this.mPagerAdapter); 
	}
	
}
