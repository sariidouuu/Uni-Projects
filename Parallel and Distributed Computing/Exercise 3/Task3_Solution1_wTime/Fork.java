import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Fork {

  private int identity;
  private Lock lock = new ReentrantLock();

  Fork(int id){
    identity = id;}

  // Επιστρέφει true αν πήρε το πιρούνι, false αν έληξε το timeout (-> για αποφυγή deadlock)
  boolean get(long timeout, TimeUnit unit) {
    try {
      return lock.tryLock(timeout, unit);
      // TryLock με timeout: Προσπαθεί να αποκτήσει το lock μέσα στο χρονικό όριο,
      // Αν δεν τα καταφέρει επιστρέφει false, αντί να μπλοκάρει για πάντα.
      // Έτσι, καταργείται η Συνθήκη Μη-Εγκατάλειψης: ο φιλόσοφος ΔΕΝ κρατάει το πρώτο πιρούνι επ' αόριστον αν δεν βρει το δεύτερο.
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt(); // Ενημερώνω το πρόγραμμα ότι αυτο το νήμα πρέπει να διακοπεί
      return false;
    }
  }

  void put() {
      lock.unlock();
  }
}
