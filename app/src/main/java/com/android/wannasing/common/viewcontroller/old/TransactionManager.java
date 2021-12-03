package com.android.wannasing.common.viewcontroller.old;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction.Function;

@Deprecated
public class TransactionManager {

  final FirebaseFirestore db;

  public TransactionManager() {
    this.db = FirebaseFirestore.getInstance();
  }

  public void run(Function<Void> updateFunction) {
    db.runTransaction(updateFunction);
  }
}
