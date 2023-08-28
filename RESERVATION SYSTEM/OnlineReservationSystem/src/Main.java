import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Login form
        boolean loggedIn = Login.login(scanner);

        if (loggedIn) {
            // Reservation and Cancellation forms
            while (true) {
                System.out.println("\nSelect an option:");
                System.out.println("1. Reserve Ticket");
                System.out.println("2. Cancel Ticket");
                System.out.println("3. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                if (choice == 1) {
                    ReservationForm.reserveTicket(scanner);
                } else if (choice == 2) {
                    CancellationForm.cancelTicket(scanner);
                } else if (choice == 3) {
                    System.out.println("Exiting...");
                    break;
                } else {
                    System.out.println("Invalid choice. Please select a valid option.");
                }
            }
        }

        scanner.close();
    }
}

class Login {
    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "admin";

    public static boolean login(Scanner scanner) {
        System.out.println("Enter your login id: ");
        String loginId = scanner.nextLine();
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();

        if (loginId.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD)) {
            System.out.println("Login successful!");
            return true;
        } else {
            System.out.println("Invalid login id or password!");
            return false;
        }
    }
}

class ReservationForm {
    public static void reserveTicket(Scanner scanner) {
        // Reservation form
        System.out.println("Enter your train number: ");
        String trainNumber = scanner.nextLine();
        System.out.println("Enter your class type (Economy/Business/First Class): ");
        String classType = scanner.nextLine();
        System.out.println("Enter the date of journey: ");
        String dateOfJourney = scanner.nextLine();
        System.out.println("Enter the from station: ");
        String fromStation = scanner.nextLine();
        System.out.println("Enter the destination station: ");
        String destinationStation = scanner.nextLine();

        Reservation reservation = new Reservation(trainNumber, classType, dateOfJourney, fromStation, destinationStation);
        reservation.bookTicket();
        ReservationRepository.addReservation(reservation);
    }
}

class CancellationForm {
    public static void cancelTicket(Scanner scanner) {
        // Cancellation form
        System.out.println("Enter your PNR number: ");
        String pnrNumber = scanner.nextLine();

        Cancellation cancellation = new Cancellation(pnrNumber);
        cancellation.cancelTicket();
    }
}

class Reservation {

    private String trainNumber;
    private String classType;
    private String dateOfJourney;
    private String fromStation;
    private String destinationStation;
    private int numberOfSeats;
    private boolean isBooked;
    private String pnrNumber;

    public Reservation(String trainNumber, String classType, String dateOfJourney, String fromStation, String destinationStation) {
        this.trainNumber = trainNumber;
        this.classType = classType;
        this.dateOfJourney = dateOfJourney;
        this.fromStation = fromStation;
        this.destinationStation = destinationStation;
        this.numberOfSeats = 1;
        this.isBooked = false;
        this.pnrNumber = generatePnrNumber();
    }

    private String generatePnrNumber() {
        // Generate a random PNR number
        return "PNR-" + UUID.randomUUID().toString();
    }

    public void bookTicket() {
        if (!isBooked) {
            this.isBooked = true;
            System.out.println("Ticket booked successfully!");
            System.out.println("Your PNR number is: " + pnrNumber); // Display the PNR number
        } else {
            System.out.println("Sorry, the ticket is already booked!");
        }
    }

    public void cancelTicket() {
        if (isBooked) {
            this.isBooked = false;
            System.out.println("Ticket cancelled successfully!");
        } else {
            System.out.println("Sorry, the ticket is not booked yet!");
        }
    }

    public String getPnrNumber() {
        return this.pnrNumber;
    }
}

class Cancellation {

    private String pnrNumber;

    public Cancellation(String pnrNumber) {
        this.pnrNumber = pnrNumber;
    }

    public void cancelTicket() {
        Reservation reservation = ReservationRepository.getReservationByPnrNumber(pnrNumber);
        if (reservation != null) {
            reservation.cancelTicket();
            System.out.println("Ticket cancelled successfully!");
        } else {
            System.out.println("Sorry, the ticket with PNR number " + pnrNumber + " does not exist!");
        }
    }
}

class ReservationRepository {

    private static Map<String, Reservation> reservations = new HashMap<>();

    public static Reservation getReservationByPnrNumber(String pnrNumber) {
        return reservations.get(pnrNumber);
    }

    public static void addReservation(Reservation reservation) {
        reservations.put(reservation.getPnrNumber(), reservation);
    }
}
