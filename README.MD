# Local Energy Charts #

More than 70% of global greenhouse gas emissions are generated in cities. The local concentration of people and industry makes cities hotspots of energy consumption. The solar potential in cities is huge, but often only a fraction is used. Therefore, cities play an important role in stopping the climate crisis. 

Since local reports on greenhouse gas emissions are often unavailable, or delayed, this application is intended to make the daily state of local renewable energies visible.

The data basis is provided by the [Marktstammdatenregister](https://www.marktstammdatenregister.de/MaStR), where all electricity generation plants are registered.

Please visit:

https://pv-frankfurt.de/

## Architecture ##
![architecture](architecture.png)

## Open API ##

This application comes with an [OpenAPI](https://pv-frankfurt.de/openapi.html) that allows you to easily create a new solar overview for cities.
You just need the [municipality key](https://www.statistikportal.de/de/gemeindeverzeichnis) of your city, and optionally the [rooftop solar potential](https://eosolar.dlr.de/) and the target year. 
Then all solar installations are downloaded from the [Marktstammdatenregister](https://www.marktstammdatenregister.de/MaStR), and updated daily.
Alternatively, the municipality key can be left empty and the zip code search can be used instead. Then please make sure that your city is listed in [Opendatasoft](https://public.opendatasoft.com/explore/dataset/georef-germany-postleitzahl/information/).

The API then provides you with a statistical analysis for the annual addition of solar installations, 
including the distribution of balcony power plants, apartment buildings, homes, schools or industrial buildings.

### Usage ###

First, create a new *solar city*

    POST /v1/create/solar-city

```json
{
  "cityName": "Frankfurt am Main",
  "municipalityKey": "06412000",
  "entireSolarPotentialOnRooftopsMWp": 2100.0,
  "targetYear": 2035
}
```

Then request e.g.:

    GET /v1/solar-cities/<id>/statistics/annual-addition-of-solar-installations/highcharts

## Getting Started Yourself ##
If you want to host the application yourself, please install Java version 21, Docker with Docker Compose
and run the following commands.

    docker compose up -d
    ./gradlew bootRun

Create a new solar city, try the api with [postman](LocalEnergyCharts.postman_collection.json) and visit: http://localhost:8080.

## Testing ##

This application is developed behavior-driven with [Cucumber](https://cucumber.io/). Cucumber reads executable specifications and validates the running application. 
All use cases are documented and simultaneously tested via the [RESTful API](https://pv-frankfurt.de/openapi.html).

* [annual addition of solar installations](test/src/test/resources/cucumber/annual_addition_of_solar_installations.feature)
* [solar overview](test/src/test/resources/cucumber/solar_systems_overview.feature)
* [action: now we need roofs!](test/src/test/resources/cucumber/now_we_need_roofs.feature)
* [solar building pie chart](test/src/test/resources/cucumber/solar_building_pie_chart.feature)

Please start the application in test mode as follows, and run the tests.

    docker compose up -d
    ./gradlew clean bootRun --args='--spring.profiles.active=test' 
    ./gradlew cucumberCli

## Webflow, Highcharts & Website ##

The website is developed, designed and previewed with [Webflow](https://webflow.com/). 
Html, CSS and JavaScript are then downloaded and saved in the application/../resources/static folder.

The charts are realised with [Highcharts](https://www.highcharts.com/). 
When the web application is started productively, a base API URL can be configured as follows.

    ./gradlew clean build -PbaseApiUrl="https://pv-frankfurt.de

Start the application and enjoy the website :)!
