1. POST / http://localhost:8080/api/products

   {
       "name": "Cofe",
       "price": 15,
       "description": "Cofe of Gold Coast"
   }


2. GET / http://localhost:8080/api/products?projection=productSummary


3. GET / http://localhost:8080/api/products?projection=productDetail
4.  Controller also works ! GET/ http://localhost:8080/api/v1/products