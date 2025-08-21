package com.reactnativecrispchatsdk

import android.content.Intent
import android.util.Log
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import im.crisp.client.external.ChatActivity
import im.crisp.client.external.Crisp
import im.crisp.client.external.data.SessionEvent
import im.crisp.client.external.data.SessionEvent.Color
import im.crisp.client.external.data.message.Message;
import im.crisp.client.external.EventsCallback


class CrispChatSdkModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "CrispChatSdk"
    }

    @ReactMethod
    fun configure(websiteId: String) {
        val context = reactApplicationContext
        Crisp.configure(context, websiteId)
    }

    @ReactMethod
    fun setTokenId(tokenId: String?){
        val context = reactApplicationContext
        Crisp.setTokenID(context, tokenId)
    }

    @ReactMethod
    fun setUserEmail(email: String) {
        Crisp.setUserEmail(email)
    }

    @ReactMethod
    fun setUserNickname(name: String) {
        Crisp.setUserNickname(name)
    }

    @ReactMethod
    fun setUserPhone(phone: String){
        Crisp.setUserPhone(phone)
    }

    @ReactMethod
    fun setUserAvatar(url: String){
        Crisp.setUserAvatar(url)
    }

    @ReactMethod
    fun setSessionSegment(segment: String){
        Crisp.setSessionSegment(segment)
    }

    @ReactMethod
    fun setSessionString(key: String, value: String){
        Crisp.setSessionString(key, value)
    }

    @ReactMethod
    fun setSessionBool(key: String, value: Boolean){
        Crisp.setSessionBool(key, value)
    }

    @ReactMethod
    fun setSessionInt(key: String, value: Int){
        Crisp.setSessionInt(key, value)
    }

    @ReactMethod
    fun pushSessionEvent(name: String, color: Int){
      var sessionEventColor: Color = Color.BLACK

      when(color){
        0->sessionEventColor= Color.RED
        1->sessionEventColor= Color.ORANGE
        2->sessionEventColor= Color.YELLOW
        3->sessionEventColor= Color.GREEN
        4->sessionEventColor= Color.BLUE
        5->sessionEventColor= Color.PURPLE
        6->sessionEventColor= Color.PINK
        7->sessionEventColor= Color.BROWN
        8->sessionEventColor= Color.GREY
        9->sessionEventColor= Color.BLACK
      }

      Crisp.pushSessionEvent(SessionEvent(
        name,
        sessionEventColor
      ))
    }

    @ReactMethod
    fun resetSession() {
        val context = reactApplicationContext
        Crisp.resetChatSession(context)
    }

    @ReactMethod
    fun show() {
        val context = reactApplicationContext
        val crispIntent = Intent(context, ChatActivity::class.java)
        crispIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(crispIntent)
    }

     @ReactMethod
    fun searchHelpdesk() {
        val context = reactApplicationContext
        Crisp.searchHelpdesk(context)
    }

    @ReactMethod
    fun openHelpdeskArticle(id: String, locale: String, title: String?, category: String?) {
        val context = reactApplicationContext
        Crisp.openHelpdeskArticle(context, id, locale, title, category)
    
    companion object {
        private val CRISP_EVENTS_CALLBACK = object : EventsCallback {
            override fun onSessionLoaded(sessionId: String) {
                Log.i("CrispChatSdk", "onSessionLoaded: $sessionId")
            }

            override fun onChatOpened() {
                Log.i("CrispChatSdk", "onChatOpened")
            }

            override fun onChatClosed() {
                Log.i("CrispChatSdk", "onChatClosed")
            }

            override fun onMessageSent(message: Message) {
                Log.i("CrispChatSdk", "onMessageSent: ${message.toJSON()}")
            }

            override fun onMessageReceived(message: Message) {
                Log.i("CrispChatSdk", "onMessageReceived: ${message.toJSON()}")
            }
        }

        @Volatile
        private var callbacksRegistered: Boolean = false
    }

    @ReactMethod
    fun addCallback() {
        if (callbacksRegistered) {
            Log.i("CrispChatSdk", "addCallback: already registered, skipping")
            return
        }
        Crisp.addCallback(CRISP_EVENTS_CALLBACK)
        callbacksRegistered = true
        Log.i("CrispChatSdk", "addCallback: registered")
    }

    @ReactMethod
    fun removeCallback() {
        if (!callbacksRegistered) {
            Log.i("CrispChatSdk", "removeCallback: not registered, skipping")
            return
        }
        Crisp.removeCallback(CRISP_EVENTS_CALLBACK)
        callbacksRegistered = false
        Log.i("CrispChatSdk", "removeCallback: removed")
    }
}
