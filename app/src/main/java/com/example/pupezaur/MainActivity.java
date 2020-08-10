package com.example.pupezaur;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pupezaur.Util.UserUtil;
import com.example.pupezaur.connections.ConnectionHandler;
import com.example.pupezaur.connections.SocketEventHandler;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pupezaur.ui.main.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    ConnectionHandler connectionHandler;
    SocketEventHandler socketEventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        connectionHandler = new ConnectionHandler();
        connectionHandler.connectSocket();
        socketEventHandler = new SocketEventHandler(connectionHandler,this);
        socketEventHandler.doSocketEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settingsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if (id == R.id.SettingsButton){
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return false;
        }
        return super.onOptionsItemSelected (item);
    }

    public void send(View view) {
        JSONObject message = new JSONObject();
        String name = UserUtil.getName();
        EditText chatBox = findViewById(R.id.text_send);
        TextView chat = findViewById(R.id.chat);
        try {
            message.put("name", name);
            message.put("message", chatBox.getText());
            if(message.get("message").toString().equals("") || message.get("message").toString().isEmpty()){
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        chat.append(name + ": "+ chatBox.getText() + "\n");
        connectionHandler.getSocket().emit("sendMessage", message);
    }

    public void updateMessage(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView chat = findViewById(R.id.left_chat);
                chat.append(socketEventHandler.getPersonName() + ": "+socketEventHandler.getMessage() + "\n");
            }
        });
    }

    public boolean logout (@org.jetbrains.annotations.NotNull Button button) {
        switch (button.getId()) {
            case R.id.btn_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                return true;
        }
        return false;
    }
}
