let LocalEnergyCharts = {};
let baseApiUrl = 'http://localhost:8080';
let language = 'de'; // default

LocalEnergyCharts.changeLanguageToDeutsch = function () {
    language = 'de';
}

LocalEnergyCharts.changeLanguageToEnglish = function () {
    language = 'en';
}

const translations = {
    de: {
        annualAdditionChart: {
            title: 'Ausbau von Photovoltaik-Anlagen in',
            name: 'Übersicht',
            yAxis: 'Ausbau in Megawatt pro Jahr [MWp]'
        },
        buildingPieChart: {
            title: 'Gebäude, Verteilung & installierte PV-Anlagen'
        },
        monthlySolarInstallationsChart: {
            title: 'Zubau der letzen Monate'
        },
        installedPvSystems: 'installierte PV-Anlagen',
        pvSystems: 'PV-Anlagen:',
        installation: 'Ausbau:',
        roofPvSystems: 'Dach-PV-Anlagen:',
        balkonSolar: 'Balkonkraftwerke',
        homes: 'Eigenheime, ..',
        apartmentBuildings: 'Mehrfamilienhäuser, Gewerbe, ..',
        schools: 'Schulen, Industriebauten, ..',
        APARTMENT_BUILDINGS_COMMERCIAL_AND_CO: 'Mehrfamilienhäuser, ..',
        HOMES: 'Eigenheime, ..',
        BALKONKRAFTWERKE: 'Balkonkraftwerke',
        SCHOOLS_INDUSTRIAL_BUILDINGS_AND_CO: 'Schulen, Industriebauten, ..',
        count: 'Anlagen',
        installedMWp: 'Installation'
    },
    en: {
        annualAdditionChart: {
            title: 'Renewable state of play in',
            name: 'Overview',
            yAxis: 'Installation in megawatts per year [MWp]'
        },
        pvSystems: 'pv-systems:',
        installation: 'installation:',
        roofPvSystems: 'roof-pv-systems:',
        balkonSolar: 'balcony-pv',
        homes: 'homes, ..',
        apartmentBuildings: 'apartment buildings, commercial roofs, ..',
        schools: 'schools, industrial buildings, ..'
    },
    koeln: {
        annualAdditionChart: {
            title: 'jährlicher Zubau von Photovoltaik in',
            name: 'Übersicht',
            yAxis: 'Ausbau in Megawatt [MWp]'
        }
    }
}

const highchartsStyle = {
    annualAdditionChart: {
        titleStyle: {color: '#333', fontFamily: 'Roboto, sans-serif', fontSize: '18px', fontWeight: 'bold'},
        style: {color: '#333', fontFamily: 'Roboto, sans-serif', fontSize: '16px'},
        columnColor: '#012b4a',
        currentColumnColor: '#ee7338',
        futureColumnColor: '#cde846',

        koeln: {
            titleStyle: {color: '#000', fontFamily: 'Candara, sans-serif', fontSize: '20px', fontWeight: 'bold'},
            style: {color: '#000', fontFamily: 'Candara, sans-serif', fontSize: '18px'},
            columnColor: '#012b4a',
            currentColumnColor: '#ee7338',
            futureColumnColor: '#66a62c',
        },
        muenchen: {
            titleStyle: {color: '#011633', fontFamily: 'Roboto, sans-serif', fontSize: '18px', fontWeight: 'bold'},
            style: {color: '#011633', fontFamily: 'Roboto, sans-serif', fontSize: '16px'},
            columnColor: '#a3d869',
            currentColumnColor: '#ee7338',
            futureColumnColor: '#ffc80c',
        },
        lueneburg: {
            titleStyle: {color: '#7a7a7a', fontFamily: 'PT Serif, sans-serif', fontSize: '18px', fontWeight: 'bold'},
            style: {color: '#7a7a7a', fontFamily: 'PT Serif, sans-serif', fontSize: '16px'},
            columnColor: '#012b4a',
            currentColumnColor: '#ee7338',
            futureColumnColor: '#f9c647',
        }
    },
    buildingPieChart: {
        titleStyle: {color: '#333', fontFamily: 'Roboto, sans-serif', fontSize: '18px', fontWeight: 'bold'},
        style: {color: '#333', fontFamily: 'Roboto, sans-serif', fontSize: '16px'}
    },
    monthlySolarInstallationsChart: {
        titleStyle: {color: '#333', fontFamily: 'Roboto, sans-serif', fontSize: '18px', fontWeight: 'bold'},
        style: {color: '#333', fontFamily: 'Roboto, sans-serif', fontSize: '16px'},
        installedMWpColor: '#012b4a',
        installedNumberColor: '#409703'
    }
}


LocalEnergyCharts.getSolarOverview = function (city) {

    $.ajax({
        url: `${baseApiUrl}/v1/solar-cities/${city}/statistics/overview`,
        contentType: 'application/json',
        dataType: 'json',

        success: function (response) {
            $('#used-solar-potential-percent').text(formatNumber(response.usedRoofSolarPotentialPercent, 1) + ' %')
            $('#rooftop-solar-systems-in-operation').text(formatNumber(response.rooftopSolarSystemsInOperation));
            $('#installed-rooftop-mwp-in-operation').text(formatNumber(response.installedRooftopMWpInOperation));
            $('#entire-solar-potential-on-rooftops-mwp').text(formatNumber(response.entireSolarPotentialOnRooftopsMWp));
            $('#updated').text(new Date(response.updated).toLocaleDateString());
        }
    });
}


LocalEnergyCharts.getAnnualAdditionOfSolarInstallationsChart = function (
    city,
    previousSolarInstallationsOnly,
    years = 17
) {

    $.ajax({
        url: `${baseApiUrl}/v1/solar-cities/${city}/statistics/annual-addition-of-solar-installations/highcharts`,
        contentType: 'application/json',
        dataType: 'json',
        data: {previousSolarInstallationsOnly: previousSolarInstallationsOnly, years: years},

        success: function (response) {
            createAnnualAdditionOfSolarInstallationsChart(response);
        }
    })
}

LocalEnergyCharts.getYourCityFormAnnualAdditionOfSolarInstallationsChart = function () {

    let cityName = '';
    let yourCityForm = $('#your-city-form');
    let yourCityRequest = JSON.stringify(convertYourCityFormToJson(yourCityForm))

    function convertYourCityFormToJson(form) {
        let formData = $(form).serializeArray();
        cityName = formData[0].value;
        let eoSolarRoofPotential = formData[1].value;
        let targetYear = formData[2].value;

        return {
            'eoSolarRoofPotential': eoSolarRoofPotential,
            'targetYear': parseInt(targetYear),
            'years': 17
        };
    }

    $.ajax({
        url: `${baseApiUrl}/v1/solar-cities/${cityName}/statistics/annual-addition-of-solar-installations/highcharts/temporary`,
        method: 'POST',
        data: yourCityRequest,
        contentType: 'application/json',
        dataType: 'json',

        success: function (response) {
            createAnnualAdditionOfSolarInstallationsChart(response);
            showSuccessMessage();
            $('#rooftop-solar-systems-in-operation').text(formatNumber(response.rooftopSolarSystemsInOperation));
            $('#installed-rooftop-mwp-in-operation').text(formatNumber(response.installedRooftopMWpInOperation, 1));
            $('html, body').animate({scrollTop: 0}, 'slow');
            $('#your-city-solar-paragraph').show();
        },

        error: function () {
            showErrorMessage();
        }
    });

    function showSuccessMessage() {
        let parent = $(yourCityForm.parent());
        parent.children('.w-form-done').css('display', 'block');
        parent.find('.w-form-fail').css('display', 'none');
    }

    function showErrorMessage() {
        let parent = $(yourCityForm.parent());
        parent.children('.w-form-done').css('display', 'none');
        parent.find('.w-form-fail').css('display', 'block');
    }
}

function createAnnualAdditionOfSolarInstallationsChart(response) {

    let description = translations[language].annualAdditionChart; // default
    let style = highchartsStyle.annualAdditionChart; // default

    if (response.cityName === 'Köln') {
        description = translations.koeln.annualAdditionChart;
        style = highchartsStyle.annualAdditionChart.koeln;
    }
    if (response.cityName === 'Lüneburg') {
        style = highchartsStyle.annualAdditionChart.lueneburg;
    }
    if (response.cityName === 'München') {
        style = highchartsStyle.annualAdditionChart.muenchen;
    }

    Highcharts.chart('solar-city-annual-addition-of-solar-installations', {
        title: {
            text: `${description.title} ${response.cityName}`,
            style: style.titleStyle
        },
        chart: {
            type: 'column',
            style: style.style
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
                style: style.style
            }
        },
        yAxis: {
            min: 0,
            title: {
                text: description.yAxis,
                style: style.style
            },
            labels: {
                style: style.style
            }
        },
        legend: {
            enabled: false
        },
        tooltip: {
            useHTML: true,
            formatter: function () {
                if (detailedInfosAreAvailable(this.point)) {
                    return createTooltipWithDistributionOfRoofs(this.point)
                }
                return createTooltip(this.point);
            }
        },
        series: [{
            name: description.name,
            data: colorize(style, response.columns)
        }]
    });

    function detailedInfosAreAvailable(point) {
        return point.numberOfSolarSystemsUpTo1kWp
            || point.numberOfSolarSystems1To10kWp
            || point.numberOfSolarSystems10To40kWp
            || point.numberOfSolarSystemsFrom40kWp
    }

    function createTooltipWithDistributionOfRoofs(point) {
        let year = point.name;
        let numberOfSolarSystems = formatNumber(point.numberOfSolarSystems);
        let totalInstalledMWp = formatNumber(point.y, 1);

        let numberOfSolarSystemsUpTo1kWp = formatNumber(point.numberOfSolarSystemsUpTo1kWp);
        let numberOfSolarSystems1To10kWp = formatNumber(point.numberOfSolarSystems1To10kWp);
        let numberOfSolarSystems10To40kWp = formatNumber(point.numberOfSolarSystems10To40kWp);
        let numberOfSolarSystemsFrom40kWp = formatNumber(point.numberOfSolarSystemsFrom40kWp);

        return `
        <div><small>${year}</small></div>
        <table style="font-weight: 700">
          <tr>
            <td style="padding-right: 0.5em">${translations[language].pvSystems}</td>
            <td>${numberOfSolarSystems}</td>
          </tr>
          <tr>
            <td style="padding-right: 0.5em">${translations[language].installation}</td>
            <td>${totalInstalledMWp} MWp</td>
          </tr>
        </table>
        </br>
        <table>
          <tr>
            <td style="padding-right: 0.5em; text-align: right"><small>0 - 1 kWp:</small></td>
            <td style="padding-right: 0.3em; text-align: right">${numberOfSolarSystemsUpTo1kWp}</td>
            <td>${translations[language].balkonSolar}</td>
          </tr>
          <tr>
            <td style="padding-right: 0.5em; text-align: right"><small>1 - 10 kWp:</small></td>
            <td style="padding-right: 0.3em; text-align: right">${numberOfSolarSystems1To10kWp}</td>
            <td>${translations[language].homes}</td>
          </tr>
          <tr>
            <td style="padding-right: 0.5em; text-align: right"><small>10 - 40 kWp:</small></td>
            <td style="padding-right: 0.3em; text-align: right">${numberOfSolarSystems10To40kWp}</td>
            <td>${translations[language].apartmentBuildings}</td>
          </tr>
          <tr>
            <td style="padding-right: 0.5em; text-align: right"><small>ab 40 kWp:</small></td>
            <td style="padding-right: 0.3em; text-align: right">${numberOfSolarSystemsFrom40kWp}</td>
            <td>${translations[language].schools}</td>
          </tr>
        </table>`;
    }

    function createTooltip(point) {
        let year = point.name;
        let numberOfSolarSystems = formatNumber(point.numberOfSolarSystems);
        let totalInstalledMWp = formatNumber(point.y, 1);

        return `
        <div><small>${year}</small></div>
        <table style="font-weight: 700">
          <tr>
            <td style="padding-right: 0.5em">${translations[language].roofPvSystems}</td>
            <td>${numberOfSolarSystems}</td>
          </tr>
          <tr>
            <td style="padding-right: 0.5em">${translations[language].installation}</td>
            <td>${totalInstalledMWp} MWp</td>
          </tr>
        </table>`;
    }

    function colorize(style, columns) {
        columns.forEach(column => {

            column.isCurrentYear = function () {
                return this.year === new Date().getFullYear();
            };
            column.isFuture = function () {
                return this.year > new Date().getFullYear();
            }

            if (column.isCurrentYear()) {
                column.color = style.currentColumnColor;
            } else if (column.isFuture()) {
                column.color = style.futureColumnColor;
            } else {
                column.color = style.columnColor;
            }
        });

        return columns;
    }
}


LocalEnergyCharts.getBuildingPieChart = function (city) {

    $.ajax({
        url: `${baseApiUrl}/v1/solar-cities/${city}/statistics/building-pie-chart/highcharts`,
        contentType: 'application/json',
        dataType: 'json',

        success: function (response) {
            createBuildingPieChart(response);
        }
    })
}

function createBuildingPieChart(response) {

    let title = translations[language].buildingPieChart.title;
    let style = highchartsStyle.buildingPieChart;
    const colors = Highcharts.getOptions().colors.map((c, i) =>
        // Start out with a darkened base color (negative brighten), and end
        // up with a much brighter color
        Highcharts.color(Highcharts.getOptions().colors[0])
            .brighten((i - 3) / 7)
            .get()
    );

    Highcharts.chart('solar-city-building-pie-chart', {
        title: {
            text: title,
            align: 'left',
            style: style.titleStyle
        },
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            type: 'pie',
            style: style.style
        },
        accessibility: {
            point: {
                valueSuffix: '%'
            }
        },
        legend: {
            enabled: false
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                colors,
                borderRadius: 5,
                dataLabels: {
                    enabled: true,
                    useHTML: true,
                    formatter: function () {
                        return createDataLabel(this.point);
                    },
                    distance: -50,
                    filter: {
                        property: 'percentage',
                        operator: '>',
                        value: 4
                    }
                }
            }
        },
        tooltip: {
            useHTML: true,
            formatter: function () {
                return createTooltip(this.point);
            }
        },
        series: [{
            name: '',
            data: response.slices
        }]
    });

    function createDataLabel(point) {
        return `
        <p>${translations[language][point.name]}</p>
        <p>${formatNumber(point.y, 2)} %</p>`;
    }

    function createTooltip(point) {
        let rangekWp =
            point.name === 'BALKONKRAFTWERKE' ? '0 - 1' :
                point.name === 'HOMES' ? '1 - 10' :
                    point.name === 'APARTMENT_BUILDINGS_COMMERCIAL_AND_CO' ? '10 - 40' :
                        point.name === 'SCHOOLS_INDUSTRIAL_BUILDINGS_AND_CO' ? '> 40' :
                            '';

        let buildingType = translations[language][point.name];
        let percentage = formatNumber(point.y, 2);
        let count = formatNumber(point.count);
        let installedMWp = formatNumber(point.installedMWp, 1);

        return `
        <div><small>${buildingType}</small></div>
        <div><small>${rangekWp} kWp</small></div>
        <table style="font-weight: 700">
        <tr>
            <td style="padding-right: 0.5em">${percentage} %</td>
            <td></td>
          </tr>
          <tr>
            <td style="padding-right: 0.5em">${translations[language].count}:</td>
            <td>${count}</td>
          </tr>
          <tr>
            <td style="padding-right: 0.5em">${translations[language].installedMWp}:</td>
            <td>${installedMWp} MWp</td>
          </tr>
        </table>`;
    }
}


LocalEnergyCharts.getMonthlySolarInstallationsChart = function (city) {

    $.ajax({
        url: `${baseApiUrl}/v1/solar-cities/${city}/statistics/monthly-solar-installations/highcharts`,
        contentType: 'application/json',
        dataType: 'json',

        success: function (response) {
            createMonthlySolarInstallationChart(response);
        }
    })
}

function createMonthlySolarInstallationChart(response) {
    let trans = translations[language];
    let style = highchartsStyle.monthlySolarInstallationsChart;
    const xAxisCategories = response.columns.map(column => column.name);

    Highcharts.chart('monthly-solar-installations', {
        chart: {
            zoomType: 'xy',
            style: style.style
        },
        title: {
            text: trans.monthlySolarInstallationsChart.title,
            align: 'left',
            style: style.titleStyle
        },
        xAxis: [{
            categories: xAxisCategories,
            crosshair: true
        }],
        yAxis: [{ // Primary yAxis
            labels: {
                format: '{value} ',
                style: {
                    color: style.installedNumberColor
                }
            },
            title: {
                text: trans.installedPvSystems,
                style: {
                    color: style.installedNumberColor
                }
            }
        }, { // Secondary yAxis
            title: {
                text: trans.installation,
                style: {
                    color: style.installedMWpColor
                }
            },
            labels: {
                formatter: function () {
                    return formatNumber(this.value, 1) + ' MWp';
                },
                style: {
                    color: style.installedMWpColor
                }
            },
            opposite: true
        }],
        tooltip: {
            shared: true,
            useHTML: true,
            formatter: function () {
                return createTooltip(this.point);
            }
        },
        legend: {
            align: 'left',
            x: 80,
            verticalAlign: 'top',
            y: 60,
            floating: true,
            backgroundColor:
                Highcharts.defaultOptions.legend.backgroundColor || // theme
                'rgba(255,255,255,0.25)'
        },
        series: [{
            name: trans.installation,
            type: 'column',
            yAxis: 1,
            data: response.columns,
            tooltip: {
                valueSuffix: ' MWp'
            },
            color: style.installedMWpColor
        }, {
            name: trans.installedPvSystems,
            type: 'spline',
            data: response.splines,
            color: style.installedNumberColor
        }]
    });

    function createTooltip(point) {
        let name = `${point.name} ${point.year}`;
        let numberOfSolarSystems = formatNumber(point.numberOfSolarSystems);
        let totalInstalledMWp = formatNumber(point.y, 2);

        let numberOfSolarSystemsUpTo1kWp = formatNumber(point.numberOfSolarSystemsUpTo1kWp);
        let numberOfSolarSystems1To10kWp = formatNumber(point.numberOfSolarSystems1To10kWp);
        let numberOfSolarSystems10To40kWp = formatNumber(point.numberOfSolarSystems10To40kWp);
        let numberOfSolarSystemsFrom40kWp = formatNumber(point.numberOfSolarSystemsFrom40kWp);

        return `
        <div><small>${name}</small></div>
        <table style="font-weight: 700">
          <tr>
            <td style="padding-right: 0.5em; color: ${style.installedNumberColor}">${translations[language].pvSystems}</td>
            <td style="color: ${style.installedNumberColor}">${numberOfSolarSystems}</td>
          </tr>
          <tr>
            <td style="padding-right: 0.5em; color: ${style.installedMWpColor}">${translations[language].installation}</td>
            <td style="color: ${style.installedMWpColor}">${totalInstalledMWp} MWp</td>
          </tr>
        </table>
        </br>
        <table>
          <tr>
            <td style="padding-right: 0.5em; text-align: right"><small>0 - 1 kWp:</small></td>
            <td style="padding-right: 0.3em; text-align: right">${numberOfSolarSystemsUpTo1kWp}</td>
            <td>${translations[language].balkonSolar}</td>
          </tr>
          <tr>
            <td style="padding-right: 0.5em; text-align: right"><small>1 - 10 kWp:</small></td>
            <td style="padding-right: 0.3em; text-align: right">${numberOfSolarSystems1To10kWp}</td>
            <td>${translations[language].homes}</td>
          </tr>
          <tr>
            <td style="padding-right: 0.5em; text-align: right"><small>10 - 40 kWp:</small></td>
            <td style="padding-right: 0.3em; text-align: right">${numberOfSolarSystems10To40kWp}</td>
            <td>${translations[language].apartmentBuildings}</td>
          </tr>
          <tr>
            <td style="padding-right: 0.5em; text-align: right"><small>ab 40 kWp:</small></td>
            <td style="padding-right: 0.3em; text-align: right">${numberOfSolarSystemsFrom40kWp}</td>
            <td>${translations[language].schools}</td>
          </tr>
        </table>`;
    }
}

function formatNumber(value, digits = 0) {

    return new Intl.NumberFormat(language, {
        maximumFractionDigits: digits,
        minimumFractionDigits: digits
    }).format(value);
}


LocalEnergyCharts.sendMail = function (defaultSubject, cc) {

    function convertMailFormToJson() {
        let subject = $('input[name="Subject"]').val();
        let from = $('input[name="From"]').val();
        let to = $('input[name="To"]').val();
        let message = $('textarea[name="Message"]').val();

        return {
            'subject': subject ? subject : defaultSubject || '',
            'from': from,
            'to': to,
            'cc': cc,
            'message': message || ''
        };
    }

    let mailForm = $('#wf-mail-form');
    let mailRequest = JSON.stringify(convertMailFormToJson(mailForm));

    $.ajax({
        url: `${baseApiUrl}/v1/mail/send`,
        method: 'POST',
        data: mailRequest,
        contentType: 'application/json',

        success: function () {
            let parent = $(mailForm.parent());
            parent.children('.w-form-done').css('display', 'block');
            parent.find('.w-form-fail').css('display', 'none');
            mailForm.css('display', 'none');
            $('html, body').animate({scrollTop: 0}, 'slow');
        },

        error: function () {
            let parent = $(mailForm.parent());
            parent.children('.w-form-done').css('display', 'none');
            parent.find('.w-form-fail').css('display', 'block');
        }
    });
}

