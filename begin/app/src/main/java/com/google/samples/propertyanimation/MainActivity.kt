/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.propertyanimation

import android.animation.*
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView

import com.google.samples.propertyanimation.databinding.ActivityMainBinding


@Suppress("UNUSED_PARAMETER")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rotateButton.setOnClickListener(this::rotator)
        binding.translateButton.setOnClickListener(this::translator)
        binding.scaleButton.setOnClickListener(this::scaler)
        binding.fadeButton.setOnClickListener(this::fader)
        binding.colorizeButton.setOnClickListener(this::colorizer)
        binding.showerButton.setOnClickListener(this::shower)
    }

    private fun rotator(view: View) {
        val animator: ObjectAnimator = ObjectAnimator.ofFloat(
            binding.star,
            View.ROTATION,
            -360f, 0f
        ).apply {
            duration = 1000
        }
        animator.disableButtonTemporarily(binding.rotateButton)
        animator.start()
    }

    private fun translator(view: View) {
        val animator: ObjectAnimator = ObjectAnimator.ofFloat(
            binding.star,
            View.TRANSLATION_X,
            200f
        ).apply {
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
        }
        animator.disableButtonTemporarily(binding.translateButton)
        animator.start()
    }

    private fun scaler(view: View) {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(binding.star, scaleX, scaleY).apply {
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
        }
        animator.disableButtonTemporarily(binding.scaleButton)
        animator.start()
    }

    private fun fader(view: View) {
        val animator = ObjectAnimator.ofFloat(
            binding.star,
            View.ALPHA,
            0f
        ).apply {
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
            duration = 1000
        }
        animator.disableButtonTemporarily(binding.fadeButton)
        animator.start()
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun colorizer(view: View) {
        val animator = ObjectAnimator.ofArgb(
            binding.star.parent,
            "backgroundColor",
            Color.BLACK,
            Color.RED
        ).apply {
            duration = 500
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
        }
        animator.disableButtonTemporarily(binding.colorizeButton)
        animator.start()
    }

    private fun shower(view: View) {
        val container = binding.star.parent as ViewGroup
        val containerW = container.width
        val containerH = container.height
        var starW: Float = binding.star.width.toFloat()
        var starH: Float = binding.star.height.toFloat()
        val newStar = AppCompatImageView(this).apply {
            setImageResource(R.drawable.ic_star)
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            scaleX = kotlin.random.Random.nextFloat() * 1.5f + 0.1f
            scaleY = this.scaleX
        }
        starW *= newStar.scaleX
        starH *= newStar.scaleY
        newStar.translationX = kotlin.random.Random.nextFloat() * containerW - starW / 2f
        container.addView(newStar)
        val mover = ObjectAnimator.ofFloat(
            newStar,
            View.TRANSLATION_Y,
            -1 * starH,
            containerH + starH
        ).apply {
            interpolator = AccelerateInterpolator(1f)
        }
        val rotator = ObjectAnimator.ofFloat(
            newStar,
            View.ROTATION,
            kotlin.random.Random.nextFloat() * 1080f
        ).apply {
            interpolator = LinearInterpolator()
        }
        val set = AnimatorSet().apply {
            duration = kotlin.random.Random.nextLong(0, 10000)
            playTogether(
                mover,
                rotator
            )
            addListener(object: AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    container.removeView(newStar)
                }
            })
        }
        set.start()
    }

    private fun ObjectAnimator.disableButtonTemporarily(view: View) {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled = true
            }
        })
    }
}
