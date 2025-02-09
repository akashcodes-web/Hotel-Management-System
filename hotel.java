import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

// Base class for rooms
class Room {
    String roomType;
    int roomNumber;
    boolean isAvailable;
    double pricePerNight;

    // Constructor to initialize room details
    Room(String roomType, int roomNumber, double pricePerNight) {
        this.roomType = roomType;
        this.roomNumber = roomNumber;
        this.isAvailable = true; // Initially available
        this.pricePerNight = pricePerNight;
    }

    // Method to book the room
    void bookRoom() {
        if (isAvailable) {
            isAvailable = false;
            System.out.println("Room " + roomNumber + " has been booked.");
        } else {
            System.out.println("Room " + roomNumber + " is already booked.");
        }
    }

    // Method to cancel booking
    void cancelBooking() {
        if (!isAvailable) {
            isAvailable = true;
            System.out.println("Booking for Room " + roomNumber + " has been canceled.");
        } else {
            System.out.println("Room " + roomNumber + " is already available.");
        }
    }

    // Method to check room availability
    boolean checkAvailability() {
        return isAvailable;
    }
}

// Derived class for Suite Room (for demonstration of inheritance)
class SuiteRoom extends Room {
    SuiteRoom(int roomNumber, double pricePerNight) {
        super("Suite", roomNumber, pricePerNight);
    }

    @Override
    void bookRoom() {
        if (isAvailable) {
            isAvailable = false;
            System.out.println("Suite Room " + roomNumber + " has been booked.");
        } else {
            System.out.println("Suite Room " + roomNumber + " is already booked.");
        }
    }
}

// Derived class for Deluxe Room
class DeluxeRoom extends Room {
    DeluxeRoom(int roomNumber, double pricePerNight) {
        super("Deluxe", roomNumber, pricePerNight);
    }

    @Override
    void bookRoom() {
        if (isAvailable) {
            isAvailable = false;
            System.out.println("Deluxe Room " + roomNumber + " has been booked.");
        } else {
            System.out.println("Deluxe Room " + roomNumber + " is already booked.");
        }
    }
}

// Guest class for storing guest details
class Guest {
    String name;
    int numOfNights;

    Guest(String name, int numOfNights) {
        this.name = name;
        this.numOfNights = numOfNights;
    }

    // Calculate total cost based on room price and number of nights
    double calculateTotalCost(Room room) {
        return room.pricePerNight * numOfNights;
    }
}

// Hotel Management System class to handle the reservation 
class HotelManagementSystem {
    HashMap<Integer, Room> rooms = new HashMap<>(); // Stores rooms
    HashSet<Integer> bookedRooms = new HashSet<>(); // Tracks booked rooms

    // Constructor to initialize the rooms
    HotelManagementSystem() {
        rooms.put(101, new Room("Single", 101, 100));
        rooms.put(102, new Room("Double", 102, 150));
        rooms.put(201, new SuiteRoom(201, 250));
        rooms.put(103, new Room("Single", 103, 100));
        rooms.put(104, new DeluxeRoom(104, 200)); // Deluxe room
    }

    // Method to show available rooms
    void showAvailableRooms() {
        System.out.println("\nAvailable Rooms:");
        for (Room room : rooms.values()) {
            if (room.checkAvailability()) {
                System.out.println(room.roomType + " - Room No: " + room.roomNumber + " - Price: $" + room.pricePerNight);
            }
        }
    }

    // Method to reserve a room
    void reserveRoom(int roomNumber, Guest guest) {
        if (bookedRooms.contains(roomNumber)) {
            System.out.println("Room " + roomNumber + " is already booked.");
            return;
        }

        Room room = rooms.get(roomNumber);
        if (room != null && room.checkAvailability()) {
            room.bookRoom();
            bookedRooms.add(roomNumber);
            double totalCost = guest.calculateTotalCost(room);
            System.out.println("Total cost for " + guest.name + ": $" + totalCost);
            saveBooking(guest, room, totalCost);
        } else {
            System.out.println("Room is not available or doesn't exist.");
        }
    }

    // Method to cancel a booking
    void cancelBooking(int roomNumber) {
        Room room = rooms.get(roomNumber);
        if (room != null && !room.checkAvailability()) {
            room.cancelBooking();
            bookedRooms.remove(roomNumber);
        } else {
            System.out.println("Room is already available or doesn't exist.");
        }
    }

    // Save booking details to a file
    void saveBooking(Guest guest, Room room, double totalCost) {
        try (FileWriter writer = new FileWriter("bookings.txt", true)) {
            writer.write(guest.name + " booked " + room.roomType + " Room " + room.roomNumber + " for " + guest.numOfNights + " nights. Total cost: $" + totalCost + "\n");
        } catch (IOException e) {
            System.out.println("Error saving booking: " + e.getMessage());
        }
    }
}


public class hotel {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HotelManagementSystem hotel = new HotelManagementSystem();

        while (true) {
            System.out.println("\nWelcome to the Hotel Reservation System!");
            System.out.println("1. Show available rooms");
            System.out.println("2. Book a room");
            System.out.println("3. Cancel booking");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    hotel.showAvailableRooms();
                    break;

                case 2:
                    System.out.print("Enter your name: ");
                    String guestName = scanner.nextLine();
                    System.out.print("Enter the number of nights: ");
                    int numOfNights = scanner.nextInt();
                    System.out.print("Enter room number to book: ");
                    int roomNumber = scanner.nextInt();

                    Guest guest = new Guest(guestName, numOfNights);
                    hotel.reserveRoom(roomNumber, guest);
                    break;

                case 3:
                    System.out.print("Enter room number to cancel booking: ");
                    int cancelRoomNumber = scanner.nextInt();
                    hotel.cancelBooking(cancelRoomNumber);
                    break;

                case 4:
                    System.out.println("Thank you for using the Hotel Reservation System. Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }
}
