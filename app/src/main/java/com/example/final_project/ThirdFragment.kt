package com.example.final_project

import android.annotation.SuppressLint
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

class ThirdFragment : Fragment() {
    private val scope = MainScope()
    private lateinit var tvMoney: TextView
    private lateinit var tvPoisonPrice : TextView
    private lateinit var btnBuyPotion : Button
    private lateinit var btnHelper1 : Button
    private lateinit var btnHelper2 : Button
    private lateinit var btnHelper3 : Button
    private lateinit var tvHelper1Lv :TextView
    private lateinit var tvHelper2Lv :TextView
    private lateinit var tvHelper3Lv :TextView
    private lateinit var tvHelper1Atk : TextView
    private lateinit var tvHelper2Atk : TextView
    private lateinit var tvHelper3Atk : TextView
    private lateinit var tvHelper1Price : TextView
    private lateinit var tvHelper2Price : TextView
    private lateinit var tvHelper3Price : TextView
    private var sellPrice : Int = (MainActivity.attack * 50 + 200)
    private var helper1Price : Int = (MainActivity.help1 * 100) + 300
    private var helper2Price : Int = (MainActivity.help2 * 100) + 300
    private var helper3Price : Int = (MainActivity.help3 * 100) + 300

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.shop, container, false)
        btnBuyPotion = v.findViewById<View>(R.id.buy_potion) as Button
        tvPoisonPrice = v.findViewById<View>(R.id.sellprice) as TextView
        tvMoney = v.findViewById<View>(R.id.money) as TextView //抓錢
        btnHelper1 = v.findViewById<View>(R.id.btn_helper1) as Button
        btnHelper2 = v.findViewById<View>(R.id.btn_helper2) as Button
        btnHelper3 = v.findViewById<View>(R.id.btn_helper3) as Button
        tvHelper1Lv = v.findViewById<View>(R.id.tv_helper1) as TextView
        tvHelper2Lv = v.findViewById<View>(R.id.tv_helper2) as TextView
        tvHelper3Lv = v.findViewById<View>(R.id.tv_helper3) as TextView
        tvHelper1Atk = v.findViewById<View>(R.id.tv_help1atk) as TextView
        tvHelper2Atk = v.findViewById<View>(R.id.tv_help2atk) as TextView
        tvHelper3Atk = v.findViewById<View>(R.id.tv_help3atk) as TextView
        tvHelper1Price = v.findViewById<View>(R.id.tv_help1Lv) as TextView
        tvHelper2Price = v.findViewById<View>(R.id.tv_help2Lv) as TextView
        tvHelper3Price = v.findViewById<View>(R.id.tv_help3Lv) as TextView

        tvPoisonPrice.text = "價格:${sellPrice}"

        tvHelper1Price.text = "價格:${helper1Price}"
        tvHelper1Lv.text = "等級:${MainActivity.help1}"
        tvHelper1Atk.text = "攻擊力:${(MainActivity.help1 * 3)}"//一等3點攻擊力

        tvHelper2Price.text = "價格:${helper2Price}"
        tvHelper2Lv.text = "等級:${MainActivity.help2}"
        tvHelper2Atk.text = "攻擊力:${(MainActivity.help2 * 3)}"//一等3點攻擊力

        tvHelper3Price.text = "價格:${helper3Price}"
        tvHelper3Lv.text = "等級:${MainActivity.help3}"
        tvHelper3Atk.text = "攻擊力:${(MainActivity.help3 * 3)}"//一等3點攻擊力



        scope.launch {
            refresh()
        }

        //-------------------------購買------------------------------
        btnBuyPotion.setOnClickListener {
            if (MainActivity.money >= sellPrice){
                MainActivity.money -= sellPrice
                MainActivity.attack += 1
                sellPrice += 50
                tvPoisonPrice.text = "價格:${sellPrice}"
            }
        }
        btnHelper1.setOnClickListener {
            if(MainActivity.money >= helper1Price){
                MainActivity.money -= helper1Price
                helper1Price += 100
                MainActivity.help1 += 1
                tvHelper1Price.text = "價格:${helper1Price}"
                tvHelper1Lv.text = "等級:${MainActivity.help1}"
                tvHelper1Atk.text = "攻擊力:${(MainActivity.help1 * 3)}"
            }
        }
        btnHelper2.setOnClickListener {
            if(MainActivity.money >= helper2Price){
                MainActivity.money -= helper2Price
                helper2Price += 100
                MainActivity.help2 += 1
                tvHelper2Price.text = "價格:${helper2Price}"
                tvHelper2Lv.text = "等級:${MainActivity.help2}"
                tvHelper2Atk.text = "攻擊力:${(MainActivity.help2 * 3)}"
            }
        }
        btnHelper3.setOnClickListener {
            if(MainActivity.money >= helper3Price){
                MainActivity.money -= helper1Price
                helper3Price += 100
                MainActivity.help3 += 1
                tvHelper3Price.text = "價格:${helper3Price}"
                tvHelper3Lv.text = "等級:${MainActivity.help3}"
                tvHelper3Atk.text = "攻擊力:${(MainActivity.help3 * 3)}"
            }
        }

        return v
    }
    private suspend fun refresh() { //每0.1秒更新錢
        while (true){
            delay(100)
            tvMoney.text = "${MainActivity.money}"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    companion object {


        fun newInstance(text: String?): ThirdFragment {
            val f = ThirdFragment()
            val b = Bundle()
            b.putString("msg", text)
            f.arguments = b
            return f
        }


    }
}