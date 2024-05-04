public class KMeans {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Musisz podac sciezke do pliku testowego i wartosc k jako argumenty.");
            return;
        }

        String testFile = args[0];
        int k = Integer.parseInt(args[1]);
    }
}
