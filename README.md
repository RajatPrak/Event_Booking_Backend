Api Documentation of Event Booking App

=> Register User Api
curl -X POST http://localhost:8080/api/auth/user/register \
  -H "Content-Type: application/json" \
  -d '{
        "name":"Sita",
        "email":"sita@example.com",
        "phone":"9999999999",
        "password":"sita@123"
      }'

=> Login User Api
curl -X POST http://localhost:8080/api/auth/user/login \
  -H "Content-Type: application/json" \
  -d '{"email":"sita@example.com", "password":"sita@123"}'

=> Login Admin Api
curl -X POST http://localhost:8080/api/auth/admin/login \
  -H "Content-Type: application/json" \
  -d '{"email":"sita@example.com", "password":"sita@123"}'

=> Create Event (ADMIN)
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <ADMIN_TOKEN>" \
  -d '{
        "eventTitle":"Ayodhya Celebration",
	"eventDesciption":"Ayodhya Celebration is a description",
        "eventCategory":"Festival",
        "eventDate":"2025-12-25",
        "eventTime":"18:00:00",
        "totalSeats":1000,
        "eventLocation":"Ayodhya"
      }'

=> Update Event (Admin)
curl -X PUT http://localhost:8080/api/events/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <ADMIN_TOKEN>" \
  -d '{
        "eventTitle":"Ayodhya Celebration",
	"eventTitle":"Ayodhya Celebration is fabulous",
        "eventCategory":"Festival",
        "eventDate":"2025-12-26",
        "eventTime":"19:00:00",
        "totalSeats":1200,
        "eventLocation":"Ayodhya"
      }'




=> Delete Event (Admin)
curl -X DELETE http://localhost:8080/api/events/1 \
  -H "Authorization: Bearer <ADMIN_TOKEN>"

=> Fetch All Event (User)
curl http://localhost:8080/api/events

=> Book Event (User)
curl -X POST http://localhost:8080/api/events/1/book \
  -H "Authorization: Bearer <USER_TOKEN>"

=> Get the current user bookings (User)
curl -X GET http://localhost:8080/api/users/me/bookings \
  -H "Authorization: Bearer <USER_TOKEN>"

=> Cancel Booking Event (User)
curl -X POST http://localhost:8080/api/events/1/cancel \
  -H "Authorization: Bearer <USER_TOKEN>"



