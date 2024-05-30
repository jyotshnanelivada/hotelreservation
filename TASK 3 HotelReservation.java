import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Room {
    private int id;
    private String type;
    private double price;
    private boolean availability;

    public Room(int id, String type, double price, boolean availability) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.availability = availability;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public boolean isAvailable() { return availability; }
    public void setAvailability(boolean availability) { this.availability = availability; }
}

class Booking {
    private int id;
    private String userName;
    private int roomId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalAmount;
    private double amountPaid;
    private String status;

    public Booking(int id, String userName, int roomId, LocalDate startDate, LocalDate endDate, double totalAmount) {
        this.id = id;
        this.userName = userName;
        this.roomId = roomId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalAmount = totalAmount;
        this.amountPaid = 0;
        this.status = "Pending";
    }

    // Getters and setters
    public int getId() { return id; }
    public String getUserName() { return userName; }
    public int getRoomId() { return roomId; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public double getTotalAmount() { return totalAmount; }
    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

class Payment {
    private int id;
    private int bookingId;
    private double amount;

    public Payment(int id, int bookingId, double amount) {
        this.id = id;
        this.bookingId = bookingId;
        this.amount = amount;
    }

    // Getters and setters
    public int getId() { return id; }
    public int getBookingId() { return bookingId; }
    public double getAmount() { return amount; }
}

class HotelReservationSystem {
    private List<Room> rooms;
    private List<Booking> bookings;
    private List<Payment> payments;

    public HotelReservationSystem() {
        rooms = new ArrayList<>();
        bookings = new ArrayList<>();
        payments = new ArrayList<>();
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public List<Room> getAvailableRooms() {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.isAvailable()) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public void makeBooking(Booking booking) {
        bookings.add(booking);
        Room bookedRoom = getRoomById(booking.getRoomId());
        if (bookedRoom != null) {
            bookedRoom.setAvailability(false);
        } else {
            throw new IllegalArgumentException("Room ID " + booking.getRoomId() + " does not exist.");
        }
    }

    public void makePayment(Payment payment) {
        Booking booking = getBookingById(payment.getBookingId());
        if (booking != null) {
            payments.add(payment);
            booking.setAmountPaid(booking.getAmountPaid() + payment.getAmount());
            if (booking.getAmountPaid() >= booking.getTotalAmount()) {
                booking.setStatus("Paid");
                System.out.println("Payment completed! Booking is fully paid. Payment ID: " + payment.getId());
            } else {
                double balance = booking.getTotalAmount() - booking.getAmountPaid();
                booking.setStatus("Partially Paid");
                System.out.println("Partial payment received. Balance amount to be paid: $" + balance);
            }
        } else {
            System.out.println("Invalid booking ID. Payment cannot be made.");
        }
    }

    public void checkBookingStatus(int bookingId) {
        Booking booking = getBookingById(bookingId);
        if (booking != null) {
            System.out.println("Booking ID: " + booking.getId() + ", Status: " + booking.getStatus() +
                               ", Amount Paid: $" + booking.getAmountPaid() + ", Total Amount: $" + booking.getTotalAmount());
        } else {
            System.out.println("Invalid booking ID.");
        }
    }

    public Room getRoomById(int roomId) {
        for (Room room : rooms) {
            if (room.getId() == roomId) {
                return room;
            }
        }
        return null;
    }

    public Booking getBookingById(int bookingId) {
        for (Booking booking : bookings) {
            if (booking.getId() == bookingId) {
                return booking;
            }
        }
        return null;
    }

    public Payment getPaymentById(int paymentId) {
        for (Payment payment : payments) {
            if (payment.getId() == paymentId) {
                return payment;
            }
        }
        return null;
    }

    public int getNextBookingId() {
        return bookings.size() + 1;
    }

    public int getNextPaymentId() {
        return payments.size() + 1;
    }
}

public class HotelReservation {
    public static void main(String[] args) {
        HotelReservationSystem hotelSystem = new HotelReservationSystem();
        Scanner scanner = new Scanner(System.in);

        // Add some rooms
        hotelSystem.addRoom(new Room(1, "Single", 100.0, true));
        hotelSystem.addRoom(new Room(2, "Double", 150.0, true));
        hotelSystem.addRoom(new Room(3, "Suite", 200.0, true));

        while (true) {
            System.out.println("1. Make a booking");
            System.out.println("2. Make a payment");
            System.out.println("3. Check booking status");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    List<Room> availableRooms = hotelSystem.getAvailableRooms();
                    if (availableRooms.isEmpty()) {
                        System.out.println("No rooms available.");
                        break;
                    }

                    System.out.println("Available rooms:");
                    for (Room room : availableRooms) {
                        System.out.println("Room ID: " + room.getId() + ", Type: " + room.getType() + ", Price: $" + room.getPrice());
                    }

                    System.out.print("Enter user name: ");
                    String userName = scanner.nextLine();
                    System.out.print("Enter room ID from the available options: ");
                    int roomId = scanner.nextInt();
                    System.out.print("Enter start date (yyyy-mm-dd): ");
                    LocalDate startDate = LocalDate.parse(scanner.next());
                    System.out.print("Enter end date (yyyy-mm-dd): ");
                    LocalDate endDate = LocalDate.parse(scanner.next());

                    Room selectedRoom = hotelSystem.getRoomById(roomId);
                    if (selectedRoom != null && selectedRoom.isAvailable()) {
                        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
                        double totalAmount = daysBetween * selectedRoom.getPrice();
                        Booking booking = new Booking(hotelSystem.getNextBookingId(), userName, roomId, startDate, endDate, totalAmount);
                        hotelSystem.makeBooking(booking);
                        System.out.println("Booking made successfully! Booking ID: " + booking.getId() + ", Total Amount: $" + totalAmount);
                    } else {
                        System.out.println("Invalid room ID or room is not available.");
                    }
                    break;
                case 2:
                    System.out.print("Enter booking ID: ");
                    int bookingId = scanner.nextInt();
                    System.out.print("Enter payment amount: ");
                    double amount = scanner.nextDouble();
                    Payment payment = new Payment(hotelSystem.getNextPaymentId(), bookingId, amount);
                    hotelSystem.makePayment(payment);
                    break;
                case 3:
                    System.out.print("Enter booking ID: ");
                    int bookingIdToCheck = scanner.nextInt();
                    hotelSystem.checkBookingStatus(bookingIdToCheck);
                    break;
                case 4:
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        }
    }
}
