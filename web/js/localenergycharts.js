let LocalEnergyCharts = {};
let pvFrankfurtApiUrl = 'http://localhost:8080';

LocalEnergyCharts.getSolarOverview = function (city) {

  $.ajax({
    url: `${pvFrankfurtApiUrl}/v1/city/${city}/solar-overview`,
    contentType: 'application/json',
    dataType: 'json',

    success: function (response) {
      $('#used-solar-potential-percent').text(Intl.NumberFormat().format(response.usedSolarPotentialPercent) + ' %');
      $('#total-solar-installations').text(Intl.NumberFormat().format(response.totalSolarInstallations));
      $('#updated').text(new Date(response.updated).toLocaleDateString());
    }
  });
}

LocalEnergyCharts.getAnnualRateOfSolarInstallationsChart = function (city, previousSolarInstallationsOnly) {

  $.ajax({
    url: `${pvFrankfurtApiUrl}/v1/highcharts/city/${city}/annual-addition-of-solar-installations`,
    contentType: 'application/json',
    dataType: 'json',
    data: {previousSolarInstallationsOnly: previousSolarInstallationsOnly},

    success: function (response) {
      createAnnualRateOfSolarInstallationsChart(response);
    }
  })
}

LocalEnergyCharts.getYourCityFormAnnualRateOfSolarInstallationsChart = function () {

  function convertYourCityFormToJson(form) {
    let formData = $(form).serializeArray();
    let cityName = formData[0].value;
    let postcodes = formData[1].value;
    let totalSolarPotentialMWp = formData[2].value;
    let totalSolarPotentialTargetYear = formData[3].value;

    return {
      'cityName': cityName || '',
      'postcodes': postcodes.split(',').map(postcode => parseInt(postcode)),
      'totalSolarPotentialMWp': parseInt(totalSolarPotentialMWp),
      'totalSolarPotentialTargetYear': parseInt(totalSolarPotentialTargetYear)
    };
  }

  let yourCityForm = $('#your-city-form');
  let yourCityRequest = JSON.stringify(convertYourCityFormToJson(yourCityForm))

  $.ajax({
    url: `${pvFrankfurtApiUrl}/v1/highcharts/create/temporary/annual-addition-of-solar-installations`,
    method: 'POST',
    data: yourCityRequest,
    contentType: 'application/json',
    dataType: 'json',

    success: function (response) {
      let parent = $(yourCityForm.parent());
      parent.children('.w-form-done').css('display', 'block');
      parent.find('.w-form-fail').css('display', 'none');

      createAnnualRateOfSolarInstallationsChart(response);
      $('#total-solar-installations').text(Intl.NumberFormat().format(response.totalSolarInstallations));
      $('html, body').animate({scrollTop: 0}, 'slow');
    },

    error: function () {
      let parent = $(yourCityForm.parent());
      parent.children('.w-form-done').css('display', 'none');
      parent.find('.w-form-fail').css('display', 'block');
    }
  });
}

function createAnnualRateOfSolarInstallationsChart(response) {
  Highcharts.chart('solar-city-annual-addition-of-solar-installations', {
    title: {
      text: 'Ausbau von Solaranlagen in ' + response.cityName
    },
    chart: {
      type: 'column',
      style: {
        color: '#333',
        fontFamily: 'Roboto, sans-serif'
      }
    },
    accessibility: {
      announceNewData: {
        enabled: true
      }
    },
    xAxis: {
      type: 'category',
      labels: {
        rotation: -45,
        style: {
          color: '#333',
          fontSize: '16px',
          fontFamily: 'Roboto, sans-serif'
        }
      }
    },
    yAxis: {
      min: 0,
      title: {
        text: 'Ausbau (MWp)'
      },
      labels: {
        style: {
          color: '#333',
          fontSize: '16px',
          fontFamily: 'Roboto, sans-serif'
        }
      }
    },
    legend: {
      enabled: false
    },
    tooltip: {
      pointFormat: '<b>Ausbau: {point.y:.1f} MWp<br/>Anlagen: {point.numberOfSolarSystems}</b><br/><br/>'
          + '&nbsp;&nbsp;0-10 kWp: {point.numberOfSolarSystemsUpTo10kWp} (Einfamilienhäuser, ..)<br/>'
          + '10-40 kWp: {point.numberOfSolarSystems10To40kWp} (Mehrfamilienhäuser, Gewerbe, ..)<br/>'
          + 'ab 40 kWp: {point.numberOfSolarSystemsFrom40kWp} (Schulen, Industriebauten, ..)'
    },
    series: [{
      name: 'Übersicht',
      data: response.data
    }],
    drilldown: {
      activeAxisLabelStyle: {
        'cursor': 'pointer',
        'color': '#333',
        'fontWeight': 'normal',
        'textDecoration': 'none'
      },
      breadcrumbs: {
        position: {align: 'center'}
      },
      series: response.drilldownData
    }
  });
}

LocalEnergyCharts.sendContactMessage = function () {

  function convertContactFormToJson(form) {
    let formData = $(form).serializeArray();
    let name = formData[0].value;
    let email = formData[1].value;
    let message = formData[2].value;

    return {
      'name': name || '',
      'email': email || '',
      'message': message || ''
    };
  }

  let contactForm = $('#wf-contact-form');
  let contactRequest = JSON.stringify(convertContactFormToJson(contactForm));

  $.ajax({
    url: `${pvFrankfurtApiUrl}/v1/contact`,
    method: 'POST',
    data: contactRequest,
    contentType: 'application/json',
    dataType: 'json',

    success: function () {
      let parent = $(contactForm.parent());
      parent.children('.w-form-done').css('display', 'block');
      parent.find('.w-form-fail').css('display', 'none');
      contactForm.css('display', 'none');
      $('html, body').animate({scrollTop: 0}, 'slow');
    },

    error: function () {
      let parent = $(contactForm.parent());
      parent.children('.w-form-done').css('display', 'none');
      parent.find('.w-form-fail').css('display', 'block');
    }
  });
}

LocalEnergyCharts.writeYourLandlordPageOpened = function () {

  $.ajax({
    url: `${pvFrankfurtApiUrl}/v1/write-your-landlord/opened`,
    method: 'POST',
    contentType: 'application/json',
    dataType: 'json'
  });
}

LocalEnergyCharts.writeYourLandlord = function () {

  function convertWriteYourLandlordFormToJson(form) {
    let formData = $(form).serializeArray();
    let fromTenant = formData[0].value;
    let toLandlord = formData[1].value;
    let message = formData[2].value;

    return {
      'fromTenant': fromTenant || '',
      'toLandlord': toLandlord || '',
      'message': message || ''
    };
  }

  let writeYourLandlordForm = $('#wf-write-your-landlord-form');
  let writeYourLandlordRequest = JSON.stringify(convertWriteYourLandlordFormToJson(writeYourLandlordForm));

  $.ajax({
    url: `${pvFrankfurtApiUrl}/v1/write-your-landlord/send`,
    method: 'POST',
    data: writeYourLandlordRequest,
    contentType: 'application/json',

    success: function () {
      let parent = $(writeYourLandlordForm.parent());
      parent.children('.w-form-done').css('display', 'block');
      parent.find('.w-form-fail').css('display', 'none');
      writeYourLandlordForm.css('display', 'none');
      $('html, body').animate({scrollTop: 0}, 'slow');
    },

    error: function () {
      let parent = $(writeYourLandlordForm.parent());
      parent.children('.w-form-done').css('display', 'none');
      parent.find('.w-form-fail').css('display', 'block');
    }
  });
}
