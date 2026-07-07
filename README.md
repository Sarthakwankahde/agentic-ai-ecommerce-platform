# Agentic Commerce AI Platform

A production-ready backend application built using Java, Spring Boot, and PostgreSQL that combines modern e-commerce functionality with an AI-ready architecture. The project follows industry-standard development practices and is designed to be scalable, secure, and easy to extend with intelligent AI-powered features.

##  Project Overview

Agentic Commerce AI Platform is an enterprise-style backend application that provides complete e-commerce functionality including authentication, product management, shopping cart, wishlist, coupon system, order management, payment gateway integration, and email notifications.

The architecture is designed to support future AI capabilities such as conversational shopping assistants, personalized product recommendations, intelligent search, autonomous workflows, and analytics powered by Large Language Models (LLMs) and Agentic AI.

# Features

## Authentication & Security
- User Registration
- User Login
- JWT Authentication
- Refresh Token Authentication
- Role-Based Authorization (Admin/User)
- Password Encryption using BCrypt
- Change Password
- Profile Management


##  Product Management
- Add Product
- Update Product
- Delete Product
- View Products
- Search Products
- Category-wise Products
- Inventory Management


## Category Management
- Create Category
- Update Category
- Delete Category
- View Categories


## Shopping Cart
- Add to Cart
- Update Cart Quantity
- Remove from Cart
- View Cart
- Calculate Total Price


## Wishlist
- Add Product to Wishlist
- Remove Product
- View Wishlist


## 🎟 Coupon & Discount
- Create Coupon
- Update Coupon
- Delete Coupon
- Apply Coupon
- Coupon Validation
- Discount Calculation


## Order Management
- Place Order
- View My Orders
- Order Details
- Cancel Order
- Automatic Stock Update


## Payment Integration
- Razorpay Payment Gateway
- Payment Verification
- Payment Success Handling
- Secure Payment Workflow


## Email Notification System
- Welcome Email
- Order Confirmation Email
- Order Cancellation Email
- Payment Success Email


## Exception Handling
- Global Exception Handler
- Custom Exceptions
- Validation Handling
- Standard API Error Responses


## Security
- Spring Security
- JWT
- Refresh Tokens
- Password Encryption
- Role-Based Access Control


# AI Vision

The project is designed with an AI-first architecture and can be extended with:

- Agentic AI Workflows
- AI Shopping Assistant
- LLM Integration
- Retrieval-Augmented Generation (RAG)
- AI Product Recommendation System
- Natural Language Product Search
- AI-powered Sales Analytics
- Intelligent Inventory Prediction
- Personalized Customer Experience


# Technologies Used

### Backend
- Java 21
- Spring Boot
- Spring MVC
- Spring Security
- Spring Data JPA
- Hibernate

### Database
- PostgreSQL

### Authentication
- JWT
- Refresh Tokens
- BCrypt Password Encoder

### Payment
- Razorpay API

### Email
- Spring Mail Sender
- Gmail SMTP

### Build Tool
- Maven

### Testing
- Postman

### Version Control
- Git
- GitHub

### IDE
- IntelliJ IDEA


# Project Structure

src
 ├── controller
 ├── service
 │     ├── impl
 ├── repository
 ├── entity
 ├── dto
 ├── security
 ├── exception
 ├── config
 ├── constant
 └── resources

# Architecture

The project follows a Layered Architecture:

Controller
      │
      ▼
Service
      │
      ▼
Repository
      │
      ▼
Database

This architecture improves maintainability, scalability, and separation of concerns.

#  Future Enhancements

- AI Chatbot using LLMs
- Product Recommendation Engine
- Semantic Product Search using Vector Database
- RAG-based Knowledge Assistant
- Agentic AI Workflow Automation
- Docker Deployment
- Redis Caching
- Microservices Architecture
- Kubernetes Deployment
- CI/CD Pipeline
- Cloud Deployment (AWS)

# Getting Started

### Clone the Repository

bash
git clone https://github.com/YOUR_USERNAME/agentic-commerce-ai-platform.git

### Navigate to Project

bash
cd agentic-commerce-ai-platform

### Configure Database

Update your `application.properties` file with your PostgreSQL credentials.

### Install Dependencies

bash
mvn clean install


### Run the Project

bash
mvn spring-boot:run

The application will start on:

http://localhost:8081

# API Testing

All REST APIs can be tested using **Postman**.


#  Contributions

Contributions, suggestions, and improvements are welcome. Feel free to fork the repository and submit a pull request.


# Author

Sarthak Wankhade

Java Full Stack Developer | Spring Boot Developer | AI Enthusiast


#  If you like this project

Please consider giving it a tar on GitHub.
