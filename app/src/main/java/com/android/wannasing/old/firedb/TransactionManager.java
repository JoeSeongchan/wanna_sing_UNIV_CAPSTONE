package com.android.wannasing.old.firedb;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction.Function;

public class TransactionManager {

  final FirebaseFirestore db;

  public TransactionManager() {
    this.db = FirebaseFirestore.getInstance();
  }

  public void run(Function<Void> updateFunction) {
    db.runTransaction(updateFunction);
  }
}
