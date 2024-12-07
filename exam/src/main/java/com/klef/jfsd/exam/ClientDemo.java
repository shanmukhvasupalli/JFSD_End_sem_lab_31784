package com.klef.jfsd.exam;

import java.util.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class ClientDemo {

    public static void main(String args[]) {
        ClientDemo operations = new ClientDemo();
        operations.addCustomer(); 
        //operations.criteriaDemo(); // Demonstrating various criteria
    }

    public void addCustomer() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");

        SessionFactory sf = configuration.buildSessionFactory();
        Session session = sf.openSession(); // Create connection

        Transaction t = session.beginTransaction();
        Scanner sc = new Scanner(System.in);

        Customer customer = new Customer();

        // Adding details to the Customer object
        System.out.println("Enter Customer Name:");
        customer.setName(sc.nextLine());

        System.out.println("Enter Customer Email:");
        customer.setEmail(sc.nextLine());

        System.out.println("Enter Customer Age:");
        customer.setAge(sc.nextInt());
        sc.nextLine(); // Consume newline left-over

        System.out.println("Enter Customer Location:");
        customer.setLocation(sc.nextLine());

        // Persist the customer object
        session.persist(customer);

        t.commit();
        System.out.println("Customer Added Successfully");
        sf.close();
        session.close();
    }

    public void criteriaDemo() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");

        SessionFactory sf = configuration.buildSessionFactory();
        Session session = sf.openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
        Root<Customer> root = cq.from(Customer.class);

        System.out.println("**** Criteria Demo ****");

        // Equal
        cq.select(root).where(cb.equal(root.get("location"), "Vijayawada"));
        displayResults(session.createQuery(cq).getResultList(), "Equal");

        // Not Equal
        cq.select(root).where(cb.notEqual(root.get("location"), "Vijayawad"));
        displayResults(session.createQuery(cq).getResultList(), "Not Equal");

        // Less Than (on age)
        cq.select(root).where(cb.lessThan(root.get("age"), 30));
        displayResults(session.createQuery(cq).getResultList(), "Less Than");

        // Greater Than (on age)
        cq.select(root).where(cb.greaterThan(root.get("age"), 40));
        displayResults(session.createQuery(cq).getResultList(), "Greater Than");

        // Like (on email)
        cq.select(root).where(cb.like(root.get("email"), "%gmail%"));
        displayResults(session.createQuery(cq).getResultList(), "Like");

        session.close();
        sf.close();
    }

    private void displayResults(List<Customer> customers, String criterion) {
        System.out.println("Results for criterion: " + criterion);
        System.out.println("Customers Count: " + customers.size());
        for (int i = 0; i < customers.size(); i++) {
            System.out.println(customers.get(i).toString());
        }
    }
}
