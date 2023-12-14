package com.nitap.attende

import com.google.android.libraries.identity.googleid.GetGoogleIdOption

class DummyActivity {




    public  fun getId(): GetGoogleIdOption {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
           // .setServerClientId("305352247527-l8grdm5f3nn1c47a93m8oek40r7851er.apps.googleusercontent.com")
            .setServerClientId("305352247527-llm2m9p0smvunhmj4aeushknk7uq1o0i.apps.googleusercontent.com")
            .build()
        return googleIdOption
    }

    companion object {
        fun staticMethod(): GetGoogleIdOption {
            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId("305352247527-l8grdm5f3nn1c47a93m8oek40r7851er.apps.googleusercontent.com")
                .build()
            return googleIdOption
        }
    }

}