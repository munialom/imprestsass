/*package com.ctecx.rentals.controller;




import com.ctecx.rentals.security.UserTenantInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.security.Principal;
import java.util.Map;

*//**
 * @author Md. Amran Hossain
 *//*
@RestController
@RequestMapping("/api/auto-logout")
public class LogoutController implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogoutController.class);

    @Autowired
    ApplicationContext applicationContext;

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseEntity<?> logoutFromApp(Principal principal) {
        LOGGER.info("AuthenticationController::logoutFromApp() method call..");
        UserTenantInformation userCharityInfo = applicationContext.getBean(UserTenantInformation.class);
        Map<String, String> map = userCharityInfo.getMap();
        map.remove(principal.getName());
        userCharityInfo.setMap(map);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}*/


package com.ctecx.brs.controller;


import com.ctecx.brs.security.UserTenantInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Md. Amran Hossain
 */
@RestController
@RequestMapping("/api/auto-logout")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class LogoutController implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogoutController.class);

    public static final String UNAUTHENTICATED_REQUEST = "Unauthenticated request";
    public static final String LOGOUT_SUCCESSFUL = "Logout successful";

    @Autowired
    ApplicationContext applicationContext;

    @GetMapping("/logout")
    public ResponseEntity<Map<String, Object>> logoutFromApp(Principal principal) {
        LOGGER.info("LogoutController::logoutFromApp() method call for user: {}", principal != null ? principal.getName() : "anonymous");

        // Check if principal is null (user not authenticated)
        if (principal == null) {
            LOGGER.warn(UNAUTHENTICATED_REQUEST);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", UNAUTHENTICATED_REQUEST);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        try {
            // Retrieve and update user tenant information
            UserTenantInformation userCharityInfo = applicationContext.getBean(UserTenantInformation.class);
            Map<String, String> map = userCharityInfo.getMap();

            // Safely remove the user from the map
            if (map != null) {
                map.remove(principal.getName());
                userCharityInfo.setMap(map);
            }

            // Clear the security context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                SecurityContextHolder.clearContext();
            }

            LOGGER.info("User {} logged out successfully", principal.getName());
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("status", "success");
            successResponse.put("message", LOGOUT_SUCCESSFUL);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            LOGGER.error("Error during logout for user {}: {}", principal.getName(), e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error during logout: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}