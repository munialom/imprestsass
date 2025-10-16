package com.ctecx.brs.tenant.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GetComputerName {
    public static void main(String[] args) {
        try {
            // Get fully qualified domain name (FQDN)
            String fqdn = InetAddress.getLocalHost().getCanonicalHostName();
            System.out.println("Full Computer Name: " + fqdn);

            // Get just the computer name
            String computerName = InetAddress.getLocalHost().getHostName();
            System.out.println("Computer Name: " + computerName);
        } catch (UnknownHostException e) {
            System.out.println("Error getting computer name: " + e.getMessage());
        }
    }
}