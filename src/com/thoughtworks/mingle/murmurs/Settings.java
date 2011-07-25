package com.thoughtworks.mingle.murmurs;

public class Settings {

  private static final boolean EMULATOR = true;

  public static String getUsername() {
    return "admin";
  }

  public static String getPassword() {
    return "p";
  }

  public static String getMingleHost() {
    return "http://" + (EMULATOR ? "10.0.2.2" : "192.168.1.66") + ":" + getPort();
  }

  public static String getProjectPath() {
    return getMingleHost() + "/api/v2/projects/bearbot";
  }

  public static int getPort() {
    return 8080;
  }

}
