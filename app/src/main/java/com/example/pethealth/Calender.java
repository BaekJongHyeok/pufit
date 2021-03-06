package com.example.pethealth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pethealth.decorators.EventDecorator;
import com.example.pethealth.decorators.OneDayDecorator;
import com.example.pethealth.decorators.SaturdayDecorator;
import com.example.pethealth.decorators.SundayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class Calender extends AppCompatActivity {
    public String fname=null;
    public String str=null;
    public Button cha_Btn,del_Btn,save_Btn;
    public TextView diaryTextView,textView2;
    public EditText contextEditText;
    public CalendarView calendarView;

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    MaterialCalendarView materialCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        //calendarView=findViewById(R.id.calendarView);
        diaryTextView=findViewById(R.id.diaryTextView);
        save_Btn=findViewById(R.id.save_Btn);
        del_Btn=findViewById(R.id.del_Btn);
        cha_Btn=findViewById(R.id.cha_Btn);
        textView2=findViewById(R.id.textView2);
        contextEditText=findViewById(R.id.contextEditText);


        //????????? ??? ???????????? ?????????????????? ????????? ?????????
        Intent intent=getIntent();
        String name=intent.getStringExtra("userName");
        final String userID=intent.getStringExtra("userID");



        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // ????????? ??????
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // ????????? ???
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator);

        String[] result = {"2021,06,18", "2021,07,18", "2021,08,18", "2021,09,18"};

        new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();


                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                diaryTextView.setText(String.format("%d / %d / %d",Year,Month+1,Day));
                contextEditText.setText("");





                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");

                String shot_Day = String.valueOf(Year) + " / " + String.valueOf(Month) + " / " + String.valueOf(Day);


                Log.i("shot_Day test", shot_Day + "");
                materialCalendarView.clearSelection();
                diaryTextView.setText(shot_Day);

                checkDay(Year,Month,Day,userID);



            }
        });

        save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDiary(fname);
                str=contextEditText.getText().toString();
                textView2.setText(str);
                save_Btn.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "?????????????????????.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void  checkDay(int cYear,int cMonth,int cDay,String userID){
        fname=""+userID+cYear+"-"+(cMonth+1)+""+"-"+cDay+".txt";//????????? ?????? ????????????
        FileInputStream fis=null;//FileStream fis ??????

        try{
            fis=openFileInput(fname);

            byte[] fileData=new byte[fis.available()];
            fis.read(fileData);
            fis.close();

            str=new String(fileData);

            contextEditText.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.VISIBLE);
            textView2.setText(str);

            save_Btn.setVisibility(View.INVISIBLE);
            cha_Btn.setVisibility(View.VISIBLE);
            del_Btn.setVisibility(View.VISIBLE);

            cha_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    contextEditText.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText(str);

                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    textView2.setText(contextEditText.getText());
                }

            });
            del_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText("");
                    contextEditText.setVisibility(View.VISIBLE);
                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    removeDiary(fname);
                    Toast.makeText(getApplicationContext(), "?????????????????????.", Toast.LENGTH_SHORT).show();
                }
            });
            if(textView2.getText()==null){
                textView2.setVisibility(View.INVISIBLE);
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @SuppressLint("WrongConstant")
    public void removeDiary(String readDay){
        FileOutputStream fos=null;

        try{
            fos=openFileOutput(readDay,MODE_NO_LOCALIZED_COLLATORS);
            String content="";
            fos.write((content).getBytes());
            fos.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @SuppressLint("WrongConstant")
    public void saveDiary(String readDay){
        FileOutputStream fos=null;

        try{
            fos=openFileOutput(readDay,MODE_NO_LOCALIZED_COLLATORS);
            String content=contextEditText.getText().toString();
            fos.write((content).getBytes());
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] Time_Result;

        ApiSimulator(String[] Time_Result){
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            /*???????????? ????????? ?????????????????????*/
            /*?????? 0??? 1??? ???,?????? ?????????*/
            //string ???????????? Time_Result ??? ???????????? ,??? ????????????????????? string??? int ??? ??????
            for(int i = 0 ; i < Time_Result.length ; i ++){
                //CalendarDay day = CalendarDay.from(calendar);
                String[] time = Time_Result[i].split(",");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                //dates.add(day);
                //calendar.set(year,month-1,dayy);
                calendar.set(year, month - 1, dayy);
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);
            }



            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            materialCalendarView.addDecorator(new EventDecorator(Color.GREEN, calendarDays, Calender.this));
        }
    }

}

/*
    public String fname=null;
    public String str=null;
    public Button cha_Btn,del_Btn,save_Btn;
    public TextView diaryTextView;
    public EditText contextEditText, contextEditText2;
    public String savingdate;
    private String str_del;
    private String postId;


    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();	// ????????? ????????? ?????????
    private List<String> uidList = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<CalendarList> arrayList;

    private View view;

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    MaterialCalendarView materialCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);


        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        // ????????????
        diaryTextView=findViewById(R.id.diaryTextView);
        save_Btn=findViewById(R.id.save_Btn);
        del_Btn=findViewById(R.id.del_Btn);
        cha_Btn=findViewById(R.id.cha_Btn);
        contextEditText=findViewById(R.id.contextEditText);
        contextEditText2=findViewById(R.id.contextEditText2);
        recyclerView = findViewById(R.id.textView2);


        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();



        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // ????????? ??????
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // ????????? ???
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator);

        String[] result = {"2021,06,18", "2021,07,18", "2021,08,18", "2021,09,18"};

        new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();


                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);
                contextEditText2.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);
                contextEditText.setText("");
                recyclerView.setVisibility(View.VISIBLE);





                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");

                String shot_Day = String.valueOf(Year) + " / " + String.valueOf(Month) + " / " + String.valueOf(Day);

                savingdate = shot_Day;

                Log.i("shot_Day test", shot_Day + "");
                materialCalendarView.clearSelection();
                diaryTextView.setText(shot_Day);



            }
        });








        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String the_uid = user.getUid();

        databaseReference = database.getReference(the_uid).child("Calendar");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // ?????????????????? ????????????????????? ???????????? ???????????? ???
                arrayList.clear(); // ?????? ?????????????????? ?????????????????? ?????????
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // ??????????????? ????????? List??? ????????????
                    CalendarList calendarList = snapshot.getValue(CalendarList.class); // ??????????????? User ????????? ???????????? ?????????.
                    arrayList.add(calendarList); // ?????? ??????????????? ?????????????????? ?????? ????????????????????? ?????? ??????
                }
                adapter.notifyDataSetChanged(); // ????????? ?????? ??? ?????????????????? ????????? ???
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // ????????? ??????????????? ?????? ?????? ???
                Log.e("Calender", String.valueOf(databaseError.toException())); // ????????? ??????
            }
        });
        adapter = new CalendarAdpater(arrayList, this);
        recyclerView.setAdapter(adapter); // ????????????????????? ????????? ??????















        save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                save_Btn.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);
                contextEditText2.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                addCalendarinfo(contextEditText.getText().toString(), contextEditText2.getText().toString());
                Toast.makeText(getApplicationContext(), "?????????????????????.", Toast.LENGTH_SHORT).show();
            }
            //
            //????????? ??????
            private void addCalendarinfo(String schedule, String time){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                DatabaseReference dataRef = databaseReference.push();
                postId = dataRef.getKey();

                CalendarList calendarlist = new CalendarList();
                calendarlist.setSchedule(schedule);
                calendarlist.setTime(time);
                calendarlist.setDate(savingdate);
                calendarlist.setId(databaseReference.child(postId).getKey());
                //databaseReference.push().setValue(calendarlist);



                dataRef.setValue(calendarlist);
            }


        });

        cha_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
                contextEditText2.setVisibility(View.VISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
            }
        });

        del_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "?????????????????????.", Toast.LENGTH_SHORT).show();
            }
        });

    }




    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] Time_Result;

        ApiSimulator(String[] Time_Result){
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            /*???????????? ????????? ?????????????????????*/
            /*?????? 0??? 1??? ???,?????? ?????????*/
            //string ???????????? Time_Result ??? ???????????? ,??? ????????????????????? string??? int ??? ??????
 /*           for(int i = 0 ; i < Time_Result.length ; i ++){
                //CalendarDay day = CalendarDay.from(calendar);
                String[] time = Time_Result[i].split(",");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                //dates.add(day);
                //calendar.set(year,month-1,dayy);
                calendar.set(year, month - 1, dayy);
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);
            }



            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            materialCalendarView.addDecorator(new EventDecorator(Color.GREEN, calendarDays, Calender.this));
        }
    }
}

*/
        // ???????????? ????????? ??????


    /*private void checkDay(int year, int month, int day) {
        fname = String.valueOf(year) + String.valueOf(month) + String.valueOf(day);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String the_uid = user.getUid();
        databaseReference = database.getReference(the_uid).child("Calendar").child(fname);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                CC1 cc1 = dataSnapshot.getValue(CC1.class);
                arrayList.add(cc1);
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
                Log.e("Calender", String.valueOf(databaseError.toException()));
            }
        });
        adapter = new CalendarAdpater(arrayList, this);
        recyclerView.setAdapter(adapter);
        Toast.makeText(getApplicationContext(), "g" + databaseReference, Toast.LENGTH_SHORT).show();

    }*/

