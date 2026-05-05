public class ServerProtocol_Ex2 {

    public String processRequest(String theInput) {
        if (theInput.equals("!")) return "CLOSE";

        // Χωρίζουμε το μήνυμα στα κενά
        String[] parts = theInput.split(" ");
        
        if (parts.length != 3) {
            return "E 1: Wrong input";
        }

        String op = parts[0];
        int a, b, r = 0;

        try {
            a = Integer.parseInt(parts[1]);
            b = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            return "E 2: No integers where given";
        }

        switch (op) {
            case "+":
                r = a + b;
                break;
            case "-":
                r = a - b;
                break;
            case "*":
                r = a * b;
                break;
            case "/":
                if (b == 0) return "E 3: Division by 0";
                r = a / b;
                break;
            default:
                return "E 4: Unknown calculation";
        }

        // replyMessage: R <space> r
        return "R " + r;
    }
}