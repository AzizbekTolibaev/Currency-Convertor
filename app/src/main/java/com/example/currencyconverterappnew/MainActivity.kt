package com.example.currencyconverterappnew

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.currencyconverterappnew.databinding.ActivityMainBinding
import com.example.currencyconverterappnew.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVariables()
        initObservers()
        currencyRise()
        initListener()

    }

    private fun initVariables() {
        val currencyCodes = resources.getStringArray(R.array.currency_codes)

        val mArrayAdapter = ArrayAdapter<Any?>(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            currencyCodes
        )

        binding.fromCurrencyTxt.adapter = mArrayAdapter
        binding.toCurrencyTxt.adapter = mArrayAdapter
    }

    private fun initListener() {
        binding.fromCurrencyTxt.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    currencyRise()
                    if (binding.etAmount.text.toString().isNotEmpty()) {
                        convertCurrency()
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.toCurrencyTxt.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    currencyRise()
                    if (binding.etAmount.text.toString().isNotEmpty()) {
                        convertCurrency()
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.etAmount.addTextChangedListener {
            if (binding.etAmount.text.toString()
                    .isNotEmpty() && binding.etAmount.toString() != "0"
            ) {
                convertCurrency()
            } else {
                binding.tvResult.text = "0"
            }
        }

        binding.btnSwap.setOnClickListener {
            val fromCurrencyId = binding.fromCurrencyTxt.selectedItemId
            val toCurrencyId = binding.toCurrencyTxt.selectedItemId
            binding.fromCurrencyTxt.setSelection(toCurrencyId.toInt())
            binding.toCurrencyTxt.setSelection(fromCurrencyId.toInt())
            if (binding.etAmount.text.toString().isNotEmpty()) {
                convertCurrency()
            }
        }
    }

    private fun initObservers() {
        viewModel.conversion.observe(this) {
            if (binding.etAmount.text.toString().isNotEmpty()) {
                val formatResult = String.format("%.2f", it).replace(",", ".")
                binding.tvResult.text = formatResult
            } else {
                binding.tvResult.text = "0"
            }
        }

        viewModel.loading.observe(this) { }

        viewModel.message.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.error.observe(this) {
            Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
        }

        viewModel.oneCurrency.observe(this) {
            binding.tvCurrencyRate.text = it
        }

        viewModel.currencyRise.observe(this) {
            if (it) {
                binding.imgCurrencyRise.setImageResource(R.drawable.ic_currency_up)
            } else {
                binding.imgCurrencyRise.setImageResource(R.drawable.ic_currency_down)
            }
        }
    }

    fun convertCurrency() {
        lifecycleScope.launch {
            viewModel.convert(
                binding.fromCurrencyTxt.selectedItem.toString(),
                binding.toCurrencyTxt.selectedItem.toString(),
                binding.etAmount.text.toString()
            )
        }
    }

    private fun currencyRise() {
        lifecycleScope.launch {
            viewModel.currencyRise(
                binding.fromCurrencyTxt.selectedItem.toString(),
                binding.toCurrencyTxt.selectedItem.toString()
            )
        }
    }
}
