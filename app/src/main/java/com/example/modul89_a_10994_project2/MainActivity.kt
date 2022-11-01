package com.example.modul89_a_10994_project2



import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.modul89_a_10994_project2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), SensorEventListener {
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"



    private lateinit var sensorManager : SensorManager
    private lateinit var square : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        setUpSensorStuff()

    }

    private fun setUpSensorStuff() {
        // Keeps phone in light mode
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO)
        square = findViewById(R.id.tv_square)
//        setUpSensorStuff()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // Specify the sensor you want to listen to
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val sides = event.values[0]
            val upDown = event.values[1]

            square.apply {
                rotationX = upDown * 3f
                rotationY = sides * 3f
                rotation = -sides
                translationX = sides * -10
                translationY = upDown * 10
            }

            val color = if (upDown.toInt() == 0 && sides.toInt() == 0) Color.GREEN else Color.RED
            square.setBackgroundColor(color)
            square.text = "up/down ${upDown.toInt()}\nleft/right${sides.toInt()}"

            if(upDown.toInt() == 2 && sides.toInt() ==2){
                // pendingIntent is an intent for future use i.e after
                // the notification is clicked, this intent will come into action


                // FLAG_UPDATE_CURRENT specifies that if a previous
                // PendingIntent already exists, then the current one
                // will update it with the latest intent
                // 0 is the request code, using it later with the
                // same method again will get back the same pending
                // intent for future reference
                // intent passed here is to our afterNotification class
                val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

                // RemoteViews are used to use the content of
                // some different layout apart from the current activity layout

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                    notificationChannel.enableLights(true)
                    notificationChannel.lightColor = Color.GREEN
                    notificationChannel.enableVibration(false)
                    notificationManager.createNotificationChannel(notificationChannel)

                    builder = Notification.Builder(this, channelId)
                        .setContentTitle("Selamat")
                        .setContentText("Selamat anda sudah berhasil mengerjakan Modul 8 dan 9 ")

                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                        .setContentIntent(pendingIntent)
                } else {

                    builder = Notification.Builder(this)
                        .setContentTitle("Selamat")
                        .setContentText("Selamat anda sudah berhasil mengerjakan Modul 8 dan 9 ")

                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                        .setContentIntent(pendingIntent)
                }
                notificationManager.notify(1234, builder.build())
            }


        }
    }

    override fun onAccuracyChanged(sensor:Sensor?, accuracy:Int){
        return
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }

//    private fun createNotificationChannel()
//    {
//        val name = "Notification Title"
//        val descriptionText = "Notification Description"
//
//        val channel1 = NotificationChannel(CHANNEL_ID_1, name , NotificationManager.IMPORTANCE_DEFAULT).apply {
//            description = descriptionText
//        }
//
//        val  channel2 = NotificationChannel(CHANNEL_ID_2, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
//            description = descriptionText
//        }
//
//        val notificationManager : NotificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(channel1)
//        notificationManager.createNotificationChannel(channel2)
//    }

//    private fun sendNotification1()
//    {
//        val intent : Intent = Intent(this, MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//
//        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0,intent,0)
//
//
//        val builder = NotificationCompat.Builder(this,CHANNEL_ID_1)
//            .setContentTitle("Selamat")
//            .setContentText("Selamat anda sudah berhasil mengerjakan Modul 8 dan 9 ")
//
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//        with(NotificationManagerCompat.from(this))
//        {
//            notify(notificationId1, builder.build())
//        }
//    }
//
//    private fun sendNotification2()
//    {
//        val builder = NotificationCompat.Builder(this,CHANNEL_ID_1)
//            .setContentTitle("Selamat")
//            .setContentText("Selamat anda sudah berhasil mengerjakan Modul 8 dan 9 ")
//            .setPriority(NotificationCompat.PRIORITY_LOW)
//
//        with(NotificationManagerCompat.from(this))
//        {
//            notify(notificationId2, builder.build())
//        }
//    }


}