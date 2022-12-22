package com.mikhaellopez.circularprogressbarsample

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import com.larswerkman.lobsterpicker.OnColorListener
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val circularProgressBar = findViewById<CircularProgressBar>(R.id.circularProgressBar)
        // Update circularProgressBar
        findViewById<SeekBar>(R.id.seekBarProgress).onProgressChanged {
            circularProgressBar.progress = it
        }
        findViewById<SeekBar>(R.id.seekBarStartAngle).onProgressChanged {
            circularProgressBar.startAngle = it
        }
        findViewById<SeekBar>(R.id.seekBarStrokeWidth).onProgressChanged {
            circularProgressBar.progressBarWidth = it
        }
        findViewById<SeekBar>(R.id.seekBarBackgroundStrokeWidth).onProgressChanged {
            circularProgressBar.backgroundProgressBarWidth = it
        }
        findViewById<LobsterShadeSlider>(R.id.shadeSlider).onColorChanged {
            circularProgressBar.progressBarColor = it
            circularProgressBar.backgroundProgressBarColor = adjustAlpha(it, 0.3f)
        }
        findViewById<SwitchCompat>(R.id.switchRoundBorder).onCheckedChange {
            circularProgressBar.roundBorder = it
        }
        findViewById<SwitchCompat>(R.id.switchProgressDirection).onCheckedChange {
            circularProgressBar.progressDirection =
                if (it) CircularProgressBar.ProgressDirection.TO_RIGHT
                else CircularProgressBar.ProgressDirection.TO_LEFT
        }

        // Indeterminate Mode
        val switchIndeterminateMode = findViewById<SwitchCompat>(R.id.switchIndeterminateMode)
        switchIndeterminateMode.onCheckedChange { circularProgressBar.indeterminateMode = it }
        circularProgressBar.onIndeterminateModeChangeListener =
            { switchIndeterminateMode.isChecked = it }

        restartAnimation()
        findViewById<Button>(R.id.btnRestart).setOnClickListener {
            restartAnimation()
        }

//        restartAnimation()


        circularProgressBar.progressBarWidth = 20f
        circularProgressBar.backgroundProgressBarWidth = 5f
        circularProgressBar.progress = 0f
        circularProgressBar.progressBarColor = resources.getColor(R.color.activity_color)
        circularProgressBar.backgroundProgressBarColor = resources.getColor(R.color.shadow_color)
        circularProgressBar.shadowColorStart = resources.getColor(R.color.shadow_color)
        circularProgressBar.shadowColorEnd = resources.getColor(R.color.shadow_color)

    }

    private fun restartAnimation() {

        val circularProgressBar = findViewById<CircularProgressBar>(R.id.circularProgressBar)

        circularProgressBar.progressBarWidth = 20f
        circularProgressBar.startAngle = 0f
        circularProgressBar.startWithFadedColor = false
        circularProgressBar.backgroundProgressBarWidth = 5f
//        circularProgressBar.backgroundProgressBarColor = resources.getColor(R.color.shadow_color)
//        circularProgressBar.progressBarColor = resources.getColor(R.color.activity_color)

        // Set Init progress with animation
        circularProgressBar.setProgressWithAnimation(100f, 1000)
        Handler().postDelayed({
            circularProgressBar.backgroundProgressBarColor = resources.getColor(R.color.activity_color)
            circularProgressBar.progressBarWidth = 20f
            circularProgressBar.backgroundProgressBarWidth = 20f
            circularProgressBar.progress = 0.5f
            circularProgressBar.startWithFadedColor = false
            circularProgressBar.setProgressWithAnimation(26f, 500)
            Handler().postDelayed({
                circularProgressBar.startWithFadedColor = true
                circularProgressBar.progressFadeEffectStartColorAlpha = 0.0f
                circularProgressBar.progressFadeStartColorPosition = 0.25f
                circularProgressBar.progressFadeEndColorPosition = 0.45f

                circularProgressBar.startAngle = 90f
                circularProgressBar.progressBarColor = resources.getColor(R.color.red_color)
                circularProgressBar.progress = 10f
                circularProgressBar.progressBarColorEnd = resources.getColor(R.color.transparent)
                circularProgressBar.shadowColorEnd = resources.getColor(R.color.neon_body)
                circularProgressBar.progressBarColorStart = resources.getColor(R.color.red_color)
                circularProgressBar.setProgressWithAnimation(95f, 800)

                Handler().postDelayed({
                    circularProgressBar.progressBarColorEnd = null
                    circularProgressBar.shadowColorEnd = resources.getColor(R.color.shadow_color)
                    circularProgressBar.backgroundProgressBarColor = resources.getColor(R.color.red_color)
                    circularProgressBar.progressBarColorStart = null
                    circularProgressBar.progressBarColorEnd = null
                    circularProgressBar.progressBarWidth = 20f
                    circularProgressBar.backgroundProgressBarWidth = 20f
                    circularProgressBar.progress = 0.5f
                    circularProgressBar.setProgressWithAnimation(5f, 300)}, 750)
            }, 450)
        }, 800)
        circularProgressBar.progressBarColor = resources.getColor(R.color.activity_color)
        circularProgressBar.backgroundProgressBarColor = resources.getColor(R.color.shadow_color)
        circularProgressBar.shadowColorStart = resources.getColor(R.color.shadow_color)
        circularProgressBar.shadowColorEnd = resources.getColor(R.color.shadow_color)

    }

    //region Extensions
    private fun SeekBar.onProgressChanged(onProgressChanged: (Float) -> Unit) {
        setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                onProgressChanged(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Nothing
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Nothing
            }
        })
    }

    private fun LobsterShadeSlider.onColorChanged(onColorChanged: (Int) -> Unit) {
        addOnColorListener(object : OnColorListener {
            override fun onColorChanged(color: Int) {
                onColorChanged(color)
            }

            override fun onColorSelected(color: Int) {
                // Nothing
            }
        })
    }

    private fun SwitchCompat.onCheckedChange(onCheckedChange: (Boolean) -> Unit) {
        setOnCheckedChangeListener { _, isChecked -> onCheckedChange(isChecked) }
    }
    //endregion

    /**
     * Transparent the given progressBarColor by the factor
     * The more the factor closer to zero the more the progressBarColor gets transparent
     *
     * @param color  The progressBarColor to transparent
     * @param factor 1.0f to 0.0f
     * @return int - A transplanted progressBarColor
     */
    private fun adjustAlpha(color: Int, factor: Float): Int {
        val alpha = (Color.alpha(color) * factor).roundToInt()
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

}