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

import com.example.hp.mody_task.PlaceActivity;
import com.example.hp.mody_task.R;

import java.util.List;

/**
 * Created by HP on 4/9/2017.
 */

public class Adapter2 extends ArrayAdapter {
    List<Places> place;
    Context context;
    int r ;
    public Adapter2(Context context, int resource, List<Places> artifacts) {
        super(context, resource, artifacts);
        this.place= artifacts;
        this.context = context;
        r = resource;
    }

    public View getView(final int position , View convertView, ViewGroup parent){
        final LayoutInflater inflater =(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.list_item2, parent, false);
        TextView Name = (TextView) view.findViewById(R.id.txtViw2);
        TextView mark = (TextView) view.findViewById(R.id.txtViw3);
        ImageView imageView = (ImageView) view.findViewById(R.id.imgplace);
        PlaceActivity pv = new PlaceActivity();
        String img = place.get(position).getImage();
        Bitmap bimg = pv.getBitmapfromurl(img);
        Drawable dimage = new BitmapDrawable(getContext().getResources(),bimg);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageView.setBackground(dimage);
        }


        Name.setText(place.get(position).getName());

        mark.setText(String.valueOf(place.get(position).getMark()));
        return view;

    }
}
