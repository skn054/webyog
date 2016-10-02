package com.example.bhanu.webyog;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bhanu kiran on 28/09/2016.
 */
public class Message implements Parcelable{
    private String name1="",name2="", subject,preview;
    private boolean isRead,isStarred;
    private int id,read,star;
    long timestap;

    public Message(JSONObject object) throws JSONException {
        subject=object.getString("subject");
        JSONArray jsonArray=object.getJSONArray("participants");
        int l=jsonArray.length();
        for(int i=0;i<l;i++)
        {
            if(name1==null)
                name1=jsonArray.getString(i);
            else
                name2=jsonArray.getString(i);
        }
        id=object.getInt("id");
        preview=object.getString("preview");
        timestap=object.getLong("ts");
        isRead=object.getBoolean("isRead");
        read=0;

        isStarred=object.getBoolean("isStarred");
        if(isStarred)
            star=1;
        else star=0;
    }


//    public Message(String name,String subject)
//    {
//        this.name=name;
//        this.subject=subject;
//    }
//
//
//
//    public String getName() {
//        return name;
//    }






    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(subject);
        parcel.writeString(name1);
        parcel.writeString(name2);
        parcel.writeInt(read);
        parcel.writeInt(star);
        parcel.writeInt(id);
        parcel.writeLong(timestap);
        parcel.writeString(preview);

    }
    public Message(Parcel parcel)
    {
        subject=parcel.readString();
        name1=parcel.readString();
        name2=parcel.readString();
        read=parcel.readInt();
        star=parcel.readInt();
        id=parcel.readInt();
        timestap=parcel.readLong();
        preview=parcel.readString();

    }
    public void setRead(int i)
    {
        read=i;
    }


    public static final Creator<Message> CREATOR=new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel parcel) {
            return new Message(parcel);
        }

        @Override
        public Message[] newArray(int i) {
            return new Message[0];
        }
    };

    public String getSubject() {
        return subject;
    }
    public String getName1()
    {
        return name1;
    }
    public String getName2(){return name2;}
    public int getRead(){return read;}
    public int getStar(){return star;}
    public int getId(){return id;}
    public String getPreview(){return preview;}

}