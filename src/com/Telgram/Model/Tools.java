package com.Telgram.Model;

/**
 * Singleton class tools which makes jobs easier to be done
 */
public class Tools {
    public static String serverAddress = "localhost";
    public static int serverPort = 8585;

    public static Message message;
    private static Tools tools = new Tools();
    private Tools (){ }
    public static Tools getTools () {return tools;}

}
