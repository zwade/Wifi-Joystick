package com.zwad3.wifijoy;

import android.util.Log;
import android.view.KeyEvent;

public final class KeycodeConverter {

  public static void print(String s) {
    Log.d("KeyCode",s);
  }
  public static int convertKey(int code) {
    if (code >= 65 && code <= 90) {
      print(""+KeyEvent.KEYCODE_A);
      return (code-65)+KeyEvent.KEYCODE_A;
    }
    if (code >= 48 && code <= 57) {
      return code - 48 + KeyEvent.KEYCODE_0;
    }

    switch (code) {
      case 9: return KeyEvent.KEYCODE_TAB;
      case 32: return KeyEvent.KEYCODE_SPACE;
      case 188: return KeyEvent.KEYCODE_COMMA;
      case 190: return KeyEvent.KEYCODE_PERIOD;
      case 13: return KeyEvent.KEYCODE_ENTER;
      case 219: return KeyEvent.KEYCODE_LEFT_BRACKET;
      case 221: return KeyEvent.KEYCODE_RIGHT_BRACKET;
      case 220: return KeyEvent.KEYCODE_BACKSLASH;
      case 186: return KeyEvent.KEYCODE_SEMICOLON;
      case 222: return KeyEvent.KEYCODE_APOSTROPHE;
      case 8: return KeyEvent.KEYCODE_DEL;
      case 189: return KeyEvent.KEYCODE_MINUS;
      case 187: return KeyEvent.KEYCODE_EQUALS;
      case 191: return KeyEvent.KEYCODE_SLASH;
      case 18: return KeyEvent.KEYCODE_ALT_LEFT;
      case 16: return KeyEvent.KEYCODE_SHIFT_LEFT;
      case 38: return KeyEvent.KEYCODE_DPAD_UP;
      case 40: return KeyEvent.KEYCODE_DPAD_DOWN;
      case 37: return KeyEvent.KEYCODE_DPAD_LEFT;
      case 39: return KeyEvent.KEYCODE_DPAD_RIGHT;
      case 112: return KeyEvent.KEYCODE_DPAD_CENTER;
      case 45: return KeyEvent.KEYCODE_DPAD_CENTER;
      case 27: return KeyEvent.KEYCODE_BACK;
      case 116: return KeyEvent.KEYCODE_BACK;
      case 113: return KeyEvent.KEYCODE_MENU;
      case 114: return KeyEvent.KEYCODE_SEARCH;
      case 121: return KeyEvent.KEYCODE_VOLUME_UP;
      case 120: return KeyEvent.KEYCODE_VOLUME_DOWN;
      case 192: return KeyEvent.KEYCODE_GRAVE;
      //case KeyboardHttpServer.FOCUS: return KeyboardHttpServer.FOCUS;
      //case 36: return WiFiInputMethod.KEY_HOME;
      //case 35: return WiFiInputMethod.KEY_END;
      //case 17: return WiFiInputMethod.KEY_CONTROL;
      //ase 46: return WiFiInputMethod.KEY_DEL;
      default: return code;
    }

  }
}
