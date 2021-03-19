package org.pondar.pacmankotlin

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text
import java.lang.Math.pow
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random.Default.nextInt

//Fix points TODO
/**
 *
 * This class should contain all your game logic
 */

class Game(private var context: Context, view: TextView, view2: TextView, view3: TextView) {

    private var pointsView: TextView = view
    private var timerView: TextView = view2
    private var lvlView: TextView = view3
    private var points: Int = 0
    var level: Int = 1;
    var running = true
    var counter: Int = 60

    var pacBitmap: Bitmap
    var coinBitmap: Bitmap
    var andreaBitmap: Bitmap
    var pacx = 0
    var pacy = 0

    var direction: Int = 0;
    val left: Int = 1;
    val right: Int = 2;
    val up: Int = 3;
    val down: Int = 4;


    var coinsInitialized = false
    var enemiesInitialized = false

    var coins = ArrayList<GoldCoin>()
    var enemies = ArrayList<Enemy>()


    //a reference to the gameview
    private var gameView: GameView? = null
    private var h: Int = 0
    private var w: Int = 0 //height and width of screen


    init {
        lvlView.text = "${context.resources.getString(R.string.lvl)} $level"
        pacBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.kaj)
        coinBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.popcorn)
        andreaBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.andrea)
    }

    fun setGameView(view: GameView) {
        this.gameView = view
    }

    fun initializeGoldcoins() {
        coins.clear()
        for (i in 0..9) {
            val rndx = (100..w - 200).random();
            val rndy = (100..h - 200).random();
            var coin = GoldCoin(rndx, rndy, false);

            coins.add(coin);
        }
        coinsInitialized = true
    }

    fun initializeEnemies() {
        enemies.clear()
        for (i in 0..1) {
            val rndx = (100..w - 200).random();
            val rndy = (100..h - 200).random();
            var enemy = Enemy(rndx, rndy, true, 0,0,0,0);

            enemies.add(enemy);
        }
        enemiesInitialized = true
    }

    fun newGame() {
        pacx = 50
        pacy = 400
        coinsInitialized = false
        enemiesInitialized = false
        points = 0
        direction = 0
        counter = 60
        running = false
        pointsView.text = "${context.resources.getString(R.string.points)} $points"
        timerView.text = context.resources.getString(R.string.timerValue, 60)
        gameView!!.invalidate() //redraw screen
    }

    fun setSize(h: Int, w: Int) {
        this.h = h
        this.w = w
    }

    fun movePacmanRight(pixels: Int) {
        if (pacx + pixels + pacBitmap.width < w) {
            pacx = pacx + pixels
        }
        gameView!!.invalidate()
        doCollisionCheck()
    }

    fun movePacmanLeft(pixels: Int) {
        if (pacx - pixels > 0) {
            pacx = pacx - pixels
        }
        gameView!!.invalidate()
        doCollisionCheck()
    }

    fun movePacmanUp(pixels: Int) {
        if (pacy - pixels > 0) {
            pacy = pacy - pixels
        }
        gameView!!.invalidate()
        doCollisionCheck()
    }

    fun movePacmanDown(pixels: Int) {
        if (pacy + pixels + pacBitmap.height < h) {
            pacy = pacy + pixels
        }
        gameView!!.invalidate()
        doCollisionCheck()
    }

    fun distance(x1: Int, y1: Int, x2: Int, y2: Int): Float {
        var dist: Double?
        var a = ((x2 - x1)).toDouble()
        var b = ((y2 - y1)).toDouble()
        dist = sqrt(a.pow(2) + b.pow(2))
        return dist.toFloat()
    }

    fun doCollisionCheck() {
        var untakenCoins = 0;
        for (GoldCoin in coins) {
            if (GoldCoin.taken == false) {
                if (distance(pacx, pacy, GoldCoin.x, GoldCoin.y) < 100) {
                    GoldCoin.taken = true
                    points += 1
                    pointsView.text = "${context.resources.getString(R.string.points)} $points"
                }
            }
        }
        for (GoldCoin in coins) {
            if (GoldCoin.taken == false) {
                untakenCoins += 1
            }
        }
        if (untakenCoins <= 0) {
            Toast.makeText(context, "KAJ IS NO LONGER HUNGRY, YOU HAVE WON FOR NOW BUT ANDREA IS GETTING FASTER AND KAJ WILL BE HUNGRY ONCE AGAIN", Toast.LENGTH_LONG).show()
            level += 1
            lvlView.text = "${context.resources.getString(R.string.lvl)} $level"
            newGame()
        }
        for (Enemy in enemies) {
            if (distance(pacx, pacy, Enemy.x, Enemy.y) < 50) {
                level = 1
                newGame()
                Toast.makeText(context, "ANDREA HAS PUT A STOP TO YOUR RAMPAGE!", Toast.LENGTH_LONG).show()
            }
        }
    }


}