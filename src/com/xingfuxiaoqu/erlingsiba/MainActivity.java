package com.xingfuxiaoqu.erlingsiba;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobads.appoffers.OffersManager;

public class MainActivity extends Activity implements OnTouchListener, OnGestureListener {
    private LinearLayout mygl;
    private LinearLayout pointsll;
    private Button restgame, model1, model2, model3, model4, getpoints;
    private TextView currencyname, mypoints;
    private GestureDetector mGestureDetector;
    private static final int FLING_MIN_DISTANCE = 100;
    private static final int FLING_MIN_VELOCITY = 50;
    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int RIGHT = 3;
    private ArrayList<TextView> textViewList = new ArrayList<TextView>();
    private ArrayList<Integer> allNumList = new ArrayList<Integer>();
    private TextView tvscore, tvhisscore;
    private int score = 0;
    private String model = "";
    private int[] tvs = { R.id.tv0, R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4, R.id.tv5, R.id.tv6, R.id.tv7, R.id.tv8,
            R.id.tv9, R.id.tv10, R.id.tv11, R.id.tv12, R.id.tv13, R.id.tv14, R.id.tv15 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mGestureDetector = new GestureDetector(this, this);
        restgame = (Button) findViewById(R.id.restgame);
        restgame.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
        model1 = (Button) findViewById(R.id.model1);
        model1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                model = "properties1.txt";
                updateText(model);
            }
        });
        model2 = (Button) findViewById(R.id.model2);
        model2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                model = "properties2.txt";
                updateText(model);
            }
        });
        model3 = (Button) findViewById(R.id.model3);
        model3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                model = "properties3.txt";
                updateText(model);
            }
        });
        model4 = (Button) findViewById(R.id.model4);
        model4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                model = "properties4.txt";
                updateText(model);
            }
        });
        getpoints = (Button) findViewById(R.id.getpoints);
        getpoints.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 打开积分墙
                OffersManager.showOffers(MainActivity.this);
            }
        });
        mypoints = (TextView) findViewById(R.id.mypoints);
        currencyname = (TextView) findViewById(R.id.currencyname);
        pointsll = (LinearLayout) findViewById(R.id.pointsll);
        // 隐藏积分墙
        getpoints.setVisibility(View.GONE);
        pointsll.setVisibility(View.GONE);

        int width = (int) (getWith() * 0.9);
        mygl = (LinearLayout) findViewById(R.id.mygl);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, width);
        mygl.setLayoutParams(lp);
        mygl.setBackgroundResource(R.drawable.grid_bg);
        for (int i = 0; i < tvs.length; i++) {
            textViewList.add((TextView) findViewById(tvs[i]));
            allNumList.add(0);
            textViewList.get(i).setOnTouchListener(MainActivity.this);

            LinearLayout.LayoutParams lptv = new LinearLayout.LayoutParams((int) (width * 0.85 / 4),
                    (int) (width * 0.85 / 4));
            lptv.setMargins((int) (width * 0.075 / 4), (int) (width * 0.075 / 4), (int) (width * 0.075 / 4),
                    (int) (width * 0.075 / 4));
            textViewList.get(i).setLayoutParams(lptv);
            textViewList.get(i).setTextSize(width / 30);
        }

        tvscore = (TextView) findViewById(R.id.score);
        tvhisscore = (TextView) findViewById(R.id.hisscore);
        mygl.setOnTouchListener(this);
        mygl.setLongClickable(true);
    }

    public int getWith() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;// 屏幕的宽px
    }

    @Override
    protected void onPause() {
        SharedPreferences sp = getSharedPreferences("SP", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (sp.getInt("highScore", 0) < score) {
            editor.putInt("highScore", score);
            editor.commit();
        }
        String num = "";
        for (int i = 0; i < allNumList.size(); i++) {
            num += allNumList.get(i) + ",";
        }
        editor.putInt("score", score);
        editor.putString("num", num);
        editor.putString("model", model);
        editor.commit();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int points = OffersManager.getPoints(this);
        mypoints.setText("" + points);
        currencyname.setText(OffersManager.getCurrencyName(this));
        getpoints.setText("赚" + OffersManager.getCurrencyName(this));
        SharedPreferences sp = getSharedPreferences("SP", MODE_PRIVATE);
        tvhisscore.setText(sp.getInt("highScore", 0) + "");
        tvscore.setText(sp.getInt("score", 0) + "");
        score = sp.getInt("score", 0);
        model = sp.getString("model", "");
        String num = sp.getString("num", "");
        if (!Utils.isNullOrEmpty(num)) {
            String numList[] = num.split(",");
            for (int i = 0; i < numList.length; i++) {
                allNumList.set(i, Integer.parseInt(numList[i]));
            }
        } else {
            addRandomItem();
            addRandomItem();
        }
        updateText(model);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
                && Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY() - e2.getY())
                && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
            // Fling left
            flingOpertion(LEFT);
        } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
                && Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY() - e2.getY())
                && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
            // Fling right
            flingOpertion(RIGHT);
        } else if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE
                && Math.abs(e1.getX() - e2.getX()) < Math.abs(e1.getY() - e2.getY())
                && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
            // Fling up
            flingOpertion(UP);
        } else if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE
                && Math.abs(e1.getX() - e2.getX()) < Math.abs(e1.getY() - e2.getY())
                && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
            // Fling down
            flingOpertion(DOWN);
        }

        updateText(model);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Toast.makeText(MainActivity.this, "长按", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    };

    /**
     * 新增数字
     */
    public void addRandomItem() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < allNumList.size(); i++) {
            if (allNumList.get(i) == 0) {
                list.add(i);
            }
        }
        int index = (int) Math.rint(Math.random() * (list.size() - 1));
        allNumList.set(list.get(index), getRandomNum());
        Utils.createScaleAnimation(textViewList.get(list.get(index)));
        gameOver();
    }

    /**
     * 重新开始
     */
    public void resetGame() {
        SharedPreferences sp = getSharedPreferences("SP", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (sp.getInt("highScore", 0) < score) {
            editor.putInt("highScore", score);
            editor.commit();
            tvhisscore.setText(score + "");
        }
        score = 0;
        tvscore.setText(score + "");
        for (int i = 0; i < allNumList.size(); i++) {
            allNumList.set(i, 0);
        }
        addRandomItem();
        addRandomItem();
        updateText(model);
    }

    /**
     * 判断是否gameover！
     */
    public void gameOver() {
        // 纵向判断
        for (int i = 0; i < 4; i++) {
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int j = 0; j < 4; j++) {
                if (allNumList.get(i + j * 4) != 0) {
                    list.add(allNumList.get(i + j * 4));
                } else {
                    return;
                }
            }
            for (int j = 0; j < list.size() - 1; j++) {
                if (list.get(j).equals(list.get(j + 1))) {
                    return;
                }
            }
        }
        // 横向判断
        for (int i = 0; i < 4; i++) {
            ArrayList<Integer> list = new ArrayList<Integer>();
            // 获取每排非空数字
            for (int j = 0; j < 4; j++) {
                if (allNumList.get(i * 4 + j) != 0) {
                    list.add(allNumList.get(i * 4 + j));
                } else {
                    return;
                }
            }
            for (int j = 0; j < list.size() - 1; j++) {
                if (list.get(j).equals(list.get(j + 1))) {
                    return;
                }
            }
        }
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("提示").setMessage("GameOver！").setCancelable(true)
                .setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        resetGame();
                    }
                }).setNegativeButton("退出", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        resetGame();
                    }

                });
        Dialog dialog = dialogBuilder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Toast.makeText(this, "GameOver!", Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取随机数2或者4
     */
    public int getRandomNum() {
        // return (int) Math.round(Math.random() + 1) * 2;
        return (int) Math.round(Math.random() + 0.6) * 2;// 90%的概率出2，10%的概率出4
    }

    /**
     * 全体更新字体
     */
    public void updateText(String model) {
        for (int i = 0; i < textViewList.size(); i++) {
            setBgAndNum(textViewList.get(i), allNumList.get(i), model);
        }
    }

    /**
     * 设置背景颜色
     */
    public void setBgAndNum(TextView tv, int num, String model) {
        if (0 == num) {
            tv.setText("");
        } else {
            tv.setText(Utils.translation2other(MainActivity.this, num, model));
        }
        switch (num) {
            case 0:
                tv.setBackgroundResource(R.drawable.item_bg0);
                break;
            case 2:
                tv.setBackgroundResource(R.drawable.item_bg2);
                break;
            case 4:
                tv.setBackgroundResource(R.drawable.item_bg4);
                break;
            case 8:
                tv.setBackgroundResource(R.drawable.item_bg8);
                break;
            case 16:
                tv.setBackgroundResource(R.drawable.item_bg16);
                break;
            case 32:
                tv.setBackgroundResource(R.drawable.item_bg32);
                break;
            case 64:
                tv.setBackgroundResource(R.drawable.item_bg64);
                break;
            case 128:
                tv.setBackgroundResource(R.drawable.item_bg128);
                break;
            case 256:
                tv.setBackgroundResource(R.drawable.item_bg256);
                break;
            case 512:
                tv.setBackgroundResource(R.drawable.item_bg512);
                break;
            case 1024:
                tv.setBackgroundResource(R.drawable.item_bg1024);
                break;
            case 2048:
                tv.setBackgroundResource(R.drawable.item_bg2048);
                break;
            case 4096:
                tv.setBackgroundResource(R.drawable.item_bg4096);
                break;
            default:
                break;
        }
    }

    /**
     * 滑动处理
     */
    public void flingOpertion(int direction) {
        // 记录变化前的状态
        ArrayList<Integer> listOld = new ArrayList<Integer>();
        for (int i = 0; i < allNumList.size(); i++) {
            listOld.add(allNumList.get(i));
        }
        switch (direction) {
            case UP:
                optionUp();
                break;
            case DOWN:
                optionDown();
                break;
            case LEFT:
                optionLeft();
                break;
            case RIGHT:
                optionRight();
                break;

            default:
                break;
        }
        // 记录变化后的状态
        ArrayList<Integer> listNew = new ArrayList<Integer>();
        for (int i = 0; i < allNumList.size(); i++) {
            listNew.add(allNumList.get(i));
        }
        // 判断是否有变化
        if (listNew.toString().equals(listOld.toString())) {
            // Toast.makeText(this, "无效滑动", Toast.LENGTH_SHORT).show();
        } else {
            addRandomItem();
        }
    }

    // 对数字进行合并
    public void plusNum(ArrayList<Integer> list) {
        if (0 != list.size()) {
            for (int m = 0; m < list.size(); m++) {
                if ((m + 1) < list.size() && list.get(m).equals(list.get(m + 1))) {
                    score += list.get(m) + list.get(m + 1);
                    list.set(m, list.get(m) + list.get(m + 1));
                    list.remove(m + 1);
                    tvscore.setText(score + "");
                }
            }
        }
    }

    public void optionUp() {
        // 每列进行操作
        for (int i = 0; i < 4; i++) {
            ArrayList<Integer> list = new ArrayList<Integer>();
            // 获取每排非空数字
            for (int j = 0; j < 4; j++) {
                if (allNumList.get(i + j * 4) != 0) {
                    list.add(allNumList.get(i + j * 4));
                }
            }

            plusNum(list);
            // 清空数字
            for (int j = 0; j < 4; j++) {
                allNumList.set(i + j * 4, 0);
            }
            // 将合并后的数字填入方格中
            for (int j = 0; j < list.size(); j++) {
                allNumList.set(i + j * 4, list.get(j));
            }
        }
    }

    public void optionDown() {
        // 每列进行操作
        for (int i = 0; i < 4; i++) {
            ArrayList<Integer> list = new ArrayList<Integer>();
            // 获取每排非空数字
            for (int j = 3; j >= 0; j--) {
                if (allNumList.get(j * 4 + i) != 0) {
                    list.add(allNumList.get(j * 4 + i));
                }
            }
            plusNum(list);
            // 清空数字
            for (int j = 0; j < 4; j++) {
                allNumList.set(i + j * 4, 0);
            }
            // 将合并后的数字填入方格中
            for (int j = 3; j >= 4 - list.size(); j--) {
                allNumList.set(j * 4 + i, list.get(3 - j));
            }
        }
    }

    public void optionLeft() {
        // 每列进行操作
        for (int i = 0; i < 4; i++) {
            ArrayList<Integer> list = new ArrayList<Integer>();
            // 获取每排非空数字
            for (int j = 0; j < 4; j++) {
                if (allNumList.get(i * 4 + j) != 0) {
                    list.add(allNumList.get(i * 4 + j));
                }
            }
            plusNum(list);
            // 清空数字
            for (int j = 0; j < 4; j++) {
                allNumList.set(i * 4 + j, 0);
            }
            // 将合并后的数字填入方格中
            for (int j = 0; j < list.size(); j++) {
                allNumList.set(i * 4 + j, list.get(j));
            }
        }
    }

    public void optionRight() {
        // 每列进行操作
        for (int i = 0; i < 4; i++) {
            ArrayList<Integer> list = new ArrayList<Integer>();
            // 获取每排非空数字
            for (int j = 3; j >= 0; j--) {
                if (allNumList.get(j + i * 4) != 0) {
                    list.add(allNumList.get(j + i * 4));
                }
            }
            plusNum(list);
            // 清空数字
            for (int j = 0; j < 4; j++) {
                allNumList.set(j + i * 4, 0);
            }
            // 将合并后的数字填入方格中
            for (int j = 3; j >= 4 - list.size(); j--) {
                allNumList.set(j + i * 4, list.get(3 - j));
            }
        }
    }

}
