package com.postit.mymomsweather;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.postit.mymomsweather.Model.AudioRecord;
import com.postit.mymomsweather.Model.EmotionRecord;
import com.postit.mymomsweather.Model.ParentUser;
import com.postit.mymomsweather.databinding.ActivityParentStatusBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class ParentStatusActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    ActivityParentStatusBinding binding;
    PieChart pieChart;
    int[] colorClassArray = new int []{Color.RED, Color.BLUE, Color.GRAY, Color.MAGENTA};
    int[] emotion_arr;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParentStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pieChart = findViewById(R.id.piechart);
        emotion_arr = new int[4];

        //인텐트에서 부모 아이디 가져오기
        String parentID = getIntent().getExtras().getString("parentID");

        //부모 아이디 문서 하위의 감정기록 콜렉션 가져오기
        db.collection("users").document(parentID)
                .collection("emotionRecord").orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener((task)->{
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot doc :
                                task.getResult()) {
                            EmotionRecord temp = doc.toObject(EmotionRecord.class);
                            Log.d("TAG_1", String.valueOf(temp.getEmotion()));
                            int a = temp.getEmotion();
                            emotion_arr[a]++;

//                            long a = (long)doc.get("emotion");
//                            Log.d("TAG", String.valueOf(a));
//                            System.out.println(a);
//                            emotion_arr[(int)a]++;
                        }
                    }
                    Log.d("TAG_arr", String.valueOf(emotion_arr[0]+ emotion_arr[1]+ emotion_arr[2]+ emotion_arr[3]));

                    showTextView();

                    PieDataSet pieDataSet = new PieDataSet(dataValues1(), "Hello");
                    pieDataSet.setColors(colorClassArray);
                    PieData pieData = new PieData(pieDataSet);
                    pieData.setValueTextSize(12);
                    pieData.setValueTextColor(Color.WHITE);

                    pieChart.setDrawEntryLabels(true);
                    pieChart.setUsePercentValues(false);
                    pieChart.setScrollBarSize(12);
                    pieChart.setCenterText("우리 엄마 날씨");
                    pieChart.setCenterTextSize(20);
                    pieChart.setEntryLabelTextSize(12);
                    //pieChart.setCenterTextRadiusPercent(50);
                    pieChart.setData(pieData);
                    pieChart.invalidate();



                });



    }

    private ArrayList<PieEntry> dataValues1(){
        ArrayList<PieEntry> dataVals = new ArrayList<>();

        dataVals.add(new PieEntry(emotion_arr[0], "0번"));
        dataVals.add(new PieEntry(emotion_arr[1], "1번"));
        dataVals.add(new PieEntry(emotion_arr[2], "2번"));
        dataVals.add(new PieEntry(emotion_arr[3], "3번"));
        return dataVals;
    }

    void showTextView(){
        TextView tv = findViewById(R.id.weather_view);
        tv.setText(emotion_arr[0]+", "+emotion_arr[1]+", "+emotion_arr[2]+", "+emotion_arr[3]);

        //binding.getRoot().addView(tv);

    }


}
