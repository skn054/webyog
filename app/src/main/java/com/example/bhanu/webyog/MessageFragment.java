package com.example.bhanu.webyog;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

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
import java.util.Arrays;
import java.util.List;

/**
* Created by bhanu kiran on 28/09/2016.
*/


public  class MessageFragment extends Fragment {





    public static at.markushi.ui.CircleButton m_circleButton;
     public static ArrayList<Message> mlist;
    public static MessageAdapter adapter;
    public static ListView listView;
    public String ArrayKey="Key";
    public MessageFragment() {
    }

    public interface Callback {
        void onItemSelected( Message message);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        m_circleButton= (at.markushi.ui.CircleButton)  rootView.findViewById(R.id.button);
       m_circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),Compose.class);
                startActivity(intent);
            }
        });
        listView= (ListView) rootView.findViewById(R.id.list_view);


        //recyclerView=(RecyclerView)rootView.findViewById(R.id.recycler_view);
//        Message[] messages=new Message[]{
//                new Message("Sai","Requirment"),
//                new Message("Sai Kiran","Requirment"),
//                new Message("Sai Kumar","Nope"),
//                new Message("Kumar sai","Requirment"),
//                new Message("Sai kiran kumar","Requirment"),
//
//
//        };

        //MessageAdapter messageAdapter=new MessageAdapter(getActivity(),list);
//        layoutManager=new LinearLayoutManager(getActivity());
//
//        recyclerView.setLayoutManager(layoutManager);
//
//        adapter=new MessageAdapter(getActivity(),new ArrayList<Message>());
//        recyclerView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Message message= (Message) adapterView.getItemAtPosition(i);
//                Intent intent=new Intent(getActivity(),DetailActivity.class);
//                intent.putExtra(DetailFragment.MESSAGE_KEY,message);
               //startActivity(intent);
                //startActivityForResult(intent,DetailFragment.BODY_KEY);
                ((Callback) getActivity()).onItemSelected(message);

            }
        });
        adapter=new MessageAdapter(getActivity(),new ArrayList<Message>());
        listView.setAdapter(adapter);
        //Bundle bundle=getArguments();
//        if(bundle!=null)
//        {
//            Message message=bundle.getParcelable("MKEY");
//            Intent intent=new Intent(getActivity(),DetailActivity.class);
//            intent.putExtra(DetailFragment.MESSAGE_KEY,message);
//            startActivityForResult(intent,DetailFragment.BODY_KEY);
//        }
       if(savedInstanceState!=null)
        {
            mlist=savedInstanceState.getParcelableArrayList(ArrayKey);
            adapter.setData(mlist);
        }
        else
        {
            MessageTask task=new MessageTask();
            task.execute();
        }






        return rootView;
    }









    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mlist!=null)
        {
            outState.putParcelableArrayList(ArrayKey,mlist);
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //super.onActivityResult(requestCode, resultCode, data);
////        if(requestCode==BodyActivity.BODY_KEY&&resultCode== Activity.RESULT_OK)
////            adapter.notifyDataSetChanged();
//        if(resultCode==2&&data!=null)
//        {
//            Message message=data.getParcelableExtra("DEL");
//            //mlist.remove(message);
//            for(Message message1:mlist)
//            {
//                if(message1.getId()==message.getId())
//                   message=message1;
//            }
//            mlist.remove(message);
//
//
//            adapter=new MessageAdapter(getActivity(),mlist);
//            listView.setAdapter(adapter);
////            adapter.notifyDataSetChanged();
////            listView.notify();
//
//
//        }
//    }

    public class MessageTask extends AsyncTask<Void,Void,List<Message>>
    {
        HttpURLConnection connection;
        String json=null;
        BufferedReader reader=null;
        @Override
        protected List<Message> doInBackground(Void... voids) {

            String url="http://10.0.3.2:8088/api/message";
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
                //Log.d("MessageTask",json);
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
        public List<Message> makejson(String json) throws JSONException {
            JSONArray array=new JSONArray(json);
            int l=array.length();
            List<Message> messages=new ArrayList<>(l);
            for(int i=0;i<l;i++)
            {
                JSONObject object=array.getJSONObject(i);
                Message message=new Message(object);
                messages.add(message);
            }
//            for(Message message:messages)
//            {
//                Log.d("List Message",message.getSubject());
//            }
            return messages;
        }

        @Override
        protected void onPostExecute(List<Message> messages) {
           if(messages!=null)
           {
              adapter.setData(messages);
               mlist=new ArrayList<Message>();
               for(Message message:messages)
               {
                   mlist.add(message);
               }


           }
        }
    }

}