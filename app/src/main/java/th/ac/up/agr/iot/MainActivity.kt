package th.ac.up.agr.iot

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Switch
import com.google.firebase.database.*
//import com.google.firebase.database.*

import kotlinx.android.synthetic.main.activity_main.*
import java.util.HashMap

class MainActivity : AppCompatActivity() {

    val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
    val time: DatabaseReference = reference.child("tree").child("time")

    val message: DatabaseReference = reference.child("tree").child("MessageBox")
    val user: DatabaseReference = message.child("user")
    val mess: DatabaseReference = message.child("message")

    var data = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val alertDialog = AlertDialog.Builder(this@MainActivity).create()
        alertDialog.setTitle("กำลังโหลดข้อมูล")
        alertDialog.setMessage("กรุณารอสักครู่แอปพลิชั่นนี้กำลังโหลดข้อมูลจาก Firebase")
        alertDialog.show()

        cardView.setOnClickListener {
            setTime()
        }

        cardViewChat.setOnClickListener {
            mInputStatus()
        }

        inputConfirm.setOnClickListener {

            val u = user_input_edittext.text.toString()
            val m = message_input_edittext.text.toString()

            user.setValue(u)
            mess.setValue(m)

            user_input_edittext.text.clear()
            message_input_edittext.text.clear()

            inputBox.visibility = View.GONE
        }

        inputCancel.setOnClickListener {
            inputBox.visibility = View.GONE
            user_input_edittext.text.clear()
            message_input_edittext.text.clear()
        }

        time.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                val errorDialog = AlertDialog.Builder(this@MainActivity).create()
                errorDialog.setTitle("แจ้งเตือน")
                errorDialog.setMessage("แอปพลิชั่นนี้ไม่สามารถโหลดข้อมูลจาก Firebase ได้ กรุณาเช็คการเชื่อมต่ออินเทอร์เน็ต")
                errorDialog.show()
            }

            override fun onDataChange(p0: DataSnapshot?) {
                //val data :String = p0?.value as String
                data = p0?.value as Boolean
                getTime()
                alertDialog.cancel()
            }

        })

        message.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                val errorDialog = AlertDialog.Builder(this@MainActivity).create()
                errorDialog.setTitle("แจ้งเตือน")
                errorDialog.setMessage("แอปพลิชั่นนี้ไม่สามารถโหลดข้อมูลจาก Firebase ได้ กรุณาเช็คการเชื่อมต่ออินเทอร์เน็ต")
                errorDialog.show()
            }

            override fun onDataChange(p0: DataSnapshot?) {
                //val data :String = p0?.value as String
                //val user = p0?.value as String

                val c = MessageBox::class.java

                val s = p0?.child("user")?.value.toString()
                val message = MessageBox(p0?.child("user")?.value.toString(),p0?.child("message")?.value.toString())


                mBox.visibility = View.VISIBLE

                mCast.text = "ผู้ส่ง : ${message.user}"
                mText.text = message.message



            }

        })

    }

    private fun mInputStatus(){
        inputBox.visibility = View.VISIBLE

    }

    private fun setTime() {
        if (data) {
            time.setValue(false)
        } else {
            time.setValue(true)
        }
        getTime()
    }

    private fun getTime() {
        if (data) {
            timeText.text = "กลางวัน"
            background.setImageResource(R.drawable.b2)
            timeImage.setImageResource(R.drawable.sun)

        } else {
            timeText.text = "กลางคืน"
            background.setImageResource(R.drawable.b1)
            timeImage.setImageResource(R.drawable.moon)
        }
    }

}