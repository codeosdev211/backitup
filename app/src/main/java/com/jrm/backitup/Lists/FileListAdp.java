package com.jrm.backitup.Lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jrm.backitup.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FileListAdp extends RecyclerView.Adapter<FileListHolder> {
    Context context;
    ArrayList<JSONObject> fileList;

    public FileListAdp(Context context) {
        this.context = context;
        fileList = new ArrayList<>();
    }

    @Override
    public FileListHolder onCreateViewHolder(ViewGroup parent, int type) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View layout = inflater.inflate(R.layout.files_list, parent, false);
        return new FileListHolder(layout);
    }

    @Override
    public void onBindViewHolder(FileListHolder holder, int position) {
        try {
        holder.fileName.setText(fileList.get(position).getString("name"));
        holder.fileExt.setText(fileList.get(position).getString("extension"));
        holder.fileSize.setText(fileList.get(position).getString("originalSize"));
        holder.fileCode.setText(fileList.get(position).getString("code"));
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        }catch(Exception error) {
            Toast.makeText(context.getApplicationContext(), "Could not load file names", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() { return fileList.size(); }

    public void add(JSONArray data) throws Exception {
        fileList.clear();
        for(int each = 0; each < data.length(); each++) {
            fileList.add(data.getJSONObject(each));
        }
        this.notifyDataSetChanged();
    }


}
class FileListHolder extends RecyclerView.ViewHolder {

    public TextView fileName, fileCode, fileExt, fileSize;
    public LinearLayoutCompat parent;

    public FileListHolder(View layout) {
        super(layout);
        parent = (LinearLayoutCompat) layout.findViewById(R.id.fileParent);
        fileName = (TextView) layout.findViewById(R.id.fileName);
        fileCode = (TextView) layout.findViewById(R.id.fileCode);
        fileExt = (TextView) layout.findViewById(R.id.fileExt);
        fileSize = (TextView) layout.findViewById(R.id.fileSize);
    }
}
