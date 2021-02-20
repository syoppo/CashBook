package com.example.cashbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    ArrayList<ListViewItem> itemlist = new ArrayList<ListViewItem>();

    @Override
    public int getCount(){
        return itemlist.size();
    }

    @Override
    public Object getItem(int position){
        return itemlist.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context c = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem, parent, false);
        }

        TextView date = (TextView)convertView.findViewById(R.id.date);
        TextView kind = (TextView)convertView.findViewById(R.id.kind);
        TextView content = (TextView)convertView.findViewById(R.id.content);
        TextView cash = (TextView)convertView.findViewById(R.id.cash);

        ListViewItem listitem = itemlist.get(pos);

        date.setText(listitem.getDate());
        kind.setText(listitem.getKind());
        content.setText(listitem.getContent());
        cash.setText(listitem.getCash());

        return convertView;
    }

    public void addItem(String date, String kind, String content, String cash, String inputNum){
        ListViewItem item = new ListViewItem();
        item.setDate(date);
        item.setKind(kind);
        item.setContent(content);
        item.setCash(cash);
        item.setInputNum(inputNum);

        itemlist.add(item);
    }
}
