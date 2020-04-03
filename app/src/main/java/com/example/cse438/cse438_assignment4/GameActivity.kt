package com.example.cse438.cse438_assignment4

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.cse438.cse438_assignment4.util.CardRandomizer
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_gmae.*
import java.util.*
import kotlin.collections.ArrayList
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.isVisible


class GameActivity : AppCompatActivity(){

    private lateinit var auth: FirebaseAuth

    lateinit var db : FirebaseFirestore

    var currentUser = User("","",0,0,0,0);

    var oldChipValue = 0;

    var documentId = ""

    private lateinit var mDetector: GestureDetectorCompat
    private var height: Int = 0
    private var width: Int = 0
    private lateinit var faceView: View
    var PC1ID =0
    var PC2ID =0
    var pc3ID = 0
    var pc4ID = 0
    var pc5ID = 0
    var PC1Name = ""
    var PC2Name = ""
    var PC1Num = 0
    var PC2Num = 0
    var pc3Num = 0
    var pc5Name = ""
    var pc4Name = ""
    var pc3Name = ""
    var dc1ID =0
    var DC2ID =0
    var dc3ID =0
    var dc4ID =0
    var dc5ID =0
    var dc1Name =""
    var dc1Num = 0
    var DC2Name = ""
    var DC2Num =0
    var dc3Name = ""
    var dc3Num =0
    var dc4Name = ""
    var dc4Num =0
    var dc5Name = ""
    var dc5Num =0

    var totalPlayerNum = 0

    var totalDealerNum =0

    //amount of bets they put in
    var betamnt =0

    var winCounter =0
    var lostCounter =0
    var totalChip = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gmae)
        mDetector = GestureDetectorCompat(this,MyGestureListener())
        faceView = carddeck
        val metrics = this.resources.displayMetrics
        this.height = metrics.heightPixels
        this.width = metrics.widthPixels

        auth = FirebaseAuth.getInstance()

        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.setFirestoreSettings(settings)

        displayDb()




    }

    fun playerWinBust(){
        //hit a blackjack
        if(pc3Num==0){
            if(totalPlayerNum==21){
                Toast.makeText(this, "player win with a blackjack!", Toast.LENGTH_SHORT).show()
                //update the win
                winCounter=winCounter+1
                //update the chip
                totalChip = totalChip + betamnt
                updateDb(totalChip,true)
                System.out.println("------------------------------------------------------------------------"+1)
                startActivity(Intent(this,GameActivity::class.java))
            }
        }
        //player bust
        if(totalPlayerNum>21){
            Toast.makeText(this, "player bust dealer win", Toast.LENGTH_SHORT).show()
            lostCounter=lostCounter+1
            totalChip = totalChip - betamnt
            if(totalChip==0){
                Toast.makeText(this, "player run out of chips, automatically add to 100", Toast.LENGTH_LONG).show()
                totalChip =100
            }
            updateDb(totalChip,false)
            System.out.println("------------------------------------------------------------------------"+2)

            startActivity(Intent(this,GameActivity::class.java))

        }
    }

    fun winlossDeter(){
        //dealer bust
        if(totalDealerNum>21){
            Toast.makeText(this,"player win",Toast.LENGTH_SHORT).show()
            winCounter=winCounter+1
            totalChip = totalChip + betamnt
            updateDb(totalChip,true)
            System.out.println("------------------------------------------------------------------------"+3)

            startActivity(Intent(this, GameActivity::class.java))
        }
        else {
            //player higher
            if (totalPlayerNum > totalDealerNum) {
                Toast.makeText(this, "player win", Toast.LENGTH_SHORT).show()
                winCounter=winCounter+1
                totalChip = totalChip + betamnt
                updateDb(totalChip,true)
                System.out.println("------------------------------------------------------------------------"+4)

                startActivity(Intent(this, GameActivity::class.java))
            }else{
                //tie
                if (totalPlayerNum == totalDealerNum) {
                    Toast.makeText(this, "game tie", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, GameActivity::class.java))
                }
                //dealer higher
                else {
                    Toast.makeText(this, "dealer win", Toast.LENGTH_SHORT).show()
                    lostCounter=lostCounter+1
                    totalChip = totalChip - betamnt
                    if(totalChip==0){
                        Toast.makeText(this, "player run out of chips, automatically add to 100", Toast.LENGTH_LONG).show()
                        totalChip =100
                    }
                    updateDb(totalChip,false)
                    System.out.println("------------------------------------------------------------------------"+5)

                    startActivity(Intent(this, GameActivity::class.java))
                }
            }

        }
    }

    fun getTotalPlayerCount(): Int{
        var pc4Num =0
        var pc5Num =0
        if(pc3Name.isNullOrEmpty()!=true){
            pc3Num = getCardNum(pc3Name)
        }
        if(pc4Name.isNullOrEmpty()!=true){
             pc4Num = getCardNum(pc4Name)
        }

        if(pc5Name.isNullOrEmpty()!=true){
            pc5Num = getCardNum(pc5Name)
        }
        var total = PC1Num + PC2Num+ pc3Num + pc4Num + pc5Num
        playerCountBox.setText(total.toString())
        return total


    }

    fun getTotalDealerCoutn(): Int{
        var dc1Num =0
        var total =0
        dc1Num = getCardNum(dc1Name)

        if(dc3Name.isNullOrEmpty()!=true){
            dc3Num=getCardNum(dc3Name)
        }
        if(dc4Name.isNullOrEmpty()!=true){
            dc4Num=getCardNum(dc4Name)
        }
        if(dc4Name.isNullOrEmpty()!=true){
            dc4Num=getCardNum(dc4Name)
        }

        total = dc1Num + DC2Num + dc3Num +dc4Num +dc5Num
        dealerCountBox.setText(total.toString())
        return total
    }


    fun getCardNum(pictureName:String): Int{
        var lastChar = pictureName.get(pictureName.lastIndex)
        var vaguereturn = 0
        var cardCount = 0
        if(lastChar=='e'){
            cardCount = 11
            return cardCount
        }
        if(lastChar=='k'){
            cardCount = 10
            return cardCount
        }
        if(lastChar=='g'){
            cardCount = 10
            return cardCount
        }
        if(lastChar=='n'){
            cardCount = 10
            return cardCount
        }

        if(lastChar.toString().toInt()==0){
            cardCount = 10
            return cardCount
        }
        if(lastChar.toInt()!=10){
            cardCount = lastChar.toString().toInt()
            return cardCount
        }
        return vaguereturn
    }


    fun moveTo(targetX: Float, targetY: Float) {

        val animSetXY = AnimatorSet()

        val x = ObjectAnimator.ofFloat(
            faceView,
            "translationX",
            faceView.translationX,
            targetX
        )

        val y = ObjectAnimator.ofFloat(
            faceView,
            "translationY",
            faceView.translationY,
            targetY
        )

        animSetXY.playTogether(x, y)
        animSetXY.duration = 300
        animSetXY.start()
    }




    private inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {

        private var swipedistance = 100

        override fun onDoubleTap(e: MotionEvent?): Boolean {

            if(playerC4.drawable!=null){
                if(playerC5.drawable==null){
                    pc5ID = getRand()
                    pc5Name = resources.getResourceEntryName(pc5ID)
                    playerC5.setImageResource(pc5ID)
                    totalPlayerNum = getTotalPlayerCount()
                    System.out.println("player count is " + totalPlayerNum)
                    playerWinBust()
                }
            }
            if(playerC3.drawable!=null){
                if(playerC4.drawable==null){
                    pc4ID=getRand()
                    pc4Name = resources.getResourceEntryName(pc4ID)
                    playerC4.setImageResource(pc4ID)
                    totalPlayerNum  = getTotalPlayerCount()
                    System.out.println("player count is " + totalPlayerNum)
                    playerWinBust()
                }
            }
            if(playerC3.drawable==null){
                pc3ID = getRand()
                pc3Name = resources.getResourceEntryName(pc3ID)
                playerC3.setImageResource(pc3ID)
                totalPlayerNum = getTotalPlayerCount()
                System.out.println("player count is " + totalPlayerNum)
                playerWinBust()
            }
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {

            totalPlayerNum = getTotalPlayerCount()

            System.out.println("player count is " + totalPlayerNum)
            if(e2!!.x - e1!!.x>swipedistance){
                dc1ID = getRand()
                dc1Name = resources.getResourceEntryName(dc1ID)
                dealerC1.setImageResource(dc1ID)
                totalDealerNum = getTotalDealerCoutn()
                System.out.println("Dealer count is " + totalDealerNum)
                //if less than 17 dealer needs to hit
                if(totalDealerNum<17){
                    dc3ID=getRand()
                    dc3Name = resources.getResourceEntryName(dc3ID)
                    dealerC3.setImageResource(dc3ID)
                    totalDealerNum = getTotalDealerCoutn()
                    System.out.println("Dealer count is " + totalDealerNum)
                    if(totalDealerNum<17){
                        dc4ID=getRand()
                        dc4Name = resources.getResourceEntryName(dc4ID)
                        dealerC4.setImageResource(dc4ID)
                        totalDealerNum = getTotalDealerCoutn()
                        System.out.println("Dealer count is " + totalDealerNum)
                    }
                    if(totalDealerNum<17){
                        dc5ID=getRand()
                        dc5Name = resources.getResourceEntryName(dc5ID)
                        dealerC5.setImageResource(dc5ID)
                        totalDealerNum = getTotalDealerCoutn()
                        System.out.println("Dealer count is " + totalDealerNum)
                        //winlossDeter()

                    }
                    //winlossDeter()
                }
                winlossDeter()
            }
            return true
        }


    }



    override fun onTouchEvent(event: MotionEvent) : Boolean {
        mDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }


        override fun onStart() {
        super.onStart()

        signoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
        }

        leaderBoardButton.setOnClickListener {
            startActivity(Intent(this,LeaderBoardActivity::class.java))
        }

            //a save instance calculator, pull out the chips that were bet.
        placeBet.setOnClickListener{
            if(betA.text.toString().toInt()<0){
                Toast.makeText(this, "player bets a negative amount will adjust to 0!", Toast.LENGTH_SHORT).show()

                betamnt=0
                System.out.println("the first" + betamnt)
            }

            if(betA.text.toString().toInt()>=0){
                if(betA.text.toString().toInt()>totalChip){
                    Toast.makeText(this, "player bets an amount above his total will adjust to 0!", Toast.LENGTH_SHORT).show()
                    betamnt=0
                }
                if(betA.text.toString().toInt()<=totalChip){

                    betamnt = betA.text.toString().toInt()
                    Toast.makeText(this, "player bets "+ betamnt, Toast.LENGTH_SHORT).show()
                }
            }
            System.out.println("tHE FINAL BET" + betamnt)
        }

        var DC2 = dealerC2
        var PC1 = playerC1
        var PC2 = playerC2

        PC1ID = getRand()
        PC2ID = getRand()

        DC2ID = getRand()

        DC2Name = resources.getResourceEntryName(DC2ID)
        PC1Name = resources.getResourceEntryName(PC1ID)
        PC2Name = resources.getResourceEntryName(PC2ID)

        PC1Num = getCardNum(PC1Name)
        PC2Num = getCardNum(PC2Name)
        DC2Num = getCardNum(DC2Name)


        totalPlayerNum = getTotalPlayerCount()
        System.out.println("player count is " + totalPlayerNum)

        playerWinBust()

        PC1.setImageResource(PC1ID)
        PC2.setImageResource(PC2ID)
        DC2.setImageResource(DC2ID)


    }

    private fun getRand(): Int{
        val randomizer: CardRandomizer = CardRandomizer()
        var cardList:ArrayList<Int> = randomizer.getIDs(this) as ArrayList<Int>
        val rand: Random = Random()
        val r: Int = rand.nextInt(cardList.size)
        val id:Int = cardList.get(r)
        val name:String = resources.getResourceEntryName(id)
        return id
    }



    private fun updateDb(bet: Int, result: Boolean){
        val data1 = hashMapOf(
            "userChips" to bet,
            "userEmail" to currentUser.userEmail,
            "userName" to currentUser.userName,
            "wins" to currentUser.wins,
            "losts" to currentUser.losts,
            "rank" to 0
        )
        if(result){
            currentUser.wins = currentUser.wins + 1;
            System.out.println("------------------------------------------------------------------ win" + currentUser.wins)
            db.collection("Users1").document(documentId)
                .set(hashMapOf(
                    "userChips" to bet,
                    "userEmail" to currentUser.userEmail,
                    "userName" to currentUser.userName,
                    "wins" to currentUser.wins,
                    "losts" to currentUser.losts,
                    "rank" to 0
                ))
        }else {
            if(!result){
                currentUser.losts = currentUser.losts + 1;
                System.out.println("------------------------------------------------------------------ lost" + currentUser.losts)
                db.collection("Users1").document(documentId)
                    .set(hashMapOf(
                        "userChips" to bet,
                        "userEmail" to currentUser.userEmail,
                        "userName" to currentUser.userName,
                        "wins" to currentUser.wins,
                        "losts" to currentUser.losts,
                        "rank" to 0
                    ))
            }
        }



    }

    private fun displayDb(){
        db.collection("Users1")
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->

                if (task.isSuccessful) {
                    for (document in task.result!!) {
//                        System.out.println("--------------------------------"+document.get("userEmail").toString())
                        //                       System.out.println("-------------------------------------------"+auth.currentUser!!.email.toString())

                        if(document.get("userEmail").toString() == auth.currentUser!!.email.toString()){
                            documentId = document.id
                            currentUser.losts = document.get("losts").toString().toInt()
                            currentUser.wins = document.get("wins").toString().toInt()
                            currentUser.userChips = document.get("userChips").toString().toInt()
                            totalChip = document.get("userChips").toString().toInt()
                            currentUser.userName = document.get("userName").toString()
                            currentUser.userEmail = document.get("userEmail").toString()
                            //System.out.println("--------------------------------"+documentId)
                            //System.out.println("--------------------------------"+document.toString())
                            playername.text = currentUser.userName
                            wins.text = "Win: " + currentUser.wins.toString()
                            losts.text = "Lost: " + currentUser.losts.toString()
                            chips.text = "Chips: " + currentUser.userChips.toString()
                        }
                    }

                } else {
                    println("failed to get data")
                }
            })
    }

}
