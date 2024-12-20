import java.util.*;

class BusTicketReservationSystem {
    private String[] seats; // Array for seat availability
    private SimpleQueue bookingQueue; // Queue for booking requests
    private SimpleLinkedList bookedPassengers; // Linked list for confirmed bookings
    private SimpleStack cancellationStack; // Stack for managing cancellations

    public BusTicketReservationSystem(int totalSeats) {
        this.seats = new String[totalSeats];
        Arrays.fill(this.seats, null);
        this.bookingQueue = new SimpleQueue();
        this.bookedPassengers = new SimpleLinkedList();
        this.cancellationStack = new SimpleStack();
    }

    public void bookGroup(String passengerName, int groupSize) {
        int booked = 0;
        for (int j = 0; j < seats.length && booked < groupSize; j++) {
            if (seats[j] == null) {
                seats[j] = passengerName;
                booked++;
            }
        }
        if (booked > 0) {
            bookingQueue.enqueue(passengerName);
            bookedPassengers.add(passengerName);
            sortPassengers();
        } else {
            System.out.println("Not enough available seats for the group booking.");
        }
    }

    public void bookSpecificSeat(String passengerName, int specificSeat) {
        if (specificSeat >= 0 && specificSeat < seats.length && seats[specificSeat] == null) {
            seats[specificSeat] = passengerName;
            bookingQueue.enqueue(passengerName);
            bookedPassengers.add(passengerName);
            sortPassengers();
        } else {
            System.out.println("Seat not available or invalid seat number.");
        }
    }

    private void sortPassengers() {
        String[] passengers = bookedPassengers.toArray();
        for (int i = 0; i < passengers.length; i++) {
            for (int j = 0; j < passengers.length - i - 1; j++) {
                if (passengers[j].compareTo(passengers[j + 1]) > 0) {
                    String temp = passengers[j];
                    passengers[j] = passengers[j + 1];
                    passengers[j + 1] = temp;
                }
            }
        }
        bookedPassengers.clear();
        for (String passenger : passengers) {
            bookedPassengers.add(passenger);
        }
    }

    public void cancelSeat(int seatNumber) {
        if (seatNumber >= 0 && seatNumber < seats.length && seats[seatNumber] != null) {
            cancellationStack.push(seatNumber);
            seats[seatNumber] = null;
        } else {
            System.out.println("Invalid seat number or seat is already empty.");
        }
    }

    public void reassignCanceledSeats() {
        while (!cancellationStack.isEmpty()) {
            int seatNumber = cancellationStack.pop();
            seats[seatNumber] = "Reassigned";
        }
    }

    public void displaySeatStatus() {
        System.out.println("Seat Status: " + Arrays.toString(seats));
    }

    public void displayBookings() {
        System.out.println("Confirmed Bookings: " + bookedPassengers);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Bus Ticket Reservation System");
        System.out.print("Enter the total number of seats: ");
        int totalSeats = scanner.nextInt();
        BusTicketReservationSystem system = new BusTicketReservationSystem(totalSeats);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Book Group");
            System.out.println("2. Book Specific Seat");
            System.out.println("3. Cancel Seat");
            System.out.println("4. Reassign Canceled Seats");
            System.out.println("5. Display Seat Status");
            System.out.println("6. Display Bookings");
            System.out.println("7. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter passenger name: ");
                    String groupName = scanner.nextLine();
                    System.out.print("Enter group size: ");
                    int groupSize = scanner.nextInt();
                    system.bookGroup(groupName, groupSize);
                    break;
                case 2:
                    System.out.print("Enter passenger name: ");
                    String passengerName = scanner.nextLine();
                    System.out.print("Enter specific seat number (0-based index): ");
                    int seatNumber = scanner.nextInt();
                    system.bookSpecificSeat(passengerName, seatNumber);
                    break;
                case 3:
                    System.out.print("Enter seat number to cancel (0-based index): ");
                    int cancelSeatNumber = scanner.nextInt();
                    system.cancelSeat(cancelSeatNumber);
                    break;
                case 4:
                    system.reassignCanceledSeats();
                    break;
                case 5:
                    system.displaySeatStatus();
                    break;
                case 6:
                    system.displayBookings();
                    break;
                case 7:
                    System.out.println("Exiting... Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

// Queue Class
class SimpleQueue {
    private Node front;
    private Node rear;

    private static class Node {
        String data;
        Node next;

        Node(String data) {
            this.data = data;
            this.next = null;
        }
    }

    public void enqueue(String item) {
        Node newNode = new Node(item);
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
    }

    public String dequeue() {
        if (front == null) {
            throw new NoSuchElementException("Queue is empty");
        }
        String item = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        return item;
    }

    public boolean isEmpty() {
        return front == null;
    }
}

// Linked List Class
class SimpleLinkedList {
    private Node head;

    private static class Node {
        String data;
        Node next;

        Node(String data) {
            this.data = data;
            this.next = null;
        }
    }

    public void add(String item) {
        Node newNode = new Node(item);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    public String[] toArray() {
        List<String> result = new ArrayList<>();
        Node current = head;
        while (current != null) {
            result.add(current.data);
            current = current.next;
        }
        return result.toArray(new String[0]);
    }

    public void clear() {
        head = null;
    }

    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node current = head;
        while (current != null) {
            sb.append(current.data).append(" -> ");
            current = current.next;
        }
        return sb.append("null").toString();
    }
}

// Stack Class
class SimpleStack {
    private Node top;

    private static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    public void push(int item) {
        Node newNode = new Node(item);
        newNode.next = top;
        top = newNode;
    }

    public int pop() {
        if (top == null) {
            throw new NoSuchElementException("Stack is empty");
        }
        int item = top.data;
        top = top.next;
        return item;
    }

    public boolean isEmpty() {
        return top == null;
    }
}