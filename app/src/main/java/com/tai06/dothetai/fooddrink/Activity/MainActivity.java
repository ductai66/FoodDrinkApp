package com.tai06.dothetai.fooddrink.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import  androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.tai06.dothetai.fooddrink.Activity.LoginSignup.LoginActivity;
import com.tai06.dothetai.fooddrink.Contacts.HomeLink;
import com.tai06.dothetai.fooddrink.Fragment.DonHangFragment;
import com.tai06.dothetai.fooddrink.Fragment.HomeFragment;
import com.tai06.dothetai.fooddrink.Object.OOP.Person;
import com.tai06.dothetai.fooddrink.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int MSG_HEADER_VIEW = 1;

    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    private boolean back_home;
    private Handler handler;
    private Person person;
    private TextView name_person,email_person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initHander();
        getInfoPerson();
    }

    private void initHander(){
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case MSG_HEADER_VIEW:
                        person = (Person) msg.obj;
                        headerView(person);
                        break;
                }
            }
        };
    }

    private void headerView(Person person){
        View headerView = navigationView.getHeaderView(0);
        name_person = headerView.findViewById(R.id.name_person);
        email_person = headerView.findViewById(R.id.email_person);
        String id_ps = String.valueOf(person.getId_person());
        HomeLink.id_person = id_ps;
        String ten = person.getName();
        HomeLink.Name = ten;
        String sdt = person.getSdt();
        HomeLink.Sdt = sdt;
        name_person.setText(person.getName());
        email_person.setText(person.getEmail());
    }

    private void getInfoPerson(){
        Intent intent = getIntent();
        final String mail = intent.getStringExtra("email");
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HomeLink.URL_INFO_PERSON, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Person person = new Person();
                    JSONObject jsonObject = new JSONObject(response);
                    person.setId_person(jsonObject.getInt("id_person"));
                    person.setEmail(jsonObject.getString("email"));
                    person.setPassword(jsonObject.getString("password"));
                    person.setName(jsonObject.getString("name"));
                    person.setSdt(jsonObject.getString("sdt"));
                    Message msg = new Message();
                    msg.what = MSG_HEADER_VIEW;
                    msg.obj = person;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                Log.d("AAA","Error" + error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("email",mail);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void init(){
        toolbar =findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.draw_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new HomeFragment()).commit();
        navigationView.setCheckedItem(R.id.menu_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_bar:
                startActivity(new Intent(MainActivity.this,SearchViewActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displayView(item.getItemId());
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayView(int viewID){
        switch (viewID){
            case R.id.menu_home:
                showFragments(new HomeFragment());
                back_home = true;
                break;
            case R.id.menu_donhang:
                showFragments(new DonHangFragment());
                back_home = false;
                break;
            case R.id.menu_dangxuat:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void showFragments(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_container,fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        if (!back_home){ // nếu view hiện tại không phải là homefragment
            displayView(R.id.menu_home); // sẽ hiển thị view home fragment
        }
        else{
            moveTaskToBack(true); // nếu là view home fragment ,sẽ thoát khỏi app
//            super.onBackPressed();
        }
    }
}
