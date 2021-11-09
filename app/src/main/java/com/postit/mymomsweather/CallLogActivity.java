package com.postit.mymomsweather;

import android.Manifest;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CallLogActivity extends AppCompatActivity {


    Button btn;
    TextView textView;
    EditText editText;
    String[] date_arr;
    int[] seconds_arr;
    String[] x_arr;
    TextView showTV;
    String toDay;
    String beforeMonth;
    String before2Month;
    String toDay_m, beforeMonth_m, before2Month_m;

    private LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calllog);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CALL_LOG}, MODE_PRIVATE);

        btn = (Button)findViewById(R.id.btn);
        textView = (TextView)findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());
        editText = (EditText)findViewById(R.id.editText);
        showTV = (TextView)findViewById(R.id.showTV);

        arrayInitialization();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(getCallHistory());
                show();
                drawGraph();
            }
        });


    }

    void arrayInitialization(){

        date_arr = new String[9];
        seconds_arr = new int[9];
        x_arr = new String[9];

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-");
        SimpleDateFormat date2 = new SimpleDateFormat("MM");
        Date today = new Date();
        toDay = date.format(today);
        toDay_m = date2.format(today);

        Calendar mon = Calendar.getInstance();
        mon.add(Calendar.MONTH , -1);
        beforeMonth = new java.text.SimpleDateFormat("yyyy-MM-").format(mon.getTime());
        beforeMonth_m = new java.text.SimpleDateFormat("MM").format(mon.getTime());

        Calendar mon2 = Calendar.getInstance();
        mon2.add(Calendar.MONTH , -2);
        before2Month = new java.text.SimpleDateFormat("yyyy-MM-").format(mon2.getTime());
        before2Month_m = new java.text.SimpleDateFormat("MM").format(mon2.getTime());

        showTV.setText(toDay_m+", "+beforeMonth_m+", "+before2Month_m);

        date_arr[0] = before2Month+"21~말일";
        date_arr[1] = before2Month+"11~20";
        date_arr[2] = before2Month+"1~10";
        date_arr[3] = beforeMonth+"21~말일";
        date_arr[4] = beforeMonth+"11~20";
        date_arr[5] = beforeMonth+"1~10";
        date_arr[6] = toDay+"21~말일";
        date_arr[7] = toDay+"11~20";
        date_arr[8] = toDay+"1~10";


        x_arr[0] = before2Month_m+"월21~";
        x_arr[1] = before2Month_m+"월11~";
        x_arr[2] = before2Month_m+"월1~";
        x_arr[3] = beforeMonth_m+"월21~";
        x_arr[4] = beforeMonth_m+"월11~";
        x_arr[5] = beforeMonth_m+"월1~";
        x_arr[6] = toDay_m+"월21~";
        x_arr[7] = toDay_m+"월11~";
        x_arr[8] = toDay_m+"월1~";



    }

    public String getCallHistory(){
        String[] callSet = new String[]{
                CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.NUMBER, CallLog.Calls.DURATION};
        Cursor c = getContentResolver().query(CallLog.Calls.CONTENT_URI, callSet,
                null, null, null);
        if(c.getCount() == 0){
            return "통화기록 없음";
        }
        StringBuffer callBuff = new StringBuffer();
        callBuff.append("\n날짜 : 구분 : 전화번호 : 통화시간 \n\n");
        c.moveToFirst();

        String lookfor = editText.getText().toString();



        do{
            if(c.getString(2).equals(lookfor)){
                long callData = c.getLong(0);
                SimpleDateFormat datePattern = new SimpleDateFormat("yyyy-MM-dd");
                String date_str = datePattern.format(new Date(callData));
                callBuff.append(date_str + ":");
                if(c.getInt(1) == CallLog.Calls.INCOMING_TYPE)
                    callBuff.append("착신 :");
                else
                    callBuff.append("발신 :");
                //callBuff.append(c.getString(2) + ":");

                callBuff.append(c.getString(2) + ":");
                callBuff.append(c.getString(3) + "초\n");



                String str1_1 = before2Month+"0";
                String str1_2 = before2Month+"1";
                String str2_1 = beforeMonth+"0";
                String str2_2 = beforeMonth+"1";
                String str3_1 = toDay+"0";
                String str3_2 = toDay+"1";

                if(date_str.substring(0, 8).equals(before2Month)){
                    if(date_str.substring(0, 9).equals(str1_1)){
                        seconds_arr[2] += Integer.valueOf(c.getString(3));
                    }
                    else if(date_str.substring(0, 9).equals(str1_2)){
                        seconds_arr[1] += Integer.valueOf(c.getString(3));
                    }
                    else{
                        seconds_arr[0] += Integer.valueOf(c.getString(3));
                    }
                }

                else if(date_str.substring(0, 8).equals(beforeMonth)){
                    if(date_str.substring(0, 9).equals(str2_1)){
                        seconds_arr[5] += Integer.valueOf(c.getString(3));
                    }
                    else if(date_str.substring(0, 9).equals(str2_2)){
                        seconds_arr[4] += Integer.valueOf(c.getString(3));
                    }
                    else{
                        seconds_arr[3] += Integer.valueOf(c.getString(3));
                    }
                }

                else if(date_str.substring(0, 8).equals(toDay)){
                    if(date_str.substring(0, 9).equals(str3_1)){
                        seconds_arr[8] += Integer.valueOf(c.getString(3));
                    }
                    else if(date_str.substring(0, 9).equals(str3_2)){
                        seconds_arr[7] += Integer.valueOf(c.getString(3));
                    }
                    else{
                        seconds_arr[6] += Integer.valueOf(c.getString(3));
                    }
                }


            }

        }while(c.moveToNext());

        c.close();
        return callBuff.toString();
    }

    void show(){

        String show_str = "";
        for(int i = 0; i < date_arr.length; i++){
            show_str += date_arr[i]+" : " + seconds_arr[i]+"초, \t";
        }
        showTV.setText(show_str);

    }

    void drawGraph(){

        chart = findViewById(R.id.linechart);

        ArrayList<Entry> values = new ArrayList<>();


        for (int i = 0; i <= 8; i++) {

            values.add(new Entry(i, seconds_arr[i]));
        }

        LineDataSet set1;
        set1 = new LineDataSet(values, "DataSet 1");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets

        // create a data object with the data sets
        LineData data = new LineData(dataSets);

        // black lines and points
        set1.setColor(Color.YELLOW);
        set1.setCircleColor(Color.YELLOW);

        chart.setBackgroundColor(Color.WHITE); // 그래프 배경 색 설정
        set1.setColor(Color.BLACK); // 차트의 선 색 설정
        set1.setCircleColor(Color.BLACK); // 차트의 points 점 색 설정

        set1.setDrawFilled(true); // 차트 아래 fill(채우기) 설정
        set1.setFillColor(Color.BLACK); // 차트 아래 채우기 색 설정

        XAxis xAxis = chart.getXAxis();
        //Set the value formatter

        xAxis.setValueFormatter(new IndexAxisValueFormatter(x_arr));
        // set data
        chart.setData(data);



    }
}
