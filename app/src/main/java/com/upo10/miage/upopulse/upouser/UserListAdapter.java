package com.upo10.miage.upopulse.upouser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.upo10.miage.upopulse.R;
import com.upo10.miage.upopulse.upowbs.RequestWebService;

import java.util.List;

/**
 * Created by Sonatines on 26/05/2015.
 */
public class UserListAdapter extends BaseAdapter
{
    private List<Users> listeUsers = null;
    LayoutInflater layoutInflater;
    Context context;

    public UserListAdapter(Context context, List<Users> listeUsers)
    {
        layoutInflater = LayoutInflater.from(context);
        this.listeUsers = listeUsers;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listeUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return listeUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView nomView;
        ToggleButton toggleView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.user_row, null);
            holder = new ViewHolder();

            holder.nomView = (TextView) convertView.findViewById(R.id.nom);
            holder.toggleView = (ToggleButton) convertView.findViewById(R.id.togglebutton);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nomView.setText(listeUsers.get(position).getNom() + " " + listeUsers.get(position).getPrenom());
        holder.toggleView.setChecked(false);

        holder.toggleView.setOnClickListener(new View.OnClickListener() {
            RequestWebService r = new RequestWebService();
            User u = User.getInstance();

            @Override
            public void onClick(View v) {
                if (holder.toggleView.isChecked()) {
                    try {
                        r.changeDroitUsers(listeUsers.get(position).getMail(), u.getToken(), 1);
                    }
                    catch (Exception e) {
                    }
                } else {
                    try {
                        r.changeDroitUsers(listeUsers.get(position).getMail(), u.getToken(), 0);
                    }
                    catch(Exception e){}
                }
            }
        });

        return convertView;
    }
}
