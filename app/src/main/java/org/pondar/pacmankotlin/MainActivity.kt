package org.pondar.pacmankotlin

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var moveTimer: Timer = Timer()
    private var enemyMoveTimer: Timer = Timer()
    private var gameTimer: Timer = Timer()
    private var game: Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)
        game = Game(this, pointsView, timerView, lvlView)

        game!!.running = false

        enemyMoveTimer.schedule(object : TimerTask() {
            override fun run() {
                enemyMoveTimerMethod()
            }

        }, 0, 1000)

        moveTimer.schedule(object : TimerTask() {
            override fun run() {
                moveTimerMethod()
            }

        }, 0, 10)

        gameTimer.schedule(object : TimerTask() {
            override fun run() {
                gameTimerMethod()
            }

        }, 0, 1000)


        game?.setGameView(gameView)
        gameView.setGame(game)
        game?.newGame()

        moveRight.setOnClickListener {
            game?.direction = game!!.right
        }
        moveLeft.setOnClickListener {
            game?.direction = game!!.left
        }
        moveDown.setOnClickListener {
            game?.direction = game!!.down
        }
        moveUp.setOnClickListener {
            game?.direction = game!!.up
        }

        startButton.setOnClickListener {
            game!!.running = true
        }
        stopButton.setOnClickListener {
            game!!.running = false
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
            Toast.makeText(this, "settings clicked", Toast.LENGTH_LONG).show()
            return true
        } else if (id == R.id.action_newGame) {
            Toast.makeText(this, "New Game clicked", Toast.LENGTH_LONG).show()
            game?.level = 1;
            game?.newGame()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun enemyMoveTimerMethod() {
        this.runOnUiThread(EnemyMoveTimerTick)
    }

    private fun moveTimerMethod() {
        this.runOnUiThread(moveTimerTick)
    }

    private fun gameTimerMethod() {
        this.runOnUiThread(gameTimerTick)
    }

    private val EnemyMoveTimerTick = Runnable {
        if (game!!.running) {

            for (Enemy in game!!.enemies) {
                Enemy.direction1 = 0
                Enemy.direction2 = 0
                Enemy.direction3 = 0
                Enemy.direction4 = 0

                var distx = Enemy.x - game!!.pacx
                var disty = Enemy.y - game!!.pacy
                if (distx < 0) {
                    Enemy.direction1 = 1
                }
                if (disty < 0) {
                    Enemy.direction2 = 1
                }
                if (distx > 0) {
                    Enemy.direction3 = 1
                }
                if (disty > 0) {
                    Enemy.direction4 = 1
                }
            }

            gameView?.invalidate()
        }
    }

    private val moveTimerTick = Runnable {
        if (game!!.running) {

            when (game!!.direction) {
                game!!.left -> game!!.movePacmanLeft(8)
                game!!.right -> game!!.movePacmanRight(8)
                game!!.up -> game!!.movePacmanUp(8)
                game!!.down -> game!!.movePacmanDown(8)
            }

            for (Enemy in game!!.enemies) {
                if (Enemy.direction1 == 1) {
                    Enemy.x = Enemy.x + 1 * game!!.level
                }
                if (Enemy.direction2 == 1) {
                    Enemy.y = Enemy.y + 1 * game!!.level
                }
                if (Enemy.direction3 == 1) {
                    Enemy.x = Enemy.x - 1 * game!!.level
                }
                if (Enemy.direction4 == 1) {
                    Enemy.y = Enemy.y - 1 * game!!.level
                }
            }

            gameView?.invalidate()
        }
    }
    private val gameTimerTick = Runnable {
        if (game!!.running) {
            game!!.counter--
            timerView.text = getString(R.string.timerValue, game!!.counter)
            if (game!!.counter <= 0) {
                Toast.makeText(this, "KAJ WAS UNABLE TO EAT ALL THE POPCORN IN TIME, YOU LOSE", Toast.LENGTH_LONG).show()
                game!!.newGame()
            }
        }
    }


}
