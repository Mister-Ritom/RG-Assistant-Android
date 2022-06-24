package com.codewithritom.rg_assistant.ui.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AboutViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Welcome to Coding With Ritom, your number one source for all things like Android apps, Java apps. We're dedicated to providing you the very best of Android applications like this, with an emphasis on Product Design, Quality, Features.\n" +
                "\n" +
                "\n" +
                "Founded in 2022 by RitomG, Coding With Ritom has come a long way from its beginnings in Kolakata,West Bengal,India. When RitomG first started out, his passion for building android apps, drove them to start their own business.\n" +
                "\n" +
                "\n" +
                "We hope you enjoy our products as much as we enjoy offering them to you. If you have any questions or comments, please don't hesitate to contact us.\n" +
                "\n" +
                "\n" +
                "Sincerely,\n" +
                "\n" +
                "RitomG"
    }
    val text: LiveData<String> = _text
}