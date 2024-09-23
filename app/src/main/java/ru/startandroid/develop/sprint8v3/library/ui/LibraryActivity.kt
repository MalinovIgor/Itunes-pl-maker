//package ru.startandroid.develop.sprint8v3.library.ui
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import com.google.android.material.tabs.TabLayoutMediator
//import ru.startandroid.develop.sprint8v3.R
//import ru.startandroid.develop.sprint8v3.databinding.ActivityLibraryBinding
//import ru.startandroid.develop.sprint8v3.library.ui.fragment.LibraryPagerAdapter
//
//class LibraryActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityLibraryBinding
//    private lateinit var tabMediator: TabLayoutMediator
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityLibraryBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.backArrow.setOnClickListener {
//            onBackPressedDispatcher.onBackPressed()
//        }
//
//        binding.viewPager.adapter = LibraryPagerAdapter(
//            fragmentManager = supportFragmentManager,
//            lifecycle = lifecycle,
//        )
//
//        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
//            when (position) {
//                0 -> tab.text = getString(R.string.favorites)
//                1 -> tab.text = getString(R.string.playlists)
//            }
//        }
//        tabMediator.attach()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        tabMediator.detach()
//    }
//}