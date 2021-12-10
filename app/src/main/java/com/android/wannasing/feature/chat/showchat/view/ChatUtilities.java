package com.android.wannasing.feature.chat.showchat.view;

import com.android.wannasing.feature.chat.showchat.model.Chat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatUtilities {

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
  private static final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

  public static String makeDateStr(Chat chat) {
    Date date = chat.getTimestamp();
    return dateFormat.format(date) + "\n" +
        timeFormat.format(date);
  }
}
