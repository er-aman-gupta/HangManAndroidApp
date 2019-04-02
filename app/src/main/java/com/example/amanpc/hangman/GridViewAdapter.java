package com.example.amanpc.hangman;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class GridViewAdapter extends BaseAdapter {
    static List<View> myList=new ArrayList<>();
    List<String> list;
    Activity activity;
    public GridViewAdapter(Activity activity,List<String> list) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {

        return list.size();
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        LayoutInflater layoutInflater=activity.getLayoutInflater();
        final View view=layoutInflater.inflate(R.layout.gridviewlayout,null,false);
        final TextView textView=view.findViewById(R.id.gridViewLayoutTextId);
        //myList.add(view.findViewById(R.id.gridViewLayoutTextId));
        textView.setText(list.get(position));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity=new MainActivity();
                String temp=String.valueOf(textView.getText());
                if(textView.getText().equals(" ")){
                    return;
                }
                int p=mainActivity.wordChecker(temp,activity);
                if(p==0){
                    textView.setText(" ");
                    MainActivity.list.set(position," ");
                    textView.setEnabled(false);
                }
                else if(p==-1){
                    MainActivity.list.set(position," ");
                    textView.setText(" ");
                    textView.setEnabled(false);
                }

            }
        });
        return view;
    }
}

class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder>{

    List<String> list;
    Activity activity;
    class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.wordSetLayoutId);
        }
    }

    public RecycleViewAdapter(Activity activity,List<String> list) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(activity).inflate(R.layout.wordshowlayout,null,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

}
