package com.example.bhanu.webyog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by bhanu kiran on 28/09/2016.
 */
//public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
//
//    private List<Message> mlist;
//    private Context mcontext;
//    public class MyViewHolder extends RecyclerView.ViewHolder
//    {
//          public TextView name;
//        public TextView sub;
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//            name= (TextView) itemView.findViewById(R.id.name);
//            sub= (TextView) itemView.findViewById(R.id.sub);
//        }
//    }
//    public MessageAdapter(Context context,List<Message> list)
//    {
//          mlist=list;
//        mcontext=context;
//    }
//    public void setData(List<Message> messages)
//    {
//        for(Message message:messages)
//        {
//            mlist.add(message);
//            notifyDataSetChanged();
//        }
//    }
//
//
//
//    @Override
//    public MessageAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View view= LayoutInflater.from(mcontext).inflate(R.layout.list_item,viewGroup,false);
//        return new MyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(MessageAdapter.MyViewHolder viewHolder, int i) {
//          Message message=mlist.get(i);
//           viewHolder.name.setText(message.getName());
//          viewHolder.sub.setText(message.getSubject());
//    }
//
//    @Override
//    public int getItemCount() {
//        return mlist.size();
//    }
//}
public class MessageAdapter extends BaseAdapter
{

    private List<Message> mlist;
       private Context mcontext;
    private static final int VIEW_TYPE_Large = 0;
    private static final int VIEW_TYPE_Small = 1;
    private final LayoutInflater mInflater;
    public MessageAdapter(Context context,List<Message> list)
    {
        mcontext=context;
        mlist=list;
        mInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int i) {
        return mlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            //int p=getItemViewType(i);
//            if(p==0)
//            {
//                view=mInflater.inflate(R.layout.list_item_1,viewGroup,false);
//            }
//            else
//            {
//                view=mInflater.inflate(R.layout.list_item,viewGroup,false);
//
//            }
            view=mInflater.inflate(R.layout.list_item_1,viewGroup,false);

            ViewHolder viewHolder=new ViewHolder(view);
            view.setTag(viewHolder);

        }
        ViewHolder holder= (ViewHolder) view.getTag();
        Message message= (Message) getItem(i);
        int read=message.getRead();
        if(read==0)
        {
             holder.isread.setImageResource(R.drawable.ic_done_all_black_24dp);
        }
        else
        {
                 holder.isread.setImageResource(R.drawable.doubletick);
        }
        int star=message.getStar();

        //long timestap=message.getTimestap();
        String sub=message.getPreview();
        if(sub.length()>70)
        {
            sub=sub.substring(0,70);
            sub=sub+"...";
        }
        holder.subject.setText("Sub: "+message.getSubject());

        DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        String d="";
        String dateformat=format.format(date);
        String arr[]=dateformat.split("-");
        String s=arr[1];
        int digit=s.charAt(1)-'0';
        d=getMonth(digit);

        d=d+"-"+arr[2];
        holder.date.setText(d);
        String sender="From:";
       sender+=message.getName1()+" "+message.getName2();
        holder.sender.setText(sender);
//        if(i==0)
//        {
//            String sender="From:";
//            sender+=message.getName1()+" "+message.getName2();
//            holder.sender.setText(sender);
////            if(star==0)
////            {
////                holder.isstar.setImageResource(R.drawable.blankstar);
////            }
////            else
////                holder.isstar.setImageResource(R.drawable.filledstar);
//        }
        if(star==0)
        {
            holder.isstar.setImageResource(R.drawable.blankstar);
        }
        else{
            holder.isstar.setImageResource(R.drawable.filledstar);
        }
        holder.message.setText(sub);


        return view;
    }

    private String getMonth(int s) {

        String month="";
        if(s==1)
        {
            month="JAN";
        }
        else if(s==2)
        {
            month="FEB";
        }
        else if(s==3)
        {
            month="MAR";
        }
        else if(s==4)
        {
            month="APR";
        }
        else if(s==5)
        {
            month="MAY";
        }
        else if(s==6)
        {
            month="JUN";
        }
        else if(s==7)
        {
            month="JUL";
        }
        else if(s==8)
        {
            month="AUG";
        }
        else if(s==9)
        {
            month="SEP";
        }
        else if(s==0)
        {
            month="OCT";
        }
        else if(s==1)
        {
            month="NOV";
        }
        else if(s==2)
        {
            month="DEC";
        }
        return month;


    }

    public void setData(List<Message> messages) {
        clear();
        for(Message message:messages)
        {
            mlist.add(message);
            notifyDataSetChanged();
        }
    }
    private void clear() {

        mlist.clear();
        notifyDataSetChanged();
    }
    public static class ViewHolder
    {
        TextView subject,date,sender,message;
        ImageView isread,isstar;
        public ViewHolder(View view)
        {
            isread= (ImageView) view.findViewById(R.id.read);
           sender= (TextView) view.findViewById(R.id.sender);
            isstar= (ImageView) view.findViewById(R.id.star);
            date= (TextView) view.findViewById(R.id.date);
            subject= (TextView) view.findViewById(R.id.subject);
            message= (TextView) view.findViewById(R.id.message);
        }

    }

//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//       return position==0 ? VIEW_TYPE_Large:VIEW_TYPE_Small;
//    }
}
