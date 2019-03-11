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

import com.example.hp.mody_task.PlacedetailsActivity;
import com.example.hp.mody_task.R;

import java.util.List;

/**
 * Created by HP on 4/20/2017.
 */

public class Adapter4 extends ArrayAdapter {

    List<ArtifactDescription> desc;
    Context context;
    int r ;
    public Adapter4(Context context, int resource, List<ArtifactDescription> artifacts) {
        super(context, resource, artifacts);
        this.desc= artifacts;
        this.context = context;
        r = resource;
    }

    public View getView(final int position , View convertView, ViewGroup parent){
        final LayoutInflater inflater =(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


        View view = inflater.inflate(R.layout.list_item4, parent, false);
       // View view1 = inflater.inflate(R.layout.activity_artifactdesc,parent,false);
      //  TextView Brief_description = (TextView) view1.findViewById(R.id.txtviw11);
        String bre_desc = desc.get(position).getBrief_description();
        ImageView iv = (ImageView) view.findViewById(R.id.imageView2);
        PlacedetailsActivity p = new PlacedetailsActivity();
        Bitmap image_url = p.getbitmapforimage(desc.get(position).getImage());
        Drawable imgd = new BitmapDrawable(getContext().getResources(),image_url);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            iv.setBackground(imgd);
        }
        return view;

    }
}
