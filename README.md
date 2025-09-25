# CMI (Moroccan Payment Gateway) Payment Integration for Sprinng Boot

# Architecture Overview

## System Components

![System Components](assets/components.png)

## Core Components

1- **Payment Controller Layer:** REST endpoints or payment operations

2- **Payment Service Layer:** Business logic and CMI integration

3- **Transaction Repository:** Database operations and audit trail

4- **CMI Client:** HTTP client for CMI API communication

5- **Callback Handler:** Webhook processing and validation

6- **Security Layer:** Request signing and validation

7- **Monitoring & Altering:** Metrics, logging and healt checks

## Request Flow Diagram


