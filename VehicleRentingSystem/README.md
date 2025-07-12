# Vehicle Renting System

## Requirements:
1. Onboard a new branch with its available vehicle inventory.
2. Add new vehicles of an existing vehicle type to an existing branch.
3. Rent a vehicle for a specified time slot and vehicle type.
4. Default selection strategy: Choose the vehicle with the lowest price by default.
5. Vehicle selection logic should be designed to support other strategies (e.g., shortest distance, preferred branch, etc.).
6. Branch fallback mechanism: If the requested vehicle type is unavailable at a branch, automatically fallback to another branch using the same or alternate strategy.
7. Provide a system-wide view showing.
8. Currently booked vehicles.
9. Currently available vehicles at all branches.

## Classes, Interfaces and Enumerations:
1. The Vehicle class represents a vehicle in the system. with properties- id, type, price, bookingSlot.
2. The Branch class represents a branch in the system. with properties- name, vehicleMap.
3. The Booking class represents a booking in the system, with properties- id, bookingSlot, vehicleType, status, bookingStrategy
4. Vehicle selection logic is implemented using Strategy pattern.
5. The BookingStrategy interface, LowestCostBookingStrategy class and BookingStrategyContext class is used for vehicle selection using Strategy pattern.
6. The Slot class represents a slot with start and end datetime
7. The BookingStatus is Enum class, represents booking status(booked or not-booked).
8. The BookingService class is the central class which manages the whole booking process. It's implemented using singleton pattern.
9. The Executor class is used to perform all the operations.
   
## Design Patterns Used:
1. Singleton pattern is used for BookingService, BranchService classes.
2. Strategy pattern is used to implement Vehicle selection logic for given type and time slot.

## Implementation
#### [Java Implementation](./src/) 

