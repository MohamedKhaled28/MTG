package Model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.hp.mody_task.R;
import com.example.hp.mody_task.ResultActivity;

import java.util.List;

/**
 * Created by HP on 5/2/2017.
 */

public class Adapter3 extends ArrayAdapter {

    Context context;
    int resource;
    List<artifact_search> listartifacts;

    public Adapter3(Context context, int resource, List<artifact_search> artifacts) {
        super(context, resource, artifacts);
        this.context = context;
        this.resource = resource;
        this.listartifacts = artifacts;
    }

    public View getView(final int position , View convertView, ViewGroup parent){

        final LayoutInflater inflater =(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item3, parent, false);



        ImageView image = (ImageView) view.findViewById(R.id.img);
       // TextView Name = (TextView) view.findViewById(R.id.name_artifact);
       // TextView Name_Museum = (TextView) view.findViewById(R.id.name_museum);
        //TextView Description = (TextView) view.findViewById(R.id.description_name);



        String img = listartifacts.get(position).getImage();
        ResultActivity v = new ResultActivity();
      //  v.setTitle(listartifacts.get(position).getName());
        Bitmap image_url = v.getBitmapfromurl(img);
        Drawable imggg = new BitmapDrawable(getContext().getResources(),image_url);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            image.setBackground(imggg);
        }
        //Name_Museum.setText(listartifacts.get(position).getMuesum_name());
       // Description.setText(listartifacts.get(position).getDescription());

        return view;

    }
}
