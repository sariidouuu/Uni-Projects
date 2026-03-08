import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Fork {

  private int identity;
  private Lock lock = new ReentrantLock();

  Fork(int id)
    {identity = id;}

  void get() {
    lock.lock();
  }
  // Συνθήκη Μη-Εγκατάλειψης: Πρόβλημα deadlock διότι χρησιμοποιώ την lock(), η οποία δεν εγκαταλείπει ποτέ την προσπάθεια (δεν έχει timeout).
  // Ο φιλόσοφος θα κρατάει το πρώτο πιρούνι "για πάντα" περιμένοντας το δεύτερο.

  void put() {
      lock.unlock();
  }

  int getId(){
    return identity;
  }
}
