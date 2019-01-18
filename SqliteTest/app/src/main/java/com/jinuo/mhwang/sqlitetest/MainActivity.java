package com.jinuo.mhwang.sqlitetest;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    Button btn_createTable;
    EditText et_createDbNum;
    EditText et_createTableNum;

    Button btn_insert;
    EditText et_insertNum;
    TextView tv_insertCount;
    DBOperator mOperator;

    Button btn_insertBatch;
    EditText et_insertNumBatch;
    TextView tv_insertCountBatch;

    Button btn_query;
    EditText et_queryNum;
    TextView tv_queryCount;

    TextView tv_error;

    boolean createdTable = false;

    int tableCount = 0;
    int dbCount = 0;
    long totalInsert = 0;

    private void showLog(String msg){
        Log.d("--MainActivity-->",msg);
    }

    private void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initComponent();
        initData();
        initListener();
    }

    private void initData(){
        getData();
        if (dbCount != 0){
            setCreateEnable();
            createdTable = true;
            MyApplication.DB_EXITS = true;
            tv_insertCount.setText(""+totalInsert);
        }
    }

    private void setCreateEnable(){
        btn_createTable.setEnabled(false);
        et_createDbNum.setText(""+dbCount);
        et_createDbNum.setEnabled(false);
        et_createTableNum.setText(""+tableCount);
        et_createTableNum.setEnabled(false);
    }

    public static final String FILE_NAME = "shareData";
    private void saveData(){
        SharedPreferences sp = getSharedPreferences(FILE_NAME,MODE_PRIVATE);
        sp.edit().putInt("dbCount",dbCount)
                .putInt("tableCount",tableCount)
                .putLong("totalInsert",totalInsert)
                .commit();
    }

    private void getData(){
        SharedPreferences sp = getSharedPreferences(FILE_NAME,MODE_PRIVATE);
        dbCount = sp.getInt("dbCount",0);
        tableCount = sp.getInt("tableCount",0);
        totalInsert = sp.getLong("totalInsert",0);
    }

    private void initComponent(){
        btn_createTable = (Button) findViewById(R.id.btn_createTable);
        et_createDbNum = (EditText) findViewById(R.id.et_createDbNum);
        et_createTableNum = (EditText) findViewById(R.id.et_createTableNum);

        btn_insert = (Button) findViewById(R.id.btn_insert);
        et_insertNum = (EditText) findViewById(R.id.et_insertNum);
        tv_insertCount = (TextView) findViewById(R.id.tv_insertCount);

        btn_insertBatch = (Button) findViewById(R.id.btn_insertBatch);
        et_insertNumBatch = (EditText) findViewById(R.id.et_insertNumBatch);
        tv_insertCountBatch = (TextView) findViewById(R.id.tv_insertCountBatch);

        btn_query = (Button) findViewById(R.id.btn_query);
        et_queryNum = (EditText) findViewById(R.id.et_queryNum);
        tv_queryCount = (TextView) findViewById(R.id.tv_queryCount);

        tv_error = (TextView) findViewById(R.id.tv_error);
    }

    private void initListener(){


        btn_createTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sCreateDbCount = et_createDbNum.getText().toString();
                String sCreateTableCount = et_createTableNum.getText().toString();
                if (TextUtils.isEmpty(sCreateDbCount) || TextUtils.isEmpty(sCreateTableCount)){
                    showToast("请输入创建数据库或表的数量");
                    return;
                }
                tableCount = Integer.parseInt(sCreateTableCount);
                dbCount = Integer.parseInt(sCreateDbCount);
//                if (mOperator == null){
                    createTable();
//                }else {
//                    showToast("已经创建数据表，请不要重复创建");
//                }
            }
        });

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbCount == 0){
                    showToast("请先创建数据库");
                    return;
                }

                String sInsertCount = et_insertNum.getText().toString();
                if (!createdTable || TextUtils.isEmpty(sInsertCount)){
                    showToast("请先创建表或输入插入数量");
                    return;
                }
                final int insertCount = Integer.parseInt(et_insertNum.getText().toString());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        insertData(insertCount,false);
                    }
                }).start();
            }
        });

        btn_insertBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbCount == 0){
                    showToast("请先创建数据库");
                    return;
                }

                String sInsertCount = et_insertNumBatch.getText().toString();
                if (!createdTable || TextUtils.isEmpty(sInsertCount)){
                    showToast("请先创建表或输入插入数量");
                    return;
                }
                final int insertCount = Integer.parseInt(et_insertNumBatch.getText().toString());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        insertData(insertCount,true);
                    }
                }).start();
            }
        });

        // 查询
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbCount == 0){
                    showToast("请先创建数据库");
                    return;
                }
                String sQueryCount = et_queryNum.getText().toString();
                if (!createdTable || TextUtils.isEmpty(sQueryCount)){
                    showToast("请先创建表或输入小于插入数量的数");
                    return;
                }
                final int queryCount = Integer.parseInt(et_queryNum.getText().toString());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        queryData(queryCount);
                    }
                }).start();
            }
        });
    }

    private void createTable(){
        CreateTableTask tableTask = new CreateTableTask();
        tableTask.execute(dbCount,tableCount);
    }

    long insertId = 0;
    private void insertData(final int insertCount, boolean batch) {
        totalInsert += insertCount;
        List<Number> numbers = new ArrayList<>();
        Number number;
        for (int i = 0; i < insertCount; i++) {
            number = new Number();
            number.number = "12" + (int) (Math.random() * 1000);
            number.pos_wan = (int) (Math.random() * 1000);
            number.pos_qian = (int) (Math.random() * 1000);
            number.pos_bai = (int) (Math.random() * 1000);
            number.pos_shi = (int) (Math.random() * 1000);
            number.maxValue = (int) (Math.random() * 1000);
            number.minValue = (int) (Math.random() * 1000);
            number.he_zhi = (int) (Math.random() * 1000);
            number.kua_du = (int) (Math.random() * 1000);
            number.is_shang_shan_number = (int) (Math.random() * 1000);
            number.is_xia_shan_number = (int) (Math.random() * 1000);
            number.xingtai_pos_wan_jiou = (int) (Math.random() * 1000);
            number.xingtai_pos_qian_jiou = (int) (Math.random() * 1000);
            number.xingtai_pos_bai_jiou = (int) (Math.random() * 1000);
            number.xingtai_pos_shi_jiou = (int) (Math.random() * 1000);
            number.xingtai_pos_ge_jiou = (int) (Math.random() * 1000);
            number.xingtai_012 = "11" + (int) (Math.random() * 1000);
            number.xingtai_jiou = "22" + (int) (Math.random() * 1000);
            number.xingtai_zhihe = "22" + (int) (Math.random() * 1000);
            number.count_xingtai_ji = (int) (Math.random() * 1000);
            number.count_xingtai_ou = (int) (Math.random() * 1000);
            number.count_xingtai_0 = (int) (Math.random() * 1000);
            number.count_xingtai_1 = (int) (Math.random() * 1000);
            number.count_xingtai_2 = (int) (Math.random() * 1000);
            number.count_xingtai_zhi = (int) (Math.random() * 1000);
            number.count_xingtai_he = (int) (Math.random() * 1000);
            number.xingtai_pos_wan_zhihe = (int) (Math.random() * 1000);
            number.xingtai_pos_qian_zhihe = (int) (Math.random() * 1000);
            number.xingtai_pos_bai_zhihe = (int) (Math.random() * 1000);
            number.xingtai_pos_shi_zhihe = (int) (Math.random() * 1000);
            number.xingtai_pos_ge_zhihe = (int) (Math.random() * 1000);
            number.xingtai_pos_wan_012 = (int) (Math.random() * 1000);
            number.xingtai_pos_qian_012 = (int) (Math.random() * 1000);
            number.xingtai_pos_bai_012 = (int) (Math.random() * 1000);
            number.xingtai_pos_shi_012 = (int) (Math.random() * 1000);
            number.xingtai_pos_ge_012 = (int) (Math.random() * 1000);
            numbers.add(number);
        }
            if(mOperator == null){
                showLog("insertData operator is null");
                mOperator = DBOperator.getInstance2(MainActivity.this,dbCount,tableCount);
                addSQLExceptionListener();
                mOperator.initDataBase();
            }
            if (batch) {
                insertId = insertCount;
                // 插入之前先初始化一次，确定路径中存在
                mOperator.insertNumberByBatch(numbers);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_insertCountBatch.setText("" + totalInsert);
                    }
                });
            } else {
                for (int j = 0; j < numbers.size(); j++) {
                    final int k = j + 1;
                    insertId = mOperator.insertNumber(numbers.get(j));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            et_insertNum.setText(""+(insertCount-k));
                            tv_insertCount.setText("" + (totalInsert - insertCount + k));
                        }
                    });
                }
            }
    }

    long queryId = 0;
    private void queryData(int queryCount){
        if (mOperator == null){
            showLog("queryData operator is null");
            mOperator = DBOperator.getInstance(MainActivity.this,dbCount,tableCount);
            addSQLExceptionListener();
            mOperator.initDataBase();
        }
        for (int i = 0; i < queryCount; i++) {
            queryId = mOperator.queryNumber();
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    tv_queryCount.setText("" + queryId);
//                }
//            });
        }
    }

    class CreateTableTask extends AsyncTask<Integer,Void,Integer>{
        ProgressDialog mProgressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setMessage("创建数据库中……");
            mProgressDialog.show();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            int createDbCount = params[0];
            int createTableCount = params[1];
            mOperator = DBOperator.getInstance(MainActivity.this,createDbCount,
                    createTableCount);
            // 保存数据库数量和表
            saveData();
            addSQLExceptionListener();
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            mProgressDialog.dismiss();
            setCreateEnable();
            createdTable = true;
            showToast("创建成功");
        }
    }

    private void addSQLExceptionListener(){
        mOperator.setOnSQLEceptionListener(new DBOperator.OnSQLEceptionListener() {
            @Override
            public void onSQLEception(final String e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_error.setText(e);
                    }
                });
            }

            @Override
            public void onBatchFinish(final String msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_error.setText(msg);
                    }
                });
            }

            @Override
            public void onQueryData(final String data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_queryCount.setText(data);
                    }
                });
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        saveData();
        running = false;
        showToast("保存表数据");
    }


    private void autoRun(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btn_insert.setEnabled(false);
                    btn_insertBatch.setEnabled(false);
                    btn_query.setEnabled(false);
                    et_queryNum.setText(""+autoQueryData);
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    insertOver = false;
                    insertData(autoInsertData,false);
                    insertOver = true;
                    showLog("insert data is over");
                }
            }).start();


            new Thread(new Runnable() {
                        @Override
                        public void run() {
                            queryOver = false;
                            queryData(autoQueryData);
                            queryOver = true;
                            showLog("query data is over");
                        }
                    }).start();

    }

    private int autoInsertData = 10;
    private int autoQueryData = 1;
    private boolean running = true;
    private boolean insertOver = true;
    private boolean queryOver = true;
    @Override
    protected void onResume() {
        super.onResume();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(running && dbCount != 0){
                    if (insertOver && queryOver) {
                        showLog("run once");
                        autoRun();
                    } else {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}
