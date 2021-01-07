package com.example.final_project
import android.annotation.SuppressLint
import android.graphics.Color
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import kotlin.math.pow

class FirstFragment : Fragment() {
    private lateinit var soundPool : SoundPool
    private val scope = MainScope()
    private var monsterHp = (5 * 1.2.pow(MainActivity.area.toDouble()))
    private var stages = 0//打10個stages加1個area
    private val effectDelay : Long = 60
    private var monsterMoney = (10 * 1.25.pow(MainActivity.area.toDouble())) //掉落的錢
    private var bgCounter = 0 //數到10更換地圖
    private var skill2time = 0 //技能持續時間
    private var moneyHandFlag = false
    private var skill3time = 0 //技能持續時間
    private var criticalHandFlag = false
    private var criticalStar = 0
    private  var soundBomb = 0
    private  var soundDead = 0
    private  var soundSwing = 0
    private lateinit var hp : TextView
    private lateinit var money: TextView
    private lateinit var stage: TextView
    private lateinit var area: TextView
    private lateinit var monster: ImageView
    private lateinit var skill1 :Button
    private lateinit var skill2 :Button
    private lateinit var skill3 :Button
    private lateinit var tvdps : TextView
    private lateinit var help1 : ImageView
    private lateinit var help2 : ImageView
    private lateinit var help3 : ImageView
    private lateinit var slash : ImageView
    private lateinit var critical : ImageView
    private lateinit var imageBg : ImageView
    private lateinit var atk : TextView

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v: View = inflater.inflate(R.layout.fight, container, false)
        soundPool = SoundPool(6, AudioManager.STREAM_MUSIC, 0)
        soundBomb = soundPool!!.load(activity, R.raw.bomb, 1)
        soundDead = soundPool!!.load(activity, R.raw.dead, 1)
        soundSwing = soundPool.load(activity, R.raw.swing1, 1)

        hp = v.findViewById<View>(R.id.HP) as TextView //抓血量
        money = v.findViewById<View>(R.id.money) as TextView //抓錢
        monster = v.findViewById<View>(R.id.monster) as ImageButton //抓怪物
        stage = v.findViewById<View>(R.id.stage) as TextView //抓關卡
        area = v.findViewById<View>(R.id.area) as TextView //抓區域
        skill1 = v.findViewById<View>(R.id.skill_1) as Button //抓技能1
        skill2 = v.findViewById<View>(R.id.skill_2) as Button //抓技能2
        skill3 = v.findViewById<View>(R.id.skill_3) as Button //抓技能3
        help1 = v.findViewById<View>(R.id.img_helper1) as ImageView
        help2 = v.findViewById<View>(R.id.img_helper2) as ImageView
        help3 = v.findViewById<View>(R.id.img_helper3) as ImageView
        slash = v.findViewById<View>(R.id.slash) as ImageView //抓攻擊特效
        critical = v.findViewById<View>(R.id.boom) as ImageView//爆擊特效
        imageBg = v.findViewById<View>(R.id.image_bg) as ImageView //抓背景
        atk = v.findViewById<View>(R.id.tv_atk) as TextView
        tvdps = v.findViewById<View>(R.id.tv_dps) as TextView

        //Create BackGround array
        val backgrounds = ArrayList<String>()//背景陣列
        backgrounds.add("bg1")
        backgrounds.add("bg2")
        backgrounds.add("bg3")
        backgrounds.add("bg4")
        //Create Monster array
        val monsters = ArrayList<String>()//怪物陣列
        monsters.add("bee")
        monsters.add("bird")
        monsters.add("crab")
        monsters.add("caterpillar")
        monsters.add("dragonfly")
        monsters.add("elves")
        monsters.add("fish")
        monsters.add("goblin")
        monsters.add("moth")
        monsters.add("rabbit")
        monsters.add("skeleton")
        monsters.add("tauren")
        monsters.add("wolf")

        //Create Boss array
        val boss = ArrayList<String>()//頭目陣列
        boss.add("boss_fish")
        boss.add("boss_dragon")
        //show
        showHp(MainActivity.currentMonsterHp,hp)
        showMoney(money)

        atk.text = MainActivity.attack.toString()
        stage.text = "${stages}/10" //顯示關卡及區域
        area.text = "關卡${MainActivity.area}"
        scope.launch {
            refresh(monsters,backgrounds)
        }

//-----------------攻擊-----------------------------------------------------------------
        monster.setOnClickListener{ //點擊怪物圖進行攻擊
            showEffect(effectDelay,slash)//攻擊特效
            soundPool?.play(soundSwing, 1F, 1F, 0, 0, 1F)
            if (criticalHandFlag)//確認有無爆擊星
            {
                criticalStar = (0..100).random()
                if ( criticalStar <= 30 + (MainActivity.sk3lv)*1 ) {
                    showEffect(effectDelay,critical)
                    soundPool?.play(soundBomb, 1F, 1F, 0, 0, 1F)
                    MainActivity.currentMonsterHp = minusMonsterHp(MainActivity.currentMonsterHp, MainActivity.attack * 3)//爆擊星觸發攻擊3倍
                }
                else
                    MainActivity.currentMonsterHp = minusMonsterHp(MainActivity.currentMonsterHp,MainActivity.attack)//血量降低

                showHp(MainActivity.currentMonsterHp,hp)//改變怪物血量
            }
            else
            {
                MainActivity.currentMonsterHp = minusMonsterHp(MainActivity.currentMonsterHp,MainActivity.attack)//血量降低
                showHp(MainActivity.currentMonsterHp,hp)//改變怪物血量
            }


            if (moneyHandFlag)//確認有無點金手
            {
                MainActivity.money += (MainActivity.sk2lv) * 3 //錢+點金手技能等級*3
                showMoney(money)
            }

            if (MainActivity.currentMonsterHp <= 0)//打死換怪階段
            {
                killMonsterProcess(monsters)
                if (bgCounter == 3){//換圖
                    randomImage(backgrounds,imageBg)
                    bgCounter = 0
                }
                else{
                    bgCounter += 1//地圖+1,數到3換圖
                }
            }//打死換怪階段結束
        }//攻擊結束

//-----------------攻擊-----------------------------------------------------------------

//-----------------技能-----------------------------------------------------------------
        skill1.setOnClickListener{  //點擊重擊
            showEffect(effectDelay,slash)
            MainActivity.currentMonsterHp = minusMonsterHp(MainActivity.currentMonsterHp, (MainActivity.attack)*MainActivity.sk1lv)//怪物血扣 重擊等級*atk
            showHp(MainActivity.currentMonsterHp,hp)//改變怪物血量
            if (MainActivity.currentMonsterHp <= 0)//打死換怪階段
            {
                killMonsterProcess(monsters)
                if (bgCounter == 3){//換圖
                    randomImage(backgrounds,imageBg)
                    bgCounter = 0
                }
                else
                    bgCounter += 1 //地圖+1,數到3換圖

            }//打死換怪階段結束
            skill1.isEnabled = false
            skill1.isVisible = true
            MainActivity.skill1Cd = 60  //重擊cd
            skill1.setTextColor(Color.parseColor("#5f000000"))
        }//重擊結束

        skill2.setOnClickListener{  //點擊點金手
            skill2time = 20  //點金手維持時間
            skill2.isEnabled = false
            skill2.isVisible = true
            MainActivity.skill2Cd = 180 //點金手cd
            skill2.setTextColor(Color.parseColor("#FFFF0000"))
        }//點金手結束

        skill3.setOnClickListener{  //點擊爆擊星
            skill3time = 30  //爆擊星維持時間
            skill3.isEnabled = false
            skill3.isVisible = true
            MainActivity.skill3Cd = 180  //爆擊星cd
            skill3.setTextColor(Color.parseColor("#FFFF0000"))
        }//爆擊星結束
//-----------------技能-----------------------------------------------------------------


        return v
    }

    private fun showHp(hp: Int , view: TextView){
        view.text = hp.toString()
    }
    private fun showMoney(view: TextView){
        view.text = MainActivity.money.toString()
    }
    private fun showEffect(delayTime:Long, imageView: ImageView){
        Handler().postDelayed({ //特效出現t秒後消失
            imageView.isVisible = false
        },delayTime)
        imageView.isVisible = true
    }
    @SuppressLint("SetTextI18n")
    private suspend fun refresh(monsters : ArrayList<String>, backgrounds : ArrayList<String>) {
        while (true){

            money.text = MainActivity.money.toString() //顯示目前金錢
            atk.text = MainActivity.attack.toString()
            tvdps.text = "DPS:${(MainActivity.help1 + MainActivity.help2 + MainActivity.help3) * 3}"
            MainActivity.playTime += 1
            skill1.isVisible = MainActivity.sk1lv > 0
            skill2.isVisible = MainActivity.sk2lv > 0
            skill3.isVisible = MainActivity.sk3lv > 0
            if(MainActivity.help1 > 0)
                help1.isVisible = true
            if(MainActivity.help2 > 0)
                help2.isVisible = true
            if(MainActivity.help3 > 0)
                help3.isVisible = true


            helperAttack(monsters,backgrounds)
            refreshSkill1()
            refreshSkill2()
            refreshSkill3()
            delay(1000)
        }
    }
    private fun minusMonsterHp(hp : Int,atk : Int) : Int{
        return hp.minus(atk)
    }
    @SuppressLint("SetTextI18n")
    private fun killMonsterProcess(monsters : ArrayList<String>){
        Handler().postDelayed({
            stage.text = "${stages}/10"
            randomImage(monsters,monster)
            monster.isVisible = true//隱藏怪物
            hp.isVisible = true//隱藏按鈕
        }, 300)
        soundPool?.play(soundDead, 1F, 1F, 0, 0, 1F)
        MainActivity.money += monsterMoney.toInt() //領取錢
        MainActivity.tMoney += monsterMoney.toInt()
        showMoney(money)//顯示當前金錢
        MainActivity.tKill += 1


        monster.isVisible = false //顯示按鈕跟怪物
        hp.isVisible = false

        if (stages>=9)//打10隻怪難度增加
        {
            monsterHp *= 1.2//新怪物血量增加
            MainActivity.currentMonsterHp = monsterHp.toInt()
            showHp(MainActivity.currentMonsterHp,hp)
            stages = 0
            MainActivity.area += 1
            monsterMoney *= 1.2
            area.text = "關卡${MainActivity.area}"
        }
        else
        {
            stages += 1
            monsterHp = (monsterHp*1)//沒換區血量維持
            MainActivity.currentMonsterHp = monsterHp.toInt()
            showHp(MainActivity.currentMonsterHp,hp)
        }
    }
    private fun randomImage(imgary : ArrayList<String> ,img : ImageView){
        val randoms = (0 until imgary.size).random() //隨機抽選圖片
        val uri = "@drawable/" + imgary[randoms]
        val imageResource = resources.getIdentifier(uri, null, activity!!.packageName)//圖片資料路徑
        img.setImageResource(imageResource) //換新圖
    }
    private fun helperAttack(monsters : ArrayList<String>,backgrounds : ArrayList<String>){
        MainActivity.currentMonsterHp = minusMonsterHp(MainActivity.currentMonsterHp,
            (MainActivity.help1 + MainActivity.help2 + MainActivity.help3)*3)//怪物扣血 三人等級總和*3
        showHp(MainActivity.currentMonsterHp,hp)//改變怪物血量

        if (MainActivity.currentMonsterHp <= 0)//打死換怪階段
        {
            killMonsterProcess(monsters)
            if (bgCounter == 3){//換圖
                randomImage(backgrounds,imageBg)
                bgCounter = 0
            }
            else{
                bgCounter += 1//地圖+1,數到3換圖
            }
        }//打死換怪階段結束
    }
    private fun refreshSkill1(){
        if(MainActivity.skill1Cd <= 0){   //cd跑完回復技能狀態
            skill1.isEnabled = true
            skill1.text = "重擊"
            skill1.setTextColor(Color.parseColor("#FFFFFFFF"))
        }
        else{//跑cd
            skill1.isEnabled = false
            skill1.isVisible = true
            MainActivity.skill1Cd -= 1
            skill1.text = MainActivity.skill1Cd.toString()
            skill1.setTextColor(Color.parseColor("#5f000000"))
        }
    }
    private fun refreshSkill2(){
        if(skill2time > 0){  //技能效果時間內
            moneyHandFlag = true
            skill2time -= 1
            skill2.text = skill2time.toString()
        }
        else if(MainActivity.skill2Cd > 0)
        {//跑cd
            skill2.isEnabled = false
            skill2.isVisible = true
            skill2.setTextColor(Color.parseColor("#5f000000"))
            moneyHandFlag = false
            MainActivity.skill2Cd -= 1
            skill2.text = MainActivity.skill2Cd.toString()
        }
        if(MainActivity.skill2Cd <= 0){  //cd跑完回復技能狀態
            skill2.setTextColor(Color.parseColor("#FFFFFFFF"))
            skill2.isEnabled = true
            skill2.text = "點金手"
        }
    }
    private fun refreshSkill3(){
        if(skill3time > 0){
            criticalHandFlag = true
            skill3time -= 1
            skill3.text = skill3time.toString()
        }
        else if(MainActivity.skill3Cd > 0)
        {//跑cd
            skill3.isEnabled = false
            skill3.isVisible = true
            skill3.setTextColor(Color.parseColor("#5f000000"))
            criticalHandFlag = false
            MainActivity.skill3Cd -= 1
            skill3.text = MainActivity.skill3Cd.toString()
        }
        if(MainActivity.skill3Cd <= 0){  //cd跑完回復技能狀態
            skill3.setTextColor(Color.parseColor("#FFFFFFFF"))
            skill3.isEnabled = true
            skill3.text = "爆擊星"
        }
    }

    override fun onDestroy() {
        scope.cancel()
        soundPool.release()
        super.onDestroy()

    }




    companion object {


        fun newInstance(text: String?): FirstFragment {

            val f = FirstFragment()
            val b = Bundle()
            b.putString("msg", text)
            f.arguments = b
            return f
        }
    }

}