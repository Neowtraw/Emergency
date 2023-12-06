package com.codingub.emergency.data.utils

import java.io.IOException

class NetworkUnavailableException(message: String = "No network connection :(") :
IOException(message){
}