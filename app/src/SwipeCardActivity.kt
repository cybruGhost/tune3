package com.example.swipfy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.swipfy.databinding.ActivitySwipeBinding
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import android.view.View

class SwipeActivity : AppCompatActivity(), CardStackListener {

    private lateinit var binding: ActivitySwipeBinding
    private lateinit var manager: CardStackLayoutManager
    private var nowArtist: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySwipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        manager = CardStackLayoutManager(this, this)
        binding.cardStackView.layoutManager = manager
    }

    // Required CardStackListener methods
    override fun onCardDragging(direction: Direction, ratio: Float) {}

    override fun onCardSwiped(direction: Direction) {}

    override fun onCardRewound() {}

    override fun onCardCanceled() {}

    override fun onCardAppeared(view: View, position: Int) {}

    override fun onCardDisappeared(view: View, position: Int) {}
}
