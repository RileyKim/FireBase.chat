package com.taeksukim.android.firebasechat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText editId, editPw;
    Button button;

    FirebaseDatabase database;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("user");


        editId = (EditText) findViewById(R.id.editId);
        editPw = (EditText) findViewById(R.id.editPw);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = editId.getText().toString();
                final String pw = editPw.getText().toString();

                userRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount() > 0){
                            String fbPw = dataSnapshot.child("password").getValue().toString();
                            String name = dataSnapshot.child("name").getValue().toString();
                            Log.i("mainActivity", "pw :" + fbPw);
                            Log.i("mainActivity", "name :" + name);
                            if(fbPw.equals(pw)){
                                Intent intent = new Intent(MainActivity.this, RoomListActivity.class);
                                intent.putExtra("userid", id);
                                intent.putExtra("username", name);
                                startActivity(intent);
                            }else {
                                Toast.makeText(MainActivity.this, "비밀번호가 틀렸습니다.",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(MainActivity.this , "User가 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
