package com.example.final_project

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FourthFragment : Fragment() {
    private val scope = MainScope()
    private lateinit var recordId : TextView
    private lateinit var recordMoney : TextView
    private lateinit var recordSkill1 : TextView
    private lateinit var recordSkill2 : TextView
    private lateinit var recordSkill3 : TextView
    private lateinit var recordTotalKill : TextView
    private lateinit var recordTotalMoney : TextView
    private lateinit var recordHelper1 : TextView
    private lateinit var recordHelper2 : TextView
    private lateinit var recordHelper3 : TextView
    private lateinit var recordStage : TextView
    private lateinit var recordPlayTime : TextView

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {

        val v: View = inflater.inflate(R.layout.set, container, false)
        val btnOut = v.findViewById<View>(R.id.btnLogout) as Button
        recordId = v.findViewById<View>(R.id.record_id) as TextView
        recordMoney = v.findViewById<View>(R.id.record_money) as TextView
        recordSkill1 = v.findViewById<View>(R.id.record_skill1) as TextView
        recordSkill2 = v.findViewById<View>(R.id.record_skill2) as TextView
        recordSkill3 = v.findViewById<View>(R.id.record_skill3) as TextView
        recordTotalKill = v.findViewById<View>(R.id.record_totalKill) as TextView
        recordTotalMoney = v.findViewById<View>(R.id.record_totalMoney) as TextView
        recordHelper1 = v.findViewById<View>(R.id.record_helper1) as TextView
        recordHelper2 = v.findViewById<View>(R.id.record_helper2) as TextView
        recordHelper3 = v.findViewById<View>(R.id.record_helper3) as TextView
        recordStage = v.findViewById<View>(R.id.record_stage) as TextView
        recordPlayTime = v.findViewById<View>(R.id.record_playtime) as TextView

        scope.launch {
            refresh()
        }
        btnOut.setOnClickListener {
           logOut()
        }
        return v
    }
    @SuppressLint("SetTextI18n")
    private suspend fun refresh(){
        while(true) {
            recordId.text = "ID: ${MainActivity.id}"
            recordMoney.text ="目前金錢: ${MainActivity.money}"
            recordSkill1.text = "重擊等級: ${MainActivity.sk1lv}"
            recordSkill2.text = "金錢手等級: ${MainActivity.sk2lv}"
            recordSkill3.text = "爆擊星等級: ${MainActivity.sk3lv}"
            recordTotalKill.text = "總共擊殺怪物: ${MainActivity.tKill}"
            recordTotalMoney.text = "總持有金錢: ${MainActivity.tMoney}"
            recordHelper1.text = "幫手A等級: ${MainActivity.help1}"
            recordHelper2.text = "幫手B等級: ${MainActivity.help2}"
            recordHelper3.text = "幫手C等級: ${MainActivity.help3}"
            recordStage.text = "目前關卡: ${MainActivity.area}"
            recordPlayTime.text = "遊玩時間: ${(MainActivity.playTime/3600).toInt()}:${(MainActivity.playTime/60)%60.toInt()}:${(MainActivity.playTime%60).toInt()}"
            delay(100)
        }

    }
    private fun removeSQLite(){
        MainActivity.dbrw.execSQL("UPDATE myTable SET attack = ${MainActivity.attack}" + " WHERE _id LIKE '" +
                MainActivity.id + "'")
        MainActivity.dbrw.execSQL("UPDATE myTable SET coin = ${MainActivity.money}" + " WHERE _id LIKE '" +
                MainActivity.id + "'")
        MainActivity.dbrw.execSQL("UPDATE myTable SET skill1Level = ${MainActivity.sk1lv}" + " WHERE _id LIKE '" +
                MainActivity.id + "'")
        MainActivity.dbrw.execSQL("UPDATE myTable SET skill2Level = ${MainActivity.sk2lv}" + " WHERE _id LIKE '" +
                MainActivity.id + "'")
        MainActivity.dbrw.execSQL("UPDATE myTable SET skill3Level = ${MainActivity.sk3lv}" + " WHERE _id LIKE '" +
                MainActivity.id + "'")
        MainActivity.dbrw.execSQL("UPDATE myTable SET killMonster = ${MainActivity.tKill}" + " WHERE _id LIKE '" +
                MainActivity.id + "'")
        MainActivity.dbrw.execSQL("UPDATE myTable SET totalCoin = ${MainActivity.tMoney}" + " WHERE _id LIKE '" +
                MainActivity.id + "'")
        MainActivity.dbrw.execSQL("UPDATE myTable SET helper1Level = ${MainActivity.help1}" + " WHERE _id LIKE '" +
                MainActivity.id + "'")
        MainActivity.dbrw.execSQL("UPDATE myTable SET helper2Level = ${MainActivity.help2}" + " WHERE _id LIKE '" +
                MainActivity.id + "'")
        MainActivity.dbrw.execSQL("UPDATE myTable SET helper3Level = ${MainActivity.help3}" + " WHERE _id LIKE '" +
                MainActivity.id + "'")
        MainActivity.dbrw.execSQL("UPDATE myTable SET area = ${MainActivity.area} " + " WHERE _id LIKE '" +
                MainActivity.id + "'")
        MainActivity.dbrw.execSQL("UPDATE myTable SET currentMonsterHp = ${MainActivity.currentMonsterHp} " + " WHERE _id LIKE '" +
                MainActivity.id + "'")
        MainActivity.dbrw.execSQL("UPDATE myTable SET skill1Cd = ${MainActivity.skill1Cd} " + " WHERE _id LIKE '" +
                MainActivity.id + "'")
        MainActivity.dbrw.execSQL("UPDATE myTable SET skill2Cd = ${MainActivity.skill2Cd} " + " WHERE _id LIKE '" +
                MainActivity.id + "'")
        MainActivity.dbrw.execSQL("UPDATE myTable SET skill3Cd = ${MainActivity.skill3Cd} " + " WHERE _id LIKE '" +
                MainActivity.id + "'")
        MainActivity.dbrw.execSQL("UPDATE myTable SET totalTime = ${MainActivity.playTime}" + " WHERE _id LIKE '" +
                MainActivity.id + "'")

        MainActivity.mediaPlayer.stop()
        MainActivity.dbrw.close()
        MainActivity.cursor.close()
    }
    private fun logOut(){
        MainActivity.dbrw.execSQL("UPDATE mytable SET logged = 0")
        val intent = Intent(this.context, Login::class.java)
        startActivity(intent)
        for (fragment in activity!!.supportFragmentManager.fragments) {
            activity!!.supportFragmentManager.beginTransaction().remove(fragment).commit()
        }
        activity?.finish()
    }

    override fun onDestroy() {
        removeSQLite()
        super.onDestroy()
        scope.cancel()
    }

    companion object {
        fun newInstance(text: String?): FourthFragment {
            val f = FourthFragment()
            val b = Bundle()
            b.putString("msg", text)
            f.arguments = b
            return f
        }
    }
}