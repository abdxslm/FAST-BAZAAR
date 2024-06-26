package com.example.model;

import java.time.LocalDateTime;
import java.util.*;

import com.almasb.fxgl.core.collection.Array;

import java.io.*;

public class Courier extends User {
    ArrayList<SellerOrder> orders = new ArrayList<>();
    private int numberOfOrdersToBeDelivered;

    public void setOrders(ArrayList<SellerOrder> orders) {
        this.orders = orders;
    }

    public Courier() {
        super();
        orders = new ArrayList<>();
        numberOfOrdersToBeDelivered = 0;
    }

    public Courier(String name, String email, String password, String phone, String address, String city, int zip, String country) {
        super(name, email, password, phone, address, city, zip, country);
        numberOfOrdersToBeDelivered = 0;
        orders = new ArrayList<>();
    }

    public int getNumberOfOrdersToBeDelivered() {
        return numberOfOrdersToBeDelivered;
    }

    public void updateOrderStatus() {
        System.out.println("Here is a list of orders you have to deliver: ");
        viewPendingOrders();
        System.out.println("Enter the order ID of the order you want to update: ");
        int orderNumber = Main.input.nextInt();
        Main.input.nextLine();
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getSellerOrderID() == orderNumber) {
                System.out.println("Enter the new status: ");
                String status = Main.input.nextLine();
                orders.get(i).setStatus(status);
                break;
            }
        }
        com.example.view.Main.couriers = readCouriersFromFile();
        for (int i = 0; i < com.example.view.Main.couriers.size(); i++) {
            if (com.example.view.Main.couriers.get(i).getEmail().equals(this.getEmail())) {
                com.example.view.Main.couriers.set(i, this);
                break;
            }
        }
        updateBinaryFile(com.example.view.Main.couriers);
    }

    public void addOrderToDeliver(SellerOrder order) {
        orders.add(order);
    }

    public ArrayList<SellerOrder> getOrders() {
        return orders;
    }

    public static void updateBinaryFile(List<Courier> couriers) {
        try {
            File directory = new File("src\\main\\resources\\couriers.ser");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            FileOutputStream fileOut = new FileOutputStream
                    ("src\\main\\resources\\couriers.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(couriers);
            out.close();
            fileOut.close();
            System.out.println("Courier file updated");
        } catch (IOException i) {
            i.printStackTrace();
            System.out.println("Courier file not updated");
        }
    }

    public static ArrayList<Courier> readCouriersFromFile() {
        ArrayList<Courier> couriers = new ArrayList<>();
        File file = new File("src\\main\\resources\\couriers.ser");
        try {
            if (!file.exists() || file.length() == 0) {
                file.createNewFile();
                System.err.println("Couriers file created");
                return couriers;
            }
            FileInputStream fileIn = new FileInputStream("src\\main\\resources\\couriers.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object readObject = in.readObject();
            if (readObject instanceof ArrayList<?>) {
                @SuppressWarnings("unchecked")
                ArrayList<Courier> tempCouriers = (ArrayList<Courier>) readObject;
                couriers = tempCouriers;
            } else {
                System.out.println("Read object is not an ArrayList");
            }
            in.close();
            fileIn.close();
        } catch (FileNotFoundException e) {
            System.out.println("Courier file not found at the specified path");
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Courier class not found");
            c.printStackTrace();
        }
        return couriers == null ? new ArrayList<>() : couriers;
    }

    public void updateProfile() {
        System.out.println("Enter new name: ");
        String name = Main.input.nextLine();
        System.out.println("Enter new email: ");
        String email = Main.input.nextLine();
        System.out.println("Enter new password: ");
        String password = Main.input.nextLine();
        System.out.println("Enter new phone: ");
        String phone = Main.input.nextLine();
        System.out.println("Enter new address: ");
        String address = Main.input.nextLine();
        System.out.println("Enter new city: ");
        String city = Main.input.nextLine();
        System.out.println("Enter new zip: ");
        int zip = Main.input.nextInt();
        Main.input.nextLine();
        System.out.println("Enter new country: ");
        String country = Main.input.nextLine();
        setName(name);
        setEmail(email);
        setPassword(password);
        setPhone(phone);
        setAddress(address);
        setCity(city);
        setZip(zip);
        setCountry(country);
        com.example.view.Main.couriers = readCouriersFromFile();
        for (int i = 0; i < com.example.view.Main.couriers.size(); i++) {
            if (com.example.view.Main.couriers.get(i).getEmail().equals(this.getEmail())) {
                com.example.view.Main.couriers.set(i, this);
                break;
            }
        }
        updateBinaryFile(com.example.view.Main.couriers);
    }

    public void deliverOrder(SellerOrder order) {

    }

    public void updateOrderStatus(SellerOrder order, String status) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).equals(order)) {
                orders.get(i).setStatus(status);
                break;
            }
        }
        ArrayList<Courier> couriers = readCouriersFromFile();
        for (int i = 0; i < couriers.size(); i++) {
            if (couriers.get(i).getEmail().equals(this.getEmail())) {
                couriers.set(i, this);
                break;
            }
        }
        updateBinaryFile(couriers);
    }

    public void viewPendingOrders() {
        for (int i = 0; i < orders.size(); i++) {
            if(!(orders.get(i).getStatus().equalsIgnoreCase("Delivered"))) {
                System.out.println(orders.get(i));
            }
        }
    }

    public void viewCompletedOrders() {
        for (int i = 0; i < orders.size(); i++) {
            if(orders.get(i).getStatus().equals("Delivered")) {
                System.out.println(orders.get(i));
            }
        }
    }

    public ArrayList<SellerOrder> getCompletedOrders() {
        ArrayList<SellerOrder> completedOrders = new ArrayList<>();
        for (SellerOrder order : orders) {
            if (order.getStatus().equals("Delivered")) {
                completedOrders.add(order);
            }
        }
        return completedOrders;
    }

    public void viewAllOrders() {
        for (int i = 0; i < orders.size(); i++) {
            System.out.println(orders.get(i));
        }
    }

    public static Courier findCourierWithLeastDeliveries() {
        com.example.view.Main.couriers = Courier.readCouriersFromFile();
        Courier courierWithLeastDeliveries = null;
        int minDeliveries = Integer.MAX_VALUE;

        for (Courier courier : com.example.view.Main.couriers) {
            int deliveries = courier.getNumberOfOrdersToBeDelivered();
            if (deliveries < minDeliveries) {
                minDeliveries = deliveries;
                courierWithLeastDeliveries = courier;
            }
        }

        return courierWithLeastDeliveries;
    }

    @Override
    public String toString() {
        return "Courier{" +
                "name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", phone='" + getPhone() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", city='" + getCity() + '\'' +
                ", zip=" + getZip() +
                ", country='" + getCountry() + '\'' +
                ", orderToDeliver=" + orders +
                ", numberOfOrdersToBeDelivered=" + numberOfOrdersToBeDelivered +
                '}';
    }

    public ArrayList<SellerOrder> getPendingOrders() {
        ArrayList<SellerOrder> pendingOrders = new ArrayList<>();
        for (SellerOrder order : orders) {
            if (!order.getStatus().equals("Delivered")) {
                pendingOrders.add(order);
            }
        }
        return pendingOrders;
    }

    public void updateSellerOrderStatus(SellerOrder order) {
        for (SellerOrder sellerOrder : orders) {
            if (sellerOrder.equals(order)) {
                sellerOrder.setStatus("Delivered");
                sellerOrder.setDeliveryDate(LocalDateTime.now());
                break;
            }
        }
        for(Seller s : com.example.view.Main.sellers) {
            for(SellerOrder sellerOrder : s.getOrders()) {
                if(sellerOrder.equals(order)) {
                    sellerOrder.setStatus("Delivered");
                    sellerOrder.setDeliveryDate(LocalDateTime.now());
                    break;
                }
            }
        }
        updateBinaryFile(com.example.view.Main.couriers);
        Seller.updateBinaryFile(com.example.view.Main.sellers);
    }
}