package com.example.pethealth.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.pethealth.*;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.circularreveal.CircularRevealFrameLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    
    public HomeFragment() {
        // Required empty public constructor
    }
    
    TextView city, txttemp, txt1, txt2, txt3;
    ImageView weatherImage;
    String nameIcon = "10d";
    String weather = "";
    ImageView calenderview;
    ImageView ib1, ib2, ib3;
    ConstraintLayout bgimg;
    
    String City = "";
    String Key = "4295500256eacb2f22f83bdb5e1c3e9a";
    //String url1 = "https://samples.openweathermap.org/data/2.5/weather?q=London&appid=4295500256eacb2f22f83bdb5e1c3e9a";



    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;

            URL url;

            HttpURLConnection httpURLConnection;

            InputStream inputStream;

            try {
                Log.i("LINK",strings[0]);
                url = new URL(strings[0]);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                inputStream = httpURLConnection.getInputStream();

                bitmap = BitmapFactory.decodeStream(inputStream);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // ?????? ????????? PERMISSIONS_REQUEST_CODE ??????, ????????? ????????? ???????????? ??????????????????

            boolean check_result = true;


            // ?????? ???????????? ??????????????? ???????????????.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {

                //?????? ?????? ????????? ??? ??????
                ;
            } else {
                // ????????? ???????????? ????????? ?????? ????????? ??? ?????? ????????? ??????????????? ?????? ???????????????.2 ?????? ????????? ????????????.

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(getContext(), "???????????? ?????????????????????. ?????? ?????? ???????????? ???????????? ??????????????????.", Toast.LENGTH_LONG).show();
                    getActivity().finish();


                } else {

                    Toast.makeText(getContext(), "???????????? ?????????????????????. ??????(??? ??????)?????? ???????????? ???????????? ?????????. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission() {

        //????????? ????????? ??????
        // 1. ?????? ???????????? ????????? ????????? ???????????????.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. ?????? ???????????? ????????? ?????????
            // ( ??????????????? 6.0 ?????? ????????? ????????? ???????????? ???????????? ????????? ?????? ????????? ?????? ???????????????.)


            // 3.  ?????? ?????? ????????? ??? ??????


        } else {  //2. ????????? ????????? ????????? ?????? ????????? ????????? ????????? ???????????????. 2?????? ??????(3-1, 4-1)??? ????????????.

            // 3-1. ???????????? ????????? ????????? ??? ?????? ?????? ????????????
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])) {

                // 3-2. ????????? ???????????? ?????? ?????????????????? ???????????? ????????? ????????? ???????????? ????????? ????????????.
                Toast.makeText(getActivity(), "??? ?????? ??????????????? ?????? ?????? ????????? ???????????????.", Toast.LENGTH_LONG).show();
                // 3-3. ??????????????? ????????? ????????? ?????????. ?????? ????????? onRequestPermissionResult?????? ???????????????.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. ???????????? ????????? ????????? ??? ?????? ?????? ???????????? ????????? ????????? ?????? ?????????.
                // ?????? ????????? onRequestPermissionResult?????? ???????????????.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }



    //??????????????? GPS ???????????? ?????? ????????????
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("?????? ????????? ????????????");
        builder.setMessage("?????? ???????????? ???????????? ?????? ???????????? ???????????????.\n"
                + "?????? ????????? ???????????????????");
        builder.setCancelable(true);
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //???????????? GPS ?????? ???????????? ??????
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS ????????? ?????????");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public class DownloadTask extends AsyncTask<String, Void , String> {
        @Override
        protected String doInBackground(String... strings) {

            String result = "";

            URL url;

            HttpURLConnection httpURLConnection;

            InputStream inputStream;

            InputStreamReader inputStreamReader;

            try {

                url = new URL(strings[0]);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                inputStream = httpURLConnection.getInputStream();

                inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while(data != -1) {

                    result += (char) data;

                    data = inputStreamReader.read();

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }
    }

    public void loading(View view) {



        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + City +"&units=metric&appid=" + Key;

        DownloadTask downloadTask = new DownloadTask();

        try {

            String result = "abc";

            result = downloadTask.execute(url).get();

            Log.i("Result:",result);

            JSONObject jsonObject = new JSONObject(result);

            JSONObject main = jsonObject.getJSONObject("main");


            String temp = main.getString("temp");



            String humidity = main.getString("humidity");

            String feel_like = main.getString("feels_like");

            String visibility = jsonObject.getString("visibility");

            nameIcon = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");

            weather = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");

            // Log.i("Name Icon",nameIcon);

            Long time = jsonObject.getLong("dt");

            String sTime = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.ENGLISH)
                    .format(new Date(time * 1000));

            city.setText(City);

            txttemp.setText(temp + "??");

            android.util.Log.i("??????",weather);

            //DownloadImage downloadImage = new DownloadImage();

            //String urlIcon = " https://openweathermap.org/img/wn/"+ nameIcon +"@2x.png";

            //Bitmap bitmap = downloadImage.execute(urlIcon).get();

            //weatherImage.setImageBitmap(bitmap);


            if (weather.equals("clear sky")) {
                if (nameIcon.equals("01d")) {
                    weatherImage.setImageResource(R.drawable.w01d);
                    bgimg.setBackgroundResource(R.drawable.wd01);
                } else if(nameIcon.equals("01n")) {
                    DownloadImage downloadImage = new DownloadImage();

                    String urlIcon = " https://openweathermap.org/img/wn/"+ nameIcon +"@2x.png";

                    Bitmap bitmap = downloadImage.execute(urlIcon).get();

                    weatherImage.setImageBitmap(bitmap);
                    bgimg.setBackgroundResource(R.drawable.wdn01);
                    city.setTextColor(Color.parseColor("#ffffff"));
                    txt1.setTextColor(Color.parseColor("#ffffff"));
                    txt2.setTextColor(Color.parseColor("#ffffff"));
                    txt3.setTextColor(Color.parseColor("#ffffff"));
                }

            }else if (weather.equals("few clouds")) {
                if (nameIcon.equals("02d")){
                    bgimg.setBackgroundResource(R.drawable.wether);
                    weatherImage.setImageResource(R.drawable.w02d);
                } else if (nameIcon.equals("02n")) {
                    DownloadImage downloadImage = new DownloadImage();

                    String urlIcon = " https://openweathermap.org/img/wn/"+ nameIcon +"@2x.png";

                    Bitmap bitmap = downloadImage.execute(urlIcon).get();

                    weatherImage.setImageBitmap(bitmap);
                    bgimg.setBackgroundResource(R.drawable.wetern1);
                    city.setTextColor(Color.parseColor("#ffffff"));
                    txt1.setTextColor(Color.parseColor("#ffffff"));
                    txt2.setTextColor(Color.parseColor("#ffffff"));
                    txt3.setTextColor(Color.parseColor("#ffffff"));

                }

            }else if (weather.equals("scattered clouds") || weather.equals("overcast clouds")) {
                if (nameIcon.equals("03d")) {
                    bgimg.setBackgroundResource(R.drawable.cd01);
                    weatherImage.setImageResource(R.drawable.w03d);
                } else if (nameIcon.equals("03n") || nameIcon.equals("04n")) {
                    DownloadImage downloadImage = new DownloadImage();

                    String urlIcon = " https://openweathermap.org/img/wn/"+ nameIcon +"@2x.png";

                    Bitmap bitmap = downloadImage.execute(urlIcon).get();

                    weatherImage.setImageBitmap(bitmap);
                    bgimg.setBackgroundResource(R.drawable.sdn01);
                    city.setTextColor(Color.parseColor("#ffffff"));
                    txt1.setTextColor(Color.parseColor("#ffffff"));
                    txt2.setTextColor(Color.parseColor("#ffffff"));
                    txt3.setTextColor(Color.parseColor("#ffffff"));
                }

            }else if (weather.equals("broken clouds")) {
                weatherImage.setImageResource(R.drawable.w04d);
                bgimg.setBackgroundResource(R.drawable.brokencloud);
                city.setTextColor(Color.parseColor("#ffffff"));
                txt1.setTextColor(Color.parseColor("#ffffff"));
                txt2.setTextColor(Color.parseColor("#ffffff"));
                txt3.setTextColor(Color.parseColor("#ffffff"));
            }else if (weather.equals("mist")) {
                DownloadImage downloadImage = new DownloadImage();

                String urlIcon = " https://openweathermap.org/img/wn/"+ nameIcon +"@2x.png";

                Bitmap bitmap = downloadImage.execute(urlIcon).get();

                weatherImage.setImageBitmap(bitmap);
                bgimg.setBackgroundResource(R.drawable.fog);
                city.setTextColor(Color.parseColor("#ffffff"));
                txt1.setTextColor(Color.parseColor("#ffffff"));
                txt2.setTextColor(Color.parseColor("#ffffff"));
                txt3.setTextColor(Color.parseColor("#ffffff"));
            }else if (weather.equals("shower rain") || weather.equals("light rain")) {
                bgimg.setBackgroundResource(R.drawable.showerrain);
                weatherImage.setImageResource(R.drawable.w09d);
                city.setTextColor(Color.parseColor("#ffffff"));
                txt1.setTextColor(Color.parseColor("#ffffff"));
                txt2.setTextColor(Color.parseColor("#ffffff"));
                txt3.setTextColor(Color.parseColor("#ffffff"));
            }else if (weather.equals("rain") || weather.equals("moderate rain")) {
                bgimg.setBackgroundResource(R.drawable.rain);
                weatherImage.setImageResource(R.drawable.w10d);
                city.setTextColor(Color.parseColor("#ffffff"));
                txt1.setTextColor(Color.parseColor("#ffffff"));
                txt2.setTextColor(Color.parseColor("#ffffff"));
                txt3.setTextColor(Color.parseColor("#ffffff"));
            }else if (weather.equals("thunderstorm")) {
                bgimg.setBackgroundResource(R.drawable.td01);
                weatherImage.setImageResource(R.drawable.w11d);
                city.setTextColor(Color.parseColor("#ffffff"));
                txt1.setTextColor(Color.parseColor("#ffffff"));
                txt2.setTextColor(Color.parseColor("#ffffff"));
                txt3.setTextColor(Color.parseColor("#ffffff"));
            }else if (weather.equals("snow")) {
                bgimg.setBackgroundResource(R.drawable.snow);
                weatherImage.setImageResource(R.drawable.w13d);
            }else {
                DownloadImage downloadImage = new DownloadImage();

                String urlIcon = " https://openweathermap.org/img/wn/"+ nameIcon +"@2x.png";

                Bitmap bitmap = downloadImage.execute(urlIcon).get();

                weatherImage.setImageBitmap(bitmap);
            }



        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //users = view.findViewById(R.id.users);
        //addusers = view.findViewById(R.id.addusers);

        calenderview = view.findViewById(R.id.calendarView);
        ib1 = view.findViewById(R.id.ib1);
        ib1.setClipToOutline(true);
        ib2 = view.findViewById(R.id.ib2);
        ib2.setClipToOutline(true);
        ib3 = view.findViewById(R.id.ib3);
        ib3.setClipToOutline(true);
        bgimg = view.findViewById(R.id.text1);
        txt1 = view.findViewById(R.id.txt1);
        txt2 = view.findViewById(R.id.txt2);
        txt3 = view.findViewById(R.id.txt3);


        if (checkLocationServicesStatus()) {
            checkRunTimePermission();
        } else {
            showDialogForLocationServiceSetting();
        }

        GpsTracker gpsTracker = new GpsTracker(getContext());
        final Geocoder geocoder = new Geocoder(getContext(), Locale.CANADA);

        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();


        List<Address> address=null;


        try {
            address = geocoder.getFromLocation(latitude,longitude,1);
            Log.d("?????? ??????",address.get(0).getLocality());
            City = address.get(0).getLocality().toString();
            HomeFragment fragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title",address.get(0).getLocality().toString());

            fragment.setArguments(bundle);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("test","???????????????");
        } catch ( NullPointerException e) {
            try {
                address = geocoder.getFromLocation(latitude,longitude,1);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Log.d("?????? ??????",address.get(0).getSubLocality());
            City = address.get(0).getSubLocality();
            HomeFragment fragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title",address.get(0).getSubLocality());

            fragment.setArguments(bundle);
        }

        calenderview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Calender.class);
                startActivity(intent);
            }
        });

        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent urlintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://dogpre.com/?gclid=CjwKCAjwy7CKBhBMEiwA0Eb7an_YgKrD3_jTl-PAmTDKbjYy8gdumPy5Yu902gyoR-kRIF36z4s4zRoCWrAQAvD_BwE"));
                startActivity(urlintent);
            }
        });

        ib2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent urlintent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.pet-friends.co.kr/main/tab/2"));
                startActivity(urlintent2);
            }
        });

        ib3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent urlintent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://queenpuppy.co.kr/"));
                startActivity(urlintent3);
            }
        });


        city = view.findViewById(R.id.id_city);
        txttemp = view.findViewById(R.id.id_degree);
        weatherImage = view.findViewById(R.id.id_weatherImage);

        loading(getView());

        return view;
    }
}