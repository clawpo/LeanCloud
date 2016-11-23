package cn.ucai.leanclouddemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        User user = User.getCurrentUser(User.class);
        Log.e("welcome","user="+user);
        if(user==null){
            startActivity(new Intent(this,LoginActivity.class));
        }else{
            startActivity(new Intent(this,MainActivity.class));
        }
    }
}
