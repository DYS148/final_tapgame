package com.example.final_project


import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.database.CursorIndexOutOfBoundsException
import android.database.sqlite.SQLiteDatabase
import android.media.MediaPlayer
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class Login : AppCompatActivity() {
    private lateinit var dbrw: SQLiteDatabase
    private lateinit var id : String
    private lateinit var mediaPlayer: MediaPlayer
    private fun showToast(text: String) =
        Toast.makeText(this,text, Toast.LENGTH_SHORT).show()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        mediaPlayer= MediaPlayer.create(this,R.raw.aoe)
        mediaPlayer.start()
        mediaPlayer.isLooping=true
        dbrw = MyDBHelper(this).writableDatabase//建立資料庫的物件
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginpage)
        val am=AlphaAnimation(1.0f,0.0f)//透明度
        am.duration = 2000//持續時間
        am.repeatCount = -1 //設定重複次數 -1為無限次數 0
        val tvStart : TextView = findViewById(R.id.tv_start)
        tvStart.startAnimation(am)
        val btnStart : Button = findViewById(R.id.btn_start)
        val tvWel: TextView = findViewById(R.id.tv_welcome)
        val logFinder : Cursor = dbrw.rawQuery("SELECT * FROM myTable WHERE logged LIKE 'True'", null)
        logFinder.moveToFirst()
        var isLogged = logFinder.count
        if(isLogged != 0){
            tvStart.text = "點擊以開始"
            id = logFinder.getString(0)
            tvWel.text = "Welcome!$id"
        }
        logFinder.close()
        btnStart.setOnClickListener {
            if (isLogged==0) {
                val builder = AlertDialog.Builder(this)
                val inflater = layoutInflater
                val dialogLayout = inflater.inflate(R.layout.login, null)
                val account : EditText = dialogLayout.findViewById(R.id.ed_text)
                val password: EditText = dialogLayout.findViewById(R.id.ed_text2)
                with(builder) {
                    setTitle("登入帳號")
                    setPositiveButton("Log in") { _ , _ ->
                        var c : Cursor? = null
                        try {
                            c = dbrw.rawQuery("SELECT * FROM myTable WHERE _id LIKE '%${account.text}%'", null)
                            c.moveToFirst()
                            if (account.text.toString() == c.getString(0)) {
                                if (password.text.toString() == c.getString(1)) {
                                    showToast("登入成功")
                                    dbrw.execSQL("UPDATE myTable SET logged = 'True'" + " WHERE _id LIKE '" +
                                            account.text.toString() + "'")
                                    isLogged = 1
                                    id = c.getString(0)
                                    tvWel.text = "Welcome!${id}"
                                    tvStart.text = "點擊以開始"
                                } else
                                    showToast("密碼錯誤")
                            }
                        }
                        catch (e: CursorIndexOutOfBoundsException) {
                            showToast("無此帳號密碼")
                        }
                        c?.close()
                    }
                    setNeutralButton("創建帳號") { _ , _ ->
                        if (account.text.isBlank() || password.text.isBlank())
                            showToast("請勿空白")
                        else {
                            try {
                                dbrw.execSQL("INSERT INTO myTable(_id,password,logged,attack,coin,skill1Level," +
                                        "skill2Level,skill3Level,killMonster,killBoss," +
                                        "totalCoin,helper1Level,helper2Level,helper3Level,area,currentMonsterHp," +
                                        "skill1Cd,skill2Cd,skill3Cd,totalTime) VALUES('${account.text}'," +
                                        "'${password.text}','0',1,0,0,0,0,0,0,0,0,0,0,1,5,0,0,0,0) ")
                                showToast("新增帳號成功")
                                account.setText("")
                                password.setText("")
                            } catch (e: Exception) {
                                showToast("新增帳號失敗$e")
                            }
                        }
                    }
                    setNegativeButton("取消") { _ , _ ->
                    }

                    setView(dialogLayout)
                    show()
                }
            }
            else{
                val intent = Intent(this@Login, MainActivity::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onDestroy() {
        dbrw.close()
        mediaPlayer.stop()
        super.onDestroy()
    }
}