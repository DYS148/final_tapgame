package com.example.final_project
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.final_project.MainActivity.Companion.money
import com.example.final_project.MainActivity.Companion.sk1lv
import com.example.final_project.MainActivity.Companion.sk2lv
import com.example.final_project.MainActivity.Companion.sk3lv
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SecondFragment : Fragment() {


    val scope = MainScope()
    private lateinit var money: TextView
    private lateinit var  skill1Cost : TextView
    private lateinit var  skill1Lv : TextView
    private lateinit var  skill2Cost : TextView
    private lateinit var  skill2Lv : TextView
    private lateinit var  skill3Cost : TextView
    private lateinit var  skill3Lv : TextView
    private lateinit var skill1Update : Button
    private lateinit var skill2Update : Button
    private lateinit var skill3Update : Button

    private var sk1cost : Long = 10 + (sk1lv.toLong()) * 15
    private var sk2cost : Long = 20 + (sk2lv.toLong()) * 20
    private var sk3cost : Long = 30 + (sk3lv.toLong()) * 20

    private var s1 = sk1lv.toString() //輸出當前技能1等級的String
    private var s2 = sk1cost.toString() //輸出當前花費的String
    private var s3 = sk2lv.toString() //輸出當前技能2等級的String
    private var s4 = sk2cost.toString() //輸出當前花費的String
    private var s5 = sk3lv.toString() //輸出當前技能3等級的String
    private var s6 = sk3cost.toString() //輸出當前花費的String


    //技能花費

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {

        val v: View = inflater.inflate(R.layout.skill, container, false)

        money = v.findViewById<View>(R.id.money) as TextView //抓錢
        skill1Cost = v.findViewById<View>(R.id.skill_1_cost) as TextView //抓技能1
        skill1Lv = v.findViewById<View>(R.id.skill_1_lv) as TextView
        skill1Update = v.findViewById<View>(R.id.skill_1_update) as Button
        skill2Cost = v.findViewById<View>(R.id.skill_2_cost) as TextView //抓技能2
        skill2Lv = v.findViewById<View>(R.id.skill_2_lv) as TextView
        skill2Update = v.findViewById<View>(R.id.skill_2_update) as Button
        skill3Cost = v.findViewById<View>(R.id.skill_3_cost) as TextView //抓技能3
        skill3Lv = v.findViewById<View>(R.id.skill_3_lv) as TextView
        skill3Update = v.findViewById<View>(R.id.skill_3_update) as Button

        initShow()

        scope.launch {
            refresh()
        }


//--------------------------技能升級------------------------------
        skill1Update.setOnClickListener {  //技能1升級

            if (MainActivity.money >= sk1cost) //錢夠才能升級
            {
                MainActivity.money -= sk1cost
                sk1lv += 1
                sk1cost += 15
                skill1Lv.text = sk1lv.toString()
                skill1Cost.text = sk1cost.toString()
            }

        }//技能1升級結束

        skill2Update.setOnClickListener {  //技能2升級

            if (MainActivity.money >= sk2cost) //錢夠才能升級
            {
                MainActivity.money -= sk2cost
                sk2lv += 1
                sk2cost += 20
                skill2Lv.text = sk2lv.toString()
                skill2Cost.text = sk2cost.toString()
            }

        }//技能2升級結束

        skill3Update.setOnClickListener {  //技能3升級

            if (MainActivity.money >= sk3cost) //錢夠才能升級
            {
                if (sk3lv <= 50) //最高50級 80% 爆擊率
                {
                    MainActivity.money -= sk3cost
                    sk3lv += 1
                    sk3cost += 20
                    skill3Lv.text = sk3lv.toString()
                    skill3Cost.text = sk3cost.toString()
                }
            }
        }//技能3升級結束
//--------------------------技能升級------------------------------

        return v


    }


    private suspend fun refresh() { //每0.5秒更新錢
        while (true){
            delay(100)
            money.text = MainActivity.money.toString()
        }
    }
    private fun initShow(){
        skill1Lv.text = s1        //顯示等級和花費
        skill1Cost.text = s2
        skill2Lv.text = s3
        skill2Cost.text = s4
        skill3Lv.text = s5
        skill3Cost.text = s6
        money.text = MainActivity.money.toString()
    }
    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    companion object {



        fun newInstance(text: String?): SecondFragment {
            val f = SecondFragment()
            val b = Bundle()
            b.putString("msg", text)
            f.arguments = b
            return f
        }


    }


}






