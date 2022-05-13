package com.hedgehog.testkz

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.hedgehog.testkz.databinding.CustomTextViewBinding

class CustomTextView @JvmOverloads constructor(
    context: Context,
    private val attr: AttributeSet? = null,
    private val defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attr, defStyleAttr) {

    private val binding: CustomTextViewBinding =
        CustomTextViewBinding.inflate(LayoutInflater.from(context), this)

    private val defaultColor = R.color.purple_500

    init {
        setupView()
        setupWithAttr()
    }

    private fun setupWithAttr() {
        with(binding) {
            context.withStyledAttributes(attr, R.styleable.CustomTextView, defStyleAttr) {
                val title = getString(R.styleable.CustomTextView_custom_text_title)
                val description = getString(R.styleable.CustomTextView_custom_text_description)
                val progressColor = getColor(
                    R.styleable.CustomTextView_custom_color,
                    ContextCompat.getColor(context, defaultColor)
                )

                textCharge.text = title
                descriptionText.text = description
                progressBar.progress = progressColor
            }
        }
    }

    private fun setupView() {
        background = ContextCompat.getDrawable(context, R.drawable.shape_corner_radius)
        elevation = 40f
    }
}