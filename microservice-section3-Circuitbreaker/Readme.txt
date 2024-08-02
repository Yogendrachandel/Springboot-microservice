In this section we have covered the reliselince circuit breaker:





run services in this order:
               configserver -> eureka -> account/cards -> gateway server
			   
			   
And follow postman documenet to hit the services via Gatwayserver using http://localhost:<Gatway port>/<serviceName>/<api URI>


###########################################USING GATEWAY ENDPOINT####################################################
run services in this order:
               config server -> eureka -> account/cards -> gateway server

ACCOUNTS BY GATWAY

POST - http://localhost:8072/accounts/api/create
{
 "name": "Madan Reddy",
 "email": "tutor@eazybytes",
 "mobileNumber": "8427155369"
 }
 
 
 GET - http://localhost:8072/accounts/api/getInfo?mobileNumber=8427155369
 
 
 
 PUT : http://localhost:8072/accounts/api/update
 
 {
    "name": "Yogendra",
    "email": "yogendra@gmail.com",
    "mobileNumber": "8427155369",
    "accountsDto": {
        "accountNumber": 1441516659,
        "accountType": "Savings",
        "branchAddress": "123 Main Street, New York"
    }
}
 
delete: http://localhost:8072/accounts/api/delete?mobileNumber=8427155369


Get Build Info : http://localhost:8072/accounts/api/build-info


Get contact info :  http://localhost:8072/accounts/api/contact-info
 
 
 #############################GET account and card ############################
 GET : http://localhost:8072/accounts/api/fetchCustomerDetails?mobileNumber=8427155369
 
 
 
 
 
CARDS by GATEWAY:
 
 POST: http://localhost:8072/cards/api/create?mobileNumber=8427155369
 
 
 GET : http://localhost:8072/cards/api/fetch?mobileNumber=8427155369
 
 {
    "mobileNumber": "8427155369",
    "cardNumber": "100404644893",
    "cardType": "Credit Card",
    "totalLimit": 100000,
    "amountUsed": 0,
    "availableAmount": 100000
}

PUT: http://localhost:8072/cards/api/update
{
    "mobileNumber": "8427155369",
    "cardNumber": "100404644893",
    "cardType": "Credit Card",
    "totalLimit": 100000,
    "amountUsed": 0,
    "availableAmount": 100000
}

delete: http://localhost:8072/cards/api/delete?mobileNumber=8427155369


Get Build Info : http://localhost:8072/cards/api/build-info


Get contact info :  http://localhost:8072/cards/api/contact-info

