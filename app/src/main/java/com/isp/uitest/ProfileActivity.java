package com.isp.uitest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.isp.uitest.data.LoginDataSource;
import com.isp.uitest.ui.login.LoginInfo;

import static com.isp.uitest.ui.login.ButtonEffect.buttonEffect;

public class ProfileActivity extends Fragment {

    LoginDataSource repository;
    LinearLayout logout;
    LoginInfo info;
    TextView userName;
    TextView userID;
    View view;
    Image profile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile, null);
        logout = view.findViewById(R.id.logout_button);
        buttonEffect(logout);

        info = (LoginInfo)getActivity().getApplication();
        userName = view.findViewById(R.id.username);
        userID = view.findViewById(R.id.userID);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clk");
                getActivity().finish();
              /*  Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                ((Activity) getActivity()).overridePendingTransition(0, 0);*/
            }
        });


        // mask profile image into circle
        ImageView imageView = view.findViewById(R.id.profile_picture);
        Bitmap photo = BitmapFactory.decodeResource(getResources(), R.drawable.dog);
        Bitmap mask = BitmapFactory.decodeResource(getResources(), R.drawable.mask_alt);
        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
        photo = Bitmap.createScaledBitmap(photo, mask.getWidth(),mask.getHeight(), true);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(photo, 0, 0, null);
        canvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);
        imageView.setImageBitmap(result);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        if (info.isLogin()){
            userID.setText(info.getLoginUser().getId());
            userName.setText(info.getLoginUser().getName());
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //for data
        super.onCreate(savedInstanceState);
/*
        repository = new LoginDataSource();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repository.logout();
            }
        });*/
    }
}

