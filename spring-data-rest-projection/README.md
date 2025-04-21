1. POST / http://localhost:8080/api/products

   {
       "name": "Cofe",
       "price": 15,
       "description": "Cofe of Gold Coast"
   }


2. GET / http://localhost:8080/api/products?projection=productSummary


3. GET / http://localhost:8080/api/products?projection=productDetail
4.  Controller also works ! GET/ http://localhost:8080/api/v1/products


# Note:
https://docs.spring.io/spring-data/rest/reference/projections-excerpts.html
Projection definitions are picked up and made available for clients if they are:

Flagged with the @Projection annotation and located in the same package (or sub-package) of the domain type, OR

Manually registered by using RepositoryRestConfiguration.getProjectionConfiguration().addProjection(…)



http://localhost:8080/api/products/search/by-name?name=Cacao&projection=productDetail
http://localhost:8080/api/products/search/by-name?name=Cacao&projection=productSummary

http://localhost:8080/api/products?page=0&size=5&sort=name,asc
http://localhost:8080/api/products?projection=productDetail&?page=0&size=5&sort=name,asc
http://localhost:8080/api/products?projection=productSummuray
http://localhost:8080/api/products/search/by-name?name=Cacao
http://localhost:8080/api/products/search/by-name?name=Cofee&projection=productDetail&?page=0&size=5&sort=name,asc
http://localhost:8080/api/products/search/by-name?name=Cacao&projection=productSummary