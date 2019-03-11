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
import com.example.hp.mody_task.ViewScanningDetailsActivity;

import java.util.List;

/**
 * Created by HP on 5/3/2017.
 */

public class Adapter5 extends ArrayAdapter {
    List<ScanningDetails> details;
    Context context;
    int r;
    public Adapter5(Context context, int resource, List<ScanningDetails> details) {
        super(context, resource, details);
        this.details = details;
        this.context = context;
        r = resource;
    }
    public View getView(final int position , View convertView, ViewGroup parent){
        final LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.list_item5, parent, false);


        ImageView image = (ImageView) view.findViewById(R.id.image);





        String img = details.get(position).getImage();
        ViewScanningDetailsActivity v = new ViewScanningDetailsActivity();
        Bitmap image_url = v.getBitmapfromurl(img);
        Drawable imgg = new BitmapDrawable(getContext().getResources(),image_url);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            image.setBackground(imgg);
        }
        // image.setImageBitmap(image_url);


        return view;
    }
}
