package com.example.bhanu.webyog;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class BodyActivity extends Activity {

    public static  String MESSAGE_KEY;
    TextView cname,pname,mbody,memail;
    public static int BODY_KEY=1;
    Message message;

    int k=0,c=0;
    public int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body);
        cname= (TextView) findViewById(R.id.cname);
        pname= (TextView) findViewById(R.id.pname);
        mbody= (TextView) findViewById(R.id.body);
        memail= (TextView)findViewById(R.id.email);
        Intent intent=getIntent();
        if(intent!=null&&intent.hasExtra(MESSAGE_KEY))
        {
           message =intent.getParcelableExtra(MESSAGE_KEY);
            message.setRead(1);
            id=message.getId();
            String vid=String.valueOf(id);
            MessageTask1 task1=new MessageTask1();
            task1.execute(vid);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_body, menu);
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
        if(id==R.id.delete)
        {
           Intent intent=new Intent();
            intent.putExtra("DEL",message);
            setResult(2,intent);
            finishEditing();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finishEditing();
    }

    private void finishEditing() {

        finish();
    }

    public class MessageTask1 extends AsyncTask<String,Void,String[]>
    {
        HttpURLConnection connection;
        String json=null;
        BufferedReader reader=null;
        @Override
        protected String[] doInBackground(String... params) {

            String baseurl="http://10.0.3.2:8088/api/message";
            String url=baseurl.concat("/"+params[0]);
            try {
                URL url1 =new URL(url);
                connection= (HttpURLConnection) url1.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream inputStream=connection.getInputStream();
                if(inputStream==null)
                    return null;
                reader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer=new StringBuffer();
                String line;
                while((line=reader.readLine())!=null)
                {
                    buffer.append(line);
                }
                if(buffer.length()==0)
                    return null;
                json=buffer.toString();
                Log.d("MessageTask", json);
                return makejson(json);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        public String[] makejson(String json) throws JSONException {
            JSONObject object=new JSONObject(json);
            JSONArray part=object.getJSONArray("participants");
            int l=part.length();
            String[] str=new String[10];
            int i;
            for(i=0;i<l;i++)
            {
                JSONObject jsonObject=part.getJSONObject(i);
                str[k++]=jsonObject.getString("name");
                str[k++]=jsonObject.getString("email");
                c=c+2;

            }
            str[k++]=object.getString("body");
//            List<Body> messages=new ArrayList<>(l);
//            for(int i=0;i<l;i++)
//            {
//                JSONObject object=array.getJSONObject(i);
//
//            }
//            for(Message message:messages)
//            {
//                Log.d("List Message",message.getSubject());
//            }
            //return messages;
            return str;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            if(strings.length>0)
            {
                String name="",email="email: ",body="";
                for(int i=1;i<=c;i++)
                {
                    if(i%2!=0) {
                        name = name + strings[i-1]+", ";

                    }
                    else
                    {
                        email=email+strings[i-1]+", ";
                    }
                }
                int l=name.length();
                name=name.substring(0,l-2);
                l=email.length();
                email=email.substring(0,l-2);
                String t=name.charAt(0)+" ";
                cname.setText(t);
                for(int i=c;i<k;i++)
                {
                    body+=strings[i];
                }
                mbody.setText(body);
                pname.setText("From: "+name);
                memail.setText(email);


            }
        }
    }
}
