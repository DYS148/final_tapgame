package com.example.final_project

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy

/*  text _id : 0
    text password : 1
    text logged : 2
    text attack : 3
    REAL coin : 4
    INTEGER skill1Level : 5
    INTEGER skill2Level : 6
    INTEGER skill3Level : 7
    REAL killMonster : 8
    REAL killBoss : 9
    REAL totalCoin : 10
    INTEGER helper1Level : 11
    INTEGER helper2Level : 12
    INTEGER helper3Level : 13
    INTEGER area : 14
    INTEGER currentMonsterHp : 15
    TEXT totalTime : 16)
*/

class MainActivity : AppCompatActivity() {
    // The pager adapter, which provides the pages to the view pager widget.
    private var pagerAdapter: FragmentStateAdapter? = null



    // Arrey of strings FOR TABS TITLES
    private val titles = arrayOf("戰鬥", "技能", "商店", "更多")
    fun showToast(text: String) =
        Toast.makeText(this,text, Toast.LENGTH_SHORT).show()
    // tab titles

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        mediaPlayer= MediaPlayer.create(this,R.raw.music)
        mediaPlayer.start()
        mediaPlayer.isLooping=true
        dbrw = MyDBHelper(this).writableDatabase
        setContentView(R.layout.activity_main)
        id = intent.getStringExtra("id").toString()
        cursor = dbrw.rawQuery("SELECT * FROM myTable WHERE _id LIKE '%${id}%'",null)
        cursor.moveToFirst()
        attack = cursor.getInt(3)
        money = cursor.getString(4).toLong()
        sk1lv = cursor.getInt(5)
        sk2lv = cursor.getInt(6)
        sk3lv = cursor.getInt(7)
        tKill = cursor.getInt(8)
        tMoney = cursor.getLong(10)
        help1 = cursor.getInt(11)
        help2 = cursor.getInt(12)
        help3 = cursor.getInt(13)
        area = cursor.getInt(14)
        currentMonsterHp = cursor.getInt(15)
        skill1Cd = cursor.getInt(16)
        skill2Cd = cursor.getInt(17)
        skill3Cd = cursor.getInt(18)
        playTime = cursor.getLong(19)





        viewPager = findViewById(R.id.mypager)
        pagerAdapter = MyPagerAdapter(this)
        viewPager?.adapter = pagerAdapter
        viewPager?.offscreenPageLimit = 3
        //inflating tab layout
        val tabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
        //displaying tabs
        TabLayoutMediator(tabLayout, viewPager!!, TabConfigurationStrategy { tab: TabLayout.Tab, position: Int -> tab.text = titles[position] }).attach()
    }

    private inner class MyPagerAdapter(fa: FragmentActivity?) : FragmentStateAdapter(fa!!) {
        override fun createFragment(pos: Int): Fragment {
            return when (pos) {
                0 -> {
                    FirstFragment.newInstance("fragment 1")
                }
                1 -> {
                    SecondFragment.newInstance("fragment 2")
                }
                2 -> {
                    ThirdFragment.newInstance("fragment 3")
                }
                3 -> {
                    FourthFragment.newInstance("fragment 4")
                }
                else -> FirstFragment.newInstance("fragment 1, Default")
            }
        }
        override fun getItemCount(): Int {
            return NUM_PAGES
        }
    }

    override fun onBackPressed() {
        if (viewPager?.currentItem == 0) {
// If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.d
            super.onBackPressed()
        } else {
// Otherwise, select the previous step.
            viewPager?.currentItem = (viewPager?.currentItem)!!.minus(1)
        }
    }

    companion object {
        private const val NUM_PAGES = 4
        lateinit var cursor : Cursor
        lateinit var mediaPlayer: MediaPlayer
        lateinit var id : String
        var sk1lv : Int = 0
        var sk2lv : Int = 0
        var sk3lv : Int = 0
        var tKill : Int = 0
        var tMoney : Long = 0
        var help1 : Int = 0
        var help2 : Int = 0
        var help3 : Int = 0
        var area : Int = 0
        var playTime : Long = 0
        var attack : Int = 0
        var money : Long = 0
        var currentMonsterHp : Int = 0
        var skill1Cd : Int = 0
        var skill2Cd : Int = 0
        var skill3Cd : Int = 0

        lateinit var dbrw : SQLiteDatabase
        //The pager widget, which handles animation and allows swiping horizontally to access previous and next wizard steps.
        var viewPager: ViewPager2? = null

    }
}