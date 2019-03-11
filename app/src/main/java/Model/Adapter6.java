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
import android.widget.TextView;

import com.example.hp.mody_task.Allartifacts;
import com.example.hp.mody_task.R;

import java.util.List;

/**
 * Created by HP on 6/24/2017.
 */

public class Adapter6 extends ArrayAdapter {
    List<AllArtifactsclass> details;
    Context context;
    int r;
    public Adapter6(Context context, int resource, List<AllArtifactsclass> details) {
        super(context, resource, details);
        this.details = details;
        this.context = context;
        r = resource;
    }
    public View getView(final int position , View convertView, ViewGroup parent){
        final LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.list_item6, parent, false);
        TextView name = (TextView) view.findViewById(R.id.txtname);
        ImageView image = (ImageView) view.findViewById(R.id.imgart);
        name.setText(details.get(position).getName());
        String img = details.get(position).getImage();
        Allartifacts allartifacts = new Allartifacts();
        Bitmap imag = allartifacts.getBitmapfromurl(img);
        Drawable dimag = new BitmapDrawable(getContext().getResources(),imag);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            image.setBackground(dimag);
        }


        return view;
    }
}
