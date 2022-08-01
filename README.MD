# Local Energy Charts #

More than 70% of global greenhouse gas emissions are generated in cities. The local concentration of people and industry makes cities hotspots of energy consumption. The solar potential in cities is huge, but often only a fraction is used. Therefore, cities play an important role in stopping the climate crisis. Because local reports on greenhouse gas emissions are often unavailable, or delayed, this application aims to provide a quick overview of the local, renewable state of play.

Please visit:

https://pv-frankfurt.de/

## Open API ##

This application comes with an API that makes it easy to create a new statistical overview for more cities. 
You only need to specify the name of the city, and all zip codes. Then all the solar installations are pulled from the [Marktstammdatenregister](https://www.marktstammdatenregister.de/MaStR), and updated daily. 
The API then offers you statistical analysis, e.g. the annual addition of solar installations.

### Documentation ###
You can find the open API documentation [here](https://pv-frankfurt.de/swagger-ui/index.html).

### Living example ###
The website in this project was realized with [Webflow](https://webflow.com/) and [Highcharts](https://www.highcharts.com/).
So to put the Open API into operation, a living example can be found in [localenergycharts.js](web/js/localenergycharts.js).

### Postman ###

You can also test the API with Postman. Find the collection in [here](api/LocalEnergyCharts.postman_collection.json).

## Getting Started Yourself ##

If you want to host the application yourself, please install Java version 17, Docker and run and run the following commands.

    docker-compose up -d
    ./gradlew bootRun

Then execute the following post request to load all solar systems from the [Marktstammdatenregister](https://www.marktstammdatenregister.de/MaStR) for Frankfurt am Main.

    POST http://localhost:8080/v1/create/solar-city

with request body:

```json
{
  "cityName": "Frankfurt am Main",
  "postcodes": [
    60306, 60308, 60310, 60311, 60312, 60313, 60314, 
    60315, 60316, 60318, 60320, 60322, 60323, 60325, 
    60326, 60327, 60329, 60385, 60386, 60388, 60389, 
    60431, 60433, 60435, 60437, 60438, 60439, 60486, 
    60487, 60488, 60489, 60528, 60529, 60549, 60594, 
    60596, 60598, 60599, 65929, 65931, 
    65933, 65934, 65936
  ],
  "totalSolarPotentialMWp": 1006.0,
  "totalSolarPotentialTargetYear": 2035
}
```

Finally, visit http://localhost/

## Testing ##

To test the application, and at the same time document it, [Cucumber](https://cucumber.io/) was used. Documentation and tests can be found in the following module.

    testing/src/test/resources/cucumber

Please start the application in test mode as follows, and run the tests.

    docker-compose up -d
    ./gradlew bootRun --args='--spring.profiles.active=test' 
    ./gradlew cucumberCli
