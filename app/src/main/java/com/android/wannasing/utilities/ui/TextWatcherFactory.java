package com.android.wannasing.utilities.ui;

import android.text.Editable;
import android.text.TextWatcher;
import java.util.function.Consumer;

public class TextWatcherFactory {

  public static TextWatcher create(Consumer<String> consumerForBefore,
      Consumer<String> consumerForOn,
      Consumer<String> consumerForAfter) {
    return new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        consumerForBefore.accept(charSequence.toString());
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        consumerForOn.accept(charSequence.toString());
      }

      @Override
      public void afterTextChanged(Editable editable) {
        consumerForAfter.accept(editable.toString());
      }
    };
  }
}
