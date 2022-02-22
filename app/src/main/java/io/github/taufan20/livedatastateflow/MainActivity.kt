package io.github.taufan20.livedatastateflow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import io.github.taufan20.livedatastateflow.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()

        viewModel.run {
            liveDataMessage.observe(this@MainActivity, { message ->
                binding.txtLivedata.text = message
            })

        }

        lifecycleScope.launchWhenStarted {
            viewModel.stateFlowMessage.collect { message ->
                binding.txtStateflow.text = message
            }
        }

    }

    private fun setListeners() {
        with (binding) {
            btnLivedata.setOnClickListener {
                viewModel.hitLiveData()
                binding.apply {
                    txtLivedataToStateflow.visibility = View.GONE
                    txtLivedata.visibility = View.VISIBLE
                }
            }
            btnStateflow.setOnClickListener { viewModel.hitStateFlow() }

            btnLivedataToStateflow.setOnClickListener {

                lifecycleScope.launchWhenStarted {
                    viewModel.changeLiveDataToStateFlow().collect {message ->
                        binding.apply {
                            txtLivedataToStateflow.text = message
                            txtLivedata.visibility = View.GONE
                            txtLivedataToStateflow.visibility = View.VISIBLE
                        }
                    }
                }

            }

            btnStateflowToLivedata.setOnClickListener {
                viewModel.run {
                    stateFlowToLiveData().observe(this@MainActivity, { message ->
                        binding.txtStateflow.text = message
                    })
                }
            }
        }
    }

}