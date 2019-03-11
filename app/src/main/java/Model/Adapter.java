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

import com.example.hp.mody_task.R;
import com.example.hp.mody_task.UserActivity;

import java.util.List;

/**
 * Created by HP on 4/9/2017.
 */

public class Adapter extends ArrayAdapter {
    List<TourismTypes> types;
    Context context;
    int r ;
    public Adapter(Context context, int resource, List<TourismTypes> artifacts) {
        super(context, resource, artifacts);
        this.types= artifacts;
        this.context = context;
        r = resource;
    }

    public View getView(final int position , View convertView, ViewGroup parent){
        final LayoutInflater inflater =(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


        View view = inflater.inflate(R.layout.list_item, parent, false);
        ImageView img = (ImageView) view.findViewById(R.id.imgtourist);


        TextView Name = (TextView) view.findViewById(R.id.txtviw);
        Name.setText(types.get(position).getName());
        String image = types.get(position).getImage();
        UserActivity resultActivity = new UserActivity();
        Bitmap mge = resultActivity.getBitmapfromurl(image);
        Drawable imagee = new BitmapDrawable(getContext().getResources(),mge);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            img.setBackground(imagee);
        }
        return view;

    }
}

