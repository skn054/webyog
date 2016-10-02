package com.example.bhanu.webyog;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends Activity implements MessageFragment.Callback{

    boolean isTwopane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setIcon(R.drawable.ic_email_black_24dp);
        getActionBar().setTitle("Inbox");
        if(findViewById(R.id.movie_detail_container)!=null)
        {
            isTwopane=true;
        }
        else
        {
            isTwopane=false;
        }
//        if (savedInstanceState == null) {
//            getFragmentManager().beginTransaction()
//                    .add(R.id.container, new MessageFragment())
//                    .commit();
//        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==2&&data!=null)
        {
            Message message=data.getParcelableExtra("DEL");
            //mlist.remove(message);
            for(Message message1:MessageFragment.mlist)
            {
                if(message1.getId()==message.getId())
                    message=message1;
            }
            MessageFragment.mlist.remove(message);


            MessageFragment.adapter=new MessageAdapter(this,MessageFragment.mlist);
            MessageFragment.listView.setAdapter(MessageFragment.adapter);
//            adapter.notifyDataSetChanged();
//            listView.notify();


        }
    }

    @Override
    public void onItemSelected(Message message) {

        if(isTwopane)
        {
             Bundle bundle=new Bundle();
            bundle.putParcelable(DetailFragment.MESSAGE_KEY,message);
            DetailFragment fragment=new DetailFragment();
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.movie_detail_container,fragment,DetailFragment.TAG).commit();

        }
        else
        {
            Intent intent=new Intent(this,DetailActivity.class);
                intent.putExtra(DetailFragment.MESSAGE_KEY,message);
            //startActivity(intent);
            startActivityForResult(intent,DetailFragment.BODY_KEY);

        }
    }




    public void onActivityResult(int resultCode, Intent data) {

    }
    /**
     * A placeholder fragment containing a simple view.
     */

}
