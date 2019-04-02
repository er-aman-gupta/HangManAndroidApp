package com.example.amanpc.hangman;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    float pixels;
    static String a="TOY";
    static boolean[] checkArray ;
    static TextView textView,text1;
    static boolean completeFlag;
    static int[] charset;
    static RecycleViewAdapter recycleViewAdapter;
    static List<String> list = new ArrayList<>();
    static RecyclerView recyclerView;
    static List<String> wordList ;
    static int guess,hint;
    static  int totalGuess=5,hintLeft=2;
    static ImageButton imageButton;
    static ImageView imageView;
    Data data;
    List<String> listOfWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pixels=getApplicationContext().getResources().getDisplayMetrics().widthPixels;

        Log.d("screen size is ",String.valueOf(pixels));

        textView=findViewById(R.id.guessLeftText);
        text1=findViewById(R.id.hintLeft);
        text1.setText("Hints Left :"+String.valueOf(hintLeft-hint));
        textView.setText("Guess Left :"+String.valueOf(totalGuess-guess));
        imageButton=findViewById(R.id.hintId);
        data=new Data();
        listOfWord=data.getWordList();
        Random random=new Random();
        int p=random.nextInt(listOfWord.size());
        a =listOfWord.get(p);
        Log.d("Wordis ",a);
        guess=0;
        wordList = new ArrayList<>();
        checkArray = new boolean[a.length()];
        imageView=findViewById(R.id.hangmanImage);
        charset=characterSet(a);
        for(int i=0;i<charset.length;i++){
            Log.d("index"+String.valueOf(i),String.valueOf(charset[i]));
        }

        for (int i = 0; i < a.length(); i++) {
            char temp = a.charAt(i);
            if (temp == ' ') {
                wordList.add("/");
                checkArray[i] = true;
            } else wordList.add("_");
        }

        final GridView gridView = findViewById(R.id.gridView);


        list=data.alphabetListReturn();

        recyclerView = findViewById(R.id.wordId);
        recycleViewAdapter = new RecycleViewAdapter(MainActivity.this, wordList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayout.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycleViewAdapter);

        final GridViewAdapter gridViewAdapter = new GridViewAdapter(MainActivity.this, list);
        gridView.setAdapter(gridViewAdapter);

        /*gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView=view.findViewById((int)parent.getItemIdAtPosition(position));
                textView.setText(" ");
            }
        });*/

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hintFetcher()) {
                    recycleViewAdapter.notifyDataSetChanged();
                    text1.setText("Hints Left :"+String.valueOf(hintLeft));
                    gridViewAdapter.notifyDataSetChanged();
                    boolean done=true;
                    for(int i=0;i<checkArray.length;i++){
                        if(checkArray[i]==false)
                            done=false;
                    }
                    if(done==true){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent1 =  new Intent(MainActivity.this,LostActivity.class);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent1.putExtra("Won",a);
                                MainActivity.this.startActivity(intent1);
                                finish();
                            }
                        },1500);

                    }
                }
                if(hintLeft==0){
                    imageButton.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recycleViewAdapter=null;
        recyclerView=null;
        wordList.clear();
        guess=0;
        checkArray=null;
        charset=null;
        hintLeft=2;
        list.clear();

    }

    boolean hintFetcher(){
        if(hintLeft>0){
            hintLeft--;
            Random random=new Random();
            int p=random.nextInt(checkArray.length);
            if(checkArray[p]==false){
                checkArray[p]=true;
                wordList.set(p, String.valueOf(a.charAt(p)));
                int position=list.indexOf(String.valueOf(a.charAt(p)));
                charset[position]--;
                if(charset[position]==0)
                list.set(position," ");
            }
            else {
                while (checkArray[p]!=false){
                    p=random.nextInt(checkArray.length);

                }checkArray[p]=true;
                wordList.set(p, String.valueOf(a.charAt(p)));
                int position=list.indexOf(String.valueOf(a.charAt(p)));
                charset[position]--;
                if(charset[position]==0)
                list.set(position," ");
            }
            return true;
        }

        return false;
    }

    int wordChecker(String character, Activity activity){
        char temp=character.charAt(0);
        int index=(int)Character.toUpperCase(character.charAt(0))-65;
        if(charset[index]>0){

            int position=a.indexOf(temp);
            int cposition=0;
            while (checkArray[position]!=false){
                cposition=position;
                position=a.indexOf(temp,cposition+1);
                if(cposition==position){
                    break;
                }
            }
            checkArray[position]=true;
            charset[index]--;
            listUpdater(position);
            recycleViewAdapter.notifyDataSetChanged();
            for(int i=0;i<checkArray.length;i++){
                if(checkArray[i]==true){
                    completeFlag=true;
                }
                else {completeFlag=false;break;}
            }
            if(completeFlag){
                Intent intent1 =  new Intent(activity,LostActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent1.putExtra("Won",a);
                activity.startActivity(intent1);
                finish();
            }
            if(charset[index]>0){
                return 1;

            }
            else return 0;
        }
        else {
            guess++;
            guessUpdate();
            if(guess<5)
                imageView.setImageResource(imageReturner(guess));
            else {
                Intent intent1 =  new Intent(activity,LostActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent1.putExtra("Word",a);
                activity.startActivity(intent1);
                guess=0;
                //startActivity(intent1);
                return -1;
            }
        }

        return -1;
    }

    void guessUpdate(){
        textView.setText("Guess Left :"+String.valueOf(totalGuess-guess));
    }

    void listUpdater(int position){
        wordList.set(position, String.valueOf(a.charAt(position)));
        Log.d(String.valueOf(position),wordList.get(position));
    }
    int[] characterSet(String word){
       int[] charSet=new int[26];
       for(int i=0;i<word.length();i++){
           if(word.charAt(i)==' '){
               continue;
           }
           else {
               char temp=Character.toUpperCase(word.charAt(i));
               charSet[(int)temp-65]++;
           }
       }
       return charSet;
    }

    int imageReturner(int guess){
        switch (guess){
            case 1:
                return R.mipmap.guess1;
            case 2:
                return R.mipmap.guess3;
            case 3:
                return R.mipmap.guess4;
            case 4:
                return R.mipmap.guess5;
            case 5:
                return R.mipmap.correctanswer;
        }
        return R.mipmap.guess1;
    }
}