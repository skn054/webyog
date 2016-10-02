package com.example.bhanu.webyog;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhanu kiran on 29/09/2016.
 */
public  class DetailFragment extends Fragment {

    public static final String TAG = DetailFragment.class.getSimpleName();

    public static int BODY_KEY=1;
    public static  String MESSAGE_KEY;
    String me;

    TextView cname,pname,mbody,memail;
    int k=0,c=0;
    Message message;
    //public static int  RESULT_KEY=0;
    public int id;
    public DetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        cname= (TextView) rootView.findViewById(R.id.cname);
        pname= (TextView) rootView.findViewById(R.id.pname);
        mbody= (TextView) rootView.findViewById(R.id.body);
        memail= (TextView) rootView.findViewById(R.id.email);
//        Intent intent=getActivity().getIntent();
//        if(intent!=null&&intent.hasExtra(MESSAGE_KEY))
//        {
//            message=intent.getParcelableExtra(MESSAGE_KEY);
//            message.setRead(1);
//            id=message.getId();
//            String vid=String.valueOf(id);
//            MessageTask1 task1=new MessageTask1();
//            task1.execute(vid);
//
//        }
        Bundle bundle=getArguments();
        if(bundle!=null)
        {
        message=bundle.getParcelable(MESSAGE_KEY);
        id=message.getId();
            String vid=String.valueOf(id);
            MessageTask1 task1=new MessageTask1();
            task1.execute(vid);
        }
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail1,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.delete)
        {
            Intent intent=new Intent();
            intent.putExtra("DEL",message);
            getActivity().setResult(2,intent);
            finishEditing();
            return true;
        }
        else if(id==R.id.reply)
        {
            Intent intent=new Intent(getActivity(),Compose.class);
            intent.putExtra(Compose.MESS,me);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void finishEditing() {

        getActivity().finish();
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
                String t=name.charAt(0)+" ";
                cname.setText(t);
                for(int i=c;i<k;i++)
                {
                    body+=strings[i];
                }
                mbody.setText(body);
                int l=name.length();
                name=name.substring(0,l-2);
                l=email.length();
                email=email.substring(0,l-2);
                me=email.substring(6,l-2);
                pname.setText("From: "+name);
                memail.setText(email);


            }
        }
    }



}
