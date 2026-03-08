
public class Supervisor{

    // Με βάση το id του fork οι φιλόσοφοι κλειδώνουν πρώτα αυτό με το μικρότερο, και στη συνέχεια επιχειρούν να κλειδώσουν το μεγαλύτερο.
    // Δεν παίζει ρόλο αν το μικρότερο είναι το αριστερό ή το δεξί.

    public void getPermission(int philosopherId, Fork leftFork, Fork rightFork){
        if(leftFork.getId() < rightFork.getId()){
            leftFork.get();
            rightFork.get();
        }else{
            rightFork.get();
            leftFork.get();
        }
    }

    public void releasePermission(int philosopherId, Fork leftFork, Fork rightFork){
        leftFork.put();
        rightFork.put();
    }
}