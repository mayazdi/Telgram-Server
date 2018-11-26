package com.Telgram.Model;

import java.io.Serializable;
import java.util.HashMap;

public class Message implements Serializable {
    public Action action;
    public HashMap<String, String> value;
}
