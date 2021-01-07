package com.example.final_project

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDBHelper (context: Context): SQLiteOpenHelper(context,"mdatabase.db",null,9){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE myTable(_id text PRIMARY KEY," + //id只能有一個不能有相同的
                " password text NOT NULL," +//密碼
                " logged text NOT NULL," +//登入旗標 1:登入,0:未登入
                " attack text NOT NULL," +//攻擊力
                " coin REAL NOT NULL," +//金錢
                " skill1Level INTEGER NOT NULL," +//技能1等級
                " skill2Level INTEGER NOT NULL," +//技能2等級
                " skill3Level INTEGER NOT NULL," +//技能3等級
                " killMonster REAL NOT NULL," +//總殺敵數
                " killBoss REAL NOT NULL," +//總殺Boss數
                " totalCoin REAL NOT NULL," +//總共金錢
                " helper1Level INTEGER NOT NULL," +//協力者1等級
                " helper2Level INTEGER NOT NULL," +//協力者2等級
                " helper3Level INTEGER NOT NULL," +//協力者3等級
                " area INTEGER NOT NULL," +//關卡
                " currentMonsterHp INTEGER NOT NULL," +//怪物血量
                " skill1Cd INTEGER NOT NULL," +//技能1CD
                " skill2Cd INTEGER NOT NULL," +//技能2CD
                " skill3Cd INTEGER NOT NULL," +//技能3CD
                " totalTime INTEGER NOT NULL)")//遊玩時間
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS myTable")
        onCreate(db)
    }
}